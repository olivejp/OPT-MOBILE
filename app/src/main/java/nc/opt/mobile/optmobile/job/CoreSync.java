package nc.opt.mobile.optmobile.job;

import android.content.Context;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import nc.opt.mobile.optmobile.NotificationSender;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.database.local.entity.ColisEntity;
import nc.opt.mobile.optmobile.database.local.entity.ColisWithSteps;
import nc.opt.mobile.optmobile.database.local.repository.ColisRepository;
import nc.opt.mobile.optmobile.database.local.repository.ColisWithStepsRepository;
import nc.opt.mobile.optmobile.database.local.repository.StepRepository;
import nc.opt.mobile.optmobile.job.opt.ColisDto;
import nc.opt.mobile.optmobile.job.opt.HtmlTransformer;
import nc.opt.mobile.optmobile.mapper.ColisMapper;
import nc.opt.mobile.optmobile.network.aftership.RetrofitClient;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by orlanth23 on 18/12/2017.
 * <p>
 * This class contains the series of network calls to make to retreive the tracking informations.
 */
class CoreSync {
    private static final String TAG = CoreSync.class.getName();

    private static CoreSync INSTANCE;
    private Context context;
    private Consumer<Throwable> consThrowable;
    private boolean sendNotification;

    private CoreSync(Context context, boolean sendNotification) {
        if (context != null) {
            this.context = context;
        }
        this.sendNotification = sendNotification;
        this.consThrowable = throwable -> Log.e(TAG, "Erreur : " + throwable.getMessage() + " Localized message : " + throwable.getLocalizedMessage(), throwable);
    }

    public static CoreSync getInstance(Context context, boolean sendNotification) {
        if (INSTANCE == null) {
            INSTANCE = new CoreSync(context, sendNotification);
        }
        return INSTANCE;
    }

    /**
     * Création d'un Observable interval qui va envoyer un élément toutes les 10 secondes.
     * Cet élément sera synchronisé avec un élément de la liste de colis présents dans la DB.
     * Pour ce colis qui sera présent toutes les 10 secondes, on va appeler le service CoreSync.callOptTracking()
     * L'interval de temps nous permet de ne pas saturer le réseau avec des requêtes quand on a trop de colis dans la DB.
     */
    void callGetAllTracking() {
        ColisRepository.getInstance(context).getAllActiveAndNonDeliveredColis()
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .doOnSuccess(listColis ->
                        Observable.zip(
                                Observable.interval(5, SECONDS),
                                Observable.just(listColis).flatMapIterable(colisEntities -> colisEntities),
                                (aLong, colisEntity) -> colisEntity)
                                .doOnNext(this::callOptTracking)
                                .doOnError(consThrowable)
                                .subscribe()
                )
                .subscribe();
    }

    /**
     * Call the OPT tracking API
     * For the moment we just call a URL and parse a HTML page.
     * With the new IPS API soon, we will be able to call a REST API
     *
     * @param colisEntity
     */
    void callOptTracking(ColisEntity colisEntity) {
        if (context != null) {
            String trackingNumber = colisEntity.getIdColis();
            RetrofitClient.getTrackingOpt(trackingNumber)
                    .doOnNext(htmlString -> {
                        ColisWithSteps colisWithSteps = new ColisWithSteps();
                        colisWithSteps.colisEntity = colisEntity;
                        ColisDto colisDto = transformHtmlToColisDto(trackingNumber, colisEntity.getDescription(), htmlString);
                        if (colisDto != null) {
                            Log.d(TAG, "Transformation de la réponse OPT OK");
                            colisWithSteps = ColisMapper.convertToEntity(colisDto, colisWithSteps);
                            saveSuccessfulColis(colisWithSteps);
                        } else {
                            Log.e(TAG, "Fail to receive response from OPT service");
                        }
                    })
                    .doOnError(consThrowable)
                    .subscribe();
        }
    }

    /**
     * @param trackingNumber
     * @param description
     * @param htmlToTransform
     * @return
     */
    private ColisDto transformHtmlToColisDto(String trackingNumber, String description, String htmlToTransform) {
        try {
            ColisDto colisDto = new ColisDto();
            colisDto.setIdColis(trackingNumber);
            colisDto.setDescription(description);
            switch (HtmlTransformer.getColisFromHtml(htmlToTransform, colisDto)) {
                case HtmlTransformer.RESULT_SUCCESS:
                    Log.d(TAG, "HtmlTransformer return SUCCESS");
                    return colisDto;
                case HtmlTransformer.RESULT_NO_ITEM_FOUND:
                    Log.w(TAG, "HtmlTransformer return NO ITEM FOUND");
                    return null;
                default:
                    return null;
            }
        } catch (HtmlTransformer.HtmlTransformerException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    /**
     * Delete tracking from the AfterShip account.
     *
     * @param colis
     */
    void deleteColis(ColisEntity colis) {
        if (colis.getIdColis() != null && colis.getIdColis().length() != 0) {
            ColisRepository.getInstance(context).delete(colis.getIdColis());
        } else {
            Log.e(TAG, "Can't markAsDeleted without tracking number");
        }
    }

    /**
     * Find the record in DB.
     * If findBy :
     * - Update only the steps.
     * If not :
     * - Insert the new colis
     * - Insert the new steps
     * Anyway update the last successful update
     *
     * @param resultColis
     */
    private void saveSuccessfulColis(ColisWithSteps resultColis) {
        Log.d(TAG, "ColisWithSteps qui va être enregistré : " + resultColis.toString());
        ColisWithStepsRepository.getInstance(context).findActiveColisWithStepsByIdColis(resultColis.colisEntity.getIdColis())
                .doOnSuccess(colisWithSteps -> {
                    if (colisWithSteps != null) {
                        if (resultColis.stepEntityList.size() > colisWithSteps.stepEntityList.size() && sendNotification) {
                            NotificationSender.sendNotification(context, context.getString(R.string.app_name), resultColis.colisEntity.getIdColis() + " a été mis à jour.", R.drawable.ic_archive_white_48dp);
                        }
                    } else {
                        ColisRepository.getInstance(context).save(resultColis.colisEntity);
                    }
                    StepRepository.getInstance(context).save(resultColis.stepEntityList);
                    ColisRepository.getInstance(context).updateLastSuccessfulUpdate(resultColis.colisEntity);
                })
                .doOnError(consThrowable)
                .subscribe();
    }
}
