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

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.constants;
import nc.opt.mobile.optmobile.network.RetrofitCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                .baseUrl(constants.URL_SUIVI_COLIS)
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

            String mimeType = "text/html";
            String encoding = "utf-8";

            String data = null;
            try {
                data = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.body() != null) {
                webResultView.loadData(data, mimeType, encoding);
            }

        }
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        Log.e("test", t.getMessage(), t);
    }
}
