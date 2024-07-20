package com.example.go4lunch.query;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.model.Workmate;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

// Abstraction to get a Document from Firestore as LiveData
public class FirestoreDocumentLiveData<T> extends LiveData<T> {

    private ListenerRegistration registration;

    private final DocumentReference myDocumentReference;

    public FirestoreDocumentLiveData(DocumentReference myDocumentReference) {
        this.myDocumentReference = myDocumentReference;
    }

    @SuppressWarnings("unchecked")
    EventListener<DocumentSnapshot> eventListener = (documentSnapshot, e) -> {
        if (e != null) return;
        if (documentSnapshot != null && documentSnapshot.exists()) {
            Workmate item = (Workmate) documentSnapshot.toObject(Workmate.class);
            assert item != null;
            item.setId(documentSnapshot.getId());
            item.setFavoritesUrl((List<String>) documentSnapshot.get(Workmate.FAVORITE_RESTAURANT_URL));

            setValue((T) item);
        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        registration = myDocumentReference.addSnapshotListener(eventListener);
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
