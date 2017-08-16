package nc.opt.mobile.opt_mobile.domain;

import org.parceler.Parcel;

/**
 * Created by orlanth23 on 10/08/2017.
 */

@Parcel
public class Feature {
    private String type;
    private Agency properties;
    private Geometry geometry;

    public Feature(String type, Agency properties, Geometry geometry) {
        this.type = type;
        this.properties = properties;
        this.geometry = geometry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Agency getProperties() {
        return properties;
    }

    public void setProperties(Agency properties) {
        this.properties = properties;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
