package nc.opt.mobile.optmobile.activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.fragment.AgencyMapFragment;
import nc.opt.mobile.optmobile.interfaces.AttachToPermissionActivity;

public class AgenceActivity extends AttachToPermissionActivity {

    private static final String SAVED_MAP_FRAGMENT = "SAVED_MAP_FRAGMENT";
    private AgencyMapFragment mMapFragment;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agence);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_MAP_FRAGMENT)) {
                mMapFragment = (AgencyMapFragment) getSupportFragmentManager().getFragment(savedInstanceState, SAVED_MAP_FRAGMENT);
            }
        }

        if (mMapFragment == null){
            mMapFragment = AgencyMapFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_master_agence, mMapFragment).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapFragment != null) {
            getSupportFragmentManager().putFragment(outState, SAVED_MAP_FRAGMENT, mMapFragment);
        }
    }
}
