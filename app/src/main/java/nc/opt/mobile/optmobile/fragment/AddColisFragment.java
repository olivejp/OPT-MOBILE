package nc.opt.mobile.optmobile.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.provider.entity.ActualiteEntity;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.services.ActualiteService;
import nc.opt.mobile.optmobile.provider.services.ColisService;
import nc.opt.mobile.optmobile.service.SyncColisService;
import nc.opt.mobile.optmobile.utils.DateConverter;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddColisFragment extends Fragment {

    @BindView(R.id.edit_id_parcel)
    EditText editIdParcel;

    @BindView(R.id.edit_description_parcel)
    EditText editDescriptionParcel;

    private AppCompatActivity mActivity;

    public AddColisFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_colis, container, false);
        ButterKnife.bind(this, rootView);

        mActivity.setTitle(getString(R.string.add_colis));

        return rootView;
    }

    @OnClick(R.id.fab_search_parcel)
    public void searchParcel(View view) {
        if (!editIdParcel.getText().toString().isEmpty()) {
            String idColis = editIdParcel.getText().toString().toUpperCase();
            ColisEntity colis = new ColisEntity();
            colis.setIdColis(idColis);
            colis.setDescription(editDescriptionParcel.getText().toString());
            colis.setDeleted(0);

            // Query our ContentProvider to avoid duplicate
            if (ColisService.exist(mActivity, idColis)) {
                Snackbar.make(view, R.string.colis_already_added, Snackbar.LENGTH_LONG).show();
            } else {
                // Add the colis to our ContentProvider
                ColisService.insert(mActivity, colis);

                // On lance une première fois le service de synchro
                SyncColisService.launchSynchroByIdColis(mActivity, idColis, false);

                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                Snackbar.make(view, idColis.concat(" " + getString(R.string.colis_added)), Snackbar.LENGTH_LONG).show();

                // Ajout d'une actualité
                ActualiteEntity actualiteEntity = new ActualiteEntity();
                actualiteEntity.setTitre(idColis + " ajouté au suivi");
                actualiteEntity.setType("1");
                actualiteEntity.setContenu("Vous avez ajouté " + idColis + " au suivi des colis. Vous serez notifié de son évolution lors de son acheminement.");
                actualiteEntity.setDate(DateConverter.getNowEntity());
                actualiteEntity.setDismissed("0");
                actualiteEntity.setDismissable("1");
                ActualiteService.insertActualite(mActivity, actualiteEntity);

                mActivity.finish();
            }
        }
    }
}
