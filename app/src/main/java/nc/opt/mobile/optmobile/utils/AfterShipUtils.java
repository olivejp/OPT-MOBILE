package nc.opt.mobile.optmobile.utils;

import android.content.Context;
import android.util.Log;

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
import nc.opt.mobile.optmobile.provider.services.ColisService;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by orlanth23 on 18/12/2017.
 */

public class AfterShipUtils {

    private static final String TAG = AfterShipUtils.class.getName();

    private static final Integer COUNTER_START = 1;
    private static final Integer ATTEMPTS = 3;

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
                .flatMap(repeatAttempt -> Observable.timer(repeatAttempt, SECONDS));
    }

    public static void getTrackingFromAfterShip(Context context, String trackingNumber, Consumer<ColisEntity> consumerColisEntity) {

        // Get the Colis from the content provider and we will send it at the end.
        ColisEntity colisFromDb = ColisService.get(context, trackingNumber);
        if (colisFromDb == null) {
            colisFromDb = new ColisEntity();
            colisFromDb.setIdColis(trackingNumber);
        }

        // Ce colis est final pour pouvoir être appelé dans les différents Consumers<>
        final ColisEntity finalColis = colisFromDb;

        // This consumer is only called when getting colis
        Consumer<TrackingData> consumerGetTracking = trackingData -> {
            Log.d(TAG, "TrackingData récupéré : " + trackingData.toString());
            Observable.just(createColisFromResponseTrackingData(finalColis, trackingData)).subscribe(consumerColisEntity, consumerThrowable);
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
                finalColis.setSlug(slug);

                Log.d(TAG, "Slug trouvé pour le colis : " + trackingNumber + ", il s'agit de " + slug);

                // Post d'un numéro
                RetrofitClient.postTracking(trackingNumber)
                        .doOnError(throwable0 -> {
                            Log.d(TAG, "Post tracking fail, try to get it by get trackings/:slug/:trackingNumber");
                            RetrofitClient.getTracking(slug, trackingNumber)
                                    .takeUntil(trackingData -> !trackingData.getCheckpoints().isEmpty())
                                    .filter(trackingData -> !trackingData.getCheckpoints().isEmpty())
                                    .repeatWhen(objectObservable -> objectObservable.compose(zipWithFlatMap()))
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
        etape.setStatus((checkpoint.getTag() != null) ? checkpoint.getTag() : "");
        etape.setDescription((checkpoint.getMessage() != null) ? checkpoint.getMessage() : "");
        etape.setCommentaire((checkpoint.getSubtag() != null) ? checkpoint.getSubtag() : "");
        etape.setPays((checkpoint.getCountryName() != null) ? checkpoint.getCountryName().toString() : "");
        return etape;
    }

    /**
     * Fill the ColisEntity with the TrackingData informations.
     *
     * @param colis
     * @param trackingData
     * @return
     */
    public static ColisEntity createColisFromResponseTrackingData(ColisEntity colis, TrackingData trackingData) {
        colis.setDeleted(0);
        for (Checkpoint c : trackingData.getCheckpoints()) {
            EtapeEntity etapeEntity = createEtapeFromCheckpoint(colis.getIdColis(), c);
            colis.getEtapeAcheminementArrayList().add(etapeEntity);
        }
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
