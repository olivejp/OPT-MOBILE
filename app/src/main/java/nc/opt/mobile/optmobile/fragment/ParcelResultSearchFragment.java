package nc.opt.mobile.optmobile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import nc.opt.mobile.optmobile.adapter.EtapeAcheminementAdapter;
import nc.opt.mobile.optmobile.domain.Colis;
import nc.opt.mobile.optmobile.utils.Constants;
import nc.opt.mobile.optmobile.utils.HtmlTransformer;
import nc.opt.mobile.optmobile.utils.RequestQueueSingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParcelResultSearchFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener {

    private static final String TAG = ParcelResultSearchFragment.class.getName();
    private static final String ARG_ID_PARCEL = "ARG_ID_PARCEL";

    private String mIdParcel;
    private EtapeAcheminementAdapter mEtapeAcheminementAdapter;
    private RequestQueueSingleton mRequestQueueSingleton;
    private StringRequest mStringRequest;

    @BindView(R.id.recycler_parcel_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.text_object_not_found)
    TextView mTextObjectNotFound;

    public static ParcelResultSearchFragment newInstance(@NotNull String idParcel) {
        ParcelResultSearchFragment fragment = new ParcelResultSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID_PARCEL, idParcel);
        fragment.setArguments(args);
        return fragment;
    }

    public ParcelResultSearchFragment() {
        // Required empty public constructor
    }

    private void populateAdapter(Colis colis){
        mEtapeAcheminementAdapter.getmEtapeAcheminements().clear();
        mEtapeAcheminementAdapter.getmEtapeAcheminements().addAll(colis.getEtapeAcheminementArrayList());
        mEtapeAcheminementAdapter.notifyDataSetChanged();
    }

    private void transformHtmlToObject(String htmlToTransform) {
        Colis colis = new Colis();
        try {
            int transformResult = HtmlTransformer.getParcelResultFromHtml(htmlToTransform, colis);
            switch (transformResult) {
                case HtmlTransformer.RESULT_SUCCESS:
                    mTextObjectNotFound.setVisibility(View.GONE);
                    populateAdapter(colis);
                    break;
                case HtmlTransformer.RESULT_NO_ITEM_FOUND:
                    mTextObjectNotFound.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        } catch (HtmlTransformer.HtmlTransformerException e) {
            Log.e(TAG, e.getMessage(), e);
            Toast.makeText(getActivity(), getActivity().getString(R.string.page_malformed), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIdParcel = getArguments().getString(ARG_ID_PARCEL);

        String url = Constants.URL_SUIVI_COLIS
                .concat(Constants.URL_SUIVI_SERVICE_OPT)
                .concat("?itemId=")
                .concat(mIdParcel)
                .concat("&Submit=Envoyer");

        mRequestQueueSingleton = RequestQueueSingleton.getInstance(getActivity().getApplicationContext());

        // Request a string response from the provided URL.
        mStringRequest = new StringRequest(Request.Method.GET, url, this, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result_search_parcel, container, false);
        ButterKnife.bind(this, rootView);

        // Changement du titre
        getActivity().setTitle(mIdParcel);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // Ajout d'une barre separatrice entre les elements
        mRecyclerView.addItemDecoration(new
                DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        // Création d'un nouvel adapter
        mEtapeAcheminementAdapter = new EtapeAcheminementAdapter();

        mRecyclerView.setAdapter(mEtapeAcheminementAdapter);

        // Add the request to the RequestQueue.
        mRequestQueueSingleton.addToRequestQueue(mStringRequest);

        return rootView;
    }

    @Override
    public void onResponse(String response) {
        try {
            String newStr = URLDecoder.decode(URLEncoder.encode(response, Constants.ENCODING_ISO), Constants.ENCODING_UTF_8);
            transformHtmlToObject(newStr);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage(), e);
            Toast.makeText(getActivity(), getActivity().getString(R.string.page_malformed), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, error.getMessage(), error);
        Toast.makeText(getActivity(), getActivity().getString(R.string.page_malformed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_ID_PARCEL, mIdParcel);
    }
}
