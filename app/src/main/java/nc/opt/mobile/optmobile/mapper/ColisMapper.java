package nc.opt.mobile.optmobile.mapper;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nc.opt.mobile.optmobile.database.local.entity.ColisWithSteps;
import nc.opt.mobile.optmobile.database.local.entity.StepEntity;
import nc.opt.mobile.optmobile.job.opt.ColisDto;
import nc.opt.mobile.optmobile.job.opt.EtapeDto;

/**
 * Created by 2761oli on 23/10/2017.
 */

public class ColisMapper {

    private static final String TAG = ColisMapper.class.getName();

    private ColisMapper() {
    }

    /**
     * Create a new ColisWithSteps
     * Fill the ColisEntity with the {@link ColisDto} informations.
     *
     * @param dto
     * @return {@link ColisWithSteps}
     */
    public static ColisWithSteps convertToEntity(ColisDto dto, ColisWithSteps result) {
        Log.d(TAG, "(convertToEntity)");
        if (dto.getEtapeDtoArrayList() != null && !dto.getEtapeDtoArrayList().isEmpty()) {
            List<StepEntity> listStepEntity = new ArrayList<>();
            for (EtapeDto etapeDto : dto.getEtapeDtoArrayList()) {
                listStepEntity.add(EtapeMapper.convertToEntity(dto.getIdColis(), etapeDto));
            }
            result.stepEntityList = listStepEntity;
        }
        return result;
    }
}
