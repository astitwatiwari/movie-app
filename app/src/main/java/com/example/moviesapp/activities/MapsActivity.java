package com.example.moviesapp.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.moviesapp.R;
import com.example.moviesapp.api.PlacesService;
import com.example.moviesapp.api.TopMoviesService;
import com.example.moviesapp.models.Places.Place;
import com.example.moviesapp.models.Places.ResultsItem;
import com.example.moviesapp.models.TopMovies;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    LatLng val;
    Button places;
    Place place;
    GoogleMap googleMap;
    String locationString;
    FusedLocationProviderClient fusedLocationProviderClient;

    int RADIUS = 3000;

    List<ResultsItem> list = new ArrayList<ResultsItem>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        places = findViewById(R.id.nearbyPlacesButton);

        places.setOnClickListener(nearbyPlacesOnClickListener);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(17.446660, 78.363478), 13);
        googleMap.animateCamera(cameraUpdate);
        locationProvider(googleMap);
        //  fusedLocationProviderClient.requestLocationUpdates();

        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);


    }

    void locationProvider(GoogleMap googleMap) {
        fusedLocationProviderClient.getLocationAvailability().addOnSuccessListener(new OnSuccessListener<LocationAvailability>() {
            @Override
            public void onSuccess(LocationAvailability locationAvailability) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        task.addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    val = new LatLng(location.getLatitude(), location.getLongitude());
                                    googleMap.addMarker(new MarkerOptions().position(val));
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    View.OnClickListener nearbyPlacesOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fetchData();
        }
    };

    void fetchData() {

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    locationString = location.getLatitude() + "," + location.getLongitude();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(getString(R.string.places_base_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    PlacesService placesService = retrofit.create(PlacesService.class);

                    Call<Place> call = placesService.getPlaces(locationString, RADIUS, "movie_theater", getString(R.string.places_api_key));
                    call.enqueue(new Callback<Place>() {
                        @Override
                        public void onResponse(Call<Place> call, Response<Place> response) {
                            Log.d("fail", String.valueOf(response.code()));
                            if (!response.isSuccessful()) {
                                return;
                            }
                            place = response.body();

                            list = place.getResults();
                            for (ResultsItem resultsItem : list) {
                                double lat = resultsItem.getGeometry().getLocation().getLat();
                                double lng = resultsItem.getGeometry().getLocation().getLng();
                                String title = resultsItem.getName();
                                val = new LatLng(lat, lng);
                                googleMap.addMarker(new MarkerOptions()
                                        .position(val)
                                        .title(title).icon(BitmapDescriptorFactory.fromBitmap(getScaledBitmap())))
                                ;


                            }

                        }

                        @Override
                        public void onFailure(Call<Place> call, Throwable t) {
                            Log.d("fail", "fail");
                        }
                    });
                }
            }
        });


    }

    private Bitmap getScaledBitmap(){
        int height = 80;
        int width = 80;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_movie);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return smallMarker;
    }

}

