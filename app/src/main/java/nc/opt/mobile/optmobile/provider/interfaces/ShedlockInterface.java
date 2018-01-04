package nc.opt.mobile.optmobile.provider.interfaces;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by 2761oli on 04/01/2018.
 */

public interface ShedlockInterface {

    @DataType(INTEGER)
    @AutoIncrement
    @PrimaryKey
    @NotNull
    String ID_SHEDLOCK = "id_shedlock";

    @DataType(TEXT)
    String LOCKED = "locked";

    @DataType(INTEGER)
    String DATE = "locked_date";
}
