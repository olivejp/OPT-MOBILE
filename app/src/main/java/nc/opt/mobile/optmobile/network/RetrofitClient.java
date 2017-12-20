package nc.opt.mobile.optmobile.network;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import nc.opt.mobile.optmobile.domain.suivi.aftership.DataGet;
import nc.opt.mobile.optmobile.domain.suivi.aftership.ResponseAfterShip;
import nc.opt.mobile.optmobile.domain.suivi.aftership.SendTrackingData;
import nc.opt.mobile.optmobile.domain.suivi.aftership.Tracking;
import nc.opt.mobile.optmobile.domain.suivi.aftership.TrackingData;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by orlanth23 on 12/12/2017.
 */

public class RetrofitClient {

    private static final String BASE_URL = "https://api.aftership.com/";

    private static Retrofit retrofit = null;

    private static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private RetrofitClient() {
    }

    /**
     * Transform a ResponseAfterShip<DataGet> to List<TrackingData>
     */
    private static final Function<ResponseAfterShip<DataGet>, List<TrackingData>> funGetTrackingData = dataGetResponseAfterShip -> dataGetResponseAfterShip.getData().getTrackings();

    /**
     * Return an Observable of Tracking
     *
     * @return
     */
    public static Observable<TrackingData> getTrackings() {
        RetrofitCall retrofitCall = RetrofitClient.getClient().create(RetrofitCall.class);
        Observable<ResponseAfterShip<DataGet>> observable = retrofitCall.getTrackings()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());

        return observable.map(funGetTrackingData).flatMap(trackingData ->);
    }

    @SafeVarargs
    public static void callPostTracking(String trackingNumber, Observer<ResponseAfterShip<Tracking<TrackingData>>>... postTrackingObservers) {
        // Création d'un tracking
        Tracking<SendTrackingData> tracking = new Tracking<>();
        SendTrackingData trackingDetect = new SendTrackingData();
        trackingDetect.setTrackingNumber(trackingNumber);
        tracking.setTracking(trackingDetect);

        RetrofitCall retrofitCall = RetrofitClient.getClient().create(RetrofitCall.class);
        Observable<ResponseAfterShip<Tracking<TrackingData>>> observable = retrofitCall.postTracking(tracking)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());

        for (Observer<ResponseAfterShip<Tracking<TrackingData>>> observer : postTrackingObservers) {
            observable.subscribe(observer);
        }
    }
}
