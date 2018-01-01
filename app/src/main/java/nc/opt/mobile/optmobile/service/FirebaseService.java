package nc.opt.mobile.optmobile.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

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

import static nc.opt.mobile.optmobile.utils.Constants.PREF_USER;

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
    public static void createRemoteDatabase(@NotNull Context context, @NotNull List<ColisEntity> listColis, @Nullable View view) {
        for (ColisEntity colisEntity : listColis) {
            updateRemoteDatabase(context, colisEntity, view);
        }
    }

    /**
     * @param idColis
     */
    public static void deleteRemoteColis(@NotNull String idColis) {
        Log.d(TAG, "(deleteRemoteColis) : Try to remove tracking : " + idColis);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            getUsersRef()
                    .child(user.getUid())
                    .child(idColis)
                    .removeValue((databaseError, databaseReference) -> Log.d(TAG, "(deleteRemoteColis) : Delete successful of : " + idColis));
        }
    }

    /**
     * @param context
     * @param colis
     * @param view
     */
    private static void updateRemoteDatabase(@NotNull Context context, @NotNull ColisEntity colis, @Nullable View view) {
        String uid = getUidOfFirebaseUser(context);
        if (uid != null) {
            if (view != null) weakRefView = new WeakReference<>(view);
            getUsersRef().child(uid).child(colis.getIdColis()).setValue(colis)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "(updateRemoteDatabase) : Insertion RÉUSSIE dans Firebase : " + colis.getIdColis());
                        if (weakRefView != null && weakRefView.get() != null) {
                            Snackbar.make(weakRefView.get(), "Insertion dans Firebase réussie.", Snackbar.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "(updateRemoteDatabase) : Insertion ÉCHOUÉE dans Firebase : " + colis.getIdColis());
                        String message = "Echec de l'insertion dans Firebase.";
                        FirebaseCrash.log(message.concat(" Exception:").concat(e.getMessage()));
                        if (weakRefView != null && weakRefView.get() != null) {
                            Snackbar.make(weakRefView.get(), message, Snackbar.LENGTH_LONG).show();
                        }
                    });
        } else {
            Log.d(TAG, "(updateRemoteDatabase) : Firebase User UID is null !");
        }
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

    /**
     * Envoi de la liste des colis à Firebase pour qu'il enregistre nos colis suivis.
     *
     * @param list
     */
    static void updateFirebase(Context context, List<ColisEntity> list) {
        FirebaseService.createRemoteDatabase(context, list, null);
    }

    /**
     * @param context
     * @return
     */
    @Nullable
    public static String getUidOfFirebaseUser(@NotNull Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_USER, null);
    }
}
