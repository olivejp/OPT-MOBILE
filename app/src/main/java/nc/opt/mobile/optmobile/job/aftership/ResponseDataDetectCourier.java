package nc.opt.mobile.optmobile.job.aftership;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orlanth23 on 15/12/2017.
 */

public class ResponseDataDetectCourier {

    @SerializedName("total")
    @Expose
    public long total;

    @SerializedName("couriers")
    @Expose
    public List<Courier> couriers = new ArrayList<>();

    /**
     * No args constructor for use in serialization
     */
    public ResponseDataDetectCourier() {
    }

    /**
     * @param total
     * @param couriers
     */
    public ResponseDataDetectCourier(long total, List<Courier> couriers) {
        super();
        this.total = total;
        this.couriers = couriers;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Courier> getCouriers() {
        return couriers;
    }

    public void setCouriers(List<Courier> couriers) {
        this.couriers = couriers;
    }
}