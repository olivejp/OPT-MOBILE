package nc.opt.mobile.optmobile.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by orlanth23 on 01/07/2017.
 */

@Database(version = OptDatabase.VERSION, packageName = "nc.opt.mobile.optmobile")
class OptDatabase {
    static final int VERSION = 12;

    private OptDatabase() {
    }

    @Table(AgencyInterface.class)
    static final String AGENCIES = "opt_agencies";

    @Table(ColisInterface.class)
    static final String COLIS = "opt_colis";

    @Table(EtapeAcheminementInterface.class)
    static final String ETAPE_ACHEMINEMENT = "opt_etape_acheminement";
}
