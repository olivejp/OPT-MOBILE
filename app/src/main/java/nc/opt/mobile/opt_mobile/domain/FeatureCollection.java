package nc.opt.mobile.opt_mobile.domain;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by orlanth23 on 10/08/2017.
 */

@Parcel
public class FeatureCollection {
    private String type;
    private ArrayList<Feature> features;

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
