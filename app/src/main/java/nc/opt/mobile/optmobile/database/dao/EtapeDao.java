package nc.opt.mobile.optmobile.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import nc.opt.mobile.optmobile.database.entity.EtapeEntity;

/**
 * Created by orlanth23 on 10/01/2018.
 */
@Dao
public interface EtapeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEtape(EtapeEntity... colisEntities);

    @Update
    void updateEtape(EtapeEntity... colisEntities);

    @Delete
    void deleteEtape(EtapeEntity... colisEntities);

    @Query("SELECT * FROM ETAPE WHERE idColis = :idColis")
    List<EtapeEntity> listEtapeByIdColis(String idColis);

    @Query("SELECT * FROM ETAPE WHERE idColis = :idColis")
    LiveData<List<EtapeEntity>> liveListEtapeByIdColis(String idColis);

    @Query("SELECT * FROM ETAPE WHERE idColis = :idColis AND origine = :origine")
    List<EtapeEntity> listEtapeByIdColisAndOrigine(String idColis, EtapeEntity.EtapeOrigine origine);

    @Query("SELECT * FROM ETAPE WHERE idColis = :idColis AND origine = :origine")
    LiveData<List<EtapeEntity>> liveListEtapeByIdColisAndOrigine(String idColis, EtapeEntity.EtapeOrigine origine);
}
