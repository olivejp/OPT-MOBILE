package nc.opt.mobile.optmobile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.RequestQueueSingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener {

    private static final String TAG = WebViewFragment.class.getName();
    private static final String ARG_URL = "ARG_URL";
    private String mUrl;
    private RequestQueueSingleton mRequestQueueSingleton;

    @BindView(R.id.web_view)
    WebView webView;

    public static WebViewFragment newInstance(String url) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString(ARG_URL);

        mRequestQueueSingleton = RequestQueueSingleton.getInstance(getActivity().getApplicationContext());

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
        String mimeType = "text/html; charset=UTF-8";
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadData(response, mimeType, null);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getActivity(), "Unabled to find the URL", Toast.LENGTH_LONG).show();
    }
}
