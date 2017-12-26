package nc.opt.mobile.optmobile.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.domain.suivi.EtapeConsolidated;
import nc.opt.mobile.optmobile.utils.DateConverter;

/**
 * Created by orlanth23 on 05/10/2017.
 */

public class EtapeConsolidatedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = EtapeConsolidatedAdapter.class.getName();

    private List<EtapeConsolidated> etapesConsolidated;

    public EtapeConsolidatedAdapter() {
        etapesConsolidated = new ArrayList<>();
    }


    public void setEtapesConsolidated(List<EtapeConsolidated> etapesConsolidated) {
        this.etapesConsolidated = etapesConsolidated;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == EtapeConsolidated.TypeEtape.HEADER.getTypeValue()) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_header_etape, parent, false);
            return new ViewHolderHeaderStepParcel(view);
        }

        if (viewType == EtapeConsolidated.TypeEtape.DETAIL.getTypeValue()) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_etape, parent, false);
            return new ViewHolderStepParcel(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int i = holder.getItemViewType();
        if (i == EtapeConsolidated.TypeEtape.HEADER.getTypeValue()) {
            ViewHolderHeaderStepParcel viewHeader = (ViewHolderHeaderStepParcel) holder;
            viewHeader.etapeConsolidated = etapesConsolidated.get(position);
            viewHeader.mStepDate.setText(DateConverter.convertDateEntityToUi(viewHeader.etapeConsolidated.getDate()));
            viewHeader.mStepPays.setText(viewHeader.etapeConsolidated.getPays());
            viewHeader.mStepLocalisation.setText(viewHeader.etapeConsolidated.getLocalisation());
            viewHeader.mStepDescription.setText(viewHeader.etapeConsolidated.getDescription());
            if (viewHeader.etapeConsolidated.getCommentaire().isEmpty()) {
                viewHeader.mStepCommentaire.setVisibility(View.GONE);
            } else {
                viewHeader.mStepCommentaire.setText(viewHeader.etapeConsolidated.getCommentaire());
            }
        }

        if (i == EtapeConsolidated.TypeEtape.DETAIL.getTypeValue()) {
            try {
                ViewHolderStepParcel viewLine = (ViewHolderStepParcel) holder;
                viewLine.etapeConsolidated = etapesConsolidated.get(position);
                viewLine.mStepDate.setText(DateConverter.convertDateEntityToUi(viewLine.etapeConsolidated.getDate()));
                viewLine.mStepDescription.setText(viewLine.etapeConsolidated.getDescription());
                if (viewLine.etapeConsolidated.getCommentaire().isEmpty()) {
                    viewLine.mStepCommentaire.setVisibility(View.GONE);
                } else {
                    viewLine.mStepCommentaire.setText(viewLine.etapeConsolidated.getCommentaire());
                }
            } catch (ClassCastException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    @Override
    public int getItemCount() {
        return etapesConsolidated.size();
    }

    @Override
    public int getItemViewType(int position) {
        return etapesConsolidated.get(position).getType().getTypeValue();
    }

    class ViewHolderStepParcel extends RecyclerView.ViewHolder {
        final View mView;

        @BindView(R.id.step_date)
        TextView mStepDate;

        @BindView(R.id.step_description)
        TextView mStepDescription;

        @BindView(R.id.step_commentaire)
        TextView mStepCommentaire;

        EtapeConsolidated etapeConsolidated;

        ViewHolderStepParcel(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, mView);
        }
    }

    class ViewHolderHeaderStepParcel extends RecyclerView.ViewHolder {
        final View mView;

        @BindView(R.id.step_pays)
        TextView mStepPays;

        @BindView(R.id.step_localisation)
        TextView mStepLocalisation;

        @BindView(R.id.step_date)
        TextView mStepDate;

        @BindView(R.id.step_description)
        TextView mStepDescription;

        @BindView(R.id.step_commentaire)
        TextView mStepCommentaire;

        EtapeConsolidated etapeConsolidated;

        ViewHolderHeaderStepParcel(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, mView);
        }
    }
}
