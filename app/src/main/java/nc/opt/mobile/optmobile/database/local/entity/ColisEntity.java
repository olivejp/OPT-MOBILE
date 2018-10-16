package nc.opt.mobile.optmobile.database.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import nc.opt.mobile.optmobile.DateConverter;

/**
 * Created by 2761oli on 11/10/2017.
 */

@Entity(tableName = "colis")
public class ColisEntity implements Parcelable {
    @NonNull
    @PrimaryKey
    private String idColis;
    private String description;
    private Long lastUpdate;
    private Long lastUpdateSuccessful;
    private Integer deleted;
    private String slug;
    private Integer fbLinked;
    private String afterShipId;
    private Integer delivered;

    public ColisEntity() {
    }

    public String getIdColis() {
        return idColis;
    }

    public void setIdColis(String idColis) {
        this.idColis = idColis;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ColisEntity buildDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getLastUpdateSuccessful() {
        return lastUpdateSuccessful;
    }

    public void setLastUpdateSuccessful(Long lastUpdateSuccessful) {
        this.lastUpdateSuccessful = lastUpdateSuccessful;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public boolean isDeleted() {
        return (deleted == 1);
    }

    /**
     * 0 = Active
     * 1 = Deleted
     * @param deleted
     */
    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getLastUpdateFormatte(){
        return DateConverter.convertDateEntityToUi(lastUpdate);
    }

    public Integer getFbLinked() {
        return fbLinked;
    }

    public void setFbLinked(Integer fbLinked) {
        this.fbLinked = fbLinked;
    }

    public String getAfterShipId() {
        return afterShipId;
    }

    public void setAfterShipId(String afterShipId) {
        this.afterShipId = afterShipId;
    }

    public Integer getDelivered() {
        return delivered;
    }

    public boolean isDelivered() {
        return (delivered != null && delivered == 1);
    }

    public void setDelivered(Integer delivered) {
        this.delivered = delivered;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idColis);
        dest.writeString(this.description);
        dest.writeValue(this.lastUpdate);
        dest.writeValue(this.lastUpdateSuccessful);
        dest.writeValue(this.deleted);
        dest.writeString(this.slug);
        dest.writeValue(this.fbLinked);
        dest.writeString(this.afterShipId);
        dest.writeValue(this.delivered);
    }

    protected ColisEntity(Parcel in) {
        this.idColis = in.readString();
        this.description = in.readString();
        this.lastUpdate = (Long) in.readValue(Long.class.getClassLoader());
        this.lastUpdateSuccessful = (Long) in.readValue(Long.class.getClassLoader());
        this.deleted = (Integer) in.readValue(Integer.class.getClassLoader());
        this.slug = in.readString();
        this.fbLinked = (Integer) in.readValue(Integer.class.getClassLoader());
        this.afterShipId = in.readString();
        this.delivered = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<ColisEntity> CREATOR = new Creator<ColisEntity>() {
        @Override
        public ColisEntity createFromParcel(Parcel source) {
            return new ColisEntity(source);
        }

        @Override
        public ColisEntity[] newArray(int size) {
            return new ColisEntity[size];
        }
    };

    @Override
    public String toString() {
        return "ColisEntity{" +
                "idColis='" + idColis + '\'' +
                ", description='" + description + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", lastUpdateSuccessful=" + lastUpdateSuccessful +
                ", deleted=" + deleted +
                ", slug='" + slug + '\'' +
                ", fbLinked=" + fbLinked +
                ", afterShipId='" + afterShipId + '\'' +
                ", delivered=" + delivered +
                '}';
    }
}
