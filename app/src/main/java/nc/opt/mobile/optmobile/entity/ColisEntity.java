package nc.opt.mobile.optmobile.entity;

/**
 * Created by 2761oli on 11/10/2017.
 */

public class ColisEntity {
    private String id_colis;
    private String description;
    private String last_update;
    private String last_update_successful;

    public ColisEntity() {
    }

    public ColisEntity(String id_colis, String description, String last_update, String last_update_successful) {
        this.id_colis = id_colis;
        this.description = description;
        this.last_update = last_update;
        this.last_update_successful = last_update_successful;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColisEntity that = (ColisEntity) o;

        if (id_colis != null ? !id_colis.equals(that.id_colis) : that.id_colis != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (last_update != null ? !last_update.equals(that.last_update) : that.last_update != null)
            return false;
        return last_update_successful != null ? last_update_successful.equals(that.last_update_successful) : that.last_update_successful == null;

    }

    public String getId_colis() {
        return id_colis;
    }

    public void setId_colis(String id_colis) {
        this.id_colis = id_colis;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getLast_update_successful() {
        return last_update_successful;
    }

    public void setLast_update_successful(String last_update_successful) {
        this.last_update_successful = last_update_successful;
    }
}
