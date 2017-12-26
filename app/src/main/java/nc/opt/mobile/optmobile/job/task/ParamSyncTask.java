package nc.opt.mobile.optmobile.job.task;

import android.content.Context;

import io.reactivex.annotations.Nullable;

public class ParamSyncTask {
    private Context context;
    private String idColis;

    public ParamSyncTask(Context context, @Nullable String idColis) {
        this.context = context;
        this.idColis = idColis;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getIdColis() {
        return idColis;
    }

    public void setIdColis(String idColis) {
        this.idColis = idColis;
    }
}
