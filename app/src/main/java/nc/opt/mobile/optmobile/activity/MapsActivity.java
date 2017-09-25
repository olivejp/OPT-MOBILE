package nc.opt.mobile.optmobile.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.domain.Agency;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private ArrayList<Agency> mListAgency;

    @BindView(R.id.txt_agence_nom)
    TextView txt_agence_nom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this);

        // Récupération de la liste des agences
        mListAgency = ProviderUtilities.getListAgencyFromContentProvider(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * Ceco
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng nc = new LatLng(-20.904305, 165.618042);
        mMap.addMarker(new MarkerOptions().position(nc).title("Marker in New Caledonia"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nc));

        for (Agency agency : mListAgency) {
            LatLng latLng = new LatLng(agency.getLATITUDE(), agency.getLONGITUDE());
            mMap.addMarker(new MarkerOptions().position(latLng).title(agency.getNOM()).snippet(agency.getHORAIRE()));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
