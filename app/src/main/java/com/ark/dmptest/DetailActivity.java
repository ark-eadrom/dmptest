package com.ark.dmptest;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    ProgressBar pbLoading;
    ImageView ivLogo, ivBack;
    TextView tvCompanyName, tvCompanyLocation, tvGoToWebsite, tvJobName, tvJobFullTime, tvJobDesc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        pbLoading = findViewById(R.id.pbLoading);
        ivBack = findViewById(R.id.ivBack);
        ivLogo = findViewById(R.id.ivLogo);
        tvCompanyName = findViewById(R.id.tvCompanyName);
        tvCompanyLocation = findViewById(R.id.tvCompanyLocation);
        tvGoToWebsite = findViewById(R.id.tvGoToWebsite);
        tvJobName = findViewById(R.id.tvJobName);
        tvJobFullTime = findViewById(R.id.tvJobFullTime);
        tvJobDesc = findViewById(R.id.tvJobDesc);

        String jobId = getIntent().getStringExtra("jobId");

        pbLoading.setVisibility(View.VISIBLE);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Job> call = apiInterface.getJobDetail(jobId);
        call.enqueue(new Callback<Job>() {
            @Override
            public void onResponse(Call<Job> call, Response<Job> response) {
                pbLoading.setVisibility(View.GONE);
                try {
                    Job job = response.body();
                    Glide.with(DetailActivity.this).load(job.url).into(ivLogo);
                    tvCompanyName.setText(job.company);
                    tvCompanyLocation.setText(job.location);
                    String value = "<a href=\"" + job.url + "\">Go To Website</a>";
                    tvGoToWebsite.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY));
                    tvGoToWebsite.setMovementMethod(LinkMovementMethod.getInstance());
                    tvJobName.setText(job.title);
                    tvJobFullTime.setText(job.type);
                    tvJobDesc.setText(Html.fromHtml(job.description, Html.FROM_HTML_MODE_LEGACY));
                }
                catch (Exception e) { e.printStackTrace(); }
            }

            @Override
            public void onFailure(Call<Job> call, Throwable t) {
                pbLoading.setVisibility(View.GONE);
            }
        });

        ivBack.setOnClickListener(view -> finish());
    }
}
