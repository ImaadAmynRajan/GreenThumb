package com.example.greenthumb;

import java.util.Date;

public class Task {
    private TaskType type;
    private Date dueDate;
    private User owner;

    public Task(TaskType type, Date dueDate, User owner) {
        this.type = type;
        this.dueDate = dueDate;
        this.owner = owner;
    }
}
