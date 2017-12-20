package nc.opt.mobile.optmobile.network;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import nc.opt.mobile.optmobile.domain.suivi.aftership.ResponseAfterShip;
import nc.opt.mobile.optmobile.domain.suivi.aftership.Tracking;
import nc.opt.mobile.optmobile.domain.suivi.aftership.TrackingData;

/**
 * Created by orlanth23 on 19/12/2017.
 */

public class GetPostTrackingObserver implements Observer<ResponseAfterShip<Tracking<TrackingData>>> {

    private static final String TAG = GetPostTrackingObserver.class.getName();

    public GetPostTrackingObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        // Do nothing
    }

    @Override
    public void onNext(ResponseAfterShip<Tracking<TrackingData>> responseAfterShip) {
        if (responseAfterShip.getMeta().getCode() == 200) {

        }
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, e.getMessage(), e);
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "POST TRACKING COMPLETE");
    }
}
