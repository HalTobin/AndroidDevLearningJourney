package com.example.go4lunch.api;

import static com.example.go4lunch.api.GetRestaurantAttributes.getIsPlaceOpen;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceAddress;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceDetails;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceId;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceImageReference;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceLatitude;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceLongitude;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceName;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceNextClosure;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlacePhoneNumber;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceRating;
import static com.example.go4lunch.api.GetRestaurantAttributes.getPlaceWebsite;

import android.util.Log;

import com.example.go4lunch.model.Restaurant;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

// This class is used to create a GsonConverter, it allow Retrofit to correctly parse the restaurantDetails's object
public class GetRestaurantDetailsDeserializer implements JsonDeserializer<Restaurant> {
    @Override
    public Restaurant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Restaurant tempRestaurant = null;

        final JsonObject jsonObject = json.getAsJsonObject();

        try {
            final JsonObject resultJson = getPlaceDetails(jsonObject);
            // Get the attributes of a restaurant
            assert resultJson != null;
            tempRestaurant = new Restaurant(
                    getPlaceId(resultJson),
                    getPlaceName(resultJson),
                    getPlaceAddress(resultJson),
                    getPlaceLatitude(resultJson),
                    getPlaceLongitude(resultJson),
                    0,
                    getIsPlaceOpen(resultJson),
                    getPlaceNextClosure(resultJson),
                    getPlaceImageReference(resultJson),
                    getPlaceRating(resultJson),
                    getPlaceWebsite(resultJson),
                    getPlacePhoneNumber(resultJson));
        } catch (Exception e) {
            Log.e("API", "ERROR");
        }

        return tempRestaurant;
    }

}
