package nc.opt.mobile.optmobile.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.chalup.microorm.annotations.Column;

import java.util.List;

import nc.opt.mobile.optmobile.provider.AgencyInterface;
import nc.opt.mobile.optmobile.provider.ColisInterface;

/**
 * Created by 2761oli on 05/10/2017.
 */

public class Colis implements Parcelable {

    @Column(ColisInterface.ID_COLIS)
    private String idColis;

    private List<EtapeAcheminement> etapeAcheminementArrayList;

    public Colis() {
    }

    public Colis(String idColis, List<EtapeAcheminement> etapeAcheminementArrayList) {
        this.idColis = idColis;
        this.etapeAcheminementArrayList = etapeAcheminementArrayList;
    }

    public String getIdColis() {
        return idColis;
    }

    public void setIdColis(String idColis) {
        this.idColis = idColis;
    }

    public List<EtapeAcheminement> getEtapeAcheminementArrayList() {
        return etapeAcheminementArrayList;
    }

    public void setEtapeAcheminementArrayList(List<EtapeAcheminement> etapeAcheminementArrayList) {
        this.etapeAcheminementArrayList = etapeAcheminementArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idColis);
        dest.writeTypedList(this.etapeAcheminementArrayList);
    }

    private Colis(Parcel in) {
        this.idColis = in.readString();
        this.etapeAcheminementArrayList = in.createTypedArrayList(EtapeAcheminement.CREATOR);
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
