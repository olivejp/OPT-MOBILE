package nc.opt.mobile.optmobile.network;

import retrofit2.Retrofit;

/**
 * Created by orlanth23 on 12/12/2017.
 */

public class RetrofitUtils {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.example.com")
            .build();
}
