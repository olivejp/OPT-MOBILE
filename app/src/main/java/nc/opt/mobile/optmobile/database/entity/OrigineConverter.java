package nc.opt.mobile.optmobile.database.entity;

import android.arch.persistence.room.TypeConverter;

import static nc.opt.mobile.optmobile.database.entity.EtapeEntity.EtapeOrigine.AFTER_SHIP;
import static nc.opt.mobile.optmobile.database.entity.EtapeEntity.EtapeOrigine.OPT;

/**
 * Created by orlanth23 on 10/01/2018.
 */

public class OrigineConverter {
    @TypeConverter
    public static EtapeEntity.EtapeOrigine getValue(String value) {
        if (value.equals(AFTER_SHIP.getValue())) {
            return AFTER_SHIP;
        } else if (value.equals(OPT.getValue())) {
            return OPT;
        } else {
            throw new IllegalArgumentException("Could not recognize status");
        }
    }

    @TypeConverter
    public static String toValue(EtapeEntity.EtapeOrigine origine) {
        return origine.getValue();
    }
}
