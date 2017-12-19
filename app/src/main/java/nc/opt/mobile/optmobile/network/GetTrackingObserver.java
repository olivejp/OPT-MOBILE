package nc.opt.mobile.optmobile.network;

import android.content.Context;
import android.util.Log;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.Checkpoint;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.DataGet;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.ResponseAfterShip;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.ResponseTrackingData;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;
import nc.opt.mobile.optmobile.provider.services.ColisService;
import nc.opt.mobile.optmobile.utils.DateConverter;

/**
 * Created by orlanth23 on 19/12/2017.
 */

public class GetTrackingObserver implements Observer<ResponseAfterShip<DataGet>> {

    private static final String TAG = GetTrackingObserver.class.getName();
    private Context context;
    private String trackingNumber;

    public GetTrackingObserver(Context context, String trackingNumber) {
        this.context = context;
        this.trackingNumber = trackingNumber;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    /**
     * Va créer une étape à partir d'un checkpoint
     *
     * @param idColis
     * @param checkpoint
     * @return EtapeEntity
     */
    private EtapeEntity createEtapeFromCheckpoint(String idColis, Checkpoint checkpoint) {
        EtapeEntity etape = new EtapeEntity();
        etape.setIdColis(idColis);
        if (checkpoint.getCheckpointTime() != null) {
            etape.setDate(DateConverter.convertDateAfterShipToEntity(checkpoint.getCheckpointTime()));
        } else {
            etape.setDate(0L);
        }
        etape.setLocalisation((checkpoint.getLocation() != null) ? checkpoint.getLocation().toString() : "");
        etape.setCommentaire((checkpoint.getTag() != null) ? checkpoint.getTag() : "");
        etape.setDescription((checkpoint.getMessage() != null) ? checkpoint.getMessage() : "");
        etape.setPays((checkpoint.getCountryName() != null) ? checkpoint.getCountryName().toString() : "");
        return etape;
    }

    private ColisEntity createNewColisFromResponseTrackingData(ResponseTrackingData r) {
        if (r.getTrackingNumber().equals(trackingNumber)) {
            // Ok, on suit déjà ce numéro
            ColisEntity colis = new ColisEntity();
            colis.setDeleted(0);
            colis.setIdColis(r.getTrackingNumber());
            List<EtapeEntity> listEtape = new ArrayList<>();

            for (Checkpoint c : r.getCheckpoints()) {
                listEtape.add(createEtapeFromCheckpoint(colis.getIdColis(), c));
            }

            colis.setEtapeAcheminementArrayList(listEtape);
            if (!ColisService.save(context, colis)) {
                Log.e(TAG, "Le colis n'a pas pu être inséré dans la DB locale.");
            }
            return colis;
        } else {
            RetrofitClient.callPostTracking();
        }
    }

    @Override
    public void onNext(ResponseAfterShip<DataGet> responseAfterShip) {
        if (responseAfterShip.getData().getTrackings() != null && !responseAfterShip.getData().getTrackings().isEmpty()) {

            // On va regarder si on suit déjà ce numéro
            for (ResponseTrackingData r : responseAfterShip.getData().getTrackings()) {
                createNewColisFromResponseTrackingData(r);
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, e.getMessage(), e);
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "GET TRACKING COMPLETE");
    }
}
