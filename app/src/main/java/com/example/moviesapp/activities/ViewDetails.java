package com.example.moviesapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moviesapp.MainActivity;
import com.example.moviesapp.R;
import com.example.moviesapp.api.MoviesService;
import com.example.moviesapp.api.TrendingService;
import com.example.moviesapp.models.GenresItem;
import com.example.moviesapp.models.Movies;
import com.example.moviesapp.models.Trending;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewDetails extends AppCompatActivity {

    ImageView movieBackdrop;
    ImageView moviePoster;
    ProgressBar progressBar;
    TextView movieTitle;
    TextView movieRating;
    TextView movieTagline;
    TextView movieGenres;
    TextView movieOverview;
    TextView movieRuntime;
    TextView movieBudget;
    TextView movieRevenue;
    TextView movieHomepage;
    Button shareButton;

    Movies movies;

    String genres = "";

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);


        movieBackdrop = findViewById(R.id.movieBackdrop);
        moviePoster = findViewById(R.id.moviePoster);
        progressBar = findViewById(R.id.detailsProgressBar);
        movieTitle = findViewById(R.id.movieTitle);
        movieRating = findViewById(R.id.movieRating);
        movieTagline = findViewById(R.id.movieTagline);
        movieGenres = findViewById(R.id.movieGenres);
        movieOverview = findViewById(R.id.detailsTextView);
        movieRuntime = findViewById(R.id.runtimeTextView);
        movieBudget = findViewById(R.id.budgetTextView);
        movieRevenue = findViewById(R.id.revenueTextView);
        movieHomepage = findViewById(R.id.homepageTextView);


        progressBar.setVisibility(View.VISIBLE);


        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

        if (appLinkData != null) {
            String movieId = appLinkData.getLastPathSegment();
            movieNetworkCall(Integer.parseInt(movieId));
        }

        if (appLinkAction != Intent.ACTION_VIEW) {
            id = getIntent().getExtras().getInt("id");
            movieNetworkCall(id);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        shareButton = findViewById(R.id.shareButton);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivityAndGotoHome();
    }

    private void finishActivityAndGotoHome() {
        finish();
        Intent i = new Intent(ViewDetails.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareButton:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Movie Details");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey! I liked this movie " +
                        movies.getTitle() +
                        ".\nCheck it out on MoviesApp.\n"
                        + "http://moviesapp.com/"
                        + id
                        + "/");
                startActivity(Intent.createChooser(shareIntent, "Share using"));
                break;
            case android.R.id.home:
                finishActivityAndGotoHome();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String url = intent.getDataString();
        Log.d("url", url);
        url = url.substring(0, url.length() - 1);
        String tag[] = url.split("/");
        id = Integer.parseInt(tag[tag.length - 1]);
        movieNetworkCall(id);
    }

    void movieNetworkCall(int id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesService moviesService = retrofit.create(MoviesService.class);
        Call<Movies> moviesCall = moviesService.getMovies(id, getString(R.string.api_key));
        moviesCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                movies = response.body();
                movieTitle.setText(movies.getOriginalTitle());
                movieRating.setText(String.valueOf(movies.getVoteAverage()));
                movieTagline.setText(movies.getTagline());
                movieOverview.setText(movies.getOverview());
                movieRuntime.setText(String.valueOf(movies.getRuntime()));
                movieBudget.setText("$ " + movies.getBudget());
                movieRevenue.setText("$ " + movies.getRevenue());
                movieHomepage.setText(movies.getHomepage());
                movieHomepage.setMovementMethod(LinkMovementMethod.getInstance());

                for (GenresItem genresItem : movies.getGenres()) {
                    genres += genresItem.getName() + " | ";
                }

                String result = "";
                if ((genres != null) && (genres.length() > 0)) {
                    result = genres.substring(0, genres.length() - 2);
                }

                movieGenres.setText(result);


                Glide.with(ViewDetails.this).load("https://image.tmdb.org/t/p/w1280" + movies.getBackdropPath()).into(movieBackdrop);
                Glide.with(ViewDetails.this).load("https://image.tmdb.org/t/p/w342" + movies.getPosterPath()).into(moviePoster);


                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.d("log", "fail");
            }
        });

    }
}
