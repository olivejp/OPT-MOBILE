package nc.opt.mobile.optmobile.activity.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.List;

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

    public GestionColisActivityViewModel(@NonNull Application application) {
        super(application);
        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.observe(application, this, OptProvider.ListColis.LIST_COLIS, OptProvider.ListEtapeAcheminement.LIST_ETAPE);
    }

    public LiveData<List<ColisEntity>> getColisEntities() {
        if (this.mListColis == null) {
            this.mListColis = new MutableLiveData<>();
            this.mListColis.setValue(ColisService.listFromProvider(getApplication(), true));
        }
        return this.mListColis;
    }

    public void setSelectedColis(ColisEntity colis) {
        if (this.mSelectedColis == null) {
            this.mSelectedColis = new MutableLiveData<>();
        }
        if (colis != null) {
            this.mSelectedColis.postValue(colis);
        }
    }

    public LiveData<ColisEntity> getSelectedColis() {
        if (this.mSelectedColis == null) {
            this.mSelectedColis = new MutableLiveData<>();
        }
        return this.mSelectedColis;
    }

    @Override
    public void onProviderChange(Uri uri) {
        this.mListColis.postValue(ColisService.listFromProvider(getApplication(), true));

        if (mSelectedColis != null && mSelectedColis.getValue() != null) {
            mSelectedColis.postValue(ColisService.get(getApplication(), mSelectedColis.getValue().getIdColis()));
        }
    }
}
