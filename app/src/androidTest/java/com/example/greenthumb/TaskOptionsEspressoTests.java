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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

public class TaskOptionsEspressoTests {

    @Rule
    public ActivityScenarioRule<ViewTasks> activityScenarioRule
            = new ActivityScenarioRule<>(ViewTasks.class);

    @Before
    public void setUp() {
        // create task
        onView(withId(R.id.addTaskButton)).perform(click());

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
}
