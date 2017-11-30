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
import nc.opt.mobile.optmobile.provider.entity.EtapeAcheminementEntity;
import nc.opt.mobile.optmobile.utils.DateConverter;

/**
 * Created by orlanth23 on 05/10/2017.
 */

public class EtapeAcheminementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EtapeAcheminementEntity> mEtapeAcheminements;
    private String previous_header_key = null;

    public EtapeAcheminementAdapter() {
        mEtapeAcheminements = new ArrayList<>();
    }

    public void setmEtapeAcheminements(List<EtapeAcheminementEntity> mEtapeAcheminements) {
        this.mEtapeAcheminements = mEtapeAcheminements;
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
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                ViewHolderHeaderStepParcel viewHeader = (ViewHolderHeaderStepParcel) holder;
                viewHeader.mEtapeEntity = mEtapeAcheminements.get(position);
                viewHeader.mStepDate.setText(DateConverter.convertDateEntityToUi(viewHeader.mEtapeEntity.getDate()));
                viewHeader.mStepPays.setText(viewHeader.mEtapeEntity.getPays());
                viewHeader.mStepLocalisation.setText(viewHeader.mEtapeEntity.getLocalisation());
                viewHeader.mStepDescription.setText(viewHeader.mEtapeEntity.getDescription());
                if (viewHeader.mEtapeEntity.getCommentaire().isEmpty()) {
                    viewHeader.mStepCommentaire.setVisibility(View.GONE);
                } else {
                    viewHeader.mStepCommentaire.setText(viewHeader.mEtapeEntity.getCommentaire());
                }
                break;
            case 1:
                ViewHolderStepParcel viewLine = (ViewHolderStepParcel) holder;
                viewLine.mEtapeEntity = mEtapeAcheminements.get(position);
                viewLine.mStepDate.setText(DateConverter.convertDateEntityToUi(viewLine.mEtapeEntity.getDate()));
                viewLine.mStepDescription.setText(viewLine.mEtapeEntity.getDescription());
                if (viewLine.mEtapeEntity.getCommentaire().isEmpty()) {
                    viewLine.mStepCommentaire.setVisibility(View.GONE);
                } else {
                    viewLine.mStepCommentaire.setText(viewLine.mEtapeEntity.getCommentaire());
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mEtapeAcheminements.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolderStepParcel extends RecyclerView.ViewHolder {
        final View mView;

        @BindView(R.id.step_date)
        TextView mStepDate;

        @BindView(R.id.step_description)
        TextView mStepDescription;

        @BindView(R.id.step_commentaire)
        TextView mStepCommentaire;

        EtapeAcheminementEntity mEtapeEntity;

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

        EtapeAcheminementEntity mEtapeEntity;

        ViewHolderHeaderStepParcel(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, mView);
        }
    }
}
