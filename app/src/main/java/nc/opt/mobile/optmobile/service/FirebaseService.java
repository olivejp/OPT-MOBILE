package nc.opt.mobile.optmobile.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    public static void createRemoteDatabase(@NotNull String userUid, @NotNull List<ColisEntity> listColis, @Nullable View view) {
        for (ColisEntity colisEntity : listColis) {
            updateRemoteDatabase(userUid, colisEntity, view);
        }
    }

    public static void deleteRemoteColis(@NotNull String userUid, @NotNull String idColis, @Nullable DatabaseReference.CompletionListener completionListener) {
        DatabaseReference.CompletionListener listener;
        if (completionListener == null) {
            listener = new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Log.d(TAG, "Suppression réussie dans Firebase");
                }
            };
        } else {
            listener = completionListener;
        }

        getUsersRef().child(userUid).child(idColis).removeValue(listener);
    }


    private static void updateRemoteDatabase(@NotNull String userUid, @NotNull ColisEntity colis, @Nullable View view) {
        if (view != null) weakRefView = new WeakReference<>(view);
        getUsersRef().child(userUid).child(colis.getIdColis()).setValue(colis)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (weakRefView != null && weakRefView.get() != null) {
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
                        if (weakRefView != null && weakRefView.get() != null) {
                            Snackbar.make(weakRefView.get(), message, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

    }

    /**
     * @param valueEventListener
     */
    public static void getFromRemoteDatabase(@NotNull String userUid, @Nullable ValueEventListener valueEventListener) {
        DatabaseReference userReference = getUsersRef().child(userUid);
        if (valueEventListener != null) {
            userReference.addValueEventListener(valueEventListener);
        }
    }
}
