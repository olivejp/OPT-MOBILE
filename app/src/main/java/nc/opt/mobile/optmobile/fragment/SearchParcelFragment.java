package nc.opt.mobile.optmobile.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.domain.Colis;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchParcelFragment extends Fragment {

    @BindView(R.id.edit_id_parcel)
    EditText editIdParcel;

    public static SearchParcelFragment newInstance() {
        SearchParcelFragment fragment = new SearchParcelFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SearchParcelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_parcel, container, false);
        ButterKnife.bind(this, rootView);

        getActivity().setTitle("Rechercher colis");

        return rootView;
    }

    @OnClick(R.id.fab_search_parcel)
    public void searchParcel(View view) {
        if (!editIdParcel.getText().toString().isEmpty()) {
            Colis colis = new Colis();
            colis.setIdColis(editIdParcel.getText().toString());

            // Query our ContentProvider to avoid duplicate
            Cursor cursor = getActivity().getContentResolver().query(OptProvider.ListColis.withId(editIdParcel.getText().toString()), null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                Snackbar.make(view, "Objet déjà suivi", Snackbar.LENGTH_LONG).show();
                cursor.close();
            } else {
                // Add the parcel to our ContentProvider
                getActivity().getContentResolver().insert(OptProvider.ListColis.LIST_COLIS, ProviderUtilities.putColisToContentValues(colis));

                // Retour au fragment précédent
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }
}
