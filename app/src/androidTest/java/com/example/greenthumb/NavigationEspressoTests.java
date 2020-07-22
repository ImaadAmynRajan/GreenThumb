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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class NavigationEspressoTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void initIntents() { Intents.init(); }

    @Before
    public void login() {
        String testEmail = "ben.kelly@dal.ca";
        String testPassword = "Test1234";

        // enter test credentials
        onView(withId(R.id.Email)).perform(click()).perform(typeText(testEmail)).perform(closeSoftKeyboard());
        onView(withId(R.id.Password)).perform(click()).perform(typeText(testPassword)).perform(closeSoftKeyboard());

        // login
        onView(withId(R.id.login_button)).perform(click()).perform(click());

        // wait for Home page to load
        SystemClock.sleep(2000);
    }

    @After
    public void releaseIntents() { Intents.release(); }

    @Test
    //check that navigation bar can take user from Home -> ViewTasks and ViewTasks -> Home
    public void testHomeAndViewTasks() {
        //navigate from Home to ViewTasks
        onView(withId(R.id.toTaskPage))
                .perform(click());
        intended(hasComponent(ViewTasks.class.getName()));
        //navigate from ViewTasks to Home
        onView(withId(R.id.toHomePage))
                .perform(click());
        intended(hasComponent(HomePage.class.getName()));
    }

    @Test
    //check that navigation bar can take user from Home -> TradeTasks and TradeTasks -> Home
    public void testHomeAndTrades() {
        //navigate from Home to TradeTasks
        onView(withId(R.id.toTradePage))
              .perform(click());
        intended(hasComponent(TradeTasks.class.getName()));
        //navigate from TradeTasks to Home
        onView(withId(R.id.toHomePage))
                .perform(click());
        intended(hasComponent(HomePage.class.getName()));
    }

    @Test
    //check that navigation bar can take user from ViewTasks -> TradeTasks and back
    public void testTasksToTrades() {
        //first navigate from Home to ViewTasks
        onView(withId(R.id.toTaskPage))
                .perform(click());
        intended(hasComponent(ViewTasks.class.getName()));
        //navigate from ViewTasks to TradeTasks
        onView(withId(R.id.toTradePage))
                .perform(click());
        intended(hasComponent(TradeTasks.class.getName()));
        //navigate from TradeTasks to ViewTasks
        onView(withId(R.id.toTaskPage))
                .perform(click());
        intended(hasComponent(ViewTasks.class.getName()));
    }
}
