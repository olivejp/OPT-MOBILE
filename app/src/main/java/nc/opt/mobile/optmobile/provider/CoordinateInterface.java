package nc.opt.mobile.optmobile.provider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by orlanth23 on 10/08/2017.
 */
public interface CoordinateInterface {

    @AutoIncrement
    @DataType(INTEGER)
    @PrimaryKey
    @NotNull
    String COORDINATEID = "coordinate_id";

    @DataType(TEXT)
    String OBJECTID = "object_id";

    @DataType(REAL)
    String LATITUDE = "latitude";

    @DataType(REAL)
    String LONGITUDE = "longitude";

}
