package nc.opt.mobile.optmobile.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.Constants;
import nc.opt.mobile.optmobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchParcelFragment extends Fragment {

    private static final String TAG_WEB_VIEW_FRAGMENT_SEARCH_PARCEL = "TAG_WEB_VIEW_FRAGMENT_SEARCH_PARCEL";

    @BindView(R.id.edit_id_parcel)
    EditText editIdParcel;

    @BindView(R.id.fab_search_parcel)
    FloatingActionButton fabSearchParcel;

    @BindView(R.id.layout_search)
    RelativeLayout layoutSearch;

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

        return rootView;
    }

    @OnClick(R.id.fab_search_parcel)
    public void searchParcel() {
        if (!editIdParcel.getText().toString().isEmpty()) {

            String url = Constants.URL_SUIVI_COLIS
                    .concat(Constants.URL_SUIVI_SERVICE_OPT)
                    .concat("?itemId=")
                    .concat(editIdParcel.getText().toString())
                    .concat("&Submit=Envoyer");

            WebViewFragment webViewFragment = WebViewFragment.newInstance(url);

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_main, webViewFragment, TAG_WEB_VIEW_FRAGMENT_SEARCH_PARCEL)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
