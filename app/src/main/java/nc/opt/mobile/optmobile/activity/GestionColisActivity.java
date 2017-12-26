package nc.opt.mobile.optmobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.broadcast.NetworkReceiver;
import nc.opt.mobile.optmobile.fragment.GestionColisFragment;
import nc.opt.mobile.optmobile.fragment.HistoriqueColisFragment;
import nc.opt.mobile.optmobile.job.task.ParamSyncTask;
import nc.opt.mobile.optmobile.job.task.SyncTask;
import nc.opt.mobile.optmobile.service.SyncColisService;

public class GestionColisActivity extends AppCompatActivity implements NetworkReceiver.NetworkChangeListener, HistoriqueColisFragment.ListenToSelectedColis {

    public static final String TAG_PARCEL_RESULT_SEARCH_FRAGMENT = "TAG_PARCEL_RESULT_SEARCH_FRAGMENT";

    private String mIdColisSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colis_gestion);

        ButterKnife.bind(this);

        boolean mTwoPane = findViewById(R.id.frame_detail) != null;

        GestionColisFragment gestionColisFragment = GestionColisFragment.newInstance(mTwoPane);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_master, gestionColisFragment).commit();

        // On écoute les changements réseau
        NetworkReceiver.getInstance().listen(this);

        setTitle(getString(R.string.suivi_des_colis));
    }

    @OnClick(R.id.fab_add_parcel)
    public void onFloatingButtonClick(View view) {
        Intent intent = new Intent(this, AddColisActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_suivi_colis, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem refresh = menu.findItem(R.id.nav_refresh);
        if (refresh != null) {
            if (NetworkReceiver.checkConnection(this)) {
                refresh.setVisible(true);
            } else {
                refresh.setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        } else if (i == R.id.nav_refresh && NetworkReceiver.checkConnection(this)) {
            if (mIdColisSelected != null) {
                ParamSyncTask paramSyncTask = new ParamSyncTask(this, mIdColisSelected);
                SyncTask syncTask = new SyncTask(SyncTask.TypeTask.SOLO);
                syncTask.execute(paramSyncTask);
            } else {
                ParamSyncTask paramSyncTask = new ParamSyncTask(this, null);
                SyncTask syncTask = new SyncTask(SyncTask.TypeTask.MULTIPLE);
                syncTask.execute(paramSyncTask);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNetworkEnable() {
        invalidateOptionsMenu();
        SyncColisService.launchSynchroForAll(this, false);
    }

    @Override
    public void onNetworkDisable() {
        invalidateOptionsMenu();
    }

    @Override
    public void subscribe(String idColis) {
        mIdColisSelected = idColis;
        setTitle(idColis);
    }

    @Override
    public void unsubscribe() {
        mIdColisSelected = null;
        setTitle(getString(R.string.suivi_des_colis));
    }
}
