package nc.opt.mobile.optmobile.provider.entity;

import org.chalup.microorm.annotations.Column;

import nc.opt.mobile.optmobile.provider.interfaces.ShedlockInterface;

/**
 * Created by 2761oli on 25/10/2017.
 */

public class ShedlockEntity {
    @Column(ShedlockInterface.ID_SHEDLOCK)
    private int idActualite;

    @Column(ShedlockInterface.LOCKED)
    private String locked;

    @Column(ShedlockInterface.DATE)
    private Long date;

    public ShedlockEntity() {
    }

    public int getIdActualite() {
        return idActualite;
    }

    public void setIdActualite(int idActualite) {
        this.idActualite = idActualite;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
