package nc.opt.mobile.optmobile.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.Utilities;
import nc.opt.mobile.optmobile.broadcast.NetworkReceiver;
import nc.opt.mobile.optmobile.database.local.entity.ColisEntity;
import nc.opt.mobile.optmobile.database.local.entity.ColisWithSteps;
import nc.opt.mobile.optmobile.ui.NoticeDialogFragment;
import nc.opt.mobile.optmobile.ui.RecyclerItemTouchHelper;
import nc.opt.mobile.optmobile.ui.activity.viewmodel.MainActivityViewModel;
import nc.opt.mobile.optmobile.ui.adapter.ColisAdapter;

import static nc.opt.mobile.optmobile.ui.activity.DetailActivity.ID_COLIS_EXTRA;

public class MainActivity extends AppCompatActivity implements NoticeDialogFragment.NoticeDialogListener, SwipeRefreshLayout.OnRefreshListener, RecyclerItemTouchHelper.SwipeListener {

    public static final String TAG = MainActivity.class.getName();
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
    private ColisAdapter colisAdapter;
    private RecyclerItemTouchHelper recyclerItemTouchHelper = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
    private ItemTouchHelper itemTouchHelper = new ItemTouchHelper(recyclerItemTouchHelper);
    private final Handler refreshTimeDelayHandler = new Handler();

    @OnClick(R.id.fab_add_colis)
    public void addColis(View v) {
        Intent intent = new Intent(getApplicationContext(), AddColisActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        // Initialize adapter
        colisAdapter = new ColisAdapter(onClickDisplay);

        // Lecture en base de toutes les colis et de leurs étapes en base.
        viewModel.getLiveColisWithSteps().observe(this, this::putListIntoAdapter);

        viewModel.isDataSetChanged().observe(this, positionItemChanged -> {
            if (positionItemChanged != null) {
                colisAdapter.notifyItemChanged(positionItemChanged);
            }
        });

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(NoticeDialogFragment dialog) {
        if (dialog.getTag() != null && dialog.getTag().equals(DIALOG_TAG_DELETE) && dialog.getBundle() != null && dialog.getBundle().containsKey(ARG_NOTICE_BUNDLE_COLIS)) {
            ColisEntity colisEntity = dialog.getBundle().getParcelable(ARG_NOTICE_BUNDLE_COLIS);
            if (colisEntity != null) {
                viewModel.delete(colisEntity)
                        .doOnSuccess(integer -> {
                            if (integer > 0) {
                                Snackbar.make(coordinatorLayout, "Colis supprimé", BaseTransientBottomBar.LENGTH_LONG).show();
                            }
                        })
                        .subscribe();
            }
        }
        if (dialog.getTag() != null && dialog.getTag().equals(DIALOG_TAG_DELIVERED) && dialog.getBundle() != null && dialog.getBundle().containsKey(ARG_NOTICE_BUNDLE_COLIS)) {
            ColisEntity colisEntity = dialog.getBundle().getParcelable(ARG_NOTICE_BUNDLE_COLIS);
            int position = dialog.getBundle().getInt(ARG_NOTICE_BUNDLE_POSITION);
            if (colisEntity != null) {
                viewModel.markAsDelivered(colisEntity);
                viewModel.notifyItemChanged(position);
            }
        }
    }

    private View.OnClickListener onClickDisplay = (View v) -> {
        ColisWithSteps colis = (ColisWithSteps) v.getTag();
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra(ID_COLIS_EXTRA, colis.getColisEntity().getIdColis());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    };

    @Override
    public void onDialogNegativeClick(NoticeDialogFragment dialog) {
        if (dialog.getTag() != null && (dialog.getTag().equals(DIALOG_TAG_DELETE) || dialog.getTag().equals(DIALOG_TAG_DELIVERED))) {
            int position = dialog.getBundle().getInt(ARG_NOTICE_BUNDLE_POSITION);
            viewModel.notifyItemChanged(position);
        }
    }

    @Override
    public void onRefresh() {
        if (NetworkReceiver.checkConnection(this)) {
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
                Utilities.sendDialogByFragmentManagerWithRes(getSupportFragmentManager(),
                        String.format("Supprimer le numéro de suivi %s ?\n\nLe numéro de suivi ainsi que toutes ses étapes seront perdues.", colis.colisEntity.getIdColis()),
                        NoticeDialogFragment.TYPE_BOUTON_YESNO,
                        R.drawable.ic_delete_grey_900_24dp,
                        DIALOG_TAG_DELETE,
                        bundle,
                        this);
            } else {
                if (!colis.colisEntity.isDelivered()) {
                    // Appel d'un fragment qui va demander à l'utilisateur s'il est sûr de vouloir délivrer le colis.
                    Utilities.sendDialogByFragmentManagerWithRes(getSupportFragmentManager(),
                            String.format("Marquer le numéro de suivi %s comme délivré ?\n\nCeci arrêtera son suivi automatique mais vous conserverez tout son historique.", colis.colisEntity.getIdColis()),
                            NoticeDialogFragment.TYPE_BOUTON_YESNO,
                            R.drawable.ic_check_circle_grey_900_48dp,
                            DIALOG_TAG_DELIVERED,
                            bundle,
                            this);
                }
            }
        } catch (ClassCastException e) {
            Log.e(TAG, "La vue doit contenir un ColisEntity comme Tag");
        }
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
}
