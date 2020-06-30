package com.example.greenthumb;

import android.os.Bundle;

public class HomePage extends NavigationBar {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        super.onCreateNav();
    }

}
