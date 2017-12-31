package nc.opt.mobile.optmobile.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by orlanth23 on 13/08/2017.
 */

public class Utilities {

    private static final String TAG = Utilities.class.getName();

    private Utilities() {
    }

    public static String loadStringFromAsset(Context context, String fileName) throws IOException {
        String json;
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            return null;
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return json;
    }

    /**
     * @param fragmentManager Get from the context
     * @param message         The message to be send
     * @param type            From NoticeDialogFragment
     * @param img             From NoticeDialogFragment
     * @param tag             A text to be a tag
     */
    public static void sendDialogByFragmentManager(FragmentManager fragmentManager, String message, int type, int img, @Nullable String tag, @Nullable Bundle bundlePar) {
        NoticeDialogFragment dialogErreur = new NoticeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NoticeDialogFragment.P_MESSAGE, message);
        bundle.putInt(NoticeDialogFragment.P_TYPE, type);
        bundle.putInt(NoticeDialogFragment.P_IMG, img);
        bundle.putBundle(NoticeDialogFragment.P_BUNDLE, bundlePar);
        dialogErreur.setArguments(bundle);
        dialogErreur.show(fragmentManager, tag);
    }

    /**
     * Affiche un box avec un message  à l'intérieur. Le message peut contenir un objet dans son bundle qui sera reçu .
     * @param activity
     * @param message
     * @param type
     * @param img
     * @param tag
     * @param bundle
     */
    public static void sendDialogByActivity(AppCompatActivity activity, String message, int type, int img, String tag, @Nullable Bundle bundle) {
        sendDialogByFragmentManager(activity.getSupportFragmentManager(), message, type, img, tag, bundle);
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void hideKeyboard(Context context, View view) {
        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
