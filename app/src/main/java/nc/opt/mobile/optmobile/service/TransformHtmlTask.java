package nc.opt.mobile.optmobile.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.domain.suiviColis.ColisDto;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.services.ColisService;
import nc.opt.mobile.optmobile.provider.services.EtapeAcheminementService;
import nc.opt.mobile.optmobile.utils.Constants;
import nc.opt.mobile.optmobile.utils.HtmlTransformer;
import nc.opt.mobile.optmobile.utils.NotificationSender;

/**
 * Created by 2761oli on 09/10/2017.
 */

class TransformHtmlTask extends AsyncTask<String, Void, ColisDto> {

    private static final String TAG = TransformHtmlTask.class.getName();

    private String idColis;
    private Context context;
    private boolean sendNotification;

    TransformHtmlTask(Context context, String idColis, boolean sendNotification) {
        this.context = context.getApplicationContext();
        this.sendNotification = sendNotification;
        this.idColis = idColis;
    }

    private ColisDto transformHtmlToObject(String idColis, String htmlToTransform) {
        ColisDto colisDto = new ColisDto();
        colisDto.setIdColis(idColis);
        try {
            switch (HtmlTransformer.getColisFromHtml(htmlToTransform, colisDto)) {
                case HtmlTransformer.RESULT_SUCCESS:
                    ColisService.updateLastUpdate(context, colisDto.getIdColis(), true);
                    ColisEntity colisEntity = ColisService.convertToEntity(colisDto);
                    if (EtapeAcheminementService.save(context, colisEntity)) {
                        if (sendNotification) {
                            // Envoi d'une notification si l'objet a bougé.
                            NotificationSender.sendNotification(context, context.getString(R.string.app_name), idColis + " a été mis à jour.", R.drawable.ic_archive_white_48dp);
                        }
                    } else {
                        Log.e(TAG, "Echec de la sauvegarde du colis dans le content provider.");
                    }
                    return colisDto;
                case HtmlTransformer.RESULT_NO_ITEM_FOUND:
                    ColisService.updateLastUpdate(context, colisDto.getIdColis(), false);
                    return null;
                default:
                    break;
            }
        } catch (HtmlTransformer.HtmlTransformerException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected ColisDto doInBackground(String... params) {
        try {
            String responseEncoded = URLDecoder.decode(URLEncoder.encode(params[0], Constants.ENCODING_ISO), Constants.ENCODING_UTF_8);
            return transformHtmlToObject(idColis, responseEncoded);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }
}
