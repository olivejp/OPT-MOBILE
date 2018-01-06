package nc.opt.mobile.optmobile.provider;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 2761oli on 11/10/2017.
 */
public class ProviderObserver extends ContentObserver {

    private static ProviderObserver mInstance;

    private static HashMap<Uri, List<ProviderObserverListener>> mapUriListeners = new HashMap<>();

    private ProviderObserver(Handler handler) {
        super(handler);
    }

    public static synchronized ProviderObserver getInstance() {
        if (mInstance == null) {
            mInstance = new ProviderObserver(new Handler());
        }
        return mInstance;
    }

    /**
     * @param context
     * @param providerObserverListener
     * @param uris
     */
    public void observe(Context context, ProviderObserverListener providerObserverListener, Uri... uris) {
        for (Uri uri : uris) {
            // Recherche si l'uri est présente dans le HashMap
            if (mapUriListeners.containsKey(uri)) {
                List<ProviderObserverListener> listeners = mapUriListeners.get(uri);
                if (!listeners.contains(providerObserverListener)) {
                    listeners.add(providerObserverListener);
                }
            } else {
                List<ProviderObserverListener> list = new ArrayList<>();
                list.add(providerObserverListener);
                mapUriListeners.put(uri, list);
                context.getContentResolver().registerContentObserver(uri, false, this);
            }
        }
    }

    public void unregister(ProviderObserverListener providerObserverListener, Uri... uris) {
        for (Uri uri : uris) {
            if (mapUriListeners.containsKey(uri)) {
                List<ProviderObserverListener> listeners = mapUriListeners.get(uri);
                for (ProviderObserverListener listener :listeners) {
                    if (listener == providerObserverListener) {
                        listeners.remove(listener);
                    }
                }
            }
        }
    }

    @Override
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        if (mapUriListeners.containsKey(uri)) {
            List<ProviderObserverListener> list = mapUriListeners.get(uri);
            for (ProviderObserverListener providerObserverListener : list) {
                providerObserverListener.onProviderChange(uri);
            }
        }
    }

    public interface ProviderObserverListener {
        void onProviderChange(Uri uri);
    }
}