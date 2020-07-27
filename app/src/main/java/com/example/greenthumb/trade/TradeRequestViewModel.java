package com.example.greenthumb.trade;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.greenthumb.BR;
import com.example.greenthumb.User;
import com.example.greenthumb.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Represents a ViewModel for TradeRequest objects.
 * Based on ContactViewModel.java from Assignment 4.
 */
public class TradeRequestViewModel extends BaseObservable {
    private TradeRequest tradeRequest;

    public TradeRequestViewModel(TradeRequest tradeRequest) {
        this.tradeRequest = tradeRequest;
    }

    @Bindable
    public User getRequester() {
        return tradeRequest.getRequester();
    }

    public void setRequester(User assignee) {
        tradeRequest.setRequester(assignee);
        notifyPropertyChanged(BR.requester);
    }

    @Bindable
    public Task getRequestedTask() {
        return tradeRequest.getRequestedTask();
    }

    @Bindable
    public void setRequestedTask(Task tradeTask) {
        tradeRequest.setRequestedTask(tradeTask);
        notifyPropertyChanged(BR.requestedTask);
    }

    @Bindable
    public TradeRequest getTradeRequest() {
        return tradeRequest;
    }

    /**
     * Saves a trade request in the database.
     */
    public void save() {
        DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference("trades");
        if (tradeRequest.getId() == null) tradeRequest.setId(firebaseReference.push().getKey());
        firebaseReference.child(tradeRequest.getId()).setValue(tradeRequest);
    }

    /**
     * Deletes a trade request from the database.
     */
    public void delete() {
        if (tradeRequest.getId()!= null) {
            DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference("trades");
            firebaseReference.child(tradeRequest.getId()).removeValue();
        }
    }
}
