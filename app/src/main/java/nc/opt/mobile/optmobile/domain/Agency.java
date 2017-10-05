package nc.opt.mobile.optmobile.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.chalup.microorm.annotations.Column;
import org.parceler.ParcelConstructor;

import nc.opt.mobile.optmobile.provider.AgencyInterface;

/**
 * Created by orlanth23 on 08/08/2017.
 */

public class Agency implements Parcelable{
    @Column(AgencyInterface.OBJECTID)
    int OBJECTID;

    @Column(AgencyInterface.TEXTE)
    String TEXTE;

    @Column(AgencyInterface.TYPE)
    String TYPE;

    @Column(AgencyInterface.NOM)
    String NOM;

    @Column(AgencyInterface.ADRESSE)
    String ADRESSE;

    @Column(AgencyInterface.CODE_POSTAL)
    String CODE_POSTAL;

    @Column(AgencyInterface.VILLE)
    String VILLE;

    @Column(AgencyInterface.TEL)
    String TEL;

    @Column(AgencyInterface.FAX)
    String FAX;

    @Column(AgencyInterface.HORAIRE)
    String HORAIRE;

    @Column(AgencyInterface.DAB_INTERNE)
    int DAB_INTERNE;

    @Column(AgencyInterface.DAB_EXTERNE)
    int DAB_EXTERNE;

    @Column(AgencyInterface.CONSEILLER_FINANCIER)
    int CONSEILLER_FINANCIER;

    @Column(AgencyInterface.CONSEILLER_TELECOM)
    int CONSEILLER_TELECOM;

    @Column(AgencyInterface.CONSEILLER_POLYVALENT)
    int CONSEILLER_POLYVALENT;

    @Column(AgencyInterface.CONSEILLER_POSTAL)
    int CONSEILLER_POSTAL;

    @Column(AgencyInterface.GLOBALID)
    String GLOBALID;

    @Column(AgencyInterface.LATITUDE)
    double LATITUDE;

    @Column(AgencyInterface.LONGITUDE)
    double LONGITUDE;

    @Column(AgencyInterface.TYPE_GEOMETRY)
    String TYPE_GEOMETRY;

    @ParcelConstructor
    public Agency(int OBJECTID, String TEXTE, String TYPE, String NOM, String ADRESSE, String CODE_POSTAL, String VILLE, String TEL, String FAX, String HORAIRE, int DAB_INTERNE, int DAB_EXTERNE, int CONSEILLER_FINANCIER, int CONSEILLER_TELECOM, int CONSEILLER_POLYVALENT, int CONSEILLER_POSTAL, String GLOBALID, double LATITUDE, double LONGITUDE, String TYPE_GEOMETRY) {
        this.OBJECTID = OBJECTID;
        this.TEXTE = TEXTE;
        this.TYPE = TYPE;
        this.NOM = NOM;
        this.ADRESSE = ADRESSE;
        this.CODE_POSTAL = CODE_POSTAL;
        this.VILLE = VILLE;
        this.TEL = TEL;
        this.FAX = FAX;
        this.HORAIRE = HORAIRE;
        this.DAB_INTERNE = DAB_INTERNE;
        this.DAB_EXTERNE = DAB_EXTERNE;
        this.CONSEILLER_FINANCIER = CONSEILLER_FINANCIER;
        this.CONSEILLER_TELECOM = CONSEILLER_TELECOM;
        this.CONSEILLER_POLYVALENT = CONSEILLER_POLYVALENT;
        this.CONSEILLER_POSTAL = CONSEILLER_POSTAL;
        this.GLOBALID = GLOBALID;
        this.LATITUDE = LATITUDE;
        this.LONGITUDE = LONGITUDE;
        this.TYPE_GEOMETRY = TYPE_GEOMETRY;
    }

    public int getOBJECTID() {
        return OBJECTID;
    }

    public void setOBJECTID(int OBJECTID) {
        this.OBJECTID = OBJECTID;
    }

    public String getTEXTE() {
        return TEXTE;
    }

