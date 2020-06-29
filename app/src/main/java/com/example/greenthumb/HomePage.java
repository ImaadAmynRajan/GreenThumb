package com.example.greenthumb;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        navigation = findViewById(R.id.mainNav);

        //allow navigation items to launch an activity when selected.
        //setOnNavigationItemSelectedListener from https://stackoverflow.com/questions/44611224/how-to-setonnavigationitemlistener-on-bottomnavigationview-in-android-using-kotl
        navigation.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.toTaskPage) {
                    Intent taskPage = new Intent(HomePage.this, ViewTasks.class);
                    startActivity(taskPage);
                }
                return false;
            }
        });

    }

}
