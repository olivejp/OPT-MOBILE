package nc.opt.mobile.optmobile.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by 2761oli on 05/10/2017.
 */

public class Colis implements Parcelable {

    private String idColis;

    private List<EtapeAcheminementDto> etapeAcheminementDtoArrayList;

    public Colis() {
    }

    public Colis(String idColis, List<EtapeAcheminementDto> etapeAcheminementDtoArrayList) {
        this.idColis = idColis;
        this.etapeAcheminementDtoArrayList = etapeAcheminementDtoArrayList;
    }

    public String getIdColis() {
        return idColis;
    }

    public void setIdColis(String idColis) {
        this.idColis = idColis;
    }

    public List<EtapeAcheminementDto> getEtapeAcheminementDtoArrayList() {
        return etapeAcheminementDtoArrayList;
    }

    public void setEtapeAcheminementDtoArrayList(List<EtapeAcheminementDto> etapeAcheminementDtoArrayList) {
        this.etapeAcheminementDtoArrayList = etapeAcheminementDtoArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idColis);
        dest.writeTypedList(this.etapeAcheminementDtoArrayList);
    }

    private Colis(Parcel in) {
        this.idColis = in.readString();
        this.etapeAcheminementDtoArrayList = in.createTypedArrayList(EtapeAcheminementDto.CREATOR);
    }

    public static final Creator<Colis> CREATOR = new Creator<Colis>() {
        @Override
        public Colis createFromParcel(Parcel source) {
            return new Colis(source);
        }

        @Override
        public Colis[] newArray(int size) {
            return new Colis[size];
        }
    };
}
