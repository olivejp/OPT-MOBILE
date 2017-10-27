package nc.opt.mobile.optmobile.provider.services;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.chalup.microorm.MicroOrm;

import java.util.ArrayList;
import java.util.List;

import nc.opt.mobile.optmobile.domain.ActualiteDto;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.entity.ActualiteEntity;
import nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface;
import nc.opt.mobile.optmobile.utils.DateConverter;

import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.CONTENU;
import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.DATE;
import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.TITRE;
import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.TYPE;
import static nc.opt.mobile.optmobile.utils.DateConverter.convertDateDtoToEntity;

/**
 * Created by 2761oli on 25/10/2017.
 */

public class ActualiteService {

    private static final MicroOrm uOrm = new MicroOrm();

    // Check existence
    private static String mWhereActualiteExistenceWhere = ActualiteInterface.ID_ACTUALITE.concat("=? AND ")
            .concat(DATE).concat("=? AND ")
            .concat(CONTENU).concat("=? AND ")
            .concat(TYPE).concat("=? AND ")
            .concat(TITRE).concat("=? AND ");

    public static Long insertActualite(Context context, ActualiteDto actualiteDto) {
        return insertActualite(context,  putToContentValues(actualiteDto));
    }

    public static Long insertActualite(Context context, ActualiteEntity actualiteEntity) {
        ContentValues contentValues = uOrm.toContentValues(actualiteEntity);
        return insertActualite(context, contentValues);
    }

    public static Long insertActualite(Context context, ContentValues contentValues) {
        Uri uriInserted = context.getContentResolver().insert(OptProvider.ListActualite.LIST_ACTUALITE, contentValues);
        return ContentUris.parseId(uriInserted);
    }

    public static int updateActualite(Context context, ActualiteDto actualiteDto) {
        String where = ActualiteInterface.ID_ACTUALITE + " = ?";
        String[] args = new String[]{actualiteDto.getIdActualite()};
        return context.getContentResolver().update(OptProvider.ListActualite.LIST_ACTUALITE, putToContentValues(actualiteDto), where, args);
    }

    public static int deleteActualite(Context context, ActualiteDto actualiteDto) {
        String where = ActualiteInterface.ID_ACTUALITE + " = ?";
        String[] args = new String[]{actualiteDto.getIdActualite()};
        return context.getContentResolver().delete(OptProvider.ListActualite.LIST_ACTUALITE, where, args);
    }

    public static List<ActualiteEntity> listFromProvider(Context context) {
        List<ActualiteEntity> actualiteList = new ArrayList<>();

        // Query the content provider to get a cursor of Etape
        String sortOrder = ActualiteInterface.DATE + " DESC";
        Cursor cursorListActualite = context.getContentResolver().query(OptProvider.ListActualite.LIST_ACTUALITE, null, null, null, sortOrder);

        if (cursorListActualite != null) {
            while (cursorListActualite.moveToNext()) {
                ActualiteEntity actualiteEntity = getFromCursor(cursorListActualite);
                actualiteList.add(actualiteEntity);
            }
            cursorListActualite.close();
        }
        return actualiteList;
    }

    private static ContentValues putToContentValues(ActualiteDto actualite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITRE, actualite.getTitre());
        contentValues.put(CONTENU, actualite.getContenu());
        contentValues.put(TYPE, actualite.getTitre());
        contentValues.put(DATE, convertDateDtoToEntity(actualite.getDate()));
        return contentValues;
    }

    private static ActualiteEntity getFromCursor(Cursor cursor) {
        return uOrm.fromCursor(cursor, ActualiteEntity.class);
    }

    private static boolean exist(Context context, ActualiteDto actualite) {
        String[] args = new String[]{
                actualite.getIdActualite(),
                String.valueOf(DateConverter.convertDateDtoToEntity(actualite.getDate())),
                actualite.getContenu(),
                actualite.getType(),
                actualite.getTitre()};

        Cursor cursor = context.getContentResolver()
                .query(OptProvider.ListActualite.LIST_ACTUALITE, null, mWhereActualiteExistenceWhere, args, null);
        if ((cursor != null) && (cursor.moveToFirst())) {
            cursor.close();
            return true;
        }
        return false;
    }
}