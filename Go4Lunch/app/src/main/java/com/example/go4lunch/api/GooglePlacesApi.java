package com.example.go4lunch.api;

import com.example.go4lunch.model.Restaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesApi {

    // Query to get the details of a restaurant
    @GET("place/details/json")
    Call<Restaurant> getDetails(@Query("place_id") String placeId,
                                @Query("key") String key,
                                @Query("fields") String fields);

    // Query to get the list of restaurants near a location
    @GET("place/nearbysearch/json")
    Call<List<Restaurant>> getRestaurants(@Query("location") String location,
                                          @Query("radius") String radius,
                                          @Query("keyword") String keyword,
                                          @Query("key") String key);

    // Query to get the list of restaurants from a text
    @GET("place/autocomplete/json")
    Call<List<Restaurant>> getRestaurantsFromSearch(@Query("input") String input,
                                                    @Query("location") String location,
                                                    @Query("radius") String radius,
                                                    @Query("key") String key);

}
