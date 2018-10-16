package nc.opt.mobile.optmobile.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.Utilities;
import nc.opt.mobile.optmobile.broadcast.NetworkReceiver;
import nc.opt.mobile.optmobile.database.local.entity.ColisWithSteps;
import nc.opt.mobile.optmobile.ui.NoticeDialogFragment;
import nc.opt.mobile.optmobile.ui.RecyclerItemTouchHelper;
import nc.opt.mobile.optmobile.ui.activity.AddColisActivity;
import nc.opt.mobile.optmobile.ui.activity.MainActivity;
import nc.opt.mobile.optmobile.ui.activity.viewmodel.MainActivityViewModel;
import nc.opt.mobile.optmobile.ui.adapter.ColisAdapter;

/**
 * Created by orlanth23 on 14/01/2018.
 */

public class MainActivityFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerItemTouchHelper.SwipeListener {

    public static final String TAG = MainActivityFragment.class.getName();
    public static final String ARG_NOTICE_BUNDLE_COLIS = "ARG_NOTICE_BUNDLE_COLIS";
    public static final String ARG_NOTICE_BUNDLE_POSITION = "ARG_NOTICE_BUNDLE_POSITION";
    public static final String DIALOG_TAG_DELETE = "DIALOG_TAG_DELETE";
    public static final String DIALOG_TAG_DELIVERED = "DIALOG_TAG_DELIVERED";

