package com.example.moviesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.moviesapp.activities.MapsActivity;
import com.example.moviesapp.activities.ProfilePageActivity;
import com.example.moviesapp.activities.TopMoviesPaginationActivity;
import com.example.moviesapp.adapters.PopularMoviesRecyclerAdapter;
import com.example.moviesapp.adapters.ViewPagerAdapter;
import com.example.moviesapp.api.PopularService;
import com.example.moviesapp.api.TrendingService;
import com.example.moviesapp.models.Popular;
import com.example.moviesapp.models.ResultsItem;
import com.example.moviesapp.models.Trending;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    public ViewPagerAdapter pagerAdapter;
    private RecyclerView recyclerView;
    public PopularMoviesRecyclerAdapter recyclerAdapter;

    List<ResultsItem> trendingList = new ArrayList<>();
    List<ResultsItem> popularList = new ArrayList<>();

    Trending trending;
    Popular popular;
    Timer timer;

    int currentPage = 0;
    final long DELAY_MS = 0;
    final long PERIOD_MS = 5000;
    int ACCESS_LOCATION = 1;

    Button userProfile;
    Button mapsButton;
    ProgressBar progressBar;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardView = findViewById(R.id.topRatedCard);
        cardView.setOnClickListener(topRatedCardClickListener);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        //Setting Viewpager Adapter
        viewPager2 = findViewById(R.id.pager);
        pagerAdapter = new ViewPagerAdapter(MainActivity.this, trendingList);
        viewPager2.setAdapter(pagerAdapter);
        autoScroll();

        //Setting Recycler Adapter
        recyclerView = findViewById(R.id.popularRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerAdapter = new PopularMoviesRecyclerAdapter(this, popularList);
        recyclerView.setAdapter(recyclerAdapter);

        //Retrofit Object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //TrendingApi Call and Data handling
        TrendingService trendingService = retrofit.create(TrendingService.class);
        Call<Trending> trendingCall = trendingService.getTrending(getString(R.string.api_key));
        trendingCall.enqueue(new Callback<Trending>() {
            @Override
            public void onResponse(Call<Trending> call, Response<Trending> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                trending = response.body();
                trendingList.addAll(trending.getResults());
                pagerAdapter.setList(trendingList);
                pagerAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Trending> call, Throwable t) {
            }
        });

        //PopularApi Call and Data handling
        PopularService popularService = retrofit.create(PopularService.class);
        Call<Popular> popularCall = popularService.getPopularMovies(getString(R.string.api_key));
        popularCall.enqueue(new Callback<Popular>() {
            @Override
            public void onResponse(Call<Popular> call, Response<Popular> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                popular = response.body();
                popularList.addAll(popular.getResults());
                recyclerAdapter.setList(popularList);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Popular> call, Throwable t) {
            }
        });


    }

    private void autoScroll() {
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == trendingList.size() - 1) {
                currentPage = 0;
            }
            viewPager2.setCurrentItem(currentPage++, true);
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_menu, menu);
        userProfile = findViewById(R.id.userProfileButton);
        mapsButton = findViewById(R.id.map);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.userProfileButton:
                Intent userProfileIntent = new Intent(MainActivity.this, ProfilePageActivity.class);
                startActivity(userProfileIntent);
                return true;
            case R.id.mapsButton:
                openMap();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            openMap();
        } else{
            Toast.makeText(getApplicationContext(), "Location Permission is required", Toast.LENGTH_SHORT).show();
        }
    }

    View.OnClickListener topRatedCardClickListener = v -> {
        Intent i = new Intent(MainActivity.this, TopMoviesPaginationActivity.class);
        startActivity(i);
    };

    void openMap(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION);
        } else {
            Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(mapsIntent);
        }
    }

}
