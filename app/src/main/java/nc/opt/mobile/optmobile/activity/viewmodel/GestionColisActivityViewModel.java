package nc.opt.mobile.optmobile.activity.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderObserver;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;
import nc.opt.mobile.optmobile.provider.services.ColisService;

import static android.os.AsyncTask.Status.FINISHED;

/**
 * Created by 2761oli on 26/12/2017.
 */

public class GestionColisActivityViewModel extends AndroidViewModel implements ProviderObserver.ProviderObserverListener {

    private MutableLiveData<ColisEntity> mSelectedColis;
    private MutableLiveData<List<ColisEntity>> mListColis;
    private MutableLiveData<List<EtapeEntity>> mListEtapes;
    private boolean mTypeOpt;
    private boolean mTypeAfterShip;
    private List<EtapeEntity> listTmp = new ArrayList<>();
    private MyAsyncTask asyncTask;

    @Override
    protected void onCleared() {
        super.onCleared();

        // Kill the AsyncTask to avoid memory Leak
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
    }

    class MyAsyncTask extends AsyncTask<Void, Void, List<EtapeEntity>> {
        @Override
        protected List<EtapeEntity> doInBackground(Void... voids) {
            listTmp.clear();
            if (mSelectedColis.getValue() != null) {
                if (mTypeOpt) {
                    listTmp.addAll(mSelectedColis.getValue().getEtapes(EtapeEntity.EtapeOrigine.OPT));
                }
                if (mTypeAfterShip) {
                    listTmp.addAll(mSelectedColis.getValue().getEtapes(EtapeEntity.EtapeOrigine.AFTER_SHIP));
                }
            }
            return listTmp;
        }

        @Override
        protected void onPostExecute(List<EtapeEntity> list) {
            super.onPostExecute(list);
            mListEtapes.postValue(listTmp);
        }
    }

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

    private void initSelectedEtapes() {
        if (mListEtapes == null) {
            mListEtapes = new MutableLiveData<>();
            mListEtapes.setValue(new ArrayList<>());
        }
        if (mSelectedColis != null && mSelectedColis.getValue() != null) {
            if (asyncTask != null) {
                if (asyncTask.getStatus() != FINISHED) {
                    asyncTask.cancel(true);
                }
            }
            asyncTask = new MyAsyncTask();
            asyncTask.execute();
        }
    }

    public LiveData<List<EtapeEntity>> getSelectedEtapes() {
        initSelectedEtapes();
        return mListEtapes;
    }

    public void releaseProviderObserver() {
        ProviderObserver.getInstance().unregister(this);
    }

    public void changeEtapeType(boolean typeOpt, boolean typeAftership) {
        this.mTypeOpt = typeOpt;
        this.mTypeAfterShip = typeAftership;
        initSelectedEtapes();
    }

    public boolean getTypeOpt() {
        return mTypeOpt;
    }

    public boolean getTypeAfterShip() {
        return mTypeAfterShip;
    }

    @Override
    public void onProviderChange(Uri uri) {
        this.mListColis.postValue(ColisService.listFromProvider(getApplication(), true));

        if (mSelectedColis != null && mSelectedColis.getValue() != null) {
            mSelectedColis.postValue(ColisService.get(getApplication(), mSelectedColis.getValue().getIdColis()));
        }
    }
}
