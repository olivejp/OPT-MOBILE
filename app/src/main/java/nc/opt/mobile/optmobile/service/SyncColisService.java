package nc.opt.mobile.optmobile.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.List;

import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.services.ColisService;
import nc.opt.mobile.optmobile.utils.CoreSync;

import static nc.opt.mobile.optmobile.provider.services.ColisService.listFromProvider;
import static nc.opt.mobile.optmobile.service.FirebaseService.updateFirebase;

public class SyncColisService extends IntentService {

    private static final String TAG = SyncColisService.class.getName();

    public static final String ARG_ID_COLIS = "ARG_ID_COLIS";
    public static final String ARG_ACTION = "ARG_ACTION";
    public static final String ARG_ACTION_SYNC_COLIS = "ARG_ACTION_SYNC_COLIS";
    public static final String ARG_ACTION_SYNC_ALL = "ARG_ACTION_SYNC_ALL";
    public static final String ARG_ACTION_SYNC_ALL_FROM_SCHEDULER = "ARG_ACTION_SYNC_ALL_FROM_SCHEDULER";
    public static final String ARG_NOTIFICATION = "ARG_NOTIFICATION";

    public SyncColisService() {
        super("SyncColisService");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void refreshRemoteConfig() {
        // cacheExpirationSeconds is set to cacheExpiration here, indicating the next fetch request
        // will use fetch data from the Remote Config service, rather than cached parameter values,
        // if cached parameter values are more than cacheExpiration seconds old.
        // See Best Practices in the README for more information.
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.fetch()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "RefreshRemoteConfig called successfully");
                        firebaseRemoteConfig.activateFetched();
                    } else {
                        Log.d(TAG, "Failed to call RefreshRemoteConfig");
                    }
                });
    }

    /**
     * Lancement du service de synchro
     *
     * @param context
     * @param idColis
     * @param sendNotification
     */
    public static void launchSynchroByIdColis(Context context, String idColis, boolean sendNotification) {
        Intent syncService = new Intent(context, SyncColisService.class);
        syncService.putExtra(SyncColisService.ARG_ACTION, SyncColisService.ARG_ACTION_SYNC_COLIS);
        syncService.putExtra(SyncColisService.ARG_ID_COLIS, idColis);
        syncService.putExtra(SyncColisService.ARG_NOTIFICATION, sendNotification);
        context.startService(syncService);
    }

    /**
     * Lancement du service de synchro pour tous les objets
     *
     * @param context
     * @param sendNotification
     */
    public static void launchSynchroForAll(Context context, boolean sendNotification) {
        Intent syncService = new Intent(context, SyncColisService.class);
        syncService.putExtra(SyncColisService.ARG_ACTION, SyncColisService.ARG_ACTION_SYNC_ALL);
        syncService.putExtra(SyncColisService.ARG_NOTIFICATION, sendNotification);
        context.startService(syncService);
    }

    /**
     * Lancement du service de synchro pour tous les objets mais à partir du scheduler
     *
     * @param context
     */
    public static void launchSynchroFromScheduler(Context context) {
        Intent syncService = new Intent(context, SyncColisService.class);
        syncService.putExtra(SyncColisService.ARG_ACTION, SyncColisService.ARG_ACTION_SYNC_ALL_FROM_SCHEDULER);
        syncService.putExtra(SyncColisService.ARG_NOTIFICATION, true);
        context.startService(syncService);
    }

    /**
     * Lecture de tous les colis inactifs dans la base de données pour suppression de l'API AfterShip.
     *
     * @param context
     */
    public static void launchSynchroDelete(Context context) {
        for (ColisEntity colis : ColisService.listDeletedFromProvider(context)) {
            CoreSync.deleteTracking(context, colis);
        }

        // Update Firebase
        updateFirebase(context, listFromProvider(context, true));
    }

    /**
     * @param bundle
     * @param sendNotification
     */
    private void handleActionSyncColis(Bundle bundle, boolean sendNotification) {
        if (bundle.containsKey(ARG_ID_COLIS)) {
            String idColis = bundle.getString(ARG_ID_COLIS);

            if (idColis != null) {
                CoreSync.getTracking(this, idColis, sendNotification);
            }

            // Suppression du tracking sur l'API AfterShip
            launchSynchroDelete(this);
        }
    }

    /**
     * @param sendNotification
     */
    private void handleActionSyncAll(boolean sendNotification) {
        List<ColisEntity> list = listFromProvider(this, true);
        if (!list.isEmpty()) {
            for (ColisEntity colis : list) {
                CoreSync.getTracking(this, colis.getIdColis(), sendNotification);
            }
        }

        // Suppression du tracking sur l'API AfterShip
        launchSynchroDelete(this);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null && bundle.containsKey(ARG_ACTION)) {
                String s = bundle.getString(ARG_ACTION);
                boolean sendNotification = (bundle.containsKey(ARG_NOTIFICATION)) && bundle.getBoolean(ARG_NOTIFICATION);
                if (s != null) {
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
            // On va rafraichir les données du RemoteConfig
            refreshRemoteConfig();
        }
    }
}
