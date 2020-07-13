package com.example.greenthumb.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.example.greenthumb.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

/**
 * Represents a custom adapter for Tasks in the RecyclerView.
 */
public class TaskAdapter extends FirebaseRecyclerAdapter<Task, TaskHolder> {
    private Context context = null;

    public TaskAdapter(FirebaseRecyclerOptions<Task> options) {
        super(options);
    }

    @Override
    public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.task_item, parent, false);
        final TaskHolder holder = new TaskHolder(view);
        return holder;
    }

    @Override
    protected void onBindViewHolder(final TaskHolder holder, int position, Task model) {
        // store associated task
        final TaskViewModel task = new TaskViewModel(model);

        // set text on TaskHolder
        holder.setTitle(model.getTitle(), context.getResources().getStringArray(R.array.task_titles));
        holder.setDueDate(model.getDateString());
        holder.setAssignee(model.getAssigneeLabel());

        // toggle done/overdue indicators
        holder.toggleDoneIndicators(task.isFinished());
        holder.toggleOverdueIndicators(task.isOverdue());

        // set on-click methods for the options button and buttons in the options menu
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
                        switch (item.getItemId()) {
                            case R.id.claimButton:
                                if (item.isEnabled())
                                    task.claim();
                                    notifyDataSetChanged();
                                break;
                            case R.id.doneButton:
                                if (item.isEnabled()) {
                                    task.markAsDone();
                                    notifyDataSetChanged();
                                }
                                break;
                            case R.id.deleteButton:
                                task.delete();
                                notifyDataSetChanged();
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
}