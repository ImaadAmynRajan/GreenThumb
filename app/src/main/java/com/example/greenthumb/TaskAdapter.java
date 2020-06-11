// Adapter code based on https://www.youtube.com/watch?v=17NbUcEts9c (accessed June 7, 2020)
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

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private ArrayList<Task> tasks;

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView dueDate;
        private TextView assignee;
        private TextView claimed;
        private Button claimButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.textViewTaskTitle);
            this.dueDate = itemView.findViewById(R.id.textViewTaskDueDate);
            this.assignee = itemView.findViewById(R.id.textViewTaskAssignee);
            this.claimButton = itemView.findViewById(R.id.claimButton);
            this.claimed = itemView.findViewById(R.id.claimedText);
        }
    }

    public TaskAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(view);
        return taskViewHolder;
    }

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

    @Override
    public int getItemCount() {
        return this.tasks.size();
    }

    private void notifyChanges() {
        this.notifyDataSetChanged();
    }
}
