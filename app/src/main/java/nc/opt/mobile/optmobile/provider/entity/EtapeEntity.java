package nc.opt.mobile.optmobile.provider.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.chalup.microorm.annotations.Column;

import nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface;

/**
 * Created by 2761oli on 11/10/2017.
 */

public class EtapeEntity implements Parcelable {
    @Column(EtapeAcheminementInterface.ID_ETAPE_ACHEMINEMENT)
    protected String idEtapeAcheminement;

    @Column(EtapeAcheminementInterface.ID_COLIS)
    protected String idColis;

    @Column(EtapeAcheminementInterface.DATE)
    protected Long date;

    @Column(EtapeAcheminementInterface.PAYS)
    protected String pays;

    @Column(EtapeAcheminementInterface.LOCALISATION)
    protected String localisation;

    @Column(EtapeAcheminementInterface.DESCRIPTION)
    protected String description;

    @Column(EtapeAcheminementInterface.COMMENTAIRE)
    protected String commentaire;

    @Column(EtapeAcheminementInterface.STATUS)
    protected String status;

    public EtapeEntity() {
    }

    public EtapeEntity(String idEtapeAcheminement, String idColis, Long date, String pays, String localisation, String description, String commentaire, String status) {
        this.idEtapeAcheminement = idEtapeAcheminement;
        this.idColis = idColis;
        this.date = date;
        this.pays = pays;
        this.localisation = localisation;
        this.description = description;
        this.commentaire = commentaire;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EtapeEntity etapeEntity = (EtapeEntity) o;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idEtapeAcheminement);
        dest.writeString(this.idColis);
        dest.writeValue(this.date);
        dest.writeString(this.pays);
        dest.writeString(this.localisation);
        dest.writeString(this.description);
        dest.writeString(this.commentaire);
        dest.writeString(this.status);
    }

    protected EtapeEntity(Parcel in) {
        this.idEtapeAcheminement = in.readString();
        this.idColis = in.readString();
        this.date = (Long) in.readValue(Long.class.getClassLoader());
        this.pays = in.readString();
        this.localisation = in.readString();
        this.description = in.readString();
        this.commentaire = in.readString();
        this.status = in.readString();
    }

    public static final Creator<EtapeEntity> CREATOR = new Creator<EtapeEntity>() {
        @Override
        public EtapeEntity createFromParcel(Parcel source) {
            return new EtapeEntity(source);
        }

        @Override
        public EtapeEntity[] newArray(int size) {
            return new EtapeEntity[size];
        }
    };
}
