package com.example.greenthumb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {
    Button logout_button;

    /***
     * This method displays the homepage contents
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        logout_button = findViewById(R.id.logout_button);

        //this is onclick method for button to log out of the app
        logout_button.setOnClickListener(new View.OnClickListener() {
            /***
             * This method logs out user when the logout button is pressed.
             * @param v
             */
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent loginPage = new Intent(HomePage.this, MainActivity.class);
                startActivity(loginPage);

            }
        });
    }
}