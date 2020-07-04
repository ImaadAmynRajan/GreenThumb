package com.example.greenthumb;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Represents the UI that users use to add new tasks.
 * Code for building a dialog in Android based on https://www.youtube.com/watch?v=ARezg1D9Zd0 (accessed 29 May, 2020)
 */
public class AddTaskDialog extends AppCompatDialogFragment {
    private Spinner spinnerTaskTitle;
    private Spinner spinnerAssignee;
    private EditText editTextDatePreview;
    private Button buttonDate;

    private String taskTitle = null;
    private Date dueDate = null;

    private ArrayList<User> users;

    private AddTaskDialogListener listener;

    /**
     * Initializes AddTaskDialog. Sets onClick events.
     * @param savedInstanceState
     * @return the configured AddTaskDialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // load all the users from the platform
        users = new ArrayList<>();
        getUsers();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_task_dialog, null);

        builder.setView(view)
                .setTitle("New Task")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // get task title and assignee
                        taskTitle = spinnerTaskTitle.getSelectedItem().toString();
                        Object assignee = getAssigneeSelection();

                        if (verifyInput()) {
                            // add task to database and to ViewTasks screen
                            listener.addTask(taskTitle, dueDate, assignee);
                        }
                    }
                });

        spinnerTaskTitle = view.findViewById(R.id.spinnerTaskTitle);
        spinnerAssignee = view.findViewById(R.id.spinnerAssignee);

        editTextDatePreview = view.findViewById(R.id.editTextDatePreview);
        buttonDate = view.findViewById(R.id.buttonDate);

        final Calendar calendar = Calendar.getInstance();

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // get date selection
                        calendar.set(year, month, dayOfMonth);

                        // format date selection to Month Day, Year
                        DateFormat dateFormat = DateFormat.getDateInstance();
                        String dateString = dateFormat.format(calendar.getTime());

                        // update preview text
                        editTextDatePreview.setText(dateString);

                        dueDate = new Date(year - 1900, month, dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        return builder.create();
    }

    // onAttach method based on https://www.youtube.com/watch?v=ARezg1D9Zd0 (accessed June 7, 2020)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            this.listener = (AddTaskDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddTaskDialogListener");
        }
    }

    /**
     * Returns true only if a task title has been selected
     * @return a boolean indicating whether the input is valid
     */
    private boolean verifyInput() {
        return !taskTitle.equals("Select task");
    }

    /*
    This will load the assignee option into a spinner that the user can select from
    Reference https://stackoverflow.com/questions/41809942/how-to-programatically-set-entries-of-spinner-in-android
     */
    private void loadAssigneeOptions(int spinner) {
        String [] userLabels = new String[users.size() + 1];
        userLabels[0] = "Select Assignee";
        for (int i = 0; i < users.size(); i++) {
            userLabels[i + 1] = users.get(i).getEmail();
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this.getContext(), spinner, userLabels);
        this.spinnerAssignee.setAdapter(spinnerArrayAdapter);
    }

    // gets User based on spinner selection
    private Object getAssigneeSelection() {
        String assignee = this.spinnerAssignee.getSelectedItem().toString();
        // now find it in our user array
        for (User u : users) {
            if (u.getEmail().equals(assignee)) {
                return u;
            }
        }
        return null;
    }

    /**
     * This function gets a list of users that we have in our database
     * We need this list of users so we can assign tasks to users
     * Reference https://stackoverflow.com/questions/32886546/how-to-get-all-child-list-from-firebase-android
     */
    private void getUsers() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // send the users to be turned into User objects
                collectUsers(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * This function is used as the callback for getUsers
     * This allows us to assign our list of users to our class variable after they have
     * been retrieved
     * Reference https://stackoverflow.com/questions/32886546/how-to-get-all-child-list-from-firebase-android
     * @param dataSnapshot A snap shot of the users branch of the database
     */
    private void collectUsers(DataSnapshot dataSnapshot) {
        for (DataSnapshot dp: dataSnapshot.getChildren()) {
            User newUser = dp.getValue(User.class);
            /*String id = dp.getKey();
            String email = (String) dp.getValue();*/
            users.add(newUser);
        }
        // now that we have the users, load them into the spinner
        loadAssigneeOptions(R.layout.support_simple_spinner_dropdown_item);
    }

    /**
     * Interface for activities to interact with the dialog for adding tasks
     */
    public interface AddTaskDialogListener {
        /**
         * Adds a task to the app's database and ArrayList of tasks
         * @param title description of the task
         * @param dueDate date by which the task must be completed
         * @param assignee user to which the task has been assigned
         */
        void addTask(String title, Date dueDate, Object assignee);
    }
}
