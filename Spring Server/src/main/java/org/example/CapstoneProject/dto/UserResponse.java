package org.example.CapstoneProject.dto;

// -------------------------------------------------------------------------
// Represents user data returned by the REST API.
//
// This DTO preserves the fields required by the Android client while
// separating the REST response structure from the internal User model.
// -------------------------------------------------------------------------
@SuppressWarnings("unused")
public class UserResponse {

    // Username of the user.
    private String userName;

    // Password is currently kept for Android compatibility.
    // It will be removed later during the security refactoring.
    private String password;

    // User's age.
    private int age;

    // User's full name.
    private String fullName;

    // User's BMI value.
    private double bmi;

    // ---------------------------------------------------------------------
    // Default constructor required for JSON serialization/deserialization.
    // ---------------------------------------------------------------------
    public UserResponse() {
    }

    // ---------------------------------------------------------------------
    // Creates a complete user response.
    // ---------------------------------------------------------------------
    public UserResponse(
            String userName,
            String password,
            int age,
            String fullName,
            double bmi) {

        this.userName = userName;
        this.password = password;
        this.age = age;
        this.fullName = fullName;
        this.bmi = bmi;
    }

    // ---------------------------------------------------------------------
    // Returns the username.
    // ---------------------------------------------------------------------
    public String getUserName() {
        return userName;
    }

    // ---------------------------------------------------------------------
    // Sets the username.
    // ---------------------------------------------------------------------
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // ---------------------------------------------------------------------
    // Returns the password.
    // ---------------------------------------------------------------------
    public String getPassword() {
        return password;
    }

    // ---------------------------------------------------------------------
    // Sets the password.
    // ---------------------------------------------------------------------
    public void setPassword(String password) {
        this.password = password;
    }

    // ---------------------------------------------------------------------
    // Returns the user's age.
    // ---------------------------------------------------------------------
    public int getAge() {
        return age;
    }

    // ---------------------------------------------------------------------
    // Sets the user's age.
    // ---------------------------------------------------------------------
    public void setAge(int age) {
        this.age = age;
    }

    // ---------------------------------------------------------------------
    // Returns the user's full name.
    // ---------------------------------------------------------------------
    public String getFullName() {
        return fullName;
    }

    // ---------------------------------------------------------------------
    // Sets the user's full name.
    // ---------------------------------------------------------------------
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // ---------------------------------------------------------------------
    // Returns the user's BMI value.
    // ---------------------------------------------------------------------
    public double getBmi() {
        return bmi;
    }

    // ---------------------------------------------------------------------
    // Sets the user's BMI value.
    // ---------------------------------------------------------------------
    public void setBmi(double bmi) {
        this.bmi = bmi;
    }
}