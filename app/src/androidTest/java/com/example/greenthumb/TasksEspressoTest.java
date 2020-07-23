package com.example.greenthumb;

import android.os.SystemClock;
import android.text.InputType;
import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withInputType;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class TasksEspressoTest {
    private final String testEmail = "ben.kelly@dal.ca";
    private final String testPassword = "Test1234";
    private final int interval = 3;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void login() {
        // enter test credentials
        onView(withId(R.id.Email)).perform(click()).perform(typeText(testEmail)).perform(closeSoftKeyboard());
        onView(withId(R.id.Password)).perform(click()).perform(typeText(testPassword)).perform(closeSoftKeyboard());

        // login
        onView(withId(R.id.login_button)).perform(click());

        // wait for Home page to load
        SystemClock.sleep(2000);

        //navigate to ViewTasks page & wait for it to load
        onView(withId(R.id.toTaskPage))
                .perform(click());
        SystemClock.sleep(1000);

    }

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
        onView(withClassName(equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2025, 1, 1));
        onView(withText("OK")).perform(click());

        // verify preview text
        onView(withId(R.id.editTextDatePreview)).check(matches(anyOf(withText("Jan. 1, 2025"), withText("Jan 1, 2025"))));
    }

    @Test
    public void verifyRecurringTask(){
        onView(withId(R.id.addTaskButton))
                .perform(click());

        // select date
        onView(withId(R.id.buttonDate)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2020, 11, 4));
        onView(withText("OK")).perform(click());

        // select that this task is recurring
        onView(withId(R.id.recurring)).perform(click());
        onView(withId(R.id.recurringInterval)).perform(click()).perform(typeText(String.valueOf(interval))).perform(closeSoftKeyboard());

        // verify due date
        onView(withId(R.id.editTextDatePreview)).check(matches(anyOf(withText("Nov. 4, 2020"), withText("Nov 4, 2020"))));
        // verify that you can see recurrence interval
        onView(withId(R.id.recurring))
                .check(matches(isDisplayed()));
        // verify that the proper recurrence interval is set
        onView(withId(R.id.recurringInterval)).check(matches(anyOf(withText("3"), withText("3"))));
    }

    @Test
    // checks that the newly added task is visible and displays expected values
    public void testNewTaskVisible(){
        onView(withId(R.id.addTaskButton))
                .perform(click());

        // select title from dropdown
        onView(withId(R.id.spinnerTaskTitle)).perform(click());

        // code snippet from https://stackoverflow.com/questions/39457305/android-testing-waited-for-the-root-of-the-view-hierarchy-to-have-window-focus
        onData(anything()).inRoot(RootMatchers.isPlatformPopup()).atPosition(1).perform(click());

        // select date
        onView(withId(R.id.buttonDate)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).
                perform(PickerActions.setDate(2025, 1, 1));
        onView(withText("OK")).perform(click());

        // submit data
        onView(withText("Add")).perform(click());

        // check that the newest task contains the specified data
        onView(new RecyclerViewMatcher(R.id.recyclerViewTasks).atPosition(getIndexOfLastChildOfViewWithId(R.id.recyclerViewTasks)))
                .check(matches(allOf(hasDescendant(withText("Install and maintain seasonal plants")),
                        hasDescendant(anyOf(withText("Due date: Jan. 1, 2025"), withText("Due date: Jan 1, 2025"))),
                        hasDescendant(withText("Assigned to: No one")))));

        // click options button
        onView(withId(R.id.recyclerViewTasks)).perform(RecyclerViewActions.
                actionOnItemAtPosition(getIndexOfLastChildOfViewWithId(R.id.recyclerViewTasks),
                        CustomRecyclerViewActions.clickChildViewWithId(R.id.taskOptions)));

        // wait for menu items to appear
        SystemClock.sleep(1000);

        // delete the task
        onView(withText(R.string.delete)).perform(click());
    }

    @Test
    // checks that the assign user spinner is visible to users, and allows you to pick a user
    public void assignUserVisible() throws InterruptedException {
        onView(withId(R.id.addTaskButton))
                .perform(click());

        // select user from dropdown
        // code snippet from https://stackoverflow.com/questions/39457305/android-testing-waited-for-the-root-of-the-view-hierarchy-to-have-window-focus
        onView(withId(R.id.spinnerAssignee)).perform(click());

        // this allows for the users to load because the app is too fast otherwise
        SystemClock.sleep(2000);

        onData(anything()).inRoot(RootMatchers.isPlatformPopup()).atPosition(1).perform(click());

        // we won't know which user is there, but we can check that "Select Assignee" wasn't selected
        // meaning we successfully selected a user
        onView(withId(R.id.spinnerAssignee)).check(matches(not(withText("Select Assignee"))));
    }

    @Test
    //check that navigation bar is present
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

    @Test
    // checks that a task is removed from the RecyclerView after a user clicks Delete
    public void testDeleteTask() {
        onView(withId(R.id.addTaskButton))
                .perform(click());

        // select title from dropdown
        onView(withId(R.id.spinnerTaskTitle)).perform(click());

        // code snippet from https://stackoverflow.com/questions/39457305/android-testing-waited-for-the-root-of-the-view-hierarchy-to-have-window-focus
        onData(anything()).inRoot(RootMatchers.isPlatformPopup()).atPosition(1).perform(click());

        // submit data
        onView(withText("Add")).perform(click());

        // get number of tasks
        int numberOfTasks = getChildCountOfViewWithId(R.id.recyclerViewTasks);

        // click options button
        onView(withId(R.id.recyclerViewTasks)).perform(RecyclerViewActions.
                actionOnItemAtPosition(getIndexOfLastChildOfViewWithId(R.id.recyclerViewTasks), CustomRecyclerViewActions.clickChildViewWithId(R.id.taskOptions)));

        // wait for menu items to appear
        SystemClock.sleep(1000);

        // click Delete button
        onView(withText(R.string.delete)).perform(click());

        // assert that there is now one less task in the RecyclerView
        onView(withId(R.id.recyclerViewTasks)).check(matches(hasChildCount(numberOfTasks - 1)));
    }

    @Test
    public void overdueIconTest(){
        onView(withId(R.id.addTaskButton))
                .perform(click());

        // select title from dropdown
        onView(withId(R.id.spinnerTaskTitle)).perform(click());
        onData(anything()).inRoot(RootMatchers.isPlatformPopup()).atPosition(1).perform(click());

        // select date in the past
        onView(withId(R.id.buttonDate)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(1980, 1, 1));
        onView(withText("OK")).perform(click());

        // add the task
        SystemClock.sleep(1000);
        onView(withText("Add")).perform(click());

        // check that the newest task displays the overdue icon
        onView(new RecyclerViewMatcher(R.id.recyclerViewTasks).atPosition(getIndexOfLastChildOfViewWithId(R.id.recyclerViewTasks)))
                .check(matches(allOf(hasDescendant(allOf(withId(R.id.overdue), isDisplayed())))));
    }

    /**
     * Returns the number of child views contained within a view
     * @param id id of the parent view
     * @return the number of child views contained within the view
     */
    private int getChildCountOfViewWithId(int id) {
        int count = 0;

        // try matching the view with an incrementing number of child views
        while (count < 10000) {
            try {
                onView(withId(id)).check(matches(hasChildCount(count)));
                return count;
            } catch (Error failedAssertion) {
                count++;
            }
        }

        return 0;
    }

    /**
     * Returns the index of the last child in a view
     * @param id id of the parent view
     * @return the index of the last child in the view
     */
    private int getIndexOfLastChildOfViewWithId(int id) {
        int numberOfChildren = getChildCountOfViewWithId(id);
        if (numberOfChildren > 0) return numberOfChildren - 1;
        return 0;
    }
}
