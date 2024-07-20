package com.example.go4lunch.activity;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;

import static com.example.go4lunch.utils.ViewChildAtPosition.childAtPosition;
import static com.example.go4lunch.utils.WaitViewAction.waitFor;
import static com.example.go4lunch.utils.WaitViewAction.waitId;

import androidx.core.view.GravityCompat;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.Espresso;
import static androidx.test.espresso.intent.Intents.intended;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.go4lunch.R;
import com.example.go4lunch.model.Workmate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainHubActivityTest {

    private MainHubActivity myActivity;

    // Depending on your phone/emulator and transitions speed, you might need to increase this value...
    private static int SLEEP_TIME = 500;

    @Rule
    public ActivityTestRule<MainHubActivity> myActivityTestRule =
            new ActivityTestRule<>(MainHubActivity.class);

    @Rule
    public GrantPermissionRule myGrantPermissionRule =
            GrantPermissionRule.grant("android.permission.ACCESS_FINE_LOCATION");

    @Before
    public void setUp() {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            if(!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("altair.alek@yahoo.fr")) FirebaseAuth.getInstance().signOut();
        }

        Map<String, String> workmateMap = new HashMap<>();
        workmateMap.put(Workmate.FIRST_NAME_FIELD, "Alan Tobin");
        workmateMap.put(Workmate.AVATAR_URL_FIELD, "https://i1.sndcdn.com/artworks-jNwYYc3ZMfBHwydh-Fk1fdA-t500x500.jpg");
        workmateMap.put(Workmate.RESTAURANT_URL_FIELD, "ChIJ-aVKIopt5kcRu8Be9J01Tz4");
        workmateMap.put(Workmate.RESTAURANT_NAME_FIELD, "Le Colibri");
        FirebaseFirestore.getInstance().collection(Workmate.COLLECTION_NAME).document("4dVdvAKdqMSxAbm0rW5zkcXmYjK2").set(workmateMap);

        if(FirebaseAuth.getInstance().getCurrentUser()==null) {
            onView(isRoot()).perform(waitFor(SLEEP_TIME));
            onView(withId(R.id.email_button)).perform(click());
            onView(isRoot()).perform(waitFor(SLEEP_TIME));
            onView(withId(R.id.email)).perform(click());
            onView(withId(R.id.email)).perform(replaceText("altair.alek@yahoo.fr"));
            onView(withId(R.id.button_next)).perform(click());
            onView(isRoot()).perform(waitFor(SLEEP_TIME));
            onView(withId(R.id.password)).perform(click());
            onView(withId(R.id.password)).perform(replaceText("just@mock"));
            onView(withId(R.id.button_done)).perform(click());
        }

        Intents.init();
        myActivity = myActivityTestRule.getActivity();
        assertThat(myActivity, notNullValue());
    }

    @After
    public void cleanUp() {
        Intents.release();
    }

    // Test side menu
    @Test
    public void mySideMenu_openAndCloseAction_shouldOpenThenClose() {
        // Open Drawer to click on navigation.
        onView(withId(R.id.main_hub_drawer_layout))
                .check(matches(isClosed(GravityCompat.START))) // Drawer should be closed
                .perform(DrawerActions.open());

        onView(isRoot()).perform(waitFor(SLEEP_TIME));
        onView(withId(R.id.main_hub_drawer_layout)) // Drawer should be open
                .check(matches(isOpen(GravityCompat.START)));

        Espresso.pressBack();

        onView(isRoot()).perform(waitFor(SLEEP_TIME));
        onView(withId(R.id.main_hub_drawer_layout))
                .check(matches(isClosed(GravityCompat.START))); // Drawer should be closed
    }

    @Test
    public void mySideMenu_logOut_shouldLogOutUser() {
        assertNotNull(FirebaseAuth.getInstance().getCurrentUser()); // User should be logged

        // Open Drawer to click on navigation.
        onView(withId(R.id.main_hub_drawer_layout))
                .check(matches(isClosed(GravityCompat.START))) // Drawer should be closed
                .perform(DrawerActions.open());

        // Click on the log out item
        onView(withId(R.id.main_hub_side_nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.main_hub_menu_logout));

        onView(isRoot()).perform(waitFor(SLEEP_TIME));
        assertNull(FirebaseAuth.getInstance().getCurrentUser()); // User shouldn't be logged
    }

    @Test
    public void mySideMenu_restaurantDetails_shouldOpenDetails() {
        // Open Drawer to click on navigation.
        onView(withId(R.id.main_hub_drawer_layout))
                .check(matches(isClosed(GravityCompat.START))) // Drawer should be closed
                .perform(DrawerActions.open());

        // Click on "YOUR LUNCH" item
        onView(withId(R.id.main_hub_side_nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.main_hub_menu_lunch));

        // Check if RestaurantsDetailsActivity is open
        intended(hasComponent(RestaurantDetailsActivity.class.getName()));
    }

    // Test navigation between fragment
    @Test
    public void myBottomNavView_navigation_shouldNavigate() {

        // Check if MapFragment is displayed
        onView(allOf(withId(R.id.fragment_layout_map),
                withParent(withId(R.id.main_hub_frame_layout)),
                isDisplayed()));

        // Click on the Workmate item
        onView(isRoot()).perform(waitFor(SLEEP_TIME));
        onView(withId(R.id.main_hub_menu_workmates))
                .perform(click());
        onView(isRoot()).perform(waitFor(SLEEP_TIME));

        // Check if WorkmatesFragment is displayed
        onView(allOf(withId(R.id.fragment_layout_workmate),
                        withParent(withId(R.id.main_hub_frame_layout)),
                        isDisplayed()));

        // Click on the List item
        onView(isRoot()).perform(waitFor(SLEEP_TIME));
        onView(withId(R.id.main_hub_menu_list))
                .perform(click());
        onView(isRoot()).perform(waitFor(SLEEP_TIME));

        // Check if ListFragment is displayed
        onView(allOf(withId(R.id.fragment_layout_list),
                withParent(withId(R.id.main_hub_frame_layout)),
                isDisplayed()));

        // Click on the Map item
        onView(isRoot()).perform(waitFor(SLEEP_TIME));
        onView(withId(R.id.main_hub_menu_map))
                .perform(click());
        onView(isRoot()).perform(waitFor(SLEEP_TIME));

        // Check if MapFragment is displayed
        onView(allOf(withId(R.id.fragment_layout_map),
                withParent(withId(R.id.main_hub_frame_layout)),
                isDisplayed()));
    }

    @Test
    public void myRestaurantFragment_clickRestaurant_shouldOpenDetails() {
        onView(isRoot()).perform(waitId(R.id.main_hub_menu_list));
        // Click on the List item
        onView(withId(R.id.main_hub_menu_list))
                .perform(click());
        onView(isRoot()).perform(waitFor(SLEEP_TIME));

        // Check if ListFragment is displayed
        onView(allOf(withId(R.id.fragment_layout_list),
                withParent(withId(R.id.main_hub_frame_layout)),
                isDisplayed()));

        onView(isRoot()).perform(waitFor(SLEEP_TIME));

        // Click on the first item of the list
        onView(withId(R.id.fragment_restaurant_list)).perform(actionOnItemAtPosition(0, click()));

        intended(hasComponent(RestaurantDetailsActivity.class.getName()));
    }
}
