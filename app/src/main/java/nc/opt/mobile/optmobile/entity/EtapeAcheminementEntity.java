package nc.opt.mobile.optmobile.entity;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import org.chalup.microorm.annotations.Column;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by 2761oli on 11/10/2017.
 */

public class EtapeAcheminementEntity {
    @Column(EtapeAcheminementInterface.ID_ETAPE_ACHEMINEMENT)
    private String idEtapeAcheminement;

    @Column(EtapeAcheminementInterface.ID_COLIS)
    private String idColis;

    @Column(EtapeAcheminementInterface.DATE)
    private String date;

    @Column(EtapeAcheminementInterface.PAYS)
    private String pays;

    @Column(EtapeAcheminementInterface.LOCALISATION)
    private String localisation;

    @Column(EtapeAcheminementInterface.DESCRIPTION)
    private String description;

    @Column(EtapeAcheminementInterface.COMMENTAIRE)
    private String commentaire;

    public EtapeAcheminementEntity() {
    }

    public EtapeAcheminementEntity(String idEtapeAcheminement, String idColis, String date, String pays, String localisation, String description, String commentaire) {
        this.idEtapeAcheminement = idEtapeAcheminement;
        this.idColis = idColis;
        this.date = date;
        this.pays = pays;
        this.localisation = localisation;
        this.description = description;
        this.commentaire = commentaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EtapeAcheminementEntity that = (EtapeAcheminementEntity) o;

        if (idEtapeAcheminement != null ? !idEtapeAcheminement.equals(that.idEtapeAcheminement) : that.idEtapeAcheminement != null)
            return false;
        if (idColis != null ? !idColis.equals(that.idColis) : that.idColis != null)
            return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (pays != null ? !pays.equals(that.pays) : that.pays != null) return false;
        if (localisation != null ? !localisation.equals(that.localisation) : that.localisation != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        return commentaire != null ? commentaire.equals(that.commentaire) : that.commentaire == null;

    }

    public String getIdEtapeAcheminement() {
        return idEtapeAcheminement;
    }

    public void setIdEtapeAcheminement(String idEtapeAcheminement) {
        this.idEtapeAcheminement = idEtapeAcheminement;
    }

    public String getIdColis() {
        return idColis;
    }

    public void setIdColis(String idColis) {
        this.idColis = idColis;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

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
}
