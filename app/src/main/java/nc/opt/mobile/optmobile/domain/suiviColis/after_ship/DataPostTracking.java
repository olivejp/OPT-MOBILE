package nc.opt.mobile.optmobile.domain.suiviColis.after_ship;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by orlanth23 on 12/12/2017.
 */
public class DataPostTracking {

    @SerializedName("tracking")
    @Expose
    public ResponseTrackingData trackings = new ResponseTrackingData();

    /**
     * No args constructor for use in serialization
     */
    public DataPostTracking() {
    }

    /**
     *
     * @param trackings
     */
    public DataPostTracking(ResponseTrackingData trackings) {
        super();
        this.trackings = trackings;
    }

    public ResponseTrackingData getTrackings() {
        return trackings;
    }

    public void setTrackings(ResponseTrackingData trackings) {
        this.trackings = trackings;
    }
}