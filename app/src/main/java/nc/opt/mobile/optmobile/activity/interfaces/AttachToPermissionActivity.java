package nc.opt.mobile.optmobile.activity.interfaces;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by orlanth23 on 03/10/2017.
 */

@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class AttachToPermissionActivity extends AppCompatActivity {

    private static ArrayList<ListenerPermissionResult> mListenerPermissionResult = new ArrayList<>();

    public void onAttachPermissionActivity(ListenerPermissionResult listenerPermissionResult) {
        mListenerPermissionResult.add(listenerPermissionResult);
    }

    public void onDetachToPermissionActivity(ListenerPermissionResult listenerPermissionResult) {
        mListenerPermissionResult.remove(listenerPermissionResult);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (ListenerPermissionResult listenerPermissionResult : mListenerPermissionResult) {
            listenerPermissionResult.onPermissionRequestResult(requestCode, permissions, grantResults);
        }
    }
}
