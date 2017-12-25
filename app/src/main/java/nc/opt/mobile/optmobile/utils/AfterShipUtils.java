package nc.opt.mobile.optmobile.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;
import nc.opt.mobile.optmobile.domain.suivi.aftership.Checkpoint;
import nc.opt.mobile.optmobile.domain.suivi.aftership.ResponseDataDetectCourier;
import nc.opt.mobile.optmobile.domain.suivi.aftership.SendTrackingData;
import nc.opt.mobile.optmobile.domain.suivi.aftership.Tracking;
import nc.opt.mobile.optmobile.domain.suivi.aftership.TrackingData;
import nc.opt.mobile.optmobile.domain.suivi.aftership.TrackingDelete;
import nc.opt.mobile.optmobile.network.RetrofitClient;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by orlanth23 on 18/12/2017.
 */

public class AfterShipUtils {

    private static final String TAG = AfterShipUtils.class.getName();

    private static final Integer COUNTER_START = 1;
    private static final Integer ATTEMPTS = 5;

    private AfterShipUtils() {
    }

    // This consumer only catch the Throwables and log them.
    private static Consumer<Throwable> consumerThrowable = throwable -> Log.e(TAG, "Erreur sur l'API AfterShip : " + throwable.getMessage(), throwable);

    // This consumer is only called when deleting colis
    private static Consumer<TrackingDelete> consumerDeleting = trackingDelete -> Log.d(TAG, "Suppression effective du tracking " + trackingDelete.getId());

    // Found on : https://medium.com/@v.danylo/server-polling-and-retrying-failed-operations-with-retrofit-and-rxjava-8bcc7e641a5a
    private static <T> ObservableTransformer<T, Long> zipWithFlatMap() {
        return observable -> observable.zipWith(
                Observable.range(COUNTER_START, ATTEMPTS), (t, repeatAttempt) -> repeatAttempt)
                .flatMap(repeatAttempt -> Observable.timer(repeatAttempt , SECONDS));
    }

    public static void getTrackingFromAfterShip(String trackingNumber, Consumer<ColisEntity> consumerColisEntity) {

        // Creation of the colisEntity which we will subscribe at the end.
        ColisEntity colisEntity = new ColisEntity();
        colisEntity.setIdColis(trackingNumber);

        // This consumer is only called when getting colis
        Consumer<TrackingData> consumerGetTracking = trackingData -> {
            Log.d(TAG, "TrackingData récupéré : " + trackingData.toString());
            RetrofitClient.deleteTracking(trackingData.getId())
                    .retry(3)
                    .subscribe(consumerDeleting, consumerThrowable);
            Observable.just(createColisFromResponseTrackingData(trackingData)).subscribe(consumerColisEntity);
        };

        // This consumer is only called when posting colis
        Consumer<TrackingData> consumerPostTracking = trackingDataPosted -> {
            Log.d(TAG, "Post Tracking Successful, try to get the tracking by get trackings/:id");
            RetrofitClient.getTracking(trackingDataPosted.getId())
                    .takeUntil(trackingData -> !trackingData.getCheckpoints().isEmpty())
                    .filter(trackingData -> !trackingData.getCheckpoints().isEmpty())
                    .repeatWhen(objectObservable -> objectObservable.compose(zipWithFlatMap()))
                    .subscribe(consumerGetTracking, consumerThrowable);
        };

        // Consumer detect courier
        Consumer<ResponseDataDetectCourier> consumerDetectCourier = responseDataDetectCourier -> {
            Log.d(TAG, "Essaie de détecter le bon courier.");
            if (!responseDataDetectCourier.getCouriers().isEmpty()) {
                String slug = responseDataDetectCourier.getCouriers().get(0).getSlug();
                colisEntity.setSlug(slug);

                Log.d(TAG, "Slug trouvé pour le colis : " + trackingNumber + ", il s'agit de " + slug);

                // Post d'un numéro
                RetrofitClient.postTracking(trackingNumber)
                        .doOnError(throwable0 -> {
                            Log.d(TAG, "Post tracking fail, try to get it by get trackings/:slug/:trackingNumber");
                            RetrofitClient.getTracking(slug, trackingNumber)
                                    .retry(3)
                                    .subscribe(consumerGetTracking, consumerThrowable);
                        })
                        .delay(10, SECONDS)
                        .subscribe(consumerPostTracking, consumerThrowable);
            } else {
                Log.d(TAG, "No courier was found for this tracking number.");
            }
        };

        // Try to detect the courier of the colis
        RetrofitClient.detectCourier(trackingNumber)
                .retry(3)
                .subscribe(consumerDetectCourier, consumerThrowable);
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
