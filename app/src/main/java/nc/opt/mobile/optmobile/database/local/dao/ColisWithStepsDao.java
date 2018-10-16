package nc.opt.mobile.optmobile.database.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import nc.opt.mobile.optmobile.database.local.entity.ColisWithSteps;

/**
 * Created by orlanth23 on 12/01/2018.
 */
@Dao
public interface ColisWithStepsDao {
    @Transaction
    @Query("SELECT * FROM colis WHERE idColis = :idColis AND deleted <> 1")
    Maybe<ColisWithSteps> findMaybeActiveColisWithStepsByIdColis(String idColis);

    @Transaction
    @Query("SELECT * FROM colis WHERE deleted <> 1")
    Flowable<List<ColisWithSteps>> getFlowableActiveColisWithSteps();

    @Transaction
    @Query("SELECT * FROM colis WHERE deleted <> 1")
    LiveData<List<ColisWithSteps>> getLiveActiveColisWithSteps();

    @Transaction
    @Query("SELECT COUNT(*) FROM colis WHERE deleted <> 1")
    LiveData<Integer> getLiveCountActiveColisWithSteps();

}
