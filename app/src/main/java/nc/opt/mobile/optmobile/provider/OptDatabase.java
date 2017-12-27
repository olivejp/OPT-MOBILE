package nc.opt.mobile.optmobile.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;
import net.simonvt.schematic.compiler.DbDescriptionInterface;

import org.chalup.microorm.MicroOrm;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.provider.entity.ActualiteEntity;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;
import nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface;
import nc.opt.mobile.optmobile.provider.interfaces.AgenceInterface;
import nc.opt.mobile.optmobile.provider.interfaces.ColisInterface;
import nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface;
import nc.opt.mobile.optmobile.utils.DateConverter;

import static nc.opt.mobile.optmobile.provider.OptDatabase.DESCRIPTION_TABLE_NAME;
import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.CONTENU;
import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.DATE;
import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.DISMISSABLE;
import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.DISMISSED;
import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.TITRE;
import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.TYPE;

/**
 * Created by orlanth23 on 01/07/2017.
 */

@Database(version = OptDatabase.VERSION, packageName = "nc.opt.mobile.optmobile", createDescriptionTable = true, descriptionTableName = DESCRIPTION_TABLE_NAME)
public class OptDatabase {
    static final int VERSION = 24;

    static final String DESCRIPTION_TABLE_NAME = "opt_description";

    private static final MicroOrm uOrm = new MicroOrm();

    private OptDatabase() {
    }

    @Table(AgenceInterface.class)
    static final String AGENCIES = "opt_agencies";

    @Table(ColisInterface.class)
    static final String COLIS = "opt_colis";

    @Table(EtapeAcheminementInterface.class)
    static final String ETAPE_ACHEMINEMENT = "opt_etape_acheminement";

    @Table(ActualiteInterface.class)
    static final String ACTUALITE = "opt_actualite";

    private static final String CREATE_AGENCIES = "CREATE TABLE " + AGENCIES + " ("
            + AgenceInterface.OBJECTID + " INTEGER NOT NULL PRIMARY KEY,"
            + AgenceInterface.TEXTE + " TEXT,"
            + AgenceInterface.TYPE + " TEXT,"
            + AgenceInterface.NOM + " TEXT NOT NULL,"
            + AgenceInterface.ADRESSE + " TEXT,"
            + AgenceInterface.CODE_POSTAL + " TEXT,"
            + AgenceInterface.VILLE + " TEXT,"
            + AgenceInterface.TEL + " TEXT,"
            + AgenceInterface.FAX + " TEXT,"
            + AgenceInterface.HORAIRE + " TEXT,"
            + AgenceInterface.DAB_INTERNE + " INTEGER,"
            + AgenceInterface.DAB_EXTERNE + " INTEGER,"
            + AgenceInterface.CONSEILLER_FINANCIER + " INTEGER,"
            + AgenceInterface.CONSEILLER_TELECOM + " INTEGER,"
            + AgenceInterface.CONSEILLER_POLYVALENT + " INTEGER,"
            + AgenceInterface.CONSEILLER_POSTAL + " INTEGER,"
            + AgenceInterface.GLOBALID + " TEXT,"
            + AgenceInterface.LATITUDE + " REAL,"
            + AgenceInterface.LONGITUDE + " REAL,"
            + AgenceInterface.TYPE_GEOMETRY + " TEXT)";

    private static final String CREATE_COLIS = "CREATE TABLE " + COLIS + " ("
            + ColisInterface.ID_COLIS + " TEXT NOT NULL PRIMARY KEY,"
            + ColisInterface.DESCRIPTION + " TEXT,"
            + ColisInterface.LAST_UPDATE + " TEXT,"
            + ColisInterface.LAST_UPDATE_SUCCESSFUL + " TEXT,"
            + ColisInterface.SLUG + " TEXT,"
            + ColisInterface.DELETED + " TEXT)";

    private static final String CREATE_ETAPE_ACHEMINEMENT = "CREATE TABLE " + ETAPE_ACHEMINEMENT + " ("
            + EtapeAcheminementInterface.ID_ETAPE_ACHEMINEMENT + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + EtapeAcheminementInterface.ID_COLIS + " TEXT,"
            + EtapeAcheminementInterface.DATE + " TEXT,"
            + EtapeAcheminementInterface.PAYS + " TEXT,"
            + EtapeAcheminementInterface.LOCALISATION + " TEXT,"
            + EtapeAcheminementInterface.DESCRIPTION + " TEXT,"
            + EtapeAcheminementInterface.STATUS + " TEXT,"
            + EtapeAcheminementInterface.COMMENTAIRE + " TEXT)";

