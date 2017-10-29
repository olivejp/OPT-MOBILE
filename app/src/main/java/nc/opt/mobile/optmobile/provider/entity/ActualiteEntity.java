package nc.opt.mobile.optmobile.provider.entity;

import org.chalup.microorm.annotations.Column;

import nc.opt.mobile.optmobile.provider.interfaces.ActualiteInterface;

/**
 * Created by 2761oli on 25/10/2017.
 */

public class ActualiteEntity {
    @Column(ActualiteInterface.ID_ACTUALITE)
    private int idActualite;

    @Column(ActualiteInterface.ID_FIREBASE)
    private String idFirebase;

    @Column(ActualiteInterface.DATE)
    private Long date;

    @Column(ActualiteInterface.TYPE)
    private String type;

    @Column(ActualiteInterface.TITRE)
    private String titre;

    @Column(ActualiteInterface.CONTENU)
    private String contenu;

    @Column(ActualiteInterface.DISMISSABLE)
    private String dismissable;

    @Column(ActualiteInterface.DISMISSED)
    private String dismissed;

    public ActualiteEntity() {
    }

    public ActualiteEntity(int idActualite, Long date, String type, String titre, String contenu) {
        this.idActualite = idActualite;
        this.date = date;
        this.type = type;
        this.titre = titre;
        this.contenu = contenu;
    }

    public int getIdActualite() {
        return idActualite;
    }

    public void setIdActualite(int idActualite) {
        this.idActualite = idActualite;
    }

    public String getIdFirebase() {
        return idFirebase;
    }

    public void setIdFirebase(String idFirebase) {
        this.idFirebase = idFirebase;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getDismissable() {
        return dismissable;
    }

    public void setDismissable(String dismissable) {
        this.dismissable = dismissable;
    }

    public String getDismissed() {
        return dismissed;
    }

    public void setDismissed(String dismissed) {
        this.dismissed = dismissed;
    }
}
