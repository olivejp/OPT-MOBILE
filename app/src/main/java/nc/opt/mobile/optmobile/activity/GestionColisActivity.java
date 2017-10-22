package nc.opt.mobile.optmobile.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.adapter.ColisAdapter;
import nc.opt.mobile.optmobile.entity.ColisEntity;
import nc.opt.mobile.optmobile.fragment.SearchColisFragment;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderObserver;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;

import static nc.opt.mobile.optmobile.activity.MainActivity.TAG_SEARCH_PARCEL_FRAGMENT;

public class GestionColisActivity extends AppCompatActivity implements ProviderObserver.ProviderObserverListener {

    private ColisAdapter mColisAdapter;

    @BindView(R.id.recycler_parcel_list_management)
    RecyclerView mRecyclerView;

    @BindView(R.id.fab_add_parcel)
    FloatingActionButton mFloatingButtonAddParcel;

    @BindView(R.id.text_explicatif_suivi_colis)
    TextView textExplicatifSuiviColis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colis_gestion);
        ButterKnife.bind(this);

        // create a ColisObserver
        ArrayList<Uri> uris = new ArrayList<>();
        uris.add(OptProvider.ListColis.LIST_COLIS);
        uris.add(OptProvider.ListEtapeAcheminement.LIST_ETAPE);

        // create a ProviderObserver to listen updates from the provider
        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.observe(this, this, uris);

        // change title
        setTitle(getString(R.string.suivi_des_colis));

        // add separator between each element
        mRecyclerView.addItemDecoration(new
                DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // get the list from the provider
        List<ColisEntity> list = ProviderUtilities.getListColisFromContentProvider(this);
        mColisAdapter = new ColisAdapter(this, list);
        mRecyclerView.setAdapter(mColisAdapter);
        mColisAdapter.notifyDataSetChanged();

        // change visibility depending on the list content
        textExplicatifSuiviColis.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onProviderChange() {
        mColisAdapter.getmColisList().clear();
        mColisAdapter.getmColisList().addAll(ProviderUtilities.getListColisFromContentProvider(this));
        mColisAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fab_add_parcel)
    public void onFloatingButtonClick(View view) {
        SearchColisFragment searchColisFragment = SearchColisFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_master, searchColisFragment, TAG_SEARCH_PARCEL_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }
}
