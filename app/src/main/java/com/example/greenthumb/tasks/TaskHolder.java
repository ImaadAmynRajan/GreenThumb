package com.example.greenthumb.tasks;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.greenthumb.R;

/**
 * Represents a ViewHolder for tasks.
 */
public class TaskHolder extends RecyclerView.ViewHolder {
    public ImageButton options;

    private TextView title;
    private TextView dueDate;
    private TextView assignee;
    private TextView doneText;
    private ImageView checkmark;
    private TextView overdueText;
    private ImageView clock;

    /**
     * Instantiates a new TaskHolder and gets references to all the views contained within it.
     * @param view
     */
    public TaskHolder(View view) {
        super(view);
        this.title = view.findViewById(R.id.textViewTaskTitle);
        this.dueDate = view.findViewById(R.id.textViewTaskDueDate);
        this.assignee = view.findViewById(R.id.textViewTaskAssignee);
        this.options = view.findViewById(R.id.taskOptions);
        this.doneText = view.findViewById(R.id.textViewDone);
        this.checkmark = view.findViewById(R.id.checkmark);
        this.overdueText = view.findViewById(R.id.textViewOverdue);
        this.clock = view.findViewById(R.id.overdue);
    }

    /**
     * Sets the title field of a task in the RecyclerView.
     * @param title the title of the associated task
     * @param taskTitles an array of all task titles
     */
    public void setTitle(TaskTitle title, String[] taskTitles) {
        int titleIndex = TaskTitle.titleToInt(title);
        this.title.setText(taskTitles[titleIndex]);
    }

    /**
     * Sets the due date field of a task in the RecyclerView.
     * @param dateString the due date of the associated task
     */
    public void setDueDate(String dateString) {
        this.dueDate.setText(String.format("Due date: %s", dateString == null ? "None" : dateString));
    }

    /**
     * Sets the assignee field of a task in the RecyclerView.
     * @param assigneeLabel the email of the user assigned to the associated task
     */
    public void setAssignee(String assigneeLabel) {
        this.assignee.setText(String.format("Assigned to: %s", assigneeLabel == null ? "No one" : assigneeLabel));
    }

    /**
     * Shows or hides the word "Done" and a checkmark in the view.
     * @param done a boolean value describing whether the associated task has been marked as done
     */
    public void toggleDoneIndicators(boolean done) {
        this.doneText.setVisibility(done ? View.VISIBLE : View.INVISIBLE);
        this.checkmark.setVisibility(done ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * Shows or hides the word "Overdue" and a clock in the view.
     * @param overdue a boolean value describing whether the associated task is overdue
     */
    public void toggleOverdueIndicators(boolean overdue) {
        this.overdueText.setVisibility(overdue ? View.VISIBLE : View.INVISIBLE);
        this.clock.setVisibility(overdue ? View.VISIBLE : View.INVISIBLE);
    }
}
