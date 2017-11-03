package nc.opt.mobile.optmobile.domain.suiviColis;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by 2761oli on 05/10/2017.
 */

@IgnoreExtraProperties
public class ActualiteDto implements Parcelable {

    @Exclude
    private String idActualite;

    @Exclude
    private String idFirebase;

    private String date;
    private String titre;
    private String type;
    private String contenu;
    private boolean dismissable;
    private boolean dismissed;

    public ActualiteDto() {
    }

    public ActualiteDto(String idActualite, String idFirebase, String date, String titre, String type, String contenu, boolean dismissable, boolean dismissed) {
        this.idActualite = idActualite;
        this.idFirebase = idFirebase;
        this.date = date;
        this.titre = titre;
        this.type = type;
        this.contenu = contenu;
        this.dismissable = dismissable;
        this.dismissed = dismissed;
    }

    public String getIdActualite() {
        return idActualite;
    }

    public void setIdActualite(String idActualite) {
        this.idActualite = idActualite;
    }

    public String getIdFirebase() {
        return idFirebase;
    }

    public void setIdFirebase(String idFirebase) {
        this.idFirebase = idFirebase;
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

    public boolean isDismissable() {
        return dismissable;
    }

    public void setDismissable(boolean dismissable) {
        this.dismissable = dismissable;
    }

    public boolean isDismissed() {
        return dismissed;
    }

    public void setDismissed(boolean dismissed) {
        this.dismissed = dismissed;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idActualite);
        dest.writeString(this.idFirebase);
        dest.writeString(this.date);
        dest.writeString(this.titre);
        dest.writeString(this.type);
        dest.writeString(this.contenu);
        dest.writeByte(this.dismissable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.dismissed ? (byte) 1 : (byte) 0);
    }

    private ActualiteDto(Parcel in) {
        this.idActualite = in.readString();
        this.idFirebase = in.readString();
        this.date = in.readString();
        this.titre = in.readString();
        this.type = in.readString();
        this.contenu = in.readString();
        this.dismissable = in.readByte() != 0;
        this.dismissed = in.readByte() != 0;
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
