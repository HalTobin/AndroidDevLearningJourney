package com.example.go4lunch.query;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.model.Workmate;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

// Abstraction to get a Collection from Firestore (based on a restaurantId) as LiveData
public class FirestoreCollectionFromRestaurantLiveData<T> extends LiveData<T> {

    private ListenerRegistration registration;

    private final CollectionReference myCollectionReference;
    private final String restaurantId;

    public FirestoreCollectionFromRestaurantLiveData(CollectionReference myCollectionReference, String restaurantId) {
        this.myCollectionReference = myCollectionReference;
        this.restaurantId = restaurantId;
    }

    @SuppressWarnings("unchecked")
    EventListener<QuerySnapshot> eventListener = (queryDocumentSnapshots, e) -> {
        if (e != null) return;

        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
            List<T> itemList = new ArrayList<>();
            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                T item = (T) snapshot.toObject(Workmate.class);
                itemList.add(item);
            }
            setValue((T) itemList);
        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        registration = myCollectionReference.whereEqualTo("restaurantUrl", restaurantId).addSnapshotListener(eventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (!hasActiveObservers()) {
            registration.remove();
            registration = null;
        }
    }
}
