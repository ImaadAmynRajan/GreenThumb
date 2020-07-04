package com.example.greenthumb;

import android.os.SystemClock;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class HomePageEspressoTest {
    private final String testEmail = "ben.kelly@dal.ca";
    private final String testPassword = "Test1234";

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void initIntents() { Intents.init(); }

    @Before
    public void login() {
        // enter test credentials
        onView(withId(R.id.Email)).perform(click()).perform(typeText(testEmail)).perform(closeSoftKeyboard());
        onView(withId(R.id.Password)).perform(click()).perform(typeText(testPassword)).perform(closeSoftKeyboard());

        // login
        onView(withId(R.id.login_button)).perform(click());

        // wait for Home page to load
        SystemClock.sleep(2000);
    }

    @After
    public void releaseIntents() { Intents.release(); }

    @Test
    public void verifyNavigationDisplayed() {
        onView(withId(R.id.mainNav))
                .check(matches(isDisplayed()));
        onView(withId(R.id.toHomePage))
                .check(matches(isDisplayed()));
        onView(withId(R.id.toTaskPage))
                .check(matches(isDisplayed()));
    }

    @Test
    //check that navigation bar can take user from Home Page -> Tasks
    public void testViewTasksPage() {
        onView(withId(R.id.toTaskPage))
                .perform(click());
        intended(hasComponent(ViewTasks.class.getName()));
    }

    @Test
    // checks that all tasks displayed on the home page belong to the current user
    public void testUserTasksDisplayed() {
        // get number of tasks on the home page
        int numberOfTasks = getChildCountOfViewWithId(R.id.recyclerViewUserTasks);

        for (int i = 0; i < numberOfTasks; ++i) {
            // check that all tasks are assigned to the test email
            onView(new RecyclerViewMatcher(R.id.recyclerViewUserTasks).atPosition(i))
                    .check(matches(hasDescendant(hasDescendant(withText("Assigned to: " + testEmail)))));
        }
    }
    
    @Test
    public void logout() {
        onView(withId(R.id.logout_button))
                .perform(click());
        intended(hasComponent(MainActivity.class.getName()));
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
}
