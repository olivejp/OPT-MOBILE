package nc.opt.mobile.opt_mobile.domain;

import org.parceler.Parcel;

/**
 * Created by orlanth23 on 08/08/2017.
 */

@Parcel
public class Agency {
    private int OBJECTID;
    private String TEXTE;
    private String TYPE;
    private String NOM;
    private String ADRESSE;
    private String CODE_POSTAL;
    private String VILLE;
    private String TEL;
    private String FAX;
    private String HORAIRE;
    private short DAB_INTERNE;
    private short DAB_EXTERNE;
    private short CONSEILLER_FINANCIER;
    private short CONSEILLER_TELECOM;
    private short CONSEILLER_POLYVALENT;
    private short CONSEILLER_POSTAL;
    private String GLOBALID;
    private float LATITUDE;
    private float LONGITUDE;

    public Agency(int OBJECTID, String TEXTE, String TYPE, String NOM, String ADRESSE, String CODE_POSTAL, String VILLE, String TEL, String FAX, String HORAIRE, short DAB_INTERNE, short DAB_EXTERNE, short CONSEILLER_FINANCIER, short CONSEILLER_TELECOM, short CONSEILLER_POLYVALENT, short CONSEILLER_POSTAL, String GLOBALID, float LATITUDE, float LONGITUDE) {
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

    public short getDAB_INTERNE() {
        return DAB_INTERNE;
    }

    public void setDAB_INTERNE(short DAB_INTERNE) {
        this.DAB_INTERNE = DAB_INTERNE;
    }

    public short getDAB_EXTERNE() {
        return DAB_EXTERNE;
    }

    public void setDAB_EXTERNE(short DAB_EXTERNE) {
        this.DAB_EXTERNE = DAB_EXTERNE;
    }

    public short getCONSEILLER_FINANCIER() {
        return CONSEILLER_FINANCIER;
    }

    public void setCONSEILLER_FINANCIER(short CONSEILLER_FINANCIER) {
        this.CONSEILLER_FINANCIER = CONSEILLER_FINANCIER;
    }

    public short getCONSEILLER_TELECOM() {
        return CONSEILLER_TELECOM;
    }

    public void setCONSEILLER_TELECOM(short CONSEILLER_TELECOM) {
        this.CONSEILLER_TELECOM = CONSEILLER_TELECOM;
    }

    public short getCONSEILLER_POLYVALENT() {
        return CONSEILLER_POLYVALENT;
    }

    public void setCONSEILLER_POLYVALENT(short CONSEILLER_POLYVALENT) {
        this.CONSEILLER_POLYVALENT = CONSEILLER_POLYVALENT;
    }

    public short getCONSEILLER_POSTAL() {
        return CONSEILLER_POSTAL;
    }

    public void setCONSEILLER_POSTAL(short CONSEILLER_POSTAL) {
        this.CONSEILLER_POSTAL = CONSEILLER_POSTAL;
    }

    public String getGLOBALID() {
        return GLOBALID;
    }

    public void setGLOBALID(String GLOBALID) {
        this.GLOBALID = GLOBALID;
    }

    public float getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(float LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public float getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(float LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }
}
