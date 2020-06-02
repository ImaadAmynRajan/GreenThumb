package com.google.firebase.quickstart.database.java.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Task {
    public int taskId;
    public String taskType;
    public String dueDate;
    public User userId;
    public Task(){}

    public Task(int taskId, String taskType, String dueDate, User userId){
        this.taskId = taskId;
        this.taskType = taskType;
        this.dueDate = dueDate;
        this.userId = userId;
    }
}