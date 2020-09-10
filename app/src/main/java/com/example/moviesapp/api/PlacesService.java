package com.example.moviesapp.api;

import com.example.moviesapp.models.Places.Place;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesService {
    @GET("maps/api/place/nearbysearch/json")
    Call<Place> getPlaces(@Query("location") String location, @Query("radius") int radius, @Query("type") String type, @Query("key") String api_key);
}
