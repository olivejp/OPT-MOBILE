package nc.opt.mobile.optmobile.Utils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by orlanth23 on 13/08/2017.
 */

public class Utilities {

    private static final String TAG = Utilities.class.getName();

    private Utilities() {
    }

    public static String loadStringFromAsset(Context context, String fileName) {
        String json;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            return null;
        }
        return json;
    }
}
