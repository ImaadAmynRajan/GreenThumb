package com.example.greenthumb;

import android.os.SystemClock;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


public class TradePageEspressoTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void login() {
        // enter test credentials
        String testEmail = "ben.kelly@dal.ca";
        onView(withId(R.id.Email)).perform(click()).perform(typeText(testEmail)).perform(closeSoftKeyboard());
        String testPassword = "Test1234";
        onView(withId(R.id.Password)).perform(click()).perform(typeText(testPassword)).perform(closeSoftKeyboard());

        // login
        onView(withId(R.id.login_button)).perform(click()).perform(click());

        // wait for Home page to load
        SystemClock.sleep(2000);

        //navigate to TradeTasks page & wait for it to load
        //onView(withId(R.id.toTradePage))
        //        .perform(click());
        SystemClock.sleep(1000);
    }

    @Test
    //check that the navigation bar is present
    public void verifyNavigationDisplayed() {
        onView(withId(R.id.mainNav))
                .check(matches(isDisplayed()));
        onView(withId(R.id.toHomePage))
                .check(matches(isDisplayed()));
        onView(withId(R.id.toTaskPage))
                .check(matches(isDisplayed()));
        onView(withId(R.id.toTradePage))
                .check(matches(isDisplayed()));
    }

}
