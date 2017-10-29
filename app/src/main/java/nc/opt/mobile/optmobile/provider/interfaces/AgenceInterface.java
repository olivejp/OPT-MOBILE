package nc.opt.mobile.optmobile.provider.interfaces;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by orlanth23 on 10/08/2017.
 */

public interface AgenceInterface {

    @DataType(INTEGER)
    @PrimaryKey
    @NotNull
    String OBJECTID = "object_id";

    @DataType(TEXT)
    String TEXTE = "texte";

    @DataType(TEXT)
    String TYPE = "type";

    @DataType(TEXT)
    @NotNull
    String NOM = "nom";

    @DataType(TEXT)
    String ADRESSE = "adresse";

    @DataType(TEXT)
    String CODE_POSTAL = "code_postal";

    @DataType(TEXT)
    String VILLE = "ville";

    @DataType(TEXT)
    String TEL = "tel";

    @DataType(TEXT)
    String FAX = "fax";

    @DataType(TEXT)
    String HORAIRE = "horaire";

    @DataType(INTEGER)
    String DAB_INTERNE = "dab_interne";

    @DataType(INTEGER)
    String DAB_EXTERNE = "dab_externe";

    @DataType(INTEGER)
    String CONSEILLER_FINANCIER = "conseiller_financier";

    @DataType(INTEGER)
    String CONSEILLER_TELECOM = "conseiller_telecom";

    @DataType(INTEGER)
    String CONSEILLER_POLYVALENT = "conseiller_polyvalent";

    @DataType(INTEGER)
    String CONSEILLER_POSTAL = "conseiller_postal";

    @DataType(TEXT)
    String GLOBALID = "global_id";

    @DataType(REAL)
    String LATITUDE = "latitude";

    @DataType(REAL)
    String LONGITUDE = "longitude";

    @DataType(TEXT)
    String TYPE_GEOMETRY = "type_geometry";
}
