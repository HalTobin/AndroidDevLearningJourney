package com.example.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.model.Workmate;
import com.example.go4lunch.query.FirestoreCollectionFromRestaurantLiveData;
import com.example.go4lunch.query.FirestoreCollectionLiveData;
import com.example.go4lunch.query.FirestoreDocumentLiveData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WorkmateRepository {

    private static volatile WorkmateRepository instance;

    private WorkmateRepository() { }

    public static WorkmateRepository getInstance() {
        WorkmateRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(WorkmateRepository.class) {
            if (instance == null) {
                instance = new WorkmateRepository();
            }
            return instance;
        }
    }

    // Get the id that correspond to the Workmate's document associated to the current user
    public String getCurrentUserUID() { return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(); }

    // Get the workmate's collection
    private CollectionReference getWorkmatesCollection() {
        return FirebaseFirestore.getInstance().collection(Workmate.COLLECTION_NAME);
    }

    // Get current workmate's data from Firestore as LiveData
    public LiveData<Workmate> getCurrentWorkmate() {
        return new FirestoreDocumentLiveData<>(this.getWorkmatesCollection().document(this.getCurrentUserUID()));
    }

    // Get a workmate's data from its id as LiveData
    public LiveData<Workmate> getWorkmate(String id) {
        return new FirestoreDocumentLiveData<>(this.getWorkmatesCollection().document(id));
    }

    // Get the list of workmate as LiveData
    public LiveData<List<Workmate>> getWorkmatesList() {
        return new FirestoreCollectionLiveData<>(this.getWorkmatesCollection());
    }

    // Get the list of workmate who eats at a specific restaurant as Task
    public Task<QuerySnapshot> getWorkmatesFromRestaurantAsTask(String restaurantId) {
        return this.getWorkmatesCollection().whereEqualTo("restaurantUrl", restaurantId).get();
    }

    // Get the list of workmate who eats at a specific restaurant as LiveData
    public LiveData<List<Workmate>> getWorkmatesFromRestaurant(String restaurandId) {
        return new FirestoreCollectionFromRestaurantLiveData<>(this.getWorkmatesCollection(), restaurandId);
    }

    // Update the fields that indicate where a workmate is planning to eat
    public void updateWorkmateRestaurant(String restaurantName, String restaurantUrl) {
        Map<String, String> workmateMap = new HashMap<>();
        workmateMap.put(Workmate.RESTAURANT_NAME_FIELD, restaurantName);
        workmateMap.put(Workmate.RESTAURANT_URL_FIELD, restaurantUrl);
        this.getWorkmatesCollection().document(getCurrentUserUID()).set(workmateMap, SetOptions.merge());
    }

    // Get the data of the current user as Task
    public Task<DocumentSnapshot> getCurrentWorkmateData() {
        return this.getWorkmatesCollection().document(getCurrentUserUID()).get();
    }

    // Add a restaurant to favorite
    public void addFavorite(String restaurantId) {
        this.getWorkmatesCollection().document(getCurrentUserUID()).update(Workmate.FAVORITE_RESTAURANT_URL, FieldValue.arrayUnion(restaurantId));
    }

    // Remove a restaurant from favorite
    public void removeFavorite(String restaurantId) {
        this.getWorkmatesCollection().document(getCurrentUserUID()).update(Workmate.FAVORITE_RESTAURANT_URL, FieldValue.arrayRemove(restaurantId));
    }

    public void searchWorkmatesAction(String string) {
        Log.i("Searching for workmate", string);
    }

}
