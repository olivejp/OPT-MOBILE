package nc.opt.mobile.optmobile.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.domain.Agency;
import nc.opt.mobile.optmobile.interfaces.AttachToPermissionActivity;
import nc.opt.mobile.optmobile.interfaces.ListenerPermissionResult;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;

import static nc.opt.mobile.optmobile.activity.MainActivity.RC_PERMISSION_CALL_PHONE;
import static nc.opt.mobile.optmobile.activity.MainActivity.RC_PERMISSION_LOCATION;

public class AgencyMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, ListenerPermissionResult {

    private static final String TAG = AgencyMapFragment.class.getName();

    private static final String ARG_AGENCY_SELECTED = "AGENCY_SELECTED";
    private static final String ARG_LIST_AGENCIES = "ARG_LIST_AGENCIES";
    private static final String REQUESTING_LOCATION_UPDATES_KEY = "REQUESTING_LOCATION_UPDATES_KEY";

    private static final String MAP_KEY = "MAP_KEY";

    private static final int RC_SEND_AGENCY_CALL = 300;
    private static final int REQUEST_CHECK_SETTINGS = 400;
    private static final float S_ZOOM = 6.6f;

    private GoogleMap mMap;
    private Marker mMarkerSelected;
    private BitmapDescriptor mIconAgence;
    private BitmapDescriptor mIconAnnexe;
    private Agency mAgencySelected;
    private List<Agency> mList;
    private AppCompatActivity mActivity;
    private boolean mRequestingLocationUpdates;
    private AttachToPermissionActivity mPermissionActivity;
    private SupportMapFragment mapFragment;

    @BindView(R.id.txt_agence_nom)
    TextView txtAgenceNom;

    @BindView(R.id.txt_agence_horaire)
    TextView txtAgenceHoraire;

    @BindView(R.id.txt_agence_nb_dab_int)
    TextView txtAgenceNbDabInt;

    @BindView(R.id.txt_agence_nb_dab_ext)
    TextView txtAgenceNbDabExt;

    @BindView(R.id.fab_call_agency)
    FloatingActionButton fabCallAgency;

    @BindView(R.id.txt_type)
    TextView txtAgenceType;

    @BindView(R.id.linear_agence_detail)
    LinearLayout linearLayoutAgenceDetail;

