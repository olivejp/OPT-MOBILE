package nc.opt.mobile.optmobile.adapter;

import android.support.constraint.ConstraintLayout;
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
import nc.opt.mobile.optmobile.provider.entity.ActualiteEntity;
import nc.opt.mobile.optmobile.utils.DateConverter;

/**
 * Created by orlanth23 on 25/10/2017.
 */

public class ActualiteAdapter extends RecyclerView.Adapter<ActualiteAdapter.ViewHolderActualite> {

    private List<ActualiteEntity> mActualites;

    public ActualiteAdapter(List<ActualiteEntity> actualites) {
        mActualites = actualites;
    }

    public ActualiteAdapter() {
        mActualites = new ArrayList<>();
    }

    public void setmActualites(List<ActualiteEntity> mActualites) {
        this.mActualites = mActualites;
    }

    public List<ActualiteEntity> getmActualites() {
        return mActualites;
    }

    @Override
    public ViewHolderActualite onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_actualite, parent, false);
        return new ViewHolderActualite(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderActualite holder, int position) {
        holder.mActualite = mActualites.get(position);
        holder.mDate.setText(DateConverter.convertDateEntityToUi(holder.mActualite.getDate()));
        holder.mTitre.setText(holder.mActualite.getTitre());
        holder.mContenu.setText(holder.mActualite.getContenu());
    }

    @Override
    public int getItemCount() {
        return mActualites.size();
    }

    class ViewHolderActualite extends RecyclerView.ViewHolder {
        final View mView;

        @BindView(R.id.titre_actualite)
        TextView mTitre;

        @BindView(R.id.date_actualite)
        TextView mDate;

        @BindView(R.id.contenu_actualite)
        TextView mContenu;

        @BindView(R.id.constraint_titre)
        ConstraintLayout mConstraintTitre;

        ActualiteEntity mActualite;

        ViewHolderActualite(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, mView);
        }
    }
}
