package nc.opt.mobile.optmobile.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.domain.suiviColis.EtapeConsolidated;
import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;
import nc.opt.mobile.optmobile.utils.DateConverter;

/**
 * Created by orlanth23 on 05/10/2017.
 */

public class EtapeAcheminementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<EtapeConsolidated> mEtapeConsolidated;

    public EtapeAcheminementAdapter() {
        mEtapeConsolidated = new ArrayList<>();
    }


    public void setmEtapeConsolidated(List<EtapeConsolidated> mEtapeConsolidated) {
        this.mEtapeConsolidated = mEtapeConsolidated;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_header_etape, parent, false);
                return new ViewHolderHeaderStepParcel(view);
            case 1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_etape, parent, false);
                return new ViewHolderStepParcel(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int i = holder.getItemViewType();
        if (i == 0) {
            ViewHolderHeaderStepParcel viewHeader = (ViewHolderHeaderStepParcel) holder;
            viewHeader.mEtapeEntity = mEtapeConsolidated.get(position);
            viewHeader.mStepDate.setText(DateConverter.convertDateEntityToUi(viewHeader.mEtapeEntity.getDate()));
            viewHeader.mStepPays.setText(viewHeader.mEtapeEntity.getPays());
            viewHeader.mStepLocalisation.setText(viewHeader.mEtapeEntity.getLocalisation());
            viewHeader.mStepDescription.setText(viewHeader.mEtapeEntity.getDescription());
            if (viewHeader.mEtapeEntity.getCommentaire().isEmpty()) {
                viewHeader.mStepCommentaire.setVisibility(View.GONE);
            } else {
                viewHeader.mStepCommentaire.setText(viewHeader.mEtapeEntity.getCommentaire());
            }

        } else if (i == 1) {
            ViewHolderStepParcel viewLine = (ViewHolderStepParcel) holder;
            viewLine.mEtapeEntity = mEtapeConsolidated.get(position);
            viewLine.mStepDate.setText(DateConverter.convertDateEntityToUi(viewLine.mEtapeEntity.getDate()));
            viewLine.mStepDescription.setText(viewLine.mEtapeEntity.getDescription());
            if (viewLine.mEtapeEntity.getCommentaire().isEmpty()) {
                viewLine.mStepCommentaire.setVisibility(View.GONE);
            } else {
                viewLine.mStepCommentaire.setText(viewLine.mEtapeEntity.getCommentaire());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mEtapeConsolidated.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (mEtapeConsolidated.get(position).getType().equals(EtapeConsolidated.TypeEtape.HEADER)) ? 1 : 0;
    }

    class ViewHolderStepParcel extends RecyclerView.ViewHolder {
        final View mView;

        @BindView(R.id.step_date)
        TextView mStepDate;

        @BindView(R.id.step_description)
        TextView mStepDescription;

        @BindView(R.id.step_commentaire)
        TextView mStepCommentaire;

        EtapeEntity mEtapeEntity;

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

        EtapeEntity mEtapeEntity;

        ViewHolderHeaderStepParcel(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, mView);
        }
    }
}
