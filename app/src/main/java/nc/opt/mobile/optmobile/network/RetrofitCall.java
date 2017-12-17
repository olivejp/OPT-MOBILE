package nc.opt.mobile.optmobile.network;

import io.reactivex.Observable;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.DataGet;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.ResponseTrackingData;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.Tracking;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.ResponseAfterShip;
import nc.opt.mobile.optmobile.domain.suiviColis.after_ship.ResponseDataDetectCourier;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by orlanth23 on 03/10/2017.
 */

public interface RetrofitCall {

    @Headers({"aftership-api-key: e1bc990c-5652-4c88-8332-f60188329fe0", "Content-Type: application/json"})
    @POST("/v4/couriers/detect")
    Observable<ResponseAfterShip<ResponseDataDetectCourier>> detectCourier(@Body Tracking tracking);

    @Headers({"aftership-api-key: e1bc990c-5652-4c88-8332-f60188329fe0", "Content-Type: application/json"})
    @POST("/v4/trackings")
    Observable<ResponseAfterShip<Tracking<ResponseTrackingData>>> postTracking(@Body Tracking tracking);

    @Headers({"aftership-api-key: e1bc990c-5652-4c88-8332-f60188329fe0", "Content-Type: application/json"})
    @GET("/v4/trackings/{id_tracking}")
    Observable<ResponseAfterShip<DataGet>> getTracking(@Path("id_tracking") String idTracking);

    @Headers({"aftership-api-key: e1bc990c-5652-4c88-8332-f60188329fe0", "Content-Type: application/json"})
    @GET("/v4/trackings")
    Observable<ResponseAfterShip<DataGet>> getTrackings();

    @Headers({"aftership-api-key: e1bc990c-5652-4c88-8332-f60188329fe0", "Content-Type: application/json"})
    @DELETE("/v4/trackings/{id_tracking}")
    Observable<ResponseAfterShip<Tracking<ResponseTrackingData>>> deleteTracking(@Path("id_tracking") String idTracking);
}
