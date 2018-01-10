package nc.opt.mobile.optmobile.provider.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.chalup.microorm.annotations.Column;

import nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface;

/**
 * Created by 2761oli on 11/10/2017.
 */

public class EtapeEntity implements Parcelable {

    public enum EtapeOrigine {
        OPT("OPT"),
        AFTER_SHIP("AFTERSHIP");

        private final String value;

        private EtapeOrigine(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

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

    protected EtapeOrigine origine;

    public EtapeEntity() {
    }

    public EtapeEntity(String idEtapeAcheminement, String idColis, Long date, String pays, String localisation, String description, String commentaire, String status, EtapeOrigine origine) {
        this.idEtapeAcheminement = idEtapeAcheminement;
        this.idColis = idColis;
        this.date = date;
        this.pays = pays;
        this.localisation = localisation;
        this.description = description;
        this.commentaire = commentaire;
        this.status = status;
        this.origine = origine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EtapeEntity that = (EtapeEntity) o;

        if (idEtapeAcheminement != null ? !idEtapeAcheminement.equals(that.idEtapeAcheminement) : that.idEtapeAcheminement != null)
            return false;
        if (idColis != null ? !idColis.equals(that.idColis) : that.idColis != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (pays != null ? !pays.equals(that.pays) : that.pays != null) return false;
        if (localisation != null ? !localisation.equals(that.localisation) : that.localisation != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (commentaire != null ? !commentaire.equals(that.commentaire) : that.commentaire != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        return origine == that.origine;
    }

    @Override
    public int hashCode() {
        int result = idEtapeAcheminement != null ? idEtapeAcheminement.hashCode() : 0;
        result = 31 * result + (idColis != null ? idColis.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (pays != null ? pays.hashCode() : 0);
        result = 31 * result + (localisation != null ? localisation.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (commentaire != null ? commentaire.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (origine != null ? origine.hashCode() : 0);
        return result;
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

    public EtapeOrigine getOrigine() {
        return origine;
    }

    public void setOrigine(EtapeOrigine origine) {
        this.origine = origine;
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
        dest.writeInt(this.origine == null ? -1 : this.origine.ordinal());
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
        int tmpOrigine = in.readInt();
        this.origine = tmpOrigine == -1 ? null : EtapeOrigine.values()[tmpOrigine];
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
