package nc.opt.mobile.optmobile.database.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import io.reactivex.Maybe;
import nc.opt.mobile.optmobile.database.local.entity.ColisEntity;


/**
 * Created by orlanth23 on 10/01/2018.
 */
@Dao
public interface ColisDao extends AbstractDao<ColisEntity>{

    @Transaction
    @Query("SELECT COUNT(*) FROM colis WHERE idColis = :idColis")
    Maybe<Integer> count(String idColis);

    @Transaction
    @Query("SELECT * FROM colis WHERE DELETED <> '1'")
    Maybe<List<ColisEntity>> listMaybeColisActifs();

    @Transaction
    @Query("SELECT * FROM colis WHERE DELETED = '1'")
    Maybe<List<ColisEntity>> listMaybeColisSupprimes();

    @Transaction
    @Query("SELECT * FROM colis WHERE idColis = :idColis")
    Maybe<ColisEntity> findById(String idColis);

    @Transaction
    @Query("SELECT * FROM colis WHERE DELETED <> '1' AND DELIVERED <> '1'")
    Maybe<List<ColisEntity>> listMaybeColisActifsAndNotDelivered();

    @Transaction
    @Query("DELETE FROM colis WHERE idColis = :idColis")
    int deleteByIdColis(String idColis);
}
