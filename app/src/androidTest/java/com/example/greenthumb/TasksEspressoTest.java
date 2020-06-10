package com.example.greenthumb;

import android.text.InputType;
import android.view.KeyEvent;
import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withInputType;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;

public class TasksEspressoTest {

    @Rule
    public ActivityScenarioRule<ViewTasks> activityScenarioRule
            = new ActivityScenarioRule<>(ViewTasks.class);

    // checks that the "Add Task" dialog contains four views
    @Test
    public void verifyViewCount(){
        onView(withId(R.id.addTaskButton))
                .perform(click());

        onView(withId(R.id.layout_add_task)).check(matches(hasChildCount(4)));
    }

    // checks that the date preview is not editable
    @Test
    public void verifyDateTextNotEditable(){
        onView(withId(R.id.addTaskButton))
                .perform(click());

        onView(withId(R.id.editTextDatePreview)).check(matches(withInputType(InputType.TYPE_NULL)));
    }

    // checks that the date preview displays correctly
    @Test
    public void verifyDateTextPreview(){
        onView(withId(R.id.addTaskButton))
                .perform(click());

        // select date
        onView(withId(R.id.buttonDate)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).
                perform(PickerActions.setDate(1970, 1, 1));
        onView(withText("OK")).perform(click());

        // verify preview text
        onView(withId(R.id.editTextDatePreview)).check(matches(withText("Jan 1, 1970")));
    }

    @Test
    // checks that the newly added task is visible and displays expected values
    public void testNewTaskVisible(){
        onView(withId(R.id.addTaskButton))
                .perform(click());

        // select title from dropdown
        // code snippet from https://stackoverflow.com/questions/39457305/android-testing-waited-for-the-root-of-the-view-hierarchy-to-have-window-focus
        onView(withId(R.id.spinnerTaskTitle)).perform(click());
        onData(anything()).inRoot(RootMatchers.isPlatformPopup()).atPosition(1).perform(click());

        // select date
        onView(withId(R.id.buttonDate)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).
                perform(PickerActions.setDate(1970, 1, 1));
        onView(withText("OK")).perform(click());

        // submit data
        onView(withText("Add")).perform(click());

        // check that there exists a task with the specified data
        onView(withText("Install and maintain seasonal plants")).check(matches(isDisplayed()));
        onView(withText("Due date: Jan 1, 1970")).check(matches(isDisplayed()));
        onView(withText("Assigned to: No one")).check(matches(isDisplayed()));
    }
}
