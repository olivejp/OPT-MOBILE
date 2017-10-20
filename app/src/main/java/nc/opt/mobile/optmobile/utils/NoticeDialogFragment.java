package nc.opt.mobile.optmobile.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
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

    public static final int TYPE_BOUTON_YESNO = 10;
    public static final int TYPE_BOUTON_OK = 20;
    public static final int TYPE_IMAGE_CAUTION = 100;
    public static final int TYPE_IMAGE_ERROR = 110;
    public static final int TYPE_IMAGE_INFORMATION = 120;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(view);

        Integer typeBouton = getArguments().getInt(P_TYPE);
        Integer typeImage = getArguments().getInt(P_IMG);
        TextView textview = view.findViewById(R.id.msgDialog);
        textview.setText(getArguments().getString(P_MESSAGE));

        switch (typeBouton) {
            case TYPE_BOUTON_OK:
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Clic sur OK
                        ((NoticeDialogListener) getActivity()).onDialogPositiveClick(NoticeDialogFragment.this);
                    }
                });
                break;
            case TYPE_BOUTON_YESNO:
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Clic sur OK
                        try {
                            NoticeDialogListener mListenerContext = (NoticeDialogListener) getActivity();
                            mListenerContext.onDialogPositiveClick(NoticeDialogFragment.this);
                        } catch (ClassCastException e) {
                            Log.e("ClassCastException", e.getMessage(), e);
                            throw new ClassCastException(getActivity().toString()
                                    + " doit implementer l'interface NoticeDialogListener");
                        }
                    }
                }).setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Clic sur annuler
                        try {
                            NoticeDialogListener mListenerContext = (NoticeDialogListener) getActivity();
                            mListenerContext.onDialogNegativeClick(NoticeDialogFragment.this);
                        } catch (ClassCastException e) {
                            Log.e("ClassCastException", e.getMessage(), e);
                            throw new ClassCastException(getActivity().toString()
                                    + " doit implementer l'interface NoticeDialogListener");
                        }
                    }
                });
                break;
            default:
                break;
        }

        ImageView imgView = view.findViewById(R.id.imageDialog);
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

        // On retourne l'objet créé.
        return builder.create();
    }

    /* The mActivity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }
}
