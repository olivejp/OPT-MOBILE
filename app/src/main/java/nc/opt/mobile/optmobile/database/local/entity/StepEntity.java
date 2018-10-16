package nc.opt.mobile.optmobile.database.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import nc.opt.mobile.optmobile.database.local.StepOrigine;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by 2761oli on 11/10/2017.
 */
@Entity(tableName = "etape", indices = {
        @Index("idColis"),
        @Index("origine")
},
        foreignKeys = @ForeignKey(entity = ColisEntity.class, parentColumns = "idColis", childColumns = "idColis", onDelete = CASCADE))
public class StepEntity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Integer idEtapeAcheminement;
    private String idColis;
    private Long date;
    private String pays;
    private String localisation;
    private String description;
    private String commentaire;
    private String status;

    @TypeConverters(OrigineConverter.class)
    public StepOrigine origine;

    public StepEntity() {
        // Do Nothing
    }

    @Ignore
    public StepEntity(@NonNull Integer idEtapeAcheminement, String idColis, Long date, String pays, String localisation, String description, String commentaire, String status, StepOrigine origine) {
        this.idEtapeAcheminement = idEtapeAcheminement;
        this.idColis = idColis;
        this.date = date;
        this.pays = pays;
        this.localisation = localisation;
        this.description = description;
        this.commentaire = commentaire;
        this.status = status;
        this.origine = origine;
    }

    @NonNull
    public Integer getIdEtapeAcheminement() {
        return idEtapeAcheminement;
    }

    public void setIdEtapeAcheminement(@NonNull Integer idEtapeAcheminement) {
        this.idEtapeAcheminement = idEtapeAcheminement;
    }

    public String getIdColis() {
        return idColis;
    }

    public void setIdColis(String idColis) {
        this.idColis = idColis;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
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

    public StepOrigine getOrigine() {
        return origine;
    }

    public void setOrigine(StepOrigine origine) {
        this.origine = origine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StepEntity that = (StepEntity) o;

        if (!idEtapeAcheminement.equals(that.idEtapeAcheminement)) return false;
        if (idColis != null ? !idColis.equals(that.idColis) : that.idColis != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (pays != null ? !pays.equals(that.pays) : that.pays != null) return false;
        if (localisation != null ? !localisation.equals(that.localisation) : that.localisation != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (commentaire != null ? !commentaire.equals(that.commentaire) : that.commentaire != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        return origine == that.origine;
    }

    @Override
    public int hashCode() {
        int result = (idColis != null ? idColis.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (pays != null ? pays.hashCode() : 0);
        result = 31 * result + (localisation != null ? localisation.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (commentaire != null ? commentaire.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (origine != null ? origine.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StepEntity{" +
                "idEtapeAcheminement=" + idEtapeAcheminement +
                ", idColis='" + idColis + '\'' +
                ", date=" + date +
                ", pays='" + pays + '\'' +
                ", localisation='" + localisation + '\'' +
                ", description='" + description + '\'' +
                ", commentaire='" + commentaire + '\'' +
                ", status='" + status + '\'' +
                ", origine=" + origine +
                '}';
    }
}
