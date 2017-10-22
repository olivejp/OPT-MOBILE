package nc.opt.mobile.optmobile.fragment;


import android.content.Context;
import android.database.Cursor;
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
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.service.SyncColisService;

import static nc.opt.mobile.optmobile.provider.services.ColisService.putToContentValues;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchColisFragment extends Fragment {

    @BindView(R.id.edit_id_parcel)
    EditText editIdParcel;

    @BindView(R.id.edit_description_parcel)
    EditText editDescriptionParcel;

    private AppCompatActivity mActivity;

    public static SearchColisFragment newInstance() {
        SearchColisFragment fragment = new SearchColisFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SearchColisFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_search_colis, container, false);
        ButterKnife.bind(this, rootView);

        mActivity.setTitle("Rechercher colis");

        return rootView;
    }

    @OnClick(R.id.fab_search_parcel)
    public void searchParcel(View view) {
        if (!editIdParcel.getText().toString().isEmpty()) {
            String idColis = editIdParcel.getText().toString().toUpperCase();
            ColisEntity colis = new ColisEntity();
            colis.setIdColis(idColis);
            colis.setDescription(editDescriptionParcel.getText().toString());

            // Query our ContentProvider to avoid duplicate
            Cursor cursor = mActivity.getContentResolver().query(OptProvider.ListColis.withId(idColis), null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                Snackbar.make(view, "Objet déjà suivi", Snackbar.LENGTH_LONG).show();
                cursor.close();
            } else {
                // Add the parcel to our ContentProvider
                mActivity.getContentResolver().insert(OptProvider.ListColis.LIST_COLIS, putToContentValues(colis));

                // On lance une première fois le service de synchro
                SyncColisService.launchSynchroByIdColis(mActivity, colis.getIdColis(), false);

                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                Snackbar.make(view, idColis.concat(" ajouté au suivi"), Snackbar.LENGTH_LONG).show();

                // Retour au fragment précédent
                mActivity.getSupportFragmentManager().popBackStack();
            }
        }
    }
}
