package nc.opt.mobile.opt_mobile.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by orlanth23 on 10/08/2017.
 */

@ContentProvider(authority = AgencyProvider.AUTHORITY, database = AgencyDatabase.class)
public class AgencyProvider {
    static final String AUTHORITY = "nc.opt.mobile.AgencyProvider";

    private static final String AGENCY = "agency";

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
}
