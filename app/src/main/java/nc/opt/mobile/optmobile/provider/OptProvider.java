package nc.opt.mobile.optmobile.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface;
import nc.opt.mobile.optmobile.provider.interfaces.AgenceInterface;
import nc.opt.mobile.optmobile.provider.interfaces.ColisInterface;
import nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface;
import nc.opt.mobile.optmobile.provider.interfaces.ShedlockInterface;

import static nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface.ID_FIREBASE;

/**
 * Created by orlanth23 on 10/08/2017.
 */

@ContentProvider(authority = OptProvider.AUTHORITY, database = OptDatabase.class, packageName = "nc.opt.mobile.optmobile")
public class OptProvider {
    static final String AUTHORITY = "nc.opt.mobile.OptProvider";

    private static final String AGENCY = "agency";
    private static final String COLIS = "colis";
    private static final String ETAPE_ACHEMINEMENT = "etape_acheminement";
    private static final String ACTUALITE = "actualite";

    private OptProvider() {
    }

    @TableEndpoint(table = OptDatabase.AGENCIES)
    public static class ListAgency {
        private ListAgency() {
        }

        @ContentUri(
                path = "agency",
                type = "vnd.android.cursor.dir/list",
                defaultSort = AgenceInterface.OBJECTID + " ASC")
        public static final Uri LIST_AGENCY = Uri.parse("content://" + AUTHORITY + "/" + AGENCY);
    }

    @TableEndpoint(table = OptDatabase.COLIS)
    public static class ListColis {
        private ListColis() {
        }

        @ContentUri(
                path = "colis",
                type = "vnd.android.cursor.dir/list",
                defaultSort = ColisInterface.ID_COLIS + " ASC")
        public static final Uri LIST_COLIS = Uri.parse("content://" + AUTHORITY + "/" + COLIS);

        @InexactContentUri(
                path = "colis/*",
                name = "COLIS_ID",
                type = "vnd.android.cursor.item/list",
                whereColumn = ColisInterface.ID_COLIS,
                pathSegment = 1)
        public static Uri withId(String id) {
            return Uri.parse("content://" + AUTHORITY + "/" + COLIS + "/" + id);
        }
    }

    @TableEndpoint(table = OptDatabase.ETAPE_ACHEMINEMENT)
    public static class ListEtapeAcheminement {
        private ListEtapeAcheminement() {
        }

        @ContentUri(
                path = "etape_acheminement",
                type = "vnd.android.cursor.dir/list",
                defaultSort = EtapeAcheminementInterface.ID_ETAPE_ACHEMINEMENT + " ASC")
        public static final Uri LIST_ETAPE = Uri.parse("content://" + AUTHORITY + "/" + ETAPE_ACHEMINEMENT);

        @InexactContentUri(
                path = "etape_acheminement/id_colis/*",
                name = "COLIS_ID",
                type = "vnd.android.cursor.dir/list",
                whereColumn = EtapeAcheminementInterface.ID_COLIS,
                pathSegment = 2)
        public static Uri withIdColis(String idColis) {
            return Uri.parse("content://" + AUTHORITY + "/" + ETAPE_ACHEMINEMENT + "/" + ColisInterface.ID_COLIS + "/" + idColis);
        }
    }

    @TableEndpoint(table = OptDatabase.ACTUALITE)
    public static class ListActualite {
        private ListActualite() {
        }

        @ContentUri(
                path = "actualite",
                type = "vnd.android.cursor.dir/list",
                defaultSort = ActualiteInterface.ID_ACTUALITE + " ASC")
        public static final Uri LIST_ACTUALITE = Uri.parse("content://" + AUTHORITY + "/" + ACTUALITE);

        @InexactContentUri(
                path = "actualite/#",
                name = "ACTUALITE_ID",
                type = "vnd.android.cursor.item/item",
                whereColumn = ActualiteInterface.ID_ACTUALITE,
                pathSegment = 1)
        public static Uri withId(int id) {
            return Uri.parse("content://" + AUTHORITY + "/" + ACTUALITE + "/" + id);
        }

        @InexactContentUri(
                path = "actualite/id_firebase/*",
                name = "FIREBASE_ID",
                type = "vnd.android.cursor.item/item",
                whereColumn = ActualiteInterface.ID_FIREBASE,
                pathSegment = 2)
        public static Uri withFirebaseId(String firebaseId) {
            return Uri.parse("content://" + AUTHORITY + "/" + ACTUALITE + "/" + ID_FIREBASE + "/" + firebaseId);
        }
    }

    @TableEndpoint(table = OptDatabase.SHEDLOCK)
    public static class Shedlock {
        private Shedlock() {
        }

        @ContentUri(
                path = "shedlock",
                type = "vnd.android.cursor.dir/list",
                defaultSort = ActualiteInterface.ID_ACTUALITE + " ASC")
        public static final Uri LIST_SHEDLOCK = Uri.parse("content://" + AUTHORITY + "/" + ACTUALITE);

        @InexactContentUri(
                path = "shedlock/#",
                name = "SHEDLOCK_ID",
                type = "vnd.android.cursor.item/item",
                whereColumn = ShedlockInterface.ID_SHEDLOCK,
                pathSegment = 1)
        public static Uri withId(int id) {
            return Uri.parse("content://" + AUTHORITY + "/" + ACTUALITE + "/" + id);
        }
    }
}
