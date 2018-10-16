package nc.opt.mobile.optmobile.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.database.local.entity.ColisEntity;
import nc.opt.mobile.optmobile.ui.NoticeDialogFragment;
import nc.opt.mobile.optmobile.ui.activity.viewmodel.MainActivityViewModel;
import nc.opt.mobile.optmobile.ui.fragment.MainActivityFragment;

import static nc.opt.mobile.optmobile.ui.fragment.MainActivityFragment.ARG_NOTICE_BUNDLE_COLIS;
import static nc.opt.mobile.optmobile.ui.fragment.MainActivityFragment.ARG_NOTICE_BUNDLE_POSITION;
import static nc.opt.mobile.optmobile.ui.fragment.MainActivityFragment.DIALOG_TAG_DELETE;
import static nc.opt.mobile.optmobile.ui.fragment.MainActivityFragment.DIALOG_TAG_DELIVERED;

public class MainActivity extends AppCompatActivity implements NoticeDialogFragment.NoticeDialogListener {

    public static final String TAG_MASTER_FRAGMENT = "TAG_MASTER_FRAGMENT";
    public static final String TAG_DETAIL_FRAGMENT = "TAG_DETAIL_FRAGMENT";

    private Fragment masterFragment;
    private Fragment detailFragment;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        setContentView(R.layout.activity_main);
        viewModel.setTwoPane(findViewById(R.id.frame_detail) != null);

        Fragment fragInMasterFrame = getSupportFragmentManager().findFragmentById(R.id.frame_master);

        // Manage the fragments
        masterFragment = getSupportFragmentManager().findFragmentByTag(TAG_MASTER_FRAGMENT);
        if (masterFragment == null) {
            masterFragment = new MainActivityFragment();
        }

        if (fragInMasterFrame == null || !fragInMasterFrame.equals(masterFragment)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_master, masterFragment, TAG_MASTER_FRAGMENT)
                    .commit();
        }

        detailFragment = getSupportFragmentManager().findFragmentByTag(TAG_DETAIL_FRAGMENT);
        if (detailFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(detailFragment).commit();
            getSupportFragmentManager().executePendingTransactions();
            if (viewModel.isTwoPane()) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_detail, detailFragment, TAG_DETAIL_FRAGMENT).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_master, detailFragment, TAG_DETAIL_FRAGMENT).addToBackStack(null).commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCount >=1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDialogPositiveClick(NoticeDialogFragment dialog) {
        if (dialog.getTag() != null && dialog.getTag().equals(DIALOG_TAG_DELETE) && dialog.getBundle() != null && dialog.getBundle().containsKey(ARG_NOTICE_BUNDLE_COLIS)) {
            ColisEntity colisEntity = dialog.getBundle().getParcelable(ARG_NOTICE_BUNDLE_COLIS);
            if (colisEntity != null) {
                viewModel.markAsDeleted(colisEntity);
            }
        }
        if (dialog.getTag() != null && dialog.getTag().equals(DIALOG_TAG_DELIVERED) && dialog.getBundle() != null && dialog.getBundle().containsKey(ARG_NOTICE_BUNDLE_COLIS)) {
            ColisEntity colisEntity = dialog.getBundle().getParcelable(ARG_NOTICE_BUNDLE_COLIS);
            int position = dialog.getBundle().getInt(ARG_NOTICE_BUNDLE_POSITION);
            if (colisEntity != null) {
                viewModel.markAsDelivered(colisEntity);
                viewModel.notifyItemChanged(position);
            }
        }
    }

    @Override
    public void onDialogNegativeClick(NoticeDialogFragment dialog) {
        if (dialog.getTag() != null && (dialog.getTag().equals(DIALOG_TAG_DELETE) || dialog.getTag().equals(DIALOG_TAG_DELIVERED))) {
            int position = dialog.getBundle().getInt(ARG_NOTICE_BUNDLE_POSITION);
            viewModel.notifyItemChanged(position);
        }
    }
}
