package nc.opt.mobile.optmobile.network.aftership;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import nc.opt.mobile.optmobile.network.StringConverter;
import nc.opt.mobile.optmobile.network.opt.RetrofitOptCall;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import static nc.opt.mobile.optmobile.Constants.URL_SUIVI_COLIS;

/**
 * Created by orlanth23 on 12/12/2017.
 */
public class RetrofitClient {

    private static Retrofit retrofitHtml = null;


    private RetrofitClient() {
    }

    /**
     * Créer un retrofit mais sans headers spécifiés.
     * Les headers peuvent être attachées mais via un appel.
     *
     * @return {@link Retrofit}
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
     * Return an Observable of <{@link String}>
     *
     * @param trackingNumber
     * @return Observable<String>
     */
    public static Observable<String> getTrackingOpt(String trackingNumber) {
        RetrofitOptCall retrofitOptCall = RetrofitClient.getHtmlClient().create(RetrofitOptCall.class);
        return retrofitOptCall.getTrackingOpt(trackingNumber)
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
    }
}
