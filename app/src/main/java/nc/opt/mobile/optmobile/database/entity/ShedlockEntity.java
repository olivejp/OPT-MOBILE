package nc.opt.mobile.optmobile.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by 2761oli on 25/10/2017.
 */

@Entity(tableName = "shedlock")
public class ShedlockEntity {
    @PrimaryKey
    private int idShedlock;
    private String locked;
    private Long date;

    public ShedlockEntity() {
    }

    public int getIdShedlock() {
        return idShedlock;
    }

    public void setIdShedlock(int idShedlock) {
        this.idShedlock = idShedlock;
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
