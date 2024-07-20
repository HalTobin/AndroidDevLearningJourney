package com.example.go4lunch.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.Workmate;
import com.example.go4lunch.repository.RestaurantRepository;
import com.example.go4lunch.repository.WorkmateRepository;

import java.util.List;

public class RestaurantDetailsViewModel extends ViewModel {

    private final WorkmateRepository workmateRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantDetailsViewModel() {
        workmateRepository = WorkmateRepository.getInstance();
        restaurantRepository = RestaurantRepository.getInstance();
    }

    // Get the detail of a restaurant as LiveData
    public LiveData<Restaurant> getRestaurantDetails(String placeId) {
        return restaurantRepository.getRestaurantDetails(placeId);
    }

    // Get the list of workmate who eats at a specific restaurant as LiveData
    public LiveData<List<Workmate>> getWorkmatesFromRestaurant(String restaurantId) {
        return workmateRepository.getWorkmatesFromRestaurant(restaurantId);
    }

    // Update the 'restaurantName' and 'restaurantUrl' field of the current user
    public void updateWorkmateRestaurant(String restaurantName, String restaurantUrl) {
        workmateRepository.updateWorkmateRestaurant(restaurantName, restaurantUrl);
    }

    // Get the current user's data as LiveData
    public LiveData<Workmate> getCurrentWorkmate() {
        return workmateRepository.getCurrentWorkmate();
    }

    // Register if a user plans to eat at a restaurant, or if he changed his mind
    public void invertEatHere(String restaurantName, String restaurantId) {
        workmateRepository.getCurrentWorkmateData().addOnCompleteListener(task -> {
            // If the field restaurantUrl exist, the fields 'restaurantName' and 'restaurantUrl' are updated
            // Else the fields are created with the data of the selected restaurant
            if(task.getResult().get(Workmate.RESTAURANT_URL_FIELD) != null) {
                if(!task.getResult().get(Workmate.RESTAURANT_URL_FIELD).equals(restaurantId)) updateWorkmateRestaurant(restaurantName, restaurantId);
                else updateWorkmateRestaurant("", "");
            }
            else updateWorkmateRestaurant(restaurantName, restaurantId);
        });
    }

    // Update the list of favorite of the current user
    public void invertFavorite(String restaurantId) {
        workmateRepository.getCurrentWorkmateData().addOnCompleteListener(task -> {
            if(task.getResult().get(Workmate.FAVORITE_RESTAURANT_URL) != null) {
                List<String> group = (List<String>) task.getResult().get(Workmate.FAVORITE_RESTAURANT_URL);
                if(group.contains(restaurantId)) workmateRepository.removeFavorite(restaurantId);
                else workmateRepository.addFavorite(restaurantId);
            }
            else workmateRepository.addFavorite(restaurantId);
        });
    }

}
