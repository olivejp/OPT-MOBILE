package nc.opt.mobile.optmobile.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by 2761oli on 11/10/2017.
 */
@Entity(tableName = "etape", indices = {
        @Index("idColis"),
        @Index("origine")
},
foreignKeys = @ForeignKey(entity = ColisEntity.class, parentColumns = "idColis", childColumns = "idColis", onDelete = CASCADE))
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

    @PrimaryKey
    protected String idEtapeAcheminement;
    protected String idColis;
    protected Long date;
    protected String pays;
    protected String localisation;
    protected String description;
    protected String commentaire;
    protected String status;

    @TypeConverters(OrigineConverter.class)
    public EtapeOrigine origine;

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
