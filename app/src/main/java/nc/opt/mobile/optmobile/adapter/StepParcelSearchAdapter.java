package nc.opt.mobile.optmobile.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.domain.StepParcelSearch;

/**
 * Created by orlanth23 on 05/10/2017.
 */

public class StepParcelSearchAdapter extends RecyclerView.Adapter<StepParcelSearchAdapter.ViewHolderStepParcel> {

    private final List<StepParcelSearch> mStepParcelSearchs;

    public StepParcelSearchAdapter(List<StepParcelSearch> steps) {
        mStepParcelSearchs = steps;
    }

    @Override
    public ViewHolderStepParcel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_parcel_search_adapter, parent, false);
        return new ViewHolderStepParcel(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderStepParcel holder, int position) {
        holder.mStepParcelSearch = mStepParcelSearchs.get(position);
        holder.mStepDate.setText(mStepParcelSearchs.get(position).getDate());
        holder.mStepPays.setText(mStepParcelSearchs.get(position).getPays());
        holder.mStepLocalisation.setText(mStepParcelSearchs.get(position).getLocalisation());
        holder.mStepDescription.setText(mStepParcelSearchs.get(position).getDescription());
        holder.mStepCommentaire.setText(mStepParcelSearchs.get(position).getCommentaire());
    }

    @Override
    public int getItemCount() {
        return mStepParcelSearchs.size();
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

        StepParcelSearch mStepParcelSearch;

        ViewHolderStepParcel(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, mView);
        }
    }
}
