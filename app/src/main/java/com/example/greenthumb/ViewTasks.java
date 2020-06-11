package com.example.greenthumb;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents the activity in which user's can view and add tasks
 * RecyclerView code based on https://www.youtube.com/watch?v=17NbUcEts9c (accessed June 7, 2020)
 */
public class ViewTasks extends AppCompatActivity implements AddTaskDialog.AddTaskDialogListener {
    private RecyclerView taskRecyclerView;
    private RecyclerView.Adapter taskAdapter;
    private RecyclerView.LayoutManager taskLayoutManager;

    private ArrayList<Task> tasks;

    /**
     * Initializes the ViewTasks activity. Populates RecyclerView of tasks.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        FloatingActionButton newTaskButton = findViewById(R.id.addTaskButton);
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewTaskDialog();
            }
        });

        // populate task list
        this.tasks = getTasks();

        // initialize RecyclerView
        this.taskRecyclerView = findViewById(R.id.recyclerViewTasks);
        this.taskRecyclerView.setHasFixedSize(true);

        this.taskAdapter = new TaskAdapter(tasks);
        this.taskLayoutManager = new LinearLayoutManager(this);

        this.taskRecyclerView.setAdapter(this.taskAdapter);
        this.taskRecyclerView.setLayoutManager(this.taskLayoutManager);
    }

    /**
     * Creates and shows an instance of AddTaskDialog
     */
    private void openNewTaskDialog() {
        AddTaskDialog addTaskDialog = new AddTaskDialog();
        addTaskDialog.show(getSupportFragmentManager(), "add new task");
    }

    /**
     * Reads tasks from the database and returns them in an ArrayList
     * @return an ArrayList of Task objects stored in the database
     */
    private ArrayList<Task> getTasks() {
        //TODO: Read tasks from database
        return new ArrayList<>();
    }

    /**
     * Adds a task to the app's database and ArrayList of tasks
     * @param title description of the task
     * @param dueDate date by which the task must be completed
     * @param assignee user to which the task has been assigned
     */
    @Override
    public void addTask(String title, Date dueDate, Object assignee) {
        // add task to database
        Task newTask = new Task(title, dueDate, assignee);
        newTask.writeToDatabase();

        // add task to ViewTasks screen
        this.tasks.add(newTask);
        this.taskAdapter.notifyDataSetChanged();
    }
}
