package org.example.model;

// This class represents a User model
// It matches the structure expected in Spring Boot and Android side
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

    // Constructor with username and password only
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // Getter for userName
    public String getUserName() {
        return userName;
    }

    // Setter for userName
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for age
    public int getAge() {
        return age;
    }

    // Setter for age
    public void setAge(int age) {
        this.age = age;
    }

    // Getter for fullName
    public String getFullName() {
        return fullName;
    }

    // Setter for fullName
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // toString for debugging
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
