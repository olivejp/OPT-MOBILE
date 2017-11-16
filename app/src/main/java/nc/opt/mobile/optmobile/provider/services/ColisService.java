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

import nc.opt.mobile.optmobile.domain.suiviColis.ColisDto;
import nc.opt.mobile.optmobile.domain.suiviColis.EtapeAcheminementDto;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeAcheminementEntity;
import nc.opt.mobile.optmobile.provider.interfaces.ColisInterface;

import static nc.opt.mobile.optmobile.utils.DateConverter.getNowEntity;

/**
 * Created by 2761oli on 23/10/2017.
 */

public class ColisService {

    private static final MicroOrm uOrm = new MicroOrm();
    private static String onlyActiveColis = ColisInterface.DELETED.concat("<> ?");
    private static String[] onlyActiveColisArgs = new String[]{"1"};

    private ColisService() {
    }

    public static ColisEntity get(Context context, String id) {
        Cursor cursor = context.getContentResolver().query(nc.opt.mobile.optmobile.provider.OptProvider.ListColis.withId(id), null, null, null, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            return getFromCursor(cursor);
        }
        return null;
    }

    public static boolean exist(Context context, String id) {
        Cursor cursor = context.getContentResolver().query(nc.opt.mobile.optmobile.provider.OptProvider.ListColis.withId(id), null, null, null, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    public static long insert(Context context, ColisEntity colis) {
        Uri uri = context.getContentResolver().insert(OptProvider.ListColis.LIST_COLIS, putToContentValues(colis));
        return ContentUris.parseId(uri);
    }

    public static List<ColisEntity> listFromProvider(Context context) {
        List<ColisEntity> colisList = new ArrayList<>();

        // Query the content provider to get a cursor of ColisDto
        Cursor cursorListColis = context.getContentResolver().query(OptProvider.ListColis.LIST_COLIS, null, onlyActiveColis, onlyActiveColisArgs, null);

        if (cursorListColis != null) {
            while (cursorListColis.moveToNext()) {
                ColisEntity colis = getFromCursor(cursorListColis);
                List<EtapeAcheminementEntity> listEtape = EtapeAcheminementService.listFromProvider(context, colis.getIdColis());
                colis.setEtapeAcheminementArrayList(listEtape);
                colisList.add(colis);
            }
            cursorListColis.close();
        }
        return colisList;
    }

    public static int count(Context context) {
        // Query the content provider to get a cursor of ColisDto
        int count = 0;

        Cursor cursorListColis = context.getContentResolver().query(OptProvider.ListColis.LIST_COLIS, null, onlyActiveColis, onlyActiveColisArgs, null);
        if (cursorListColis != null) {
            count = cursorListColis.getCount();
            cursorListColis.close();
        }
        return count;
    }

    public static int delete(Context context, String idColis) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColisInterface.DELETED, 1);

        String where = ColisInterface.ID_COLIS.concat("=?");

        return context.getContentResolver().update(OptProvider.ListColis.LIST_COLIS, contentValues, where, new String[]{idColis});
    }

    public static boolean realDelete(Context context, String idColis) {
        // Suppression des étapes d'acheminement
        EtapeAcheminementService.delete(context, idColis);

        // Suppression du colis
        int result = context.getContentResolver().delete(OptProvider.ListColis.LIST_COLIS, ColisInterface.ID_COLIS.concat("=?"), new String[]{idColis});

        return result == 1;
    }

    private static ContentValues putToContentValues(ColisEntity colis) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColisInterface.ID_COLIS, colis.getIdColis());
        contentValues.put(ColisInterface.DESCRIPTION, colis.getDescription());
        contentValues.put(ColisInterface.DELETED, colis.getDeleted());
        return contentValues;
    }

    private static ColisEntity getFromCursor(Cursor cursor) {
        return uOrm.fromCursor(cursor, ColisEntity.class);
    }

    public static int updateLastUpdate(Context context, @NonNull String idColis, boolean successful) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColisInterface.LAST_UPDATE, getNowEntity());
        if (successful) {
            contentValues.put(ColisInterface.LAST_UPDATE_SUCCESSFUL, getNowEntity());
        }

        String where = ColisInterface.ID_COLIS.concat("=?");

        return context.getContentResolver().update(OptProvider.ListColis.LIST_COLIS, contentValues, where, new String[]{idColis});
    }

    public static ColisDto convertToDto(ColisEntity entity) {
        ColisDto dto = new ColisDto();
        dto.setIdColis(entity.getIdColis());
        if (entity.getEtapeAcheminementArrayList() != null) {
            List<EtapeAcheminementDto> listEtapeDto = new ArrayList<>();
            for (EtapeAcheminementEntity etapeEntity : entity.getEtapeAcheminementArrayList()) {
                listEtapeDto.add(EtapeAcheminementService.convertToDto(etapeEntity));
            }
            dto.setEtapeAcheminementDtoArrayList(listEtapeDto);
        }
        return dto;
    }

}
