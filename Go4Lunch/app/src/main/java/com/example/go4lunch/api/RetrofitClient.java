package com.example.go4lunch.api;

import com.example.go4lunch.model.Restaurant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";

    private static Retrofit retrofit = null;

    // This allow the use of custom deserializers (GetRestaurantListDeserializer and GetRestaurantDetailsDeserializer)
    private static Converter.Factory createGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(new TypeToken<List<Restaurant>>() {}.getType(), new GetRestaurantListDeserializer());
        gsonBuilder.registerTypeAdapter(new TypeToken<Restaurant>() {}.getType(), new GetRestaurantDetailsDeserializer());
        Gson gson = gsonBuilder.create();
        return GsonConverterFactory.create(gson);
    }

    public static Retrofit getClient() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder().
                    baseUrl(BASE_URL).
                    addConverterFactory(createGsonConverter()).
                    build();
        }
        return retrofit;
    }

}

