package nc.opt.mobile.opt_mobile.provider;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by orlanth23 on 10/08/2017.
 */

public interface AgencyInterface {

    @DataType(INTEGER)
    @PrimaryKey
    @NotNull
    String OBJECTID = "objectid";

    @DataType(TEXT)
    @NotNull
    String TEXTE = "texte";

    @DataType(TEXT)
    @NotNull
    String TYPE = "type";

    @DataType(TEXT)
    @NotNull
    String NOM = "nom";

    @DataType(TEXT)
    @NotNull
    String ADRESSE = "adresse";

    @DataType(TEXT)
    @NotNull
    String CODE_POSTAL = "code_postal";

    @DataType(TEXT)
    @NotNull
    String VILLE = "ville";

    @DataType(TEXT)
    @NotNull
    String TEL = "tel";

    @DataType(TEXT)
    @NotNull
    String FAX = "fax";

    @DataType(TEXT)
    @NotNull
    String HORAIRE = "horaire";

    @DataType(INTEGER)
    @NotNull
    String DAB_INTERNE = "dab_interne";

    @DataType(INTEGER)
    @NotNull
    String DAB_EXTERNE = "dab_externe";

    @DataType(INTEGER)
    @NotNull
    String CONSEILLER_FINANCIER = "conseiller_financier";

    @DataType(INTEGER)
    @NotNull
    String CONSEILLER_TELECOM = "conseiller_telecom";

    @DataType(INTEGER)
    @NotNull
    String CONSEILLER_POLYVALENT = "conseiller_polyvalent";

    @DataType(INTEGER)
    @NotNull
    String CONSEILLER_POSTAL = "conseiller_postal";

    @DataType(TEXT)
    @NotNull
    String GLOBALID = "globalid";

    @DataType(REAL)
    @NotNull
    String LATITUDE = "latitude";

    @DataType(REAL)
    @NotNull
    String LONGITUDE = "longitude";
}
