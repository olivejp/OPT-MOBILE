package nc.opt.mobile.optmobile;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import nc.opt.mobile.optmobile.broadcast.NetworkReceiver;
import nc.opt.mobile.optmobile.job.SyncColisJob;
import nc.opt.mobile.optmobile.job.SyncColisJobCreator;

/**
 * Created by 2761oli on 10/10/2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // On attache le receiver à notre application
        registerReceiver(NetworkReceiver.getInstance(), NetworkReceiver.CONNECTIVITY_CHANGE_INTENT_FILTER);

        JobManager.create(this).addJobCreator(new SyncColisJobCreator());

        // Plannification d'un job
        SyncColisJob.scheduleJob();

        // Lancement d'une synchro dès le début du programme
        SyncColisJob.launchImmediateJob();

        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_default);
    }
}
