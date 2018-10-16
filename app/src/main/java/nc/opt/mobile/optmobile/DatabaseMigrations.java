package nc.opt.mobile.optmobile;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

/**
 * Created by 2761oli on 30/01/2018.
 */

public class DatabaseMigrations {

    private DatabaseMigrations() {
    }

    public static final Migration MIGRATION_1_2 = new Migration(1, 30) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Lecture de toutes les occurences des anciens colis
            Cursor optCursorColisOld = database.query("SELECT id_colis, description, last_update, last_update_successful, deleted FROM opt_colis");

            // Insertion des anciens codes colis dans la nouvelle table
            while (optCursorColisOld.moveToNext()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("idColis", optCursorColisOld.getString(optCursorColisOld.getColumnIndex("id_colis")));
                contentValues.put("description", optCursorColisOld.getString(optCursorColisOld.getColumnIndex("description")));
                contentValues.put("lastUpdate", optCursorColisOld.getString(optCursorColisOld.getColumnIndex("last_update")));
                contentValues.put("lastUpdateSuccessful", optCursorColisOld.getString(optCursorColisOld.getColumnIndex("last_update_successful")));
                contentValues.put("deleted", optCursorColisOld.getString(optCursorColisOld.getColumnIndex("deleted")));
                database.insert("colis", SQLiteDatabase.CONFLICT_REPLACE, contentValues);
            }

            // Suppression des tables historiques
            database.execSQL("DROP TABLE opt_etape_acheminement");
            database.execSQL("DROP TABLE opt_colis");
            database.execSQL("DROP TABLE opt_agencies");
        }
    };
}
