package nc.opt.mobile.optmobile.interfaces;

import android.support.annotation.NonNull;

/**
 * Created by orlanth23 on 03/10/2017.
 */

public interface ListenerPermissionResult {
    void onPermissionRequestResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
