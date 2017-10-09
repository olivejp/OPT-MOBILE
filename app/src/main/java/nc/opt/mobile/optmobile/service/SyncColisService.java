package nc.opt.mobile.optmobile.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.List;

import nc.opt.mobile.optmobile.domain.Colis;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;
import nc.opt.mobile.optmobile.utils.Constants;
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

    // Lancement du service de synchro
    public static void launchSynchroByIdColis(Context context, String idColis) {
        Intent syncService = new Intent(context, SyncColisService.class);
        syncService.putExtra(SyncColisService.ARG_ACTION, SyncColisService.ARG_ACTION_SYNC_COLIS);
        syncService.putExtra(SyncColisService.ARG_ID_COLIS, idColis);
        context.startService(syncService);
    }

    // Lancement du service de synchro pour tous les objets
    public static void launchSynchroForAll(Context context) {
        Intent syncService = new Intent(context, SyncColisService.class);
        syncService.putExtra(SyncColisService.ARG_ACTION, SyncColisService.ARG_ACTION_SYNC_ALL);
        context.startService(syncService);
    }

    private void handleActionSyncColis(Bundle bundle) {
        if (bundle.containsKey(ARG_ID_COLIS)) {
            String idColis = bundle.getString(ARG_ID_COLIS);
            if (idColis != null) {
                String url = String.format(mUrl, idColis);

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new ActionSyncColisListener(idColis), new ActionSyncColisErrorListener());

                // Add the request to the RequestQueue.
                mRequestQueueSingleton.addToRequestQueue(stringRequest);
            }
        }
    }

    private void handleActionSyncAll() {
        List<Colis> list = ProviderUtilities.getListColisFromContentProvider(this);
        if (!list.isEmpty()) {
            for (Colis colis : list) {
                String url = String.format(mUrl, colis.getIdColis());

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new ActionSyncColisListener(colis.getIdColis()), new ActionSyncColisErrorListener());

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
                        handleActionSyncColis(bundle);
                        break;
                    case ARG_ACTION_SYNC_ALL:
                        handleActionSyncAll();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private class ActionSyncColisListener implements Response.Listener<String> {
        private String idColis;

        ActionSyncColisListener(String idColis) {
            this.idColis = idColis;
        }

        @Override
        public void onResponse(final String response) {
            TransformHtmlTask asyncTask = new TransformHtmlTask(SyncColisService.this, idColis);
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