    public void setTEXTE(String TEXTE) {
        this.TEXTE = TEXTE;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getNOM() {
        return NOM;
    }

    public void setNOM(String NOM) {
        this.NOM = NOM;
    }

    public String getADRESSE() {
        return ADRESSE;
    }

    public void setADRESSE(String ADRESSE) {
        this.ADRESSE = ADRESSE;
    }

    public String getCODE_POSTAL() {
        return CODE_POSTAL;
    }

    public void setCODE_POSTAL(String CODE_POSTAL) {
        this.CODE_POSTAL = CODE_POSTAL;
    }

    public String getVILLE() {
        return VILLE;
    }

    public void setVILLE(String VILLE) {
        this.VILLE = VILLE;
    }

    public String getTEL() {
        return TEL;
    }

    public void setTEL(String TEL) {
        this.TEL = TEL;
    }

    public String getFAX() {
        return FAX;
    }

    public void setFAX(String FAX) {
        this.FAX = FAX;
    }

    public String getHORAIRE() {
        return HORAIRE;
    }

    public void setHORAIRE(String HORAIRE) {
        this.HORAIRE = HORAIRE;
    }

    public int getDAB_INTERNE() {
        return DAB_INTERNE;
    }

    public void setDAB_INTERNE(int DAB_INTERNE) {
        this.DAB_INTERNE = DAB_INTERNE;
    }

    public int getDAB_EXTERNE() {
        return DAB_EXTERNE;
    }

    public void setDAB_EXTERNE(int DAB_EXTERNE) {
        this.DAB_EXTERNE = DAB_EXTERNE;
    }

    public int getCONSEILLER_FINANCIER() {
        return CONSEILLER_FINANCIER;
    }

    public void setCONSEILLER_FINANCIER(int CONSEILLER_FINANCIER) {
        this.CONSEILLER_FINANCIER = CONSEILLER_FINANCIER;
    }

    public int getCONSEILLER_TELECOM() {
        return CONSEILLER_TELECOM;
    }

    public void setCONSEILLER_TELECOM(int CONSEILLER_TELECOM) {
        this.CONSEILLER_TELECOM = CONSEILLER_TELECOM;
    }

    public int getCONSEILLER_POLYVALENT() {
        return CONSEILLER_POLYVALENT;
    }

    public void setCONSEILLER_POLYVALENT(int CONSEILLER_POLYVALENT) {
        this.CONSEILLER_POLYVALENT = CONSEILLER_POLYVALENT;
    }

    public int getCONSEILLER_POSTAL() {
        return CONSEILLER_POSTAL;
    }

    public void setCONSEILLER_POSTAL(int CONSEILLER_POSTAL) {
        this.CONSEILLER_POSTAL = CONSEILLER_POSTAL;
    }

    public String getGLOBALID() {
        return GLOBALID;
    }

    public void setGLOBALID(String GLOBALID) {
        this.GLOBALID = GLOBALID;
    }

    public double getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(double LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public double getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(double LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getTYPE_GEOMETRY() {
        return TYPE_GEOMETRY;
    }

    public void setTYPE_GEOMETRY(String TYPE_GEOMETRY) {
        this.TYPE_GEOMETRY = TYPE_GEOMETRY;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.OBJECTID);
        dest.writeString(this.TEXTE);
        dest.writeString(this.TYPE);
        dest.writeString(this.NOM);
        dest.writeString(this.ADRESSE);
        dest.writeString(this.CODE_POSTAL);
        dest.writeString(this.VILLE);
        dest.writeString(this.TEL);
        dest.writeString(this.FAX);
        dest.writeString(this.HORAIRE);
        dest.writeInt(this.DAB_INTERNE);
        dest.writeInt(this.DAB_EXTERNE);
        dest.writeInt(this.CONSEILLER_FINANCIER);
        dest.writeInt(this.CONSEILLER_TELECOM);
        dest.writeInt(this.CONSEILLER_POLYVALENT);
        dest.writeInt(this.CONSEILLER_POSTAL);
        dest.writeString(this.GLOBALID);
        dest.writeDouble(this.LATITUDE);
        dest.writeDouble(this.LONGITUDE);
        dest.writeString(this.TYPE_GEOMETRY);
    }

    protected Agency(Parcel in) {
        this.OBJECTID = in.readInt();
        this.TEXTE = in.readString();
        this.TYPE = in.readString();
        this.NOM = in.readString();
        this.ADRESSE = in.readString();
        this.CODE_POSTAL = in.readString();
        this.VILLE = in.readString();
        this.TEL = in.readString();
        this.FAX = in.readString();
        this.HORAIRE = in.readString();
        this.DAB_INTERNE = in.readInt();
        this.DAB_EXTERNE = in.readInt();
        this.CONSEILLER_FINANCIER = in.readInt();
        this.CONSEILLER_TELECOM = in.readInt();
        this.CONSEILLER_POLYVALENT = in.readInt();
        this.CONSEILLER_POSTAL = in.readInt();
        this.GLOBALID = in.readString();
        this.LATITUDE = in.readDouble();
        this.LONGITUDE = in.readDouble();
        this.TYPE_GEOMETRY = in.readString();
    }

    public static final Creator<Agency> CREATOR = new Creator<Agency>() {
        @Override
        public Agency createFromParcel(Parcel source) {
            return new Agency(source);
        }

        @Override
        public Agency[] newArray(int size) {
            return new Agency[size];
        }
    };
}
