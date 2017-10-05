package nc.opt.mobile.optmobile.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by 2761oli on 05/10/2017.
 */

public class ParcelSearchResult implements Parcelable {

    String idParcel;
    ArrayList<StepParcelSearch> stepParcelSearchArrayList;

    public ParcelSearchResult() {
    }

    public ParcelSearchResult(String idParcel, ArrayList<StepParcelSearch> stepParcelSearchArrayList) {
        this.idParcel = idParcel;
        this.stepParcelSearchArrayList = stepParcelSearchArrayList;
    }

    public String getIdParcel() {
        return idParcel;
    }

    public void setIdParcel(String idParcel) {
        this.idParcel = idParcel;
    }

    public ArrayList<StepParcelSearch> getStepParcelSearchArrayList() {
        return stepParcelSearchArrayList;
    }

    public void setStepParcelSearchArrayList(ArrayList<StepParcelSearch> stepParcelSearchArrayList) {
        this.stepParcelSearchArrayList = stepParcelSearchArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idParcel);
        dest.writeTypedList(this.stepParcelSearchArrayList);
    }

    protected ParcelSearchResult(Parcel in) {
        this.idParcel = in.readString();
        this.stepParcelSearchArrayList = in.createTypedArrayList(StepParcelSearch.CREATOR);
    }

    public static final Creator<ParcelSearchResult> CREATOR = new Creator<ParcelSearchResult>() {
        @Override
        public ParcelSearchResult createFromParcel(Parcel source) {
            return new ParcelSearchResult(source);
        }

        @Override
        public ParcelSearchResult[] newArray(int size) {
            return new ParcelSearchResult[size];
        }
    };
}
