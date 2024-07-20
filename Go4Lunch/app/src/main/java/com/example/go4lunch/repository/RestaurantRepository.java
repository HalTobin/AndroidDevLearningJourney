package com.example.go4lunch.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.api.GooglePlacesApi;
import com.example.go4lunch.api.RetrofitClient;
import com.example.go4lunch.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RestaurantRepository {

    private static volatile RestaurantRepository instance;

    private String API_KEY = "NONE";
    final String REQUEST = " https://maps.googleapis.com/maps/api/place/photo?photo_reference=";

    private final MutableLiveData<List<Restaurant>> listOfRestaurantsFromLocation = new MutableLiveData<>();
    private final MutableLiveData<List<Restaurant>> listOfRestaurantsFromSearch = new MutableLiveData<>();
    private final MutableLiveData<Restaurant> detailsOfRestaurant = new MutableLiveData<>();

    Retrofit retrofit;
    GooglePlacesApi googlePlacesApi;

    WorkmateRepository workmateRepository;

    private RestaurantRepository() {
        // Initialize the Retrofit's Client
        retrofit = RetrofitClient.getClient();
        googlePlacesApi = retrofit.create(GooglePlacesApi.class);

        workmateRepository = WorkmateRepository.getInstance();
    }

    // Get restaurant's from location as LiveData
    public MutableLiveData<List<Restaurant>> getRestaurantsFromLocation(double viewLatitude, double viewLongitude, double userLatitude, double userLongitude) {
        final String location = viewLatitude + "," + viewLongitude;

        // The query is send only if the api key has been retrieved
        if(!API_KEY.equals("NONE")) {
            // Send a query to find restaurants nearby
            Call<List<Restaurant>> call = googlePlacesApi.getRestaurants(location, "35", "restaurant", API_KEY);
            call.enqueue(new Callback<List<Restaurant>>() {
                @Override
                public void onResponse(@NonNull Call<List<Restaurant>> call, @NonNull Response<List<Restaurant>> response) {
                    if(!response.isSuccessful()) Log.e("API ERROR", "" + response.code());
                    else {
                        assert response.body() != null;
                        for(Restaurant cRestaurant : response.body()) {
                            // Send a request to retrieve the detail of a restaurant for each result of the "nearbysearch" request
                            Call<Restaurant> subCall = googlePlacesApi.getDetails(cRestaurant.getId(), API_KEY, "name,opening_hours,formatted_phone_number,website,place_id,geometry,vicinity,photo,rating");
                            subCall.enqueue(new Callback<Restaurant>() {
                                @Override
                                public void onResponse(@NonNull Call<Restaurant> subCall, @NonNull Response<Restaurant> subResponse) {
                                    // Complete the information of the Restaurant object with data of RestaurantDetails object
                                    if(subResponse.body() != null) {
                                        cRestaurant.setNextClosure(subResponse.body().getNextClosure());
                                        cRestaurant.setPhoneNumber(subResponse.body().getPhoneNumber());
                                        cRestaurant.setWebURL(subResponse.body().getWebURL());
                                        listOfRestaurantsFromLocation.setValue(response.body());
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<Restaurant> subCall, @NonNull Throwable subT) {
                                    //Log.e("REQUEST ERROR", subT.getMessage());
                                    Log.e("REQUEST ERROR", "ERROR");
                                }
                            });
                            // Set the URL of the image of a restaurant
                            cRestaurant.setImageURL(REQUEST + cRestaurant.getImageReference() + "&key=" + API_KEY);
                            // Calculates the distance between the user and the restaurant
                            cRestaurant.setDistance((int) calculateDistance(userLatitude, userLongitude, cRestaurant.getLatitude(), cRestaurant.getLongitude()));
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Restaurant>> call, @NonNull Throwable t) {
                    Log.e("API ERROR ", t.getMessage());
                    listOfRestaurantsFromLocation.postValue(null);
                }
            });
        }
        return listOfRestaurantsFromLocation;
    }

    // Get details of a restaurant (from its id) as LiveData
    public MutableLiveData<Restaurant> getRestaurantDetails(String placeId) {
        // The query is send only if the api key has been retrieved
        if(!API_KEY.equals("NONE")) {
            // Send the query to get restaurant's details
            Call<Restaurant> call = googlePlacesApi.getDetails(placeId, API_KEY, "name,opening_hours,formatted_phone_number,website,place_id,geometry,vicinity,photo,rating");
            call.enqueue(new Callback<Restaurant>() {
                @Override
                public void onResponse(@NonNull Call<Restaurant> call, @NonNull Response<Restaurant> response) {
                    assert response.body() != null;
                    response.body().setImageURL(REQUEST + response.body().getImageReference() + "&key=" + API_KEY);
                    detailsOfRestaurant.setValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<Restaurant> subCall, @NonNull Throwable subT) {
                    Log.e("DETAILS ERROR", subT.getMessage());
                }
            });
        }
        return detailsOfRestaurant;
    }

    // Used to calculate the distance between the user and a restaurant
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6378.137; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d * 1000; // Return meters
    }

    // Get restaurant's from a user's search
    public MutableLiveData<List<Restaurant>> getRestaurantsFromSearch(double viewLatitude, double viewLongitude, double userLatitude, double userLongitude, String search) {
        final String location = viewLatitude + "," + viewLongitude;
        listOfRestaurantsFromSearch.setValue(new ArrayList<>());

        // The query is send only if the api key has been retrieved and if the text contains more than three characters
        if(!API_KEY.equals("NONE")&&(search.length() > 3)) {
            Log.i("API", "sending query from text...");

            // Send the query to find restaurant's with text
            Call<List<Restaurant>> call = googlePlacesApi.getRestaurantsFromSearch(search, location, "35", API_KEY);
            call.enqueue(new Callback<List<Restaurant>>() {
                @Override
                public void onResponse(@NonNull Call<List<Restaurant>> call, @NonNull Response<List<Restaurant>> response) {
                    List<Restaurant> predictions = new ArrayList<>();
                    if(!response.isSuccessful()) Log.e("API ERROR","" + response.code());
                    else {
                        if(response.body()!=null) {
                            for (Restaurant cRestaurant : response.body()) {
                                if (cRestaurant.isARestaurant()) {
                                    Log.i("API", "sending sub request...");
                                    // Send a request to retrieve the detail of a restaurant for each result of the "autocomplete" request
                                    Call<Restaurant> subCall = googlePlacesApi.getDetails(cRestaurant.getId(), API_KEY, "name,opening_hours,formatted_phone_number,website,place_id,geometry,vicinity,photo,rating");
                                    subCall.enqueue(new Callback<Restaurant>() {
                                        @Override
                                        public void onResponse(@NonNull Call<Restaurant> subCall, @NonNull Response<Restaurant> subResponse) {
                                            // Complete the information of the Restaurant object with data of the details query
                                            if (subResponse.body() != null) {
                                                subResponse.body().setImageURL(REQUEST + subResponse.body().getImageReference() + "&key=" + API_KEY);
                                                subResponse.body().setDistance((int) calculateDistance(userLatitude, userLongitude, subResponse.body().getLatitude(), subResponse.body().getLongitude()));
                                                predictions.add(subResponse.body());
                                                listOfRestaurantsFromSearch.setValue(predictions);
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<Restaurant> subCall, @NonNull Throwable subT) {
                                            Log.e("REQUEST ERROR", subT.getMessage());
                                        }
                                    });
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Restaurant>> call, @NonNull Throwable t) {

                }
            });
        }

        return listOfRestaurantsFromSearch;
    }

    public static RestaurantRepository getInstance() {
        RestaurantRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(RestaurantRepository.class) {
            if (instance == null) {
                instance = new RestaurantRepository();
            }
            return instance;
        }
    }

    public void setApiKey(String apiKey) {
        this.API_KEY = apiKey;
    }

}
