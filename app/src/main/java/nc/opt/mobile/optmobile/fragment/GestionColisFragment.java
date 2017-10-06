package nc.opt.mobile.optmobile.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.adapter.ColisAdapter;
import nc.opt.mobile.optmobile.domain.Colis;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;

/**
 * Fragment that shows list of followed parcel
 * -FAB allow to add a parcel
 */
public class GestionColisFragment extends Fragment {

    private ColisAdapter mColisAdapter;

    @BindView(R.id.recycler_parcel_list_management)
    RecyclerView mRecyclerView;

    @BindView(R.id.fab_add_parcel)
    FloatingActionButton mFloatingButtonAddParcel;

    public static GestionColisFragment newInstance() {
        GestionColisFragment fragment = new GestionColisFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public GestionColisFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_parcel_management, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        List<Colis> listColis = ProviderUtilities.getListColisFromContentProvider(getActivity());

        // Création d'un nouvel adapter
        mColisAdapter = new ColisAdapter(listColis);

        mRecyclerView.setAdapter(mColisAdapter);

        mColisAdapter.notifyDataSetChanged();

        return rootView;
    }
}
