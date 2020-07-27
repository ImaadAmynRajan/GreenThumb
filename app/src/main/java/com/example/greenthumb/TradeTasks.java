package com.example.greenthumb;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenthumb.trade.TradeRequest;
import com.example.greenthumb.trade.TradeRequestAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class TradeTasks extends NavigationBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_tasks);
        super.onCreateNav();

        initializeTradeRequestList();
    }

    private void initializeTradeRequestList() {
        // get current user's email
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // create query for our trade requests, getting only those associated with tasks
        // assigned to the current user
        Query query = FirebaseDatabase.getInstance().getReference().child("trades")
                .orderByChild("requestedTask/assigneeLabel").equalTo(currentUser);

        // create options for our RecyclerView
        FirebaseRecyclerOptions<TradeRequest> options = new FirebaseRecyclerOptions.Builder<TradeRequest>()
                .setQuery(query, TradeRequest.class).setLifecycleOwner(this).build();

        // initialize RecyclerView
        final RecyclerView tradeRequestList = findViewById(R.id.recyclerViewTradeRequests);
        tradeRequestList.setLayoutManager(new LinearLayoutManager(this));

        // create RecyclerView adapter
        TradeRequestAdapter adapter = new TradeRequestAdapter(options);

        adapter.notifyDataSetChanged();
        tradeRequestList.setAdapter(adapter);
    }
}
