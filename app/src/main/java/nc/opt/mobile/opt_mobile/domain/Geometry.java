package nc.opt.mobile.opt_mobile.domain;

import org.parceler.Parcel;

/**
 * Created by orlanth23 on 08/08/2017.
 */

@Parcel
public class Geometry {
    private String type;
    private double[] coordinates;

    public Geometry(String type, double[] coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
