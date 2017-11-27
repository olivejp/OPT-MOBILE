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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.adapter.ColisAdapter;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderObserver;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;

import static nc.opt.mobile.optmobile.provider.services.ColisService.listFromProvider;

/**
 * Fragment that shows mList of followed parcel
 * -FAB allow to add a parcel
 */
public class GestionColisFragment extends Fragment implements ProviderObserver.ProviderObserverListener {

    private ColisAdapter mColisAdapter;
    private AppCompatActivity mActivity;
    private boolean mTwoPane;
    private static final String ARG_TWO_PANE = "ARG_TWO_PANE";

    @BindView(R.id.recycler_parcel_list_management)
    RecyclerView mRecyclerView;

    @BindView(R.id.text_explicatif_suivi_colis)
    TextView textExplicatifSuiviColis;

    private List<ColisEntity> mList;

    public static GestionColisFragment newInstance(boolean twoPane) {
        GestionColisFragment fragment = new GestionColisFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_TWO_PANE, twoPane);
        fragment.setArguments(args);
        return fragment;
    }

    public GestionColisFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTwoPane = false;
        if (getArguments() != null && getArguments().containsKey(ARG_TWO_PANE)) {
            mTwoPane = getArguments().getBoolean(ARG_TWO_PANE);
        }

        // create a ProviderObserver to listen updates from the provider
        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.observe(mActivity, this, OptProvider.ListColis.LIST_COLIS, OptProvider.ListEtapeAcheminement.LIST_ETAPE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gestion_colis, container, false);
        ButterKnife.bind(this, rootView);

        // change title
        mActivity.setTitle(getActivity().getString(R.string.suivi_des_colis));

        // add separator between each element
        mRecyclerView.addItemDecoration(new
                DividerItemDecoration(mActivity,
                DividerItemDecoration.VERTICAL));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // get the mList from the provider
        mList = listFromProvider(mActivity, true);
        mColisAdapter = new ColisAdapter(mActivity, mList, mTwoPane);
        mRecyclerView.setAdapter(mColisAdapter);
        mColisAdapter.notifyDataSetChanged();

        // change visibility depending on the mList content
        changeVisibility();

        return rootView;
    }

    @Override
    public void onProviderChange() {
        mColisAdapter.setmColisList(null);
        mList = listFromProvider(mActivity, true);
        mColisAdapter.setmColisList(mList);
        mColisAdapter.notifyDataSetChanged();
        changeVisibility();
    }

    private void changeVisibility() {
        textExplicatifSuiviColis.setVisibility(mList.isEmpty() ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(mList.isEmpty() ? View.GONE : View.VISIBLE);
    }
}
