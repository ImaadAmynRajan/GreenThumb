package com.example.greenthumb;
import java.text.DateFormat;

import java.util.Date;

/**
 * Represents a task created by a user.
 */
public class Task {
    private String title;
    private String id;
    private Date dueDate;
    private String assigneeId;
    private String assigneeLabel;

    public Task() { }
    /**
     * Creates a new Task
     * @param id the ID associated with the task
     * @param title description of the task
     * @param dueDate the date by which the task must be completed
     * @param user the user to which the task is assigned
     */
    public Task(String id, String title, Date dueDate, User user) {
        this.title = title;
        this.dueDate = dueDate;
        setAssignee(user);
    }

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
     * @return the due date of a task
     */
    public Date getDueDate() {
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
}
