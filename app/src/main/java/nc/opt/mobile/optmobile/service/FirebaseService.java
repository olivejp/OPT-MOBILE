package nc.opt.mobile.optmobile.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.List;

import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.utils.Constants;

/**
 * Created by orlanth23 on 19/11/2017.
 */

public class FirebaseService {

    private FirebaseService() {
    }

    private static final String TAG = FirebaseService.class.getName();

    public static DatabaseReference getUsersRef() {
        return FirebaseDatabase.getInstance().getReference(Constants.DATABASE_USERS_REFERENCE);
    }

    private static WeakReference<View> weakRefView;

    /**
     * @param listColis
     * @param view
     */
    public static void createRemoteDatabase(@NotNull List<ColisEntity> listColis, @Nullable View view) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            for (ColisEntity colisEntity : listColis) {
                updateRemoteDatabase(firebaseUser, colisEntity, view);
            }
        }
    }

    public static void deleteRemoteColis(FirebaseUser firebaseUser, @NotNull ColisEntity colis) {
        if (firebaseUser != null) {
            getUsersRef().child(firebaseUser.getUid()).child(colis.getIdColis()).removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Log.d(TAG, "Suppression réussie dans Firebase");
                }
            });
        }
    }

    public static void updateRemoteDatabase(FirebaseUser firebaseUser, @NotNull ColisEntity colis, @Nullable View view) {
        if (firebaseUser != null) {
            if (view != null) weakRefView = new WeakReference<>(view);
            getUsersRef().child(firebaseUser.getUid()).child(colis.getIdColis()).setValue(colis)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (weakRefView.get() != null) {
                                Snackbar.make(weakRefView.get(), "Insertion dans Firebase réussie.", Snackbar.LENGTH_LONG).show();
                            }
                            Log.d(TAG, "Insertion réussie dans Firebase");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String message = "Echec de l'insertion dans Firebase.";
                            FirebaseCrash.log(message.concat(" Exception:").concat(e.getMessage()));
                            if (weakRefView.get() != null) {
                                Snackbar.make(weakRefView.get(), message, Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    /**
     * @param valueEventListener
     */
    public static void getFromRemoteDatabase(@Nullable ValueEventListener valueEventListener) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            DatabaseReference userReference = getUsersRef().child(firebaseUser.getUid());
            if (valueEventListener != null) {
                userReference.addValueEventListener(valueEventListener);
            }
        }
    }
}
