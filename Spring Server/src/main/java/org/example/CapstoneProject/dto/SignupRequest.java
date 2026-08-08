package org.example.CapstoneProject.dto;

// -------------------------------------------------------------------------
// Represents the data required for a signup request.
//
// This DTO contains the fields accepted from the client during
// user registration and keeps the controller separated from the
// persistence model.
// -------------------------------------------------------------------------
@SuppressWarnings("unused")
public class SignupRequest {

    // Username provided by the client.
    private String userName;

    // Password provided by the client.
    private String password;

    // Full name provided by the client.
    private String fullName;

    // Age provided by the client.
    private int age;

    // ---------------------------------------------------------------------
    // Default constructor required for JSON deserialization.
    // ---------------------------------------------------------------------
    public SignupRequest() {}

    // ---------------------------------------------------------------------
    // Returns the username.
    // ---------------------------------------------------------------------
    public String getUserName() { return userName; }

    // ---------------------------------------------------------------------
    // Sets the username.
    // ---------------------------------------------------------------------
    public void setUserName(String userName) { this.userName = userName; }

    // ---------------------------------------------------------------------
    // Returns the password.
    // ---------------------------------------------------------------------
    public String getPassword() { return password; }

    // ---------------------------------------------------------------------
    // Sets the password.
    // ---------------------------------------------------------------------
    public void setPassword(String password) { this.password = password; }

    // ---------------------------------------------------------------------
    // Returns the full name.
    // ---------------------------------------------------------------------
    public String getFullName() { return fullName; }

    // ---------------------------------------------------------------------
    // Sets the full name.
    // ---------------------------------------------------------------------
    public void setFullName(String fullName) { this.fullName = fullName; }

    // ---------------------------------------------------------------------
    // Returns the age.
    // ---------------------------------------------------------------------
    public int getAge() { return age; }

    // ---------------------------------------------------------------------
    // Sets the age.
    // ---------------------------------------------------------------------
    public void setAge(int age) { this.age = age; }
}