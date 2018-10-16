package nc.opt.mobile.optmobile.network.opt;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by orlanth23 on 15/01/2018.
 */

public interface RetrofitOptCall {
    @GET("/ipswebtracking/IPSWeb_item_events.asp?Submit=Envoyer")
    Observable<String> getTrackingOpt(@Query("itemid") String idTracking);
}
