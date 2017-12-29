package nc.opt.mobile.optmobile.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import nc.opt.mobile.optmobile.R;

/**
 * Created by 2761oli on 10/10/2017.
 */

public class NoticeDialogFragment extends DialogFragment {

    public static final String P_MESSAGE = "message";
    public static final String P_TYPE = "type";
    public static final String P_IMG = "image";
    public static final String P_BUNDLE = "mBundle";

    public static final int TYPE_BOUTON_YESNO = 10;
    public static final int TYPE_BOUTON_OK = 20;

    public static final int TYPE_IMAGE_CAUTION = 100;
    public static final int TYPE_IMAGE_ERROR = 110;
    public static final int TYPE_IMAGE_INFORMATION = 120;

    private Bundle mBundle;
    private NoticeDialogListener mListenerContext;

    public Bundle getBundle() {
        return mBundle;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListenerContext = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            Log.e("ClassCastException", e.getMessage(), e);
            throw new ClassCastException(getActivity().toString()
                    + " doit implementer l'interface NoticeDialogListener");
        }
    }

    private void getTypeBouton(AlertDialog.Builder builder, Integer typeButton) {
        switch (typeButton) {
            case TYPE_BOUTON_OK:
                builder.setPositiveButton("OK", (dialog, which) -> mListenerContext.onDialogPositiveClick(NoticeDialogFragment.this));
                break;
            case TYPE_BOUTON_YESNO:
                builder.setPositiveButton("Oui", (dialog, which) -> mListenerContext.onDialogPositiveClick(NoticeDialogFragment.this))
                        .setNegativeButton("Non", (dialog, which) -> mListenerContext.onDialogNegativeClick(NoticeDialogFragment.this));
                break;
            default:
                break;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(view);

        Integer typeBouton = null;
        Integer typeImage = null;
        TextView textview;

        // Récupération des arguments
        if (getArguments() != null) {
            if (getArguments().containsKey(P_TYPE)) {
                typeBouton = getArguments().getInt(P_TYPE);
            }
            if (getArguments().containsKey(P_IMG)) {
                typeImage = getArguments().getInt(P_IMG);
            }
            if (getArguments().containsKey(P_MESSAGE)) {
                textview = view.findViewById(R.id.msgDialog);
                textview.setText(getArguments().getString(P_MESSAGE));
            }
            if (getArguments().containsKey(P_BUNDLE)) {
                mBundle = getArguments().getBundle(P_BUNDLE);
            }
        }

        // Gestion de l'image à afficher en haut de la fenêtre
        ImageView imgView = view.findViewById(R.id.imageDialog);
        if (typeImage != null) {
            switch (typeImage) {
                case TYPE_IMAGE_CAUTION:
                    imgView.setImageResource(R.drawable.ic_warning_white_48dp);
                    break;
                case TYPE_IMAGE_ERROR:
                    imgView.setImageResource(R.drawable.ic_error_white_48dp);
                    break;
                case TYPE_IMAGE_INFORMATION:
                    imgView.setImageResource(R.drawable.ic_announcement_white_48dp);
                    break;
                default:
                    imgView.setImageResource(R.drawable.ic_announcement_white_48dp);
                    break;
            }
        }

        // Récupération du bon type de bouton.
        getTypeBouton(builder, typeBouton);

        // On retourne l'objet créé.
        return builder.create();
    }

    /* The mActivity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        void onDialogPositiveClick(NoticeDialogFragment dialog);

        void onDialogNegativeClick(NoticeDialogFragment dialog);
    }
}
