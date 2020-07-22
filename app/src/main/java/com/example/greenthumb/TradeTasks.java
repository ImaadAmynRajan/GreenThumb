package com.example.greenthumb;

import android.os.Bundle;

public class TradeTasks extends NavigationBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_tasks);
        super.onCreateNav();
    }

}
