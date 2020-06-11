package com.example.greenthumb;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

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

    private AddTaskDialogListener listener;

    /**
     * Initializes AddTaskDialog. Sets onClick events.
     * @param savedInstanceState
     * @return the configured AddTaskDialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
        //loadAssigneeOptions();

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

    // gets assignee options from database and adds them to assignee spinner
    private void loadAssigneeOptions() {

    }

    // gets User based on spinner selection
    private Object getAssigneeSelection() {
        return null;
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
