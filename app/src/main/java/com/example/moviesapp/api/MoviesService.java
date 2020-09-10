package com.example.moviesapp.api;


import com.example.moviesapp.models.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesService {

    @GET("movie/{movie_id}")
    Call<Movies> getMovies(@Path("movie_id") int movieId, @Query("api_key") String apiKey);
}
