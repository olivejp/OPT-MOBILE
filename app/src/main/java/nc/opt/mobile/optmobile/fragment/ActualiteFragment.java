package nc.opt.mobile.optmobile.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.adapter.ActualiteAdapter;
import nc.opt.mobile.optmobile.domain.suivi.ActualiteDto;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderObserver;
import nc.opt.mobile.optmobile.provider.entity.ActualiteEntity;
import nc.opt.mobile.optmobile.provider.services.ActualiteService;
import nc.opt.mobile.optmobile.utils.Constants;

/**
 * Fragment that shows list of news
 */
public class ActualiteFragment extends Fragment implements ProviderObserver.ProviderObserverListener {

    private static final String TAG = ActualiteFragment.class.getName();

    private static final String KEY_ACTUALITE = "actualites";

    private ActualiteAdapter mActualiteAdapter;
    private AppCompatActivity mActivity;

    @BindView(R.id.recycler_actualite)
    RecyclerView mRecyclerView;

    @BindView(R.id.text_explicatif_actualite)
    TextView textExplicatifActualite;

    private OnCompleteListener<QuerySnapshot> onCompleteListener = task -> {
        if (task.isSuccessful()) {
            for (DocumentSnapshot document : task.getResult()) {
                Log.d(TAG, document.getId() + " => " + document.getData());
            }
        } else {
            Log.w(TAG, "Error getting documents.", task.getException());
        }
    };

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            ActualiteDto actualiteDto = dataSnapshot.getValue(ActualiteDto.class);
            if (actualiteDto != null) {
                actualiteDto.setIdFirebase(dataSnapshot.getKey());
                if (!ActualiteService.existWithFirebaseId(mActivity, actualiteDto.getIdFirebase())) {
                    ActualiteService.insertActualite(mActivity, actualiteDto);
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            ActualiteDto actualiteDto = dataSnapshot.getValue(ActualiteDto.class);
            if (actualiteDto != null) {
                actualiteDto.setIdFirebase(dataSnapshot.getKey());
                ActualiteEntity entity = ActualiteService.getByFirebaseId(mActivity, actualiteDto.getIdFirebase());
                if (entity != null) {
                    actualiteDto.setIdActualite(String.valueOf(entity.getIdActualite()));
                    ActualiteService.updateActualite(mActivity, actualiteDto);
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            ActualiteDto actualiteDto = dataSnapshot.getValue(ActualiteDto.class);
            if (actualiteDto != null) {
                actualiteDto.setIdFirebase(dataSnapshot.getKey());
                if (ActualiteService.existWithFirebaseId(mActivity, actualiteDto.getIdFirebase())) {
                    ActualiteService.deleteByIdFirebase(mActivity, actualiteDto.getIdFirebase());
                }
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            // Do nothing
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Do nothing
        }
    };

    public static ActualiteFragment newInstance() {
        ActualiteFragment fragment = new ActualiteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ActualiteFragment() {
        // Required empty public constructor
    }

    private void attachDatabaseListener() {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        if (mFirebaseRemoteConfig.getBoolean(Constants.CLOUD_FIRESTORE)) {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection(KEY_ACTUALITE)
                    .get()
                    .addOnCompleteListener(onCompleteListener);
        }

        if (mFirebaseRemoteConfig.getBoolean(Constants.FIREBASE_DATABASE)) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.getReference().child(KEY_ACTUALITE).addChildEventListener(childEventListener);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onPause() {
        super.onPause();
        ProviderObserver.getInstance().unregister(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_actualite, container, false);
        ButterKnife.bind(this, rootView);

        // change title
        mActivity.setTitle(getActivity().getString(R.string.activity_title_actualite));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // get the list from the provider
        List<ActualiteEntity> list = ActualiteService.listActiveActualite(mActivity);
        mActualiteAdapter = new ActualiteAdapter(getContext(), list);
        mRecyclerView.setAdapter(mActualiteAdapter);
        mActualiteAdapter.notifyDataSetChanged();

        // change visibility depending on the list content
        textExplicatifActualite.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);

        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.observe(mActivity, this, OptProvider.ListActualite.LIST_ACTUALITE);

        attachDatabaseListener();

        return rootView;
    }

    @Override
    public void onProviderChange(Uri uri) {
        mActualiteAdapter.getmActualites().clear();
        mActualiteAdapter.getmActualites().addAll(ActualiteService.listActiveActualite(mActivity));
        mActualiteAdapter.notifyDataSetChanged();
    }
}
