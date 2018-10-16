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
import nc.opt.mobile.optmobile.database.local.entity.ColisEntity;
import nc.opt.mobile.optmobile.database.local.entity.ColisWithSteps;
import nc.opt.mobile.optmobile.database.local.repository.ColisRepository;
import nc.opt.mobile.optmobile.database.local.repository.ColisWithStepsRepository;
import nc.opt.mobile.optmobile.job.SyncTask;

/**
 * Created by orlanth23 on 11/01/2018.
 */

public class MainActivityViewModel extends AndroidViewModel {

    private ColisWithStepsRepository colisWithStepsRepository;
    private ColisRepository colisRepository;
    private MutableLiveData<ColisWithSteps> colisWithStepsSelected = new MutableLiveData<>();
    private MutableLiveData<List<ColisWithSteps>> colisWithStepsList = new MutableLiveData<>();
    private MutableLiveData<Integer> shouldNotify = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        colisWithStepsRepository = ColisWithStepsRepository.getInstance(application);
        colisRepository = ColisRepository.getInstance(application);

        colisWithStepsRepository.getActiveFlowableColisWithSteps()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(colisWithSteps -> colisWithStepsList.postValue(colisWithSteps))
                .subscribe();
    }

    public LiveData<List<ColisWithSteps>> getLiveColisWithSteps() {
        return colisWithStepsRepository.getLiveActiveColisWithSteps();
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
    public Maybe<Integer> delete(ColisEntity colisEntity) {
        return ColisRepository.getInstance(getApplication()).delete(colisEntity.getIdColis());
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

    public void refresh() {
        if (NetworkReceiver.checkConnection(getApplication())) {
            if (colisWithStepsSelected.getValue() != null) {
                launchSyncTask(SyncTask.TypeSyncTask.SOLO, colisWithStepsSelected.getValue().colisEntity.getIdColis());
            } else {
                launchSyncTask(SyncTask.TypeSyncTask.ALL, null);
            }
        }
    }
}
