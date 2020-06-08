package com.example.greenthumb;
import java.text.DateFormat;
import com.google.firebase.quickstart.database.java.models.User;
import java.util.Date;

public class Task {
    private String title;
    private Date dueDate;
    private Object assignee;

    public Task(String title, Date dueDate, Object assignee) {
        this.title = title;
        this.dueDate = dueDate;
        this.assignee = assignee;
    }

    public String getTitle() {
        return this.title;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public String getDateString()  {
        if (this.dueDate == null) {
            return null;
        }

        // format date to Month Day, Year
        DateFormat dateFormat = DateFormat.getDateInstance();
        return dateFormat.format(this.dueDate);
    }

    public Object getAssignee() {
        return this.assignee;
    }

    public void writeToDatabase() {

    }
}
