package nc.opt.mobile.optmobile.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import nc.opt.mobile.optmobile.database.dao.ColisDao;
import nc.opt.mobile.optmobile.database.dao.EtapeDao;
import nc.opt.mobile.optmobile.database.dao.ShedlockDao;
import nc.opt.mobile.optmobile.database.entity.ColisEntity;
import nc.opt.mobile.optmobile.database.entity.EtapeEntity;
import nc.opt.mobile.optmobile.database.entity.ShedlockEntity;

/**
 * Created by orlanth23 on 01/07/2017.
 */

@Database(version = 24, entities = {ColisEntity.class, EtapeEntity.class, ShedlockEntity.class})
public abstract class OptDatabase extends RoomDatabase {
    private static OptDatabase INSTANCE;

    public static OptDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), OptDatabase.class, "colis-database").build();
        }
        return INSTANCE;
    }

    abstract public ColisDao colisDao();
    abstract public EtapeDao etapeDao();
    abstract public ShedlockDao shedlockDao();
}