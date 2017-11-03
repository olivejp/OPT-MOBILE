package nc.opt.mobile.optmobile.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.domain.suiviColis.Colis;
import nc.opt.mobile.optmobile.utils.Constants;
import nc.opt.mobile.optmobile.utils.HtmlTransformer;
import nc.opt.mobile.optmobile.utils.NotificationSender;

import static nc.opt.mobile.optmobile.provider.services.ColisService.updateLastUpdate;
import static nc.opt.mobile.optmobile.provider.services.EtapeAcheminementService.checkAndInsert;

/**
 * Created by 2761oli on 09/10/2017.
 */

class TransformHtmlTask extends AsyncTask<String, Void, Colis> {

    private static final String TAG = TransformHtmlTask.class.getName();

    private String idColis;
    private Context context;
    private boolean sendNotification;

    TransformHtmlTask(Context context, String idColis, boolean sendNotification) {
        this.context = context;
        this.sendNotification = sendNotification;
        this.idColis = idColis;
    }

    private Colis transformHtmlToObject(String idColis, String htmlToTransform) {
        Colis colis = new Colis();
        colis.setIdColis(idColis);
        try {
            int transformResult = HtmlTransformer.getParcelResultFromHtml(htmlToTransform, colis);
            switch (transformResult) {
                case HtmlTransformer.RESULT_SUCCESS:
                    updateLastUpdate(context, colis.getIdColis(), true);
                    if (checkAndInsert(context, colis.getIdColis(), colis.getEtapeAcheminementDtoArrayList()) && sendNotification) {
                        // Envoi d'une notification si l'objet a bougé.
                        NotificationSender.sendNotification(context, context.getString(R.string.app_name), idColis + " a été mis à jour.", R.drawable.ic_archive_white_48dp);
                    }
                    return colis;
                case HtmlTransformer.RESULT_NO_ITEM_FOUND:
                    updateLastUpdate(context, colis.getIdColis(), false);
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
    protected Colis doInBackground(String... params) {
        try {
            String responseEncoded = URLDecoder.decode(URLEncoder.encode(params[0], Constants.ENCODING_ISO), Constants.ENCODING_UTF_8);
            return transformHtmlToObject(idColis, responseEncoded);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }
}
