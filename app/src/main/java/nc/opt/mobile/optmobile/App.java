package nc.opt.mobile.optmobile;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.squareup.leakcanary.LeakCanary;

import nc.opt.mobile.optmobile.broadcast.NetworkReceiver;
import nc.opt.mobile.optmobile.job.SyncColisJob;
import nc.opt.mobile.optmobile.job.SyncColisJobCreator;

/**
 * Created by orlanth23 on 14/01/2018.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        // On attache le receiver à notre application
        registerReceiver(NetworkReceiver.getInstance(), NetworkReceiver.CONNECTIVITY_CHANGE_INTENT_FILTER);

        JobManager.create(this).addJobCreator(new SyncColisJobCreator());

        // Plannification d'un job
        SyncColisJob.scheduleJob();

        // Lancement d'une synchro dès le début du programme
        SyncColisJob.launchImmediateJob();
    }
}
