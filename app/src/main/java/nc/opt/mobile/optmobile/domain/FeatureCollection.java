package nc.opt.mobile.optmobile.domain;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;

/**
 * Created by orlanth23 on 10/08/2017.
 */

@Parcel
public class FeatureCollection {
    String type;
    ArrayList<Feature> features;

    @ParcelConstructor
    public FeatureCollection(String type, ArrayList<Feature> features) {
        this.type = type;
        this.features = features;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<Feature> features) {
        this.features = features;
    }
}
