package org.example.CapstoneProject.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

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
    @NotBlank(message = "Username is required")
    private String userName;

    // Password provided by the client.
    @NotBlank(message = "Password is required")
    private String password;

    // Full name provided by the client.
    @NotBlank(message = "Full name is required")
    private String fullName;

    // Age provided by the client.
    @Min(value = 1, message = "Age must be greater than 0")
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