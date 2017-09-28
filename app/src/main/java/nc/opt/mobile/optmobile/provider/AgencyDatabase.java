package nc.opt.mobile.optmobile.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by orlanth23 on 01/07/2017.
 */

@Database(version = AgencyDatabase.VERSION, packageName = "nc.opt.mobile.optmobile")
class AgencyDatabase {
    static final int VERSION = 5;

    @Table(AgencyInterface.class)
    static final String LIST_AGENCY = "listAgency";

    @Table(PlaqueInterface.class)
    static final String LIST_PLAQUE = "listPlaque";

    @Table(CoordinateInterface.class)
    static final String LIST_COORDINATE = "listCoordinate";
}
