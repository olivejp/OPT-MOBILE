package nc.opt.mobile.optmobile.job.aftership;

/**
 * Created by orlanth23 on 17/12/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tracking<T> {

    @SerializedName("tracking")
    @Expose
    public T tracking;

    /**
     * No args constructor for use in serialization
     */
    public Tracking() {
    }

    /**
     * @param tracking
     */
    public Tracking(T tracking) {
        super();
        this.tracking = tracking;
    }

    public T getTracking() {
        return tracking;
    }

    public void setTracking(T tracking) {
        this.tracking = tracking;
    }
}
