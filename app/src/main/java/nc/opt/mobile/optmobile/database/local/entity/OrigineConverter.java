package nc.opt.mobile.optmobile.database.local.entity;

import android.arch.persistence.room.TypeConverter;

import nc.opt.mobile.optmobile.database.local.StepOrigine;


/**
 * Created by orlanth23 on 10/01/2018.
 */

public class OrigineConverter {
    @TypeConverter
    public static StepOrigine getValue(String value) {
        if (value.equals(StepOrigine.AFTER_SHIP.getValue())) {
            return StepOrigine.AFTER_SHIP;
        } else if (value.equals(StepOrigine.OPT.getValue())) {
            return StepOrigine.OPT;
        } else {
            throw new IllegalArgumentException("Could not recognize status");
        }
    }

    @TypeConverter
    public static String toValue(StepOrigine origine) {
        return origine.getValue();
    }
}
