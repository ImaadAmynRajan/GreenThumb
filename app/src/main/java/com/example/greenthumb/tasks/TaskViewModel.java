package com.example.greenthumb.tasks;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.greenthumb.BR;
import com.example.greenthumb.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Represents a ViewModel for Task objects.
 * Based on ContactViewModel.java from Assignment 4.
 */
public class TaskViewModel extends BaseObservable {
    private Task task;

    public TaskViewModel(Task task) {
        this.task = task;
    }

    @Bindable
    public TaskTitle getTitle() { return task.getTitle(); }
    public void setTitle(TaskTitle title) {
        task.setTitle(title);
        notifyPropertyChanged(BR.task);
    }

    @Bindable
    public Long getDueDate() { return task.getDueDate(); }
    public void setDueDate(Long dueDate) {
        task.setDueDate(dueDate);
        notifyPropertyChanged(BR.dueDate);
    }

    @Bindable
    public String getAssigneeId() { return task.getAssigneeId(); }

    @Bindable
    public String getAssigneeLabel() { return task.getAssigneeLabel(); }
    public void setAssignee(User assignee) {
        task.setAssignee(assignee);
        notifyPropertyChanged(BR.assigneeId);
        notifyPropertyChanged(BR.assigneeLabel);
    }

    @Bindable
    public boolean isClaimed() { return task.isClaimed(); }

    @Bindable
    public boolean isFinished() { return task.isFinished(); }
    public void setFinished(boolean finished) {
        task.setFinished(finished);
        notifyPropertyChanged(BR.finished);
    }

    @Bindable
    public boolean isOverdue() { return task.isOverdue(); }

    @Bindable
    public int getInterval() { return task.getInterval(); }

    public void setInterval(int interval) {
        task.setInterval(interval);
        notifyPropertyChanged(BR.interval);
    }

    /**
     * Saves a task in the database.
     */
    public void save() {
        DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference("tasks");
        if (task.getId() == null) task.setId(firebaseReference.push().getKey());
        firebaseReference.child(task.getId()).setValue(task);
    }

    /**
     * Deletes a task from the database.
     */
    public void delete() {
        if (task.getId()!= null) {
            DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference("tasks");
            firebaseReference.child(task.getId()).removeValue();
        }
    }

    /**
     * Sets the assignee of a task to the current user and saves the change.
     */
    public void claim() {
        // get current user
        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = new User(curUser.getUid(), curUser.getEmail());

        // set task's assignee as current user
        setAssignee(user);

        // mark task as claimed
        task.markAsClaimed();
        notifyPropertyChanged(BR.claimed);

        save();
    }

    /**
     * Sets the 'Finished' attribute of a task to true and saves the change.
     */
    public void markAsDone() {
        setFinished(true);
        save();
    }
}
