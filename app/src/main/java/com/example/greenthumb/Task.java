package com.example.greenthumb;
import java.text.DateFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * Represents a task created by a user.
 */
public class Task {
    private String title;
    private String id;
    private Long dueDate;
    private String assigneeId;
    private String assigneeLabel;

    private boolean isFinished = false;

    public Task() { }
    /**
     * Creates a new Task
     * @param id the ID associated with the task
     * @param title description of the task
     * @param dueDate the date by which the task must be completed
     * @param user the user to which the task is assigned
     */
    public Task(String id, String title, Date dueDate, User user) {
        this.id = id;
        this.title = title;
        // storing it as a long allows for easier database reference
        this.dueDate = dueDate != null ? dueDate.getTime() : null;
        setAssignee(user);
    }

    /**
     * Creates a new Task
     * @param id the ID associated with the task
     * @param title description of the task
     * @param dueDate the date by which the task must be completed
     * @param user the user to which the task is assigned
     * @param isFinished whether the task has been marked as finished
     */
    public Task(String id, String title, Date dueDate, User user, boolean isFinished) {
        this(id, title, dueDate, user);
        this.isFinished = isFinished;
    }

    /**
     * The id corresponding to the database entry
     * @return the id of the task
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the title of a task
     * @return the title of a task
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets the due date of a task
     * @return the due date of a task in milliseconds
     */
    public Long getDueDate() {
        return this.dueDate;
    }

    /**
     * Gets a string representation of a task's due date
     * @return a string representation of a task's due date or null if due date is null
     */
    public String getDateString()  {
        if (this.dueDate == null) {
            return null;
        }

        // format date to Month Day, Year
        DateFormat dateFormat = DateFormat.getDateInstance();
        return dateFormat.format(this.dueDate);
    }

    /**
     * Gets the user assigned to a task
     * @return the user's label assigned to a task
     */
    public String getAssigneeLabel() {
        return this.assigneeLabel;
    }

    /**
     * Gets the user assigned to a task
     * @return the user's id assigned to a task
     */
    public String getAssigneeId() { return this.assigneeId; }

    public void setAssignee(User user) {
        if (user != null) {
            this.assigneeLabel = user.getEmail();
            this.assigneeId = user.getId();
        } else {
            this.assigneeId = null;
            this.assigneeLabel = null;
        }
    }

    /**
     * Gets a boolean value representing whether the task has been marked as finished
     * @return boolean value representing whether the task has been marked as finished
     */
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * Marks a task as finished
     */
    public void markAsFinished() {
        this.isFinished = true;
    }

    /**
     * Returns whether the task is overdue
     * @return boolean representing whether the task is overdue or not
     */
    public boolean isOverdue() {
        if (getDueDate() != null) {
            return !isFinished && Calendar.getInstance().getTimeInMillis() > getDueDate();
        }
        return false;
    }
}
