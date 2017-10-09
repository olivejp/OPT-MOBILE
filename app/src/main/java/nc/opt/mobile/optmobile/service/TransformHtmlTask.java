package nc.opt.mobile.optmobile.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import nc.opt.mobile.optmobile.domain.Colis;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;
import nc.opt.mobile.optmobile.utils.Constants;
import nc.opt.mobile.optmobile.utils.HtmlTransformer;

/**
 * Created by 2761oli on 09/10/2017.
 */

class TransformHtmlTask extends AsyncTask<String, Void, Colis> {

    private static final String TAG = TransformHtmlTask.class.getName();

    private String idColis;
    private Context context;

    TransformHtmlTask(Context context, String idColis) {
        this.context = context;
        this.idColis = idColis;
    }

    private Colis transformHtmlToObject(String idColis, String htmlToTransform) {
        Colis colis = new Colis();
        colis.setIdColis(idColis);
        try {
            int transformResult = HtmlTransformer.getParcelResultFromHtml(htmlToTransform, colis);
            switch (transformResult) {
                case HtmlTransformer.RESULT_SUCCESS:
                    ProviderUtilities.updateLastUpdate(context, colis.getIdColis(), true);
                    ProviderUtilities.checkAndInsertEtape(context, colis.getIdColis(), colis.getEtapeAcheminementArrayList());
                    return colis;
                case HtmlTransformer.RESULT_NO_ITEM_FOUND:
                    ProviderUtilities.updateLastUpdate(context, colis.getIdColis(), false);
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
