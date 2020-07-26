package com.example.greenthumb.trade;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.greenthumb.BR;
import com.example.greenthumb.User;

public class TradeRequestViewModel extends BaseObservable {
    private TradeRequest tradeRequest;

    public TradeRequestViewModel(TradeRequest tradeRequest) {
        this.tradeRequest = tradeRequest;
    }

    @Bindable

    public String getAssigneeId() {
        return TradeRequest.getAssigneeId();
    }

    @Bindable
    public String getAssigneeLabel() {
        return TradeRequest.getAssigneeLabel();
    }

    public void setAssignee(User assignee) {
        TradeRequest.setAssignee(assignee);
        notifyPropertyChanged(BR.assigneeId);
        notifyPropertyChanged(BR.assigneeLabel);
    }
}
