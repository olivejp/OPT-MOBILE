package nc.opt.mobile.optmobile.network;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.DataGet;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.ResponseAfterShip;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.ResponseTrackingData;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.Tracking;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by orlanth23 on 12/12/2017.
 */

public class RetrofitClient {

    private static Retrofit retrofit = null;

    private static final String baseUrl = "https://api.aftership.com/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void callGetTrackings(Observer<ResponseAfterShip<DataGet>> getTrackingsObserver){
        RetrofitCall retrofitCall = RetrofitClient.getClient().create(RetrofitCall.class);
        retrofitCall.getTrackings()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getTrackingsObserver);
    }

    public static void callPostTracking(Tracking tracking, Observer<ResponseAfterShip<Tracking<ResponseTrackingData>>> postTrackingObserver){
        RetrofitCall retrofitCall = RetrofitClient.getClient().create(RetrofitCall.class);
        retrofitCall.postTracking(tracking)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(postTrackingObserver);
    }
}
