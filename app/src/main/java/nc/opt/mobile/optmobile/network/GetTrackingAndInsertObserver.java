package nc.opt.mobile.optmobile.network;

import android.content.Context;
import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.DataGet;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.ResponseAfterShip;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.ResponseTrackingData;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.Tracking;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.services.ColisService;
import nc.opt.mobile.optmobile.utils.AfterShipUtils;

/**
 * Created by orlanth23 on 19/12/2017.
 */

public class GetTrackingAndInsertObserver implements Observer<ResponseAfterShip<DataGet>> {

    private static final String TAG = GetTrackingAndInsertObserver.class.getName();

    private Context context;
    private String trackingNumber;
    private Observer<ResponseAfterShip<Tracking<ResponseTrackingData>>>[] observersPostTracking;

    public GetTrackingAndInsertObserver(Context context, String trackingNumber, Observer<ResponseAfterShip<Tracking<ResponseTrackingData>>>... observersPostTracking) {
        this.context = context;
        this.trackingNumber = trackingNumber;
        this.observersPostTracking = observersPostTracking;
    }

    @Override
    public void onSubscribe(Disposable d) {
        // Do nothing
    }

    @Override
    public void onNext(ResponseAfterShip<DataGet> responseAfterShip) {
        if (responseAfterShip.getData().getTrackings() != null && !responseAfterShip.getData().getTrackings().isEmpty()) {

            // On va regarder si on suit déjà ce numéro
            for (ResponseTrackingData r : responseAfterShip.getData().getTrackings()) {
                if (r.getTrackingNumber().equals(trackingNumber)) {
                    ColisEntity colis = AfterShipUtils.createColisFromResponseTrackingData(r);
                    ColisService.save(context, colis);
                } else {
                    RetrofitClient.callPostTracking(trackingNumber, observersPostTracking);
                }
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
