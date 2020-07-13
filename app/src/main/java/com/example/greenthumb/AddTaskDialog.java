package com.example.greenthumb;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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

import androidx.databinding.DataBindingUtil;
import com.example.greenthumb.databinding.AddTaskDialogBinding;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Represents the UI that users use to add new tasks.
 * Code for building a dialog in Android based on https://www.youtube.com/watch?v=ARezg1D9Zd0 (accessed 29 May, 2020)
 */
public class AddTaskDialog extends AppCompatDialogFragment {
    private TaskAdapter adapter;
    private AddTaskDialogBinding binding;
    private Spinner spinnerAssignee;
    private EditText editTextDatePreview;
    private ArrayList<User> users;

    /**
     * Instantiates a new AddTaskDialog
     * @param adapter the TaskAdapter for the RecyclerView in the activity that this dialog is opened in
     */
    public AddTaskDialog(TaskAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Initializes AddTaskDialog. Sets onClick events.
     * @param savedInstanceState
     * @return the configured AddTaskDialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // create a new task
        Task task = new Task(null, TaskTitle.None, null,  null);

        // configure databinding
        binding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.add_task_dialog, null, false);
        binding.setTask(new TaskViewModel(task));
        binding.setLifecycleOwner(this);

        // load all the users from the platform
        users = new ArrayList<>();
        getUsers();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_task_dialog, null);

        //builder.setView(binding.getRoot())
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
                        saveTask();
                    }
                });

        // configure event listener for task title selection
        Spinner spinnerTaskTitle = view.findViewById(R.id.spinnerTaskTitle);
        spinnerTaskTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.getTask().setTitle(TaskTitle.intToTitle(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        // configure event listener for assignee selection
        spinnerAssignee = view.findViewById(R.id.spinnerAssignee);
        spinnerAssignee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.getTask().setAssignee(getAssigneeSelection());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        editTextDatePreview = view.findViewById(R.id.editTextDatePreview);
        Button buttonDate = view.findViewById(R.id.buttonDate);

        // configure event listener for due date selection
        final Calendar calendar = Calendar.getInstance();
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // get date selection
                        calendar.set(year, month, dayOfMonth);

                        // format date selection to Month Day, Year
                        DateFormat dateFormat = DateFormat.getDateInstance();
                        String dateString = dateFormat.format(calendar.getTime());

                        // update preview text
                        editTextDatePreview.setText(dateString);

                        Date dueDate = new Date(year - 1900, month, dayOfMonth);
                        binding.getTask().setDueDate(dueDate.getTime());
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                /* disables selection of past dates
                code snippet from https://stackoverflow.com/questions/23762231/how-to-disable-past-dates-in-android-date-picker */
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        return builder.create();
    }

    /**
     * Saves the task to the database.
     */
    private void saveTask() {
        if (verifyInput()) {
            binding.getTask().save();
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Returns true only if a task title has been selected
     * @return a boolean indicating whether the input is valid
     */
    private boolean verifyInput() {
        return binding.getTask().getTitle() != TaskTitle.None;
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
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this.getContext(), spinner, userLabels);
        this.spinnerAssignee.setAdapter(spinnerArrayAdapter);
    }

    // gets User based on spinner selection
    private User getAssigneeSelection() {
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
}
