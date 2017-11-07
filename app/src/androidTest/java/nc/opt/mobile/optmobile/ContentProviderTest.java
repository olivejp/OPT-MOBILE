package nc.opt.mobile.optmobile;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import nc.opt.mobile.optmobile.provider.OptProvider;

import static junit.framework.Assert.assertTrue;

/**
 * Created by orlanth23 on 06/10/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ContentProviderTest {

    private Context mContext;

    private ProviderTestUtilities.TestContentObserver tco;


    private void validateUri(Uri uri, ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                uri,
                null, // leaving "columns" null just returns all the columns.
                selection, // cols for "where" clause
                selectionArgs, // values for "where" clause
                null  // sort order
        );

        ProviderTestUtilities.validateCursor("testInsertReadProvider. Error validating URI ".concat(uri.toString()),
                cursor, contentValues);
    }

    private Uri insertContentValues(Uri uri, ContentValues contentValues) {
        /* try to insert */
        return mContext.getContentResolver().insert(uri, contentValues);
    }

    private void testInsertReadUriProvider(Uri uri, ContentValues contentValues) {

        /* Suppression des enregistrements précédents */
        ProviderTestUtilities.deleteRecords(mContext, uri);

        /* add a ContentObserver */
        mContext.getContentResolver().registerContentObserver(uri, true, tco);

        /* try to insert */
        Uri categorieUri = insertContentValues(uri, contentValues);

        /* verify that the notifyChange has been called */
        tco.waitForNotificationOrFail();

        /* unregister the contentObserver */
        mContext.getContentResolver().unregisterContentObserver(tco);

        long rowId = ContentUris.parseId(categorieUri);

        // Verify we got a row back.
        assertTrue(rowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.
        validateUri(uri, contentValues, null, null);
    }

    @Before
    public void precondition() {
        mContext = InstrumentationRegistry.getTargetContext();
        tco = ProviderTestUtilities.getTestContentObserver();
        deleteAllRecordsFromProvider();
    }

    @Test
    public void deleteAllRecordsFromProvider() {
        ProviderTestUtilities.deleteRecords(mContext, OptProvider.ListAgency.LIST_AGENCY);
        ProviderTestUtilities.deleteRecords(mContext, OptProvider.ListColis.LIST_COLIS);
        ProviderTestUtilities.deleteRecords(mContext, OptProvider.ListEtapeAcheminement.LIST_ETAPE);
        ProviderTestUtilities.deleteRecords(mContext, OptProvider.ListActualite.LIST_ACTUALITE);
    }

    @Test
    public void testInsertReadEtapeProvider() {
        ContentValues testValuesStep = ProviderTestUtilities.createEtapeValues(ProviderTestUtilities.COLIS_ID, 1234);
        testInsertReadUriProvider(OptProvider.ListEtapeAcheminement.LIST_ETAPE, testValuesStep);
    }

    @Test
    public void testInsertReadEtapeWithNullIdProvider() {
        ContentValues testValuesEtapeNull = ProviderTestUtilities.createEtapeValues(ProviderTestUtilities.COLIS_ID, null);
        testInsertReadUriProvider(OptProvider.ListEtapeAcheminement.LIST_ETAPE, testValuesEtapeNull);
    }

    @Test
    public void testInsertReadColisProvider() {
        ContentValues testValuesAnnonce = ProviderTestUtilities.createColisValues(ProviderTestUtilities.COLIS_ID);
        testInsertReadUriProvider(OptProvider.ListColis.LIST_COLIS, testValuesAnnonce);
    }

    @Test
    public void testInsertColisInsertEtapesRead() {
        ContentValues testValuesColis = ProviderTestUtilities.createColisValues(ProviderTestUtilities.COLIS_ID);
        Uri uriColis = insertContentValues(OptProvider.ListColis.LIST_COLIS, testValuesColis);
        if (ContentUris.parseId(uriColis) != -1) {
            ContentValues testValuesEtape = ProviderTestUtilities.createEtapeValues(ProviderTestUtilities.COLIS_ID, 1234);
            insertContentValues(OptProvider.ListEtapeAcheminement.LIST_ETAPE, testValuesEtape);
            validateUri(OptProvider.ListEtapeAcheminement.withIdColis(ProviderTestUtilities.COLIS_ID), testValuesEtape, null, null);
        }
    }

    @Test
    public void testInsertReadActualiteProvider() {
        ContentValues testActualite = ProviderTestUtilities.createActualiteValues();
        testInsertReadUriProvider(OptProvider.ListActualite.LIST_ACTUALITE, testActualite);
    }
}
