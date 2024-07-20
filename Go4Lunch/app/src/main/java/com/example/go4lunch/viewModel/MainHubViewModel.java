package com.example.go4lunch.viewModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.R;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.Workmate;
import com.example.go4lunch.model.XY;
import com.example.go4lunch.repository.ProfileRepository;
import com.example.go4lunch.repository.RestaurantRepository;
import com.example.go4lunch.repository.WorkmateRepository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainHubViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final WorkmateRepository workmateRepository;
    private final ProfileRepository profileRepository;

    private String STARTUP = "NOT_STARTED";
    private final MutableLiveData<String> startupLive = new MutableLiveData<>();

    private double focusedLatitude = 0.;
    private double focusedLongitude = 0.;
    private final MutableLiveData<XY> focusedLocationLive = new MutableLiveData<>();

    private double userLatitude = 0.;
    private double userLongitude = 0.;

    private String userSearch = "";

    private boolean isFromLocation = true;
    private boolean isListeningLocation = false;
    private boolean isListeningText = false;

    // Used to merge the data from the workmateRepository and from the restaurantRepository
    MediatorLiveData<List<Restaurant>> liveDataMerger = new MediatorLiveData<>();

    public MainHubViewModel() {
        restaurantRepository = RestaurantRepository.getInstance();
        workmateRepository = WorkmateRepository.getInstance();
        profileRepository = ProfileRepository.getInstance();
        autoListen();
    }

    // Register a user on Firestore
    public void createWorkmate() { profileRepository.createWorkmate(); }

    // Get the logged user
    public FirebaseUser getCurrentUser(){
        return profileRepository.getCurrentUser();
    }

    // Get the logged user as LiveData
    public LiveData<FirebaseUser> getMyProfile() {
        return profileRepository.getMyProfile();
    }

    // Get the current user data as LiveData
    public LiveData<Workmate> getMyWorkmateData() { return profileRepository.getWorkmateData(); }

    // Log out the current user
    public Task<Void> signOut(Context context){
        return profileRepository.signOut(context);
    }

    @SuppressLint("NonConstantResourceId")
    public void searchAction(int tab, String text) {
        Log.i("My query", text);
        // The search query is send to the ViewModel that correspond to the current tab
        switch (tab) {
            case R.id.main_hub_menu_map:
                setSearch(text);
            case R.id.main_hub_menu_list:
                setSearch(text);
                break;
            case R.id.main_hub_menu_workmates:
                workmateRepository.searchWorkmatesAction(text);
                break;
            default:
                break;
        }
    }

    public void getRestaurantsFromLocation() {
        restaurantRepository.getRestaurantsFromLocation(focusedLatitude, focusedLongitude, userLatitude, userLongitude);
    }

    public LiveData<List<Restaurant>> getRestaurantsAndWorkmates() {
        return liveDataMerger;
    }

    public void getRestaurantsFromSearch() {
        restaurantRepository.getRestaurantsFromSearch(focusedLatitude, focusedLongitude, userLatitude, userLongitude, userSearch);
    }

    // Search the user's location and defined 'userLatitude' and 'userLongitude'
    @SuppressLint("MissingPermission")
    public void searchUserLocation(Context context) {
        try {
            // If the app is running in Test Mode, then we fix a specific location
            Class.forName ("androidx.test.espresso.Espresso");
            this.userLatitude = 48.8563;
            this.userLongitude = 2.3944;
            setFocusedLocation(this.userLatitude, this.userLongitude);
            Log.i("RUNNING", "TEST");
        } catch (ClassNotFoundException e) {
            Log.i("RUNNING","DEBUG / RELEASE");

            FusedLocationProviderClient myLocationClient = LocationServices.getFusedLocationProviderClient(context);
            myLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                @Override
                public boolean isCancellationRequested() {
                    return false;
                }
                @NonNull
                @Override
                public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                    return this;
                }
            }).addOnSuccessListener(location -> {
                if (location != null) {
                    this.userLatitude = location.getLatitude();
                    this.userLongitude = location.getLongitude();
                    setFocusedLocation(this.userLatitude, this.userLongitude);
                }
            });
        }

    }

    // Select a location without updating LiveData getFocusedLocation
    public void setLocation(double latitude, double longitude) {
        this.focusedLatitude = latitude;
        this.focusedLongitude = longitude;
        this.getRestaurantsFromLocation();
    }

    public void setSearch(String text) {
        isFromLocation = text.equals("");

        autoListen();
        this.userSearch = text;
        this.getRestaurantsFromSearch();
    }

    // Select a location
    public void setFocusedLocation(double latitude, double longitude) {
        setLocation(latitude, longitude);
        getFocusedLocation();
    }

    // Return the location used by RestaurantViewModel
    public LiveData<XY> getFocusedLocation() {
        XY focusedLocation = new XY(focusedLatitude, focusedLongitude);
        focusedLocationLive.setValue(focusedLocation);
        return focusedLocationLive;
    }

    // Indicates that the RestaurantViewModel has been fully initialized
    public LiveData<String> getIsStartedUp() {
        startupLive.setValue(STARTUP);
        return startupLive;
    }

    public void setStartedUp(String newState) {
        STARTUP = newState;
    }

    // Share the api key to the RestaurantRepository
    public void setApiKey(String apiKey) {
        restaurantRepository.setApiKey(apiKey);
    }

    // Add and remove listeners dynamically to avoid conflict between the search from location and the search from text
    public void autoListen() {
        if(isFromLocation) {
            if(isListeningText) stopListeningFromText();
            if(!isListeningLocation) listenFromLocation();
            isListeningLocation = true;
            isListeningText = false;
        }
        else {
            if(isListeningLocation) stopListeningFromLocation();
            if(!isListeningText) listenFromText();
            isListeningLocation = false;
            isListeningText = true;
        }
    }

    // Start searching for restaurants depending on the user's location
    public void listenFromLocation() {
        liveDataMerger.addSource(restaurantRepository.getRestaurantsFromLocation(focusedLatitude, focusedLongitude, userLatitude, userLongitude), restaurants -> liveDataMerger.addSource(workmateRepository.getWorkmatesList(), workmates -> {
            if(restaurants != null) {
                for (Restaurant cRestaurant : restaurants) {
                    cRestaurant.setWorkmateList(new ArrayList<>());
                    for (Workmate cWorkmate : workmates) {
                        if (cRestaurant.getId().equals(cWorkmate.getRestaurantUrl())) {
                            if(!cRestaurant.getWorkmateList().contains(cWorkmate)) cRestaurant.addWorkmate(cWorkmate);
                        }
                    }
                }
                liveDataMerger.setValue(restaurants);
            }
        }));
    }

    // Stop searching for restaurants depending on the user's location
    public void stopListeningFromLocation() {
        liveDataMerger.removeSource(restaurantRepository.getRestaurantsFromLocation(focusedLatitude, focusedLongitude, userLatitude, userLongitude));
    }

    // Start searching for restaurants depending on the user's search query
    public void listenFromText() {
        liveDataMerger.addSource(restaurantRepository.getRestaurantsFromSearch(focusedLatitude, focusedLongitude, userLatitude, userLongitude, userSearch), restaurants -> liveDataMerger.addSource(workmateRepository.getWorkmatesList(), workmates -> {
            if(restaurants != null) {
                for (Restaurant cRestaurant : restaurants) {
                    cRestaurant.setWorkmateList(new ArrayList<>());
                    for (Workmate cWorkmate : workmates) {
                        if (cRestaurant.getId().equals(cWorkmate.getRestaurantUrl())) {
                            if(!cRestaurant.getWorkmateList().contains(cWorkmate)) cRestaurant.addWorkmate(cWorkmate);
                        }
                    }
                }
                liveDataMerger.setValue(restaurants);
            }
        }));
    }

    // Stop searching for restaurants depending on the user's search query
    public void stopListeningFromText() {
        liveDataMerger.removeSource(restaurantRepository.getRestaurantsFromSearch(focusedLatitude, focusedLongitude, userLatitude, userLongitude, userSearch));
    }
}
