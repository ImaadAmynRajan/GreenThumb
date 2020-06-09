package com.example.greenthumb;

import org.junit.Test;
import static org.junit.Assert.*;


//Password should be at least 8 character long -- test1
//Password should have at least one uppercase and one lowercase
//Password should allow only $,@,!,_ as special characters
//Password should contain one digit

public class passwordValidatorTest {
    //This test case check for password length
    @Test
    public void passwordLengthTest() {
        passwordValidator v1 = new passwordValidator("Password");
        passwordValidator v2 = new passwordValidator("Test");
        passwordValidator v3 = new passwordValidator("CSCI3130");

        assertTrue(v1.longEnough());
        assertFalse(v2.longEnough());
        assertTrue(v3.longEnough());
    }

    //This test case check for one uppercase and one lower case
    @Test
    public void caseCheckTest(){
        passwordValidator v1 = new passwordValidator("test");
        passwordValidator v2 = new passwordValidator("Test");
        passwordValidator v3 = new passwordValidator("TEST");
        passwordValidator v4 = new passwordValidator("");

        assertFalse(v1.caseCheck());
        assertTrue(v2.caseCheck());
        assertFalse(v3.caseCheck());
        assertFalse(v4.caseCheck());
    }

    //This test case check for one special character (@,!,$,_)
    @Test
    public void specialCharacterTest(){
        passwordValidator v1 = new passwordValidator("test");
        passwordValidator v2 = new passwordValidator("Test!");
        passwordValidator v3 = new passwordValidator("TEST#");
        passwordValidator v4 = new passwordValidator("@$!_");

        assertTrue(v1.specialCharCheck());
        assertTrue(v2.specialCharCheck());
        assertFalse(v3.specialCharCheck());
        assertTrue(v4.specialCharCheck());
    }

    //This test case check for occurrence of digit
    @Test
    public void passwordDigitTest(){
        passwordValidator v1 = new passwordValidator("9999999999");
        passwordValidator v2 = new passwordValidator("test");
        passwordValidator v3 = new passwordValidator("9testtest");
        passwordValidator v4 = new passwordValidator("");

        assertTrue(v1.containsDigit());
        assertFalse(v2.containsDigit());
        assertTrue(v3.containsDigit());
        assertFalse(v4.containsDigit());
    }

    @Test
    public void validateTest(){
        passwordValidator v1 = new passwordValidator("Nihir@123");
        passwordValidator v2 = new passwordValidator("nihir123");
        passwordValidator v3 = new passwordValidator("$_123Nn@!");
        passwordValidator v4 = new passwordValidator("");

        assertTrue(v1.validate());
        assertFalse(v2.validate());
        assertTrue(v3.validate());
        assertFalse(v4.validate());
    }
}
