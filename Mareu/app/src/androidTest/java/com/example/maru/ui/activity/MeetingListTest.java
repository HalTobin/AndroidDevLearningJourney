package com.example.maru.ui.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.maru.R;
import com.example.maru.utils.RecyclerViewItemCountAssertion;
import com.example.maru.utils.RecyclerViewItemCountDifferent;

import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.maru.utils.RecyclerViewItemCountAssertion.*;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MeetingListTest {

    private static int MEETINGS_COUNT = 8;

    private ListMeetingActivity mActivity;
    private ActivityScenario mScenario;

    @Rule
    public ActivityScenarioRule<ListMeetingActivity> mActivityRule =
            new ActivityScenarioRule(ListMeetingActivity.class);

    @Before
    public void setUp() {
        Intents.init();
        mScenario = mActivityRule.getScenario();
        assertThat(mScenario, notNullValue());
    }

    @After
    public void cleanUp() {
        Intents.release();
    }

    @Test
    public void myListMeeting_openAddMeeting_isLaunched() {
        onView(withId(R.id.add_meeting)).perform(click());
        intended(hasComponent(AddMeetingActivity.class.getName()));
    }

    @Test
    public void myListMeeting_createAction_isCreated() {
        //check the number of meeting
        onView(ViewMatchers.withId(R.id.list_meetings)).check(new RecyclerViewItemCountAssertion(MEETINGS_COUNT));

        //open the AddMeeting activity
        onView(withId(R.id.add_meeting)).perform(click());

        //set the name "Test"
        onView(withId(R.id.activity_add_meeting_txt_name)).perform(replaceText("Test"), closeSoftKeyboard());

        //click on the colored image
        onView(withId(R.id.activity_add_meeting_img_color)).perform(click());

        //select a color in the dialog
        onView(allOf(withId(R.id.color), childAtPosition(allOf(withId(R.id.linearLayout), childAtPosition(withId(R.id.color_palette), 8)), 0))).perform(click());
        //click on the button "Valider"
        onView(withText("Valider")).perform(click());

        //click on the date card
        onView(withId(R.id.activity_add_meeting_bt_pick_date)).perform(click());
        //select a date
        onView(withId(android.R.id.button1)).perform(click());
        //select an hour
        onView(withId(android.R.id.button1)).perform(click());

        //click on the room card
        onView(withId(R.id.activity_add_meeting_bt_pick_room)).perform(click());
        //select a room
        onView(withId(R.id.dialog_picker_room_list)).perform(actionOnItemAtPosition(1, click()));

        //add user and check that they've been added
        onView(ViewMatchers.withId(R.id.activity_add_meeting_list_user)).check(new RecyclerViewItemCountAssertion(0));
        myAddMeetingActivity_addUser(0);
        onView(ViewMatchers.withId(R.id.activity_add_meeting_list_user)).check(new RecyclerViewItemCountAssertion(1));
        myAddMeetingActivity_addUser(4);
        onView(ViewMatchers.withId(R.id.activity_add_meeting_list_user)).check(new RecyclerViewItemCountAssertion(2));

        //click on the button "Créer une réunion" to create the meeting
        onView(withId(R.id.activity_add_meeting_bt_create_meeting)).perform(click());
        //check the number of meeting
        MEETINGS_COUNT += 1;
        onView(ViewMatchers.withId(R.id.list_meetings)).check(new RecyclerViewItemCountAssertion(MEETINGS_COUNT));
    }

    public void myAddMeetingActivity_addUser(int indexUser) {
        //click on the add user button
        onView(withId(R.id.activity_add_meeting_bt_add_user)).perform(click());
        //pick an user
        onView(withId(R.id.dialog_picker_user_list_users)).perform(actionOnItemAtPosition(indexUser, click()));
        //confirm the selection
        onView(withId(android.R.id.button1)).perform(click());
    }

    @Test
    public void myListMeeting_removeAction_isRemoved() {
        //check number of elements in the meetings list
        onView(ViewMatchers.withId(R.id.list_meetings)).check(new RecyclerViewItemCountAssertion(MEETINGS_COUNT));
        //click on a delete icon
        onView(allOf(withId(R.id.item_list_delete_button), childAtPosition(childAtPosition(withId(R.id.list_meetings), 3), 3))).perform(click());
        //check if one item has been removed
        MEETINGS_COUNT -= 1;
        onView(ViewMatchers.withId(R.id.list_meetings)).check(new RecyclerViewItemCountAssertion(MEETINGS_COUNT));
    }

    @Test
    public void myListMeeting_filterByDate_isApplied() {
        onView(withId(R.id.item1)).perform(click());
        onView(allOf(withId(R.id.title), withText("Filtrer par date"), childAtPosition(childAtPosition(withId(R.id.content), 0), 0), isDisplayed())).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(ViewMatchers.withId(R.id.list_meetings)).check(new RecyclerViewItemCountDifferent(MEETINGS_COUNT));
    }

    @Test
    public void myListMeeting_filterByRoom_isApplied() {
        onView(withId(R.id.item1)).perform(click());
        onView(allOf(withId(R.id.title), withText("Filtrer par salle"), childAtPosition(childAtPosition(withId(R.id.content), 0), 0), isDisplayed())).perform(click());
        onView(withId(R.id.dialog_picker_room_list)).perform(actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.list_meetings)).check(new RecyclerViewItemCountDifferent(MEETINGS_COUNT));
    }

    @Test
    public void myListMeeting_filterReset_isApplied() {
        onView(ViewMatchers.withId(R.id.list_meetings)).check(new RecyclerViewItemCountAssertion(MEETINGS_COUNT));
        myListMeeting_filterByRoom_isApplied();

        onView(withId(R.id.item1)).perform(click());
        onView(allOf(withId(R.id.title), withText("Tout afficher"), childAtPosition(childAtPosition(withId(R.id.content), 0), 0), isDisplayed())).perform(click());

        onView(ViewMatchers.withId(R.id.list_meetings)).check(new RecyclerViewItemCountAssertion(MEETINGS_COUNT));
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.maru", appContext.getPackageName());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}