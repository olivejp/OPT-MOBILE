package nc.opt.mobile.optmobile.provider.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.chalup.microorm.annotations.Column;

import java.util.ArrayList;
import java.util.List;

import nc.opt.mobile.optmobile.provider.interfaces.ColisInterface;

/**
 * Created by 2761oli on 11/10/2017.
 */

public class ColisEntity implements Parcelable {
    @Column(value = ColisInterface.ID_COLIS, treatNullAsDefault = true)
    private String idColis;

    @Column(value = ColisInterface.DESCRIPTION, treatNullAsDefault = true)
    private String description;

    @Column(value = ColisInterface.LAST_UPDATE, treatNullAsDefault = true)
    private Long lastUpdate;

    @Column(value = ColisInterface.LAST_UPDATE_SUCCESSFUL, treatNullAsDefault = true)
    private Long lastUpdateSuccessful;

    @Column(value = ColisInterface.DELETED)
    private Integer deleted;

    @Column(value = ColisInterface.SLUG)
    private String slug;

    private List<EtapeEntity> etapeAcheminementArrayList;

    public ColisEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ColisEntity)) return false;

        ColisEntity that = (ColisEntity) o;

        if (deleted.equals(that.deleted)) return false;
        if (idColis != null ? !idColis.equals(that.idColis) : that.idColis != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (lastUpdate != null ? !lastUpdate.equals(that.lastUpdate) : that.lastUpdate != null)
            return false;
        if (lastUpdateSuccessful != null ? !lastUpdateSuccessful.equals(that.lastUpdateSuccessful) : that.lastUpdateSuccessful != null)
            return false;
        return etapeAcheminementArrayList != null ? etapeAcheminementArrayList.equals(that.etapeAcheminementArrayList) : that.etapeAcheminementArrayList == null;
    }

    @Override
    public int hashCode() {
        int result = idColis != null ? idColis.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastUpdate != null ? lastUpdate.hashCode() : 0);
        result = 31 * result + (lastUpdateSuccessful != null ? lastUpdateSuccessful.hashCode() : 0);
        result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
        result = 31 * result + (slug != null ? slug.hashCode() : 0);
        result = 31 * result + (etapeAcheminementArrayList != null ? etapeAcheminementArrayList.hashCode() : 0);
        return result;
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

    public List<EtapeEntity> getEtapeAcheminementArrayList() {
        return etapeAcheminementArrayList;
    }

    public void setEtapeAcheminementArrayList(List<EtapeEntity> etapeAcheminementArrayList) {
        this.etapeAcheminementArrayList = etapeAcheminementArrayList;
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
        dest.writeList(this.etapeAcheminementArrayList);
    }

    protected ColisEntity(Parcel in) {
        this.idColis = in.readString();
        this.description = in.readString();
        this.lastUpdate = (Long) in.readValue(Long.class.getClassLoader());
        this.lastUpdateSuccessful = (Long) in.readValue(Long.class.getClassLoader());
        this.deleted = (Integer) in.readValue(Integer.class.getClassLoader());
        this.slug = in.readString();
        this.etapeAcheminementArrayList = new ArrayList<>();
        in.readList(this.etapeAcheminementArrayList, EtapeEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<ColisEntity> CREATOR = new Parcelable.Creator<ColisEntity>() {
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
