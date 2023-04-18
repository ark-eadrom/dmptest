package com.ark.dmptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView tvName;
    Button btnSignOut;
    ProgressBar pbLoading;
    ConstraintLayout clAccount, clHome, clSearchMore, clHomeTab, clAccountTab;
    ImageView ivSearchMore;
    EditText edtSearch, edtLocation;
    SwitchMaterial switchFullTime;
    Button btnApplyFilter;

    RecyclerView rvJob;
    JobAdapter jobAdapter;
    ArrayList<Job> jobList = new ArrayList<>();
    boolean isLoading = false;
    Integer page = null;
    String location = null;
    String fullTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = findViewById(R.id.tvName);
        btnSignOut = findViewById(R.id.btnSignOut);
        pbLoading = findViewById(R.id.pbLoading);
        clAccount = findViewById(R.id.clAccount);
        clHome = findViewById(R.id.clHome);
        clSearchMore = findViewById(R.id.clSearchMore);
        clHomeTab = findViewById(R.id.clHomeTab);
        clAccountTab = findViewById(R.id.clAccountTab);
        ivSearchMore = findViewById(R.id.ivSearchMore);
        edtSearch = findViewById(R.id.edtSearch);
        edtLocation = findViewById(R.id.edtLocation);
        switchFullTime = findViewById(R.id.switchFullTime);
        btnApplyFilter = findViewById(R.id.btnApplyFilter);
        rvJob = findViewById(R.id.rvJob);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isFbLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isFbLoggedIn) {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    (object, response) -> {
                        try {
                            tvName.setText(object.getString("name"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link");
            request.setParameters(parameters);
            request.executeAsync();

            btnSignOut.setOnClickListener(v -> {
                LoginManager.getInstance().logOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            });
        }
        else {
            if (account == null) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
            else {
                tvName.setText(account.getDisplayName());
                btnSignOut.setOnClickListener(v -> {
                    gsc.signOut().addOnCompleteListener(task -> {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    });
                });
            }
        }

        clHomeTab.setOnClickListener(view -> {
            clHome.setVisibility(View.VISIBLE);
            clAccount.setVisibility(View.GONE);
        });
        clAccountTab.setOnClickListener(view -> {
            clAccount.setVisibility(View.VISIBLE);
            clHome.setVisibility(View.GONE);
        });
        ivSearchMore.setOnClickListener(view -> {
            if (clSearchMore.getVisibility() == View.VISIBLE) {
                clSearchMore.setVisibility(View.GONE);
                fullTime = null;
                location = null;
            }
            else clSearchMore.setVisibility(View.VISIBLE);
        });

        switchFullTime.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) fullTime = "true";
            else fullTime = "false";
        });

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getJob();
                return true;
            }
            return false;
        });

        btnApplyFilter.setOnClickListener(view -> {
            page = null;
            getJob();
        });

        jobAdapter = new JobAdapter(this, jobList);
        rvJob.setAdapter(jobAdapter);
        rvJob.setLayoutManager(new LinearLayoutManager(this));

        rvJob.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == jobList.size() - 1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });

        getJob();
    }

    private void getJob() {
        pbLoading.setVisibility(View.VISIBLE);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<Job>> call = apiInterface.getMoreJob(edtSearch.getText().toString(), location, fullTime, page);
        call.enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                pbLoading.setVisibility(View.GONE);
                jobList.clear();
                jobList.addAll(response.body());

                jobAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                pbLoading.setVisibility(View.GONE);
            }

        });
    }

    private void loadMore() {
        if (page == null) page = 1;
        else page++;
        getJob();
    }
}