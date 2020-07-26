package com.example.greenthumb.trade;

import com.example.greenthumb.User;
import com.example.greenthumb.tasks.Task;

/**
 * Represents a request to trade a task.
 */
public class TradeRequest {
    private String id;
    private Task tradeTask;
    private User requestedTradeUser;


    // default constructor for databinding
    public TradeRequest() {
    }

    /**
     * Creates a new trade request
     * @param id the ID associated with the trade request
     * @param requestedTradeUser the user who requested the trade
     * @param tradeTask the task to be traded
     */
    public TradeRequest(String id, User requestedTradeUser, Task tradeTask) {
        this.id = id;
        this.requestedTradeUser = requestedTradeUser;
        this.tradeTask = tradeTask;
    }

    /**
     * Gets the ID associated with the trade request
     * @return the ID associated with the trade request
     */
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the user who requested the trade
     * @return the user who requested the trade
     */
    public User getRequester() {
        return this.requestedTradeUser;
    }

    public void setRequester(User user) {
        this.requestedTradeUser = user;
    }

    /**
     * Gets the task to be traded
     * @return the task to be traded
     */
    public Task getRequestedTask() {
        return tradeTask;
    }

    public void setRequestedTask(Task tradeTask) {
        this.tradeTask = tradeTask;
    }

    public User getRequestedTradeUser() {
        return requestedTradeUser;
    }

    public void setRequestedTradeUser(User requestedTradeUser) {
        this.requestedTradeUser = requestedTradeUser;
    }
}
