package nc.opt.mobile.optmobile.provider.services;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import org.chalup.microorm.MicroOrm;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import nc.opt.mobile.optmobile.domain.suivi.ColisDto;
import nc.opt.mobile.optmobile.domain.suivi.EtapeDto;
import nc.opt.mobile.optmobile.domain.suivi.aftership.Checkpoint;
import nc.opt.mobile.optmobile.domain.suivi.aftership.TrackingData;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;
import nc.opt.mobile.optmobile.provider.interfaces.ColisInterface;

import static nc.opt.mobile.optmobile.provider.services.EtapeService.createEtapeFromCheckpoint;
import static nc.opt.mobile.optmobile.utils.DateConverter.getNowEntity;

/**
 * Created by 2761oli on 23/10/2017.
 */

public class ColisService {

    private static final String TAG = ColisService.class.getName();

    private static final MicroOrm uOrm = new MicroOrm();
    private static String selectionOnlyActiveColis = ColisInterface.DELETED.concat("<> ?");
    private static String selectionOnlyDeletedColis = ColisInterface.DELETED.concat("= ?");
    private static String[] argsDeletedColisArgs = new String[]{"1"};

    private ColisService() {
    }

    public static ColisEntity get(Context context, String id) {
        Cursor cursor = context.getContentResolver().query(nc.opt.mobile.optmobile.provider.OptProvider.ListColis.withId(id), null, null, null, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            ColisEntity colisEntity = getFromCursor(cursor);

            // Récupération des étapes associées.
            List<EtapeEntity> list = EtapeService.listFromProvider(context, id);
            colisEntity.setEtapeAcheminementArrayList(list);
            return colisEntity;
        }
        return null;
    }

