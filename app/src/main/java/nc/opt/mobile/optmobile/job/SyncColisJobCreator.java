package nc.opt.mobile.optmobile.job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by 2761oli on 10/10/2017.
 */

public class SyncColisJobCreator implements JobCreator {
    @Override
    @Nullable
    public Job create(@NonNull String tag) {
        if (tag.equals(SyncColisJob.SYNC_COLIS_JOB)) {
            return new SyncColisJob();
        } else {
            return null;
        }
    }
}
