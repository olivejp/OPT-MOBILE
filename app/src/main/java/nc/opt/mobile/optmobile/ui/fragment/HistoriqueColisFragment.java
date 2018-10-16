package nc.opt.mobile.optmobile.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.database.local.entity.StepEntity;
import nc.opt.mobile.optmobile.ui.InputDialogFragment;
import nc.opt.mobile.optmobile.ui.activity.viewmodel.MainActivityViewModel;
import nc.opt.mobile.optmobile.ui.adapter.EtapeAdapter;


public class HistoriqueColisFragment extends Fragment implements InputDialogFragment.InputDialogFragmentListener {

    private static final String TAG = HistoriqueColisFragment.class.getCanonicalName();
    private static final String FRAGMENT_TAG = "UPDATE_FRAGMENT";

    @BindView(R.id.recycler_etape_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.text_object_not_found)
    TextView textObjectNotFound;

    @BindView(R.id.step_line)
    View stepView;

    private InputDialogFragment dialogErreur;
    private MainActivityViewModel viewModel;
    private AppCompatActivity appCompatActivity;
    private EtapeAdapter etapeAdapter;


    public HistoriqueColisFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        appCompatActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(appCompatActivity).get(MainActivityViewModel.class);
        etapeAdapter = new EtapeAdapter();

        viewModel.getListStepFromOpt(viewModel.getSelectedIdColis()).observe(this, this::initViews);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_historique_colis, container, false);
        ButterKnife.bind(this, rootView);

        mRecyclerView.setAdapter(etapeAdapter);

        // Change title
        appCompatActivity.setTitle(viewModel.getSelectedIdColis());

        // Manage the back button in the navigation bar
        if (!viewModel.isTwoPane() && appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setHomeButtonEnabled(true);
            appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel.getListStepFromOpt(viewModel.getSelectedIdColis()).observe(this, this::initViews);

        // If we had a tag, we want to retrieve this fragment back with its listener
        if (getFragmentManager() != null) {
            dialogErreur = (InputDialogFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
            if (dialogErreur != null) {
                dialogErreur.setmListenerContext(this);
            }
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_activity_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.menu_reinit == item.getItemId()) {
            viewModel.deleteAllSteps();
            viewModel.refresh();
            return true;
        }
        if (R.id.menu_update == item.getItemId()) {
            if (getFragmentManager() != null) {
                viewModel.findColisById(viewModel.getSelectedIdColis())
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess(colisEntity -> {
                            dialogErreur = new InputDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString(InputDialogFragment.P_TEXT, colisEntity.getDescription());
                            dialogErreur.setmListenerContext(this);
                            dialogErreur.setArguments(bundle);
                            dialogErreur.show(getFragmentManager(), FRAGMENT_TAG);
                        })
                        .doOnError(throwable -> Log.e(TAG, throwable.getMessage()))
                        .subscribe();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(List<StepEntity> stepEntities) {
        etapeAdapter.setEtapes(stepEntities);
        boolean isEtapeListEmpty = stepEntities == null || stepEntities.isEmpty();
        textObjectNotFound.setVisibility(isEtapeListEmpty ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(isEtapeListEmpty ? View.GONE : View.VISIBLE);
        stepView.setVisibility(isEtapeListEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onInputDialogFinishClick(InputDialogFragment fragment, String description) {
        viewModel.updateDescription(description);
        fragment.dismiss();
    }
}
