package com.example.greenthumb;

import android.os.SystemClock;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;

public class LoginEspressoTest {

    //@Before & @After code snippets from first answer of https://stackoverflow.com/questions/25998659/espresso-how-can-i-check-if-an-activity-is-launched-after-performing-a-certain
    @Before
    public void initIntents() { Intents.init(); }

    @After
    public void releaseIntents() { Intents.release(); }

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void nonVerifiedEmailTest() {
        // enter email that does has not been activated
        onView(withId(R.id.Email))
                .perform(click())
                .perform(typeText("sample@email.ca"));
        closeSoftKeyboard();
        onView(withId(R.id.Password))
                .perform(click())
                .perform(typeText("SamplePassw0rd!"));
        closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());

        SystemClock.sleep(2000);
        /* use of intended based on https://developer.android.com/training/testing/espresso/cheat-sheet
         * https://developer.android.com/reference/androidx/test/espresso/intent/matcher/IntentMatchers#hascomponent
         */
        // a toast should appear on the screen saying the login is not verified
        onView(withText(R.string.non_verified_toast)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void verifiedEmailTest() {
        // enter email that has been verified
        onView(withId(R.id.Email))
                .perform(click())
                .perform(typeText("rhys.mackenzie@dal.ca"));
        closeSoftKeyboard();
        onView(withId(R.id.Password))
                .perform(click())
                .perform(typeText("@Rhys1234"));
        closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());

        SystemClock.sleep(2000);
        /* use of intended based on https://developer.android.com/training/testing/espresso/cheat-sheet
         * https://developer.android.com/reference/androidx/test/espresso/intent/matcher/IntentMatchers#hascomponent
         */
        // app should login successfully
        intended(hasComponent(HomePage.class.getName()));
    }

}
