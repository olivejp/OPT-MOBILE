package nc.opt.mobile.optmobile.provider.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;

import org.chalup.microorm.MicroOrm;

import java.util.ArrayList;
import java.util.List;

import nc.opt.mobile.optmobile.domain.Agency;
import nc.opt.mobile.optmobile.domain.Feature;
import nc.opt.mobile.optmobile.domain.FeatureCollection;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.utils.Utilities;

/**
 * Created by 2761oli on 23/10/2017.
 */

public class AgencyService {

    private static final MicroOrm uOrm = new MicroOrm();

    public static void populateContentProviderFromAsset(Context context) {
        String json = Utilities.loadStringFromAsset(context, "opt_agencies.json");
        Gson gson = new Gson();

        // Okay we 've got the list in a json let's just getParcelResultFromHtml to an arrayList and send it to the content provider.
        FeatureCollection featureCollection = gson.fromJson(json, FeatureCollection.class);
        populateProviderFromCollection(context, featureCollection);
    }

    private static Agency getFromCursor(Cursor cursor) {
        return uOrm.fromCursor(cursor, Agency.class);
    }

    /**
     * Query the ContentProvider from the context to get the agency list
     *
     * @param context
     * @return List of recipe
     */
    public static List<Agency> listFromProvider(Context context) {
        ArrayList<Agency> agencyList = new ArrayList<>();

        // Query the content provider to get a cursor of Agency
        Cursor cursorListAgency = context.getContentResolver().query(OptProvider.ListAgency.LIST_AGENCY, null, null, null, null);

        if (cursorListAgency != null) {
            while (cursorListAgency.moveToNext()) {
                Agency agency = getFromCursor(cursorListAgency);
                agencyList.add(agency);
            }
            cursorListAgency.close();
        }
        return agencyList;
    }

    private static void populateProviderFromCollection(Context context, FeatureCollection featureCollection) {

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

}
