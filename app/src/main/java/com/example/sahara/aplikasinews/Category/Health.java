package com.example.sahara.aplikasinews.Category;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.sahara.aplikasinews.API.ApiService;
import com.example.sahara.aplikasinews.API.Server;
import com.example.sahara.aplikasinews.Adapter.NewsAdapter;
import com.example.sahara.aplikasinews.BuildConfig;
import com.example.sahara.aplikasinews.Entity.News;
import com.example.sahara.aplikasinews.Entity.ResponseNews;
import com.example.sahara.aplikasinews.MainActivity;
import com.example.sahara.aplikasinews.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Health extends AppCompatActivity {

    private RecyclerView news;
    private NewsAdapter adapter;
    List<News> list = new ArrayList<>();
    final String category = "health";
    ProgressDialog loading;
    ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        news  = findViewById(R.id.news);
        api = Server.getApiService();
        adapter = new NewsAdapter(Health.this, list);

        news.setHasFixedSize(true);
        news.setLayoutManager(new LinearLayoutManager(Health.this));
        news.setAdapter(adapter);
        refresh();

        //membuat back button toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public void refresh() {
        loading = new ProgressDialog(Health.this);
        loading.setCancelable(false);
        loading.setMessage("Loading...");
        showDialog();
        api.getListNews("id", category, BuildConfig.NEWS_API_TOKEN).enqueue(new Callback<ResponseNews>() {
            @Override
            public void onResponse(Call<ResponseNews> call, Response<ResponseNews> response) {
                if (response.isSuccessful()){
                    hideDialog();
                    list = response.body().getNewsList();
                    news.setAdapter(new NewsAdapter(Health.this, list));
                    adapter.notifyDataSetChanged();
                } else {
                    hideDialog();
                    Toast.makeText(Health.this, "Gagal mengambil data !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseNews> call, Throwable t) {
                hideDialog();
                Toast.makeText(Health.this, "Gagal menyambung ke internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog() {
        if (!loading.isShowing())
            loading.show();
    }

    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(Health.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Health.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
