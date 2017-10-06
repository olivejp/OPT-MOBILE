package nc.opt.mobile.optmobile;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

import java.util.Map;
import java.util.Set;

import nc.opt.mobile.optmobile.provider.ColisInterface;
import nc.opt.mobile.optmobile.provider.EtapeAcheminementInterface;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

class TestUtilities {

    private static final String ETAPE_DATE = "31/12/2017";
    private static final String ETAPE_PAYS = "NOUVELLE-CALEDONIE";
    private static final String ETAPE_COMMENTAIRE = "SIGNED BY OLIVE Edna";
    private static final String ETAPE_DESCRIPTION = "This parcel is in transit to NC";
    private static final String ETAPE_LOCALISATION = "NOUMEA CTC";
    private static final String ETAPE_ID = "1";

    private static final String RECIPE_IMAGE = "recipeImage";
    private static final String RECIPE_NAME = "recipeName";
    private static final String RECIPE_SERVINGS = "recipeServings";

    static boolean isTablet(AppCompatActivity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        return (width >= 1800 || height >= 1800);
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    private static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createEtapeValues(String idColis, @Nullable Integer idEtape) {
        ContentValues testValues = new ContentValues();
        if (idEtape != null) {
            testValues.put(EtapeAcheminementInterface.ID_ETAPE_ACHEMINEMENT, idEtape);
        }
        testValues.put(EtapeAcheminementInterface.DATE, ETAPE_DATE);
        testValues.put(EtapeAcheminementInterface.PAYS, ETAPE_PAYS);
        testValues.put(EtapeAcheminementInterface.COMMENTAIRE, ETAPE_COMMENTAIRE);
        testValues.put(EtapeAcheminementInterface.DESCRIPTION, ETAPE_DESCRIPTION);
        testValues.put(EtapeAcheminementInterface.LOCALISATION, ETAPE_LOCALISATION);
        testValues.put(EtapeAcheminementInterface.ID_COLIS, idColis);
        return testValues;
    }

    static ContentValues createColisValues(String idColis) {
        ContentValues testValues = new ContentValues();
        testValues.put(ColisInterface.ID_COLIS, idColis);
        return testValues;
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(120000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }
}

