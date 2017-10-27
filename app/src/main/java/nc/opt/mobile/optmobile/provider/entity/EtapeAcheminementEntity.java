package nc.opt.mobile.optmobile.provider.entity;

import org.chalup.microorm.annotations.Column;

import nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface;

/**
 * Created by 2761oli on 11/10/2017.
 */

public class EtapeAcheminementEntity {
    @Column(EtapeAcheminementInterface.ID_ETAPE_ACHEMINEMENT)
    private String idEtapeAcheminement;

    @Column(EtapeAcheminementInterface.ID_COLIS)
    private String idColis;

    @Column(EtapeAcheminementInterface.DATE)
    private Long date;

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

    public EtapeAcheminementEntity(String idEtapeAcheminement, String idColis, Long date, String pays, String localisation, String description, String commentaire) {
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

        EtapeAcheminementEntity etapeEntity = (EtapeAcheminementEntity) o;

        if (idEtapeAcheminement != null ? !idEtapeAcheminement.equals(etapeEntity.idEtapeAcheminement) : etapeEntity.idEtapeAcheminement != null)
            return false;
        if (idColis != null ? !idColis.equals(etapeEntity.idColis) : etapeEntity.idColis != null)
            return false;
        if (date != null ? !date.equals(etapeEntity.date) : etapeEntity.date != null) return false;
        if (pays != null ? !pays.equals(etapeEntity.pays) : etapeEntity.pays != null) return false;
        if (localisation != null ? !localisation.equals(etapeEntity.localisation) : etapeEntity.localisation != null)
            return false;
        if (description != null ? !description.equals(etapeEntity.description) : etapeEntity.description != null)
            return false;
        return commentaire != null ? commentaire.equals(etapeEntity.commentaire) : etapeEntity.commentaire == null;

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

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
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
