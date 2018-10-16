package nc.opt.mobile.optmobile.database.local.repository.task;

import android.os.AsyncTask;

import nc.opt.mobile.optmobile.database.local.dao.ColisDao;
import nc.opt.mobile.optmobile.database.local.entity.ColisEntity;

/**
 * Created by orlanth23 on 11/01/2018.
 */

public class ColisRepositoryTask extends AsyncTask<ColisEntity, Void, Void> {

    private TypeTask typeTask;
    private ColisDao colisDao;

    public ColisRepositoryTask(ColisDao colisDao, TypeTask typeTask) {
        this.typeTask = typeTask;
        this.colisDao = colisDao;
    }

    @Override
    protected Void doInBackground(ColisEntity... colisEntities) {
        switch (this.typeTask) {
            case DELETE:
                colisDao.delete(colisEntities);
                break;
            case INSERT:
                colisDao.insert(colisEntities);
                break;
            case UPDATE:
                colisDao.update(colisEntities);
                break;
        }
        return null;
    }
}
