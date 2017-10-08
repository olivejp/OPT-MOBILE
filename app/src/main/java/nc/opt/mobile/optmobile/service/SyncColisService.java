package nc.opt.mobile.optmobile.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import nc.opt.mobile.optmobile.domain.Colis;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;
import nc.opt.mobile.optmobile.utils.Constants;
import nc.opt.mobile.optmobile.utils.HtmlTransformer;
import nc.opt.mobile.optmobile.utils.RequestQueueSingleton;

public class SyncColisService extends IntentService {

    private static final String TAG = SyncColisService.class.getName();

    public static final String ARG_ID_COLIS = "ARG_ID_COLIS";
    public static final String ARG_ACTION = "ARG_ACTION";
    public static final String ARG_ACTION_SYNC_COLIS = "ARG_ACTION_SYNC_COLIS";
    public static final String ARG_ACTION_SYNC_ALL = "ARG_ACTION_SYNC_ALL";

    // Create the URL to query
    private static String mUrl = Constants.URL_SUIVI_COLIS
            .concat(Constants.URL_SUIVI_SERVICE_OPT)
            .concat("?itemId=")
            .concat("%s")
            .concat("&Submit=Envoyer");

    private RequestQueueSingleton mRequestQueueSingleton;

    public SyncColisService() {
        super("SyncColisService");
        mRequestQueueSingleton = RequestQueueSingleton.getInstance(SyncColisService.this);
    }

    private void actionSyncColis(Bundle bundle) {
        if (bundle.containsKey(ARG_ID_COLIS)) {
            String idColis = bundle.getString(ARG_ID_COLIS);
            if (idColis != null) {
                String url = String.format(mUrl, idColis);

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new ActionSyncColisListener(), new ActionSyncColisErrorListener());

                // Add the request to the RequestQueue.
                mRequestQueueSingleton.addToRequestQueue(stringRequest);
            }
        }
    }

    private void actionSyncAll() {
        List<Colis> list = ProviderUtilities.getListColisFromContentProvider(this);
        if (!list.isEmpty()) {
            for (Colis colis : list) {
                String url = String.format(mUrl, colis.getIdColis());

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new ActionSyncColisListener(), new ActionSyncColisErrorListener());

                // Add the request to the RequestQueue.
                mRequestQueueSingleton.addToRequestQueue(stringRequest);
            }
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle.containsKey(ARG_ACTION)) {
                String s = bundle.getString(ARG_ACTION);
                switch (s) {
                    case ARG_ACTION_SYNC_COLIS:
                        actionSyncColis(bundle);
                        break;
                    case ARG_ACTION_SYNC_ALL:
                        actionSyncAll();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private class TransformHtmlTask extends AsyncTask<String, Void, Colis> {


        private Colis transformHtmlToObject(String htmlToTransform) {
            Colis colis = new Colis();
            try {
                int transformResult = HtmlTransformer.getParcelResultFromHtml(htmlToTransform, colis);
                switch (transformResult) {
                    case HtmlTransformer.RESULT_SUCCESS:
                        ProviderUtilities.updateLastUpdate(SyncColisService.this, colis.getIdColis(), true);
                        ProviderUtilities.checkAndInsertEtape(SyncColisService.this, colis.getIdColis(), colis.getEtapeAcheminementArrayList());
                        return colis;
                    case HtmlTransformer.RESULT_NO_ITEM_FOUND:
                        ProviderUtilities.updateLastUpdate(SyncColisService.this, colis.getIdColis(), false);
                        return null;
                    default:
                        break;
                }

            } catch (HtmlTransformer.HtmlTransformerException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected Colis doInBackground(String... params) {
            try {
                String responseEncoded = URLDecoder.decode(URLEncoder.encode(params[0], Constants.ENCODING_ISO), Constants.ENCODING_UTF_8);
                return transformHtmlToObject(responseEncoded);
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }
    }

    private class ActionSyncColisListener implements Response.Listener<String> {
        @Override
        public void onResponse(final String response) {
            TransformHtmlTask asyncTask = new TransformHtmlTask();
            asyncTask.execute(response);
        }
    }

    private class ActionSyncColisErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, error.getMessage(), error);
        }
    }
}
