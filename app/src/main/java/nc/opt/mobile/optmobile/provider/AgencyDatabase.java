package nc.opt.mobile.optmobile.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

import nc.opt.mobile.optmobile.domain.Feature;

/**
 * Created by orlanth23 on 01/07/2017.
 */

@Database(version = AgencyDatabase.VERSION, packageName = "nc.opt.mobile.optmobile")
class AgencyDatabase {
    static final int VERSION = 3;

    @Table(AgencyInterface.class)
    static final String LIST_AGENCY = "listAgency";
}
