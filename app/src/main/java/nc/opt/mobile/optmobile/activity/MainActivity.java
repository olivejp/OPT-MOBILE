package nc.opt.mobile.optmobile.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.activity.interfaces.AttachToPermissionActivity;
import nc.opt.mobile.optmobile.broadcast.NetworkReceiver;
import nc.opt.mobile.optmobile.fragment.ActualiteFragment;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderObserver;
import nc.opt.mobile.optmobile.service.FirebaseService;
import nc.opt.mobile.optmobile.utils.CatchPhotoFromUrlTask;
import nc.opt.mobile.optmobile.utils.NoticeDialogFragment;
import nc.opt.mobile.optmobile.utils.Utilities;

import static nc.opt.mobile.optmobile.provider.services.AgenceService.populateContentProviderFromAsset;
import static nc.opt.mobile.optmobile.provider.services.ColisService.count;
import static nc.opt.mobile.optmobile.utils.Constants.PREF_USER;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class MainActivity extends AttachToPermissionActivity
        implements NavigationView.OnNavigationItemSelectedListener, NoticeDialogFragment.NoticeDialogListener, ProviderObserver.ProviderObserverListener, CatchPhotoFromUrlTask.PhotoFromUrlListener {

    private static final String TAG = MainActivity.class.getName();

    public static final String DIALOG_TAG_EXIT = "DIALOG_TAG_EXIT";

    public static final String SAVED_ACTUALITE_FRAGMENT = "SAVED_ACTUALITE_FRAGMENT";

    public static final int RC_PERMISSION_LOCATION = 100;
    public static final int RC_PERMISSION_CALL_PHONE = 200;
    public static final int RC_PERMISSION_INTERNET = 300;
    public static final int RC_SIGN_IN = 300;

    private static final String PREF_POPULATED = "POPULATE_CP";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private ImageView mImageViewProfile;
    private Button mButtonConnexion;
    private TextView mProfilName;

    private ActualiteFragment mActualiteFragment;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private void signIn() {
        if (NetworkReceiver.checkConnection(this)) {
            signOut();

            List<AuthUI.IdpConfig> listProviders = new ArrayList<>();
            listProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
            listProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(listProviders)
                            .build(),
                    RC_SIGN_IN);
        } else {
            Snackbar.make(navigationView, "Une connexion est requise pour se connecter", Snackbar.LENGTH_LONG).show();
        }
    }

    private void signOut() {
        mButtonConnexion.setText(R.string.login);
        mProfilName.setText(null);
        mFirebaseUser = null;
        mImageViewProfile.setImageResource(R.drawable.ic_person_white_48dp);

        // We delete the shared Preference containing the UI of the user.
        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PREF_USER);
        editor.apply();
    }

    private void defineAuthListener() {
        mAuthStateListener = firebaseAuth -> {
            mFirebaseUser = firebaseAuth.getCurrentUser();
            if (mFirebaseUser != null) {
                mButtonConnexion.setText(R.string.logout);
                mProfilName.setText(mFirebaseUser.getDisplayName());
                Uri[] uris = new Uri[]{mFirebaseUser.getPhotoUrl()};

                new CatchPhotoFromUrlTask(MainActivity.this, this).execute(uris);

                FirebaseService.firebaseDatabaseUserExist(this, mFirebaseUser.getUid(), navigationView);

                // Save the UID of the user in the SharedPreference
                SharedPreferences sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PREF_USER, mFirebaseUser.getUid());
                editor.apply();
            } else {
                signOut();
            }
        };
    }

    private void updateBadge() {
        // Récupération du textView présent dans le menu
        Menu menu = navigationView.getMenu();
        TextView suiviColisBadgeCounter = (TextView) menu.findItem(R.id.nav_suivi_colis).getActionView();
        suiviColisBadgeCounter.setGravity(Gravity.CENTER_VERTICAL);
        suiviColisBadgeCounter.setTypeface(null, Typeface.BOLD);
        suiviColisBadgeCounter.setTextColor(getResources().getColor(R.color.colorPrimary));
        suiviColisBadgeCounter.setText(String.valueOf(count(this, true)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_ACTUALITE_FRAGMENT)) {
            mActualiteFragment = (ActualiteFragment) getSupportFragmentManager().getFragment(savedInstanceState, SAVED_ACTUALITE_FRAGMENT);
        }
        if (mActualiteFragment == null) {
            mActualiteFragment = ActualiteFragment.newInstance();
        }

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        View headerLayout = navigationView.getHeaderView(0);

        mImageViewProfile = headerLayout.findViewById(R.id.image_view_profile);
        mButtonConnexion = headerLayout.findViewById(R.id.button_connexion);
        mProfilName = headerLayout.findViewById(R.id.text_profil_name);

        mFirebaseAuth = FirebaseAuth.getInstance();

        defineAuthListener();

        mButtonConnexion.setOnClickListener(v -> {
            if (mFirebaseUser != null) {
                signOut();
            } else {
                signIn();
            }
        });

        // Define the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null)

        {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Si la permission Internet n'a pas été accordée on va la demander
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, RC_PERMISSION_INTERNET);
        }

        // Populate the contentProvider with assets, only the first time
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(PREF_POPULATED, false)) {
            populateContentProviderFromAsset(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PREF_POPULATED, true);
            editor.apply();
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        updateBadge();

        // Enregistrement d'un observer pour écouter les modifications sur le ContentProvider
        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.observe(this, this, OptProvider.ListColis.LIST_COLIS);

        // Création du premier fragment
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.frame_main, mActualiteFragment).
                commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    Utilities.sendDialogByActivity(this, getString(R.string.want_you_quit), NoticeDialogFragment.TYPE_BOUTON_YESNO, NoticeDialogFragment.TYPE_IMAGE_INFORMATION, DIALOG_TAG_EXIT, this);
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_actualite:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, mActualiteFragment).commit();
                break;
            case R.id.nav_geo_agence:
                startActivity(new Intent(this, AgenceActivity.class));
                break;
            case R.id.nav_suivi_colis:
                startActivity(new Intent(this, GestionColisActivity.class));
                break;
            default:
                break;
        }

        // Après avoir cliquer sur un menu, on ferme le menu latéral
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFirebaseAuth != null) {
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFirebaseAuth != null && mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActualiteFragment != null) {
            getSupportFragmentManager().putFragment(outState, SAVED_ACTUALITE_FRAGMENT, mActualiteFragment);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                Toast.makeText(this, R.string.welcome, Toast.LENGTH_LONG).show();

                // Call the task to retrieve the photo
                Uri[] uris = new Uri[]{mFirebaseUser.getPhotoUrl()};
                new CatchPhotoFromUrlTask(MainActivity.this, this).execute(uris);

                // Put/Get datas from FirebaseDatabase.
                FirebaseService.firebaseDatabaseUserExist(this, mFirebaseUser.getUid(), navigationView);
            }
            if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, R.string.sign_in_cancelled, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION_INTERNET && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, R.string.internet_needed, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onDialogPositiveClick(NoticeDialogFragment dialog) {
        String s = dialog.getTag();
        if (s != null && s.equals(DIALOG_TAG_EXIT)) {
            finish();
        }
    }

    @Override
    public void onDialogNegativeClick(NoticeDialogFragment dialog) {
        // Do nothing
    }

    @Override
    public void onProviderChange(Uri uri) {
        updateBadge();
    }

    @Override
    public void catchPhotoFromUrl(Drawable drawable) {
        mImageViewProfile.setImageDrawable(drawable);
    }
}
