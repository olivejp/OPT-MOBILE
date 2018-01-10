package nc.opt.mobile.optmobile.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import nc.opt.mobile.optmobile.database.entity.ShedlockEntity;

/**
 * Created by orlanth23 on 10/01/2018.
 */
@Dao
public interface ShedlockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEtape(ShedlockEntity shedlock);

    @Update
    void updateEtape(ShedlockEntity shedlock);

    @Query("SELECT * FROM SHEDLOCK")
    ShedlockEntity getShedlock();

    @Query("SELECT * FROM SHEDLOCK")
    LiveData<ShedlockEntity> liveGetShedlock();
}
