/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nc.opt.mobile.optmobile.ui.adapter;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.DateConverter;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.Utilities;
import nc.opt.mobile.optmobile.database.local.entity.ColisEntity;
import nc.opt.mobile.optmobile.database.local.entity.ColisWithSteps;
import nc.opt.mobile.optmobile.database.local.entity.StepEntity;

public class ColisAdapter extends RecyclerView.Adapter<ColisAdapter.ViewHolderColisAdapter> {

    private List<ColisWithSteps> colisEntities;
    private View.OnClickListener onClickListener;

    public ColisAdapter(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setColisList(final List<ColisWithSteps> colisEntities1) {
        if (colisEntities == null) {
            colisEntities = colisEntities1;
            notifyItemRangeInserted(0, colisEntities1.size());
        } else {
            checkDifferenceBetweenLists(colisEntities1);
        }
    }

    private void checkDifferenceBetweenLists(List<ColisWithSteps> colisEntities1) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return colisEntities.size();
            }

            @Override
            public int getNewListSize() {
                return colisEntities1.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return colisEntities.get(oldItemPosition).colisEntity.getIdColis().equals(colisEntities1.get(newItemPosition).colisEntity.getIdColis());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                ColisWithSteps newColis = colisEntities1.get(newItemPosition);
                ColisWithSteps oldColis = colisEntities.get(oldItemPosition);
                return Objects.equals(newColis.colisEntity.getIdColis(), oldColis.colisEntity.getIdColis())
                        && Objects.equals(newColis.colisEntity.getDescription(), oldColis.colisEntity.getDescription())
                        && Objects.equals(newColis.colisEntity.getSlug(), oldColis.colisEntity.getSlug());
            }
        });
        colisEntities = colisEntities1;
        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ViewHolderColisAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_colis, parent, false);
        return new ViewHolderColisAdapter(rootView);
    }

    private void bindColis(ViewHolderColisAdapter holder, ColisEntity colis) {
        holder.mIdColis.setText(colis.getIdColis());
        holder.mParcelDescription.setText(colis.getDescription());
        if (colis.getLastUpdate() == null) {
            holder.mStepLastUpdateText.setVisibility(View.GONE);
            holder.mStepLastUpdate.setVisibility(View.GONE);
        } else {
            holder.mStepLastUpdateText.setVisibility(View.VISIBLE);
            holder.mStepLastUpdate.setVisibility(View.VISIBLE);
            holder.mStepLastUpdate.setText((colis.getLastUpdate() != null) ? DateConverter.howLongFromNow(colis.getLastUpdate()) : null);
        }
    }

    private void bindEtape(ViewHolderColisAdapter holder, StepEntity step) {
        if (step != null) {
            holder.mStepLastDate.setText(DateConverter.convertDateEntityToUi(step.getDate()));
            holder.mStepLastLocalisation.setText(step.getLocalisation());
            holder.mStepLastDescription.setText(step.getDescription());
        } else {
            holder.mStepLastDate.setVisibility(View.GONE);
            holder.mStepLastLocalisation.setVisibility(View.GONE);
            holder.mStepLastDescription.setText(R.string.NO_STEP_FOR_THIS_PARCEL);
        }
    }

    private void bindImageStatus(ViewHolderColisAdapter holder, ColisEntity colisEntity, StepEntity stepEntity) {
        holder.mStepStatus.setImageResource(R.drawable.ic_status_pending);
        if (colisEntity.isDelivered()) {
            holder.mStepStatus.setImageResource(Utilities.getStatusDrawable(Utilities.DELIVERED));
        } else {
            if (stepEntity != null && stepEntity.getStatus() != null) {
                holder.mStepStatus.setImageResource(Utilities.getStatusDrawable(stepEntity.getStatus()));
            } else {
                holder.mStepStatus.setImageResource(R.drawable.ic_status_pending);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderColisAdapter holder, int position) {
        holder.colisWithSteps = colisEntities.get(position);
        holder.mConstraintDetailColisLayout.setTag(holder.colisWithSteps);
        ColisEntity colis = holder.colisWithSteps.colisEntity;
        List<StepEntity> list = holder.colisWithSteps.stepEntityList;
        StepEntity lastEtape = null;
        if (list != null && !list.isEmpty()) {
            lastEtape = list.get(list.size() - 1);
        }
        bindColis(holder, colis);
        bindEtape(holder, lastEtape);
        bindImageStatus(holder, colis, lastEtape);
    }


    @Override
    public int getItemCount() {
        return colisEntities == null ? 0 : colisEntities.size();
    }

    public class ViewHolderColisAdapter extends RecyclerView.ViewHolder {

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

        @BindView(R.id.constraint_delete_layout)
        ConstraintLayout mConstraintDeleted;

        @BindView(R.id.constraint_delivered_layout)
        ConstraintLayout mConstraintDelivered;

        @BindView(R.id.step_last_update)
        TextView mStepLastUpdate;

        @BindView(R.id.step_last_update_text)
        TextView mStepLastUpdateText;

        @BindView(R.id.step_slug)
        ImageView mStepSlug;

        @BindView(R.id.step_status)
        ImageView mStepStatus;

        ColisWithSteps colisWithSteps;

        ViewHolderColisAdapter(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mConstraintDetailColisLayout.setOnClickListener(onClickListener);
        }

        public ConstraintLayout getmConstraintDetailColisLayout() {
            return mConstraintDetailColisLayout;
        }

        public ConstraintLayout getmConstraintDeletedLayout() {
            return mConstraintDeleted;
        }

        public ConstraintLayout getmConstraintDeliveredLayout() {
            return mConstraintDelivered;
        }

        public ColisWithSteps getColisWithSteps() {
            return colisWithSteps;
        }
    }
}
