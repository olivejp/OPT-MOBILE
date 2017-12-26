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
import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;
import nc.opt.mobile.optmobile.utils.DateConverter;

/**
 * Created by orlanth23 on 05/10/2017.
 */

public class EtapeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = EtapeAdapter.class.getName();

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
        viewHeader.mStepPays.setText(viewHeader.etape.getPays());
        viewHeader.mStepLocalisation.setText(viewHeader.etape.getLocalisation());
        viewHeader.mStepDescription.setText(viewHeader.etape.getDescription());
        if (viewHeader.etape.getCommentaire().isEmpty()) {
            viewHeader.mStepCommentaire.setVisibility(View.GONE);
        } else {
            viewHeader.mStepCommentaire.setText(viewHeader.etape.getCommentaire());
        }
    }

    @Override
    public int getItemCount() {
        return etapes.size();
    }

    class ViewHolderEtape extends RecyclerView.ViewHolder {
        final View mView;

        @BindView(R.id.step_pays)
        TextView mStepPays;

        @BindView(R.id.step_localisation)
        TextView mStepLocalisation;

        @BindView(R.id.step_date)
        TextView mStepDate;

        @BindView(R.id.step_description)
        TextView mStepDescription;

        @BindView(R.id.step_commentaire)
        TextView mStepCommentaire;

        EtapeEntity etape;

        ViewHolderEtape(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, mView);
        }
    }
}
