package nc.opt.mobile.optmobile.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

import nc.opt.mobile.optmobile.provider.entity.ActualiteEntity;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeAcheminementEntity;

/**
 * Created by orlanth23 on 01/07/2017.
 */

@Database(version = OptDatabase.VERSION, packageName = "nc.opt.mobile.optmobile")
class OptDatabase {
    static final int VERSION = 15;

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
}
