package nc.opt.mobile.optmobile.activity;

import android.os.Bundle;

import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.fragment.AgencyMapFragment;
import nc.opt.mobile.optmobile.interfaces.AttachToPermissionActivity;

public class AgenceActivity extends AttachToPermissionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agence);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_master_agence, AgencyMapFragment.newInstance()).commit();
    }
}
