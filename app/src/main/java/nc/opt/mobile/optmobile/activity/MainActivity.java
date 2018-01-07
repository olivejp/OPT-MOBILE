package nc.opt.mobile.optmobile.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.activity.interfaces.AttachToPermissionActivity;
import nc.opt.mobile.optmobile.activity.viewmodel.GestionColisActivityViewModel;
import nc.opt.mobile.optmobile.broadcast.NetworkReceiver;
import nc.opt.mobile.optmobile.fragment.ActualiteFragment;
import nc.opt.mobile.optmobile.fragment.GestionColisFragment;
import nc.opt.mobile.optmobile.job.task.SyncTask;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderObserver;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.services.ColisService;
import nc.opt.mobile.optmobile.service.FirebaseService;
import nc.opt.mobile.optmobile.utils.CatchPhotoFromUrlTask;
import nc.opt.mobile.optmobile.utils.CoreSync;
import nc.opt.mobile.optmobile.utils.NoticeDialogFragment;
import nc.opt.mobile.optmobile.utils.Utilities;

import static nc.opt.mobile.optmobile.fragment.GestionColisFragment.DIALOG_TAG_DELETE;
import static nc.opt.mobile.optmobile.provider.services.AgenceService.populateContentProviderFromAsset;
import static nc.opt.mobile.optmobile.provider.services.ColisService.count;
import static nc.opt.mobile.optmobile.utils.Constants.PREF_USER;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class MainActivity extends AttachToPermissionActivity
        implements NetworkReceiver.NetworkChangeListener, NavigationView.OnNavigationItemSelectedListener, NoticeDialogFragment.NoticeDialogListener, ProviderObserver.ProviderObserverListener, CatchPhotoFromUrlTask.PhotoFromUrlListener {

    public static final String TAG_PARCEL_RESULT_SEARCH_FRAGMENT = "TAG_PARCEL_RESULT_SEARCH_FRAGMENT";
    public static final String GESTION_FRAGMENT_TAG = "GESTION_FRAGMENT_TAG";
    public static final String ACTUALITE_FRAGMENT_TAG = "ACTUALITE_FRAGMENT_TAG";
    public static final String ARG_NOTICE_BUNDLE_COLIS = "ARG_NOTICE_BUNDLE_COLIS";
    public static final String ARG_NOTICE_BUNDLE_POSITION = "ARG_NOTICE_BUNDLE_POSITION";
    public static final String DIALOG_TAG_EXIT = "DIALOG_TAG_EXIT";
    public static final int RC_PERMISSION_LOCATION = 100;
    public static final int RC_PERMISSION_CALL_PHONE = 200;
    public static final int RC_PERMISSION_INTERNET = 300;
    public static final int RC_SIGN_IN = 300;

    private static final String PREF_POPULATED = "POPULATE_CP";

    private ColisEntity mColisSelected;
    private GestionColisFragment gestionColisFragment;
    private GestionColisActivityViewModel viewModel;

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

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                if (dataSnapshot.hasChild(user.getUid())) {
                    DatabaseReference userReference = FirebaseService.getUsersRef().child(user.getUid());
                    userReference.addValueEventListener(getFromRemoteValueEventListener);
                } else {
                    List<ColisEntity> listColis = ColisService.listFromProvider(MainActivity.this, true);
                    FirebaseService.createRemoteDatabase(MainActivity.this, listColis, navigationView);
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Do Nothing
        }
    };

    ValueEventListener getFromRemoteValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                ColisEntity remoteColisEntity = postSnapshot.getValue(ColisEntity.class);
                if (remoteColisEntity != null) {
                    if (ColisService.exist(MainActivity.this, remoteColisEntity.getIdColis(), false)) {
                        ColisEntity colisEntity = ColisService.get(MainActivity.this, remoteColisEntity.getIdColis());
                        if (colisEntity != null && colisEntity.getDeleted() == 1) {
                            // Colis exist in our local DB but has been deleted.
                            // We update our remote database.
                            FirebaseService.deleteRemoteColis(remoteColisEntity.getIdColis());
                            ColisService.realDelete(MainActivity.this, remoteColisEntity.getIdColis());
                        }
                    } else {
                        // Colis don't already exist in our local DB, we insert it.
                        ColisService.save(MainActivity.this, remoteColisEntity);
                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            //Do nothing
        }
    };

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

                FirebaseService.getUsersRef().addListenerForSingleValueEvent(valueEventListener);

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

        viewModel = ViewModelProviders.of(this).get(GestionColisActivityViewModel.class);
        viewModel.getSelectedColis().observe(this, colisEntity -> {
            mColisSelected = colisEntity;
            if (colisEntity != null) {
                setTitle(colisEntity.getIdColis());
            }
        });

        setContentView(R.layout.activity_main);
        boolean mTwoPane = findViewById(R.id.frame_detail) != null;

        if (savedInstanceState != null && savedInstanceState.containsKey(ACTUALITE_FRAGMENT_TAG)) {
            mActualiteFragment = (ActualiteFragment) getSupportFragmentManager().getFragment(savedInstanceState, ACTUALITE_FRAGMENT_TAG);
        }
        if (mActualiteFragment == null) {
            mActualiteFragment = ActualiteFragment.newInstance();
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(GESTION_FRAGMENT_TAG)) {
            gestionColisFragment = (GestionColisFragment) getSupportFragmentManager().getFragment(savedInstanceState, GESTION_FRAGMENT_TAG);
        }
        if (gestionColisFragment == null) {
            gestionColisFragment = GestionColisFragment.newInstance(mTwoPane);
        }

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
        if (ab != null) ab.setDisplayHomeAsUpEnabled(true);


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

        // Création du premier fragment
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.frame_main, gestionColisFragment, GESTION_FRAGMENT_TAG).
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
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, mActualiteFragment, ACTUALITE_FRAGMENT_TAG).commit();
                break;
            case R.id.nav_geo_agence:
                startActivity(new Intent(this, AgenceActivity.class));
                break;
            case R.id.nav_suivi_colis:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, gestionColisFragment, GESTION_FRAGMENT_TAG).commit();
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

        // Enregistrement d'un observer pour écouter les modifications sur le ContentProvider
        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.observe(this, this, OptProvider.ListColis.LIST_COLIS);

        // On écoute les changements réseau
        NetworkReceiver.getInstance().listen(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFirebaseAuth != null && mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }

        FirebaseService.getUsersRef().removeEventListener(valueEventListener);

        // Enregistrement d'un observer pour écouter les modifications sur le ContentProvider
        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.unregister(this, OptProvider.ListColis.LIST_COLIS);

        viewModel.releaseProviderObserver();
        NetworkReceiver.getInstance().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFirebaseAuth != null && mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }

        FirebaseService.getUsersRef().removeEventListener(valueEventListener);

        // Enregistrement d'un observer pour écouter les modifications sur le ContentProvider
        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.unregister(this, OptProvider.ListColis.LIST_COLIS);

        viewModel.releaseProviderObserver();
        NetworkReceiver.getInstance().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActualiteFragment != null) {
            if (getSupportFragmentManager().findFragmentByTag(ACTUALITE_FRAGMENT_TAG) != null) {
                getSupportFragmentManager().putFragment(outState, ACTUALITE_FRAGMENT_TAG, mActualiteFragment);
            }
        }
        if (gestionColisFragment != null) {
            if (getSupportFragmentManager().findFragmentByTag(GESTION_FRAGMENT_TAG) != null) {
                getSupportFragmentManager().putFragment(outState, GESTION_FRAGMENT_TAG, gestionColisFragment);
            }
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
                FirebaseService.getUsersRef().addListenerForSingleValueEvent(valueEventListener);
            }
            if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, R.string.sign_in_cancelled, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_suivi_colis, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem refresh = menu.findItem(R.id.nav_refresh);
        if (refresh != null) {
            if (NetworkReceiver.checkConnection(this)) {
                refresh.setVisible(true);
            } else {
                refresh.setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        } else if (i == R.id.nav_refresh && NetworkReceiver.checkConnection(this)) {
            SyncTask syncTask;
            if (mColisSelected != null) {
                syncTask = new SyncTask(SyncTask.TypeTask.SOLO, this, mColisSelected.getIdColis());
            } else {
                syncTask = new SyncTask(SyncTask.TypeTask.ALL, this);
            }
            syncTask.execute();
        }
        return super.onOptionsItemSelected(item);
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
        if (dialog.getTag() != null) {
            switch (dialog.getTag()) {
                case DIALOG_TAG_DELETE:
                    // Récupération du bundle qu'on a envoyé au NoticeDialogFragment
                    if (dialog.getBundle() != null && dialog.getBundle().containsKey(ARG_NOTICE_BUNDLE_COLIS)) {

                        // Récupération du colis présent dans le bundle
                        ColisEntity colisEntity = dialog.getBundle().getParcelable(ARG_NOTICE_BUNDLE_COLIS);
                        if (colisEntity != null) {

                            // Suppression du colis dans la base de données
                            ColisService.delete(this, colisEntity.getIdColis());

                            // Si on a une connexion, on supprime le colis sur le réseau.
                            if (NetworkReceiver.checkConnection(this)) {
                                CoreSync.deleteTracking(this, colisEntity);
                            }
                        }
                    }
                    break;
                case DIALOG_TAG_EXIT:
                    finish();
                    break;
            }
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

    @Override
    public void onNetworkEnable() {
        invalidateOptionsMenu();
        new SyncTask(SyncTask.TypeTask.ALL, this).execute();
    }

    @Override
    public void onNetworkDisable() {
        invalidateOptionsMenu();
    }
}
