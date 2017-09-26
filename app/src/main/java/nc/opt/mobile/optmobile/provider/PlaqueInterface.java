package nc.opt.mobile.optmobile.provider;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by orlanth23 on 10/08/2017.
 */

public interface PlaqueInterface {

    @DataType(INTEGER)
    @PrimaryKey
    @NotNull
    String OBJECTID = "object_id";

    @DataType(TEXT)
    String NOM = "nom";

    @DataType(INTEGER)
    String ANNEE = "annee";

    @DataType(TEXT)
    String GLOBALID = "global_id";

    @DataType(REAL)
    String SHAPE_Length = "shape_length";

    @DataType(REAL)
    String SHAPE_Area = "shape_area";

}
