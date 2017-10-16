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
import nc.opt.mobile.optmobile.domain.EtapeAcheminement;
import nc.opt.mobile.optmobile.entity.EtapeAcheminementEntity;

/**
 * Created by orlanth23 on 05/10/2017.
 */

public class EtapeAcheminementAdapter extends RecyclerView.Adapter<EtapeAcheminementAdapter.ViewHolderStepParcel> {

    private final List<EtapeAcheminementEntity> mEtapeAcheminements;

    public EtapeAcheminementAdapter(List<EtapeAcheminementEntity> steps) {
        mEtapeAcheminements = steps;
    }

    public EtapeAcheminementAdapter() {
        mEtapeAcheminements = new ArrayList<>();
    }

    public List<EtapeAcheminementEntity> getmEtapeAcheminements() {
        return mEtapeAcheminements;
    }

    @Override
    public ViewHolderStepParcel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_parcel_search_adapter, parent, false);
        return new ViewHolderStepParcel(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderStepParcel holder, int position) {
        holder.mEtapeAcheminement = mEtapeAcheminements.get(position);
        holder.mStepDate.setText(mEtapeAcheminements.get(position).getDate());
        holder.mStepPays.setText(mEtapeAcheminements.get(position).getPays());
        holder.mStepLocalisation.setText(mEtapeAcheminements.get(position).getLocalisation());
        holder.mStepDescription.setText(mEtapeAcheminements.get(position).getDescription());
        if (mEtapeAcheminements.get(position).getCommentaire().isEmpty()) {
            holder.mStepCommentaire.setVisibility(View.GONE);
        } else {
            holder.mStepCommentaire.setText(mEtapeAcheminements.get(position).getCommentaire());
        }
    }

    @Override
    public int getItemCount() {
        return mEtapeAcheminements.size();
    }

    class ViewHolderStepParcel extends RecyclerView.ViewHolder {
        final View mView;

        @BindView(R.id.step_date)
        TextView mStepDate;

        @BindView(R.id.step_pays)
        TextView mStepPays;

        @BindView(R.id.step_localisation)
        TextView mStepLocalisation;

        @BindView(R.id.step_description)
        TextView mStepDescription;

        @BindView(R.id.step_commentaire)
        TextView mStepCommentaire;

        EtapeAcheminementEntity mEtapeAcheminement;

        ViewHolderStepParcel(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, mView);
        }
    }
}
