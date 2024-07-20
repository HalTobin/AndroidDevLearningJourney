package com.example.go4lunch.api;

import static com.example.go4lunch.api.GetRestaurantAttributes.getIsARestaurant;
import static com.example.go4lunch.api.GetRestaurantAttributes.getIsPlaceOpen;
import static com.example.go4lunch.api.GetRestaurantAttributes.getMainText;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceAddress;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceId;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceImageReference;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceLatitude;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceList;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceLongitude;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceName;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlacePrediction;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceRating;

import android.util.Log;

import com.example.go4lunch.model.Restaurant;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

// This class is used to create a GsonConverter, it allow Retrofit to correctly parse the Restaurant's object
public class GetRestaurantListDeserializer implements JsonDeserializer<List<Restaurant>> {
    @Override
    public ArrayList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList restaurants = new ArrayList<>();
        final JsonObject jsonObject = json.getAsJsonObject();
        Log.i("API : RAW", json.toString());

        // Test if the Json came from a 'nearbysearch' or from an 'autocomplete' query
        if(jsonObject.get("results")!=null) {
            final JsonArray itemsJsonArray = getPlaceList(jsonObject);
            // Get the list of restaurants from a nearbysearch query
            assert itemsJsonArray != null;
            for (JsonElement jsonElement : itemsJsonArray) {
                try {
                    if(jsonElement!=null) {
                        Log.i("API : DETAIL", jsonElement.toString());
                        final JsonObject subJsonObject = jsonElement.getAsJsonObject();
                        // Get the attributes of a restaurant
                        restaurants.add(new Restaurant(getPlaceId(subJsonObject),
                                getPlaceName(subJsonObject),
                                getPlaceAddress(subJsonObject),
                                getPlaceLatitude(subJsonObject),
                                getPlaceLongitude(subJsonObject),
                                0,
                                getIsPlaceOpen(subJsonObject),
                                "UNKNOWN",
                                getPlaceImageReference(subJsonObject),
                                getPlaceRating(subJsonObject)));
                    }
                } catch (Exception e) {
                    Log.e("API : ERROR", e.getMessage());
                }
            }
        } else {
            final JsonArray itemsJsonArray = getPlacePrediction(jsonObject);
            // Get the list of restaurants from an autocomplete query
            assert itemsJsonArray != null;
            for (JsonElement jsonElement : itemsJsonArray) {
                try {
                    if(jsonElement!=null) {
                        Log.i("API : DETAIL", jsonElement.toString());
                        final JsonObject subJsonObject = jsonElement.getAsJsonObject();
                        restaurants.add(new Restaurant(getPlaceId(subJsonObject),
                                getMainText(subJsonObject), getIsARestaurant(subJsonObject)));
                    }
                } catch (Exception e) {
                    Log.e("API : ERROR", e.getMessage());
                }
            }
        }

        return restaurants;
    }

}
