package nc.opt.mobile.optmobile.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.Utils.Constants;
import nc.opt.mobile.optmobile.Utils.HtmlTransformer;
import nc.opt.mobile.optmobile.Utils.RequestQueueSingleton;
import nc.opt.mobile.optmobile.adapter.StepParcelSearchAdapter;
import nc.opt.mobile.optmobile.domain.ParcelSearchResult;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParcelResultSearchFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener {

    private static final String TAG = ParcelResultSearchFragment.class.getName();
    private static final String ARG_ID_PARCEL = "ARG_ID_PARCEL";

    private String mIdParcel;

    private String mUrl;

    @BindView(R.id.recycler_parcel_list)
    private RecyclerView mRecyclerView;


    public static ParcelResultSearchFragment newInstance(@NotNull @NonNull String idParcel) {
        ParcelResultSearchFragment fragment = new ParcelResultSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID_PARCEL, idParcel);
        fragment.setArguments(args);
        return fragment;
    }

    public ParcelResultSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIdParcel = getArguments().getString(ARG_ID_PARCEL);

        mUrl = Constants.URL_SUIVI_COLIS
                .concat(Constants.URL_SUIVI_SERVICE_OPT)
                .concat("?itemId=")
                .concat(mIdParcel)
                .concat("&Submit=Envoyer");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result_search_parcel, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        RequestQueueSingleton mRequestQueueSingleton = RequestQueueSingleton.getInstance(getActivity().getApplicationContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, mUrl, this, this);

        // Add the request to the RequestQueue.
        mRequestQueueSingleton.addToRequestQueue(stringRequest);

        return rootView;
    }

    @Override
    public void onResponse(String response) {
        try {
            String newStr = URLDecoder.decode(URLEncoder.encode(response, "iso8859-1"), "UTF-8");

            ParcelSearchResult parcelSearchResult = HtmlTransformer.transform(newStr, mIdParcel);

            StepParcelSearchAdapter stepParcelSearchAdapter = new StepParcelSearchAdapter(parcelSearchResult.getStepParcelSearchArrayList());

            mRecyclerView.setAdapter(stepParcelSearchAdapter);

        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage(), e);
            Toast.makeText(getActivity(), "Récupération d'une erreur sérieuse.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, error.getMessage(), error);
        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_ID_PARCEL, mIdParcel);
    }
}
