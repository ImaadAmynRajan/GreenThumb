package com.example.greenthumb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenthumb.tasks.Task;
import com.example.greenthumb.tasks.TaskAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class HomePage extends NavigationBar {
    private TaskAdapter adapter;
    private Button logout_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        super.onCreateNav();

        // initialize RecyclerView
        initializeTaskList();

        // set on-click listener for logout button
        logout_button = findViewById(R.id.logout_button);

    }

    private void initializeTaskList() {
        // get current user's email
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // create query for our tasks, getting only those assigned to the current user
        Query query = FirebaseDatabase.getInstance().getReference().child("tasks")
                .orderByChild("assigneeLabel").equalTo(currentUser);

        // create options for our RecyclerView
        FirebaseRecyclerOptions<Task> options = new FirebaseRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class).setLifecycleOwner(this).build();

        // initialize RecyclerView
        final RecyclerView taskList = findViewById(R.id.recyclerViewUserTasks);
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

    /****
     * this is homeListener method. it handles listeners of the Home Page.
     * @param homeView: this is event object that can handle multiple event listeners.
     *
     */
    public void homeListener(View homeView) {
        if (homeView.getId() == R.id.logout_button) {
            logout_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent loginPage = new Intent(HomePage.this, MainActivity.class);
                    startActivity(loginPage);
                }
            });
        }
    }
}
