package nc.opt.mobile.optmobile.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.network.RetrofitCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static nc.opt.mobile.optmobile.Constants.URL_SUIVI_COLIS;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchParcelFragment extends Fragment implements Callback<ResponseBody> {

    private static final String TAG = SearchParcelFragment.class.getName();

    @BindView(R.id.edit_id_parcel)
    EditText editIdParcel;

    @BindView(R.id.fab_search_parcel)
    FloatingActionButton fabSearchParcel;

    @BindView(R.id.layout_search)
    RelativeLayout layoutSearch;

    @BindView(R.id.layout_result)
    LinearLayout layoutResult;

    @BindView(R.id.web_result_view)
    WebView webResultView;

    public static SearchParcelFragment newInstance() {
        SearchParcelFragment fragment = new SearchParcelFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SearchParcelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_parcel, container, false);
        ButterKnife.bind(this, rootView);

        layoutSearch.setVisibility(View.VISIBLE);
        layoutResult.setVisibility(View.GONE);
        return rootView;
    }

    @OnClick(R.id.fab_search_parcel)
    public void searchParcel() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_SUIVI_COLIS)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitCall service = retrofit.create(RetrofitCall.class);
        String itemId = editIdParcel.getText().toString();
        String envoyer = "Envoyer";
        Call<ResponseBody> call = service.searchParcel(itemId, envoyer);
        call.enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            layoutSearch.setVisibility(View.GONE);
            layoutResult.setVisibility(View.VISIBLE);
            webResultView.setVisibility(View.VISIBLE);

            String mimeType = "text/html; charset=UTF-8";

            InputStream data = response.body().byteStream();

            // Get the result from RAW
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            try {
                while ((length = data.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            webResultView.getSettings().setJavaScriptEnabled(false);
            webResultView.getSettings().setBuiltInZoomControls(true);
            webResultView.setInitialScale(1);
            webResultView.getSettings().setLoadWithOverviewMode(true);
            webResultView.getSettings().setUseWideViewPort(true);
            webResultView.loadData(result.toString(), mimeType, null);


            // Solution 2
//            webResultView.loadUrl(URL_SUIVI_COLIS
//                    .concat(URL_SUIVI_SERVICE_OPT)
//                    .concat("?itemId=")
//                    .concat(editIdParcel.getText().toString())
//                    .concat("&Submit=Envoyer"));
        }
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        Log.e("test", t.getMessage(), t);
    }
}
