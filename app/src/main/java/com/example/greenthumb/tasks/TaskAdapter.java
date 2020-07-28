package com.example.greenthumb.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.fragment.app.FragmentActivity;

import com.example.greenthumb.AddTaskDialog;
import com.example.greenthumb.R;
import com.example.greenthumb.User;
import com.example.greenthumb.trade.TradeRequest;
import com.example.greenthumb.trade.TradeRequestViewModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Represents a custom adapter for Tasks in the RecyclerView.
 */
public class TaskAdapter extends FirebaseRecyclerAdapter<Task, TaskHolder> {
    private Context context = null;
    private TaskAdapter adapter; // need to store self to pass to new AddTaskDialog in onClickListener

    public TaskAdapter(FirebaseRecyclerOptions<Task> options) {
        super(options);
        adapter = this;
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
    protected void onBindViewHolder(final TaskHolder holder, int position, final Task model) {
        // store associated task
        final TaskViewModel task = new TaskViewModel(model);
        // use this so we can enable/disable the trade button
        final User curUser = new User(FirebaseAuth.getInstance().getUid(),
                FirebaseAuth.getInstance().getCurrentUser().getEmail());

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
                MenuItem editButton = popupMenu.getMenu().findItem(R.id.editButton);
                editButton.setEnabled(!task.isClaimed());

                MenuItem claimButton = popupMenu.getMenu().findItem(R.id.claimButton);
                claimButton.setEnabled(!task.isClaimed());

                MenuItem doneButton = popupMenu.getMenu().findItem(R.id.doneButton);
                doneButton.setEnabled(!task.isFinished());

                MenuItem tradeButton = popupMenu.getMenu().findItem(R.id.tradeButton);
                tradeButton.setEnabled(task.getAssigneeId() != null && !task.getAssigneeLabel().equals(curUser.getEmail()));

                // set event handlers for menu options
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.editButton:
                                if (item.isEnabled()) {
                                    showEditTaskDialog(model);
                                    notifyDataSetChanged();
                                }
                                break;
                            case R.id.claimButton:
                                if (item.isEnabled()) {
                                    task.claim();
                                    notifyDataSetChanged();
                                }
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
                            case R.id.tradeButton:
                                TradeRequestViewModel trade = new TradeRequestViewModel(new TradeRequest(null, curUser, model));
                                trade.save();
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
     * Displays the AddTask dialog in edit mode
     * @param model
     */
    private void showEditTaskDialog(final Task model) {
        AddTaskDialog editTaskDialog = new AddTaskDialog(adapter, model);
        editTaskDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "edit a task");
    }
}