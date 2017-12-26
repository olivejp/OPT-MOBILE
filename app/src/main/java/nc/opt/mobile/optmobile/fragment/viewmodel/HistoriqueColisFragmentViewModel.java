package nc.opt.mobile.optmobile.fragment.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import nc.opt.mobile.optmobile.domain.suivi.EtapeConsolidated;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderObserver;
import nc.opt.mobile.optmobile.provider.services.EtapeAcheminementService;

/**
 * Created by 2761oli on 26/12/2017.
 */

public class HistoriqueColisFragmentViewModel extends AndroidViewModel implements ProviderObserver.ProviderObserverListener {

    private String mIdColis;
    private MutableLiveData<List<EtapeConsolidated>> mEtapesConsolidated;

    public HistoriqueColisFragmentViewModel(@NonNull Application application) {
        super(application);

        // On va écouter le content provider, dans le cas d'un changement, on va appeler onProviderChange()
        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.observe(application, this, OptProvider.ListColis.LIST_COLIS, OptProvider.ListEtapeAcheminement.LIST_ETAPE);
    }

    private void retrieveEtapeConsolidated(String idColis) {
        EtapeAcheminementService.getConsolidatedEtapeList(getApplication(), idColis)
                .subscribe(etapesConsolidated -> this.mEtapesConsolidated.postValue(etapesConsolidated));
    }

    public void clearList() {
        if (this.mEtapesConsolidated != null && this.mEtapesConsolidated.getValue() != null && !this.mEtapesConsolidated.getValue().isEmpty()) {
            this.mEtapesConsolidated.getValue().clear();
        }
    }

    private boolean conditionVisible() {
        return (mEtapesConsolidated == null || mEtapesConsolidated.getValue() == null || mEtapesConsolidated.getValue().isEmpty());
    }

    public void setIdColis(String idColis) {
        this.mIdColis = idColis;
    }

    public String getIdColis() {
        return this.mIdColis;
    }

    public LiveData<List<EtapeConsolidated>> getEtapesConsolidated(String idColis) {
        if (mEtapesConsolidated == null) {
            mEtapesConsolidated = new MutableLiveData<>();
            retrieveEtapeConsolidated(idColis);
        }
        return mEtapesConsolidated;
    }

    public int getTextObjectNotFoundVisibility() {
        return conditionVisible() ? View.VISIBLE : View.GONE;
    }

    public int getRecyclerViewVisibility() {
        return conditionVisible() ? View.GONE : View.VISIBLE;
    }

    @Override
    public void onProviderChange() {
        retrieveEtapeConsolidated(this.mIdColis);
    }
}
