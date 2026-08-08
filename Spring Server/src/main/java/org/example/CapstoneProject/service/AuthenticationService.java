package org.example.CapstoneProject.service;

import org.example.CapstoneProject.model.User;
import org.example.CapstoneProject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

// -------------------------------------------------------------------------
// Contains authentication-related business logic.
//
// This service handles operations such as signup and login.
// It depends on UserRepository and does not know that Firebase is used
// as the database.
// -------------------------------------------------------------------------
@Service
public class AuthenticationService {

    // Repository used to access user data.
    private final UserRepository userRepository;

    // ---------------------------------------------------------------------
    // Builds the service using constructor injection.
    // ---------------------------------------------------------------------
    public AuthenticationService(UserRepository userRepository) {
        // Store the injected repository.
        this.userRepository = userRepository;
    }

    public CompletableFuture<String> signup(User user) {
        // Extract the username safely.
        var username = user != null ? user.getUserName() : null;

        // Reject invalid usernames.
        if (username == null || username.isBlank()) {
            return CompletableFuture.completedFuture("Error: invalid username");
        }

        // Query users by username.
        return userRepository.findByUsername(username)
                .thenCompose(existingUser -> {
                    // Username already exists.
                    if (existingUser != null) {
                        return CompletableFuture.completedFuture(
                                "Username already exists"
                        );
                    }

                    // Insert a new user node.
                    return userRepository.insert(user);
                });
    }

    // ---------------------------------------------------------------------
    // Logs in a user by username and password.
    //
    // Returns the matching user when the credentials are valid.
    // Returns null when no matching user is found.
    // ---------------------------------------------------------------------
    public CompletableFuture<User> login(String username, String password) {
        // Get all users that match the provided username.
        return userRepository.findAllByUsername(username)
                .thenApply(users -> {
                    // Loop over all matching users.
                    for (User existingUser : users) {
                        // Check that the user exists and the password matches.
                        if (existingUser != null
                                && existingUser.getPassword() != null
                                && existingUser.getPassword().equals(password)) {

                            // Return the matching user.
                            return existingUser;
                        }
                    }

                    // Return null when no matching user was found.
                    return null;
                });
    }


}