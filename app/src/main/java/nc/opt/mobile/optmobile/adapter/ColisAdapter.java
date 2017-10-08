package nc.opt.mobile.optmobile.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.activity.MainActivity;
import nc.opt.mobile.optmobile.domain.Colis;
import nc.opt.mobile.optmobile.domain.EtapeAcheminement;
import nc.opt.mobile.optmobile.fragment.ParcelResultSearchFragment;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;

/**
 * Created by orlanth23 on 05/10/2017.
 */

public class ColisAdapter extends RecyclerView.Adapter<ColisAdapter.ViewHolderStepParcel> {

    private final List<Colis> mColisList;
    private Context mContext;

    public ColisAdapter(Context context, List<Colis> colisList) {
        mContext = context;
        mColisList = colisList;
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

        @BindView(R.id.fab_delete_colis)
        ImageButton mDeleteButton;

        @BindView(R.id.linear_detail_layout)
        LinearLayout linearDetailLayout;

        @BindView(R.id.relative_delete_layout)
        RelativeLayout relativeDeleteLayout;

        Colis mColis;

        boolean mDeleteMode;

        private void changeDeleteVisibility(){
            relativeDeleteLayout.setVisibility(mDeleteMode ? View.VISIBLE : View.GONE);
        }

        ViewHolderStepParcel(View view) {
            super(view);
            mView = view;
            mDeleteMode = false;
            ButterKnife.bind(this, mView);

            linearDetailLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // If we aren't in delete mode we call the parcel result search fragment
                    // Otherwise we deactivate the delete mode and make the delete button invisible
                    if (!mDeleteMode) {
                        ParcelResultSearchFragment parcelResultSearchFragment = ParcelResultSearchFragment.newInstance(mColis.getIdColis());
                        ((AppCompatActivity) mContext).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_main, parcelResultSearchFragment, MainActivity.TAG_PARCEL_RESULT_SEARCH_FRAGMENT)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        mDeleteMode = !mDeleteMode;
                        changeDeleteVisibility();
                    }
                }
            });

            // When we long click on the view, we display Delete button
            linearDetailLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mDeleteMode = !mDeleteMode;
                    changeDeleteVisibility();
                    return true;
                }
            });

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Delete from the memory list
                    mColisList.remove(mColis);

                    // Remove from the content provider
                    ProviderUtilities.deleteColis(mContext, mColis.getIdColis());

                    // Notify the adapter
                    ColisAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }
}
