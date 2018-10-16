package nc.opt.mobile.optmobile.database.local.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;

import eu.davidea.flexibleadapter.utils.Log;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nc.opt.mobile.optmobile.database.local.ColisDatabase;
import nc.opt.mobile.optmobile.database.local.StepOrigine;
import nc.opt.mobile.optmobile.database.local.dao.StepDao;
import nc.opt.mobile.optmobile.database.local.entity.StepEntity;
import nc.opt.mobile.optmobile.database.local.repository.task.EtapeRepositoryTask;

import static nc.opt.mobile.optmobile.database.local.repository.task.TypeTask.DELETE;
import static nc.opt.mobile.optmobile.database.local.repository.task.TypeTask.INSERT;
import static nc.opt.mobile.optmobile.database.local.repository.task.TypeTask.UPDATE;

/**
 * Created by orlanth23 on 11/01/2018.
 */

public class StepRepository {

    private static final String TAG = StepRepository.class.getCanonicalName();
    private static StepRepository INSTANCE;

    private StepDao stepDao;

    private StepRepository(Context context) {
        ColisDatabase db = ColisDatabase.getInstance(context);
        this.stepDao = db.stepDao();
    }

    public static synchronized StepRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new StepRepository(context);
        }
        return INSTANCE;
    }

    public LiveData<List<StepEntity>> liveListStepsOrderedByIdColisAndOrigine(String idColis, StepOrigine origine) {
        return this.stepDao.liveListStepsOrderedByIdColisAndOrigine(idColis, origine.getValue());
    }

    public void insert(StepEntity... stepEntities) {
        new EtapeRepositoryTask(this.stepDao, INSERT).execute(stepEntities);
    }

    public void insert(List<StepEntity> stepEntities) {
        new EtapeRepositoryTask(this.stepDao, INSERT).execute((StepEntity[]) stepEntities.toArray(new StepEntity[0]));
    }

    public void update(StepEntity... stepEntities) {
        new EtapeRepositoryTask(this.stepDao, UPDATE).execute(stepEntities);
    }

    public void delete(StepEntity... stepEntities) {
        new EtapeRepositoryTask(this.stepDao, DELETE).execute(stepEntities);
    }

    private Maybe<Integer> count(StepEntity stepEntity) {
        return this.stepDao.exist(stepEntity.getIdColis(), stepEntity.getOrigine().getValue(), stepEntity.getDate(), stepEntity.getDescription())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public void save(List<StepEntity> stepEntities) {
        for (StepEntity stepEntity : stepEntities) {
            count(stepEntity)
                    .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                    .doOnSuccess(count -> {
                        if (count > 0) {
                            update(stepEntity);
                        } else {
                            insert(stepEntity);
                        }
                    })
                    .doOnError(throwable -> Log.e(TAG, "Erreur lors de la sauvegarde de la liste de step " + stepEntities))
                    .subscribe();
        }
    }

    public void deleteByIdColis(String idColis) {
        this.stepDao.getAllByIdColis(idColis)
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .doOnSuccess(this.stepDao::delete)
                .subscribe();
    }
}
