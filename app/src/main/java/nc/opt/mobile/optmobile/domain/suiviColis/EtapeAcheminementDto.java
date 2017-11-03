package nc.opt.mobile.optmobile.domain.suiviColis;

import android.os.Parcelable;

/**
 * Created by 2761oli on 05/10/2017.
 */

public class EtapeAcheminementDto implements Parcelable {

    private String date;
    private String pays;
    private String localisation;
    private String description;
    private String commentaire;

    public EtapeAcheminementDto(String date, String pays, String localisation, String description, String commentaire) {
        this.date = date;
        this.pays = pays;
        this.localisation = localisation;
        this.description = description;
        this.commentaire = commentaire;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.pays);
        dest.writeString(this.localisation);
        dest.writeString(this.description);
        dest.writeString(this.commentaire);
    }

    private EtapeAcheminementDto(android.os.Parcel in) {
        this.date = in.readString();
        this.pays = in.readString();
        this.localisation = in.readString();
        this.description = in.readString();
        this.commentaire = in.readString();
    }

    public static final Creator<EtapeAcheminementDto> CREATOR = new Creator<EtapeAcheminementDto>() {
        @Override
        public EtapeAcheminementDto createFromParcel(android.os.Parcel source) {
            return new EtapeAcheminementDto(source);
        }

        @Override
        public EtapeAcheminementDto[] newArray(int size) {
            return new EtapeAcheminementDto[size];
        }
    };
}
