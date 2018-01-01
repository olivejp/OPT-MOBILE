package nc.opt.mobile.optmobile.network;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import nc.opt.mobile.optmobile.domain.suivi.aftership.ResponseAfterShip;
import nc.opt.mobile.optmobile.domain.suivi.aftership.ResponseDataDetectCourier;
import nc.opt.mobile.optmobile.domain.suivi.aftership.SendTrackingData;
import nc.opt.mobile.optmobile.domain.suivi.aftership.Tracking;
import nc.opt.mobile.optmobile.domain.suivi.aftership.TrackingData;
import nc.opt.mobile.optmobile.domain.suivi.aftership.TrackingDelete;
import nc.opt.mobile.optmobile.utils.CoreSync;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static nc.opt.mobile.optmobile.utils.Constants.URL_AFTERSHIP_BASE_URL;
import static nc.opt.mobile.optmobile.utils.Constants.URL_SUIVI_COLIS;

/**
 * Created by orlanth23 on 12/12/2017.
 */
public class RetrofitClient {

    private static Retrofit retrofitJson = null;
    private static Retrofit retrofitHtml = null;


    private RetrofitClient() {
    }

    private static Retrofit getJsonClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("aftership-api-key", "e1bc990c-5652-4c88-8332-f60188329fe0")
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(request);
                })
                .build();


        if (retrofitJson == null) {
            retrofitJson = new Retrofit.Builder()
                    .baseUrl(URL_AFTERSHIP_BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofitJson;
    }

    /**
     * @return
     */
    private static Retrofit getHtmlClient() {
        if (retrofitHtml == null) {
            retrofitHtml = new Retrofit.Builder()
                    .baseUrl(URL_SUIVI_COLIS)
                    .addConverterFactory(new StringConverter())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofitHtml;
    }

    /**
     * Detect courier for the tracking number passed
     *
     * @param trackingNumber
     * @return Observable<ResponseDataDetectCourier>
     */
    public static Observable<ResponseDataDetectCourier> detectCourier(String trackingNumber) {
        RetrofitCall retrofitCall = RetrofitClient.getJsonClient().create(RetrofitCall.class);

        Tracking<SendTrackingData> trackingDataTracking = CoreSync.createTrackingData(trackingNumber);

        return retrofitCall.detectCourier(trackingDataTracking)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(r -> Observable.just(r.getData()));
    }

    /**
     * Return an Observable of TrackingDelete
     *
     * @return Observable<TrackingDelete>
     */
    public static Observable<TrackingDelete> deleteTrackingBySlugAndTrackingNumber(String slug, String trackingNumber) {
        RetrofitCall retrofitCall = RetrofitClient.getJsonClient().create(RetrofitCall.class);
        return retrofitCall.deleteTracking(slug, trackingNumber)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .flatMap(dataGetResponseAfterShip -> Observable.just(dataGetResponseAfterShip.getData().getTracking()));
    }

    /**
     * Return an Observable of TrackingData
     *
     * @return
     */
    public static Observable<TrackingData> getTracking(String trackingId) {
        RetrofitCall retrofitCall = RetrofitClient.getJsonClient().create(RetrofitCall.class);
        return retrofitCall.getTracking(trackingId)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .flatMap(trackingResponseAfterShip -> Observable.just(trackingResponseAfterShip.getData().getTracking()));
    }

    /**
     * Return an Observable of TrackingData
     *
     * @return
     */
    public static Observable<TrackingData> getTracking(String slug, String trackingNumber) {
        RetrofitCall retrofitCall = RetrofitClient.getJsonClient().create(RetrofitCall.class);
        return retrofitCall.getTracking(slug, trackingNumber)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .flatMap(trackingResponseAfterShip -> Observable.just(trackingResponseAfterShip.getData().getTracking()));
    }

    /**
     * Return an Observable of <TrackingData>
     *
     * @param trackingNumber
     * @return Observable<TrackingData>
     */
    public static Observable<TrackingData> postTracking(String trackingNumber) {
        Function<ResponseAfterShip<Tracking<TrackingData>>, TrackingData> funPostTrackingData = trackingResponseAfterShip -> trackingResponseAfterShip.getData().getTracking();

        Tracking<SendTrackingData> trackingDataTracking = CoreSync.createTrackingData(trackingNumber);

        RetrofitCall retrofitCall = RetrofitClient.getJsonClient().create(RetrofitCall.class);
        Observable<ResponseAfterShip<Tracking<TrackingData>>> observable = retrofitCall.postTracking(trackingDataTracking)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation());

        return observable.map(funPostTrackingData);
    }

    /**
     * Return an Observable of <{@link String}>
     *
     * @param trackingNumber
     * @return Observable<String>
     */
    public static Observable<String> getTrackingOpt(String trackingNumber) {
        RetrofitCall retrofitCall = RetrofitClient.getHtmlClient().create(RetrofitCall.class);
        return retrofitCall.getTrackingOpt(trackingNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }
}
