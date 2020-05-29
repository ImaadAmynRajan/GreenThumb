package com.example.greenthumb;

import android.view.KeyEvent;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TasksEspressoTest {

    @Rule
    public ActivityScenarioRule<ViewTasks> activityScenarioRule
            = new ActivityScenarioRule<>(ViewTasks.class);


    @Test
    public void testNewTaskVisible(){
        onView(withId(R.id.addTaskButton))
                .perform(click());
        // select title from dropdown

        // select date and time

        // select user from dropdown

        onView(withId(R.id.addTaskDoneButton))
                .perform(click());

        onView(withId(R.id.task))
                .check(matches(withText("Install and maintain seasonal plants")));

        onView(withId(R.id.task))
                .check(matches(withText("Due: " + date.toString())));

        onView(withId(R.id.task))
                .check(matches(withText("Assigned to: " + user.toString())));
    }
}
