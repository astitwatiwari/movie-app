package com.example.moviesapp.api;


import com.example.moviesapp.models.TopMovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TopMoviesService {

    @GET("movie/top_rated")
    Call<TopMovies> getTopMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);
}
