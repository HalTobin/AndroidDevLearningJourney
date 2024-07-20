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

// Abstraction to get a Collection from Firestore as LiveData
public class FirestoreCollectionLiveData<T> extends LiveData<T> {

    private ListenerRegistration registration;

    private final CollectionReference myCollectionReference;

    public FirestoreCollectionLiveData(CollectionReference myCollectionReference) {
        this.myCollectionReference = myCollectionReference;
    }

    @SuppressWarnings("unchecked")
    EventListener<QuerySnapshot> eventListener = (queryDocumentSnapshots, e) -> {
        if (e != null) return;

        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
            List<Workmate> itemList = new ArrayList<>();
            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                Workmate item = (Workmate) snapshot.toObject(Workmate.class);
                assert item != null;
                item.setId(snapshot.getId());
                item.setFavoritesUrl((List<String>) snapshot.get(Workmate.FAVORITE_RESTAURANT_URL));
                if(item.getRestaurantUrl() == null) {
                    item.setRestaurantName("");
                    item.setRestaurantUrl("");
                }
                itemList.add(item);
            }
            setValue((T) itemList);
        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        registration = myCollectionReference.addSnapshotListener(eventListener);
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
