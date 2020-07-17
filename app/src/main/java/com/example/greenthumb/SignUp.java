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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUp extends AppCompatActivity {
    EditText signUpEmail, signUpPassword;
    Button signUp_button;
    TextView login_TextView; // takes to login page when clicked
    private FirebaseAuth mAuth;

    /***
     * This method displays the content of the screen when the activity is started.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        signUpEmail = findViewById(R.id.editTextTextEmailAddress);
        signUpPassword = findViewById(R.id.editTextTextPassword);
        login_TextView = findViewById(R.id.login_TextView);
        signUp_button = findViewById(R.id.signUp_button);
    }

    // this is onclick listener with method to create user in firebase
    public void signUpListener(View v) {
        final String email = signUpEmail.getText().toString();
        String password = signUpPassword.getText().toString();
        passwordValidator pv = new passwordValidator(password);
        if (email.isEmpty() || password.isEmpty()) {
            Log.d(null, "signUpWithEmail:please enter both email and password");

        } else if (!(signUpEmail.getText().toString().isEmpty()) && !(signUpPassword.getText().toString().isEmpty())) {
            if (pv.validate()) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            /***
                             * This method displays result of the firebase communication to the user. when user is successfully signed up they are displayed home screen,
                             * otherwise they are displayed what is the error like email already existing.
                             * @param task: contains result of authentication from firebase.
                             */
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    final FirebaseUser user = mAuth.getCurrentUser();

                                    // send email verification
                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // check that the email was sent successfully
                                            if (task.isSuccessful()) {
                                                // add new user to our database
                                                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                                db.child("users/" + user.getUid()).setValue(new User(user.getUid(), email));

                                                // create a toast that notifies the user of successful signup
                                                Toast.makeText(SignUp.this, R.string.verification_sent,
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    // launch the login page after signing up successfully
                                    Intent login = new Intent(SignUp.this, MainActivity.class);
                                    startActivity(login);
                                } else {
                                    Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }

                            }

                        });
            } else {
                Toast.makeText(SignUp.this, "Invalid Password, doesn't match the requirements.", Toast.LENGTH_LONG).show();
            }


        }


    }
    public void loginPageListener(View v) {
        Intent login_Page = new Intent(SignUp.this, MainActivity.class);
        startActivity(login_Page);
    }


}
