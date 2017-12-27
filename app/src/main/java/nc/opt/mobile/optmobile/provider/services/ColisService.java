package nc.opt.mobile.optmobile.provider.services;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import org.chalup.microorm.MicroOrm;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nc.opt.mobile.optmobile.domain.suivi.ColisDto;
import nc.opt.mobile.optmobile.domain.suivi.EtapeAcheminementDto;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;
import nc.opt.mobile.optmobile.provider.interfaces.ColisInterface;

import static nc.opt.mobile.optmobile.utils.DateConverter.getNowEntity;

/**
 * Created by 2761oli on 23/10/2017.
 */

public class ColisService {

    private static final MicroOrm uOrm = new MicroOrm();
    private static String selectionOnlyActiveColis = ColisInterface.DELETED.concat("<> ?");
    private static String[] argsOnlyActiveColisArgs = new String[]{"1"};

    private ColisService() {
    }

    public static ColisEntity get(Context context, String id) {
        Cursor cursor = context.getContentResolver().query(nc.opt.mobile.optmobile.provider.OptProvider.ListColis.withId(id), null, null, null, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            ColisEntity colisEntity = getFromCursor(cursor);
            // Récupération des étapes associées.
            List<EtapeEntity> list = EtapeAcheminementService.listFromProvider(context, id);
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
        Cursor cursor = context.getContentResolver().query(nc.opt.mobile.optmobile.provider.OptProvider.ListColis.withId(id), null, onlyActive ? selectionOnlyActiveColis : null, onlyActive ? argsOnlyActiveColisArgs : null, null);
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
        int nbUpdated = 0;
        long id = 0;
        if (exist(context, colisEntity.getIdColis(), false)) {
            nbUpdated = context.getContentResolver().update(OptProvider.ListColis.LIST_COLIS, putToContentValues(colisEntity), ColisInterface.ID_COLIS.concat("=?"), new String[]{colisEntity.getIdColis()});
        } else {
            id = insert(context, colisEntity);
        }

        EtapeAcheminementService.save(context, colisEntity);
        return (nbUpdated != 0 || id != -1);
    }

    public static List<ColisEntity> listFromProvider(Context context, boolean onlyActive) {
        List<ColisEntity> colisList = new ArrayList<>();

        // Query the content provider to get a cursor of ColisDto
        Cursor cursorListColis = context.getContentResolver().query(OptProvider.ListColis.LIST_COLIS, null, onlyActive ? selectionOnlyActiveColis : null, onlyActive ? argsOnlyActiveColisArgs : null, null);

        if (cursorListColis != null) {
            while (cursorListColis.moveToNext()) {
                ColisEntity colis = getFromCursor(cursorListColis);
                List<EtapeEntity> listEtape = EtapeAcheminementService.listFromProvider(context, colis.getIdColis());
                colis.setEtapeAcheminementArrayList(listEtape);
                colisList.add(colis);
            }
            cursorListColis.close();
        }
        return colisList;
    }

    public static Observable<List<ColisEntity>> observableListColisFromProvider(Context context, boolean onlyActive) {
        List<ColisEntity> colisList = new ArrayList<>();

        // Query the content provider to get a cursor of ColisDto
        Cursor cursorListColis = context.getContentResolver().query(OptProvider.ListColis.LIST_COLIS, null, onlyActive ? selectionOnlyActiveColis : null, onlyActive ? argsOnlyActiveColisArgs : null, null);

        if (cursorListColis != null) {
            while (cursorListColis.moveToNext()) {
                ColisEntity colis = getFromCursor(cursorListColis);
                List<EtapeEntity> listEtape = EtapeAcheminementService.listFromProvider(context, colis.getIdColis());
                colis.setEtapeAcheminementArrayList(listEtape);
                colisList.add(colis);
            }
            cursorListColis.close();
        }
        return Observable.just(colisList)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param context
     * @param onlyActive
     * @return
     */
    public static Observable<ColisEntity> observableListFromProvider(Context context, boolean onlyActive) {
        List<ColisEntity> colisList = new ArrayList<>();

        // Query the content provider to get a cursor of ColisDto
        Cursor cursorListColis = context.getContentResolver().query(OptProvider.ListColis.LIST_COLIS, null, onlyActive ? selectionOnlyActiveColis : null, onlyActive ? argsOnlyActiveColisArgs : null, null);

        if (cursorListColis != null) {
            while (cursorListColis.moveToNext()) {
                ColisEntity colis = getFromCursor(cursorListColis);
                List<EtapeEntity> listEtape = EtapeAcheminementService.listFromProvider(context, colis.getIdColis());
                colis.setEtapeAcheminementArrayList(listEtape);
                colisList.add(colis);
            }
            cursorListColis.close();
        }
        return Observable.just(colisList)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .flatMap(Observable::fromIterable);
    }

    /**
     * @param context
     * @param onlyActive
     * @return
     */
    public static int count(Context context, boolean onlyActive) {
        // Query the content provider to get a cursor of ColisDto
        int count = 0;

        Cursor cursorListColis = context.getContentResolver().query(OptProvider.ListColis.LIST_COLIS, null, onlyActive ? selectionOnlyActiveColis : null, onlyActive ? argsOnlyActiveColisArgs : null, null);
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
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColisInterface.DELETED, 1);

        String where = ColisInterface.ID_COLIS.concat("=?");

        return context.getContentResolver().update(OptProvider.ListColis.LIST_COLIS, contentValues, where, new String[]{idColis});
    }

    /**
     * @param context
     * @param idColis
     * @return
     */
    public static int realDelete(Context context, String idColis) {
        // Suppression des étapes d'acheminement
        EtapeAcheminementService.delete(context, idColis);

        // Suppression du colis
        return context.getContentResolver().delete(OptProvider.ListColis.LIST_COLIS, ColisInterface.ID_COLIS.concat("=?"), new String[]{idColis});
    }

    /**
     * @param colis
     * @return
     */
    private static ContentValues putToContentValues(ColisEntity colis) {
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
     * @return
     */
    public static int updateLastUpdate(Context context, @NonNull String idColis, boolean successful) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColisInterface.LAST_UPDATE, getNowEntity());
        if (successful) {
            contentValues.put(ColisInterface.LAST_UPDATE_SUCCESSFUL, getNowEntity());
        }

        String where = ColisInterface.ID_COLIS.concat("=?");

        return context.getContentResolver().update(OptProvider.ListColis.LIST_COLIS, contentValues, where, new String[]{idColis});
    }

