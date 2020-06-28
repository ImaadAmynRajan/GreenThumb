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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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

    @Rule
    public ActivityScenarioRule<ViewTasks> activityScenarioRule
            = new ActivityScenarioRule<>(ViewTasks.class);

    @Before
    public void setUp() {
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
    }

    // ensure that menu options are disabled after they are selected
    @Test
    public void testOptionsDisabled() {
        // click options button
        onView(withId(R.id.recyclerViewTasks)).perform(RecyclerViewActions.
                actionOnItemAtPosition(0, CustomRecyclerViewActions.clickChildViewWithId(R.id.taskOptions)));

        // wait for menu items to appear
        SystemClock.sleep(1000);

        // click Claim button
        onView(withText(R.string.claim)).perform(click());

        // click options button
        onView(withId(R.id.recyclerViewTasks)).perform(RecyclerViewActions.
                actionOnItemAtPosition(0, CustomRecyclerViewActions.clickChildViewWithId(R.id.taskOptions)));

        // wait for menu items to appear
        SystemClock.sleep(1000);

        // assert Claim button is disabled
        onView(withText(R.string.claim)).check(matches(not(isClickable())));

        // click Mark As Done button
        onView(withText(R.string.mark_as_done)).perform(click());

        // click options button
        onView(withId(R.id.recyclerViewTasks)).perform(RecyclerViewActions.
                actionOnItemAtPosition(0, CustomRecyclerViewActions.clickChildViewWithId(R.id.taskOptions)));

        // wait for menu items to appear
        SystemClock.sleep(1000);

        // assert Claim button is disabled
        onView(withText(R.string.mark_as_done)).check(matches(not(isClickable())));
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

        // check that the newest task displays "Done" and a checkmark
        onView(new RecyclerViewMatcher(R.id.recyclerViewTasks).atPosition(0))
                .check(matches(allOf(hasDescendant(allOf(withText("Done"), isDisplayed())),
                        hasDescendant(allOf(withId(R.id.checkmark), isDisplayed())))));
    }
}
