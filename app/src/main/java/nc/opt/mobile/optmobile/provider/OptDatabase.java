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

import java.util.ArrayList;
import java.util.List;

import nc.opt.mobile.optmobile.provider.entity.ActualiteEntity;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeAcheminementEntity;
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
    static final int VERSION = 22;

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


    private static final String CREATE_AGENCIES = "CREATE TABLE opt_agencies ("
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

    private static final String CREATE_COLIS = "CREATE TABLE opt_colis ("
            + ColisInterface.ID_COLIS + " TEXT NOT NULL PRIMARY KEY,"
            + ColisInterface.DESCRIPTION + " TEXT,"
            + ColisInterface.LAST_UPDATE + " TEXT,"
            + ColisInterface.LAST_UPDATE_SUCCESSFUL + " TEXT,"
            + ColisInterface.DELETED + " TEXT)";

    private static final String CREATE_ETAPE_ACHEMINEMENT = "CREATE TABLE opt_etape_acheminement ("
            + EtapeAcheminementInterface.ID_ETAPE_ACHEMINEMENT + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + EtapeAcheminementInterface.ID_COLIS + " TEXT,"
            + EtapeAcheminementInterface.DATE + " TEXT,"
            + EtapeAcheminementInterface.PAYS + " TEXT,"
            + EtapeAcheminementInterface.LOCALISATION + " TEXT,"
            + EtapeAcheminementInterface.DESCRIPTION + " TEXT,"
            + EtapeAcheminementInterface.COMMENTAIRE + " TEXT)";

    private static final String CREATE_ACTUALITE = "CREATE TABLE opt_actualite ("
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
        String sql = "INSERT INTO " + ACTUALITE + " (" + TITRE + "," + CONTENU + "," + DATE + "," + TYPE + "," + DISMISSED + "," + DISMISSABLE + ") ";
        sql += "VALUES ('Bienvenue', 'Votre application Colis NC vous permet de suivre l''acheminement de vos colis à destination de Nouvelle Calédonie.\n" +
                "Commencez à l''utiliser dès à présent en ouvrant le menu latéral droit puis en cliquant sur Suivi des colis.', ";
        sql += "'" + DateConverter.getNowEntity() + "', '1', 0, 0)";
        db.execSQL(sql);
    }

    private static List<String> catchDescription(SQLiteDatabase db, int oldVersion, String tableName){
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

    private static <T> List<T> retreiveDataFromVersion(SQLiteDatabase db, int version, String tableName, Class<T> klass) {
        List<String> columns = catchDescription(db, version, tableName);
        if (!columns.isEmpty()) {
            String[] columnsArray = columns.toArray(new String[0]);
            Cursor cursor = db.query(tableName, columnsArray, null, null, null, null, null);
            return uOrm.listFromCursor(cursor, klass);
        }
        return new ArrayList<>();
    }

    private static <T> void insertInto(SQLiteDatabase db, String tableName, List<T> list){
        for (T entity : list) {
            ContentValues contentValues = uOrm.toContentValues(entity);
            db.insert(tableName, null, contentValues);
        }
    }

    @OnUpgrade
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Récupération des anciennes données
        List<ColisEntity> listColis = retreiveDataFromVersion(db, oldVersion, COLIS, ColisEntity.class);
        List<EtapeAcheminementEntity> listEtape = retreiveDataFromVersion(db, oldVersion, ETAPE_ACHEMINEMENT, EtapeAcheminementEntity.class);
        List<ActualiteEntity> listActualite = retreiveDataFromVersion(db, oldVersion, ACTUALITE, ActualiteEntity.class);

        // Suppression des anciennes tables et insertion des nouvelles
        db.execSQL("DROP TABLE " + ETAPE_ACHEMINEMENT);
        db.execSQL("DROP TABLE " + COLIS);
        db.execSQL("DROP TABLE " + ACTUALITE);

        // Création des nouvelles tables
        db.execSQL(CREATE_ETAPE_ACHEMINEMENT);
        db.execSQL(CREATE_COLIS);
        db.execSQL(CREATE_ACTUALITE);

        // Réinsertion des données
        insertInto(db, COLIS, listColis);
        insertInto(db, ETAPE_ACHEMINEMENT, listEtape);
        insertInto(db, ACTUALITE, listActualite);
    }
}
