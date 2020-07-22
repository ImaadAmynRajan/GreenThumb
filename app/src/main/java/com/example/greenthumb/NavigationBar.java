package com.example.greenthumb;

import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Represents the main navigation bar that appears on all activities
 * based on the top answer of https://stackoverflow.com/questions/19451715/same-navigation-drawer-in-different-activities
 * which explains how to put bar on multiple page without having to re-make them into fragments
 */
public class NavigationBar extends AppCompatActivity {
    BottomNavigationView navigation;

    protected void onCreateNav() {
        navigation = findViewById(R.id.mainNav);

        //allow navigation items to launch an activity when selected.
        //setOnNavigationItemSelectedListener from https://stackoverflow.com/questions/44611224/how-to-setonnavigationitemlistener-on-bottomnavigationview-in-android-using-kotl
        navigation.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getItemId() == R.id.toTaskPage) {
                            Intent taskPage = new Intent(NavigationBar.this, ViewTasks.class);
                            startActivity(taskPage);
                        } else if (item.getItemId() == R.id.toHomePage) {
                            Intent homePage = new Intent(NavigationBar.this, HomePage.class);
                            startActivity(homePage);
                        } else if (item.getItemId() == R.id.toTradePage) {
                            Intent tradesPage = new Intent(NavigationBar.this, TradeTasks.class);
                            startActivity(tradesPage);
                        }
                        return false;
                    }
                });

    }
}
