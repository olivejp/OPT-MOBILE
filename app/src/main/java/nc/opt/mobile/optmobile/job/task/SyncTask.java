package nc.opt.mobile.optmobile.job.task;

import android.content.Context;
import android.os.AsyncTask;

import nc.opt.mobile.optmobile.service.SyncColisService;

/**
 * Created by 2761oli on 27/12/2017.
 */


public class SyncTask extends AsyncTask<ParamSyncTask, Void, Void> {

    public enum TypeTask {
        SOLO,
        MULTIPLE
    }

    private TypeTask type;

    public SyncTask(TypeTask type) {
        this.type = type;
    }

    @Override
    protected Void doInBackground(ParamSyncTask... paramSyncTasks) {
        if (paramSyncTasks.length > 0) {
            Context context = paramSyncTasks[0].getContext();
            String idColis = paramSyncTasks[0].getIdColis();
            if (type == TypeTask.SOLO) {
                SyncColisService.launchSynchroByIdColis(context, idColis, false);
            } else if (type == TypeTask.MULTIPLE) {
                SyncColisService.launchSynchroForAll(context, false);
            }
        }
        return null;
    }


}