    /**
     * Customise the styling of the base map using a JSON object defined
     * in a raw resource file.
     */
    private boolean changeMapStyle(GoogleMap map, @RawRes int idResource) {
        boolean success = false;
        try {
            success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), idResource));
            if (!success) {
                Log.e(TAG, getString(R.string.error_map_style_parsing));
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, getString(R.string.error_map_style_not_found), e);
        }
        return success;
    }

    private void centerMap(double latitude, double longitude, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,
                longitude)));

        if (zoom != -1) {
            CameraUpdate uptadeZoom = CameraUpdateFactory.zoomTo(zoom);
            mMap.animateCamera(uptadeZoom);
        }
    }

    public static AgencyMapFragment newInstance() {
        AgencyMapFragment fragment = new AgencyMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @param agency
     */
    private void setAgencyToLayout(Agency agency) {
        // Mise à jour des infos de l'agence sélectionnée
        txtAgenceNom.setText(agency.getNOM());
        txtAgenceType.setText(agency.getTYPE());
        txtAgenceHoraire.setText(agency.getHORAIRE());
        txtAgenceNbDabInt.setText(String.valueOf(agency.getDAB_INTERNE()));
        txtAgenceNbDabExt.setText(String.valueOf(agency.getDAB_EXTERNE()));
    }

    private void populateMap(List<Agency> agencyList) {
        for (Agency agency : agencyList) {
            LatLng latLng = new LatLng(agency.getLATITUDE(), agency.getLONGITUDE());

            BitmapDescriptor bitmapDescriptor;
            if (agency.equals(mAgencySelected)) {
                bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            } else {
                bitmapDescriptor = agency.getTYPE().equals("Agence") ? mIconAgence : mIconAnnexe;
            }

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(agency.getNOM())
                    .snippet(agency.getHORAIRE())
                    .icon(bitmapDescriptor));

            marker.setTag(agency);
        }

        if (mAgencySelected != null) {
            setAgencyToLayout(mAgencySelected);
        }
    }

    /**
     * Create AsyncTask to retrieve Agencies informations
     *
     * @return AsyncTask<Void, Void, ArrayList<Agency>>
     */
    private AsyncTask<Void, Void, List<Agency>> createTask() {
        return new AsyncTask<Void, Void, List<Agency>>() {
            @Override
            protected List<Agency> doInBackground(Void... voids) {
                // Get the list of agencies from content provider
                return ProviderUtilities.getListAgencyFromContentProvider(getActivity());
            }

            @Override
            protected void onPostExecute(List<Agency> list) {
                super.onPostExecute(list);

                // Set the map in New Caledonia
                centerMap(-20.904305, 165.618042, S_ZOOM);

                // Populate GoogleMap with the agencies list
                mList = list;
                populateMap(mList);
            }
        };
    }

    private void enableLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            // Add settings to the location request
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            // Build the Location Setting request with the previous LocationRequest
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);

            SettingsClient client = LocationServices.getSettingsClient(getActivity());
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

            task.addOnFailureListener(getActivity(), new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case CommonStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(getActivity(),
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendEx) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    private void callSelectedAgency() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:" + mAgencySelected.getTEL().replaceAll("\\s+", "")));
        startActivityForResult(intent, RC_SEND_AGENCY_CALL);
    }

    private void changeVisibility() {
        if (mAgencySelected != null) {
            linearLayoutAgenceDetail.setVisibility(View.VISIBLE);
        } else {
            linearLayoutAgenceDetail.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.fab_call_agency)
    public void clickCallAgency(View v) {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CALL_PHONE}, RC_PERMISSION_CALL_PHONE);
        } else {
            callSelectedAgency();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
        if (context instanceof AttachToPermissionActivity) {
            mPermissionActivity = (AttachToPermissionActivity) context;
            mPermissionActivity.onAttachPermissionActivity(this);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPermissionActivity.onDetachToPermissionActivity(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mAgencySelected = savedInstanceState.getParcelable(ARG_AGENCY_SELECTED);
            mList = savedInstanceState.getParcelableArrayList(ARG_LIST_AGENCIES);
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
            }
        }

        // Changement du titre
        mActivity.setTitle(mActivity.getString(R.string.agences_opt));
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_agency_map, container, false);

        ButterKnife.bind(this, rootView);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (savedInstanceState != null && savedInstanceState.containsKey(MAP_KEY)) {
            mapFragment = (SupportMapFragment) getChildFragmentManager().getFragment(savedInstanceState, MAP_KEY);
        } else {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        }
        mapFragment.getMapAsync(this);


        mIconAgence = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        mIconAnnexe = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);

        changeVisibility();

        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ARG_LIST_AGENCIES, (ArrayList<? extends Parcelable>) mList);
        outState.putParcelable(ARG_AGENCY_SELECTED, mAgencySelected);
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        getChildFragmentManager().putFragment(outState, MAP_KEY, mapFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        changeMapStyle(mMap, R.raw.google_map_style_retro);

        // Activation des boutons de zoom
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        mMap.setOnMarkerClickListener(AgencyMapFragment.this);

        // We ask permission to get local position of the mobile
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Call activity to request permissions
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, RC_PERMISSION_LOCATION);
        } else {
            // We already get the permissions, we enable the location
            enableLocation();
        }

        // Now the map is ready, we retrieve the data from the content provider
        if (mList == null) {
            createTask().execute();
        } else {
            populateMap(mList);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker != null) {
            if (mMarkerSelected != null && !marker.equals(mMarkerSelected)) {
                mMarkerSelected.setIcon(mAgencySelected.getTYPE().equals("Agence") ? mIconAgence : mIconAnnexe);
            }

            mMarkerSelected = marker;
            mAgencySelected = (Agency) mMarkerSelected.getTag();
            mMarkerSelected.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            setAgencyToLayout(mAgencySelected);

            changeVisibility();

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPermissionRequestResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enableLocation();
            }
        } else if (requestCode == RC_PERMISSION_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callSelectedAgency();
            }
        }
    }
}
