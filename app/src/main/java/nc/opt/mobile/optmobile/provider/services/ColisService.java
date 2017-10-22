package nc.opt.mobile.optmobile.provider.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import org.chalup.microorm.MicroOrm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeAcheminementEntity;

import static nc.opt.mobile.optmobile.provider.entity.EtapeAcheminementEntity.EtapeAcheminementInterface.ID_COLIS;
import static nc.opt.mobile.optmobile.utils.DateConverter.getNowDto;

/**
 * Created by 2761oli on 23/10/2017.
 */

public class ColisService {

    private static final MicroOrm uOrm = new MicroOrm();

    public static List<ColisEntity> listFromProvider(Context context) {
        List<ColisEntity> colisList = new ArrayList<>();

        // Query the content provider to get a cursor of Colis
        Cursor cursorListColis = context.getContentResolver().query(OptProvider.ListColis.LIST_COLIS, null, null, null, null);

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
        // Query the content provider to get a cursor of Colis
        int count = 0;
        Cursor cursorListColis = context.getContentResolver().query(OptProvider.ListColis.LIST_COLIS, null, null, null, null);
        if (cursorListColis != null) {
            count = cursorListColis.getCount();
            cursorListColis.close();
        }
        return count;
    }

    public static boolean delete(Context context, String idColis) {
        // Suppression des étapes d'acheminement
        context.getContentResolver().delete(OptProvider.ListEtapeAcheminement.LIST_ETAPE, ID_COLIS.concat("=?"), new String[]{idColis});

        // Suppression du colis
        int result = context.getContentResolver().delete(OptProvider.ListColis.LIST_COLIS, ColisEntity.ColisInterface.ID_COLIS.concat("=?"), new String[]{idColis});

        return result == 1;
    }

    public static ContentValues putToContentValues(ColisEntity colis) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColisEntity.ColisInterface.ID_COLIS, colis.getIdColis());
        contentValues.put(ColisEntity.ColisInterface.DESCRIPTION, colis.getDescription());
        return contentValues;
    }

    private static ColisEntity getFromCursor(Cursor cursor) {
        return uOrm.fromCursor(cursor, ColisEntity.class);
    }

    public static int updateLastUpdate(Context context, @NonNull String idColis, boolean successful) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColisEntity.ColisInterface.LAST_UPDATE, getNowDto());
        if (successful) {
            contentValues.put(ColisEntity.ColisInterface.LAST_UPDATE_SUCCESSFUL, getNowDto());
        }

        String where = ColisEntity.ColisInterface.ID_COLIS.concat("=?");

        return context.getContentResolver().update(OptProvider.ListColis.LIST_COLIS, contentValues, where, new String[]{idColis});
    }

}
