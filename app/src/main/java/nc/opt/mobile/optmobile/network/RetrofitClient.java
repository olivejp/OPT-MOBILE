package nc.opt.mobile.optmobile.network;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import nc.opt.mobile.optmobile.domain.suivi.aftership.ResponseAfterShip;
import nc.opt.mobile.optmobile.domain.suivi.aftership.SendTrackingData;
import nc.opt.mobile.optmobile.domain.suivi.aftership.Tracking;
import nc.opt.mobile.optmobile.domain.suivi.aftership.TrackingData;
import nc.opt.mobile.optmobile.domain.suivi.aftership.TrackingDelete;
import nc.opt.mobile.optmobile.utils.AfterShipUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by orlanth23 on 12/12/2017.
 */
public class RetrofitClient {

    private static final String BASE_URL = "https://api.aftership.com/";

    private static Retrofit retrofit = null;

    private RetrofitClient() {
    }

    private static Retrofit getClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("aftership-api-key", "e1bc990c-5652-4c88-8332-f60188329fe0")
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(request);
                })
                .build();


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }

    /**
     * Return an Observable of TrackingDelete
     *
     * @return
     */
    public static Observable<TrackingDelete> deleteTracking(String trackingId) {
        RetrofitCall retrofitCall = RetrofitClient.getClient().create(RetrofitCall.class);
        return retrofitCall.deleteTracking(trackingId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(dataGetResponseAfterShip -> Observable.just(dataGetResponseAfterShip.getData().getTracking()));
    }

    /**
     * Return an Observable of TrackingData
     *
     * @return
     */
    public static Observable<TrackingData> getTracking(String trackingId) {
        RetrofitCall retrofitCall = RetrofitClient.getClient().create(RetrofitCall.class);
        return retrofitCall.getTracking(trackingId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(dataGetResponseAfterShip -> Observable.just(dataGetResponseAfterShip.getData().getTracking()));
    }

    /**
     * Return an Observable TrackingData
     *
     * @return
     */
    public static Observable<TrackingData> getTrackings() {
        RetrofitCall retrofitCall = RetrofitClient.getClient().create(RetrofitCall.class);
        return retrofitCall.getTrackings()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(dataGetResponseAfterShip -> Observable.fromIterable(dataGetResponseAfterShip.getData().getTrackings()));
    }

    /**
     * Return an Observable of <TrackingData>
     *
     * @param trackingNumber
     * @return
     */
    public static Observable<TrackingData> postTracking(String trackingNumber) {
        Function<ResponseAfterShip<Tracking<TrackingData>>, TrackingData> funPostTrackingData = trackingResponseAfterShip -> trackingResponseAfterShip.getData().getTracking();

        Tracking<SendTrackingData> trackingDataTracking = AfterShipUtils.createTrackingData(trackingNumber);

        RetrofitCall retrofitCall = RetrofitClient.getClient().create(RetrofitCall.class);
        Observable<ResponseAfterShip<Tracking<TrackingData>>> observable = retrofitCall.postTracking(trackingDataTracking)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());

        return observable.map(funPostTrackingData);
    }
}
