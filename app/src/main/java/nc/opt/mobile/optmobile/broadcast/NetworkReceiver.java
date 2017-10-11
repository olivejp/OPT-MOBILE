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

    private NetworkReceiver() {
        super();
    }

    public static synchronized NetworkReceiver getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkReceiver();
        }
        return mInstance;
    }

    public int listen(NetworkChangeListener networkChangeListener) {
        if (mNetworkChangeListener.contains(networkChangeListener)) {
            return mNetworkChangeListener.indexOf(networkChangeListener);
        } else {
            if (mNetworkChangeListener.add(networkChangeListener)) {
                return mNetworkChangeListener.indexOf(networkChangeListener);
            } else {
                return -1;
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        notifyListener(context);
    }

    public static boolean notifyListener(Context context) {
        ConnectivityManager conn = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            for (NetworkChangeListener networkChangeListener :
                    mNetworkChangeListener) {
                networkChangeListener.OnNetworkEnable();
            }
            return true;
        } else if (networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected()) {
            for (NetworkChangeListener networkChangeListener :
                    mNetworkChangeListener) {
                networkChangeListener.OnNetworkDisable();
            }
            return false;
        }
        return false;
    }

    public static boolean checkConnection(Context context) {
        ConnectivityManager conn = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            return true;
        } else if (networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected()) {
            return false;
        }
        return false;
    }

    public interface NetworkChangeListener {
        void OnNetworkEnable();

        void OnNetworkDisable();
    }
}
