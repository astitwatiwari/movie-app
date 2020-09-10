package com.example.moviesapp.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.moviesapp.R;
import com.example.moviesapp.adapters.TopMoviesPaginationAdapter;
import com.example.moviesapp.api.TopMoviesService;
import com.example.moviesapp.models.ResultsItem;
import com.example.moviesapp.models.TopMovies;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TopMoviesPaginationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TopMoviesPaginationAdapter paginationAdapter;
    GridLayoutManager gridLayoutManager;

    static int page = 1;
    public int TOTAL_PAGES;
    TopMovies topMovies;
    List<ResultsItem> list = new ArrayList<>();

    ProgressBar activityProgressBar;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            gridLayoutManager.setSpanCount(2);
        else
            gridLayoutManager.setSpanCount(1);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_movies_pagination);

        recyclerView = findViewById(R.id.paginationRecyclerId);
        activityProgressBar = findViewById(R.id.paginationLoadingProgressBarId);
        activityProgressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        paginationAdapter = new TopMoviesPaginationAdapter(TopMoviesPaginationActivity.this, list);
        recyclerView.setAdapter(paginationAdapter);

        paginationAdapter.setOnBottomReachedListener(position -> {
            if (page <= TOTAL_PAGES)
                fetchData(position);
        });


        TopMoviesService topMoviesService = retrofit.create(TopMoviesService.class);
        Call<TopMovies> call = topMoviesService.getTopMovies(getString(R.string.api_key), "en-US", 1);
        page++;
        call.enqueue(new Callback<TopMovies>() {
            @Override
            public void onResponse(Call<TopMovies> call, Response<TopMovies> response) {
                Log.d("movie", String.valueOf(response.code()));
                if (!response.isSuccessful()) {
                    Log.d("movie", "unsuccessful");
                    return;
                }
                Log.d("movie", "successful");
                topMovies = response.body();
                TOTAL_PAGES = topMovies.getTotalPages();
                list.addAll(topMovies.getResults());
                paginationAdapter.setList(list);
                paginationAdapter.notifyDataSetChanged();
                activityProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TopMovies> call, Throwable t) {
            }
        });


    }

    private void fetchData(int position) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TopMoviesService topMoviesService = retrofit.create(TopMoviesService.class);

        Call<TopMovies> call = topMoviesService.getTopMovies(getString(R.string.api_key), "en-US", page);
        page++;
        call.enqueue(new Callback<TopMovies>() {
            @Override
            public void onResponse(Call<TopMovies> call, Response<TopMovies> response) {
                Log.d("movie", String.valueOf(response.code()));
                if (!response.isSuccessful()) {
                    return;
                }
                topMovies = response.body();

                list.remove(position);
                list.addAll(topMovies.getResults());

                paginationAdapter.setList(list);
                paginationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TopMovies> call, Throwable t) {

            }
        });


    }
}
