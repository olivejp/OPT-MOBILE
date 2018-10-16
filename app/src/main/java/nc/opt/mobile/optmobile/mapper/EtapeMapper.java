package nc.opt.mobile.optmobile.mapper;


import android.support.annotation.NonNull;

import nc.opt.mobile.optmobile.DateConverter;
import nc.opt.mobile.optmobile.database.local.StepOrigine;
import nc.opt.mobile.optmobile.database.local.entity.ColisEntity;
import nc.opt.mobile.optmobile.database.local.entity.StepEntity;
import nc.opt.mobile.optmobile.job.opt.EtapeDto;

/**
 * Created by 2761oli on 23/10/2017.
 */

public class EtapeMapper {

    private EtapeMapper() {
    }

    /**
     * Convert an {@link EtapeDto} to an {@link StepEntity}
     *
     * @param idColis  Foreign key to {@link ColisEntity}
     * @param etapeDto to transform
     * @return {@link StepEntity}
     */
    static StepEntity convertToEntity(@NonNull String idColis, @NonNull EtapeDto etapeDto) {
        StepEntity stepEntity = new StepEntity();
        stepEntity.setIdColis(idColis);
        stepEntity.setDate(DateConverter.convertDateDtoToEntity(etapeDto.getDate()));
        stepEntity.setCommentaire(etapeDto.getCommentaire());
        stepEntity.setDescription(etapeDto.getDescription());
        stepEntity.setLocalisation(etapeDto.getLocalisation());
        stepEntity.setStatus(etapeDto.getStatus());
        stepEntity.setPays(etapeDto.getPays());
        stepEntity.setOrigine(StepOrigine.OPT);
        return stepEntity;
    }
}
