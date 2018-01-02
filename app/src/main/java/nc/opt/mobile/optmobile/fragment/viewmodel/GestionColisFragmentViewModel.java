package nc.opt.mobile.optmobile.fragment.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderObserver;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.services.ColisService;

/**
 * Created by 2761oli on 26/12/2017.
 */

public class GestionColisFragmentViewModel extends AndroidViewModel implements ProviderObserver.ProviderObserverListener {

    private MutableLiveData<List<ColisEntity>> mListColis;

    public GestionColisFragmentViewModel(@NonNull Application application) {
        super(application);

        // On va écouter le content provider, dans le cas d'un changement, on va appeler onProviderChange()
        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.observe(application, this, OptProvider.ListColis.LIST_COLIS, OptProvider.ListEtapeAcheminement.LIST_ETAPE);
    }

    private void retrieveColisEntities() {
        ColisService.observableListColisFromProvider(getApplication())
                .subscribe(colisEntities -> this.mListColis.postValue(colisEntities));
    }

    public LiveData<List<ColisEntity>> getColisEntities() {
        if (this.mListColis == null) {
            this.mListColis = new MutableLiveData<>();
            this.mListColis.setValue(new ArrayList<>());
            retrieveColisEntities();
        }
        return this.mListColis;
    }

    private boolean conditionVisible() {
        return (mListColis == null || mListColis.getValue() == null || mListColis.getValue().isEmpty());
    }

    public int getTextObjectNotFoundVisibility() {
        return conditionVisible() ? View.VISIBLE : View.GONE;
    }

    public int getRecyclerViewVisibility() {
        return conditionVisible() ? View.GONE : View.VISIBLE;
    }

    @Override
    public void onProviderChange() {
        retrieveColisEntities();
    }
}
