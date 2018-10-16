package nc.opt.mobile.optmobile.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.database.local.entity.StepEntity;
import nc.opt.mobile.optmobile.ui.InputDialogFragment;
import nc.opt.mobile.optmobile.ui.activity.viewmodel.DetailActivityViewModel;
import nc.opt.mobile.optmobile.ui.adapter.EtapeAdapter;

public class DetailActivity extends AppCompatActivity implements InputDialogFragment.InputDialogFragmentListener {

    private static final String TAG = DetailActivity.class.getCanonicalName();
    private static final String FRAGMENT_TAG = "UPDATE_FRAGMENT";
    public static final String ID_COLIS_EXTRA = "ID_COLIS_EXTRA";

    @BindView(R.id.recycler_etape_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.text_object_not_found)
    TextView textObjectNotFound;

    private InputDialogFragment dialogErreur;
    private DetailActivityViewModel viewModel;
    private EtapeAdapter etapeAdapter;
    private String idColis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = (getIntent().getExtras() != null) ? getIntent().getExtras() : savedInstanceState;
        if (bundle != null && bundle.containsKey(ID_COLIS_EXTRA)) {
            idColis = bundle.getString(ID_COLIS_EXTRA);
        }

        if (idColis == null || idColis.isEmpty()) {
            Log.e(TAG, "Impossible de continuer sans Id Colis");
            finish();
        }

        // Create view and bind widgets
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        etapeAdapter = new EtapeAdapter();
        mRecyclerView.setAdapter(etapeAdapter);

        viewModel = ViewModelProviders.of(this).get(DetailActivityViewModel.class);
        viewModel.getListStepFromOpt(idColis).observe(this, this::initViews);

        // Change title
        setTitle(idColis);

        // If we had a tag, we want to retrieve this fragment back with its listener
        if (getSupportFragmentManager() != null) {
            dialogErreur = (InputDialogFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
            if (dialogErreur != null) {
                dialogErreur.setmListenerContext(this);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ID_COLIS_EXTRA, idColis);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            onBackPressed();
            return true;
        }
        if (R.id.menu_reinit == item.getItemId()) {
            viewModel.deleteAllSteps(idColis);
            viewModel.refresh();
            return true;
        }
        if (R.id.menu_update == item.getItemId()) {
            if (getSupportFragmentManager() != null) {
                viewModel.findColisById(idColis)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess(colisEntity -> {
                            dialogErreur = new InputDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString(InputDialogFragment.P_TEXT, colisEntity.getDescription());
                            dialogErreur.setmListenerContext(this);
                            dialogErreur.setArguments(bundle);
                            dialogErreur.show(getSupportFragmentManager(), FRAGMENT_TAG);
                        })
                        .doOnError(throwable -> Log.e(TAG, throwable.getMessage()))
                        .subscribe();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void initViews(List<StepEntity> stepEntities) {
        etapeAdapter.setEtapes(stepEntities);
        boolean isEtapeListEmpty = stepEntities == null || stepEntities.isEmpty();
        textObjectNotFound.setVisibility(isEtapeListEmpty ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(isEtapeListEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onInputDialogFinishClick(InputDialogFragment fragment, String description) {
        viewModel.updateDescription(idColis, description);
        fragment.dismiss();
    }
}