    /**
     * Return true if colis with id exist in the content provider
     *
     * @param context
     * @param id
     * @param onlyActive
     * @return
     */
    public static boolean exist(Context context, String id, boolean onlyActive) {
        Cursor cursor = context.getContentResolver().query(nc.opt.mobile.optmobile.provider.OptProvider.ListColis.withId(id), null, onlyActive ? selectionOnlyActiveColis : null, onlyActive ? argsDeletedColisArgs : null, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    /**
     * Insert colisEntity in the Content Provider
     *
     * @param context
     * @param colis
     * @return the id of the new object inserted or -1 if object has not been inserted.
     */
    public static long insert(Context context, ColisEntity colis) {
        Uri uri = context.getContentResolver().insert(OptProvider.ListColis.LIST_COLIS, putToContentValues(colis));
        if (uri == null) {
            return -1;
        } else {
            return ContentUris.parseId(uri);
        }
    }

    /**
     * Create or update ColisEntity and on cascade the EtapeEntity related.
     *
     * @param context
     * @param colisEntity
     * @return 0 if no row was updated, otherwise return the number of row updated
     */
    public static boolean save(Context context, ColisEntity colisEntity) {
        Log.d(TAG, "(save) Sauvegarde en Db du tracking : " + colisEntity.getIdColis());
        int nbUpdated = 0;
        long id = 0;
        if (exist(context, colisEntity.getIdColis(), false)) {
            nbUpdated = context.getContentResolver().update(OptProvider.ListColis.LIST_COLIS, putToContentValues(colisEntity), ColisInterface.ID_COLIS.concat("=?"), new String[]{colisEntity.getIdColis()});
        } else {
            id = insert(context, colisEntity);
        }

        EtapeService.save(context, colisEntity);
        return (nbUpdated != 0 || id != -1);
    }

    /**
     * @param context
     * @param onlyActive
     * @return
     */
    public static List<ColisEntity> listFromProvider(Context context, boolean onlyActive) {
        Log.d(TAG, "(listFromProvider) List de tous les colis présents dans l'application");
        List<ColisEntity> colisList = new ArrayList<>();

        // Query the content provider to get a cursor of ColisDto
        Cursor cursorListColis = context.getContentResolver().query(OptProvider.ListColis.LIST_COLIS, null, onlyActive ? selectionOnlyActiveColis : null, onlyActive ? argsDeletedColisArgs : null, null);

        if (cursorListColis != null) {
            while (cursorListColis.moveToNext()) {
                ColisEntity colis = getFromCursor(cursorListColis);
                List<EtapeEntity> listEtape = EtapeService.listFromProvider(context, colis.getIdColis());
                colis.setEtapeAcheminementArrayList(listEtape);
                colisList.add(colis);
            }
            cursorListColis.close();
        }
        return colisList;
    }

    /**
     * @param context
     * @return
     */
    public static List<ColisEntity> listDeletedFromProvider(Context context) {
        Log.d(TAG, "(listDeletedFromProvider) List de tous les colis 'SUPPRIMES' présents dans l'application");
        List<ColisEntity> colisList = new ArrayList<>();

        // Query the content provider to get a cursor of ColisDto
        Cursor cursorListColis = context.getContentResolver().query(OptProvider.ListColis.LIST_COLIS, null, selectionOnlyDeletedColis, argsDeletedColisArgs, null);

        if (cursorListColis != null) {
            while (cursorListColis.moveToNext()) {
                ColisEntity colis = getFromCursor(cursorListColis);
                List<EtapeEntity> listEtape = EtapeService.listFromProvider(context, colis.getIdColis());
                colis.setEtapeAcheminementArrayList(listEtape);
                colisList.add(colis);
            }
            cursorListColis.close();
        }
        return colisList;
    }

    public static Observable<ColisEntity> observableGetColisFromProvider(Context context, String idColis) {
        return Observable.just(get(context, idColis))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public static Observable<List<ColisEntity>> observableListColisFromProvider(Context context) {
        return Observable.just(listFromProvider(context, true))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    /**
     * @param context
     * @param onlyActive
     * @return
     */
    public static int count(Context context, boolean onlyActive) {
        // Query the content provider to get a cursor of ColisDto
        int count = 0;

        Cursor cursorListColis = context.getContentResolver().query(OptProvider.ListColis.LIST_COLIS, null, onlyActive ? selectionOnlyActiveColis : null, onlyActive ? argsDeletedColisArgs : null, null);
        if (cursorListColis != null) {
            count = cursorListColis.getCount();
            cursorListColis.close();
        }
        return count;
    }

    /**
     * @param context
     * @param idColis
     * @return
     */
    public static int delete(Context context, String idColis) {
        Log.d(TAG, "(delete) Suppression effective du colis : " + idColis);
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColisInterface.DELETED, 1);
        String where = ColisInterface.ID_COLIS.concat("=?");
        return context.getContentResolver().update(OptProvider.ListColis.LIST_COLIS, contentValues, where, new String[]{idColis});
    }

    /**
     * @param context
     * @param idColis
     * @return Number of rows really deleted.
     */
    public static int realDelete(Context context, String idColis) {
        Log.d(TAG, "(realDelete) Suppression réelle du colis : " + idColis);

        // Suppression des étapes d'acheminement
        EtapeService.delete(context, idColis);

        // Suppression du colis
        return context.getContentResolver().delete(OptProvider.ListColis.LIST_COLIS, ColisInterface.ID_COLIS.concat("=?"), new String[]{idColis});
    }

    /**
     * @param colis
     * @return
     */
    private static ContentValues putToContentValues(ColisEntity colis) {
        Log.d(TAG, "(putToContentValues)");
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColisInterface.ID_COLIS, colis.getIdColis());
        contentValues.put(ColisInterface.DESCRIPTION, colis.getDescription());
        contentValues.put(ColisInterface.DELETED, colis.getDeleted());
        contentValues.put(ColisInterface.SLUG, colis.getSlug());
        return contentValues;
    }

    /**
     * @param cursor
     * @return
     */
    private static ColisEntity getFromCursor(Cursor cursor) {
        return uOrm.fromCursor(cursor, ColisEntity.class);
    }

    /**
     * @param context
     * @param idColis
     * @param successful
     */
    public static void updateLastUpdate(Context context, @NonNull String idColis, boolean successful) {
        Log.d(TAG, "(updateLastUpdate)");
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColisInterface.LAST_UPDATE, getNowEntity());
        if (successful) {
            contentValues.put(ColisInterface.LAST_UPDATE_SUCCESSFUL, getNowEntity());
        }

        String where = ColisInterface.ID_COLIS.concat("=?");
        context.getContentResolver().update(OptProvider.ListColis.LIST_COLIS, contentValues, where, new String[]{idColis});
    }

    /**
     * @param dto
     * @return
     */
    public static ColisEntity convertToEntity(ColisDto dto) {
        Log.d(TAG, "(convertToEntity)");
        ColisEntity entity = new ColisEntity();
        entity.setIdColis(dto.getIdColis());
        if (dto.getEtapeDtoArrayList() != null && !dto.getEtapeDtoArrayList().isEmpty()) {
            List<EtapeEntity> listEtapeEntity = new ArrayList<>();
            for (EtapeDto etapeDto : dto.getEtapeDtoArrayList()) {
                listEtapeEntity.add(EtapeService.convertToEntity(etapeDto));
            }
            entity.setEtapeAcheminementArrayList(listEtapeEntity);
        }
        return entity;
    }


    /**
     * Fill the ColisEntity with the TrackingData informations.
     *
     * @param colis
     * @param trackingData
     * @return
     */
    public static ColisEntity convertTrackingDataToEntity(ColisEntity colis, TrackingData trackingData) {
        Log.d(TAG, "(convertTrackingDataToEntity)");
        colis.setDeleted(0);
        for (Checkpoint c : trackingData.getCheckpoints()) {
            EtapeEntity etapeEntity = createEtapeFromCheckpoint(colis.getIdColis(), c);
            colis.getEtapeAcheminementArrayList().add(etapeEntity);
        }
        return colis;
    }
}
