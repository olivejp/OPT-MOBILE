package nc.opt.mobile.optmobile.utils;

import java.util.ArrayList;
import java.util.List;

import nc.opt.mobile.optmobile.domain.suivi.aftership.Checkpoint;
import nc.opt.mobile.optmobile.domain.suivi.aftership.SendTrackingData;
import nc.opt.mobile.optmobile.domain.suivi.aftership.Tracking;
import nc.opt.mobile.optmobile.domain.suivi.aftership.TrackingData;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;

/**
 * Created by orlanth23 on 18/12/2017.
 */

public class AfterShipUtils {

    private AfterShipUtils() {
    }

    /**
     * Va créer une étape à partir d'un checkpoint
     *
     * @param idColis
     * @param checkpoint
     * @return EtapeEntity
     */
    public static EtapeEntity createEtapeFromCheckpoint(String idColis, Checkpoint checkpoint) {
        EtapeEntity etape = new EtapeEntity();
        etape.setIdColis(idColis);
        if (checkpoint.getCheckpointTime() != null) {
            etape.setDate(DateConverter.convertDateAfterShipToEntity(checkpoint.getCheckpointTime()));
        } else {
            etape.setDate(0L);
        }
        etape.setLocalisation((checkpoint.getLocation() != null) ? checkpoint.getLocation().toString() : "");
        etape.setCommentaire((checkpoint.getTag() != null) ? checkpoint.getTag() : "");
        etape.setDescription((checkpoint.getMessage() != null) ? checkpoint.getMessage() : "");
        etape.setPays((checkpoint.getCountryName() != null) ? checkpoint.getCountryName().toString() : "");
        return etape;
    }

    public static ColisEntity createColisFromResponseTrackingData(TrackingData r) {
        ColisEntity colis = new ColisEntity();
        colis.setDeleted(0);
        colis.setIdColis(r.getTrackingNumber());
        List<EtapeEntity> listEtape = new ArrayList<>();

        for (Checkpoint c : r.getCheckpoints()) {
            listEtape.add(createEtapeFromCheckpoint(colis.getIdColis(), c));
        }
        colis.setEtapeAcheminementArrayList(listEtape);
        return colis;
    }

    /**
     *
     * @param trackingNumber
     * @return
     */
    public static Tracking<SendTrackingData> createTrackingData(String trackingNumber) {
        // Création d'un tracking
        Tracking<SendTrackingData> tracking = new Tracking<>();
        SendTrackingData trackingDetect = new SendTrackingData();
        trackingDetect.setTrackingNumber(trackingNumber);
        tracking.setTracking(trackingDetect);
        return tracking;
    }
}
