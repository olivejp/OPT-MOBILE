package nc.opt.mobile.optmobile.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.adapter.EtapeAcheminementAdapter;
import nc.opt.mobile.optmobile.fragment.viewmodel.HistoriqueColisFragmentViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoriqueColisFragment extends Fragment {

    private static final String ARG_ID_COLIS = "ARG_ID_COLIS";

    private AppCompatActivity mAppCompatActivity;
    private EtapeAcheminementAdapter mEtapeAcheminementAdapter;
    private ListenToSelectedColis mListener;
    private HistoriqueColisFragmentViewModel viewModel;

    @BindView(R.id.recycler_colis_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.text_object_not_found)
    TextView textObjectNotFound;

    /**
     * Instanciation d'un nouveau fragment
     * @param idColis
     * @return
     */
    public static HistoriqueColisFragment newInstance(@NotNull String idColis) {
        HistoriqueColisFragment fragment = new HistoriqueColisFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID_COLIS, idColis);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Constructor vide
     */
    public HistoriqueColisFragment() {
        // Required empty public constructor
    }

    /**
     * On start we retrieve or create the ViewModel
     */
    @Override
    public void onStart() {
        super.onStart();
        viewModel = ViewModelProviders.of(this).get(HistoriqueColisFragmentViewModel.class);
    }

    /**
     * On attach, if the parent activity implements ListenToSelectedColis,
     * we record the activity in mListener.
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppCompatActivity = (AppCompatActivity) context;
        if (context instanceof ListenToSelectedColis) {
            mListener = (ListenToSelectedColis) context;
        }
    }

    /**
     * We pass the id colis passed through the constructor to the viewModel.
     * If mListener is not null we will pass the id colis to it.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            viewModel.setIdColis(getArguments().getString(ARG_ID_COLIS));

            // On envoie à notre listener l'id colis avec lequel on est arrivé.
            if (mListener != null) {
                mListener.subscribe(viewModel.getIdColis());
            }
        }

        // create new adapter from the provider mListEtape
        mEtapeAcheminementAdapter = new EtapeAcheminementAdapter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_historique_colis, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mAppCompatActivity);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mEtapeAcheminementAdapter);

        updateVisibility();
        updateViewFromViewModel();

        return rootView;
    }

    /**
     * Display the recycler view if the list<etapesConsolidated> != null or show a text instead
     */
    private void updateVisibility() {
        textObjectNotFound.setVisibility(viewModel.getTextObjectNotFoundVisibility());
        mRecyclerView.setVisibility(viewModel.getRecyclerViewVisibility());
    }

    /**
     * Update the UI from the viewModel
     */
    private void updateViewFromViewModel() {
        // get history from the view model
        viewModel.getEtapesConsolidated(viewModel.getIdColis()).observe(this, etapeConsolidateds -> {
            mEtapeAcheminementAdapter.setEtapesConsolidated(etapeConsolidateds);
            mEtapeAcheminementAdapter.notifyDataSetChanged();

            // Change the visibility of the textView
            updateVisibility();
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_ID_COLIS, viewModel.getIdColis());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mListener != null) {
            mListener.unsubscribe();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mListener != null) {
            mListener.unsubscribe();
        }
    }

    public interface ListenToSelectedColis {
        void subscribe(String idColis);

        void unsubscribe();
    }
}
