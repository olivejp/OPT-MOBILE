package nc.opt.mobile.optmobile.job.opt;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 2761oli on 05/10/2017.
 */

public class EtapeDto implements Parcelable {

    private String date;
    private String pays;
    private String localisation;
    private String description;
    private String commentaire;
    private String status;

    public EtapeDto() {
    }

    public EtapeDto(String date, String pays, String localisation, String description, String commentaire, String status) {
        this.date = date;
        this.pays = pays;
        this.localisation = localisation;
        this.description = description;
        this.commentaire = commentaire;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
        dest.writeString(this.date);
        dest.writeString(this.pays);
        dest.writeString(this.localisation);
        dest.writeString(this.description);
        dest.writeString(this.commentaire);
        dest.writeString(this.status);
    }

    protected EtapeDto(Parcel in) {
        this.date = in.readString();
        this.pays = in.readString();
        this.localisation = in.readString();
        this.description = in.readString();
        this.commentaire = in.readString();
        this.status = in.readString();
    }

    public static final Creator<EtapeDto> CREATOR = new Creator<EtapeDto>() {
        @Override
        public EtapeDto createFromParcel(Parcel source) {
            return new EtapeDto(source);
        }

        @Override
        public EtapeDto[] newArray(int size) {
            return new EtapeDto[size];
        }
    };
}
