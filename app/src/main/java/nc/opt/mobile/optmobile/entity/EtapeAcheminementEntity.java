package nc.opt.mobile.optmobile.entity;

/**
 * Created by 2761oli on 11/10/2017.
 */

public class EtapeAcheminementEntity {

    private String id_etape_acheminement;
    private String id_colis;
    private String date;
    private String pays;
    private String localisation;
    private String description;
    private String commentaire;

    public EtapeAcheminementEntity() {
    }

    public EtapeAcheminementEntity(String id_etape_acheminement, String id_colis, String date, String pays, String localisation, String description, String commentaire) {
        this.id_etape_acheminement = id_etape_acheminement;
        this.id_colis = id_colis;
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

        if (id_etape_acheminement != null ? !id_etape_acheminement.equals(that.id_etape_acheminement) : that.id_etape_acheminement != null)
            return false;
        if (id_colis != null ? !id_colis.equals(that.id_colis) : that.id_colis != null)
            return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (pays != null ? !pays.equals(that.pays) : that.pays != null) return false;
        if (localisation != null ? !localisation.equals(that.localisation) : that.localisation != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        return commentaire != null ? commentaire.equals(that.commentaire) : that.commentaire == null;

    }

    public String getId_etape_acheminement() {
        return id_etape_acheminement;
    }

    public void setId_etape_acheminement(String id_etape_acheminement) {
        this.id_etape_acheminement = id_etape_acheminement;
    }

    public String getId_colis() {
        return id_colis;
    }

    public void setId_colis(String id_colis) {
        this.id_colis = id_colis;
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
}
