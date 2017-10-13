package nc.opt.mobile.optmobile.provider;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by orlanth23 on 10/08/2017.
 */

public interface ColisInterface {

    @DataType(TEXT)
    @PrimaryKey
    @NotNull
    String ID_COLIS = "id_colis";

    @DataType(TEXT)
    String DESCRIPTION = "description";

    @DataType(TEXT)
    String LAST_UPDATE = "last_update";

    @DataType(TEXT)
    String LAST_UPDATE_SUCCESSFUL = "last_update_successful";
}
