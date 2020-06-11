package com.example.greenthumb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        /**
         * Creates a ViewHolder to store task items
         * @param itemView the layout for a task item
         */
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.textViewTaskTitle);
            this.dueDate = itemView.findViewById(R.id.textViewTaskDueDate);
            this.assignee = itemView.findViewById(R.id.textViewTaskAssignee);
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
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = this.tasks.get(position);

        String title = task.getTitle();
        String dueDate = task.getDueDate() == null ? "None" : task.getDateString();
        String assignee = task.getAssignee() == null ? "No one" : task.getAssignee().toString();

        holder.title.setText(title);
        holder.dueDate.setText("Due date: " + dueDate);
        holder.assignee.setText("Assigned to: " + assignee);
    }

    /**
     * Gets the number of tasks in the RecyclerView
     * @return number of tasks in the RecyclerView
     */
    @Override
    public int getItemCount() {
        return this.tasks.size();
    }
}
