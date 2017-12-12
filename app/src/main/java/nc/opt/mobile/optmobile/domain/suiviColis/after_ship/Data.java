package nc.opt.mobile.optmobile.domain.suiviColis.after_ship;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by orlanth23 on 12/12/2017.
 */
public class Data {

    @SerializedName("page")
    @Expose
    public long page;
    @SerializedName("limit")
    @Expose
    public long limit;
    @SerializedName("count")
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
    public List<Object> origin = new ArrayList<Object>();
    @SerializedName("destination")
    @Expose
    public List<Object> destination = new ArrayList<Object>();
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
    public List<Tracking> trackings = new ArrayList<Tracking>();

    /**
     * No args constructor for use in serialization
     */
    public Data() {
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
    public Data(long page, long limit, long count, String keyword, String slug, List<Object> origin, List<Object> destination, String tag, String fields, String createdAtMin, String createdAtMax, Object lastUpdatedAt, List<Tracking> trackings) {
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
}