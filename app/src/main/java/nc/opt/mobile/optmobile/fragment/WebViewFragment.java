package nc.opt.mobile.optmobile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
import nc.opt.mobile.optmobile.utils.RequestQueueSingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener {

    private static final String TAG = WebViewFragment.class.getName();
    private static final String ARG_URL = "ARG_URL";
    private static final String ARG_ID_PARCEL = "ARG_ID_PARCEL";
    private static final String MIME_TYPE = "text/html; charset=UTF-8";

    private String mUrl;
    private String mIdParcel;

    @BindView(R.id.web_view)
    WebView webView;

    public static WebViewFragment newInstance(@NotNull String url, @Nullable String idParcel) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        if (idParcel != null) {
            args.putString(ARG_ID_PARCEL, idParcel);
        }
        fragment.setArguments(args);
        return fragment;
    }

    public WebViewFragment() {
        // Required empty public constructor
    }

    private void setWebView(String newStr) {
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadData(newStr, MIME_TYPE, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUrl = getArguments().getString(ARG_URL);
        if (getArguments().containsKey(ARG_ID_PARCEL)) {
            mIdParcel = getArguments().getString(ARG_ID_PARCEL);
        }

        RequestQueueSingleton mRequestQueueSingleton = RequestQueueSingleton.getInstance(getActivity().getApplicationContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, mUrl, this, this);

        // Add the request to the RequestQueue.
        mRequestQueueSingleton.addToRequestQueue(stringRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onResponse(String response) {
        try {
            String newStr = URLDecoder.decode(URLEncoder.encode(response, "iso8859-1"), "UTF-8");

            setWebView(newStr);

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
        outState.putString(ARG_URL, mUrl);
        outState.putString(ARG_ID_PARCEL, mIdParcel);
    }
}
