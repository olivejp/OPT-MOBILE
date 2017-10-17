package nc.opt.mobile.optmobile.activity;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.broadcast.NetworkReceiver;
import nc.opt.mobile.optmobile.fragment.AgencyMapFragment;
import nc.opt.mobile.optmobile.fragment.GestionColisFragment;
import nc.opt.mobile.optmobile.interfaces.AttachToPermissionActivity;
import nc.opt.mobile.optmobile.interfaces.ListenerPermissionResult;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderObserver;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;
import nc.opt.mobile.optmobile.service.SyncColisService;
import nc.opt.mobile.optmobile.utils.NoticeDialogFragment;
import nc.opt.mobile.optmobile.utils.RequestQueueSingleton;
import nc.opt.mobile.optmobile.utils.Utilities;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AttachToPermissionActivity, NoticeDialogFragment.NoticeDialogListener, ProviderObserver.ProviderObserverListener, NetworkReceiver.NetworkChangeListener {

    private static final String TAG = MainActivity.class.getName();
    public static final String TAG_AGENCY_MAP_FRAGMENT = "AGENCY_MAP_FRAGMENT";
    public static final String TAG_GESTION_COLIS_FRAGMENT = "TAG_GESTION_COLIS_FRAGMENT";
    public static final String TAG_SEARCH_PARCEL_FRAGMENT = "TAG_SEARCH_PARCEL_FRAGMENT";
    public static final String TAG_PARCEL_RESULT_SEARCH_FRAGMENT = "TAG_PARCEL_RESULT_SEARCH_FRAGMENT";

    public static final String DIALOG_TAG_EXIT = "DIALOG_TAG_EXIT";

    public static final int RC_PERMISSION_LOCATION = 100;
    public static final int RC_PERMISSION_CALL_PHONE = 200;
    public static final int RC_PERMISSION_INTERNET = 300;
    public static final int RC_SIGN_IN = 300;

    private static final String PREF_POPULATED = "POPULATE_CP";

    private static final String SAVED_AGENCY_FRAGMENT = "SAVED_AGENCY_FRAGMENT";
    private static final String SAVED_GESTION_COLIS_FRAGMENT = "SAVED_GESTION_COLIS_FRAGMENT";

    private static final String BACK_STACK_MAP = "BACK_STACK_MAP";
    private static final String BACK_STACK_COLIS = "BACK_STACK_COLIS";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Drawable mDrawablePhoto;
    private MenuItem mMenuItemProfil;
    private AgencyMapFragment agencyMapFragment;
    private GestionColisFragment gestionColisFragment;
    private NetworkReceiver mNetworkReceiver;
    private static ArrayList<ListenerPermissionResult> mListenerPermissionResult = new ArrayList<>();
    private NavigationView navigationView;

    private void callAgencyMapFragment() {
        if (!(getSupportFragmentManager().findFragmentById(R.id.frame_main) instanceof AgencyMapFragment)) {
            agencyMapFragment = (AgencyMapFragment) getSupportFragmentManager().findFragmentByTag(TAG_AGENCY_MAP_FRAGMENT);
            if (agencyMapFragment == null) {
                agencyMapFragment = AgencyMapFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_main, agencyMapFragment, TAG_AGENCY_MAP_FRAGMENT)
                        .addToBackStack(BACK_STACK_MAP)
                        .commit();
            } else {
                getSupportFragmentManager().popBackStack(BACK_STACK_MAP, 0);
            }
        }
    }

    private void callSuiviColis() {
        if (!(getSupportFragmentManager().findFragmentById(R.id.frame_main) instanceof GestionColisFragment)) {
            gestionColisFragment = (GestionColisFragment) getSupportFragmentManager().findFragmentByTag(TAG_GESTION_COLIS_FRAGMENT);
            if (gestionColisFragment == null) {
                gestionColisFragment = GestionColisFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_main, gestionColisFragment, TAG_GESTION_COLIS_FRAGMENT)
                        .addToBackStack(BACK_STACK_COLIS)
                        .commit();
            } else {
                getSupportFragmentManager().popBackStack(BACK_STACK_COLIS, 0);
            }
        }
    }

    private void defineAuthListener() {
        // On veut que l'utilisateur soit identifié pour continuer
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();

                if (mFirebaseUser != null) {
                    createAsyncTaskGetPhoto().execute();
                } else {
                    // User is signed out
                    onSignedOutCleanup();

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
                }
            }
        };
    }

    private AsyncTask<Void, Void, Drawable> createAsyncTaskGetPhoto() {
        // On définit une tache pour recuperer la photo de la personne connectee
        return new AsyncTask<Void, Void, Drawable>() {
            @Override
            protected Drawable doInBackground(Void... voids) {
                try {
                    return Glide.with(MainActivity.this)
                            .asDrawable()
                            .load(mFirebaseUser.getPhotoUrl())
                            .apply(RequestOptions.circleCropTransform())
                            .submit()
                            .get();
                } catch (InterruptedException | ExecutionException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Drawable drawable) {
                if (drawable != null) {
                    mDrawablePhoto = drawable;
                    invalidateOptionsMenu();
                }
            }
        };
    }

    private void onSignedOutCleanup() {
        mFirebaseUser = null;
        mDrawablePhoto = null;
        invalidateOptionsMenu();
    }

    private boolean isInternetPermited() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    private void updateBadge() {
        // Récupération du textView présent dans le menu
        Menu menu = navigationView.getMenu();
        TextView suiviColisBadgeCounter = (TextView) menu.findItem(R.id.nav_suivi_colis).getActionView();
        suiviColisBadgeCounter.setGravity(Gravity.CENTER_VERTICAL);
        suiviColisBadgeCounter.setTypeface(null, Typeface.BOLD);
        suiviColisBadgeCounter.setTextColor(getResources().getColor(R.color.colorPrimary));
        suiviColisBadgeCounter.setText(String.valueOf(ProviderUtilities.countColis(this)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get information back from the savedInstanceState
        if (savedInstanceState != null) {
            agencyMapFragment = (AgencyMapFragment) getSupportFragmentManager().getFragment(savedInstanceState, SAVED_AGENCY_FRAGMENT);
            gestionColisFragment = (GestionColisFragment) getSupportFragmentManager().getFragment(savedInstanceState, SAVED_GESTION_COLIS_FRAGMENT);
        }

        // Si la permission Internet n'a pas été accordée on va la demander
        if (!isInternetPermited()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, RC_PERMISSION_INTERNET);
        }

        mFirebaseAuth = FirebaseAuth.getInstance();

        // Populate the contentProvider with assets, only the first time
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(PREF_POPULATED, false)) {
            ProviderUtilities.populateContentProviderFromAsset(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PREF_POPULATED, true);
            editor.apply();
        }

        defineAuthListener();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        updateBadge();

        // Appel de la premiere instance
        RequestQueueSingleton.getInstance(this.getApplicationContext());

        // Enregistrement d'un observer pour écouter les modifications sur le ContentProvider
        ArrayList<Uri> uris = new ArrayList<>();
        uris.add(OptProvider.ListColis.LIST_COLIS);
        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.observe(this, this, uris);

        // Lancement du service de synchro
        SyncColisService.launchSynchroForAll(this, true);

    }

    @Override
    public void onBackPressed() {
        // Fermeture du drawer latéral s'il est ouvert
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    // on demande avant de quitter l'application
                    Utilities.SendDialogByActivity(this, "Voulez vous quitter l'application ?", NoticeDialogFragment.TYPE_BOUTON_YESNO, NoticeDialogFragment.TYPE_IMAGE_INFORMATION, DIALOG_TAG_EXIT);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mFirebaseUser != null) {
            if (mMenuItemProfil != null) {
                menu.removeItem(mMenuItemProfil.getItemId());
            }
            mMenuItemProfil = menu.add(mFirebaseUser.getDisplayName())
                    .setIcon(mDrawablePhoto)
                    .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            mMenuItemProfil = null;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                mFirebaseAuth.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_geo_agence:
                callAgencyMapFragment();
                break;
            case R.id.nav_suivi_colis:
                callSuiviColis();
                break;
            default:
                break;
        }

        // Après avoir cliquer sur un menu, on ferme le menu latéral
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

        // On attache le receiver a l'application
        mNetworkReceiver = NetworkReceiver.getInstance();
        registerReceiver(mNetworkReceiver, NetworkReceiver.CONNECTIVITY_CHANGE_INTENT_FILTER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNetworkReceiver != null) {
            unregisterReceiver(mNetworkReceiver);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                Toast.makeText(this, R.string.welcome, Toast.LENGTH_LONG).show();

                // On appelle la tache pour aller recuperer la photo
                createAsyncTaskGetPhoto().execute();

            }
            if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, R.string.sign_in_cancelled, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        AgencyMapFragment fragA = (AgencyMapFragment) getSupportFragmentManager().findFragmentByTag(TAG_AGENCY_MAP_FRAGMENT);
        if (fragA != null) {
            getSupportFragmentManager().putFragment(outState, SAVED_AGENCY_FRAGMENT, fragA);
        }
        GestionColisFragment fragB = (GestionColisFragment) getSupportFragmentManager().findFragmentByTag(TAG_GESTION_COLIS_FRAGMENT);
        if (fragB != null) {
            getSupportFragmentManager().putFragment(outState, SAVED_GESTION_COLIS_FRAGMENT, fragB);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (ListenerPermissionResult listenerPermissionResult : mListenerPermissionResult) {
            listenerPermissionResult.onPermissionRequestResult(requestCode, permissions, grantResults);
        }

        switch (requestCode) {
            case RC_PERMISSION_INTERNET:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Un accès à internet est nécessaire pour cette application.", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttachPermissionActivity(ListenerPermissionResult listenerPermissionResult) {
        mListenerPermissionResult.add(listenerPermissionResult);
    }

    @Override
    public void onDetachToPermissionActivity(ListenerPermissionResult listenerPermissionResult) {
        mListenerPermissionResult.remove(listenerPermissionResult);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        switch (dialog.getTag()) {
            case DIALOG_TAG_EXIT:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        switch (dialog.getTag()) {
            case DIALOG_TAG_EXIT:
                break;
            default:
                break;
        }
    }

    @Override
    public void onProviderChange() {
        updateBadge();
    }

    @Override
    public void OnNetworkEnable() {
        SyncColisService.launchSynchroForAll(MainActivity.this, true);
    }

    @Override
    public void OnNetworkDisable() {
        throw new UnsupportedOperationException();
    }
}
