package nc.opt.mobile.optmobile.adapter;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestBuilder;

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
import nc.opt.mobile.optmobile.utils.DateConverter;

/**
 * Created by orlanth23 on 05/10/2017.
 */

public class ColisAdapter extends RecyclerView.Adapter<ColisAdapter.ViewHolderStepParcel> {

    private List<ColisEntity> mColisList;
    private Context mContext;
    private boolean mTwoPane;
    private RequestBuilder<PictureDrawable> requester;

    public ColisAdapter(Context context, boolean twoPane) {
        this.mContext = context;
        this.mColisList = new ArrayList<>();
        this.mTwoPane = twoPane;
        this.requester = GlideRequester.getSvgRequester(mContext, R.drawable.ic_archive_grey_900_48dp, R.drawable.ic_archive_grey_900_48dp);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ColisEntity colis = (ColisEntity) v.getTag();
            HistoriqueColisFragment historiqueColisFragment = HistoriqueColisFragment.newInstance(colis);
            FragmentTransaction ft = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
            if (mTwoPane) {
                ft.replace(R.id.frame_detail, historiqueColisFragment, GestionColisActivity.TAG_PARCEL_RESULT_SEARCH_FRAGMENT).commit();
            } else {
                ft.replace(R.id.frame_master, historiqueColisFragment, GestionColisActivity.TAG_PARCEL_RESULT_SEARCH_FRAGMENT).addToBackStack(null).commit();
            }
        }
    };


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
        holder.mConstraintDetailColisLayout.setTag(holder.mColis);

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
                holder.mStepStatus.setImageResource(EtapeAcheminementService.getStatusDrawable(etape.getStatus()));
            } else {
                holder.mStepStatus.setImageResource(R.drawable.ic_status_pending);
            }
        } else {
            holder.mStepLastDate.setText(null);
            holder.mStepLastPays.setText(null);
            holder.mStepLastDate.setVisibility(View.GONE);
            holder.mStepLastPays.setVisibility(View.GONE);
            holder.mStepLastDescription.setText(R.string.no_data_for_parcel);
        }

        if (holder.mColis.getSlug() != null) {
            this.requester.load("http://assets.aftership.com/couriers/svg/" + holder.mColis.getSlug() + ".svg")
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

    public class ViewHolderStepParcel extends RecyclerView.ViewHolder {
        View mView;

        @BindView(R.id.step_id_colis)
        TextView mIdColis;

        @BindView(R.id.step_last_date)
        TextView mStepLastDate;

        @BindView(R.id.step_last_pays)
        TextView mStepLastPays;

        @BindView(R.id.step_last_description)
        TextView mStepLastDescription;

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

        @BindView(R.id.relative_delete_layout)
        RelativeLayout relativeLayout;

        ColisEntity mColis;

        public ColisEntity getmColis() {
            return mColis;
        }

        public ConstraintLayout getmConstraintDetailColisLayout() {
            return mConstraintDetailColisLayout;
        }

        ViewHolderStepParcel(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, mView);
            mConstraintDetailColisLayout.setOnClickListener(onClickListener);
        }
    }
}
