package nc.opt.mobile.optmobile.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.activity.viewmodel.GestionColisActivityViewModel;
import nc.opt.mobile.optmobile.adapter.EtapeAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoriqueColisFragment extends Fragment {

    private AppCompatActivity mAppCompatActivity;
    private EtapeAdapter mEtapeAdapter;
    private GestionColisActivityViewModel viewModel;

    @BindView(R.id.recycler_colis_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.text_object_not_found)
    TextView textObjectNotFound;

    public HistoriqueColisFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppCompatActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(GestionColisActivityViewModel.class);
        mEtapeAdapter = new EtapeAdapter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_historique_colis, container, false);
        ButterKnife.bind(this, rootView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mAppCompatActivity);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mEtapeAdapter);

        viewModel.getSelectedColis().observe(this, colisEntity -> {
            mEtapeAdapter.setEtapes(colisEntity != null ? colisEntity.getEtapeAcheminementArrayList() : null);
            mEtapeAdapter.notifyDataSetChanged();
        });

        viewModel.getVisibility().observe(this, atomicBoolean -> {
            if (atomicBoolean != null) {
                textObjectNotFound.setVisibility(atomicBoolean.get() ? View.VISIBLE : View.GONE);
                mRecyclerView.setVisibility(atomicBoolean.get() ? View.GONE : View.VISIBLE);
            }
        });

        return rootView;
    }
}
