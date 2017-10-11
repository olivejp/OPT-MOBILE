package nc.opt.mobile.optmobile.fragment;


import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.adapter.ColisAdapter;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderObserver;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;
import nc.opt.mobile.optmobile.service.SyncColisService;

import static nc.opt.mobile.optmobile.activity.MainActivity.TAG_SEARCH_PARCEL_FRAGMENT;

/**
 * Fragment that shows list of followed parcel
 * -FAB allow to add a parcel
 */
public class GestionColisFragment extends Fragment implements ProviderObserver.ProviderObserverListener{

    private ColisAdapter mColisAdapter;
    private AppCompatActivity mActivity;

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

    @OnClick(R.id.fab_add_parcel)
    public void onFloatingButtonClick(View view) {
        SearchParcelFragment searchParcelFragment = SearchParcelFragment.newInstance();
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main, searchParcelFragment, TAG_SEARCH_PARCEL_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Création d'un ColisObserver
        ArrayList<Uri> uris = new ArrayList<>();
        uris.add(OptProvider.ListColis.LIST_COLIS);
        uris.add(OptProvider.ListEtapeAcheminement.LIST_ETAPE);

        // Création d'un ProviderObserver pour écouter les modifications sur le content provider
        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.observe(mActivity, this, uris);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_parcel_management, container, false);
        ButterKnife.bind(this, rootView);

        // Changement du titre
        mActivity.setTitle(getActivity().getString(R.string.suivi_des_colis));

        // Ajout d'une barre separatrice entre les elements
        mRecyclerView.addItemDecoration(new
                DividerItemDecoration(mActivity,
                DividerItemDecoration.VERTICAL));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // Création d'un nouvel adapter
        mColisAdapter = new ColisAdapter(mActivity, ProviderUtilities.getListColisFromContentProvider(mActivity));

        mRecyclerView.setAdapter(mColisAdapter);

        mColisAdapter.notifyDataSetChanged();

        return rootView;
    }

    @Override
    public void onProviderChange() {
        mColisAdapter.getmColisList().clear();
        mColisAdapter.getmColisList().addAll(ProviderUtilities.getListColisFromContentProvider(mActivity));
        mColisAdapter.notifyDataSetChanged();
    }
}
