package com.example.greenthumb;

import com.example.greenthumb.tasks.Task;
import com.example.greenthumb.trade.TradeRequest;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class TradeRequestTest {

    @Test
    public void getTradeRequest() {
        String requestId = "ABC";
        String userId = "123";
        String userEmail = "test@email.com";
        User user = new User(userId, userEmail);
        Task testTask = new Task();

        // create new trade request
        TradeRequest tradeRequest = new TradeRequest(requestId, user, testTask);

        // test getters
        assertEquals(tradeRequest.getId(), requestId);
        assertEquals(tradeRequest.getRequester(), user);
        assertEquals(tradeRequest.getRequestedTask(), testTask);
    }
}
