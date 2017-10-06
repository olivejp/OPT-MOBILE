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

import com.orlanth23.bakingapp.provider.IngredientInterface;
import com.orlanth23.bakingapp.provider.RecipeInterface;
import com.orlanth23.bakingapp.provider.StepInterface;

import java.util.Map;
import java.util.Set;

import nc.opt.mobile.optmobile.provider.ColisInterface;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

class TestUtilities {

    private static final String STEP_DESRIPTION = "description";
    private static final String STEP_SHORT_DESRIPTION = "shortDescription";
    private static final String STEP_VIDEO_URL = "videoUrl";
    private static final String STEP_THUMBNAIL_URL = "thumbnailUrl";


    private static final String RECIPE_IMAGE = "recipeImage";
    private static final String RECIPE_NAME = "recipeName";
    private static final String RECIPE_SERVINGS = "recipeServings";

    private static final String INGREDIENT_INGREDIENT = "ingredient";
    private static final String INGREDIENT_MEASURE = "measure";
    private static final double INGREDIENT_QUANTITY = 2.5;

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
            testValues.put(StepInterface._ID, idEtape);
        }
        testValues.put(StepInterface.description, STEP_DESRIPTION);
        testValues.put(StepInterface.shortDescription, STEP_SHORT_DESRIPTION);
        testValues.put(StepInterface.videoURL, STEP_VIDEO_URL);
        testValues.put(StepInterface.thumbnailURL, STEP_THUMBNAIL_URL);
        testValues.put(StepInterface.RECIPE_ID, idColis);
        return testValues;
    }

    static ContentValues createIngredientValues(Integer idRecipe, @Nullable Integer idIngredient) {
        ContentValues testValues = new ContentValues();
        if (idIngredient != null) {
            testValues.put(IngredientInterface._ID, idIngredient);
        }
        testValues.put(IngredientInterface.ingredient, INGREDIENT_INGREDIENT);
        testValues.put(IngredientInterface.measure, INGREDIENT_MEASURE);
        testValues.put(IngredientInterface.quantity, INGREDIENT_QUANTITY);
        testValues.put(IngredientInterface.RECIPE_ID, idRecipe);
        return testValues;
    }

    static ContentValues createRecipeValuesNullId() {
        ContentValues testValues = new ContentValues();
        testValues.put(RecipeInterface.image, RECIPE_IMAGE);
        testValues.put(RecipeInterface.name, RECIPE_NAME);
        testValues.put(RecipeInterface.servings, RECIPE_SERVINGS);
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

