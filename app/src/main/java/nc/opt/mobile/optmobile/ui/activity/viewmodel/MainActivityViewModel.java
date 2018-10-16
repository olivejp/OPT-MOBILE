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
 * Created by orlanth23 on 11/01/2018.
 */

public class MainActivityViewModel extends AndroidViewModel {

    private ColisWithStepsRepository colisWithStepsRepository;
    private ColisRepository colisRepository;
    private StepRepository stepRepository;
    private MutableLiveData<ColisWithSteps> colisWithStepsSelected = new MutableLiveData<>();
    private MutableLiveData<List<ColisWithSteps>> colisWithStepsList = new MutableLiveData<>();
    private MutableLiveData<Integer> shouldNotify = new MutableLiveData<>();
    private String idColisSelected;
    private boolean twoPane;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        colisWithStepsRepository = ColisWithStepsRepository.getInstance(application);
        colisRepository = ColisRepository.getInstance(application);
        stepRepository = StepRepository.getInstance(application);

        colisWithStepsRepository.getActiveFlowableColisWithSteps()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(colisWithSteps -> colisWithStepsList.postValue(colisWithSteps))
                .subscribe();
    }

    public LiveData<List<ColisWithSteps>> getLiveColisWithSteps() {
        return colisWithStepsRepository.getLiveActiveColisWithSteps();
    }

    /**
     * Return True if we have two panes, false otherwise
     *
     * @return boolean
     */
    public boolean isTwoPane() {
        return twoPane;
    }

    public void setTwoPane(boolean twoPane) {
        this.twoPane = twoPane;
    }

    /**
     * Record the selected colis
     *
     * @param colis
     */
    public void setSelectedColis(ColisWithSteps colis) {
        colisWithStepsSelected.postValue(colis);
        if (colis != null && colis.colisEntity != null) {
            idColisSelected = colis.colisEntity.getIdColis();
        }
    }

    public Maybe<ColisEntity> findColisById(String idColis) {
        return colisRepository.findById(idColis);
    }

    public String getSelectedIdColis() {
        return idColisSelected;
    }

    public LiveData<List<StepEntity>> getListStepFromOpt(String idColis) {
        return stepRepository.liveListStepsOrderedByIdColisAndOrigine(idColis, StepOrigine.OPT);
    }

    private void launchSyncTask(SyncTask.TypeSyncTask type, @Nullable String idColis) {
        new SyncTask(getApplication(), type, idColis).execute();
    }

    /**
     * Tag the colis to deleted = 1 if it had a link to Firebase or AfterShip
     * Delete it from the DB otherwise
     *
     * @param colisEntity
     */
    public void markAsDeleted(ColisEntity colisEntity) {
        if (!ColisRepository.getInstance(getApplication()).markAsDeleted(colisEntity)) {
            launchSyncTask(SyncTask.TypeSyncTask.DELETE, null);
        }
    }

    public void markAsDelivered(ColisEntity colisEntity) {
        colisRepository.markAsDelivered(colisEntity);
    }

    public LiveData<Integer> isListColisActiveEmpty() {
        return colisWithStepsRepository.getLiveCountActiveColisWithSteps();
    }

    public void notifyItemChanged(int position) {
        shouldNotify.postValue(position);
    }

    public LiveData<Integer> isDataSetChanged() {
        return this.shouldNotify;
    }

    public void deleteAllSteps() {
        stepRepository.deleteByIdColis(idColisSelected);
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

    public void updateDescription(String description) {
        colisRepository.findById(idColisSelected)
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .map(colisEntity -> colisEntity.buildDescription(description))
                .doOnSuccess(colisRepository::update)
                .subscribe();
    }
}
