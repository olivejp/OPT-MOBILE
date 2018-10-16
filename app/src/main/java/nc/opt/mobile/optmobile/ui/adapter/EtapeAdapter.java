package nc.opt.mobile.optmobile.ui.adapter;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.DateConverter;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.Utilities;
import nc.opt.mobile.optmobile.database.local.entity.StepEntity;

/**
 * Created by orlanth23 on 05/10/2017.
 */

public class EtapeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StepEntity> mEtapes;

    public EtapeAdapter() {
        mEtapes = new ArrayList<>();
    }

    public void setEtapes(final List<StepEntity> etapes) {
        if (mEtapes == null) {
            mEtapes = etapes;
            notifyItemRangeInserted(0, etapes.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mEtapes.size();
                }

                @Override
                public int getNewListSize() {
                    return etapes.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mEtapes.get(oldItemPosition).getIdColis().equals(etapes.get(newItemPosition).getIdColis());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    StepEntity newEtape = etapes.get(newItemPosition);
                    StepEntity oldEtape = mEtapes.get(oldItemPosition);
                    return Objects.equals(newEtape.getIdEtapeAcheminement(), oldEtape.getIdEtapeAcheminement())
                            && Objects.equals(newEtape.getIdColis(), oldEtape.getIdColis())
                            && Objects.equals(newEtape.getDate(), oldEtape.getDate())
                            && Objects.equals(newEtape.getCommentaire(), oldEtape.getCommentaire())
                            && Objects.equals(newEtape.getDescription(), oldEtape.getDescription())
                            && Objects.equals(newEtape.getLocalisation(), oldEtape.getLocalisation());
                }
            });
            mEtapes = etapes;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_etape, parent, false);
        return new ViewHolderEtape(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ViewHolderEtape viewHeader = (ViewHolderEtape) holder;
        viewHeader.etape = mEtapes.get(position);
        viewHeader.mStepDate.setText(DateConverter.convertDateEntityToUi(viewHeader.etape.getDate()));
        viewHeader.mStepLocalisation.setText(viewHeader.etape.getLocalisation());
        viewHeader.mStepDescription.setText(viewHeader.etape.getDescription());
        if (viewHeader.etape.getCommentaire().isEmpty()) {
            viewHeader.mStepCommentaire.setVisibility(View.GONE);
        } else {
            viewHeader.mStepCommentaire.setText(viewHeader.etape.getCommentaire());
        }

        // Change status image.
        viewHeader.mStepStatus.setImageResource(Utilities.getStatusDrawable(viewHeader.etape.getStatus()));
    }

    @Override
    public int getItemCount() {
        return mEtapes.size();
    }

    class ViewHolderEtape extends RecyclerView.ViewHolder {

        @BindView(R.id.step_localisation)
        TextView mStepLocalisation;

        @BindView(R.id.step_date)
        TextView mStepDate;

        @BindView(R.id.step_description)
        TextView mStepDescription;

        @BindView(R.id.step_commentaire)
        TextView mStepCommentaire;

        @BindView(R.id.img_step_status)
        ImageView mStepStatus;

        StepEntity etape;

        ViewHolderEtape(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
