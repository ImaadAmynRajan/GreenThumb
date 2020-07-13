package com.example.greenthumb;

import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenthumb.tasks.Task;
import com.example.greenthumb.tasks.TaskAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.firebase.ui.database.FirebaseRecyclerOptions;

/**
 * Represents the activity in which user's can view and add tasks
 */
public class ViewTasks extends NavigationBar {
    private TaskAdapter adapter;

    /**
     * Initializes the ViewTasks activity. Populates RecyclerView of tasks.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);
        super.onCreateNav();

        // initialize RecyclerView
        initializeTaskList();
    }

    private void initializeTaskList() {
        // create query for our tasks
        Query query = FirebaseDatabase.getInstance().getReference().child("tasks");

        // create options for our RecyclerView
        FirebaseRecyclerOptions<Task> options = new FirebaseRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class).setLifecycleOwner(this).build();

        // initialize RecyclerView
        final RecyclerView taskList = (RecyclerView) findViewById(R.id.recyclerViewTasks);
        taskList.setLayoutManager(new LinearLayoutManager(this));

        // create RecyclerView adapter
        adapter = new TaskAdapter(options);

        adapter.notifyDataSetChanged();
        taskList.setAdapter(adapter);
    }

    /**
     * Creates and shows an instance of AddTaskDialog
     */
    public void openNewTaskDialog(View view) {
        AddTaskDialog addTaskDialog = new AddTaskDialog(adapter);
        addTaskDialog.show(getSupportFragmentManager(), "add new task");
    }
}
