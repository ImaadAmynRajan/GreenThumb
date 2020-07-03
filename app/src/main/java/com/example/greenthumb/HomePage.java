package com.example.greenthumb;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class HomePage extends NavigationBar {
    private RecyclerView taskRecyclerView;
    private RecyclerView.Adapter taskAdapter;
    private RecyclerView.LayoutManager taskLayoutManager;

    private ArrayList<Task> tasks;
    public static ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        super.onCreateNav();

        // init task list list
        this.tasks = new ArrayList<>();
        
        // collect all the users and tasks we have in our database
        getTasks();

        // initialize RecyclerView
        this.taskRecyclerView = findViewById(R.id.recyclerViewUserTasks);
        this.taskRecyclerView.setHasFixedSize(true);

        this.taskAdapter = new TaskAdapter(tasks);
        this.taskLayoutManager = new LinearLayoutManager(this);

        this.taskRecyclerView.setAdapter(this.taskAdapter);
        this.taskRecyclerView.setLayoutManager(this.taskLayoutManager);
    }

    /**
     * This function retrieves all the tasks from the data base.
     * Once it takes a snapshot of the database,
     * it  sends them to the collectTasks function where they are turned into task objects
     * Reference: https://stackoverflow.com/questions/32886546/how-to-get-all-child-list-from-firebase-android
     */
    private void getTasks() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("tasks");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // send our tasks to be added to our recycle viewer
                collectTasks(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method is called after we have retrieved our tasks from the database
     * It will cycle through the tasks and create task objects out of them
     * Reference: https://stackoverflow.com/questions/32886546/how-to-get-all-child-list-from-firebase-android
     * @param dataSnapshot A snapshot of the tasks branch of the database
     */
    private void collectTasks(DataSnapshot dataSnapshot) {
        for (DataSnapshot dp: dataSnapshot.getChildren()) {
            Map<String, Object> info = (Map<String, Object>) dp.getValue();
            String assigneeLabel, assigneeId;
            Date date;
            boolean isFinished;

            // ensure that assignee label matches email of current user
            if (info.get("assigneeLabel") != null) {
                assigneeLabel = (String) info.get("assigneeLabel");
                if (!assigneeLabel.equals(CurrentUser.email)) {
                    continue;
                }
            } else {
                continue;
            }

            // set all our values
            String title = (String) info.get("title");
            String id = (String) dp.getKey();

            if (info.get("dueDate") != null) {
                date = new Date((Long) info.get("dueDate"));
            } else {
                date = null;
            }

            if (info.get("assigneeId") != null) {
                assigneeId = (String) info.get("assigneeId");
            } else {
                assigneeId = null;
            }

            if (info.get("finished") != null) {
                isFinished = (boolean) info.get("finished");
            } else {
                isFinished = false;
            }

            this.tasks.add(new Task(id, title, date, new User(assigneeId, assigneeLabel), isFinished));
        }
        this.taskAdapter.notifyDataSetChanged();
    }
}
