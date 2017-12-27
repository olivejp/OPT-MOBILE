package nc.opt.mobile.optmobile.adapter;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.activity.GestionColisActivity;
import nc.opt.mobile.optmobile.fragment.HistoriqueColisFragment;
import nc.opt.mobile.optmobile.glide.GlideRequester;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;
import nc.opt.mobile.optmobile.provider.services.EtapeAcheminementService;
import nc.opt.mobile.optmobile.service.FirebaseService;
import nc.opt.mobile.optmobile.utils.DateConverter;

import static nc.opt.mobile.optmobile.provider.services.ColisService.delete;
import static nc.opt.mobile.optmobile.provider.services.ColisService.realDelete;

/**
 * Created by orlanth23 on 05/10/2017.
 */

public class ColisAdapter extends RecyclerView.Adapter<ColisAdapter.ViewHolderStepParcel> {

    private List<ColisEntity> mColisList;
    private Context mContext;
    private boolean mTwoPane;

    public ColisAdapter(Context context, boolean twoPane) {
        this.mContext = context;
        this.mColisList = new ArrayList<>();
        this.mTwoPane = twoPane;
    }

    @Override
    public ViewHolderStepParcel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_colis, parent, false);
        return new ViewHolderStepParcel(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderStepParcel holder, int position) {
        holder.mColis = mColisList.get(position);
        holder.mIdColis.setText(holder.mColis.getIdColis());
        holder.mParcelDescription.setText(holder.mColis.getDescription());

        if (holder.mColis.getLastUpdate() == null) {
            holder.mStepLastUpdateText.setVisibility(View.GONE);
        } else {
            holder.mStepLastUpdateText.setVisibility(View.VISIBLE);
            holder.mStepLastUpdate.setText(DateConverter.howLongFromNow(holder.mColis.getLastUpdate()));
        }

        if (!holder.mColis.getEtapeAcheminementArrayList().isEmpty()) {
            // On prend la dernière étape
            EtapeEntity etape = holder.mColis.getEtapeAcheminementArrayList().get(holder.mColis.getEtapeAcheminementArrayList().size() - 1);
            holder.mStepLastDate.setText(DateConverter.convertDateEntityToUi(etape.getDate()));
            holder.mStepLastPays.setText(etape.getPays());
            holder.mStepLastDescription.setText(etape.getDescription());
            holder.mStepLastDate.setVisibility(View.VISIBLE);
            holder.mStepLastPays.setVisibility(View.VISIBLE);
            if (etape.getStatus() != null) {
                holder.mStepStatus.setImageResource(EtapeAcheminementService.getStatusDrawable(etape));
            }
        } else {
            holder.mStepLastDate.setText(null);
            holder.mStepLastPays.setText(null);
            holder.mStepLastDate.setVisibility(View.GONE);
            holder.mStepLastPays.setVisibility(View.GONE);
            holder.mStepLastDescription.setText(R.string.no_data_for_parcel);
        }

        if (holder.mColis.getSlug() != null) {
            RequestBuilder<PictureDrawable> requester = GlideRequester.getSvgRequester(mContext, R.drawable.ic_archive_grey_900_48dp, R.drawable.ic_archive_grey_900_48dp);
            requester.load("http://assets.aftership.com/couriers/svg/" + holder.mColis.getSlug() + ".svg")
                    .into(holder.mStepImageColis);
        }


    }

    @Override
    public int getItemCount() {
        return mColisList.size();
    }

    public void setColisList(List<ColisEntity> list) {
        this.mColisList = list;
    }

    class ViewHolderStepParcel extends RecyclerView.ViewHolder {
        final View mView;

        @BindView(R.id.step_id_colis)
        TextView mIdColis;

        @BindView(R.id.step_last_date)
        TextView mStepLastDate;

        @BindView(R.id.step_last_pays)
        TextView mStepLastPays;

        @BindView(R.id.step_last_description)
        TextView mStepLastDescription;

        @BindView(R.id.fab_delete_colis)
        Button mDeleteButton;

        @BindView(R.id.parcel_description)
        TextView mParcelDescription;

        @BindView(R.id.constraint_detail_colis_layout)
        ConstraintLayout mConstraintDetailColisLayout;

        @BindView(R.id.step_last_update)
        TextView mStepLastUpdate;

        @BindView(R.id.step_last_update_text)
        TextView mStepLastUpdateText;

        @BindView(R.id.step_image_colis)
        ImageView mStepImageColis;

        @BindView(R.id.step_status)
        ImageView mStepStatus;

        ColisEntity mColis;

        boolean mDeleteMode;

        private void changeDeleteVisibility() {
            mDeleteButton.setVisibility(mDeleteMode ? View.VISIBLE : View.GONE);
        }

        ViewHolderStepParcel(View view) {
            super(view);
            mView = view;
            mDeleteMode = false;
            ButterKnife.bind(this, mView);

            mConstraintDetailColisLayout.setOnClickListener(v -> {
                // If we aren't in delete mode we call the parcel result search fragment
                // Otherwise we deactivate the delete mode and make the delete button invisible
                if (!mDeleteMode) {
                    HistoriqueColisFragment historiqueColisFragment = HistoriqueColisFragment.newInstance(mColis);
                    if (mTwoPane) {
                        ((AppCompatActivity) mContext).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_detail, historiqueColisFragment, GestionColisActivity.TAG_PARCEL_RESULT_SEARCH_FRAGMENT)
                                .commit();
                    } else {
                        ((AppCompatActivity) mContext).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_master, historiqueColisFragment, GestionColisActivity.TAG_PARCEL_RESULT_SEARCH_FRAGMENT)
                                .addToBackStack(null)
                                .commit();
                    }
                } else {
                    mDeleteMode = !mDeleteMode;
                    changeDeleteVisibility();
                }
            });

            // When we long click on the view, we display Delete button
            mConstraintDetailColisLayout.setOnLongClickListener(v -> {
                mDeleteMode = !mDeleteMode;
                changeDeleteVisibility();
                return true;
            });

            mDeleteButton.setOnClickListener(v -> {
                final String idColisToRemove = mColis.getIdColis();

                // Delete from the memory list
                mColisList.remove(mColis);

                // We leave the delete mode
                mDeleteMode = false;

                // Just pass the deleted boolean to 1
                int nbUpdated = delete(mContext, idColisToRemove);

                // we try to delete the remote
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    FirebaseService.deleteRemoteColis(user.getUid(), idColisToRemove, (databaseError, databaseReference) -> {
                        // If remote has been deleted, we delete local
                        realDelete(mContext, idColisToRemove);
                    });
                }

                Snackbar snackbar;
                if (nbUpdated == 1) {
                    changeDeleteVisibility();
                    snackbar = Snackbar.make(mView, idColisToRemove.concat(mContext.getString(R.string.deleted_from_management)), Snackbar.LENGTH_LONG);
                } else {
                    snackbar = Snackbar.make(mView, idColisToRemove.concat(" suppression échouée."), Snackbar.LENGTH_LONG);
                }
                snackbar.show();
            });
        }
    }
}
