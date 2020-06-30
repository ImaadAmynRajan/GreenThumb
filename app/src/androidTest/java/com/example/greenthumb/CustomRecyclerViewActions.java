package com.example.greenthumb;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

/**
 * Contains custom ViewActions to perform on recycler views.
 * Code based on https://stackoverflow.com/questions/28476507/using-espresso-to-click-view-inside-recyclerview-item (accessed June 26, 2020)
 * @author Ben Kelly
 */
public class CustomRecyclerViewActions {

    /**
     * Clicks on a view within a recycler view holder
     * @param id the id of the view to be clicked
     * @return a click ViewAction object
     */
    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with a specific id";
            }

            @Override
            public void perform(UiController controller, View view) {
                View child = view.findViewById(id);
                child.performClick();
            }
        };
    }
}
