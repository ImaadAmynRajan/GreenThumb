// Adapter code based on https://www.youtube.com/watch?v=17NbUcEts9c (accessed June 7, 2020)
package com.example.greenthumb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private ArrayList<Task> tasks;

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView dueDate;
        private TextView assignee;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.textViewTaskTitle);
            this.dueDate = itemView.findViewById(R.id.textViewTaskDueDate);
            this.assignee = itemView.findViewById(R.id.textViewTaskAssignee);
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
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = this.tasks.get(position);

        String title = task.getTitle();
        String dueDate = task.getDueDate() == null ? "None" : task.getDateString();
        String assignee = task.getAssigneeLabel() == null ? "No one" : task.getAssigneeLabel();

        holder.title.setText(title);
        holder.dueDate.setText("Due date: " + dueDate);
        holder.assignee.setText("Assigned to: " + assignee);
    }

    @Override
    public int getItemCount() {
        return this.tasks.size();
    }
}
