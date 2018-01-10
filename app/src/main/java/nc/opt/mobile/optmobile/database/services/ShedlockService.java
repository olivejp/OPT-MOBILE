package nc.opt.mobile.optmobile.database.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.util.concurrent.atomic.AtomicBoolean;

import nc.opt.mobile.optmobile.database.interfaces.ShedlockInterface;
import nc.opt.mobile.optmobile.utils.DateConverter;

/**
 * Created by 2761oli on 25/10/2017.
 */

public class ShedlockService {

    private static final String TAG = ShedlockService.class.getName();

    private ShedlockService() {
    }

    /**
     * @param context
     * @return
     */
    public static synchronized boolean releaseLock(Context context) {
        if (islocked(context).get()) {
            String where = ShedlockInterface.ID_SHEDLOCK + " = ?";
            String[] args = new String[]{"1"};
            ContentValues contentValues = new ContentValues();
            contentValues.put(ShedlockInterface.DATE, 0);
            contentValues.put(ShedlockInterface.LOCKED, "false");
            return context.getContentResolver().update(OptProvider.Shedlock.LIST_SHEDLOCK, contentValues, where, args) != 0;
        } else {
            return false;
        }
    }

    /**
     * @param context
     * @return
     */
    public static synchronized boolean lock(Context context) {
        if (!islocked(context).get()) {
            String where = ShedlockInterface.ID_SHEDLOCK + " = ?";
            String[] args = new String[]{"1"};
            ContentValues contentValues = new ContentValues();
            contentValues.put(ShedlockInterface.DATE, DateConverter.getNowEntity());
            contentValues.put(ShedlockInterface.LOCKED, "true");
            return context.getContentResolver().update(OptProvider.Shedlock.LIST_SHEDLOCK, contentValues, where, args) != 0;
        } else {
            return false;
        }
    }

    /**
     * @param context
     * @return
     */
    public static synchronized AtomicBoolean islocked(Context context) {
        AtomicBoolean atom = new AtomicBoolean(false);
        Cursor cursor = context.getContentResolver().query(OptProvider.Shedlock.withId(1), null, null, null, null);
        if (cursor == null) return atom;
        if (cursor.getCount() == 0) return atom;
        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(ShedlockInterface.LOCKED);
            if (cursor.getString(index).equals("true")) {
                cursor.close();
                atom.set(true);
                return atom;
            }
        }
        cursor.close();
        return atom;
    }

    /**
     * @param context
     * @return time in milliseconds since
     */
    public static long timeUntilLastLock(Context context) {
        long duration = 0L;
        Cursor cursor = context.getContentResolver().query(OptProvider.Shedlock.withId(1), null, null, null, null);
        if (cursor == null) return 0;
        if (cursor.getCount() == 0) return 0;
        try {
            if (cursor.moveToFirst()) {

                int lastShedlock = cursor.getInt(cursor.getColumnIndex(ShedlockInterface.DATE));
                duration = DateConverter.howLongSince(String.valueOf(lastShedlock), DateConverter.DatePattern.PATTERN_ENTITY);
            }
            cursor.close();
        } catch (ParseException e) {
            Log.e(TAG, "Erreur de parsing pour la date récupéré du Shedlock");
        }
        return duration;
    }
}
