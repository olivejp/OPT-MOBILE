package nc.opt.mobile.optmobile.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by orlanth23 on 10/08/2017.
 */

@ContentProvider(authority = AgencyProvider.AUTHORITY, database = AgencyDatabase.class, packageName = "nc.opt.mobile.optmobile")
public class AgencyProvider {
    static final String AUTHORITY = "nc.opt.mobile.AgencyProvider";

    private static final String AGENCY = "agency";
    private static final String PLAQUE = "plaque";
    private static final String COORDINATE = "coordinate";

    @TableEndpoint(table = AgencyDatabase.LIST_AGENCY)
    public static class ListAgency {
        @ContentUri(
                path = "agency",
                type = "vnd.android.cursor.dir/list",
                defaultSort = AgencyInterface.OBJECTID + " ASC")
        public static final Uri LIST_AGENCY = Uri.parse("content://" + AUTHORITY + "/" + AGENCY);

        @InexactContentUri(
                path = "agency/#",
                name = "AGENCY_ID",
                type = "vnd.android.cursor.item/list",
                whereColumn = AgencyInterface.OBJECTID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return Uri.parse("content://" + AUTHORITY + "/" + AGENCY + "/" + id);
        }

    }

    @TableEndpoint(table = AgencyDatabase.LIST_PLAQUE)
    public static class ListPlaque {
        @ContentUri(
                path = "plaque",
                type = "vnd.android.cursor.dir/list",
                defaultSort = PlaqueInterface.OBJECTID + " ASC")
        public static final Uri LIST_PLAQUE = Uri.parse("content://" + AUTHORITY + "/" + PLAQUE);

        @InexactContentUri(
                path = "plaque/#",
                name = "PLAQUE_ID",
                type = "vnd.android.cursor.item/list",
                whereColumn = PlaqueInterface.OBJECTID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return Uri.parse("content://" + AUTHORITY + "/" + PLAQUE + "/" + id);
        }

    }

    @TableEndpoint(table = AgencyDatabase.LIST_COORDINATE)
    public static class ListCoordinate {
        @ContentUri(
                path = "coordinate",
                type = "vnd.android.cursor.dir/list",
                defaultSort = CoordinateInterface.COORDINATEID + " ASC")
        public static final Uri LIST_COORDINATE = Uri.parse("content://" + AUTHORITY + "/" + COORDINATE);

        @InexactContentUri(
                path = "coordinate/#",
                name = "COORDINATE_ID",
                type = "vnd.android.cursor.item/list",
                whereColumn = CoordinateInterface.COORDINATEID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return Uri.parse("content://" + AUTHORITY + "/" + COORDINATE + "/" + id);
        }

    }

}
