package com.example.greenthumb;

import android.os.SystemClock;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

public class TaskOptionsEspressoTests {
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

        //navigate to ViewTasks page & wait for it to load
        onView(withId(R.id.toTaskPage))
                .perform(click());
        SystemClock.sleep(1000);

        // wait for UI
        SystemClock.sleep(1000);

        // create task
        onView(withId(R.id.addTaskButton)).perform(click());

        // wait for Add Task dialog
        SystemClock.sleep(1000);

        // select title from dropdown
        onView(withId(R.id.spinnerTaskTitle)).perform(click());
        onData(anything()).inRoot(RootMatchers.isPlatformPopup()).atPosition(1).perform(click());

        // submit task
        onView(withText("Add")).perform(click());
    }

    // ensure that menu options are enabled on newly created task
    @Test
    public void testOptionsEnabled() {
        // click options button
        onView(withId(R.id.recyclerViewTasks)).perform(RecyclerViewActions.
                        actionOnItemAtPosition(0, CustomRecyclerViewActions.clickChildViewWithId(R.id.taskOptions)));

        // wait for menu items to appear
        SystemClock.sleep(1000);

        // assert options are visible and enabled
        onView(withText(R.string.claim)).check(matches(allOf(isEnabled(), isDisplayed())));
        onView(withText(R.string.mark_as_done)).check(matches(allOf(isEnabled(), isDisplayed())));

        // delete the task
        onView(withText(R.string.delete)).perform(click());
    }

    // ensure that menu options are disabled after they are selected
    @Test
    public void testOptionsDisabled() {
        // click options button
        onView(withId(R.id.recyclerViewTasks)).perform(RecyclerViewActions.
                actionOnItemAtPosition(getIndexOfLastChildOfViewWithId(R.id.recyclerViewTasks),
                        CustomRecyclerViewActions.clickChildViewWithId(R.id.taskOptions)));

        // wait for menu items to appear
        SystemClock.sleep(1000);

        // click Claim button
        onView(withText(R.string.claim)).perform(click());

        // wait
        SystemClock.sleep(1000);

        // click options button
        onView(withId(R.id.recyclerViewTasks)).perform(RecyclerViewActions.
                actionOnItemAtPosition(getIndexOfLastChildOfViewWithId(R.id.recyclerViewTasks),
                        CustomRecyclerViewActions.clickChildViewWithId(R.id.taskOptions)));

        // wait for menu items to appear
        SystemClock.sleep(1000);

        // assert Claim button is disabled
        onView(withText(R.string.claim)).check(matches(not(isClickable())));

        // click Mark As Done button
        onView(withText(R.string.mark_as_done)).perform(click());

        // wait
        SystemClock.sleep(1000);

        // click options button
        onView(withId(R.id.recyclerViewTasks)).perform(RecyclerViewActions.
                actionOnItemAtPosition(getIndexOfLastChildOfViewWithId(R.id.recyclerViewTasks),
                        CustomRecyclerViewActions.clickChildViewWithId(R.id.taskOptions)));

        // wait for menu items to appear
        SystemClock.sleep(1000);

        // assert Claim button is disabled
        onView(withText(R.string.mark_as_done)).check(matches(not(isClickable())));

        // delete the task
        onView(withText(R.string.delete)).perform(click());
    }

    // ensure that a task shows that it has been marked done only after a user clicks 'Mark As Done'
    @Test
    public void testTaskDoneConfirmation() {
        // check that the newest task does not display "Done" and a checkmark
        onView(new RecyclerViewMatcher(R.id.recyclerViewTasks).atPosition(0))
                .check(matches(allOf(hasDescendant(allOf(withText("Done"), not(isDisplayed()))),
                        hasDescendant(allOf(withId(R.id.checkmark), not(isDisplayed()))))));

        // click options button
        onView(withId(R.id.recyclerViewTasks)).perform(RecyclerViewActions.
                actionOnItemAtPosition(0, CustomRecyclerViewActions.clickChildViewWithId(R.id.taskOptions)));

        // wait for menu items to appear
        SystemClock.sleep(1000);

        // click Mark As Done button
        onView(withText(R.string.mark_as_done)).perform(click());

        // wait
        SystemClock.sleep(1000);

        // check that the newest task displays "Done" and a checkmark
        onView(new RecyclerViewMatcher(R.id.recyclerViewTasks).atPosition(0))
                .check(matches(allOf(hasDescendant(allOf(withText("Done"), isDisplayed())),
                        hasDescendant(allOf(withId(R.id.checkmark), isDisplayed())))));

        // click options button
        onView(withId(R.id.recyclerViewTasks)).perform(RecyclerViewActions.
                actionOnItemAtPosition(0, CustomRecyclerViewActions.clickChildViewWithId(R.id.taskOptions)));

        // wait for menu items to appear
        SystemClock.sleep(1000);

        // delete the task
        onView(withText(R.string.delete)).perform(click());
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
