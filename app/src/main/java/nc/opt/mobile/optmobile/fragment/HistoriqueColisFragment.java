package nc.opt.mobile.optmobile.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.adapter.EtapeAcheminementAdapter;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderObserver;
import nc.opt.mobile.optmobile.provider.entity.EtapeAcheminementEntity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static nc.opt.mobile.optmobile.provider.services.EtapeAcheminementService.listFromProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoriqueColisFragment extends Fragment implements ProviderObserver.ProviderObserverListener {

    private static final String ARG_ID_PARCEL = "ARG_ID_PARCEL";

    private AppCompatActivity mAppCompatActivity;
    private String mIdColis;
    private List<EtapeAcheminementEntity> mListEtape;
    private EtapeAcheminementAdapter mEtapeAcheminementAdapter;

    @BindView(R.id.recycler_parcel_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.text_object_not_found)
    TextView textObjectNotFound;

    public static HistoriqueColisFragment newInstance(@NotNull String idColis) {
        HistoriqueColisFragment fragment = new HistoriqueColisFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID_PARCEL, idColis);
        fragment.setArguments(args);
        return fragment;
    }

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
        mIdColis = getArguments().getString(ARG_ID_PARCEL);

        // create new adapter from the provider mListEtape
        mEtapeAcheminementAdapter = new EtapeAcheminementAdapter();

        // create a ProviderObserver to listen updates from the provider
        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.observe(mAppCompatActivity, this, OptProvider.ListColis.LIST_COLIS, OptProvider.ListEtapeAcheminement.LIST_ETAPE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_historique_colis, container, false);
        ButterKnife.bind(this, rootView);

        // Changement du titre
        mAppCompatActivity.setTitle(mIdColis);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mAppCompatActivity);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // Ajout d'une barre separatrice entre les elements
        mRecyclerView.addItemDecoration(new
                DividerItemDecoration(mAppCompatActivity,
                DividerItemDecoration.VERTICAL));

        // get history from the provider
        mListEtape = listFromProvider(mAppCompatActivity, mIdColis);
        mEtapeAcheminementAdapter.setmEtapeAcheminements(mListEtape);
        mRecyclerView.setAdapter(mEtapeAcheminementAdapter);
        mEtapeAcheminementAdapter.notifyDataSetChanged();

        // Change the visibility of the textView
        textObjectNotFound.setVisibility(mListEtape.isEmpty() ? VISIBLE : GONE);
        mRecyclerView.setVisibility(mListEtape.isEmpty() ? View.GONE : VISIBLE);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_ID_PARCEL, mIdColis);
    }

    @Override
    public void onProviderChange() {
        mListEtape = listFromProvider(mAppCompatActivity, mIdColis);
        mEtapeAcheminementAdapter.setmEtapeAcheminements(mListEtape);
        mEtapeAcheminementAdapter.notifyDataSetChanged();
    }
}
