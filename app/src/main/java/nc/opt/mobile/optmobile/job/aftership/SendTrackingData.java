package nc.opt.mobile.optmobile.job.aftership;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by orlanth23 on 17/12/2017.
 */

public class SendTrackingData {

    @SerializedName("tracking_number")
    @Expose
    public String trackingNumber;
    @SerializedName("tracking_postal_code")
    @Expose
    public String trackingPostalCode;
    @SerializedName("tracking_ship_date")
    @Expose
    public String trackingShipDate;
    @SerializedName("tracking_account_number")
    @Expose
    public String trackingAccountNumber;
    @SerializedName("slug")
    @Expose
    public List<String> slug;

    /**
     * No args constructor for use in serialization
     */
    public SendTrackingData() {
    }

    /**
     * @param trackingNumber
     * @param trackingShipDate
     * @param trackingAccountNumber
     * @param slug
     * @param trackingPostalCode
     */
    public SendTrackingData(String trackingNumber, String trackingPostalCode, String trackingShipDate, String trackingAccountNumber, List<String> slug) {
        super();
        this.trackingNumber = trackingNumber;
        this.trackingPostalCode = trackingPostalCode;
        this.trackingShipDate = trackingShipDate;
        this.trackingAccountNumber = trackingAccountNumber;
        this.slug = slug;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getTrackingPostalCode() {
        return trackingPostalCode;
    }

    public void setTrackingPostalCode(String trackingPostalCode) {
        this.trackingPostalCode = trackingPostalCode;
    }

    public String getTrackingShipDate() {
        return trackingShipDate;
    }

    public void setTrackingShipDate(String trackingShipDate) {
        this.trackingShipDate = trackingShipDate;
    }

    public String getTrackingAccountNumber() {
        return trackingAccountNumber;
    }

    public void setTrackingAccountNumber(String trackingAccountNumber) {
        this.trackingAccountNumber = trackingAccountNumber;
    }

    public List<String> getSlug() {
        return slug;
    }

    public void setSlug(List<String> slug) {
        this.slug = slug;
    }
}