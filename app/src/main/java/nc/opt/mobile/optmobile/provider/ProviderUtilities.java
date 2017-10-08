package nc.opt.mobile.optmobile.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;

import org.chalup.microorm.MicroOrm;

import java.util.ArrayList;
import java.util.List;

import nc.opt.mobile.optmobile.domain.Agency;
import nc.opt.mobile.optmobile.domain.Colis;
import nc.opt.mobile.optmobile.domain.EtapeAcheminement;
import nc.opt.mobile.optmobile.domain.Feature;
import nc.opt.mobile.optmobile.domain.FeatureCollection;
import nc.opt.mobile.optmobile.utils.Utilities;

/**
 * Created by orlanth23 on 12/08/2017.
 */

public class ProviderUtilities {

    private static final MicroOrm uOrm = new MicroOrm();

    private ProviderUtilities() {
    }

    public static boolean deleteColis(Context context, String idColis) {
        // Suppression des étapes d'acheminement
        context.getContentResolver().delete(OptProvider.ListEtapeAcheminement.LIST_ETAPE, EtapeAcheminementInterface.ID_COLIS.concat("=?"), new String[]{idColis});

        // Suppression du colis
        int result = context.getContentResolver().delete(OptProvider.ListColis.LIST_COLIS, ColisInterface.ID_COLIS.concat("=?"), new String[]{idColis});

        return result == 1;
    }

    public static ContentValues putColisToContentValues(Colis colis) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColisInterface.ID_COLIS, colis.getIdColis());
        return contentValues;
    }

    public static ContentValues putEtapeToContentValues(EtapeAcheminement etapeAcheminement, String idColis) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EtapeAcheminementInterface.PAYS, etapeAcheminement.getPays());
        contentValues.put(EtapeAcheminementInterface.LOCALISATION, etapeAcheminement.getLocalisation());
        contentValues.put(EtapeAcheminementInterface.COMMENTAIRE, etapeAcheminement.getCommentaire());
        contentValues.put(EtapeAcheminementInterface.DESCRIPTION, etapeAcheminement.getDescription());
        contentValues.put(EtapeAcheminementInterface.DATE, etapeAcheminement.getDate());
        contentValues.put(EtapeAcheminementInterface.ID_COLIS, idColis);
        return contentValues;
    }

    private static Agency getAgencyFromCursor(Cursor cursor) {
        return uOrm.fromCursor(cursor, Agency.class);
    }

    private static Colis getColisFromCursor(Cursor cursor) {
        return uOrm.fromCursor(cursor, Colis.class);
    }

    private static EtapeAcheminement getEtapeFromCursor(Cursor cursor) {
        return uOrm.fromCursor(cursor, EtapeAcheminement.class);
    }

    public static void populateContentProviderFromAsset(Context context) {
        String json = Utilities.loadStringFromAsset(context, "opt_agencies.json");
        Gson gson = new Gson();

        // Okay we 've got the list in a json let's just getParcelResultFromHtml to an arrayList and send it to the content provider.
        FeatureCollection featureCollection = gson.fromJson(json, FeatureCollection.class);
        ProviderUtilities.populateContentProviderFromFeatureCollection(context, featureCollection);
    }

    private static void populateContentProviderFromFeatureCollection(Context context, FeatureCollection featureCollection) {

        // Delete all the content
        context.getContentResolver().delete(OptProvider.ListAgency.LIST_AGENCY, null, null);

        ContentValues contentValues = new ContentValues();
        Agency agency;
        for (Feature feature : featureCollection.getFeatures()) {
            contentValues.clear();
            agency = feature.getProperties();
            agency.setTYPE_GEOMETRY(feature.getType());
            // Pour le moment seul les points sont gérés.
            if (feature.getGeometry().getType().equals("Point")) {
                agency.setLONGITUDE(feature.getGeometry().getCoordinates()[0]);
                agency.setLATITUDE(feature.getGeometry().getCoordinates()[1]);
            }
            context.getContentResolver().insert(OptProvider.ListAgency.LIST_AGENCY, uOrm.toContentValues(agency));
        }
    }

    /**
     * Query the ContentProvider from the context to get the agency list
     *
     * @param context
     * @return List of recipe
     */
    public static List<Agency> getListAgencyFromContentProvider(Context context) {
        ArrayList<Agency> agencyList = null;

        // Query the content provider to get a cursor of Agency
        Cursor cursorListAgency = context.getContentResolver().query(OptProvider.ListAgency.LIST_AGENCY, null, null, null, null);

        if (cursorListAgency != null) {
            agencyList = new ArrayList<>();
            while (cursorListAgency.moveToNext()) {
                Agency agency = ProviderUtilities.getAgencyFromCursor(cursorListAgency);
                agencyList.add(agency);
            }
            cursorListAgency.close();
        }
        return agencyList;
    }

    public static List<Colis> getListColisFromContentProvider(Context context) {
        List<Colis> colisList = null;

        // Query the content provider to get a cursor of Colis
        Cursor cursorListColis = context.getContentResolver().query(OptProvider.ListColis.LIST_COLIS, null, null, null, null);

        if (cursorListColis != null) {
            colisList = new ArrayList<>();
            while (cursorListColis.moveToNext()) {
                Colis colis = ProviderUtilities.getColisFromCursor(cursorListColis);
                List<EtapeAcheminement> listEtape = ProviderUtilities.getListEtapeFromContentProvider(context, colis.getIdColis());
                colis.setEtapeAcheminementArrayList(listEtape);
                colisList.add(colis);
            }
            cursorListColis.close();
        }
        return colisList;
    }

    public static List<EtapeAcheminement> getListEtapeFromContentProvider(Context context, String idColis) {
        List<EtapeAcheminement> etapeList = null;

        // Query the content provider to get a cursor of Etape
        Cursor cursorListEtape = context.getContentResolver().query(OptProvider.ListEtapeAcheminement.withIdColis(idColis), null, null, null, null);

        if (cursorListEtape != null) {
            etapeList = new ArrayList<>();
            while (cursorListEtape.moveToNext()) {
                EtapeAcheminement etape = ProviderUtilities.getEtapeFromCursor(cursorListEtape);
                etapeList.add(etape);
            }
            cursorListEtape.close();
        }
        return etapeList;
    }
}
