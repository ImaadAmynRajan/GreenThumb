package com.example.greenthumb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Represents an Adapter that configures task items in a ViewHolder
 * Adapter code based on https://www.youtube.com/watch?v=17NbUcEts9c (accessed June 7, 2020)
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private ArrayList<Task> tasks;

    /**
     * Represents a ViewHolder that contains tasks
     */
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView dueDate;
        private TextView assignee;
        private TextView claimed;
        private Button claimButton;

        /**
         * Creates a ViewHolder to store task items
         * @param itemView the layout for a task item
         */
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.textViewTaskTitle);
            this.dueDate = itemView.findViewById(R.id.textViewTaskDueDate);
            this.assignee = itemView.findViewById(R.id.textViewTaskAssignee);
            this.claimButton = itemView.findViewById(R.id.claimButton);
            this.claimed = itemView.findViewById(R.id.claimedText);
        }
    }

    /**
     * Creates a TaskAdapter containing a list of tasks
     * @param tasks an ArrayList of tasks
     */
    public TaskAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Initializes a ViewHolder for task items
     * @param parent
     * @param viewType
     * @return initialized task item ViewHolder
     */
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(view);
        return taskViewHolder;
    }

    /**
     * Configures text for a task item layout in the RecyclerView
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, final int position) {
        Task task = this.tasks.get(position);

        String title = task.getTitle();
        String dueDate = task.getDueDate() == null ? "None" : task.getDateString();

        holder.title.setText(title);
        holder.dueDate.setText("Due date: " + dueDate);

        // handling the assignee text
        String assignee;
        if (task.getAssigneeId() == null) {
            assignee = "No one";
            // we want to make the claim button visible if no one is assigned
            holder.claimButton.setVisibility(View.VISIBLE);
            holder.claimed.setVisibility(View.INVISIBLE);
        } else {
            holder.claimButton.setVisibility(View.INVISIBLE);
            holder.claimed.setVisibility(View.VISIBLE);
            assignee = task.getAssigneeLabel();
        }
        // set the assignee text
        holder.assignee.setText("Assigned to: " + assignee);

        // set the click listener for the button
        holder.claimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set up our database reference to the tasks branch
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("tasks");

                // select the current task
                Task task = TaskAdapter.this.tasks.get(position);

                // find our current user based on the current UID
                String curId = FirebaseAuth.getInstance().getUid();
                User user = new User();
                for (User u: ViewTasks.users) {
                    if (u.getId().equals(curId)) {
                        user = new User(u.getId(), u.getEmail());
                        break;
                    }
                }
                // assign our user
                task.setAssignee(user);

                // update the db entry
                db.child(task.getId()).setValue(task);

                // notify changes
                notifyChanges();
            }
        });
    }

    /**
     * Gets the number of tasks in the RecyclerView
     * @return number of tasks in the RecyclerView
     */
    @Override
    public int getItemCount() {
        return this.tasks.size();
    }

    private void notifyChanges() {
        this.notifyDataSetChanged();
    }
}
