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
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.domain.Agency;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;

public class AgencyMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ArrayList<Agency> mListAgency;
    private Marker mMarkerSelected;
    private BitmapDescriptor mIconAgence;
    private BitmapDescriptor mIconAnnexe;
    private HashMap<Marker, Agency> mMapMarkerAgency;
    private AsyncTask<Void, Void, ArrayList<Agency>> taskGetRecipeList;

    @BindView(R.id.txt_agence_nom)
    TextView txtAgenceNom;

    @BindView(R.id.txt_agence_horaire)
    TextView txtAgenceHoraire;

    @BindView(R.id.txt_agence_nb_dab_int)
    TextView txtAgenceNbDabInt;

    @BindView(R.id.txt_agence_nb_dab_ext)
    TextView txtAgenceNbDabExt;

    public AgencyMapFragment() {}

    public static AgencyMapFragment newInstance() {
        AgencyMapFragment fragment = new AgencyMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *
     * @param agency
     */
    private void setAgencyToLayout(Agency agency) {
        // Mise à jour des infos de l'agence sélectionnée
        txtAgenceNom.setText(agency.getNOM());
        txtAgenceHoraire.setText(agency.getHORAIRE());
        txtAgenceNbDabInt.setText(String.valueOf(agency.getDAB_INTERNE()));
        txtAgenceNbDabExt.setText(String.valueOf(agency.getDAB_EXTERNE()));
    }

    /**
     * Create AsyncTask to retrieve Agencies informations
     * @return AsyncTask<Void, Void, ArrayList<Agency>>
     */
    private AsyncTask<Void, Void, ArrayList<Agency>> createTask(){
        return new AsyncTask<Void, Void, ArrayList<Agency>>() {
            @Override
            protected ArrayList<Agency> doInBackground(Void... voids) {
                return ProviderUtilities.getListAgencyFromContentProvider(getActivity());
            }

            @Override
            protected void onPostExecute(ArrayList<Agency> agencies) {
                mListAgency = agencies;

                LatLng nc = new LatLng(-20.904305, 165.618042);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(nc));

                for (Agency agency : mListAgency) {
                    LatLng latLng = new LatLng(agency.getLATITUDE(), agency.getLONGITUDE());

                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(agency.getNOM())
                            .snippet(agency.getHORAIRE())
                            .icon(agency.getTYPE().equals("Agence") ? mIconAgence : mIconAnnexe));

                    mMapMarkerAgency.put(marker, agency);
                }

                mMap.setOnMarkerClickListener(AgencyMapFragment.this);
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskGetRecipeList = createTask();
        mMapMarkerAgency = new HashMap<>();
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Now the map is ready, we retreive the datas from the content provider
        taskGetRecipeList.execute();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Agency agency;
        if (marker != null) {
            if (mMarkerSelected != null && !marker.equals(mMarkerSelected)) {
                agency = mMapMarkerAgency.get(mMarkerSelected);
                mMarkerSelected.setIcon(agency.getTYPE().equals("Agence") ? mIconAgence : mIconAnnexe);
            }

            agency = mMapMarkerAgency.get(marker);
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMarkerSelected = marker;

            setAgencyToLayout(agency);

            return true;
        } else {
            return false;
        }
    }
}
