package nc.opt.mobile.optmobile.job.aftership;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orlanth23 on 13/12/2017.
 */

public class Courier {

    @SerializedName("slug")
    @Expose
    public String slug;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("phone")
    @Expose
    public String phone;

    @SerializedName("other_name")
    @Expose
    public String otherName;

    @SerializedName("web_url")
    @Expose
    public String webUrl;

    @SerializedName("required_fields")
    @Expose
    public List<Object> requiredFields = new ArrayList<>();

    @SerializedName("optional_fields")
    @Expose
    public List<Object> optionalFields = new ArrayList<>();

    @SerializedName("default_language")
    @Expose
    public String defaultLanguage;

    @SerializedName("support_languages")
    @Expose
    public List<String> supportLanguages = new ArrayList<>();

    @SerializedName("service_from_country_iso3")
    @Expose
    public List<String> serviceFromCountryIso3 = new ArrayList<>();


    /**
     * No args constructor for use in serialization
     */
    public Courier() {
    }

    /**
     * @param webUrl
     * @param otherName
     * @param serviceFromCountryIso3
     * @param phone
     * @param requiredFields
     * @param name
     * @param defaultLanguage
     * @param slug
     * @param supportLanguages
     * @param optionalFields
     */
    public Courier(String slug, String name, String phone, String otherName, String webUrl, List<Object> requiredFields, List<Object> optionalFields, String defaultLanguage, List<String> supportLanguages, List<String> serviceFromCountryIso3) {
        super();
        this.slug = slug;
        this.name = name;
        this.phone = phone;
        this.otherName = otherName;
        this.webUrl = webUrl;
        this.requiredFields = requiredFields;
        this.optionalFields = optionalFields;
        this.defaultLanguage = defaultLanguage;
        this.supportLanguages = supportLanguages;
        this.serviceFromCountryIso3 = serviceFromCountryIso3;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public List<Object> getRequiredFields() {
        return requiredFields;
    }

    public void setRequiredFields(List<Object> requiredFields) {
        this.requiredFields = requiredFields;
    }

    public List<Object> getOptionalFields() {
        return optionalFields;
    }

    public void setOptionalFields(List<Object> optionalFields) {
        this.optionalFields = optionalFields;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public List<String> getSupportLanguages() {
        return supportLanguages;
    }

    public void setSupportLanguages(List<String> supportLanguages) {
        this.supportLanguages = supportLanguages;
    }

    public List<String> getServiceFromCountryIso3() {
        return serviceFromCountryIso3;
    }

    public void setServiceFromCountryIso3(List<String> serviceFromCountryIso3) {
        this.serviceFromCountryIso3 = serviceFromCountryIso3;
    }
}