    /**
     * @param entity
     * @return
     */
    public static ColisDto convertToDto(ColisEntity entity) {
        ColisDto dto = new ColisDto();
        dto.setIdColis(entity.getIdColis());
        if (entity.getEtapeAcheminementArrayList() != null) {
            List<EtapeAcheminementDto> listEtapeDto = new ArrayList<>();
            for (EtapeEntity etapeEntity : entity.getEtapeAcheminementArrayList()) {
                listEtapeDto.add(EtapeAcheminementService.convertToDto(etapeEntity));
            }
            dto.setEtapeAcheminementDtoArrayList(listEtapeDto);
        }
        return dto;
    }

    /**
     * @param dto
     * @return
     */
    public static ColisEntity convertToEntity(ColisDto dto) {
        ColisEntity entity = new ColisEntity();
        entity.setIdColis(dto.getIdColis());
        if (dto.getEtapeAcheminementDtoArrayList() != null) {
            List<EtapeEntity> listEtapeEntity = new ArrayList<>();
            for (EtapeAcheminementDto etapeDto : dto.getEtapeAcheminementDtoArrayList()) {
                listEtapeEntity.add(EtapeAcheminementService.convertToEntity(etapeDto));
            }
            entity.setEtapeAcheminementArrayList(listEtapeEntity);
        }
        return entity;
    }
}
