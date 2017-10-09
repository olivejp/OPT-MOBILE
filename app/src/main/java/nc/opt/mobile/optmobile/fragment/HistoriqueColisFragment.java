package nc.opt.mobile.optmobile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.adapter.EtapeAcheminementAdapter;
import nc.opt.mobile.optmobile.domain.EtapeAcheminement;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoriqueColisFragment extends Fragment {

    private static final String TAG = HistoriqueColisFragment.class.getName();
    private static final String ARG_ID_PARCEL = "ARG_ID_PARCEL";

    private String mIdColis;
    private EtapeAcheminementAdapter mEtapeAcheminementAdapter;

    @BindView(R.id.recycler_parcel_list)
    RecyclerView mRecyclerView;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIdColis = getArguments().getString(ARG_ID_PARCEL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result_search_parcel, container, false);
        ButterKnife.bind(this, rootView);

        // Changement du titre
        getActivity().setTitle(mIdColis);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // Ajout d'une barre separatrice entre les elements
        mRecyclerView.addItemDecoration(new
                DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        // Recuperer l'historique a partir du content provider
        List<EtapeAcheminement> list = ProviderUtilities.getListEtapeFromContentProvider(getActivity(), mIdColis);

        // Création d'un nouvel adapter avec notre liste
        mEtapeAcheminementAdapter = new EtapeAcheminementAdapter(list);

        mRecyclerView.setAdapter(mEtapeAcheminementAdapter);

        mEtapeAcheminementAdapter.notifyDataSetChanged();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_ID_PARCEL, mIdColis);
    }
}
