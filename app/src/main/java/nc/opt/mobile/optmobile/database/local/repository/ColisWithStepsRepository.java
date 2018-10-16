package nc.opt.mobile.optmobile.database.local.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;
import nc.opt.mobile.optmobile.database.local.ColisDatabase;
import nc.opt.mobile.optmobile.database.local.dao.ColisWithStepsDao;
import nc.opt.mobile.optmobile.database.local.entity.ColisWithSteps;

/**
 * Created by orlanth23 on 11/01/2018.
 */

public class ColisWithStepsRepository {

    private static ColisWithStepsRepository INSTANCE;
    private ColisWithStepsDao colisWithStepsDao;

    private ColisWithStepsRepository(Context context) {
        ColisDatabase db = ColisDatabase.getInstance(context);
        this.colisWithStepsDao = db.colisWithStepsDao();
    }

    public synchronized static ColisWithStepsRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ColisWithStepsRepository(context);
        }
        return INSTANCE;
    }

    public Maybe<ColisWithSteps> findActiveColisWithStepsByIdColis(String idColis) {
        return this.colisWithStepsDao.findMaybeActiveColisWithStepsByIdColis(idColis)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<ColisWithSteps>> getActiveFlowableColisWithSteps() {
        return this.colisWithStepsDao.getFlowableActiveColisWithSteps();
    }

    public LiveData<List<ColisWithSteps>> getLiveActiveColisWithSteps() {
        return this.colisWithStepsDao.getLiveActiveColisWithSteps();
    }


    public LiveData<Integer> getLiveCountActiveColisWithSteps() {
        return this.colisWithStepsDao.getLiveCountActiveColisWithSteps();
    }


}
