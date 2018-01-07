package nc.opt.mobile.optmobile.job.task;

import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;

import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.services.ColisService;
import nc.opt.mobile.optmobile.service.FirebaseService;

/**
 * Created by 2761oli on 27/12/2017.
 */

public class SyncFirebaseTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private DataSnapshot dataSnapshot;

    public SyncFirebaseTask(Context context, DataSnapshot dataSnapshot) {
        this.context = context;
        this.dataSnapshot = dataSnapshot;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            ColisEntity remoteColisEntity = postSnapshot.getValue(ColisEntity.class);
            if (remoteColisEntity != null) {
                if (ColisService.exist(context, remoteColisEntity.getIdColis(), false)) {
                    ColisEntity colisEntity = ColisService.get(context, remoteColisEntity.getIdColis());
                    if (colisEntity != null && colisEntity.getDeleted() == 1) {
                        FirebaseService.deleteRemoteColis(remoteColisEntity.getIdColis());
                        ColisService.realDelete(context, remoteColisEntity.getIdColis());
                    }
                } else {
                    // Colis don't already exist in our local DB, we insert it.
                    ColisService.save(context, remoteColisEntity);
                }
            }
        }

        return null;
    }
}

