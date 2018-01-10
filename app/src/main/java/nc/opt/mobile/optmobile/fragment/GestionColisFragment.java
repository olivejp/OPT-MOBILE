package nc.opt.mobile.optmobile.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.activity.GestionColisActivity;
import nc.opt.mobile.optmobile.activity.viewmodel.GestionColisActivityViewModel;
import nc.opt.mobile.optmobile.adapter.ColisAdapter;
import nc.opt.mobile.optmobile.gfx.RecyclerItemTouchHelper;
import nc.opt.mobile.optmobile.database.entity.ColisEntity;
import nc.opt.mobile.optmobile.utils.NoticeDialogFragment;
import nc.opt.mobile.optmobile.utils.Utilities;

import static nc.opt.mobile.optmobile.activity.GestionColisActivity.ARG_NOTICE_BUNDLE_COLIS;
import static nc.opt.mobile.optmobile.activity.GestionColisActivity.ARG_NOTICE_BUNDLE_POSITION;

/**
 * Fragment that shows mList of followed parcel
 * -FAB allow to add a parcel
 */
public class GestionColisFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private static final String TAG = GestionColisActivity.class.getName();

    private AppCompatActivity mActivity;
    private static final String ARG_TWO_PANE = "ARG_TWO_PANE";

    private static final String DIALOG_TAG_DELETE = "DIALOG_TAG_DELETE";

    private GestionColisActivityViewModel viewModel;
    private NoticeDialogFragment.NoticeDialogListener mDeleteListener;
    private boolean mTwoPane;
    private ColisAdapter colisAdapter;

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

    private View.OnClickListener mOnClickListener = (View v) -> {
        ColisEntity colis = (ColisEntity) v.getTag();
        viewModel.setSelectedColis(colis);
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        if (mTwoPane) {
            ft.replace(R.id.frame_detail, new HistoriqueColisFragment(), GestionColisActivity.TAG_PARCEL_RESULT_SEARCH_FRAGMENT).commit();
        } else {
            ft.replace(R.id.frame_master, new HistoriqueColisFragment(), GestionColisActivity.TAG_PARCEL_RESULT_SEARCH_FRAGMENT).addToBackStack(null).commit();
        }
    };

    public void setTwoPane(boolean twopane) {
        this.mTwoPane = twopane;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
        try {
            mDeleteListener = (NoticeDialogFragment.NoticeDialogListener) context;
        } catch (ClassCastException c) {
            Log.e(TAG, "Activity should implements NoticeDialogFragment.NoticeDialogListener", c);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(mActivity).get(GestionColisActivityViewModel.class);
        mTwoPane = false;
        if (getArguments() != null && getArguments().containsKey(ARG_TWO_PANE)) {
            mTwoPane = getArguments().getBoolean(ARG_TWO_PANE);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gestion_colis, container, false);
        ButterKnife.bind(this, rootView);

        // change title
        mActivity.setTitle(mActivity.getString(R.string.suivi_des_colis));

        colisAdapter = new ColisAdapter(mActivity, mOnClickListener);
        colisAdapter.setColisList(null);
        colisAdapter.notifyDataSetChanged();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(colisAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));

        // Add Swipe to the recycler view
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        viewModel.getColisEntities().observe(this, colisEntities -> {
            colisAdapter.setColisList(colisEntities);
            colisAdapter.notifyDataSetChanged();

            if (colisEntities == null || colisEntities.isEmpty()) {
                textExplicatifSuiviColis.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                textExplicatifSuiviColis.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        });
        return rootView;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ColisAdapter.ViewHolderStepParcel) {
            ColisAdapter.ViewHolderStepParcel r = (ColisAdapter.ViewHolderStepParcel) viewHolder;

            // Création d'un bundle dans lequel on va passer nos items
            Bundle bundle = new Bundle();
            bundle.putParcelable(ARG_NOTICE_BUNDLE_COLIS, r.getmColis());
            bundle.putInt(ARG_NOTICE_BUNDLE_POSITION, position);

            // Appel d'un fragment qui va demander à l'utilisateur s'il est sûr de vouloir supprimer le colis.
            Utilities.sendDialogByFragmentManager(getFragmentManager(),
                    String.format("Etes-vous sûr de vouloir supprimer le colis %s ?", r.getmColis().getIdColis()),
                    NoticeDialogFragment.TYPE_BOUTON_YESNO,
                    NoticeDialogFragment.TYPE_IMAGE_INFORMATION,
                    DIALOG_TAG_DELETE,
                    bundle,
                    mDeleteListener);
        }
    }
}
