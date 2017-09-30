package nc.opt.mobile.optmobile.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import icepick.Icepick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.fragment.AgencyMapFragment;
import nc.opt.mobile.optmobile.provider.ProviderUtilities;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getName();

    private static final int RC_SIGN_IN = 100;
    private static final String PREF_POPULATED = "POPULATE_CP";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Drawable mDrawablePhoto;
    private MenuItem mMenuItemProfil;

    private void callAgencyMapFragment() {
        AgencyMapFragment agencyMapFragment = AgencyMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, agencyMapFragment).addToBackStack(null).commit();
    }

    private void defineAuthListener() {
        // On veut que l'utilisateur soit identifié pour continuer
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();

                if (mFirebaseUser != null) {
                    defineAsyncTaskGetPhoto().execute();
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

    private AsyncTask<Void, Void, Drawable> defineAsyncTaskGetPhoto() {
        // On définit une tache pour recuperer la photo de la personne connectee
        return new AsyncTask<Void, Void, Drawable>() {
            @Override
            protected Drawable doInBackground(Void... voids) {
                try {
                    return Glide.with(MainActivity.this)
                            .asDrawable()
                            .load(mFirebaseUser.getPhotoUrl())
                            .apply(bitmapTransform(new RoundedCornersTransformation(3, 3, RoundedCornersTransformation.CornerType.ALL)))
                            .submit(200, 200)
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get information back from the savedInstanceState
        Icepick.restoreInstanceState(this, savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();

        // Populate the contentProvider with assets, only the first time
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(PREF_POPULATED, false)) {
            ProviderUtilities.populateContentProviderFromAsset(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PREF_POPULATED, true);
            editor.apply();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        defineAuthListener();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        defineAsyncTaskGetPhoto();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            case R.id.action_settings:
                return true;
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
            case R.id.nav_geo_fibre:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
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
                defineAsyncTaskGetPhoto().execute();

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
        Icepick.saveInstanceState(this, outState);
    }
}
