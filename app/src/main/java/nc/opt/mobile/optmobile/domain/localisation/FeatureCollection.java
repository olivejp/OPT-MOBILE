package nc.opt.mobile.optmobile.domain.localisation;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

/**
 * Created by orlanth23 on 10/08/2017.
 */

@Parcel
public class FeatureCollection {
    String type;
    List<Feature> features;

    @ParcelConstructor
    public FeatureCollection(String type, List<Feature> features) {
        this.type = type;
        this.features = features;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
