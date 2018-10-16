package nc.opt.mobile.optmobile.job.aftership;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orlanth23 on 12/12/2017.
 */
public class Checkpoint {

    @SerializedName("slug")
    @Expose
    public String slug;

    @SerializedName("city")
    @Expose
    public Object city;

    @SerializedName("created_at")
    @Expose
    public String createdAt;

    @SerializedName("location")
    @Expose
    public Object location;

    @SerializedName("country_name")
    @Expose
    public Object countryName;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("country_iso3")
    @Expose
    public Object countryIso3;

    @SerializedName("tag")
    @Expose
    public String tag;

    @SerializedName("subtag")
    @Expose
    public String subtag;

    @SerializedName("checkpoint_time")
    @Expose
    public String checkpointTime;

    @SerializedName("coordinates")
    @Expose
    public List<Object> coordinates = new ArrayList<>();

    @SerializedName("state")
    @Expose
    public Object state;

    @SerializedName("zip")
    @Expose
    public Object zip;

    /**
     * No args constructor for use in serialization
     */
    public Checkpoint() {
    }

    /**
     * @param zip
     * @param subtag
     * @param countryName
     * @param location
     * @param countryIso3
     * @param tag
     * @param state
     * @param city
     * @param message
     * @param createdAt
     * @param checkpointTime
     * @param slug
     * @param coordinates
     */
    public Checkpoint(String slug, Object city, String createdAt, Object location, Object countryName, String message, Object countryIso3, String tag, String subtag, String checkpointTime, List<Object> coordinates, Object state, Object zip) {
        super();
        this.slug = slug;
        this.city = city;
        this.createdAt = createdAt;
        this.location = location;
        this.countryName = countryName;
        this.message = message;
        this.countryIso3 = countryIso3;
        this.tag = tag;
        this.subtag = subtag;
        this.checkpointTime = checkpointTime;
        this.coordinates = coordinates;
        this.state = state;
        this.zip = zip;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public Object getCountryName() {
        return countryName;
    }

    public void setCountryName(Object countryName) {
        this.countryName = countryName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getCountryIso3() {
        return countryIso3;
    }

    public void setCountryIso3(Object countryIso3) {
        this.countryIso3 = countryIso3;
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

    public String getCheckpointTime() {
        return checkpointTime;
    }

    public void setCheckpointTime(String checkpointTime) {
        this.checkpointTime = checkpointTime;
    }

    public List<Object> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Object> coordinates) {
        this.coordinates = coordinates;
    }

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public Object getZip() {
        return zip;
    }

    public void setZip(Object zip) {
        this.zip = zip;
    }
}