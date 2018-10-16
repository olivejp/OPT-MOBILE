package nc.opt.mobile.optmobile.database.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import nc.opt.mobile.optmobile.database.local.entity.StepEntity;


/**
 * Created by orlanth23 on 10/01/2018.
 */
@Dao
public interface StepDao extends AbstractDao<StepEntity> {
    @Query("SELECT COUNT(*) FROM  etape WHERE idColis = :idColis AND origine = :origine AND date = :date AND description = :description")
    Maybe<Integer> exist(String idColis, String origine, Long date, String description);

    @Query("SELECT * FROM etape WHERE idColis = :idColis AND origine = :origine ORDER BY date DESC, idEtapeAcheminement DESC")
    LiveData<List<StepEntity>> liveListStepsOrderedByIdColisAndOrigine(String idColis, String origine);

    @Query("SELECT * FROM etape WHERE idColis = :idColis")
    Single<List<StepEntity>> getAllByIdColis(String idColis);
}

