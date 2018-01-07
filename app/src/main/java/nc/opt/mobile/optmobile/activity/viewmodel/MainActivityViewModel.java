package nc.opt.mobile.optmobile.activity.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.NonNull;

import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderObserver;
import nc.opt.mobile.optmobile.provider.services.ColisService;
import nc.opt.mobile.optmobile.service.FirebaseService;

/**
 * Created by orlanth23 on 07/01/2018.
 */

public class MainActivityViewModel extends AndroidViewModel implements ProviderObserver.ProviderObserverListener {

    private MutableLiveData<Integer> countColis;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.observe(application, this, OptProvider.ListColis.LIST_COLIS);
    }

    public LiveData<Integer> getColisCount() {
        if (this.countColis == null) {
            this.countColis = new MutableLiveData<>();
            this.countColis.setValue(ColisService.count(getApplication(), true));
        }
        return this.countColis;
    }

    public void sendFirebaseSync(){
        FirebaseService.catchDbFromFirebase(getApplication());
    }

    @Override
    public void onProviderChange(Uri uri) {
        this.countColis.postValue(ColisService.count(getApplication(), true));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        ProviderObserver.getInstance().unregister(this);
    }
}
