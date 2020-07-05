package com.example.greenthumb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Represents an Adapter that configures task items in a ViewHolder
 * Adapter code based on https://www.youtube.com/watch?v=17NbUcEts9c (accessed June 7, 2020)
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private Context context;
    private ArrayList<Task> tasks;

    /**
     * Represents a ViewHolder that contains tasks
     */
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView dueDate;
        private TextView assignee;
        private ImageButton options;
        private TextView doneText;
        private ImageView checkmark;

        /**
         * Creates a ViewHolder to store task items
         * @param itemView the layout for a task item
         */
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.textViewTaskTitle);
            this.dueDate = itemView.findViewById(R.id.textViewTaskDueDate);
            this.assignee = itemView.findViewById(R.id.textViewTaskAssignee);
            this.options = itemView.findViewById(R.id.taskOptions);
            this.doneText = itemView.findViewById(R.id.textViewDone);
            this.checkmark = itemView.findViewById(R.id.checkmark);
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
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(view);
        return taskViewHolder;
    }

    /**
     * Configures text for a task item layout in the RecyclerView
     * @param holder the holder represents a task in the view
     * @param position the position in the arraylist of that holder
     */
    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder holder, final int position) {
        final Task task = this.tasks.get(position);

        String title = task.getTitle();
        String dueDate = task.getDueDate() == null ? "None" : task.getDateString();

        holder.title.setText(title);
        holder.dueDate.setText("Due date: " + dueDate);

        // set the assignee text
        String assignee = task.getAssigneeId() == null ? "No one" : task.getAssigneeLabel();
        holder.assignee.setText("Assigned to: " + assignee);

        // make done indicators visible only if task is marked as done
        holder.doneText.setVisibility(task.isFinished() ? View.VISIBLE : View.INVISIBLE);
        holder.checkmark.setVisibility(task.isFinished() ? View.VISIBLE : View.INVISIBLE);

        // configure options menu
        // popup menu code based on https://www.javatpoint.com/android-popup-menu-example#:~:text=%E2%86%92%20%E2%86%90%20prev-,Android%20Popup%20Menu%20Example,The%20android. (accessed June 26, 2020)
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.options);
                popupMenu.getMenuInflater().inflate(R.menu.task_options, popupMenu.getMenu());

                // enable/disable menu options
                MenuItem claimButton = popupMenu.getMenu().findItem(R.id.claimButton);
                claimButton.setEnabled(task.getAssigneeId() == null);

                MenuItem doneButton = popupMenu.getMenu().findItem(R.id.doneButton);
                doneButton.setEnabled(!task.isFinished());

                // set event handlers for menu options
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {
                            case R.id.claimButton:
                                if (item.isEnabled()) {
                                    // set up our database reference to the tasks branch
                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("tasks");

                                    // select the current task
                                    Task task = TaskAdapter.this.tasks.get(position);

                                    // get the details of the current user
                                    FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
                                    User user = new User(curUser.getUid(), curUser.getEmail());

                                    // assign our user
                                    task.setAssignee(user);

                                    // update the db entry
                                    db.child(task.getId()).setValue(task);

                                    // notify the class that there has been changes and we need to update the UI
                                    TaskAdapter.this.notifyDataSetChanged();
                                }
                                break;
                            case R.id.doneButton:
                                if (item.isEnabled()) {
                                    // set up our database reference to the tasks branch
                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("tasks");

                                    // select the current task
                                    Task task = TaskAdapter.this.tasks.get(position);
                                    
                                    // mark current task as finished
                                    task.markAsFinished();

                                    // update the db entry
                                    db.child(task.getId()).setValue(task);

                                    // notify the class that there has been changes and we need to update the UI
                                    TaskAdapter.this.notifyDataSetChanged();
                                }
                                break;
                            case R.id.deleteButton:
                                // set up our database reference to the tasks branch
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("tasks");

                                // select the current task
                                Task task = TaskAdapter.this.tasks.get(position);

                                // remove task from our task list
                                TaskAdapter.this.tasks.remove(task);

                                // remove task from the database
                                db.child(task.getId()).removeValue();

                                // notify the class that there has been changes and we need to update the UI
                                TaskAdapter.this.notifyDataSetChanged();

                                break;
                            default:
                                return false;
                        }

                        return true;
                    }
                });

                popupMenu.show();
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
}
