package org.example.CapstoneProject.dto;

// -------------------------------------------------------------------------
// Represents user data returned by the REST API.
//
// This DTO separates the API response structure from the internal
// User model while preserving the same JSON fields currently expected
// by the Android client.
// -------------------------------------------------------------------------
@SuppressWarnings("unused")
public class UserResponse {

    // Username returned to the client.
    private String userName;

    // Password returned to the client.
    // This is kept temporarily to preserve Android compatibility.
    private String password;

    // User age returned to the client.
    private int age;

    // Full name returned to the client.
    private String fullName;

    // ---------------------------------------------------------------------
    // Default constructor required for JSON serialization/deserialization.
    // ---------------------------------------------------------------------
    public UserResponse() {
    }

    // ---------------------------------------------------------------------
    // Creates a complete user response.
    // ---------------------------------------------------------------------
    public UserResponse(String userName, String password, int age, String fullName) {
        this.userName = userName;
        this.password = password;
        this.age = age;
        this.fullName = fullName;
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
    // Returns the age.
    // ---------------------------------------------------------------------
    public int getAge() {
        return age;
    }

    // ---------------------------------------------------------------------
    // Sets the age.
    // ---------------------------------------------------------------------
    public void setAge(int age) {
        this.age = age;
    }

    // ---------------------------------------------------------------------
    // Returns the full name.
    // ---------------------------------------------------------------------
    public String getFullName() {
        return fullName;
    }

    // ---------------------------------------------------------------------
    // Sets the full name.
    // ---------------------------------------------------------------------
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}