package nc.opt.mobile.optmobile.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;


/**
 * Created by 2761oli on 10/10/2017.
 */
public class InputDialogFragment extends DialogFragment {

    public static final String P_TEXT = "text";

    private AppCompatActivity appCompatActivity;
    private InputDialogFragmentListener mListenerContext;

    @BindView(R.id.colis_description)
    EditText colisDescription;

    public InputDialogFragment() {
        // Empty constructor
    }

    public void setmListenerContext(InputDialogFragmentListener mListenerContext) {
        this.mListenerContext = mListenerContext;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            appCompatActivity = (AppCompatActivity) context;
        } catch (ClassCastException e) {
            Log.e("ClassCastException", e.getMessage(), e);
            throw new ClassCastException(getActivity().toString() + " doit être une AppCompatActivity");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Inflate and bind the view
        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.input_dialog_layout, null);
        builder.setView(view);
        ButterKnife.bind(this, view);

        // Get the arguments in
        if (getArguments() != null && getArguments().containsKey(P_TEXT)) {
            colisDescription.setText(getArguments().getString(P_TEXT));
        }

        // Récupération du bon type de bouton.
        builder.setPositiveButton("OK", (dialog, which) -> mListenerContext.onInputDialogFinishClick(this, colisDescription.getText().toString()));

        // On retourne l'objet créé.
        return builder.create();
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }

    public interface InputDialogFragmentListener {
        void onInputDialogFinishClick(InputDialogFragment fragment, String description);
    }
}
