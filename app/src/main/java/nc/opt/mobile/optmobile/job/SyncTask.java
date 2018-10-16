package nc.opt.mobile.optmobile.job;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by 2761oli on 27/12/2017.
 * <p>
 * This class is used to call the SyncColisService from the MainThread to the background with an AsyncTask
 * Contains a {@link WeakReference} with a {@link Context}
 * If idColis is filled we sync only one colis, all if not.
 */

public class SyncTask extends AsyncTask<Void, Void, Void> {

    public enum TypeSyncTask {
        SOLO,
        ALL,
        DELETE
    }

    private static final String TAG = SyncTask.class.getName();

    private WeakReference<Context> weakContext;
    private String idColis;
    private TypeSyncTask typeSyncTask;

    /**
     * Constructor to launch a SyncTask.
     *
     * @param context
     * @param type
     * @param idColis
     */
    public SyncTask(Context context, TypeSyncTask type, @Nullable String idColis) {
        if (context != null) {
            this.weakContext = new WeakReference<>(context);
        }
        this.typeSyncTask = type;
        if (this.typeSyncTask == TypeSyncTask.SOLO) {
            if (idColis == null || idColis.isEmpty()) {
                Log.e(TAG, "SyncTask SOLO appelé sans le paramètre idColis");
            } else {
                this.idColis = idColis;
            }
        } else {
            this.idColis = null;
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (this.weakContext != null && this.weakContext.get() != null) {
            Context context = this.weakContext.get();
            switch (this.typeSyncTask) {
                case SOLO:
                    if (this.idColis != null) {
                        SyncColisService.launchSynchroByIdColis(context, this.idColis);
                    }
                    break;
                case ALL:
                    SyncColisService.launchSynchroForAll(context);
                    break;
                case DELETE:
                    SyncColisService.launchSynchroDelete(context);
                    break;
            }
        }
        return null;
    }
}