    @BindView(R.id.recycler_colis_list)
    public RecyclerView recyclerViewColisList;

    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.text_explicatif_suivi_colis)
    public TextView textExplicatifView;

    @BindView(R.id.coordinator_main_activity)
    public CoordinatorLayout coordinatorLayout;

    private MainActivityViewModel viewModel;
    private AppCompatActivity appCompatActivity;
    private NoticeDialogFragment.NoticeDialogListener noticeDialogListener;
    private ColisAdapter colisAdapter;
    private RecyclerView.ItemDecoration itemDecoration;
    private RecyclerItemTouchHelper recyclerItemTouchHelper = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
    private ItemTouchHelper itemTouchHelper = new ItemTouchHelper(recyclerItemTouchHelper);
    private final Handler refreshTimeDelayHandler = new Handler();

    /**
     * onClickDisplay use in the Adapter to display the {@link HistoriqueColisFragment}
     */
    private View.OnClickListener onClickDisplay = (View v) -> {
        ColisWithSteps colis = (ColisWithSteps) v.getTag();
        viewModel.setSelectedColis(colis);
        displayHistorique(viewModel.isTwoPane());
    };

    public MainActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        appCompatActivity = (AppCompatActivity) context;
        try {
            noticeDialogListener = (NoticeDialogFragment.NoticeDialogListener) appCompatActivity;
        } catch (ClassCastException e) {
            Log.e(TAG, "L'activité appelant MainActivityFragment doit implémenter NoticeDialogListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(appCompatActivity).get(MainActivityViewModel.class);
        itemDecoration = new DividerItemDecoration(appCompatActivity, DividerItemDecoration.VERTICAL);

        // Initialize adapter
        colisAdapter = new ColisAdapter(onClickDisplay);

        // Lecture en base de toutes les colis et de leurs étapes en base.
        viewModel.getLiveColisWithSteps().observe(this, this::putListIntoAdapter);

        viewModel.isDataSetChanged().observe(this, positionItemChanged -> {
            if (positionItemChanged != null) {
                colisAdapter.notifyItemChanged(positionItemChanged);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel.setSelectedColis(null);

        // View creation
        View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);
        ButterKnife.bind(this, rootView);

        // If empty list, then
        viewModel.isListColisActiveEmpty().observe(this, integer -> {
            if (integer != null && integer > 0) {
                recyclerViewColisList.setVisibility(View.VISIBLE);
                textExplicatifView.setVisibility(View.GONE);
            } else {
                recyclerViewColisList.setVisibility(View.GONE);
                textExplicatifView.setVisibility(View.VISIBLE);
            }
        });

        // Attach adapter to the recyclerView
        recyclerViewColisList.setAdapter(colisAdapter);

        // Add OnRefreshListener to the recyclerView
        swipeRefreshLayout.setOnRefreshListener(this);

        // Add Swipe to the recycler view
        itemTouchHelper.attachToRecyclerView(recyclerViewColisList);

        // Add itemDecorator to the recycler view
        recyclerViewColisList.addItemDecoration(itemDecoration);

        return rootView;
    }

    @OnClick(R.id.fab_add_colis)
    public void addColis(View v) {
        Intent intent = new Intent(getContext(), AddColisActivity.class);
        startActivity(intent);
    }

    private void putListIntoAdapter(List<ColisWithSteps> colisWithSteps) {
        if (colisWithSteps != null) {
            // On trie les étapes dans l'ordre des dates
            for (ColisWithSteps colis : colisWithSteps) {
                Collections.sort(colis.getStepEntityList(), (stepEntity1, stepEntity2) -> stepEntity1.getDate().compareTo(stepEntity2.getDate()));
            }
            colisAdapter.setColisList(colisWithSteps);
        }
    }

    private void displayHistorique(boolean twoPane) {
        if (getFragmentManager() != null) {
            HistoriqueColisFragment historiqueFragment = new HistoriqueColisFragment();
            if (twoPane) {
                getFragmentManager().beginTransaction().replace(R.id.frame_detail, historiqueFragment, MainActivity.TAG_DETAIL_FRAGMENT).commit();
            } else {
                getFragmentManager().beginTransaction().replace(R.id.frame_master, historiqueFragment, MainActivity.TAG_DETAIL_FRAGMENT).addToBackStack(null).commit();
            }
        }
    }

    @Override
    public void onRefresh() {
        if (NetworkReceiver.checkConnection(appCompatActivity)) {
            viewModel.refresh();
            swipeRefreshLayout.setRefreshing(true);
            refreshTimeDelayHandler.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 5000);
        } else {
            Snackbar.make(coordinatorLayout, R.string.CONNECTION_REQUIRED, Snackbar.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction) {
        try {
            ColisAdapter.ViewHolderColisAdapter viewHolderColisAdapter = (ColisAdapter.ViewHolderColisAdapter) viewHolder;
            ColisWithSteps colis = viewHolderColisAdapter.getColisWithSteps();

            // Création d'un bundle dans lequel on va passer nos items
            Bundle bundle = new Bundle();
            bundle.putParcelable(ARG_NOTICE_BUNDLE_COLIS, colis.colisEntity);
            bundle.putInt(ARG_NOTICE_BUNDLE_POSITION, viewHolderColisAdapter.getAdapterPosition());

            if (direction == ItemTouchHelper.LEFT) {
                // Appel d'un fragment qui va demander à l'utilisateur s'il est sûr de vouloir supprimer le colis.
                Utilities.sendDialogByFragmentManagerWithRes(getFragmentManager(),
                        String.format("Supprimer le numéro de suivi %s ?\n\nLe numéro de suivi ainsi que toutes ses étapes seront perdues.", colis.colisEntity.getIdColis()),
                        NoticeDialogFragment.TYPE_BOUTON_YESNO,
                        R.drawable.ic_delete_grey_900_24dp,
                        DIALOG_TAG_DELETE,
                        bundle,
                        noticeDialogListener);
            } else {
                if (!colis.colisEntity.isDelivered()) {
                    // Appel d'un fragment qui va demander à l'utilisateur s'il est sûr de vouloir délivrer le colis.
                    Utilities.sendDialogByFragmentManagerWithRes(getFragmentManager(),
                            String.format("Marquer le numéro de suivi %s comme délivré ?\n\nCeci arrêtera son suivi automatique mais vous conserverez tout son historique.", colis.colisEntity.getIdColis()),
                            NoticeDialogFragment.TYPE_BOUTON_YESNO,
                            R.drawable.ic_check_circle_grey_900_48dp,
                            DIALOG_TAG_DELIVERED,
                            bundle,
                            noticeDialogListener);
                }
            }
        } catch (ClassCastException e) {
            Log.e(TAG, "La vue doit contenir un ColisEntity comme Tag");
        }
    }
}
