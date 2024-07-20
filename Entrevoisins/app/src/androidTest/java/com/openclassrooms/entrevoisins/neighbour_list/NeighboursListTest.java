
package com.openclassrooms.entrevoisins.neighbour_list;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.ui.neighbour_data.NeighbourDataActivity;
import com.openclassrooms.entrevoisins.ui.neighbour_list.AddNeighbourActivity;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;
import com.openclassrooms.entrevoisins.utils.RemoveViewAction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;

import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;

import static org.hamcrest.core.IsNull.notNullValue;



/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static int ITEMS_COUNT = 12;

    private ListNeighbourActivity mActivity;

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        Intents.init();
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    @After
    public void endTest() {
        Intents.release();
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.list_neighbours))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(ViewMatchers.withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(ViewMatchers.withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(ViewMatchers.withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT-1));
    }

    @Test
    public void myAddNeighbourActivity_isLaunched() {
        // Perform a click on the first element from the list
        onView(withId(R.id.add_neighbour)).perform(click());
        // Check if the AddNeighbourActivity is launched
        intended(hasComponent(AddNeighbourActivity.class.getName()));
    }

    @Test
    public void myNeighbourDataActivity_isLaunched() {
        // Perform a click on the first element from the list
        onView(withId(R.id.list_neighbours)).perform(actionOnItemAtPosition(0, click()));
        // Check if the NeighbourDataActivity is launched
        intended(hasComponent(NeighbourDataActivity.class.getName()));
    }

    @Test
    public void myNeighbourDataActivity_isNameValid() {
        // Perform a click on the first element from the list
        onView(withId(R.id.list_neighbours)).perform(actionOnItemAtPosition(0, click()));
        // Check the TextView content
        onView(withId(R.id.activity_neighbour_data_txt_name)).check(matches(withText("Caroline")));
    }

    @Test
    public void myFavoriteList_addAFavorite() {
        // Perform a click on the first element from the list
        onView(withId(R.id.list_neighbours)).perform(actionOnItemAtPosition(0, click()));
        // Perform a click on the favorite button
        onView(withId(R.id.activity_neighbour_data_bt_favorite)).perform(click());
        // Perform a click on the cancel image
        onView(withId(R.id.activity_neighbour_data_img_cancel)).perform(click());
        // Check that the list contain 1 item
        onView(ViewMatchers.withId(R.id.list_favorite)).check(withItemCount(1));
    }

    @Test
    public void myNeighboursList_removeAction_shouldRemoveItem() {
        // Add an item to the list of favorite
        myFavoriteList_addAFavorite();
        // Perform a click on the tab 'Favorites'
        onView(allOf(withContentDescription("Favorites"))).perform(click());
        // Swipe to the left to display the content of the tab 'Favorites'
        onView(allOf(withId(R.id.container))).perform(swipeLeft());
        // Check if the list contain an item
        onView(ViewMatchers.withId(R.id.list_favorite)).check(withItemCount(1));
        // Perform a click on a remove icon
        onView(ViewMatchers.withId(R.id.list_favorite)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new RemoveViewAction()));
        // Check if the list is empty
        onView(ViewMatchers.withId(R.id.list_favorite)).check(withItemCount(0));
    }

}