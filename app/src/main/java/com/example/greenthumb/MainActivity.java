package com.example.greenthumb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        final String TAG = "MainActivity";
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        //how to call database
//        /* FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message"); */
//
//
//    }

    EditText email, password;
    Button login_button;
    TextView signUp_textView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fireBase_Listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String TAG = "MainActivity";
        //how to call database
       /* FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference myRef = database.getReference("message"); */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        login_button = findViewById(R.id.login_button);
        signUp_textView = findViewById(R.id.signUp_textView);

        fireBase_Listener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser list_of_User = mAuth.getCurrentUser();
                if (list_of_User != null) {
                    Toast.makeText(MainActivity.this, "your logged in", Toast.LENGTH_SHORT).show();
                    Intent homepage = new Intent(MainActivity.this, HomePage.class);
                    startActivity(homepage);
                } else {
                    Toast.makeText(MainActivity.this, "Please login", Toast.LENGTH_SHORT).show();

                }
            }
        };


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_Email = email.getText().toString();
                String user_Password = password.getText().toString();
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter both user email and password ", Toast.LENGTH_SHORT).show();
                    email.requestFocusFromTouch();
                    password.requestFocusFromTouch();
                } else if ((!email.getText().toString().isEmpty()) && (!password.getText().toString().isEmpty())) {
                    mAuth.signInWithEmailAndPassword(user_Email, user_Password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(null, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(MainActivity.this, Objects.requireNonNull(task.getException()).getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        Intent homepage = new Intent(MainActivity.this, HomePage.class);
                                        startActivity(homepage);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(MainActivity.this, Objects.requireNonNull(task.getException()).getMessage(),
                                                Toast.LENGTH_SHORT).show();

                                    }

                                    // ...


                                }
                            });


                }


            }
        });

        signUp_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(MainActivity.this, SignUp.class);
                startActivity(signUp);

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        mAuth.addAuthStateListener(fireBase_Listener);
    }


}
