package nc.opt.mobile.optmobile.job.aftership;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("code")
    @Expose
    public long code;

    /**
     * No args constructor for use in serialization
     */
    public Meta() {
    }

    /**
     * @param code
     */
    public Meta(long code) {
        super();
        this.code = code;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }
}
