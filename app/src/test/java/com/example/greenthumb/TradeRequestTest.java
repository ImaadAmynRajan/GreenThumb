package com.example.greenthumb;

import com.example.greenthumb.tasks.Task;
import com.example.greenthumb.trade.TradeRequest;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class TradeRequestTest {
    private User user;

    @Test
    public void getTradeRequest() {
        TradeRequest tradeRequest = new TradeRequest("userid", new User("userid", "email@email.com"), new Task());
        // assert the tradeRequest doesn't have an assignee yet
        assertTrue(TradeRequest.getAssigneeId(), true);
//        assertTrue(tradeRequest.getRequestedTask(),true);
        // assign a user
        tradeRequest.setRequestedTradeUser(new User("user-2-id", "email@email.com"));
        // check that the values are updated
        assertEquals("email@email.com", tradeRequest.getRequestedTradeUser().getEmail());
        assertEquals("userid", TradeRequest.getAssigneeId());
    }
}
