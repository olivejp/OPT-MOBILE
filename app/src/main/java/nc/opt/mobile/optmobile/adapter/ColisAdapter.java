package nc.opt.mobile.optmobile.adapter;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.gfx.GlideRequester;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;
import nc.opt.mobile.optmobile.provider.services.EtapeService;
import nc.opt.mobile.optmobile.utils.DateConverter;

import static nc.opt.mobile.optmobile.utils.Constants.AFTERSHIP_COURIER_EXTENSION;
import static nc.opt.mobile.optmobile.utils.Constants.URL_AFTERSHIP_COURIER;

/**
 * Created by orlanth23 on 05/10/2017.
 */

public class ColisAdapter extends RecyclerView.Adapter<ColisAdapter.ViewHolderStepParcel> {

    private List<ColisEntity> mColisList;
    private Context mContext;
    private boolean mTwoPane;
    private RequestBuilder<PictureDrawable> requester;
    private View.OnClickListener onClickListener;

    public ColisAdapter(Context context, boolean twoPane, View.OnClickListener onClickListener) {
        this.mContext = context;
        this.mColisList = new ArrayList<>();
        this.mTwoPane = twoPane;
        this.onClickListener = onClickListener;
        this.requester = GlideRequester.getSvgRequester(mContext, R.drawable.ic_archive_grey_900_48dp, R.drawable.ic_archive_grey_900_48dp);
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
        holder.mConstraintDetailColisLayout.setTag(holder.mColis);
        holder.mStepStatus.setImageResource(R.drawable.ic_status_pending);

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
            holder.mStepLastLocalisation.setText(etape.getLocalisation());
            holder.mStepLastDescription.setText(etape.getDescription());
            holder.mStepLastDate.setVisibility(View.VISIBLE);
            holder.mStepLastLocalisation.setVisibility(View.VISIBLE);
            if (etape.getStatus() != null) {
                holder.mStepStatus.setImageResource(EtapeService.getStatusDrawable(etape.getStatus()));
            }
        } else {
            holder.mStepLastDate.setText(null);
            holder.mStepLastLocalisation.setText(null);
            holder.mStepLastDate.setVisibility(View.GONE);
            holder.mStepLastLocalisation.setVisibility(View.GONE);
            holder.mStepLastDescription.setText(R.string.no_data_for_parcel);
        }

        if (holder.mColis.getSlug() != null) {
            holder.mStepSlug.setVisibility(View.VISIBLE);
            this.requester.load(URL_AFTERSHIP_COURIER + holder.mColis.getSlug() + AFTERSHIP_COURIER_EXTENSION)
                    .into(holder.mStepSlug);
        } else {
            holder.mStepSlug.setVisibility(View.GONE);
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

        @BindView(R.id.step_last_localisation)
        TextView mStepLastLocalisation;

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

        @BindView(R.id.step_slug)
        ImageView mStepSlug;

        @BindView(R.id.step_status)
        ImageView mStepStatus;

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
