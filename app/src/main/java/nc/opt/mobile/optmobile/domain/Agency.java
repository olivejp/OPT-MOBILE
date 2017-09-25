package nc.opt.mobile.optmobile.domain;

import org.chalup.microorm.annotations.Column;
import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import nc.opt.mobile.optmobile.provider.AgencyInterface;

/**
 * Created by orlanth23 on 08/08/2017.
 */

@Parcel
public class Agency {
    @Column(AgencyInterface.OBJECTID)
    private int OBJECTID;

    @Column(AgencyInterface.TEXTE)
    private String TEXTE;

    @Column(AgencyInterface.TYPE)
    private String TYPE;

    @Column(AgencyInterface.NOM)
    private String NOM;

    @Column(AgencyInterface.ADRESSE)
    private String ADRESSE;

    @Column(AgencyInterface.CODE_POSTAL)
    private String CODE_POSTAL;

    @Column(AgencyInterface.VILLE)
    private String VILLE;

    @Column(AgencyInterface.TEL)
    private String TEL;

    @Column(AgencyInterface.FAX)
    private String FAX;

    @Column(AgencyInterface.HORAIRE)
    private String HORAIRE;

    @Column(AgencyInterface.DAB_INTERNE)
    private int DAB_INTERNE;

    @Column(AgencyInterface.DAB_EXTERNE)
    private int DAB_EXTERNE;

    @Column(AgencyInterface.CONSEILLER_FINANCIER)
    private int CONSEILLER_FINANCIER;

    @Column(AgencyInterface.CONSEILLER_TELECOM)
    private int CONSEILLER_TELECOM;

    @Column(AgencyInterface.CONSEILLER_POLYVALENT)
    private int CONSEILLER_POLYVALENT;

    @Column(AgencyInterface.CONSEILLER_POSTAL)
    private int CONSEILLER_POSTAL;

    @Column(AgencyInterface.GLOBALID)
    private String GLOBALID;

    @Column(AgencyInterface.LATITUDE)
    private double LATITUDE;

    @Column(AgencyInterface.LONGITUDE)
    private double LONGITUDE;

    @Column(AgencyInterface.TYPE_GEOMETRY)
    private String TYPE_GEOMETRY;

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
}
