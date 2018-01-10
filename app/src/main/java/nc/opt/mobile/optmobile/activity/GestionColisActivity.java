package nc.opt.mobile.optmobile.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.activity.viewmodel.GestionColisActivityViewModel;
import nc.opt.mobile.optmobile.broadcast.NetworkReceiver;
import nc.opt.mobile.optmobile.fragment.GestionColisFragment;
import nc.opt.mobile.optmobile.job.task.SyncTask;
import nc.opt.mobile.optmobile.database.entity.ColisEntity;
import nc.opt.mobile.optmobile.database.services.ColisService;
import nc.opt.mobile.optmobile.utils.CoreSync;
import nc.opt.mobile.optmobile.utils.NoticeDialogFragment;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class GestionColisActivity extends AppCompatActivity implements NetworkReceiver.NetworkChangeListener, NoticeDialogFragment.NoticeDialogListener {

    public static final String TAG_PARCEL_RESULT_SEARCH_FRAGMENT = "TAG_PARCEL_RESULT_SEARCH_FRAGMENT";

    public static final String GESTION_FRAGMENT = "GESTION_FRAGMENT";
    public static final String ARG_NOTICE_BUNDLE_COLIS = "ARG_NOTICE_BUNDLE_COLIS";
    public static final String ARG_NOTICE_BUNDLE_POSITION = "ARG_NOTICE_BUNDLE_POSITION";
    private ColisEntity mColisSelected;
    private GestionColisFragment gestionColisFragment;
    private GestionColisActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(GestionColisActivityViewModel.class);
        viewModel.getSelectedColis().observe(this, colisEntity -> {
            mColisSelected = colisEntity;
            if (colisEntity != null) {
                setTitle(colisEntity.getIdColis());
            }
        });

        setContentView(R.layout.activity_colis_gestion);

        ButterKnife.bind(this);

        boolean mTwoPane = findViewById(R.id.frame_detail) != null;

        if (savedInstanceState != null) {
            gestionColisFragment = (GestionColisFragment) getSupportFragmentManager().getFragment(savedInstanceState, GESTION_FRAGMENT);
            gestionColisFragment.setTwoPane(mTwoPane);
        } else {
            gestionColisFragment = GestionColisFragment.newInstance(mTwoPane);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_master, gestionColisFragment).commit();

        setTitle(getString(R.string.suivi_des_colis));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // On écoute les changements réseau
        NetworkReceiver.getInstance().listen(this);
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
            SyncTask syncTask;
            if (mColisSelected != null) {
                syncTask = new SyncTask(SyncTask.TypeTask.SOLO, this, mColisSelected.getIdColis());
            } else {
                syncTask = new SyncTask(SyncTask.TypeTask.ALL, this);
            }
            syncTask.execute();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, GESTION_FRAGMENT, gestionColisFragment);
    }

    @Override
    public void onNetworkEnable() {
        invalidateOptionsMenu();
        new SyncTask(SyncTask.TypeTask.ALL, this).execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.releaseProviderObserver();
        NetworkReceiver.getInstance().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.releaseProviderObserver();
        NetworkReceiver.getInstance().unregister(this);
    }

    @Override
    public void onNetworkDisable() {
        invalidateOptionsMenu();
    }

    @Override
    public void onDialogPositiveClick(NoticeDialogFragment dialog) {
        // Récupération du bundle qu'on a envoyé au NoticeDialogFragment
        if (dialog.getBundle() != null && dialog.getBundle().containsKey(ARG_NOTICE_BUNDLE_COLIS)) {

            // Récupération du colis présent dans le bundle
            ColisEntity colisEntity = dialog.getBundle().getParcelable(ARG_NOTICE_BUNDLE_COLIS);
            if (colisEntity != null) {

                // Suppression du colis dans la base de données
                ColisService.delete(this, colisEntity.getIdColis());

                // Si on a une connexion, on supprime le colis sur le réseau.
                if (NetworkReceiver.checkConnection(this)) {
                    CoreSync.deleteTracking(this, colisEntity);
                }
            }
        }
    }

    @Override
    public void onDialogNegativeClick(NoticeDialogFragment dialog) {
        // Do Nothing
    }
}
