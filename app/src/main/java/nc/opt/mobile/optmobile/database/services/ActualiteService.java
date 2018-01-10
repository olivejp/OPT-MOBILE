package nc.opt.mobile.optmobile.database.services;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.chalup.microorm.MicroOrm;

import java.util.ArrayList;
import java.util.List;

import nc.opt.mobile.optmobile.domain.suivi.ActualiteDto;
import nc.opt.mobile.optmobile.database.interfaces.ActualiteInterface;
import nc.opt.mobile.optmobile.utils.DateConverter;

import static nc.opt.mobile.optmobile.database.interfaces.ActualiteInterface.CONTENU;
import static nc.opt.mobile.optmobile.database.interfaces.ActualiteInterface.DATE;
import static nc.opt.mobile.optmobile.database.interfaces.ActualiteInterface.DISMISSABLE;
import static nc.opt.mobile.optmobile.database.interfaces.ActualiteInterface.DISMISSED;
import static nc.opt.mobile.optmobile.database.interfaces.ActualiteInterface.ID_ACTUALITE;
import static nc.opt.mobile.optmobile.database.interfaces.ActualiteInterface.ID_FIREBASE;
import static nc.opt.mobile.optmobile.database.interfaces.ActualiteInterface.TITRE;
import static nc.opt.mobile.optmobile.database.interfaces.ActualiteInterface.TYPE;
import static nc.opt.mobile.optmobile.utils.DateConverter.convertDateDtoToEntity;

/**
 * Created by 2761oli on 25/10/2017.
 */

public class ActualiteService {

    private ActualiteService() {
    }

    private static final MicroOrm uOrm = new MicroOrm();

    public static Long insertActualite(Context context, ActualiteDto actualiteDto) {
        return insertActualite(context, putToContentValues(actualiteDto));
    }

    public static Long insertActualite(Context context, String titre, String contenu, boolean dismissable) {
        ActualiteEntity actualiteEntity = new ActualiteEntity();
        actualiteEntity.setTitre(titre);
        actualiteEntity.setType("1");
        actualiteEntity.setContenu(contenu);
        actualiteEntity.setDate(DateConverter.getNowEntity());
        actualiteEntity.setDismissed("0");
        actualiteEntity.setDismissable(dismissable ? "1" : "0");
        ContentValues contentValues = uOrm.toContentValues(actualiteEntity);
        return insertActualite(context, contentValues);
    }

    public static Long insertActualite(Context context, ActualiteEntity actualiteEntity) {
        ContentValues contentValues = uOrm.toContentValues(actualiteEntity);
        return insertActualite(context, contentValues);
    }

    private static Long insertActualite(Context context, ContentValues contentValues) {
        if (!existWithFirebaseId(context, contentValues.getAsString(ID_FIREBASE))) {
            contentValues.remove(ID_ACTUALITE);
            Uri uriInserted = context.getContentResolver().insert(OptProvider.ListActualite.LIST_ACTUALITE, contentValues);
            return ContentUris.parseId(uriInserted);
        }
        return 0L;
    }

    public static int updateActualite(Context context, ActualiteDto actualiteDto) {
        String where = ActualiteInterface.ID_ACTUALITE + " = ?";
        String[] args = new String[]{actualiteDto.getIdActualite()};
        return context.getContentResolver().update(OptProvider.ListActualite.LIST_ACTUALITE, putToContentValues(actualiteDto), where, args);
    }

    public static int delete(Context context, ActualiteDto actualiteDto) {
        String where = ActualiteInterface.ID_ACTUALITE + " = ?";
        String[] args = new String[]{actualiteDto.getIdActualite()};
        return context.getContentResolver().delete(OptProvider.ListActualite.LIST_ACTUALITE, where, args);
    }

    public static int dismiss(Context context, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ActualiteInterface.DISMISSED, "1");
        String where = ActualiteInterface.ID_ACTUALITE + " = ?";
        String[] args = new String[]{String.valueOf(id)};
        return context.getContentResolver().update(OptProvider.ListActualite.LIST_ACTUALITE, contentValues, where, args);
    }

    public static int deleteByIdFirebase(Context context, String firebaseId) {
        String where = ActualiteInterface.ID_FIREBASE + " = ?";
        String[] args = new String[]{firebaseId};
        return context.getContentResolver().delete(OptProvider.ListActualite.LIST_ACTUALITE, where, args);
    }

    public static List<ActualiteEntity> listActiveActualite(Context context) {
        List<ActualiteEntity> actualiteList = new ArrayList<>();

        // Query the content provider to get a cursor of Etape
        String sortOrder = ActualiteInterface.DATE + " DESC";
        String where = DISMISSED + " = ?";
        String[] args = new String[]{"0"};
        Cursor cursorListActualite = context.getContentResolver().query(OptProvider.ListActualite.LIST_ACTUALITE, null, where, args, sortOrder);

        if (cursorListActualite != null) {
            while (cursorListActualite.moveToNext()) {
                ActualiteEntity actualiteEntity = getFromCursor(cursorListActualite);
                actualiteList.add(actualiteEntity);
            }
            cursorListActualite.close();
        }
        return actualiteList;
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

    public static ActualiteEntity getById(Context context, int id) {
        ActualiteEntity actualiteEntity = null;
        Cursor cursor = context.getContentResolver().query(OptProvider.ListActualite.withId(id), null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                actualiteEntity = getFromCursor(cursor);
            }
            cursor.close();
        }
        return actualiteEntity;
    }

    public static boolean existWithFirebaseId(Context context, String firebaseId) {
        boolean exist = false;
        Cursor cursor = context.getContentResolver().query(OptProvider.ListActualite.withFirebaseId(firebaseId), null, null, null, null);
        if (cursor != null) {
            exist = cursor.getCount() > 0;
            cursor.close();
        }
        return exist;
    }

    public static ActualiteEntity getByFirebaseId(Context context, String firebaseId) {
        ActualiteEntity actualiteEntity = null;
        Cursor cursor = context.getContentResolver().query(OptProvider.ListActualite.withFirebaseId(firebaseId), null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                actualiteEntity = getFromCursor(cursor);
            }
            cursor.close();
        }
        return actualiteEntity;
    }

    private static ContentValues putToContentValues(ActualiteDto actualite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIREBASE, actualite.getIdFirebase());
        contentValues.put(DISMISSABLE, actualite.isDismissable());
        contentValues.put(DISMISSED, actualite.isDismissed());
        contentValues.put(TITRE, actualite.getTitre());
        contentValues.put(CONTENU, actualite.getContenu());
        contentValues.put(TYPE, actualite.getTitre());
        contentValues.put(DATE, convertDateDtoToEntity(actualite.getDate()));
        return contentValues;
    }

    private static ActualiteEntity getFromCursor(Cursor cursor) {
        return uOrm.fromCursor(cursor, ActualiteEntity.class);
    }
}
