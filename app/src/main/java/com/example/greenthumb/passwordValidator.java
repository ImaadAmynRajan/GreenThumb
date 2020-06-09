package com.example.greenthumb;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class passwordValidator {
    private String password ;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public passwordValidator(String password) {
        this.password =  password;
    }



    public boolean longEnough() {
        if(password.length() >= 8){
            return true;
        }else{
            return false;
        }
    }

    public boolean caseCheck() {
        boolean capitalFlag = false;
        boolean  lowerCaseFlag = false;
        char ch;
        for(int i=0;i < password.length();i++) {
            ch = password.charAt(i);
            if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
        }
        if(capitalFlag && lowerCaseFlag) {
            return true;
        }
        return false;
    }

    public boolean specialCharCheck() {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9_@$!]*");
        Matcher matcher = pattern.matcher(password);

        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }

    }

    public boolean containsDigit() {
        boolean digitFlag = false;
        char ch;
        for(int i=0;i < password.length();i++) {
            ch = password.charAt(i);
            if (Character.isDigit(ch)) {
                digitFlag = true;
            }
        }
        if(digitFlag) {
            return true;
        }
        return false;
    }

    public boolean validate() {
        boolean test1 = longEnough();
        boolean test2 = caseCheck();
        boolean test3 = specialCharCheck();;
        if(test1 && test2 && test3) {
            return true;
        }
        return false;
    }
}
