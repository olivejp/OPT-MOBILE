package nc.opt.mobile.optmobile.database.local.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by orlanth23 on 11/01/2018.
 */

public interface AbstractDao<T> {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T... entities);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<T> entities);

    @Transaction
    @Update
    void update(T... entities);

    @Transaction
    @Delete
    void delete(T... entities);

    @Transaction
    @Delete
    void delete(List<T> entities);
}
