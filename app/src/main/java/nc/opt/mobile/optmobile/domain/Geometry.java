package nc.opt.mobile.optmobile.domain;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by orlanth23 on 08/08/2017.
 */

@Parcel
public class Geometry {
    String type;
    double[] coordinates;

    @ParcelConstructor
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
