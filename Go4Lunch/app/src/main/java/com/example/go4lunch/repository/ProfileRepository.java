package com.example.go4lunch.repository;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.Workmate;
import com.example.go4lunch.query.FirestoreDocumentLiveData;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileRepository {

    private static volatile ProfileRepository instance;

    private final MutableLiveData<FirebaseUser> currentUser = new MutableLiveData<>();

    private ProfileRepository() { }

    public static ProfileRepository getInstance() {
        ProfileRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(ProfileRepository.class) {
            if (instance == null) {
                instance = new ProfileRepository();
            }
            return instance;
        }
    }

    // Get the workmate's Collection Reference
    private CollectionReference getWorkmatesCollection(){
        return FirebaseFirestore.getInstance().collection(Workmate.COLLECTION_NAME);
    }

    // If user's is connected for the first time, a new workmate is create in Firestore's database
    public void createWorkmate() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            String firstName = user.getDisplayName();
            String avatarUrl = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;

            Map<String, String> workmateMap = new HashMap<>();
            workmateMap.put(Workmate.FIRST_NAME_FIELD, firstName);
            workmateMap.put(Workmate.AVATAR_URL_FIELD, avatarUrl);

            // If the workmate already exist in Firestore, we get his data
            this.getWorkmatesCollection().document(uid).set(workmateMap, SetOptions.merge());
        }
    }

    // Retrieve the data of a workmate
    public LiveData<Workmate> getWorkmateData() {
        return new FirestoreDocumentLiveData<>(this.getWorkmatesCollection().document(getCurrentUserUID()));
    }

    // Get data from the user who's using the app
    @Nullable
    public FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    // Get the id of the current user
    public String getCurrentUserUID() { return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(); }

    // Get the data of the current user
    public LiveData<FirebaseUser> getMyProfile() {
        currentUser.postValue(getCurrentUser());
        return currentUser;
    }

    // Allow a user to log out from the app
    public Task<Void> signOut(Context context){
        return AuthUI.getInstance().signOut(context);
    }

}
