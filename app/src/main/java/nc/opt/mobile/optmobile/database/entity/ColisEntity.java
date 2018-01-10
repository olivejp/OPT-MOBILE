package nc.opt.mobile.optmobile.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 2761oli on 11/10/2017.
 */

@Entity(tableName = "colis")
public class ColisEntity implements Parcelable {
    @PrimaryKey
    private String idColis;
    private String description;
    private Long lastUpdate;
    private Long lastUpdateSuccessful;
    private Integer deleted;
    private String slug;

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
    }

    protected ColisEntity(Parcel in) {
        this.idColis = in.readString();
        this.description = in.readString();
        this.lastUpdate = (Long) in.readValue(Long.class.getClassLoader());
        this.lastUpdateSuccessful = (Long) in.readValue(Long.class.getClassLoader());
        this.deleted = (Integer) in.readValue(Integer.class.getClassLoader());
        this.slug = in.readString();
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
}
