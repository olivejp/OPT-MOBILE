package nc.opt.mobile.optmobile.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
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

    public static DatabaseReference getUsersRef() {
        return FirebaseDatabase.getInstance().getReference(Constants.DATABASE_USERS_REFERENCE);
    }

    private static WeakReference<View> weakRefView;

    /**
     *
     * @param listColis
     * @param view
     */
    public static void createInRemoteDatabase(@NotNull List<ColisEntity> listColis, @Nullable View view) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            for (ColisEntity colisEntity : listColis) {
                updateRemoteDatabase(firebaseUser, colisEntity, view);
            }
        }
    }

    public static void updateRemoteDatabase(FirebaseUser firebaseUser, @NotNull ColisEntity colis, @Nullable View view) {
        if (firebaseUser != null) {
            weakRefView = new WeakReference<>(view);
            getUsersRef().child(firebaseUser.getUid()).child(colis.getIdColis()).setValue(colis)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (weakRefView.get() != null) {
                                Snackbar.make(weakRefView.get(), "Insertion dans Firebase réussie.", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (weakRefView.get() != null) {
                                String message = "Echec de l'insertion dans Firebase.";
                                Snackbar.make(weakRefView.get(), message, Snackbar.LENGTH_LONG).show();
                                FirebaseCrash.log(message.concat(" Exception:").concat(e.getMessage()));
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
