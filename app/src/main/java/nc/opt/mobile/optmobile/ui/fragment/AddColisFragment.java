package nc.opt.mobile.optmobile.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.Utilities;
import nc.opt.mobile.optmobile.barcodreader.BarcodeCaptureActivity;
import nc.opt.mobile.optmobile.broadcast.NetworkReceiver;
import nc.opt.mobile.optmobile.ui.fragment.viewmodel.AddColisFragmentViewModel;

import static nc.opt.mobile.optmobile.ui.activity.AddColisActivity.RC_BARCODE_CAPTURE;

public class AddColisFragment extends Fragment {

    public static final String ARG_ID_COLIS = "ARG_ID_COLIS";

    private AddColisFragmentViewModel viewModel;
    private AppCompatActivity appCompatActivity;
    @BindView(R.id.edit_id_parcel)
    EditText editIdParcel;
    private boolean hasCamera;

    @BindView(R.id.edit_description_parcel)
    EditText editDescriptionParcel;

    @BindView(R.id.coordinator_add_colis)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.img_scan)
    ImageView imageScan;


    public AddColisFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        appCompatActivity = (AppCompatActivity) context;
    }

    public static AddColisFragment getInstance(@Nullable String idColis) {
        AddColisFragment addColisFragment = new AddColisFragment();
        if (idColis != null) {
            Bundle args = new Bundle();
            args.putString(ARG_ID_COLIS, idColis);
            addColisFragment.setArguments(args);
        }
        return addColisFragment;
    }

    @OnClick(R.id.img_scan)
    public void launchScanner(View v) {
        Intent intent = new Intent(getActivity(), BarcodeCaptureActivity.class);
        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
        intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

    @OnClick(R.id.fab_add_colis)
    public void addColisCallbackListener(View v) {
        // On cache le clavier
        Utilities.hideKeyboard(appCompatActivity);

        if (!editIdParcel.getText().toString().isEmpty()) {
            String idColis = editIdParcel.getText().toString();
            String description = (editDescriptionParcel.getText() != null) ? editDescriptionParcel.getText().toString() : null;

            viewModel.findBy(idColis)
                    .doOnComplete(() -> callVmToCreateColis(idColis, description))
                    .doOnSuccess(colisEntity -> {
                        if (colisEntity.isDeleted()) {
                            viewModel.deleteColis(idColis);
                            callVmToCreateColis(idColis, description);
                        } else {
                            Snackbar.make(coordinatorLayout, String.format("Le colis %s est déjà suivi", idColis), Snackbar.LENGTH_LONG).show();
                        }
                    })
                    .doOnError(throwable -> Log.e(throwable.getMessage(), "C'est la merde"))
                    .subscribe();
        }
    }

    private void callVmToCreateColis(String idColis, String description) {
        viewModel.saveColis(idColis, description);
        if (NetworkReceiver.checkConnection(appCompatActivity)) {
            viewModel.syncColis(idColis);
        }
        appCompatActivity.finish();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(appCompatActivity).get(AddColisFragmentViewModel.class);
        PackageManager pm = appCompatActivity.getPackageManager();
        hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_colis, container, false);
        ButterKnife.bind(this, rootView);
        editIdParcel.requestFocus();
        viewModel.getIdColis().observe(appCompatActivity, s -> {
            if (s != null) {
                editIdParcel.setText(s);
            }
        });
        imageScan.setVisibility(hasCamera ? View.VISIBLE : View.GONE);
        imageScan.setEnabled(hasCamera);
        return rootView;
    }
}
