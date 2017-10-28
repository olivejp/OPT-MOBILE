package nc.opt.mobile.optmobile.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.domain.ActualiteDto;
import nc.opt.mobile.optmobile.utils.Constants;
import nc.opt.mobile.optmobile.utils.DateConverter;

public class AddActualiteActivity extends AppCompatActivity {

    private static final String TAG = AddActualiteActivity.class.getName();

    @BindView(R.id.edit_titre)
    EditText editTitre;

    @BindView(R.id.edit_contenu)
    EditText editContenu;

    @BindView(R.id.checkbox_dismissable)
    CheckBox check_dismissable;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private static final String KEY_ACTUALITE = "actualites";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        setContentView(R.layout.activity_add_actualite);

        ButterKnife.bind(this);
    }

    private ActualiteDto getActualiteFromScreen() {
        ActualiteDto actualiteDto = new ActualiteDto();
        actualiteDto.setTitre(editTitre.getText().toString());
        actualiteDto.setContenu(editContenu.getText().toString());
        actualiteDto.setDismissable(check_dismissable.isChecked());
        actualiteDto.setDate(DateConverter.getNowDto());
        return actualiteDto;
    }

    private void raz() {
        editTitre.setText(null);
        editContenu.setText(null);
        check_dismissable.setChecked(false);
    }

    @OnClick(R.id.button_valider)
    public void onValidate(View v) {

        // On enregistre dans FIRESTORE
        if (mFirebaseRemoteConfig.getBoolean(Constants.CLOUD_FIRESTORE)) {

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            CollectionReference collectionReference = firebaseFirestore.collection(KEY_ACTUALITE);

            collectionReference
                    .add(getActualiteFromScreen())
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            raz();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }

        // On enregistre dans FIREBASE
        if (mFirebaseRemoteConfig.getBoolean(Constants.FIREBASE_DATABASE)) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child(KEY_ACTUALITE);
            DatabaseReference newReference = databaseReference.push();
            newReference
                    .setValue(getActualiteFromScreen())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot added with ID");
                            raz();
                        }
                    });
        }
    }
}
