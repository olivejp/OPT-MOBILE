package nc.opt.mobile.optmobile.domain.suiviColis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Tracking {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("last_updated_at")
    @Expose
    public String lastUpdatedAt;
    @SerializedName("tracking_number")
    @Expose
    public String trackingNumber;
    @SerializedName("slug")
    @Expose
    public String slug;
    @SerializedName("active")
    @Expose
    public boolean active;
    @SerializedName("android")
    @Expose
    public List<Object> android = new ArrayList<Object>();
    @SerializedName("custom_fields")
    @Expose
    public Object customFields;
    @SerializedName("customer_name")
    @Expose
    public Object customerName;
    @SerializedName("delivery_time")
    @Expose
    public long deliveryTime;
    @SerializedName("destination_country_iso3")
    @Expose
    public String destinationCountryIso3;
    @SerializedName("emails")
    @Expose
    public List<Object> emails = new ArrayList<Object>();
    @SerializedName("expected_delivery")
    @Expose
    public Object expectedDelivery;
    @SerializedName("ios")
    @Expose
    public List<Object> ios = new ArrayList<Object>();
    @SerializedName("note")
    @Expose
    public String note;
    @SerializedName("order_id")
    @Expose
    public Object orderId;
    @SerializedName("order_id_path")
    @Expose
    public Object orderIdPath;
    @SerializedName("origin_country_iso3")
    @Expose
    public String originCountryIso3;
    @SerializedName("shipment_package_count")
    @Expose
    public long shipmentPackageCount;
    @SerializedName("shipment_pickup_date")
    @Expose
    public String shipmentPickupDate;
    @SerializedName("shipment_delivery_date")
    @Expose
    public String shipmentDeliveryDate;
    @SerializedName("shipment_type")
    @Expose
    public Object shipmentType;
    @SerializedName("shipment_weight")
    @Expose
    public Object shipmentWeight;
    @SerializedName("shipment_weight_unit")
    @Expose
    public Object shipmentWeightUnit;
    @SerializedName("signed_by")
    @Expose
    public Object signedBy;
    @SerializedName("smses")
    @Expose
    public List<Object> smses = new ArrayList<Object>();
    @SerializedName("source")
    @Expose
    public String source;
    @SerializedName("tag")
    @Expose
    public String tag;
    @SerializedName("subtag")
    @Expose
    public String subtag;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("tracked_count")
    @Expose
    public long trackedCount;
    @SerializedName("last_mile_tracking_supported")
    @Expose
    public Object lastMileTrackingSupported;
    @SerializedName("unique_token")
    @Expose
    public String uniqueToken;
    @SerializedName("checkpoints")
    @Expose
    public List<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
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
     */
    public Tracking() {
    }

    /**
     * @param checkpoints
     * @param subtag
     * @param signedBy
     * @param android
     * @param shipmentType
     * @param shipmentWeight
     * @param tag
     * @param destinationCountryIso3
     * @param shipmentPackageCount
     * @param trackingPostalCode
     * @param orderIdPath
     * @param smses
     * @param id
     * @param shipmentWeightUnit
     * @param lastUpdatedAt
     * @param trackingKey
     * @param title
     * @param shipmentDeliveryDate
     * @param trackingShipDate
     * @param createdAt
     * @param lastMileTrackingSupported
     * @param trackingAccountNumber
     * @param note
     * @param orderId
     * @param shipmentPickupDate
     * @param uniqueToken
     * @param customerName
     * @param customFields
     * @param ios
     * @param trackedCount
     * @param originCountryIso3
     * @param expectedDelivery
     * @param emails
     * @param trackingDestinationCountry
     * @param updatedAt
     * @param source
     * @param deliveryTime
     * @param trackingNumber
     * @param active
     * @param slug
     */
    public Tracking(String id, String createdAt, String updatedAt, String lastUpdatedAt, String trackingNumber, String slug, boolean active, List<Object> android, Object customFields, Object customerName, long deliveryTime, String destinationCountryIso3, List<Object> emails, Object expectedDelivery, List<Object> ios, String note, Object orderId, Object orderIdPath, String originCountryIso3, long shipmentPackageCount, String shipmentPickupDate, String shipmentDeliveryDate, Object shipmentType, Object shipmentWeight, Object shipmentWeightUnit, Object signedBy, List<Object> smses, String source, String tag, String subtag, String title, long trackedCount, Object lastMileTrackingSupported, String uniqueToken, List<Checkpoint> checkpoints, Object trackingAccountNumber, Object trackingDestinationCountry, Object trackingKey, Object trackingPostalCode, Object trackingShipDate) {
        super();
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastUpdatedAt = lastUpdatedAt;
        this.trackingNumber = trackingNumber;
        this.slug = slug;
        this.active = active;
        this.android = android;
        this.customFields = customFields;
        this.customerName = customerName;
        this.deliveryTime = deliveryTime;
        this.destinationCountryIso3 = destinationCountryIso3;
        this.emails = emails;
        this.expectedDelivery = expectedDelivery;
        this.ios = ios;
        this.note = note;
        this.orderId = orderId;
        this.orderIdPath = orderIdPath;
        this.originCountryIso3 = originCountryIso3;
        this.shipmentPackageCount = shipmentPackageCount;
        this.shipmentPickupDate = shipmentPickupDate;
        this.shipmentDeliveryDate = shipmentDeliveryDate;
        this.shipmentType = shipmentType;
        this.shipmentWeight = shipmentWeight;
        this.shipmentWeightUnit = shipmentWeightUnit;
        this.signedBy = signedBy;
        this.smses = smses;
        this.source = source;
        this.tag = tag;
        this.subtag = subtag;
        this.title = title;
        this.trackedCount = trackedCount;
        this.lastMileTrackingSupported = lastMileTrackingSupported;
        this.uniqueToken = uniqueToken;
        this.checkpoints = checkpoints;
        this.trackingAccountNumber = trackingAccountNumber;
        this.trackingDestinationCountry = trackingDestinationCountry;
        this.trackingKey = trackingKey;
        this.trackingPostalCode = trackingPostalCode;
        this.trackingShipDate = trackingShipDate;
    }
}