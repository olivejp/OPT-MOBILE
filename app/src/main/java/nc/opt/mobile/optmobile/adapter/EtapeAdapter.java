package nc.opt.mobile.optmobile.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;
import nc.opt.mobile.optmobile.provider.services.EtapeService;
import nc.opt.mobile.optmobile.utils.DateConverter;

/**
 * Created by orlanth23 on 05/10/2017.
 */

public class EtapeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EtapeEntity> etapes;

    public EtapeAdapter() {
        etapes = new ArrayList<>();
    }

    public void setEtapes(List<EtapeEntity> etapes) {
        this.etapes = etapes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_header_etape, parent, false);
        return new ViewHolderEtape(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ViewHolderEtape viewHeader = (ViewHolderEtape) holder;
        viewHeader.etape = etapes.get(position);
        viewHeader.mStepDate.setText(DateConverter.convertDateEntityToUi(viewHeader.etape.getDate()));
        viewHeader.mStepLocalisation.setText(viewHeader.etape.getLocalisation());
        viewHeader.mStepDescription.setText(viewHeader.etape.getDescription());
        if (viewHeader.etape.getCommentaire().isEmpty()) {
            viewHeader.mStepCommentaire.setVisibility(View.GONE);
        } else {
            viewHeader.mStepCommentaire.setText(viewHeader.etape.getCommentaire());
        }

        // We remove the line if we are on the last element.
        if (position == etapes.size() - 1) {
            ((ViewHolderEtape) holder).mStepLine.setVisibility(View.GONE);
        }

        viewHeader.mStepStatus.setImageResource(EtapeService.getStatusDrawable(viewHeader.etape.getStatus()));
    }

    @Override
    public int getItemCount() {
        return etapes.size();
    }

    class ViewHolderEtape extends RecyclerView.ViewHolder {
        final View mView;

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

        @BindView(R.id.img_step_line)
        CardView mStepLine;

        EtapeEntity etape;

        ViewHolderEtape(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, mView);
        }
    }
}
