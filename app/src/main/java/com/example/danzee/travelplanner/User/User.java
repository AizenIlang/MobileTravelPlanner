package com.example.danzee.travelplanner.User;

import com.google.firebase.database.Exclude;

/**
 * Created by DanZee on 03/08/2017.
 */

public class User {
    @Exclude
    public static final String TYPE_ADMIN = "ADMIN";
    @Exclude
    public static final String TYPE_USER = "USER";

    private String UserID;
    private String UserName;
    private String FirstName;
    private String LastName;
    private String Email;
    private String Password;
    private String Type;
    private double Budget;


    public User() {
    }

    public User(String userID, String userName, String firstName, String lastName, String email, String password, String type) {
        UserID = userID;
        UserName = userName;
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        Password = password;
        Type = type;
    }

    public double getBudget() {
        return Budget;
    }

    public void setBudget(double budget) {
        Budget = budget;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
