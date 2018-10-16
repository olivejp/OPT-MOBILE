package nc.opt.mobile.optmobile.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orlanth23 on 09/10/2017.
 */
public class NetworkReceiver extends BroadcastReceiver {

    public static final IntentFilter CONNECTIVITY_CHANGE_INTENT_FILTER = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    private static NetworkReceiver mInstance;
    private static List<NetworkChangeListener> mNetworkChangeListener = new ArrayList<>();

    /**
     * Constructor
     */
    private NetworkReceiver() {
        super();
    }

    /**
     * NetworkReceiver is a singleton, so getInstance return THE instance.
     *
     * @return
     */
    public static synchronized NetworkReceiver getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkReceiver();
        }
        return mInstance;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        notifyListener(context);
    }

    /**
     * Add a new NetworkChangeListener to the List<NetworkChangeListener>
     * Don't forget to unregister to avoid memory leak
     *
     * @param networkChangeListener
     */
    public void register(NetworkChangeListener networkChangeListener) {
        if (!mNetworkChangeListener.contains(networkChangeListener)) {
            mNetworkChangeListener.add(networkChangeListener);
        }
    }


    /**
     * Delete the NetworkChangeListener from the inner list.
     * This is mandatory to avoid memory leak
     *
     * @param listener
     */
    public void unregister(NetworkChangeListener listener) {
        if (mNetworkChangeListener.contains(listener)) {
            mNetworkChangeListener.remove(listener);
        }
    }

    /**
     * Inner method which notify all the NetworkChangeListener in the inner list
     *
     * @param context
     */
    private static void notifyListener(Context context) {
        ConnectivityManager conn = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn != null ? conn.getActiveNetworkInfo() : null;

        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            for (NetworkChangeListener networkChangeListener :
                    mNetworkChangeListener) {
                networkChangeListener.onNetworkEnable();
            }
        } else if (networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected()) {
            for (NetworkChangeListener networkChangeListener :
                    mNetworkChangeListener) {
                networkChangeListener.onNetworkDisable();
            }
        }
    }

    /**
     * Static method to know if we get a connection.
     *
     * @param context
     * @return true if connection findBy, false otherwise.
     */
    public static boolean checkConnection(Context context) {
        ConnectivityManager conn = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = conn != null ? conn.getActiveNetworkInfo() : null;

        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            return true;
        } else if (networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected()) {
            return false;
        }
        return false;
    }

    /**
     * Interface Listener need to implement before register to the NetworkReceiver
     */
    public interface NetworkChangeListener {
        void onNetworkEnable();

        void onNetworkDisable();
    }
}
