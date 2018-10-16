package nc.opt.mobile.optmobile.job.aftership;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by orlanth23 on 15/12/2017.
 */

public class TrackingDelete {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("tracking_number")
    @Expose
    public String trackingNumber;
    @SerializedName("slug")
    @Expose
    public String slug;
    @SerializedName("tracking_account_number")
    @Expose
    public Object trackingAccountNumber;
    @SerializedName("tracking_destination_country")
    @Expose
    public Object trackingDestinationCountry;
    @SerializedName("tracking_key")
    @Expose
    public Object trackingKey;
    @SerializedName("tracking_postal_code")
    @Expose
    public Object trackingPostalCode;
    @SerializedName("tracking_ship_date")
    @Expose
    public Object trackingShipDate;

    /**
     * No args constructor for use in serialization
     *
     */
    public TrackingDelete() {
    }

    /**
     *
     * @param id
     * @param trackingKey
     * @param trackingNumber
     * @param trackingShipDate
     * @param trackingAccountNumber
     * @param slug
     * @param trackingPostalCode
     * @param trackingDestinationCountry
     */
    public TrackingDelete(String id, String trackingNumber, String slug, Object trackingAccountNumber, Object trackingDestinationCountry, Object trackingKey, Object trackingPostalCode, Object trackingShipDate) {
        super();
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.slug = slug;
        this.trackingAccountNumber = trackingAccountNumber;
        this.trackingDestinationCountry = trackingDestinationCountry;
        this.trackingKey = trackingKey;
        this.trackingPostalCode = trackingPostalCode;
        this.trackingShipDate = trackingShipDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Object getTrackingAccountNumber() {
        return trackingAccountNumber;
    }

    public void setTrackingAccountNumber(Object trackingAccountNumber) {
        this.trackingAccountNumber = trackingAccountNumber;
    }

    public Object getTrackingDestinationCountry() {
        return trackingDestinationCountry;
    }

    public void setTrackingDestinationCountry(Object trackingDestinationCountry) {
        this.trackingDestinationCountry = trackingDestinationCountry;
    }

    public Object getTrackingKey() {
        return trackingKey;
    }

    public void setTrackingKey(Object trackingKey) {
        this.trackingKey = trackingKey;
    }

    public Object getTrackingPostalCode() {
        return trackingPostalCode;
    }

    public void setTrackingPostalCode(Object trackingPostalCode) {
        this.trackingPostalCode = trackingPostalCode;
    }

    public Object getTrackingShipDate() {
        return trackingShipDate;
    }

    public void setTrackingShipDate(Object trackingShipDate) {
        this.trackingShipDate = trackingShipDate;
    }
}