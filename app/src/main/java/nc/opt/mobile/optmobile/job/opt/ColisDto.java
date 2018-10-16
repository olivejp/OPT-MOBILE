package nc.opt.mobile.optmobile.job.opt;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by 2761oli on 05/10/2017.
 */

public class ColisDto implements Parcelable {

    private String idColis;
    private String description;
    private List<EtapeDto> etapeDtoArrayList;
    private String slug;

    public ColisDto() {
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

    public List<EtapeDto> getEtapeDtoArrayList() {
        return etapeDtoArrayList;
    }

    public void setEtapeDtoArrayList(List<EtapeDto> etapeDtoArrayList) {
        this.etapeDtoArrayList = etapeDtoArrayList;
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
        dest.writeTypedList(this.etapeDtoArrayList);
        dest.writeString(this.slug);
    }

    protected ColisDto(Parcel in) {
        this.idColis = in.readString();
        this.description = in.readString();
        this.etapeDtoArrayList = in.createTypedArrayList(EtapeDto.CREATOR);
        this.slug = in.readString();
    }

    public static final Creator<ColisDto> CREATOR = new Creator<ColisDto>() {
        @Override
        public ColisDto createFromParcel(Parcel source) {
            return new ColisDto(source);
        }

        @Override
        public ColisDto[] newArray(int size) {
            return new ColisDto[size];
        }
    };
}
