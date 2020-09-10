package com.example.moviesapp.api;


import com.example.moviesapp.models.Popular;
import com.example.moviesapp.models.TopMovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PopularService {

    @GET("movie/popular")
    Call<Popular> getPopularMovies(@Query("api_key") String apiKey);
}
