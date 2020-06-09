package com.example.greenthumb;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class passwordEspressoTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void testStrongPassword(){
        onView(withId(R.id.editTextTextPassword))
                .perform(click())
                .perform(typeText("Nihir@123"));
        onView(withId(R.id.signUp_button))
                .perform(click());
        onView(withId(R.id.textView))
                .check(matches(withText("Strong password")));
    }


    @Test
    public void testWeakPassword(){
        onView(withId(R.id.editTextTextPassword))
                .perform(click())
                .perform(typeText("nihir@123"));
        onView(withId(R.id.signUp_button))
                .perform(click());
        onView(withId(R.id.textView))
                .check(matches(withText("Weak password, enter a stronger one.")));
    }
}
