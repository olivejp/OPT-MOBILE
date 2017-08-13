package nc.opt.mobile.opt_mobile.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by orlanth23 on 01/07/2017.
 */

@Database(version = AgencyDatabase.VERSION)
class AgencyDatabase {
    static final int VERSION = 1;

    @Table(AgencyInterface.class)
    static final String LIST_AGENCY = "listAgency";

}
