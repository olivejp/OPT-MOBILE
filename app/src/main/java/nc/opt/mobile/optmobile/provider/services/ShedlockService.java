package nc.opt.mobile.optmobile.provider.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface;
import nc.opt.mobile.optmobile.provider.interfaces.ShedlockInterface;
import nc.opt.mobile.optmobile.utils.DateConverter;

import static io.reactivex.Observable.just;

/**
 * Created by 2761oli on 25/10/2017.
 */

public class ShedlockService {

    private ShedlockService() {
    }

    public static Observable waitUntilIsUnlocked(Context context) {
        return Observable.zip(
                Observable.interval(5, TimeUnit.SECONDS), observableIsLocked(context),
                (occurence, locked) -> {
                    if (occurence < 10) {
                        return Observable.just(locked);
                    } else {
                        return Observable.error(new Exception());
                    }
                });
    }

    public static Observable<AtomicBoolean> observableIsLocked(Context context) {
        return just(islocked(context));
    }

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

    public static synchronized boolean lock(Context context) {
        if (!islocked(context).get()) {
            String where = ActualiteInterface.ID_ACTUALITE + " = ?";
            String[] args = new String[]{"1"};
            ContentValues contentValues = new ContentValues();
            contentValues.put(ShedlockInterface.DATE, DateConverter.getNowEntity());
            contentValues.put(ShedlockInterface.LOCKED, "true");
            return context.getContentResolver().update(OptProvider.Shedlock.LIST_SHEDLOCK, contentValues, where, args) != 0;
        } else {
            return false;
        }
    }

    public static synchronized AtomicBoolean islocked(Context context) {
        AtomicBoolean atom = new AtomicBoolean(false);
        Cursor cursor = context.getContentResolver().query(OptProvider.Shedlock.withId(1), null, null, null, null);
        if (cursor == null) return atom;
        if (cursor.moveToFirst()) {
            if (cursor.getString(cursor.getColumnIndex(ShedlockInterface.LOCKED)).equals("true")) {
                cursor.close();
                atom.set(true);
                return atom;
            }
        }
        cursor.close();
        return atom;
    }

    public static int timeUntilLastLock(Context context) {
        // ToDo Implementer la méthode pour calculer depuis quand le lock est pris.
        return 1;
    }
}
