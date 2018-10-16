package nc.opt.mobile.optmobile.job.aftership;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by orlanth23 on 12/12/2017.
 */
public class DataGet {

    @SerializedName("page")
    @Expose
    public long page;

    @SerializedName("limit")
    @Expose
    public long limit;

    @SerializedName("findBy")
    @Expose
    public long count;

    @SerializedName("keyword")
    @Expose
    public String keyword;

    @SerializedName("slug")
    @Expose
    public String slug;

    @SerializedName("origin")
    @Expose
    public List<Object> origin = new ArrayList<>();

    @SerializedName("destination")
    @Expose
    public List<Object> destination = new ArrayList<>();

    @SerializedName("tag")
    @Expose
    public String tag;

    @SerializedName("fields")
    @Expose
    public String fields;

    @SerializedName("created_at_min")
    @Expose
    public String createdAtMin;

    @SerializedName("created_at_max")
    @Expose
    public String createdAtMax;

    @SerializedName("last_updated_at")
    @Expose
    public Object lastUpdatedAt;

    @SerializedName("trackings")
    @Expose
    public List<TrackingData> trackings = new ArrayList<>();

    /**
     * No args constructor for use in serialization
     */
    public DataGet() {
    }

    /**
     * @param limit
     * @param createdAtMax
     * @param count
     * @param tag
     * @param keyword
     * @param origin
     * @param createdAtMin
     * @param destination
     * @param trackings
     * @param lastUpdatedAt
     * @param page
     * @param slug
     * @param fields
     */
    public DataGet(long page, long limit, long count, String keyword, String slug, List<Object> origin, List<Object> destination, String tag, String fields, String createdAtMin, String createdAtMax, Object lastUpdatedAt, List<TrackingData> trackings) {
        super();
        this.page = page;
        this.limit = limit;
        this.count = count;
        this.keyword = keyword;
        this.slug = slug;
        this.origin = origin;
        this.destination = destination;
        this.tag = tag;
        this.fields = fields;
        this.createdAtMin = createdAtMin;
        this.createdAtMax = createdAtMax;
        this.lastUpdatedAt = lastUpdatedAt;
        this.trackings = trackings;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public List<Object> getOrigin() {
        return origin;
    }

    public void setOrigin(List<Object> origin) {
        this.origin = origin;
    }

    public List<Object> getDestination() {
        return destination;
    }

    public void setDestination(List<Object> destination) {
        this.destination = destination;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getCreatedAtMin() {
        return createdAtMin;
    }

    public void setCreatedAtMin(String createdAtMin) {
        this.createdAtMin = createdAtMin;
    }

    public String getCreatedAtMax() {
        return createdAtMax;
    }

    public void setCreatedAtMax(String createdAtMax) {
        this.createdAtMax = createdAtMax;
    }

    public Object getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Object lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public List<TrackingData> getTrackings() {
        return trackings;
    }

    public void setTrackings(List<TrackingData> trackings) {
        this.trackings = trackings;
    }
}