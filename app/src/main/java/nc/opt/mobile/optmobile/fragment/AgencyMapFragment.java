package nc.opt.mobile.optmobile.fragment;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.domain.Agency;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;

public class AgencyMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = AgencyMapFragment.class.getName();

    private static final String ARG_AGENCY_SELECTED = "AGENCY_SELECTED";
    private static final String ARG_LIST_AGENCIES = "ARG_LIST_AGENCIES";
    private static final String REQUESTING_LOCATION_UPDATES_KEY = "REQUESTING_LOCATION_UPDATES_KEY";

    private static final int RC_LOCATION_FINE = 100;
    private static final int REQUEST_CHECK_SETTINGS = 200;

    private GoogleMap mMap;
    private Marker mMarkerSelected;
    private BitmapDescriptor mIconAgence;
    private BitmapDescriptor mIconAnnexe;
    private Agency mAgencySelected;
    private ArrayList<Agency> mList;
    private boolean launchTask;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates;

    @BindView(R.id.txt_agence_nom)
    TextView txtAgenceNom;

    @BindView(R.id.txt_agence_horaire)
    TextView txtAgenceHoraire;

    @BindView(R.id.txt_agence_nb_dab_int)
    TextView txtAgenceNbDabInt;

    @BindView(R.id.txt_agence_nb_dab_ext)
    TextView txtAgenceNbDabExt;

    private FusedLocationProviderClient mFusedLocationClient;

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

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
            }
        }
    };

    public AgencyMapFragment() {
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
        txtAgenceHoraire.setText(agency.getHORAIRE());
        txtAgenceNbDabInt.setText(String.valueOf(agency.getDAB_INTERNE()));
        txtAgenceNbDabExt.setText(String.valueOf(agency.getDAB_EXTERNE()));
    }

    private void populateMap(ArrayList<Agency> agencyList) {
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
    private AsyncTask<Void, Void, ArrayList<Agency>> createTask() {
        return new AsyncTask<Void, Void, ArrayList<Agency>>() {
            @Override
            protected ArrayList<Agency> doInBackground(Void... voids) {
                // Get the list of agencies from content provider
                return ProviderUtilities.getListAgencyFromContentProvider(getActivity());
            }

            @Override
            protected void onPostExecute(ArrayList<Agency> list) {
                super.onPostExecute(list);

                // Set the map in New Caledonia
                LatLng nc = new LatLng(-20.904305, 165.618042);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(nc));

                // Populate GoogleMap with the agencies list
                mList = list;
                populateMap(mList);
            }
        };
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null);
        }
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mAgencySelected = savedInstanceState.getParcelable(ARG_AGENCY_SELECTED);
            mList = savedInstanceState.getParcelableArrayList(ARG_LIST_AGENCIES);
            launchTask = false;
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
            }
        } else {
            launchTask = true;
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_agency_map, container, false);

        ButterKnife.bind(this, rootView);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        mIconAgence = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        mIconAnnexe = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);

        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ARG_LIST_AGENCIES, mList);
        outState.putParcelable(ARG_AGENCY_SELECTED, mAgencySelected);
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_LOCATION_FINE) {
            enableLocation();
        }
    }

    private void enableLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);

            SettingsClient client = LocationServices.getSettingsClient(getActivity());
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

            task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    startLocationUpdates();
                }
            });

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
                    }
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        changeMapStyle(mMap, R.raw.google_map_style);

        mMap.setOnMarkerClickListener(AgencyMapFragment.this);

        // We ask permission to get local position of the mobile
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, RC_LOCATION_FINE);
        } else {
            enableLocation();
        }

        // Now the map is ready, we retreive the datas from the content provider
        if (launchTask) {
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

            return true;
        } else {
            return false;
        }
    }

}
