package nc.opt.mobile.optmobile.activity.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderObserver;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.services.ColisService;

/**
 * Created by 2761oli on 26/12/2017.
 */

public class GestionColisActivityViewModel extends AndroidViewModel implements ProviderObserver.ProviderObserverListener {

    private MutableLiveData<ColisEntity> mSelectedColis;
    private MutableLiveData<List<ColisEntity>> mListColis;
    private MutableLiveData<AtomicBoolean> mVisibility;

    public GestionColisActivityViewModel(@NonNull Application application) {
        super(application);
        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.observe(application, this, OptProvider.ListColis.LIST_COLIS, OptProvider.ListEtapeAcheminement.LIST_ETAPE);
    }

    private void refreshVisibility() {
        this.mVisibility.postValue(new AtomicBoolean(mListColis == null || mListColis.getValue() == null || mListColis.getValue().isEmpty()));
    }

    public LiveData<List<ColisEntity>> getColisEntities() {
        if (this.mListColis == null) {
            this.mListColis = new MutableLiveData<>();
            this.mListColis.setValue(new ArrayList<>());
            ColisService
                    .observableListColisFromProvider(getApplication())
                    .subscribe(colisEntities -> this.mListColis.postValue(colisEntities));
        }
        return this.mListColis;
    }

    public void setSelectedColis(ColisEntity colis) {
        if (this.mSelectedColis == null) {
            this.mSelectedColis = new MutableLiveData<>();
        }
        if (colis != null) {
            this.mSelectedColis.setValue(colis);
        }
    }

    public LiveData<ColisEntity> getSelectedColis() {
        if (this.mSelectedColis == null) {
            this.mSelectedColis = new MutableLiveData<>();
        }
        return this.mSelectedColis;
    }

    public LiveData<AtomicBoolean> getVisibility() {
        if (this.mVisibility == null) {
            this.mVisibility = new MutableLiveData<>();
            this.mVisibility.setValue(new AtomicBoolean(mListColis == null || mListColis.getValue() == null || mListColis.getValue().isEmpty()));
        }
        return this.mVisibility;
    }

    @Override
    public void onProviderChange(Uri uri) {
        ColisService
                .observableListColisFromProvider(getApplication())
                .subscribe(colisEntities -> this.mListColis.postValue(colisEntities));

        if (mSelectedColis != null && mSelectedColis.getValue() != null) {
            ColisService.observableGetColisFromProvider(getApplication(), mSelectedColis.getValue().getIdColis())
                    .subscribe(colisEntity -> mSelectedColis.postValue(colisEntity));
        }

        refreshVisibility();
    }
}
