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

    private static HashMap<Uri, List<ProviderObserverListener>> hashMap = new HashMap<>();

    private ProviderObserver(Handler handler) {
        super(handler);
    }

    public static synchronized ProviderObserver getInstance() {
        if (mInstance == null) {
            mInstance = new ProviderObserver(new Handler());
        }
        return mInstance;
    }

    public void observe(Context context, ProviderObserverListener providerObserverListener, Uri ... uris) {
        for (Uri uri : uris) {
            // Recherche si l'uri est présente dans le HashMap
            if (hashMap.containsKey(uri)) {
                List<ProviderObserverListener> listeners = hashMap.get(uri);
                if (!listeners.contains(providerObserverListener)) {
                    listeners.add(providerObserverListener);
                }
            } else {
                List<ProviderObserverListener> list = new ArrayList<>();
                list.add(providerObserverListener);
                hashMap.put(uri, list);
                context.getContentResolver().registerContentObserver(uri, false, this);
            }
        }
    }

    @Override
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        if (hashMap.containsKey(uri)) {
            List<ProviderObserverListener> list = hashMap.get(uri);
            for (ProviderObserverListener providerObserverListener : list) {
                providerObserverListener.onProviderChange();
            }
        }
    }

    public interface ProviderObserverListener {
        void onProviderChange();
    }
}