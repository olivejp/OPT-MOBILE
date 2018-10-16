package nc.opt.mobile.optmobile.database.local.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orlanth23 on 12/01/2018.
 */

public class ColisWithSteps {
    @Embedded
    public ColisEntity colisEntity;

    @Relation(parentColumn = "idColis", entityColumn = "idColis")
    public List<StepEntity> stepEntityList;

    public ColisWithSteps() {
        this.colisEntity = new ColisEntity();
        this.stepEntityList = new ArrayList<>();
        this.colisEntity.setDeleted(0);
        this.colisEntity.setDelivered(0);
    }

    public ColisEntity getColisEntity() {
        return colisEntity;
    }

    public void setColisEntity(ColisEntity colisEntity) {
        this.colisEntity = colisEntity;
    }

    public List<StepEntity> getStepEntityList() {
        return stepEntityList;
    }

    @Override
    public String toString() {
        return "ColisWithSteps{" +
                "colisEntity=" + colisEntity +
                ", stepEntityList=" + stepEntityList +
                '}';
    }
}
