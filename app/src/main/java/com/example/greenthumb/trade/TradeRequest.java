package com.example.greenthumb.trade;

import com.example.greenthumb.User;
import com.example.greenthumb.tasks.Task;


public class TradeRequest {
    private static Task tradeTask;
    private static User requestedTradeUser;
    private static String assigneeId;
    private static String assigneeLabel;


    public TradeRequest() {
    }

    public TradeRequest(String assigneeId, User requestedTradeUser, Task tradeTask) {
        TradeRequest.assigneeId = assigneeId;
        TradeRequest.requestedTradeUser = requestedTradeUser;
        TradeRequest.tradeTask = tradeTask;
    }

    public static String getAssigneeLabel() {
        return assigneeLabel;
    }

    public static String getAssigneeId() {
        return assigneeId;
    }

    public static void setAssignee(User user) {
        if (user != null) {
            assigneeLabel = user.getEmail();
            assigneeId = user.getId();
        } else {
            assigneeId = null;
            assigneeLabel = null;
        }
    }

    public Task getRequestedTask() {
        return tradeTask;
    }

    public void setRequestedTask(Task tradeTask) {
        TradeRequest.tradeTask = tradeTask;
    }

    public User getRequestedTradeUser() {
        return requestedTradeUser;
    }

    public void setRequestedTradeUser(User requestedTradeUser) {
        TradeRequest.requestedTradeUser = requestedTradeUser;
    }


}
