package nc.opt.mobile.optmobile.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;

import org.chalup.microorm.MicroOrm;

import java.util.ArrayList;

import nc.opt.mobile.optmobile.domain.Agency;
import nc.opt.mobile.optmobile.domain.Feature;
import nc.opt.mobile.optmobile.domain.FeatureCollection;
import nc.opt.mobile.optmobile.utils.Utilities;

/**
 * Created by orlanth23 on 12/08/2017.
 */

public class ProviderUtilities {

    private static final MicroOrm uOrm = new MicroOrm();

    private static Agency getAgencyFromCursor(Cursor cursor) {
        return uOrm.fromCursor(cursor, Agency.class);
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
        context.getContentResolver().delete(AgencyProvider.ListAgency.LIST_AGENCY, null, null);

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
            context.getContentResolver().insert(AgencyProvider.ListAgency.LIST_AGENCY, uOrm.toContentValues(agency));
        }
    }

    /**
     * Query the ContentProvider from the context to get the agency list
     *
     * @param context
     * @return List of recipe
     */
    public static ArrayList<Agency> getListAgencyFromContentProvider(Context context) {
        ArrayList<Agency> recipeList = null;

        // Query the content provider to get a cursor of recipe
        Cursor cursorListAgency = context.getContentResolver().query(AgencyProvider.ListAgency.LIST_AGENCY, null, null, null, null);

        if (cursorListAgency != null) {
            recipeList = new ArrayList<>();
            while (cursorListAgency.moveToNext()) {
                Agency agency = ProviderUtilities.getAgencyFromCursor(cursorListAgency);
                recipeList.add(agency);
            }
            cursorListAgency.close();
        }
        return recipeList;
    }
}
