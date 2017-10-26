package nc.opt.mobile.optmobile.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

import nc.opt.mobile.optmobile.provider.entity.ActualiteEntity;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeAcheminementEntity;
import nc.opt.mobile.optmobile.utils.DateConverter;

import static nc.opt.mobile.optmobile.provider.entity.ActualiteEntity.ActualiteInterface.CONTENU;
import static nc.opt.mobile.optmobile.provider.entity.ActualiteEntity.ActualiteInterface.DATE;
import static nc.opt.mobile.optmobile.provider.entity.ActualiteEntity.ActualiteInterface.TITRE;
import static nc.opt.mobile.optmobile.provider.entity.ActualiteEntity.ActualiteInterface.TYPE;

/**
 * Created by orlanth23 on 01/07/2017.
 */

@Database(version = OptDatabase.VERSION, packageName = "nc.opt.mobile.optmobile")
public class OptDatabase {
    static final int VERSION = 17;

    private OptDatabase() {
    }

    @Table(AgencyInterface.class)
    static final String AGENCIES = "opt_agencies";

    @Table(ColisEntity.ColisInterface.class)
    static final String COLIS = "opt_colis";

    @Table(EtapeAcheminementEntity.EtapeAcheminementInterface.class)
    static final String ETAPE_ACHEMINEMENT = "opt_etape_acheminement";

    @Table(ActualiteEntity.ActualiteInterface.class)
    static final String ACTUALITE = "opt_actualite";

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {
        String sql = "INSERT INTO " + ACTUALITE + " (" + TITRE + "," + CONTENU + "," + DATE + "," + TYPE + ") ";
        sql += "VALUES ('Bienvenue', 'Votre application Colis NC vous permet de suivre vos colis\nCommencez à l''utiliser dès à présent', ";
        sql += "'" + String.valueOf(DateConverter.getNowEntity()) + "', '1')";
        db.execSQL(sql);
    }

    @OnUpgrade
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "INSERT INTO " + ACTUALITE + " (" + TITRE + "," + CONTENU + "," + DATE + "," + TYPE + ") ";
        sql += "VALUES ('Bienvenue', 'Votre application Colis NC vous permet de suivre vos colis\nCommencez à l''utiliser dès à présent', ";
        sql += "'" + String.valueOf(DateConverter.getNowEntity()) + "', '1')";
        db.execSQL(sql);
    }
}
