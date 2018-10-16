package nc.opt.mobile.optmobile.job.aftership;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TrackingData {

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
    public List<Object> android = new ArrayList<>();
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
    public List<Object> emails = new ArrayList<>();
    @SerializedName("expected_delivery")
    @Expose
    public Object expectedDelivery;
    @SerializedName("ios")
    @Expose
    public List<Object> ios = new ArrayList<>();
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
    public List<Object> smses = new ArrayList<>();
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
    public List<Checkpoint> checkpoints = new ArrayList<>();
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
    public TrackingData() {
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
    public TrackingData(String id, String createdAt, String updatedAt, String lastUpdatedAt, String trackingNumber, String slug, boolean active, List<Object> android, Object customFields, Object customerName, long deliveryTime, String destinationCountryIso3, List<Object> emails, Object expectedDelivery, List<Object> ios, String note, Object orderId, Object orderIdPath, String originCountryIso3, long shipmentPackageCount, String shipmentPickupDate, String shipmentDeliveryDate, Object shipmentType, Object shipmentWeight, Object shipmentWeightUnit, Object signedBy, List<Object> smses, String source, String tag, String subtag, String title, long trackedCount, Object lastMileTrackingSupported, String uniqueToken, List<Checkpoint> checkpoints, Object trackingAccountNumber, Object trackingDestinationCountry, Object trackingKey, Object trackingPostalCode, Object trackingShipDate) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(String lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Object> getAndroid() {
        return android;
    }

    public void setAndroid(List<Object> android) {
        this.android = android;
    }

    public Object getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Object customFields) {
        this.customFields = customFields;
    }

    public Object getCustomerName() {
        return customerName;
    }

    public void setCustomerName(Object customerName) {
        this.customerName = customerName;
    }

    public long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDestinationCountryIso3() {
        return destinationCountryIso3;
    }

    public void setDestinationCountryIso3(String destinationCountryIso3) {
        this.destinationCountryIso3 = destinationCountryIso3;
    }

    public List<Object> getEmails() {
        return emails;
    }

    public void setEmails(List<Object> emails) {
        this.emails = emails;
    }

    public Object getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(Object expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public List<Object> getIos() {
        return ios;
    }

    public void setIos(List<Object> ios) {
        this.ios = ios;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Object getOrderId() {
        return orderId;
    }

    public void setOrderId(Object orderId) {
        this.orderId = orderId;
    }

    public Object getOrderIdPath() {
        return orderIdPath;
    }

    public void setOrderIdPath(Object orderIdPath) {
        this.orderIdPath = orderIdPath;
    }

    public String getOriginCountryIso3() {
        return originCountryIso3;
    }

    public void setOriginCountryIso3(String originCountryIso3) {
        this.originCountryIso3 = originCountryIso3;
    }

    public long getShipmentPackageCount() {
        return shipmentPackageCount;
    }

    public void setShipmentPackageCount(long shipmentPackageCount) {
        this.shipmentPackageCount = shipmentPackageCount;
    }

    public String getShipmentPickupDate() {
        return shipmentPickupDate;
    }

    public void setShipmentPickupDate(String shipmentPickupDate) {
        this.shipmentPickupDate = shipmentPickupDate;
    }

    public String getShipmentDeliveryDate() {
        return shipmentDeliveryDate;
    }

    public void setShipmentDeliveryDate(String shipmentDeliveryDate) {
        this.shipmentDeliveryDate = shipmentDeliveryDate;
    }

    public Object getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(Object shipmentType) {
        this.shipmentType = shipmentType;
    }

    public Object getShipmentWeight() {
        return shipmentWeight;
    }

    public void setShipmentWeight(Object shipmentWeight) {
        this.shipmentWeight = shipmentWeight;
    }

    public Object getShipmentWeightUnit() {
        return shipmentWeightUnit;
    }

    public void setShipmentWeightUnit(Object shipmentWeightUnit) {
        this.shipmentWeightUnit = shipmentWeightUnit;
    }

    public Object getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(Object signedBy) {
        this.signedBy = signedBy;
    }

    public List<Object> getSmses() {
        return smses;
    }

    public void setSmses(List<Object> smses) {
        this.smses = smses;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSubtag() {
        return subtag;
    }

    public void setSubtag(String subtag) {
        this.subtag = subtag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTrackedCount() {
        return trackedCount;
    }

    public void setTrackedCount(long trackedCount) {
        this.trackedCount = trackedCount;
    }

    public Object getLastMileTrackingSupported() {
        return lastMileTrackingSupported;
    }

    public void setLastMileTrackingSupported(Object lastMileTrackingSupported) {
        this.lastMileTrackingSupported = lastMileTrackingSupported;
    }

    public String getUniqueToken() {
        return uniqueToken;
    }

    public void setUniqueToken(String uniqueToken) {
        this.uniqueToken = uniqueToken;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
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

    @Override
    public String toString() {
        return "TrackingData{" +
                "id='" + id + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", lastUpdatedAt='" + lastUpdatedAt + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", slug='" + slug + '\'' +
                ", active=" + active +
                ", android=" + android +
                ", customFields=" + customFields +
                ", customerName=" + customerName +
                ", deliveryTime=" + deliveryTime +
                ", destinationCountryIso3='" + destinationCountryIso3 + '\'' +
                ", emails=" + emails +
                ", expectedDelivery=" + expectedDelivery +
                ", ios=" + ios +
                ", note='" + note + '\'' +
                ", orderId=" + orderId +
                ", orderIdPath=" + orderIdPath +
                ", originCountryIso3='" + originCountryIso3 + '\'' +
                ", shipmentPackageCount=" + shipmentPackageCount +
                ", shipmentPickupDate='" + shipmentPickupDate + '\'' +
                ", shipmentDeliveryDate='" + shipmentDeliveryDate + '\'' +
                ", shipmentType=" + shipmentType +
                ", shipmentWeight=" + shipmentWeight +
                ", shipmentWeightUnit=" + shipmentWeightUnit +
                ", signedBy=" + signedBy +
                ", smses=" + smses +
                ", source='" + source + '\'' +
                ", tag='" + tag + '\'' +
                ", subtag='" + subtag + '\'' +
                ", title='" + title + '\'' +
                ", trackedCount=" + trackedCount +
                ", lastMileTrackingSupported=" + lastMileTrackingSupported +
                ", uniqueToken='" + uniqueToken + '\'' +
                ", checkpoints=" + checkpoints +
                ", trackingAccountNumber=" + trackingAccountNumber +
                ", trackingDestinationCountry=" + trackingDestinationCountry +
                ", trackingKey=" + trackingKey +
                ", trackingPostalCode=" + trackingPostalCode +
                ", trackingShipDate=" + trackingShipDate +
                '}';
    }
}