// RecyclerView code based on https://www.youtube.com/watch?v=17NbUcEts9c (accessed June 7, 2020)

package com.example.greenthumb;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Represents the activity in which user's can view and add tasks
 * RecyclerView code based on https://www.youtube.com/watch?v=17NbUcEts9c (accessed June 7, 2020)
 */
public class ViewTasks extends NavigationBar implements AddTaskDialog.AddTaskDialogListener {
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
        super.onCreateNav();

        FloatingActionButton newTaskButton = findViewById(R.id.addTaskButton);
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewTaskDialog();
            }
        });

        // init task list and user list
        this.tasks = new ArrayList<>();
        // collect all tasks we have in our database
        getTasks();


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

            if (info.get("assigneeLabel") != null) {
                assigneeLabel = (String) info.get("assigneeLabel");
            } else {
                assigneeLabel = null;
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

    /**
     * Adds a task to the app's database and ArrayList of tasks
     * @param title description of the task
     * @param dueDate date by which the task must be completed
     * @param assignee user to which the task has been assigned
     */
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
        this.tasks.add(0, newTask);
        this.taskAdapter.notifyDataSetChanged();
    }
}
