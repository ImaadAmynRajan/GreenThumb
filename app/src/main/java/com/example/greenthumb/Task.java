package com.example.greenthumb;
import java.text.DateFormat;

import java.util.Date;

public class Task {
    private String title;
    private String id;
    private Date dueDate;
    private String assigneeId;
    private String assigneeLabel;

    public Task() { }
    public Task(String id, String title, Date dueDate, User user) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        setAssignee(user);
    }

    public String getId() {
        return id;
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

    public String getAssigneeLabel() {
        return this.assigneeLabel;
    }
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
