package nc.opt.mobile.optmobile.provider.interfaces;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by 2761oli on 27/10/2017.
 */

public interface EtapeAcheminementInterface {

    @DataType(INTEGER)
    @AutoIncrement
    @PrimaryKey
    @NotNull
    String ID_ETAPE_ACHEMINEMENT = "id_etape_acheminement";

    @DataType(TEXT)
    String ID_COLIS = "id_colis";

    @DataType(TEXT)
    String DATE = "date";

    @DataType(TEXT)
    String PAYS = "pays";

    @DataType(TEXT)
    String LOCALISATION = "localisation";

    @DataType(TEXT)
    String DESCRIPTION = "description";

    @DataType(TEXT)
    String COMMENTAIRE = "commentaire";

    @DataType(TEXT)
    String STATUS = "status";

    @DataType(TEXT)
    String ORIGINE = "origine";
}
