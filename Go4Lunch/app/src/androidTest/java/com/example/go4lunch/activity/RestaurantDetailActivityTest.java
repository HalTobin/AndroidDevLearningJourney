package com.example.go4lunch.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.example.go4lunch.utils.WaitViewAction.waitFor;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.util.Log;

import androidx.core.view.GravityCompat;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.go4lunch.R;
import com.example.go4lunch.model.Workmate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import kotlin.jvm.JvmField;

public class RestaurantDetailActivityTest {

    private MainHubActivity myActivity;

    // Depending on your phone/emulator and transitions speed, you might need to increase this value...
    private static int SLEEP_TIME = 500;

    static String USER_ID = "4dVdvAKdqMSxAbm0rW5zkcXmYjK2";
    static String RESTAURANT_URL = "ChIJ-aVKIopt5kcRu8Be9J01Tz4";
    static String RESTAURANT_NAME = "Le Colibri";

    private DocumentReference FIRESTORE_DOC;

    @Rule
    public ActivityTestRule<MainHubActivity> myActivityTestRule =
            new ActivityTestRule<>(MainHubActivity.class);

    @Rule
    public GrantPermissionRule myGrantPermissionRule =
            GrantPermissionRule.grant("android.permission.ACCESS_FINE_LOCATION");

    @Before
    public void setUp() {
        FIRESTORE_DOC = FirebaseFirestore.getInstance().collection(Workmate.COLLECTION_NAME).document(USER_ID);

        FirebaseAuth.getInstance().signInWithEmailAndPassword("altair.alek@yahoo.fr", "just@mock");

        Map<String, String> workmateMap = new HashMap<>();
        workmateMap.put(Workmate.FIRST_NAME_FIELD, "Alan Tobin");
        workmateMap.put(Workmate.AVATAR_URL_FIELD, "https://i1.sndcdn.com/artworks-jNwYYc3ZMfBHwydh-Fk1fdA-t500x500.jpg");
        workmateMap.put(Workmate.RESTAURANT_URL_FIELD, RESTAURANT_URL);
        workmateMap.put(Workmate.RESTAURANT_NAME_FIELD, RESTAURANT_NAME);
        FIRESTORE_DOC.set(workmateMap);

        /*Intent i = new Intent();
        i.putExtra("RestaurantId", RESTAURANT_URL);
        myActivityTestRule.launchActivity(i);*/

        Intents.init();
        myActivity = myActivityTestRule.getActivity();
        assertThat(myActivity, notNullValue());

        // NAVIGATE TO RESTAURANTDETAILSACTIVITY
        // Open Drawer to click on navigation.
        onView(withId(R.id.main_hub_drawer_layout))
                .check(matches(isClosed(GravityCompat.START))) // Drawer should be closed
                .perform(DrawerActions.open());
        // Click on the "YOUR LUNCH" item
        onView(withId(R.id.main_hub_side_nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.main_hub_menu_lunch));
        // Check if RestaurantsDetailsActivity is open
        intended(hasComponent(RestaurantDetailsActivity.class.getName()));
    }

    @After
    public void cleanUp() {
        Intents.release();
    }

    @Test
    public void eatHereButton_shouldRemoveRestaurant() {
        // Fill the restaurant fields
        Map<String, String> workmateMap = new HashMap<>();
        workmateMap.put(Workmate.RESTAURANT_NAME_FIELD, RESTAURANT_NAME);
        workmateMap.put(Workmate.RESTAURANT_URL_FIELD, RESTAURANT_URL);
        FIRESTORE_DOC.set(workmateMap, SetOptions.merge());

        onView(isRoot()).perform(waitFor(2000));
        onView(withId(R.id.restaurant_detail_eat_here)).perform(click());
        FIRESTORE_DOC.get().addOnCompleteListener(task -> {
            assertEquals("", task.getResult().get(Workmate.RESTAURANT_URL_FIELD));
        });
    }

    @Test
    public void eatHereButton_shouldAddRestaurant() {
        // Set the restaurant fields to nothing
        Map<String, String> workmateMap = new HashMap<>();
        workmateMap.put(Workmate.RESTAURANT_NAME_FIELD, "");
        workmateMap.put(Workmate.RESTAURANT_URL_FIELD, "");
        FIRESTORE_DOC.set(workmateMap, SetOptions.merge());

        onView(isRoot()).perform(waitFor(2000));
        onView(withId(R.id.restaurant_detail_eat_here)).perform(click());
        FIRESTORE_DOC.get().addOnCompleteListener(task -> {
            assertEquals(RESTAURANT_URL, task.getResult().get(Workmate.RESTAURANT_URL_FIELD));
        });
    }

    @Test
    public void likeButton_shouldAddFavorite() {
        onView(isRoot()).perform(waitFor(2000));
        onView(withId(R.id.restaurant_detail_like)).perform(click());

        FIRESTORE_DOC.get().addOnCompleteListener(task -> {
            Log.i("UI TEST : ", Objects.requireNonNull(task.getResult().get(Workmate.FAVORITE_RESTAURANT_URL)).toString());
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    ArrayList<String> favorites = (ArrayList<String>) document.getData().get(Workmate.FAVORITE_RESTAURANT_URL);
                    assertTrue(favorites.contains(RESTAURANT_URL));
                }
            }

        });
    }

    @SuppressWarnings("unchecked")
    @Test
    public void likeButton_shouldRemoveFavorite() {
        FIRESTORE_DOC.update(Workmate.FAVORITE_RESTAURANT_URL, FieldValue.arrayUnion(RESTAURANT_URL));

        onView(isRoot()).perform(waitFor(2000));
        onView(withId(R.id.restaurant_detail_like)).perform(click());

        FIRESTORE_DOC.get().addOnCompleteListener(task -> {
            Log.i("UI TEST", Objects.requireNonNull(task.getResult().get(Workmate.FAVORITE_RESTAURANT_URL)).toString());
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    ArrayList<String> favorites = (ArrayList<String>) Objects.requireNonNull(document.getData()).get(Workmate.FAVORITE_RESTAURANT_URL);
                    assert favorites != null;
                    assertFalse(favorites.contains(RESTAURANT_URL));
                }
            }

        });
    }

}
