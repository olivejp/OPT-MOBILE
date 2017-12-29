package nc.opt.mobile.optmobile.fragment.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderObserver;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;
import nc.opt.mobile.optmobile.provider.services.ColisService;

/**
 * Created by 2761oli on 26/12/2017.
 */
public class HistoriqueColisFragmentViewModel extends AndroidViewModel implements ProviderObserver.ProviderObserverListener {

    private MutableLiveData<ColisEntity> mColis;
    private MutableLiveData<List<EtapeEntity>> mEtapes;

    public HistoriqueColisFragmentViewModel(@NonNull Application application) {
        super(application);

        // On va écouter le content provider, dans le cas d'un changement, on va appeler onProviderChange()
        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.observe(application, this, OptProvider.ListColis.LIST_COLIS, OptProvider.ListEtapeAcheminement.LIST_ETAPE);
    }

    private boolean conditionVisible() {
        return (mColis == null || mColis.getValue() == null || mColis.getValue().getEtapeAcheminementArrayList().isEmpty());
    }

    public void setColis(ColisEntity colis) {
        if (this.mColis == null) {
            this.mColis = new MutableLiveData<>();
            this.mEtapes = new MutableLiveData<>();
        }
        if (colis != null) {
            this.mColis.setValue(colis);
            this.mEtapes.setValue(colis.getEtapeAcheminementArrayList());
        }
    }

    public LiveData<ColisEntity> getColis() {
        if (this.mColis == null) {
            this.mColis = new MutableLiveData<>();
        }
        return this.mColis;
    }

    public LiveData<List<EtapeEntity>> getEtapes() {
        if (this.mEtapes == null) {
            this.mEtapes = new MutableLiveData<>();
        }
        return this.mEtapes;
    }

    public int getTextObjectNotFoundVisibility() {
        return conditionVisible() ? View.VISIBLE : View.GONE;
    }

    public int getRecyclerViewVisibility() {
        return conditionVisible() ? View.GONE : View.VISIBLE;
    }

    @Override
    public void onProviderChange() {
        if (mColis != null && mColis.getValue() != null) {
            // Récupération du colis à partir de la BD
            ColisEntity colisEntity = ColisService.get(getApplication(), mColis.getValue().getIdColis());

            // Récupération des étapes à partir de la BD
            mColis.postValue(colisEntity);
            if (colisEntity != null && colisEntity.getEtapeAcheminementArrayList() != null && !colisEntity.getEtapeAcheminementArrayList().isEmpty()) {
                mEtapes.postValue(colisEntity.getEtapeAcheminementArrayList());
            }
        }
    }
}
