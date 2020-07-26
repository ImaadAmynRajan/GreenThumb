package com.example.greenthumb.trade;

import com.example.greenthumb.User;
import com.example.greenthumb.tasks.Task;


public class TradeRequest {
    private static Task tradeTask;
    private static User requestedTradeUser;
    private static String requesterId;
    private static String requestedLabel;


    public TradeRequest() {
    }

    public TradeRequest(String requesterId, User requestedTradeUser, Task tradeTask) {
        TradeRequest.requesterId = requesterId;
        TradeRequest.requestedTradeUser = requestedTradeUser;
        TradeRequest.tradeTask = tradeTask;
    }

    public static String getRequestedLabel() {
        return requestedLabel;
    }

    public static String getRequesterId() {
        return requesterId;
    }

    public static void setRequester(User user) {
        if (user != null) {
            requestedLabel = user.getEmail();
            requesterId = user.getId();
        } else {
            requesterId = null;
            requestedLabel = null;
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
