package nc.opt.mobile.optmobile.utils;

import android.content.Context;
import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.domain.suivi.ColisDto;
import nc.opt.mobile.optmobile.domain.suivi.aftership.SendTrackingData;
import nc.opt.mobile.optmobile.domain.suivi.aftership.Tracking;
import nc.opt.mobile.optmobile.domain.suivi.aftership.TrackingData;
import nc.opt.mobile.optmobile.network.RetrofitClient;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.services.ColisService;
import nc.opt.mobile.optmobile.provider.services.EtapeService;
import nc.opt.mobile.optmobile.service.FirebaseService;

import static java.util.concurrent.TimeUnit.SECONDS;
import static nc.opt.mobile.optmobile.provider.services.ColisService.convertTrackingDataToEntity;
import static nc.opt.mobile.optmobile.provider.services.ColisService.listFromProvider;

/**
 * Created by orlanth23 on 18/12/2017.
 */

public class CoreSync {
    private static final String TAG = CoreSync.class.getName();

    private CoreSync() {
    }

    // This consumer only catch the Throwables and log them.
    private static Consumer<Throwable> consThrowable = throwable -> Log.e(TAG, "Erreur sur l'API AfterShip : " + throwable.getMessage() + " Localized message : " + throwable.getLocalizedMessage(), throwable);

    /**
     * @param observable
     * @param resultColis
     * @param context
     */
    private static void callGetTracking(Observable<TrackingData> observable, ColisEntity resultColis, Context context) {
        observable.retry(2).subscribe(trackingData -> {
            Log.d(TAG, "TrackingData récupéré : " + trackingData.toString());
            Observable.just(convertTrackingDataToEntity(resultColis, trackingData))
                    .subscribe(colisEntity -> {
                        if (ColisService.save(context, colisEntity)) {
                            Log.d(TAG, "Insertion en base réussie");
                        } else {
                            Log.d(TAG, "Insertion en base échouée");
                        }
                    }, consThrowable);
        }, consThrowable);
    }

    /**
     * @param context
     * @param sendNotification
     */
    public static void getAllTracking(Context context, boolean sendNotification) {
        List<ColisEntity> list = listFromProvider(context, true);
        if (!list.isEmpty()) {
            for (ColisEntity colis : list) {
                CoreSync.getTracking(context, colis.getIdColis(), sendNotification);
            }
        }
    }

    /**
     * Tracking Core
     *
     * @param context
     * @param trackingNumber
     * @param sendNotification
     */
    public static void getTracking(Context context, String trackingNumber, boolean sendNotification) {
        // Get the Colis from the content provider, if it exists, or create a new one.
        // We will send it at the end.
        ColisEntity colisFromDb = ColisService.get(context, trackingNumber);
        if (colisFromDb == null) {
            colisFromDb = new ColisEntity();
            colisFromDb.setIdColis(trackingNumber);
        }
        ColisEntity resultColis = colisFromDb;

        // ToDo , Il faudrait pourvoir espacer les requêtes pour chaque colis pour éviter l'effet entonoir.
        // Voir le lien suivant : https://stackoverflow.com/questions/33291245/rxjava-delay-for-each-item-of-list-emitted
        // Call OPT Service
        RetrofitClient.getTrackingOpt(trackingNumber)
                .retry(2)
                .subscribe(htmlString -> {
                            Log.d(TAG, "Réponse reçue lors de l'appel service OPT : " + htmlString);
                            if (transformHtmlToColisDto(context, trackingNumber, htmlString, sendNotification)) {
                                Log.d(TAG, "Transformation de la réponse OPT OK");
                            } else {
                                Log.e(TAG, "Fail to receive response from OPT service");
                            }
                        },
                        consThrowable,
                        // Detect courier
                        () -> RetrofitClient.detectCourier(trackingNumber).retry(2).subscribe(responseDataDetectCourier -> {
                            Log.d(TAG, "Détection du bon slug.");
                            if (!responseDataDetectCourier.getCouriers().isEmpty()) {
                                String slug = responseDataDetectCourier.getCouriers().get(0).getSlug();
                                resultColis.setSlug(slug);

                                Log.d(TAG, "Slug found for the tracking : " + trackingNumber + ", it's : " + slug);

                                // Post tracking
                                RetrofitClient.postTracking(trackingNumber)
                                        .doOnError(throwable0 -> {
                                            Log.d(TAG, "Post tracking fail, try to get it by get trackings/:slug/:trackingNumber for the tracking : " + trackingNumber);
                                            callGetTracking(RetrofitClient.getTracking(slug, trackingNumber), resultColis, context);
                                        })
                                        .retry(2)
                                        .delay(10, SECONDS)
                                        .subscribe(trackingData -> {
                                            Log.d(TAG, "Post Tracking Successful, try to get the tracking by get trackings/:id");
                                            callGetTracking(RetrofitClient.getTracking(trackingData.getId()), resultColis, context);
                                        }, consThrowable);
                            } else {
                                Log.d(TAG, "No courier was found for this tracking number : " + trackingNumber);
                            }
                        }, consThrowable));
    }

