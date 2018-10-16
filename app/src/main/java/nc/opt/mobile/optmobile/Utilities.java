package nc.opt.mobile.optmobile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import nc.opt.mobile.optmobile.ui.NoticeDialogFragment;

/**
 * Created by orlanth23 on 13/08/2017.
 */

public class Utilities {

    public static final String INFO_RECEIVED = "InfoReceived";
    public static final String ATTEMPT_FAIL = "AttemptFail";
    public static final String DELIVERED = "Delivered";
    public static final String EXCEPTION = "Exception";
    public static final String EXPIRED = "Expired";
    public static final String IN_TRANSIT = "InTransit";
    public static final String OUT_FOR_DELIVERY = "OutForDelivery";
    public static final String PENDING = "Pending";

    private Utilities() {
    }

    /**
     * Hide the keyboard
     *
     * @param ctx
     */
    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * Retrieve the associated drawable id from the etape status
     *
     * @param status The etape status
     * @return Drawable Id corresponding on the etape status
     */
    @DrawableRes
    public static int getStatusDrawable(@NonNull String status) {
        switch (status) {
            case INFO_RECEIVED:
                return nc.opt.mobile.optmobile.R.drawable.ic_status_info_receive;
            case ATTEMPT_FAIL:
                return nc.opt.mobile.optmobile.R.drawable.ic_status_attemptfail;
            case DELIVERED:
                return nc.opt.mobile.optmobile.R.drawable.ic_status_delivered;
            case EXCEPTION:
                return nc.opt.mobile.optmobile.R.drawable.ic_status_exception;
            case EXPIRED:
                return nc.opt.mobile.optmobile.R.drawable.ic_status_expired;
            case IN_TRANSIT:
                return nc.opt.mobile.optmobile.R.drawable.ic_status_in_transit;
            case OUT_FOR_DELIVERY:
                return nc.opt.mobile.optmobile.R.drawable.ic_status_out_for_delivery;
            case PENDING:
                return nc.opt.mobile.optmobile.R.drawable.ic_status_pending;
            default:
                return nc.opt.mobile.optmobile.R.drawable.ic_status_pending;
        }
    }

    /**
     * @param fragmentManager Get from the context
     * @param message         The message to be send
     * @param type            From NoticeDialogFragment
     * @param img             From NoticeDialogFragment
     * @param tag             A text to be a tag
     */
    public static void sendDialogByFragmentManager(FragmentManager fragmentManager, String message, int type, int img, @Nullable String tag, @Nullable Bundle bundlePar, NoticeDialogFragment.NoticeDialogListener listener) {
        NoticeDialogFragment dialogErreur = new NoticeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NoticeDialogFragment.P_MESSAGE, message);
        bundle.putInt(NoticeDialogFragment.P_TYPE, type);
        bundle.putInt(NoticeDialogFragment.P_IMG, img);
        bundle.putBundle(NoticeDialogFragment.P_BUNDLE, bundlePar);
        dialogErreur.setListener(listener);
        dialogErreur.setArguments(bundle);
        dialogErreur.show(fragmentManager, tag);
    }

    /**
     * @param fragmentManager Get from the context
     * @param message         The message to be send
     * @param type            From NoticeDialogFragment
     * @param idDrawable      From NoticeDialogFragment
     * @param tag             A text to be a tag
     */
    public static void sendDialogByFragmentManagerWithRes(FragmentManager fragmentManager, String message, int type, @DrawableRes int idDrawable, @Nullable String tag, @Nullable Bundle bundlePar, NoticeDialogFragment.NoticeDialogListener listener) {
        NoticeDialogFragment dialogErreur = new NoticeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NoticeDialogFragment.P_MESSAGE, message);
        bundle.putInt(NoticeDialogFragment.P_TYPE, type);
        bundle.putInt(NoticeDialogFragment.P_IMG_ID_RES, idDrawable);
        bundle.putBundle(NoticeDialogFragment.P_BUNDLE, bundlePar);
        dialogErreur.setListener(listener);
        dialogErreur.setArguments(bundle);
        dialogErreur.show(fragmentManager, tag);
    }

}
