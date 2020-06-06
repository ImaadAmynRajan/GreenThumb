package com.google.firebase.quickstart.database.java.models;
import com.google.firebase.database.IgnoreExtraProperties;

//Note: should this password variable be declared differently? Or is it fine to have no security functionality
@IgnoreExtraProperties
public class User {
    public String username;
    public String email;
    public String password;
    public User() {}
// Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}