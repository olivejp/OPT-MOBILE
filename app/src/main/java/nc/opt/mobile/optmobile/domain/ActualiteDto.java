package nc.opt.mobile.optmobile.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 2761oli on 05/10/2017.
 */

public class ActualiteDto implements Parcelable {

    private String idActualite;
    private String date;
    private String titre;
    private String type;
    private String contenu;

    public ActualiteDto() {
    }

    public ActualiteDto(String idActualite, String date, String titre, String type, String contenu) {
        this.idActualite = idActualite;
        this.date = date;
        this.titre = titre;
        this.type = type;
        this.contenu = contenu;
    }

    public String getIdActualite() {
        return idActualite;
    }

    public void setIdActualite(String idActualite) {
        this.idActualite = idActualite;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idActualite);
        dest.writeString(this.date);
        dest.writeString(this.titre);
        dest.writeString(this.type);
        dest.writeString(this.contenu);
    }

    protected ActualiteDto(Parcel in) {
        this.idActualite = in.readString();
        this.date = in.readString();
        this.titre = in.readString();
        this.type = in.readString();
        this.contenu = in.readString();
    }

    public static final Creator<ActualiteDto> CREATOR = new Creator<ActualiteDto>() {
        @Override
        public ActualiteDto createFromParcel(Parcel source) {
            return new ActualiteDto(source);
        }

        @Override
        public ActualiteDto[] newArray(int size) {
            return new ActualiteDto[size];
        }
    };
}
