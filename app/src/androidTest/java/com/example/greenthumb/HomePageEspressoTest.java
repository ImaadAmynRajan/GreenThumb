package com.example.greenthumb;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class HomePageEspressoTest {

    @Before
    public void initIntents() { Intents.init(); }

    @After
    public void releaseIntents() { Intents.release(); }

    @Rule
    public ActivityScenarioRule<HomePage> activityScenarioRule
            = new ActivityScenarioRule<>(HomePage.class);

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
    public void logout() {
        onView(withId(R.id.logout_button))
                .perform(click());
        intended(hasComponent(MainActivity.class.getName()));


    }

}
