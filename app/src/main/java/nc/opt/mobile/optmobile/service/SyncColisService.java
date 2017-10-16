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

import nc.opt.mobile.optmobile.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;
import nc.opt.mobile.optmobile.utils.Constants;
import nc.opt.mobile.optmobile.utils.RequestQueueSingleton;

public class SyncColisService extends IntentService {

    private static final String TAG = SyncColisService.class.getName();

    public static final String ARG_ID_COLIS = "ARG_ID_COLIS";
    public static final String ARG_ACTION = "ARG_ACTION";
    public static final String ARG_ACTION_SYNC_COLIS = "ARG_ACTION_SYNC_COLIS";
    public static final String ARG_ACTION_SYNC_ALL = "ARG_ACTION_SYNC_ALL";
    public static final String ARG_ACTION_SYNC_ALL_FROM_SCHEDULER = "ARG_ACTION_SYNC_ALL_FROM_SCHEDULER";
    public static final String ARG_NOTIFICATION = "ARG_NOTIFICATION";

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
    public static void launchSynchroByIdColis(Context context, String idColis, boolean sendNotification) {
        Intent syncService = new Intent(context, SyncColisService.class);
        syncService.putExtra(SyncColisService.ARG_ACTION, SyncColisService.ARG_ACTION_SYNC_COLIS);
        syncService.putExtra(SyncColisService.ARG_ID_COLIS, idColis);
        syncService.putExtra(SyncColisService.ARG_NOTIFICATION, sendNotification);
        context.startService(syncService);
    }

    // Lancement du service de synchro pour tous les objets
    public static void launchSynchroForAll(Context context, boolean sendNotification) {
        Intent syncService = new Intent(context, SyncColisService.class);
        syncService.putExtra(SyncColisService.ARG_ACTION, SyncColisService.ARG_ACTION_SYNC_ALL);
        syncService.putExtra(SyncColisService.ARG_NOTIFICATION, sendNotification);
        context.startService(syncService);
    }

    // Lancement du service de synchro pour tous les objets
    public static void launchSynchroFromScheduler(Context context) {
        Intent syncService = new Intent(context, SyncColisService.class);
        syncService.putExtra(SyncColisService.ARG_ACTION, SyncColisService.ARG_ACTION_SYNC_ALL_FROM_SCHEDULER);
        syncService.putExtra(SyncColisService.ARG_NOTIFICATION, true);
        context.startService(syncService);
    }

    private void handleActionSyncColis(Bundle bundle, boolean sendNotification) {
        if (bundle.containsKey(ARG_ID_COLIS)) {
            String idColis = bundle.getString(ARG_ID_COLIS);

            if (idColis != null) {
                String url = String.format(mUrl, idColis);

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new ActionSyncColisListener(idColis, sendNotification), new ActionSyncColisErrorListener());

                // Add the request to the RequestQueue.
                mRequestQueueSingleton.addToRequestQueue(stringRequest);
            }
        }
    }

    private void handleActionSyncAll(boolean sendNotification) {
        List<ColisEntity> list = ProviderUtilities.getListColisFromContentProvider(this);
        if (!list.isEmpty()) {
            for (ColisEntity colis : list) {
                String url = String.format(mUrl, colis.getIdColis());

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new ActionSyncColisListener(colis.getIdColis(), sendNotification), new ActionSyncColisErrorListener());

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
                boolean sendNotification = (bundle.containsKey(ARG_NOTIFICATION)) && bundle.getBoolean(ARG_NOTIFICATION);
                switch (s) {
                    case ARG_ACTION_SYNC_COLIS:
                        handleActionSyncColis(bundle, sendNotification);
                        break;
                    case ARG_ACTION_SYNC_ALL:
                        handleActionSyncAll(sendNotification);
                        break;
                    case ARG_ACTION_SYNC_ALL_FROM_SCHEDULER:
                        handleActionSyncAll(true);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private class ActionSyncColisListener implements Response.Listener<String> {
        private String idColis;
        private boolean sendNotification;

        ActionSyncColisListener(String idColis, boolean sendNotification) {
            this.idColis = idColis;
            this.sendNotification = sendNotification;
        }

        @Override
        public void onResponse(final String response) {
            TransformHtmlTask asyncTask = new TransformHtmlTask(SyncColisService.this, idColis, sendNotification);
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
