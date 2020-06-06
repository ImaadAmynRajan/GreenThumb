package com.google.firebase.quickstart.database.java.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Task {
    public int taskId;
    public String taskType;
    public Date dueDate;
    public User user;
    public Task(){}

    public Task(int taskId, String taskType, Date dueDate, User user){
        this.taskId = taskId;
        this.taskType = taskType;
        this.dueDate = dueDate;
        this.user = user;
    }
}