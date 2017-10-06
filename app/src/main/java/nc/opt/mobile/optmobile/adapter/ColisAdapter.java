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
import nc.opt.mobile.optmobile.domain.Colis;
import nc.opt.mobile.optmobile.domain.EtapeAcheminement;

/**
 * Created by orlanth23 on 05/10/2017.
 */

public class ColisAdapter extends RecyclerView.Adapter<ColisAdapter.ViewHolderStepParcel> {

    private final List<Colis> mColisList;

    public ColisAdapter(List<Colis> colisList) {
        mColisList = colisList;
    }

    public ColisAdapter() {
        mColisList = new ArrayList<>();
    }

    public List<Colis> getmColisList() {
        return mColisList;
    }

    @Override
    public ViewHolderStepParcel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.colis_adapter, parent, false);
        return new ViewHolderStepParcel(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderStepParcel holder, int position) {
        holder.mColis = mColisList.get(position);
        holder.mIdColis.setText(holder.mColis.getIdColis());
        if (!holder.mColis.getEtapeAcheminementArrayList().isEmpty()) {
            // On prend la dernière étape
            EtapeAcheminement etapeAcheminement = holder.mColis.getEtapeAcheminementArrayList().get(holder.mColis.getEtapeAcheminementArrayList().size()-1);
            holder.mStepLastDate.setText(etapeAcheminement.getDate());
            holder.mStepLastPays.setText(etapeAcheminement.getPays());
            holder.mStepLastLocalisation.setText(etapeAcheminement.getLocalisation());
            holder.mStepLastDescription.setText(etapeAcheminement.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return mColisList.size();
    }

    class ViewHolderStepParcel extends RecyclerView.ViewHolder {
        final View mView;

        @BindView(R.id.text_id_colis)
        TextView mIdColis;

        @BindView(R.id.step_last_date)
        TextView mStepLastDate;

        @BindView(R.id.step_last_pays)
        TextView mStepLastPays;

        @BindView(R.id.step_last_localisation)
        TextView mStepLastLocalisation;

        @BindView(R.id.step_last_description)
        TextView mStepLastDescription;

        Colis mColis;

        ViewHolderStepParcel(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, mView);
        }
    }
}