    private static final String CREATE_ACTUALITE = "CREATE TABLE " + ACTUALITE + " ("
            + ActualiteInterface.ID_ACTUALITE + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + ActualiteInterface.ID_FIREBASE + " TEXT UNIQUE ON CONFLICT ABORT,"
            + ActualiteInterface.DATE + " TEXT,"
            + ActualiteInterface.TYPE + " TEXT,"
            + ActualiteInterface.TITRE + " TEXT,"
            + ActualiteInterface.CONTENU + " TEXT,"
            + ActualiteInterface.DISMISSABLE + " INTEGER,"
            + ActualiteInterface.DISMISSED + " INTEGER)";

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {
        String insertInto = "INSERT INTO " + ACTUALITE;
        String welcome = context.getString(R.string.welcome);
        String description = context.getString(R.string.message_bienvenue);
        String columns = " (" + TITRE + "," + CONTENU + "," + DATE + "," + TYPE + "," + DISMISSED + "," + DISMISSABLE + ") ";
        String values = "VALUES ('" + welcome + "', '" + description + "', '" + DateConverter.getNowEntity() + "', '1', 0, 0)";
        db.execSQL(insertInto.concat(columns).concat(values));
    }

    @OnUpgrade
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Récupération des anciennes données
        List<ColisEntity> listColis = new ArrayList<>();
        List<EtapeEntity> listEtape = new ArrayList<>();
        List<ActualiteEntity> listActualite = new ArrayList<>();

        retreiveDataFromVersion(db, oldVersion, COLIS, listColis, ColisEntity.class);
        retreiveDataFromVersion(db, oldVersion, ETAPE_ACHEMINEMENT, listEtape, EtapeEntity.class);
        retreiveDataFromVersion(db, oldVersion, ACTUALITE, listActualite, ActualiteEntity.class);

        // Suppression des anciennes tables
        db.execSQL("DROP TABLE " + ETAPE_ACHEMINEMENT);
        db.execSQL("DROP TABLE " + COLIS);
        db.execSQL("DROP TABLE " + ACTUALITE);

        // Création des nouvelles tables
        db.execSQL(CREATE_ETAPE_ACHEMINEMENT);
        db.execSQL(CREATE_COLIS);
        db.execSQL(CREATE_ACTUALITE);

        // Réinsertion des données
        insertDataFromList(db, COLIS, listColis);
        insertDataFromList(db, ETAPE_ACHEMINEMENT, listEtape);
        insertDataFromList(db, ACTUALITE, listActualite);
    }

    /**
     * Get list of the columns names for the given table name and db version.
     *
     * @param db
     * @param oldVersion
     * @param tableName
     * @return List<String> Containing all the columns names, list will be empty if no data.
     */
    private static List<String> catchDescription(SQLiteDatabase db, int oldVersion, String tableName) {
        List<String> listColumnNames = new ArrayList<>();

        String selection = DbDescriptionInterface.DB_VERSION + " = ? AND " + DbDescriptionInterface.TABLE_NAME + " = ?";
        String[] args = new String[]{String.valueOf(oldVersion), tableName};

        Cursor cursorDescription = db.query(DESCRIPTION_TABLE_NAME, null, selection, args, null, null, null);
        while (cursorDescription.moveToNext()) {
            listColumnNames.add(cursorDescription.getString(cursorDescription.getColumnIndex(DbDescriptionInterface.COLUMN_NAME)));
        }
        cursorDescription.close();
        return listColumnNames;
    }

    /**
     * Retreive datas from the DB for the version given and the table name.
     * @param db
     * @param version
     * @param tableName
     * @param list
     * @param klassEntity
     * @param <T>
     */
    private static <T> void retreiveDataFromVersion(SQLiteDatabase db, int version, String tableName, List<T> list, Class<T> klassEntity) {
        List<String> columns = catchDescription(db, version, tableName);
        if (!columns.isEmpty()) {
            String[] columnsArray = columns.toArray(new String[0]);
            Cursor cursor = db.query(tableName, columnsArray, null, null, null, null, null);
            while(cursor.moveToNext()) {
                list.add(uOrm.fromCursor(cursor, klassEntity));
            }
            cursor.close();
        }
    }

    /**
     * Insert datas from the list to the specified table
     *
     * @param db
     * @param tableName
     * @param list
     * @param <T>
     */
    private static <T> void insertDataFromList(@NotNull SQLiteDatabase db, @NotNull String tableName, @NotNull List<T> list) {
        for (T entity : list) {
            ContentValues contentValues = uOrm.toContentValues(entity);
            db.insert(tableName, null, contentValues);
        }
    }
}
