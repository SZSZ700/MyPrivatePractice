package com.example.myfinaltopapplication;

// This is the client-side User model
// It is used ONLY on Android side to send/receive data via REST
public class User {

    // Username field
    private String userName;
    // Password field
    private String password;
    // Age field
    private int age;
    // Full name field
    private String fullName;

    // Constructor with all fields
    public User(String userName, String password, int age, String fullName) {
        this.userName = userName;
        this.password = password;
        this.age = age;
        this.fullName = fullName;
    }

    // Constructor with username + password only
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // Getters and setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    // For debugging / logging
    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
