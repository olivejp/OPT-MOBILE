package nc.opt.mobile.optmobile.job.aftership;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by orlanth23 on 12/12/2017.
 */
public class ResponseAfterShip<T> {

    @SerializedName("meta")
    @Expose
    public Meta meta;

    @SerializedName("data")
    @Expose
    public T data;

    /**
     * No args constructor for use in serialization
     */
    public ResponseAfterShip() {
    }

    /**
     * @param data
     * @param meta
     */
    public ResponseAfterShip(Meta meta, T data) {
        super();
        this.meta = meta;
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
