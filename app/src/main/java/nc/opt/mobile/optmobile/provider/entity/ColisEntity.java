package nc.opt.mobile.optmobile.provider.entity;

import org.chalup.microorm.annotations.Column;

import java.util.List;

import nc.opt.mobile.optmobile.provider.interfaces.ColisInterface;

/**
 * Created by 2761oli on 11/10/2017.
 */

public class ColisEntity {
    @Column(ColisInterface.ID_COLIS)
    private String idColis;

    @Column(ColisInterface.DESCRIPTION)
    private String description;

    @Column(ColisInterface.LAST_UPDATE)
    private Long lastUpdate;

    @Column(ColisInterface.LAST_UPDATE_SUCCESSFUL)
    private Long lastUpdateSuccessful;

    private List<EtapeAcheminementEntity> etapeAcheminementArrayList;

    public ColisEntity() {
    }

    public ColisEntity(String idColis, String description, Long lastUpdate, Long lastUpdateSuccessful) {
        this.idColis = idColis;
        this.description = description;
        this.lastUpdate = lastUpdate;
        this.lastUpdateSuccessful = lastUpdateSuccessful;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColisEntity that = (ColisEntity) o;

        if (idColis != null ? !idColis.equals(that.idColis) : that.idColis != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (lastUpdate != null ? !lastUpdate.equals(that.lastUpdate) : that.lastUpdate != null)
            return false;
        return lastUpdateSuccessful != null ? lastUpdateSuccessful.equals(that.lastUpdateSuccessful) : that.lastUpdateSuccessful == null;

    }

    public String getIdColis() {
        return idColis;
    }

    public void setIdColis(String idColis) {
        this.idColis = idColis;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getLastUpdateSuccessful() {
        return lastUpdateSuccessful;
    }

    public void setLastUpdateSuccessful(Long lastUpdateSuccessful) {
        this.lastUpdateSuccessful = lastUpdateSuccessful;
    }

    public List<EtapeAcheminementEntity> getEtapeAcheminementArrayList() {
        return etapeAcheminementArrayList;
    }

    public void setEtapeAcheminementArrayList(List<EtapeAcheminementEntity> etapeAcheminementArrayList) {
        this.etapeAcheminementArrayList = etapeAcheminementArrayList;
    }

}
