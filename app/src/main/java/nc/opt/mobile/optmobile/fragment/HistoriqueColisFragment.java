package nc.opt.mobile.optmobile.fragment;


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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.adapter.EtapeAcheminementAdapter;
import nc.opt.mobile.optmobile.domain.suiviColis.EtapeConsolidated;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderObserver;
import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;

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
    private List<EtapeEntity> mListEtapeEntity;
    private List<EtapeConsolidated> mListEtapeConsolidated;
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

    private List<EtapeConsolidated> updateHeadersList(List<EtapeEntity> listEntity) {
        List<EtapeConsolidated> listConsolidated = new ArrayList<>();
        if (!listEntity.isEmpty()) {
            EtapeEntity previousEtape;
            EtapeEntity actualEtape;
            String previousHeader;
            String actualHeader;

            // Lecture de toute la liste d'entity
            for (int i = 0; i == listEntity.size() - 1; i++) {
                actualEtape = listEntity.get(i);
                if (i == 0) {
                    listConsolidated.set(i, new EtapeConsolidated(EtapeConsolidated.TypeEtape.HEADER, actualEtape));
                } else {
                    previousEtape = listEntity.get(i - 1);
                    actualHeader = actualEtape.getPays().concat(" ").concat(actualEtape.getLocalisation());
                    previousHeader = previousEtape.getPays().concat(" ").concat(previousEtape.getLocalisation());
                    if (actualHeader.equals(previousHeader)) {
                        listConsolidated.set(i, new EtapeConsolidated(EtapeConsolidated.TypeEtape.DETAIL, actualEtape));
                    } else {
                        listConsolidated.set(i, new EtapeConsolidated(EtapeConsolidated.TypeEtape.HEADER, actualEtape));
                    }
                }
            }
        }
        return listConsolidated;
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

        // get history from the provider
        mListEtapeEntity = listFromProvider(mAppCompatActivity, mIdColis);
        mListEtapeConsolidated = updateHeadersList(mListEtapeEntity);
        mEtapeAcheminementAdapter.setmEtapeConsolidated(mListEtapeConsolidated);
        mRecyclerView.setAdapter(mEtapeAcheminementAdapter);
        mEtapeAcheminementAdapter.notifyDataSetChanged();

        // Change the visibility of the textView
        textObjectNotFound.setVisibility(mListEtapeEntity.isEmpty() ? VISIBLE : GONE);
        mRecyclerView.setVisibility(mListEtapeEntity.isEmpty() ? View.GONE : VISIBLE);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_ID_PARCEL, mIdColis);
    }

    @Override
    public void onProviderChange() {
        mListEtapeEntity = listFromProvider(mAppCompatActivity, mIdColis);
        mListEtapeConsolidated = updateHeadersList(mListEtapeEntity);
        mEtapeAcheminementAdapter.setmEtapeConsolidated(mListEtapeConsolidated);
        mEtapeAcheminementAdapter.notifyDataSetChanged();
    }
}
