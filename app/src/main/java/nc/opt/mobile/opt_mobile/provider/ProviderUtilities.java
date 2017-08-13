package nc.opt.mobile.opt_mobile.provider;

import android.content.ContentValues;
import android.content.Context;

import nc.opt.mobile.opt_mobile.domain.Agency;
import nc.opt.mobile.opt_mobile.domain.Feature;
import nc.opt.mobile.opt_mobile.domain.FeatureCollection;

/**
 * Created by orlanth23 on 12/08/2017.
 */

public class ProviderUtilities {
    public static void populateContentProviderFromList(Context context, FeatureCollection featureCollection) {

        // Delete all the content
        context.getContentResolver().delete(AgencyProvider.ListAgency.LIST_AGENCY, null, null);

        ContentValues contentValues = new ContentValues();
        Agency agency;
        for (Feature feature : featureCollection.getFeatures()) {
            contentValues.clear();
            agency = feature.getProperties();
            contentValues.put(AgencyInterface.OBJECTID, agency.getOBJECTID());
            contentValues.put(AgencyInterface.TEXTE, agency.getTEXTE());
            contentValues.put(AgencyInterface.TYPE, agency.getTYPE());
            contentValues.put(AgencyInterface.NOM, agency.getNOM());
            contentValues.put(AgencyInterface.ADRESSE, agency.getADRESSE());
            contentValues.put(AgencyInterface.CODE_POSTAL, agency.getCODE_POSTAL());
            contentValues.put(AgencyInterface.VILLE, agency.getVILLE());
            contentValues.put(AgencyInterface.TEL, agency.getTEL());
            contentValues.put(AgencyInterface.FAX, agency.getFAX());
            contentValues.put(AgencyInterface.HORAIRE, agency.getHORAIRE());
            contentValues.put(AgencyInterface.DAB_INTERNE, agency.getDAB_INTERNE());
            contentValues.put(AgencyInterface.DAB_EXTERNE, agency.getDAB_EXTERNE());
            contentValues.put(AgencyInterface.CONSEILLER_FINANCIER, agency.getCONSEILLER_FINANCIER());
            contentValues.put(AgencyInterface.CONSEILLER_TELECOM, agency.getCONSEILLER_TELECOM());
            contentValues.put(AgencyInterface.CONSEILLER_POLYVALENT, agency.getCONSEILLER_POLYVALENT());
            contentValues.put(AgencyInterface.CONSEILLER_POSTAL, agency.getCONSEILLER_POSTAL());
            contentValues.put(AgencyInterface.LATITUDE, agency.getLATITUDE());
            contentValues.put(AgencyInterface.LONGITUDE, agency.getLONGITUDE());
            contentValues.put(AgencyInterface.GLOBALID, agency.getGLOBALID());
            context.getContentResolver().insert(AgencyProvider.ListAgency.LIST_AGENCY, contentValues);
        }
    }
}
