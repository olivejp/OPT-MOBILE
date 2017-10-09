package nc.opt.mobile.optmobile.fragment;


import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.adapter.ColisAdapter;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;

import static nc.opt.mobile.optmobile.activity.MainActivity.TAG_SEARCH_PARCEL_FRAGMENT;

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

    @OnClick(R.id.fab_add_parcel)
    public void onFloatingButtonClick(View view) {
        SearchParcelFragment searchParcelFragment = SearchParcelFragment.newInstance();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main, searchParcelFragment, TAG_SEARCH_PARCEL_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Création d'un ColisObserver
        ColisObserver colisObserver = new ColisObserver(new Handler());
        colisObserver.observe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_parcel_management, container, false);
        ButterKnife.bind(this, rootView);

        // Changement du titre
        getActivity().setTitle(getActivity().getString(R.string.suivi_des_colis));

        // Ajout d'une barre separatrice entre les elements
        mRecyclerView.addItemDecoration(new
                DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // Création d'un nouvel adapter
        mColisAdapter = new ColisAdapter(getActivity(), ProviderUtilities.getListColisFromContentProvider(getActivity()));

        mRecyclerView.setAdapter(mColisAdapter);

        mColisAdapter.notifyDataSetChanged();

        return rootView;
    }

    // Anonymous inner class to handle watching Uris
    private class ColisObserver extends ContentObserver {
        ColisObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            ContentResolver resolver = getActivity().getContentResolver();
            // set our watcher for the Uri we are concerned with
            resolver.registerContentObserver(
                    OptProvider.ListColis.LIST_COLIS,
                    false, // only notify that Uri of change *prob won't need true here often*
                    this); // this innerclass handles onChange

            // set our watcher for the Uri we are concerned with
            resolver.registerContentObserver(
                    OptProvider.ListEtapeAcheminement.LIST_ETAPE,
                    false, // only notify that Uri of change *prob won't need true here often*
                    this); // this innerclass handles onChange
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mColisAdapter.getmColisList().clear();
            mColisAdapter.getmColisList().addAll(ProviderUtilities.getListColisFromContentProvider(getActivity()));
            mColisAdapter.notifyDataSetChanged();
        }
    }
}
