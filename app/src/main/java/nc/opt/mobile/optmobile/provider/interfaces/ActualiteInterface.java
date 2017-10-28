package nc.opt.mobile.optmobile.provider.interfaces;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by 2761oli on 27/10/2017.
 */

public interface ActualiteInterface {

    @DataType(INTEGER)
    @AutoIncrement
    @PrimaryKey
    @NotNull
    String ID_ACTUALITE = "id_actualite";

    @Unique(onConflict = ConflictResolutionType.ABORT)
    @DataType(TEXT)
    String ID_FIREBASE = "id_firebase";

    @DataType(TEXT)
    String DATE = "date";

    @DataType(TEXT)
    String TYPE = "type";

    @DataType(TEXT)
    String TITRE = "titre";

    @DataType(TEXT)
    String CONTENU = "contenu";

    @DataType(INTEGER)
    String DISMISSABLE = "dismissable";

    @DataType(INTEGER)
    String DISMISSED = "dismissed";
}
