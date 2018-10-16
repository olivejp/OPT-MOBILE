package nc.opt.mobile.optmobile.job.aftership;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by orlanth23 on 12/12/2017.
 */
public class DataPostTracking {

    @SerializedName("tracking")
    @Expose
    public TrackingData trackings = new TrackingData();

    /**
     * No args constructor for use in serialization
     */
    public DataPostTracking() {
    }

    /**
     *
     * @param trackings
     */
    public DataPostTracking(TrackingData trackings) {
        super();
        this.trackings = trackings;
    }

    public TrackingData getTrackings() {
        return trackings;
    }

    public void setTrackings(TrackingData trackings) {
        this.trackings = trackings;
    }
}