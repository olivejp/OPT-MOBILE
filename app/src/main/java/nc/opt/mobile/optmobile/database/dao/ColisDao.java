package nc.opt.mobile.optmobile.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import nc.opt.mobile.optmobile.database.entity.ColisEntity;

/**
 * Created by orlanth23 on 10/01/2018.
 */
@Dao
public interface ColisDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertColis(ColisEntity... colisEntities);

    @Update
    void updateColis(ColisEntity... colisEntities);

    @Delete
    void deleteColis(ColisEntity... colisEntities);

    @Query("SELECT * FROM COLIS")
    List<ColisEntity> listAllColis();

    @Query("SELECT * FROM COLIS WHERE DELETED <> '1'")
    List<ColisEntity> listColisActifs();

    @Query("SELECT * FROM COLIS WHERE DELETED = '1'")
    List<ColisEntity> listColisSupprimes();
}
