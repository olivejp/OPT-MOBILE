package nc.opt.mobile.optmobile.provider.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;

import org.chalup.microorm.MicroOrm;

import java.util.ArrayList;
import java.util.List;

import nc.opt.mobile.optmobile.domain.localisation.Agence;
import nc.opt.mobile.optmobile.domain.localisation.Feature;
import nc.opt.mobile.optmobile.domain.localisation.FeatureCollection;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.utils.Utilities;

/**
 * Created by 2761oli on 23/10/2017.
 */

public class AgenceService {

    private static final MicroOrm uOrm = new MicroOrm();

    public static void populateContentProviderFromAsset(Context context) {
        String json = Utilities.loadStringFromAsset(context, "opt_agencies.json");
        Gson gson = new Gson();

        // Okay we 've got the list in a json let's just getColisFromHtml to an arrayList and send it to the content provider.
        FeatureCollection featureCollection = gson.fromJson(json, FeatureCollection.class);
        populateProviderFromCollection(context, featureCollection);
    }

    private static Agence getFromCursor(Cursor cursor) {
        return uOrm.fromCursor(cursor, Agence.class);
    }

    /**
     * Query the ContentProvider from the context to get the agency list
     *
     * @param context
     * @return List of recipe
     */
    public static List<Agence> listFromProvider(Context context) {
        ArrayList<Agence> agenceList = new ArrayList<>();

        // Query the content provider to get a cursor of Agence
        Cursor cursorListAgency = context.getContentResolver().query(OptProvider.ListAgency.LIST_AGENCY, null, null, null, null);

        if (cursorListAgency != null) {
            while (cursorListAgency.moveToNext()) {
                Agence agence = getFromCursor(cursorListAgency);
                agenceList.add(agence);
            }
            cursorListAgency.close();
        }
        return agenceList;
    }

    private static void populateProviderFromCollection(Context context, FeatureCollection featureCollection) {

        // Delete all the content
        context.getContentResolver().delete(OptProvider.ListAgency.LIST_AGENCY, null, null);

        ContentValues contentValues = new ContentValues();
        Agence agence;
        for (Feature feature : featureCollection.getFeatures()) {
            contentValues.clear();
            agence = feature.getProperties();
            agence.setTYPE_GEOMETRY(feature.getType());
            // Pour le moment seul les points sont gérés.
            if (feature.getGeometry().getType().equals("Point")) {
                agence.setLONGITUDE(feature.getGeometry().getCoordinates()[0]);
                agence.setLATITUDE(feature.getGeometry().getCoordinates()[1]);
            }
            context.getContentResolver().insert(OptProvider.ListAgency.LIST_AGENCY, uOrm.toContentValues(agence));
        }
    }

}
