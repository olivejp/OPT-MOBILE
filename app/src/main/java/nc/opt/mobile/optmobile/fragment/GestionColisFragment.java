package nc.opt.mobile.optmobile.fragment;

import android.arch.lifecycle.ViewModelProviders;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.adapter.ColisAdapter;
import nc.opt.mobile.optmobile.fragment.viewmodel.GestionColisFragmentViewModel;

/**
 * Fragment that shows mList of followed parcel
 * -FAB allow to add a parcel
 */
public class GestionColisFragment extends Fragment {

    private ColisAdapter mColisAdapter;
    private AppCompatActivity mActivity;
    private boolean mTwoPane;
    private static final String ARG_TWO_PANE = "ARG_TWO_PANE";
    private GestionColisFragmentViewModel viewModel;

    @BindView(R.id.recycler_parcel_list_management)
    RecyclerView mRecyclerView;

    @BindView(R.id.text_explicatif_suivi_colis)
    TextView textExplicatifSuiviColis;

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

        viewModel = ViewModelProviders.of(this).get(GestionColisFragmentViewModel.class);

        mTwoPane = false;
        if (getArguments() != null && getArguments().containsKey(ARG_TWO_PANE)) {
            mTwoPane = getArguments().getBoolean(ARG_TWO_PANE);
        }

        mColisAdapter = new ColisAdapter(mActivity, mTwoPane);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gestion_colis, container, false);
        ButterKnife.bind(this, rootView);

        // change title
        mActivity.setTitle(getActivity().getString(R.string.suivi_des_colis));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mColisAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));

        updateVisibility();
        updateViewFromViewModel();

        return rootView;
    }

    /**
     * Display the recycler view if the list<etapesConsolidated> != null or show a text instead
     */
    private void updateVisibility() {
        textExplicatifSuiviColis.setVisibility(viewModel.getTextObjectNotFoundVisibility());
        mRecyclerView.setVisibility(viewModel.getRecyclerViewVisibility());
    }

    /**
     * Update the UI from the viewModel
     */
    private void updateViewFromViewModel() {
        // get history from the view model
        viewModel.getColisEntities().observe(this, colisEntities -> {
            mColisAdapter.setColisList(colisEntities);
            mColisAdapter.notifyDataSetChanged();

            // Change the visibility of the textView
            updateVisibility();
        });
    }
}
