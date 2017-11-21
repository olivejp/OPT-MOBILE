package nc.opt.mobile.optmobile.provider.interfaces;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by 2761oli on 27/10/2017.
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

    @DataType(INTEGER)
    String DELETED = "deleted";
}