    /**
     * Mets à jour la dernière date de mise à jour du colis.
     *
     * @param context
     * @param colisDto
     * @param sendNotification
     */
    private static void saveColisDto(Context context, ColisDto colisDto, boolean sendNotification) {
        ColisService.updateLastUpdate(context, colisDto.getIdColis(), true);
        ColisEntity colisEntity = ColisService.convertToEntity(colisDto);
        if (EtapeService.shouldInsertNewEtape(context, colisEntity)) {
            if (EtapeService.save(context, colisEntity)) {
                if (sendNotification)
                    NotificationSender.sendNotification(context, context.getString(R.string.app_name), colisDto.getIdColis() + " a été mis à jour.", R.drawable.ic_archive_white_48dp);
            } else {
                Log.e(TAG, "Echec de la sauvegarde du colis dans le content provider.");
            }
        } else {
            Log.d(TAG, "Ce colis n'a pas de nouvelle étape à rajouter.");
        }
    }

    /**
     * @param context
     * @param idColis
     * @param htmlToTransform
     * @param sendNotification
     * @return
     */
    private static boolean transformHtmlToColisDto(Context context, String idColis, String htmlToTransform, boolean sendNotification) {
        ColisDto colisDto = new ColisDto();
        colisDto.setIdColis(idColis);
        try {
            switch (HtmlTransformer.getColisFromHtml(htmlToTransform, colisDto)) {
                case HtmlTransformer.RESULT_SUCCESS:
                    Log.d(TAG, "HtmlTransformer return SUCCESS");
                    saveColisDto(context, colisDto, sendNotification);
                    return true;
                case HtmlTransformer.RESULT_NO_ITEM_FOUND:
                    Log.d(TAG, "HtmlTransformer return NO ITEM FOUND");
                    ColisService.updateLastUpdate(context, colisDto.getIdColis(), false);
                    return false;
                default:
                    return false;
            }
        } catch (HtmlTransformer.HtmlTransformerException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return false;
    }

    /**
     * @param trackingNumber
     * @return
     */
    public static Tracking<SendTrackingData> createTrackingData(String trackingNumber) {
        Tracking<SendTrackingData> tracking = new Tracking<>();
        SendTrackingData trackingDetect = new SendTrackingData();
        trackingDetect.setTrackingNumber(trackingNumber);
        tracking.setTracking(trackingDetect);
        return tracking;
    }

    /**
     * Delete tracking from the AfterShip account.
     *
     * @param context
     * @param colis
     */
    public static void deleteTracking(Context context, ColisEntity colis) {
        if (colis.getSlug() != null && colis.getSlug().length() != 0 && colis.getIdColis() != null && colis.getIdColis().length() != 0) {
            RetrofitClient.deleteTrackingBySlugAndTrackingNumber(colis.getSlug(), colis.getIdColis())
                    .retry(2)
                    .subscribe(trackingDelete -> {
                        Log.d(TAG, "Suppression effective du tracking " + trackingDelete.getId() + " sur l'API AfterShip");
                        FirebaseService.deleteRemoteColis(colis.getIdColis());
                        ColisService.realDelete(context, colis.getIdColis());
                    }, consThrowable);
        }
    }
}
