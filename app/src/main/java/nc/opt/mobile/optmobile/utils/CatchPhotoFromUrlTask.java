package nc.opt.mobile.optmobile.utils;

/**
 * Created by 2761oli on 05/01/2018.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

/**
 * Static class to retrieve photo from an url.
 */
public class CatchPhotoFromUrlTask extends AsyncTask<Uri, Void, Drawable> {

    private static final String TAG = CatchPhotoFromUrlTask.class.getName();

    private WeakReference<Context> activityReference;
    private PhotoFromUrlListener listener;

    // only retain a weak reference to the activity
    public CatchPhotoFromUrlTask(Context context, PhotoFromUrlListener listener) {
        this.activityReference = new WeakReference<>(context);
        this.listener = listener;
    }

    @Override
    protected Drawable doInBackground(Uri... uris) {
        Context activity = activityReference.get();
        if (activity == null) return null;
        if (uris.length < 1) return null;
        try {
            return Glide.with(activity)
                    .asDrawable()
                    .load(uris[0])
                    .apply(RequestOptions.circleCropTransform())
                    .submit()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        if (listener == null) return;
        if (drawable != null) {
            listener.catchPhotoFromUrl(drawable);
        }
    }

    public interface PhotoFromUrlListener {
        void catchPhotoFromUrl(Drawable drawable);
    }
}
