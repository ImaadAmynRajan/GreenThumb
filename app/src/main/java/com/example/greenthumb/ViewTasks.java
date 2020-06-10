// RecyclerView code based on https://www.youtube.com/watch?v=17NbUcEts9c (accessed June 7, 2020)

package com.example.greenthumb;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ViewTasks extends AppCompatActivity implements AddTaskDialog.AddTaskDialogListener {
    private RecyclerView taskRecyclerView;
    private RecyclerView.Adapter taskAdapter;
    private RecyclerView.LayoutManager taskLayoutManager;

    private ArrayList<Task> tasks;

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
        this.tasks = new ArrayList<>();
        getTasks();

        // initialize RecyclerView
        this.taskRecyclerView = findViewById(R.id.recyclerViewTasks);
        this.taskRecyclerView.setHasFixedSize(true);

        this.taskAdapter = new TaskAdapter(tasks);
        this.taskLayoutManager = new LinearLayoutManager(this);

        this.taskRecyclerView.setAdapter(this.taskAdapter);
        this.taskRecyclerView.setLayoutManager(this.taskLayoutManager);
    }

    private void openNewTaskDialog() {
        AddTaskDialog addTaskDialog = new AddTaskDialog();
        addTaskDialog.show(getSupportFragmentManager(), "add new task");
    }

    /*
    This function retrieves all the tasks from the data base.
    One it takes a snapshot of the database, it cycles through all the objects
    and creates tasks out of their information.
    It then sends that data collectTasks function
     */
    private void getTasks() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("tasks");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Task> tasks = new ArrayList<>();
                for (DataSnapshot dp: dataSnapshot.getChildren()) {
                    Map<String, Object> info = (Map<String, Object>) dp.getValue();
                    String assigneeLabel, assigneeId;
                    Date date;

                    // set all our values
                    String title = (String) info.get("title");
                    String id = (String) dp.getKey();

                    if (info.get("date") != null) {
                        date = new Date((Long) info.get("date"));
                    } else {
                        date = null;
                    }

                    if (info.get("assigneeId") != null) {
                        assigneeId = (String) info.get("assigneeId");
                    } else {
                        assigneeId = null;
                    }

                    if (info.get("assigneeLabel") != null) {
                        assigneeLabel = (String) info.get("assigneeLabel");
                    } else {
                        assigneeLabel = null;
                    }

                    tasks.add(new Task(id, title, date, new User(assigneeId, assigneeLabel)));
                }
                // send our tasks to be added to our recycle viewer
                collectTasks(tasks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*
    This method is called after we have retrieved our tasks from the database
    It will take in the list of tasks and set it equal to the classes tasks
    After it has done that we notify the task adapter that changes have occurred so
    The view will update and show the tasks
     */
    private void collectTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
        this.taskAdapter.notifyDataSetChanged();
    }

    @Override
    public void addTask(String title, Date dueDate, Object assignee) {
        // get our database reference to the tasks branch
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("tasks");
        // create a new id for the task
        String id = db.push().getKey();
        // create a task
        Task newTask = new Task(id, title, dueDate, (User) assignee);

        // push that task to the database
        db.child(id).setValue(newTask);

        // add task to ViewTasks screen
        this.tasks.add(newTask);
        this.taskAdapter.notifyDataSetChanged();
    }
}
