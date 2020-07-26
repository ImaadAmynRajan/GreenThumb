package com.example.greenthumb.trade;

import com.example.greenthumb.User;
import com.example.greenthumb.tasks.Task;


public class TradeRequest {
    private Task tradeTask;
    private User requestedTradeUser;
    private String assigneeId;
    private String assigneeLabel;


    public TradeRequest() {
    }

    public TradeRequest(String assigneeId, User requestedTradeUser, Task tradeTask) {
        this.assigneeId = assigneeId;
        this.requestedTradeUser = requestedTradeUser;
        this.tradeTask = tradeTask;
    }

    public Task getRequestedTask() {
        return this.tradeTask;
    }

    public void setRequestedTask(Task tradeTask) {
        this.tradeTask = tradeTask;
    }

    public User getRequestedTradeUser() {
        return this.requestedTradeUser;
    }

    public void setRequestedTradeUser(User requestedTradeUser) {
        this.requestedTradeUser = requestedTradeUser;
    }


    public String getAssigneeLabel() {
        return this.assigneeLabel;
    }

    public String getAssigneeId() {
        return this.assigneeId;
    }


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
