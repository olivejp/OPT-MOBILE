package nc.opt.mobile.optmobile.database.local.repository;

import android.content.Context;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;
import nc.opt.mobile.optmobile.DateConverter;
import nc.opt.mobile.optmobile.database.local.ColisDatabase;
import nc.opt.mobile.optmobile.database.local.dao.ColisDao;
import nc.opt.mobile.optmobile.database.local.entity.ColisEntity;
import nc.opt.mobile.optmobile.database.local.repository.task.ColisRepositoryTask;
import nc.opt.mobile.optmobile.database.local.repository.task.TypeTask;

/**
 * Created by orlanth23 on 11/01/2018.
 */

public class ColisRepository {

    private static ColisRepository INSTANCE;

    private ColisDao colisDao;

    private ColisRepository(Context context) {
        ColisDatabase db = ColisDatabase.getInstance(context);
        this.colisDao = db.colisDao();
    }

    public static synchronized ColisRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ColisRepository(context);
        }
        return INSTANCE;
    }

    public void update(ColisEntity... colisEntities) {
        new ColisRepositoryTask(colisDao, TypeTask.UPDATE).execute(colisEntities);
    }

    public void markAsDelivered(ColisEntity... colisEntities) {
        for (ColisEntity colis : colisEntities) {
            colis.setDelivered(1);
        }
        new ColisRepositoryTask(colisDao, TypeTask.UPDATE).execute(colisEntities);
    }

    public void updateLastSuccessfulUpdate(ColisEntity... colisEntities) {
        Long now = DateConverter.getNowEntity();
        for (ColisEntity colis : colisEntities) {
            colis.setLastUpdate(now);
            colis.setLastUpdateSuccessful(now);
        }
        new ColisRepositoryTask(colisDao, TypeTask.UPDATE).execute(colisEntities);
    }

    public void insert(ColisEntity... colisEntities) {
        new ColisRepositoryTask(colisDao, TypeTask.INSERT).execute(colisEntities);
    }

    /**
     * If the colis has a link with Firebase, we pass the tag deleted to '1'
     * If the colis has a AfterShip Id, we pass the tag deleted to '1'
     * Otherwise we just delete the colis from the DB.
     * We have to wait a connection to delete the record in AfterShip or in Firebase.
     *
     * @param colis to delete or to mark as deleted. They will be deleted in the SyncColisService.
     */
    public boolean markAsDeleted(ColisEntity colis) {
        if ((colis.getFbLinked() == null || colis.getFbLinked() == 0) && (colis.getAfterShipId() == null || colis.getAfterShipId().isEmpty())) {
            new ColisRepositoryTask(colisDao, TypeTask.DELETE).execute(colis);
            return true;
        } else {
            colis.setDeleted(1);
            update(colis);
            return false;
        }
    }

    /**
     * Delete the colis with the idColis from the database
     *
     * @param idColis
     */
    public void delete(String idColis) {
        findById(idColis)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(colisEntity ->
                        new ColisRepositoryTask(colisDao, TypeTask.DELETE).execute(colisEntity)
                );
    }

    public Maybe<List<ColisEntity>> getAllColis(boolean active) {
        if (active) {
            return this.colisDao.listMaybeColisActifs();
        } else {
            return this.colisDao.listMaybeColisSupprimes();
        }
    }

    public Maybe<List<ColisEntity>> getAllActiveAndNonDeliveredColis() {
        return this.colisDao.listMaybeColisActifsAndNotDelivered();
    }


    public Maybe<Integer> count(String idColis) {
        return this.colisDao.count(idColis);
    }

    /**
     * Check if the idColis findBy in the DB.
     * If it findBy we just update, otherwise insert.
     *
     * @param colisEntities to save (update or insert)
     */
    public void save(ColisEntity... colisEntities) {
        for (ColisEntity colisEntity : colisEntities) {
            count(colisEntity.getIdColis())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(count -> {
                        if (count > 0) {
                            update(colisEntity);
                        } else {
                            insert(colisEntity);
                        }
                    });
        }
    }

    public Maybe<ColisEntity> findById(String idColis) {
        return this.colisDao.findById(idColis);
    }
}
