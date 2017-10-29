package nc.opt.mobile.optmobile.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

import nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface;
import nc.opt.mobile.optmobile.provider.interfaces.AgenceInterface;
import nc.opt.mobile.optmobile.provider.interfaces.ColisInterface;
import nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface;
import nc.opt.mobile.optmobile.utils.DateConverter;

import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.CONTENU;
import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.DATE;
import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.DISMISSABLE;
import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.DISMISSED;
import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.TITRE;
import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.TYPE;

/**
 * Created by orlanth23 on 01/07/2017.
 */

@Database(version = OptDatabase.VERSION, packageName = "nc.opt.mobile.optmobile")
public class OptDatabase {
    static final int VERSION = 21;

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

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {
        String sql = "INSERT INTO " + ACTUALITE + " (" + TITRE + "," + CONTENU + "," + DATE + "," + TYPE + "," + DISMISSED + "," + DISMISSABLE + ") ";
        sql += "VALUES ('Bienvenue', 'Votre application Colis NC vous permet de suivre l''acheminement de vos colis à destination de Nouvelle Calédonie.\n" +
                "Commencez à l''utiliser dès à présent en ouvrant le menu latéral droit puis en cliquant sur Suivi des colis.', ";
        sql += "'" + DateConverter.getNowEntity() + "', '1', 0, 0)";
        db.execSQL(sql);
    }

    @OnUpgrade
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "INSERT INTO " + ACTUALITE + " (" + TITRE + "," + CONTENU + "," + DATE + "," + TYPE + "," + DISMISSED + "," + DISMISSABLE + ") ";
        sql += "VALUES ('Bienvenue', 'Votre application Colis NC vous permet de suivre l''acheminement de vos colis à destination de Nouvelle Calédonie.\n" +
                "Commencez à l''utiliser dès à présent en ouvrant le menu latéral droit puis en cliquant sur Suivi des colis.', ";
        sql += "'" + DateConverter.getNowEntity() + "', '1', 0, 0)";
        db.execSQL(sql);
    }
}
