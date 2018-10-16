package nc.opt.mobile.optmobile.ui.activity.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nc.opt.mobile.optmobile.broadcast.NetworkReceiver;
import nc.opt.mobile.optmobile.database.local.StepOrigine;
import nc.opt.mobile.optmobile.database.local.entity.ColisEntity;
import nc.opt.mobile.optmobile.database.local.entity.ColisWithSteps;
import nc.opt.mobile.optmobile.database.local.entity.StepEntity;
import nc.opt.mobile.optmobile.database.local.repository.ColisRepository;
import nc.opt.mobile.optmobile.database.local.repository.ColisWithStepsRepository;
import nc.opt.mobile.optmobile.database.local.repository.StepRepository;
import nc.opt.mobile.optmobile.job.SyncTask;

/**
 * Created by orlanth23 on 16/10/2018.
 */

public class DetailActivityViewModel extends AndroidViewModel {

    private ColisRepository colisRepository;
    private StepRepository stepRepository;
    private MutableLiveData<ColisWithSteps> colisWithStepsSelected = new MutableLiveData<>();
    private MutableLiveData<List<ColisWithSteps>> colisWithStepsList = new MutableLiveData<>();

    public DetailActivityViewModel(@NonNull Application application) {
        super(application);
        ColisWithStepsRepository colisWithStepsRepository = ColisWithStepsRepository.getInstance(application);
        colisRepository = ColisRepository.getInstance(application);
        stepRepository = StepRepository.getInstance(application);

        colisWithStepsRepository.getActiveFlowableColisWithSteps()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(colisWithSteps -> colisWithStepsList.postValue(colisWithSteps))
                .subscribe();
    }

    public Maybe<ColisEntity> findColisById(String idColis) {
        return colisRepository.findById(idColis);
    }

    public LiveData<List<StepEntity>> getListStepFromOpt(String idColis) {
        return stepRepository.liveListStepsOrderedByIdColisAndOrigine(idColis, StepOrigine.OPT);
    }

    private void launchSyncTask(SyncTask.TypeSyncTask type, @Nullable String idColis) {
        new SyncTask(getApplication(), type, idColis).execute();
    }

    public void deleteAllSteps(String idColis) {
        stepRepository.deleteByIdColis(idColis);
    }

    public void refresh() {
        if (NetworkReceiver.checkConnection(getApplication())) {
            if (colisWithStepsSelected.getValue() != null) {
                launchSyncTask(SyncTask.TypeSyncTask.SOLO, colisWithStepsSelected.getValue().colisEntity.getIdColis());
            } else {
                launchSyncTask(SyncTask.TypeSyncTask.ALL, null);
            }
        }
    }

    public void updateDescription(String idColis, String description) {
        colisRepository.findById(idColis)
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .map(colisEntity -> colisEntity.buildDescription(description))
                .doOnSuccess(colisRepository::update)
                .subscribe();
    }
}
