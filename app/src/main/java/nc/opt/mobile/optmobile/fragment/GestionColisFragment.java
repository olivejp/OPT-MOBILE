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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.adapter.ColisAdapter;
import nc.opt.mobile.optmobile.fragment.viewmodel.GestionColisFragmentViewModel;
import nc.opt.mobile.optmobile.gfx.RecyclerItemTouchHelper;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.utils.NoticeDialogFragment;
import nc.opt.mobile.optmobile.utils.Utilities;

import static nc.opt.mobile.optmobile.activity.GestionColisActivity.ARG_NOTICE_BUNDLE_COLIS;

/**
 * Fragment that shows mList of followed parcel
 * -FAB allow to add a parcel
 */
public class GestionColisFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private ColisAdapter mColisAdapter;
    private AppCompatActivity mActivity;
    private static final String ARG_TWO_PANE = "ARG_TWO_PANE";

    private static final String DIALOG_TAG_DELETE = "DIALOG_TAG_DELETE";

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

        boolean mTwoPane = false;
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

        // Add Swipe to the recycler view
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        updateVisibility();

        // get history from the view model
        viewModel.getColisEntities().observe(this, this::updateViewFromViewModel);

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
    private void updateViewFromViewModel(List<ColisEntity> colisEntities) {
        mColisAdapter.setColisList(colisEntities);
        mColisAdapter.notifyDataSetChanged();
        updateVisibility();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ColisAdapter.ViewHolderStepParcel) {
            ColisAdapter.ViewHolderStepParcel r = (ColisAdapter.ViewHolderStepParcel) viewHolder;

            // Création d'un bundle dans lequel on va passer nos items
            Bundle bundle = new Bundle();
            bundle.putParcelable(ARG_NOTICE_BUNDLE_COLIS, r.getmColis());

            // Appel d'un fragment qui va demander à l'utilisateur s'il est sûr de vouloir supprimer le colis.
            Utilities.sendDialogByFragmentManager(getFragmentManager(), "Etes-vous sûr de vouloir supprimer ce colis ?", NoticeDialogFragment.TYPE_BOUTON_YESNO, NoticeDialogFragment.TYPE_IMAGE_INFORMATION, DIALOG_TAG_DELETE, bundle);
        }
    }
}
