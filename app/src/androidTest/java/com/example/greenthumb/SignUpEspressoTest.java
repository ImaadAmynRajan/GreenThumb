package com.example.greenthumb;

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
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpEspressoTest {

    @Before
    public void initIntents() {
        Intents.init();
    }

    @Rule
    public ActivityScenarioRule<SignUp> activityScenarioRule
            = new ActivityScenarioRule<>(SignUp.class);

    @Test
    public void verifyHomePageShown() {
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(click())
                .perform(typeText("new@email.ca"));
        closeSoftKeyboard();
        onView(withId(R.id.editTextTextPassword))
                .perform(click())
                .perform(typeText("NewP@55word"));
        closeSoftKeyboard();
        onView(withId(R.id.signUp_button))
                .perform(click());
        intended(hasComponent(HomePage.class.getName()));
        /* delete user so that the email does not need to be changed for each run
         * code snippet from https://firebase.google.com/docs/auth/android/manage-users#delete_a_user
         */
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            user.delete();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

}
