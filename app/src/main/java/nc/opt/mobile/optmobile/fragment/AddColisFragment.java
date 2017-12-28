package nc.opt.mobile.optmobile.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.services.ActualiteService;
import nc.opt.mobile.optmobile.provider.services.ColisService;
import nc.opt.mobile.optmobile.service.FirebaseService;
import nc.opt.mobile.optmobile.service.SyncColisService;

/**
 *
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_colis, container, false);
        ButterKnife.bind(this, rootView);
        mActivity.setTitle(getString(R.string.add_colis));
        return rootView;
    }

    @OnClick(R.id.fab_search_parcel)
    public void searchParcel(View view) {
        if (!editIdParcel.getText().toString().isEmpty()) {



            // Get the idColis from the view
            String idColis = editIdParcel.getText().toString().toUpperCase();

            // Query our ContentProvider to avoid duplicate
            if (ColisService.exist(mActivity, idColis, true)) {
                Snackbar.make(view, R.string.colis_already_added, Snackbar.LENGTH_LONG).show();
            } else {
                // Colis was already in our local DB but marked as deleted. We call real delete and reinsert it.
                if (ColisService.exist(mActivity, idColis, false)) {
                    ColisService.realDelete(mActivity, idColis);
                }
                // Add the colis to our ContentProvider
                ColisEntity colis = new ColisEntity();
                colis.setIdColis(idColis);
                colis.setDescription(editDescriptionParcel.getText().toString());
                colis.setDeleted(0);
                long insertResult = ColisService.insert(mActivity, colis);
                if (insertResult != -1) {

                    // On lance une première fois le service de synchro
                    SyncColisService.launchSynchroByIdColis(mActivity, idColis, false);

                    Snackbar.make(view, String.format(getString(R.string.colis_added), idColis), Snackbar.LENGTH_LONG).show();

                    // Ajout d'une actualité
                    String titre = String.format(getString(R.string.colis_added), idColis);
                    String contenu = String.format(getString(R.string.insert_contenu_actualite), idColis);
                    ActualiteService.insertActualite(mActivity, titre, contenu, true);

                    // Try to send to the remote DB
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        FirebaseService.createRemoteDatabase(user.getUid(), ColisService.listFromProvider(getActivity(), true), null);
                    }
                }
                mActivity.finish();
            }
        }
    }
}
