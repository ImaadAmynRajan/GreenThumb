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

public class SignUp extends AppCompatActivity {
    EditText signUpEmail, signUpPassword;
    Button signUp_button;
    TextView login_TextView; // takes to login page when clicked
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        signUpEmail = findViewById(R.id.editTextTextEmailAddress);
        signUpPassword = findViewById(R.id.editTextTextPassword);
        login_TextView = findViewById(R.id.login_TextView);
        signUp_button = findViewById(R.id.signUp_button);

        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signUpEmail.getText().toString();
                String password = signUpPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Log.d(null, "signUpWithEmail:please enter both email and password");

                } else if (!(signUpEmail.getText().toString().isEmpty()) && !(signUpPassword.getText().toString().isEmpty())) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(null, "signUpWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        Intent homepage = new Intent(SignUp.this, HomePage.class);
                                        startActivity(homepage);

                                    } else {
                                        Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }

                            });


                }


            }
        });

        login_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_Page = new Intent(SignUp.this, MainActivity.class);
                startActivity(login_Page);
            }
        });


    }

}