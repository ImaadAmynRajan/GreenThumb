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
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class ForgotPasswordTest {
    @Rule
    public ActivityScenarioRule<ForgotPassword> activityScenarioRule
            = new ActivityScenarioRule<>(ForgotPassword.class);

    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void testEmail() {

        onView(withId(R.id.editTextResetEmailAddress2))
                .perform(click())
                .perform(typeText("imj@dal.ca"));

        onView(withId(R.id.resetEmail))
                .perform(click());
        SystemClock.sleep(2000);

        intended(hasComponent(MainActivity.class.getName()));

    }

}
