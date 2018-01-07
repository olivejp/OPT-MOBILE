package nc.opt.mobile.optmobile.job.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import nc.opt.mobile.optmobile.service.SyncColisService;

/**
 * Created by 2761oli on 27/12/2017.
 */

public class SyncTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = SyncTask.class.getName();

    public enum TypeTask {
        SOLO,
        ALL
    }

    private Context context;
    private String idColis;
    private TypeTask typeTask;

    public SyncTask(TypeTask typeTask, Context context, @Nullable String idColis) {
        this.context = context;
        this.typeTask = typeTask;
        this.idColis = idColis;
    }

    public SyncTask(TypeTask typeTask, Context context) {
        this.context = context;
        this.typeTask = typeTask;
        this.idColis = null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (this.typeTask == TypeTask.SOLO) {
            if (this.idColis != null) {
                SyncColisService.launchSynchroByIdColis(this.context, this.idColis);
            } else {
                Log.e(TAG, "Try to call SyncColisService.launchSynchroByIdColis but don't get the idColis");
            }
        } else if (this.typeTask == TypeTask.ALL) {
            SyncColisService.launchSynchroForAll(context);
        }
        return null;
    }
}

