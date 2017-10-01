package nc.opt.mobile.optmobile.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.domain.Agency;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;

public class AgencyMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String ARG_AGENCY_SELECTED = "AGENCY_SELECTED";
    private static final String ARG_LIST_AGENCIES = "ARG_LIST_AGENCIES";

    private GoogleMap mMap;
    private Marker mMarkerSelected;
    private BitmapDescriptor mIconAgence;
    private BitmapDescriptor mIconAnnexe;
    private Agency mAgencySelected;
    private ArrayList<Agency> mList;
    private boolean launchTask;

    @BindView(R.id.txt_agence_nom)
    TextView txtAgenceNom;

    @BindView(R.id.txt_agence_horaire)
    TextView txtAgenceHoraire;

    @BindView(R.id.txt_agence_nb_dab_int)
    TextView txtAgenceNbDabInt;

    @BindView(R.id.txt_agence_nb_dab_ext)
    TextView txtAgenceNbDabExt;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mAgencySelected = savedInstanceState.getParcelable(ARG_AGENCY_SELECTED);
            mList = savedInstanceState.getParcelableArrayList(ARG_LIST_AGENCIES);
            launchTask = false;
        } else {
            launchTask = true;
        }
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
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARG_LIST_AGENCIES, mList);
        outState.putParcelable(ARG_AGENCY_SELECTED, mAgencySelected);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(AgencyMapFragment.this);

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
