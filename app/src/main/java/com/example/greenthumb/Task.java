package com.example.greenthumb;
import java.text.DateFormat;
import com.google.firebase.quickstart.database.java.models.User;
import java.util.Date;

/**
 * Represents a task created by a user.
 */
public class Task {
    private String title;
    private Date dueDate;
    private Object assignee;

    /**
     * Creates a new Task
     * @param title description of the task
     * @param dueDate the date by which the task must be completed
     * @param assignee the user to which the task is assigned
     */
    public Task(String title, Date dueDate, Object assignee) {
        this.title = title;
        this.dueDate = dueDate;
        this.assignee = assignee;
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
     * @return the user assigned to a task
     */
    public Object getAssignee() {
        return this.assignee;
    }

    /**
     * Stores a task in the database
     */
    public void writeToDatabase() {

    }
}
