package nc.opt.mobile.optmobile.network;

import io.reactivex.Observable;
import nc.opt.mobile.optmobile.domain.suivi.aftership.ResponseAfterShip;
import nc.opt.mobile.optmobile.domain.suivi.aftership.ResponseDataDetectCourier;
import nc.opt.mobile.optmobile.domain.suivi.aftership.SendTrackingData;
import nc.opt.mobile.optmobile.domain.suivi.aftership.Tracking;
import nc.opt.mobile.optmobile.domain.suivi.aftership.TrackingData;
import nc.opt.mobile.optmobile.domain.suivi.aftership.TrackingDelete;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by orlanth23 on 03/10/2017.
 */

public interface RetrofitCall {

    @POST("/v4/couriers/detect")
    Observable<ResponseAfterShip<ResponseDataDetectCourier>> detectCourier(@Body Tracking tracking);

    @POST("/v4/trackings")
    Observable<ResponseAfterShip<Tracking<TrackingData>>> postTracking(@Body Tracking<SendTrackingData> tracking);

    @GET("/v4/trackings/{id_tracking}")
    Observable<ResponseAfterShip<Tracking<TrackingData>>> getTracking(@Path("id_tracking") String idTracking);

    @GET("/v4/trackings/{slug}/{tracking_number}")
    Observable<ResponseAfterShip<Tracking<TrackingData>>> getTracking(@Path("slug") String slug, @Path("tracking_number") String trackingNumber);

    @DELETE("/v4/trackings/{id_tracking}")
    Observable<ResponseAfterShip<Tracking<TrackingDelete>>> deleteTracking(@Path("id_tracking") String idTracking);
}
