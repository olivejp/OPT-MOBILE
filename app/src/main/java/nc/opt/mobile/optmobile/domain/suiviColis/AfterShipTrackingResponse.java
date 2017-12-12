package nc.opt.mobile.optmobile.domain.suiviColis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by orlanth23 on 12/12/2017.
 */
public class AfterShipTrackingResponse {

    @SerializedName("meta")
    @Expose
    public Meta meta;
    @SerializedName("data")
    @Expose
    public Data data;

    /**
     * No args constructor for use in serialization
     */
    public AfterShipTrackingResponse() {
    }

    /**
     * @param data
     * @param meta
     */
    public AfterShipTrackingResponse(Meta meta, Data data) {
        super();
        this.meta = meta;
        this.data = data;
    }
}
