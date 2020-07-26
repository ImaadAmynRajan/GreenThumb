package com.example.greenthumb;

import android.os.SystemClock;
import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.greenthumb.tasks.Task;
import com.example.greenthumb.tasks.TaskTitle;
import com.example.greenthumb.trade.TradeRequest;
import com.example.greenthumb.trade.TradeRequestViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class TradeRequestEspressoTests {
    private final String testEmail = "ben.kelly@dal.ca";
    private final String testPassword = "Test1234";

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        // first login

        // enter test credentials
        onView(withId(R.id.Email)).perform(click()).perform(typeText(testEmail)).perform(closeSoftKeyboard());
        onView(withId(R.id.Password)).perform(click()).perform(typeText(testPassword)).perform(closeSoftKeyboard());

        // login
        onView(withId(R.id.login_button)).perform(click());

        // wait for Home page to load
        SystemClock.sleep(2000);

        //navigate to Trades page & wait for it to load
        onView(withId(R.id.toTradePage))
                .perform(click());
        SystemClock.sleep(1000);

        // wait for UI
        SystemClock.sleep(1000);
    }

    @Test
    // adds a trade request and checks that it is shown properly
    public void testTradeRequestHolder() {
        String requestId = "ABC";
        String requesterId = "123";
        String requesterEmail = "requester@email.com";
        User requester = new User(requesterId, requesterEmail);
        User assignee = new User("DEF", testEmail);
        Task dummyTask = new Task("456", TaskTitle.ClearDebris, null, assignee);

        // create new trade request and save it to database
        TradeRequestViewModel dummyTradeRequest = new TradeRequestViewModel(new TradeRequest(requestId, requester, dummyTask));
        dummyTradeRequest.save();

        // leave trades page and come back to refresh RecyclerView (not necessary when adding trade requests manually)
        onView(withId(R.id.toHomePage))
                .perform(click());
        SystemClock.sleep(1000);
        onView(withId(R.id.toTradePage))
                .perform(click());
        SystemClock.sleep(1000);

        // check that trade request is properly shown
        onView(new RecyclerViewMatcher(R.id.recyclerViewTradeRequests).atPosition(getIndexOfLastChildOfViewWithId(R.id.recyclerViewTradeRequests)))
                .check(matches(allOf(hasDescendant(withText("Keep gardens and green spaces clear of debris and litter")),
                        hasDescendant((withText("Due date: None"))),
                        hasDescendant(withText("Requested by: requester@email.com")))));

        // delete the trade request
        dummyTradeRequest.delete();
    }

    /**
     * Returns the number of child views contained within a view
     * @param id id of the parent view
     * @return the number of child views contained within the view
     */
    private int getChildCountOfViewWithId(int id) {
        int count = 0;

        // try matching the view with an incrementing number of child views
        while (count < 10000) {
            try {
                onView(withId(id)).check(matches(hasChildCount(count)));
                return count;
            } catch (Error failedAssertion) {
                count++;
            }
        }

        return 0;
    }

    /**
     * Returns the index of the last child in a view
     * @param id id of the parent view
     * @return the index of the last child in the view
     */
    private int getIndexOfLastChildOfViewWithId(int id) {
        int numberOfChildren = getChildCountOfViewWithId(id);
        if (numberOfChildren > 0) return numberOfChildren - 1;
        return 0;
    }
}
