package nc.opt.mobile.optmobile.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchParcelFragment extends Fragment {

    private static final String TAG_FRAGMENT_SEARCH_PARCEL = "TAG_FRAGMENT_SEARCH_PARCEL";

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

        return rootView;
    }

    @OnClick(R.id.fab_search_parcel)
    public void searchParcel() {
        if (!editIdParcel.getText().toString().isEmpty()) {

            ParcelResultSearchFragment parcelResultSearchFragment = ParcelResultSearchFragment.newInstance(editIdParcel.getText().toString());

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_main, parcelResultSearchFragment, TAG_FRAGMENT_SEARCH_PARCEL)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
