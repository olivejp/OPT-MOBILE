package nc.opt.mobile.optmobile.provider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by orlanth23 on 10/08/2017.
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
}
