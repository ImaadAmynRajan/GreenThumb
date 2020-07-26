package com.example.greenthumb.trade;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.greenthumb.BR;
import com.example.greenthumb.User;
import com.example.greenthumb.tasks.Task;

public class TradeRequestViewModel extends BaseObservable {
    private TradeRequest tradeRequest;
    private Task tradeTask;

    public TradeRequestViewModel(TradeRequest tradeRequest) {
        this.tradeRequest = tradeRequest;
    }

    @Bindable
    public String getRequesterId() {
        return TradeRequest.getRequesterId();
    }

    @Bindable
    public String getRequestedLabel() {
        return TradeRequest.getRequestedLabel();
    }

    public void setRequester(User assignee) {
        TradeRequest.setRequester(assignee);
        notifyPropertyChanged(BR.requesterId);
        notifyPropertyChanged(BR.requestedLabel);
    }

    @Bindable
    public Task getRequestedTask() {
        return tradeTask;
    }

    @Bindable
    public void setRequestedTask(Task tradeTask) {
        this.tradeTask = tradeTask;
    }

    @Bindable
    public TradeRequest getTradeRequest() {
        return tradeRequest;
    }

    @Bindable
    public void setTradeRequest(TradeRequest tradeRequest) {
        this.tradeRequest = tradeRequest;
    }
}
