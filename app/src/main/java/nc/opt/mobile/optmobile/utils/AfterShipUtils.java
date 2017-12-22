package nc.opt.mobile.optmobile.utils;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import nc.opt.mobile.optmobile.domain.suivi.aftership.Checkpoint;
import nc.opt.mobile.optmobile.domain.suivi.aftership.SendTrackingData;
import nc.opt.mobile.optmobile.domain.suivi.aftership.Tracking;
import nc.opt.mobile.optmobile.domain.suivi.aftership.TrackingData;
import nc.opt.mobile.optmobile.domain.suivi.aftership.TrackingDelete;
import nc.opt.mobile.optmobile.network.RetrofitClient;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;
import nc.opt.mobile.optmobile.provider.services.ColisService;

/**
 * Created by orlanth23 on 18/12/2017.
 */

public class AfterShipUtils {

    private static final String TAG = AfterShipUtils.class.getName();

    private AfterShipUtils() {
    }

    /**
     * @param context
     * @param trackingData
     * @return
     */
    private static boolean insertTrackingToDb(Context context, TrackingData trackingData) {
        ColisEntity colisEntity = AfterShipUtils.createColisFromResponseTrackingData(trackingData);
        return ColisService.save(context, colisEntity);
    }

    /**
     * Try to post a tracking number
     * -If Ok -> Call getTracking
     * @param context
     * @param trackingNumber
     */
    public static void getTrackingFromAfterShip(Context context, String trackingNumber) {
        // This consumer only catch the Throwables and log them.
        Consumer<Throwable> consumerThrowable = throwable -> Log.e(TAG, throwable.getMessage(), throwable);

        // This consumer is only called when deleting colis
        Consumer<TrackingDelete> consumerDeleting = trackingDelete -> Log.d(TAG, "Suppression effective du numéro " + trackingDelete.getId());

        // This consumer is only called when getting colis
        Consumer<TrackingData> consumerGetTracking = trackingData -> {
            if (insertTrackingToDb(context, trackingData)) {
                Log.d(TAG, "Insertion en base réussi");
            }
            RetrofitClient.deleteTracking(trackingData.getId()).subscribe(consumerDeleting, consumerThrowable);
        };

        // This consumer is only called when posting colis
        Consumer<TrackingData> consumerPostTracking = trackingDataPosted -> RetrofitClient.getTracking(trackingDataPosted.getId()).subscribe(consumerGetTracking, consumerThrowable);

        // This consumer check if this is the right colis, and then insert it to the DB, then delete it from AftershipApi.
        Consumer<TrackingData> consumerGetTrackings = trackingData -> {
            if (trackingData.getTrackingNumber().equals(trackingNumber)) {
                Observable.just(trackingData).subscribe(consumerGetTracking);
            }
        };

        // Post d'un numéro
        RetrofitClient.postTracking(trackingNumber)
                .doOnError(throwable0 -> RetrofitClient.getTrackings().subscribe(consumerGetTrackings, consumerThrowable))
                .subscribe(consumerPostTracking, consumerThrowable);
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
