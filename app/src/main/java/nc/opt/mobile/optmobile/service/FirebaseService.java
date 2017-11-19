package nc.opt.mobile.optmobile.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import nc.opt.mobile.optmobile.utils.Constants;

/**
 * Created by orlanth23 on 19/11/2017.
 */

public class FirebaseService {

    public static DatabaseReference getUsersRef() {
        return FirebaseDatabase.getInstance().getReference(Constants.DATABASE_USERS_REFERENCE);
    }

    public static void createInRemoteDatabase(@NotNull String userId, @NotNull Object value, @Nullable final View view) {
        getUsersRef().child(userId).setValue(value)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (view != null) {
                            Snackbar.make(view, "Insertion dans Firebase réussie.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (view != null) {
                            String message = "Echec de l'insertion dans Firebase.";
                            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                            FirebaseCrash.log(message.concat(" Exception:").concat(e.getMessage()));
                        }
                    }
                });
    }

    public static DatabaseReference getFromRemoteDatabase(@NotNull String userId, @Nullable ChildEventListener childEventListener, @Nullable ValueEventListener valueEventListener) {
        DatabaseReference userReference = getUsersRef().child(userId);
        if (childEventListener != null) {
            userReference.addChildEventListener(childEventListener);
        }
        if (valueEventListener != null) {
            userReference.addValueEventListener(valueEventListener);
        }
        return userReference;
    }
}
