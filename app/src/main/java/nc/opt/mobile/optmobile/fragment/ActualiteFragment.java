package nc.opt.mobile.optmobile.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.adapter.ActualiteAdapter;
import nc.opt.mobile.optmobile.domain.ActualiteDto;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.ProviderObserver;
import nc.opt.mobile.optmobile.provider.entity.ActualiteEntity;
import nc.opt.mobile.optmobile.provider.services.ActualiteService;

/**
 * Fragment that shows list of news
 */
public class ActualiteFragment extends Fragment implements ProviderObserver.ProviderObserverListener {

    private static final String KEY_ACTUALITE = "actualites";

    private ActualiteAdapter mActualiteFragment;
    private AppCompatActivity mActivity;
    private ChildEventListener mChildEventListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @BindView(R.id.recycler_actualite)
    RecyclerView mRecyclerView;

    @BindView(R.id.text_explicatif_actualite)
    TextView textExplicatifActualite;

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

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ActualiteDto actualiteDto = dataSnapshot.getValue(ActualiteDto.class);
                ActualiteService.insertActualite(getActivity(), actualiteDto);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ActualiteDto actualiteDto = dataSnapshot.getValue(ActualiteDto.class);
                ActualiteService.updateActualite(getActivity(), actualiteDto);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ActualiteDto actualiteDto = dataSnapshot.getValue(ActualiteDto.class);
                ActualiteService.deleteActualite(getActivity(), actualiteDto);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabaseReference = mFirebaseDatabase.getReference().child(KEY_ACTUALITE);
        mDatabaseReference.addChildEventListener(childEventListener);
        mChildEventListener = childEventListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        attachDatabaseListener();

        // create a ActualiteObserver
        ArrayList<Uri> uris = new ArrayList<>();
        uris.add(OptProvider.ListActualite.LIST_ACTUALITE);

        // create a ProviderObserver to listen updates from the provider
        ProviderObserver providerObserver = ProviderObserver.getInstance();
        providerObserver.observe(mActivity, this, uris);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_actualite, container, false);
        ButterKnife.bind(this, rootView);

        // change title
        mActivity.setTitle(getActivity().getString(R.string.activity_title_actualite));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // get the list from the provider
        List<ActualiteEntity> list = ActualiteService.listFromProvider(mActivity);
        mActualiteFragment = new ActualiteAdapter(list);
        mRecyclerView.setAdapter(mActualiteFragment);
        mActualiteFragment.notifyDataSetChanged();

        // change visibility depending on the list content
        textExplicatifActualite.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);

        return rootView;
    }

    @Override
    public void onProviderChange() {
        mActualiteFragment.getmActualites().clear();
        mActualiteFragment.getmActualites().addAll(ActualiteService.listFromProvider(mActivity));
        mActualiteFragment.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
        }
    }
}
