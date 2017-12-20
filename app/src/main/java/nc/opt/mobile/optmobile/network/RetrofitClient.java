package nc.opt.mobile.optmobile.network;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.DataGet;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.ResponseAfterShip;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.ResponseTrackingData;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.SendTrackingData;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.Tracking;
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

    @SafeVarargs
    public static void callGetTrackings(@NotNull Observer<ResponseAfterShip<DataGet>>... getTrackingsObservers) {
        RetrofitCall retrofitCall = RetrofitClient.getClient().create(RetrofitCall.class);
        Observable<ResponseAfterShip<DataGet>> observable = retrofitCall.getTrackings()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());

        for (Observer<ResponseAfterShip<DataGet>> observer : getTrackingsObservers) {
            observable.subscribe(observer);
        }
    }

    @SafeVarargs
    public static void callPostTracking(String trackingNumber, Observer<ResponseAfterShip<Tracking<ResponseTrackingData>>>... postTrackingObservers) {
        // Création d'un tracking
        Tracking<SendTrackingData> tracking = new Tracking<>();
        SendTrackingData trackingDetect = new SendTrackingData();
        trackingDetect.setTrackingNumber(trackingNumber);
        tracking.setTracking(trackingDetect);

        RetrofitCall retrofitCall = RetrofitClient.getClient().create(RetrofitCall.class);
        Observable<ResponseAfterShip<Tracking<ResponseTrackingData>>> observable = retrofitCall.postTracking(tracking)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());

        for (Observer<ResponseAfterShip<Tracking<ResponseTrackingData>>> observer : postTrackingObservers) {
            observable.subscribe(observer);
        }
    }
}
