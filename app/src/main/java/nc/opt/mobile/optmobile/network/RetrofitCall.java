package nc.opt.mobile.optmobile.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by orlanth23 on 03/10/2017.
 */

public interface RetrofitCall {

    @GET("IPSWeb_item_events.asp")
    Call<ResponseBody> searchParcel(@Query("itemId") String itemId,
                                    @Query("Submit") String submit);
}
