package nc.opt.mobile.optmobile.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.domain.Agency;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ArrayList<Agency> mListAgency;
    private Marker mMarkerSelected;
    private BitmapDescriptor mIconAgence;
    private BitmapDescriptor mIconAnnexe;
    private HashMap<Marker, Agency> mMapMarkerAgency;
    private AsyncTask<Void, Void, ArrayList<Agency>> taskGetRecipeList;

    @BindView(R.id.txt_agence_nom)
    TextView txt_agence_nom;

    @BindView(R.id.txt_agence_horaire)
    TextView txt_agence_horaire;

    @BindView(R.id.txt_agence_nb_dab_int)
    TextView txt_agence_nb_dab_int;

    @BindView(R.id.txt_agence_nb_dab_ext)
    TextView txt_agence_nb_dab_ext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this);

        mMapMarkerAgency = new HashMap<>();

        // Récupération de la liste des agences
        taskGetRecipeList = new AsyncTask<Void, Void, ArrayList<Agency>>() {
            @Override
            protected ArrayList<Agency> doInBackground(Void... voids) {
                return ProviderUtilities.getListAgencyFromContentProvider(MapsActivity.this);
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

                mMap.setOnMarkerClickListener(MapsActivity.this);
            }
        };

        mIconAgence = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        mIconAnnexe = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * Ceco
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Now the map is ready, we retreive the datas from the content provider
        taskGetRecipeList.execute();
    }

    private void setAgencyToLayout(Agency agency){
        // Mise à jour des infos de l'agence sélectionnée
        txt_agence_nom.setText(agency.getNOM());
        txt_agence_horaire.setText(agency.getHORAIRE());
        txt_agence_nb_dab_int.setText(String.valueOf(agency.getDAB_INTERNE()));
        txt_agence_nb_dab_ext.setText(String.valueOf(agency.getDAB_EXTERNE()));
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
