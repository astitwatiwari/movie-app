package com.example.moviesapp.api;

import com.example.moviesapp.models.Trending;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TrendingService {

    @GET("trending/movie/day")
    Call<Trending> getTrending(@Query("api_key") String api_key);
}
