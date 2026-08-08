package org.example.CapstoneProject.service;

import org.example.CapstoneProject.model.User;
import org.example.CapstoneProject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

// -------------------------------------------------------------------------
// Contains business logic related to users.
//
// This service depends on the UserRepository interface and does not know
// that Firebase is used as the database.
// -------------------------------------------------------------------------
@SuppressWarnings("unused")
@Service
public class UserService {

    // Repository used to access user data.
    private final UserRepository userRepository;

    // ---------------------------------------------------------------------
    // Builds the service using constructor injection.
    // ---------------------------------------------------------------------
    public UserService(UserRepository userRepository) {
        // Store the injected repository.
        this.userRepository = userRepository;
    }

    // ---------------------------------------------------------------------
    // Finds a user by username.
    //
    // Returns the user when found.
    // Returns null when no matching user exists.
    // ---------------------------------------------------------------------
    public CompletableFuture<User> getUser(String username) {
        // Delegate the database operation to the repository.
        return userRepository.findByUsername(username);
    }

    // ---------------------------------------------------------------------
    // Returns all users stored in the database.
    // ---------------------------------------------------------------------
    public CompletableFuture<List<User>> getAllUsers() {
        // Delegate the database operation to the repository.
        return userRepository.findAll();
    }

    // ---------------------------------------------------------------------
    // Checks whether a user exists by username.
    // ---------------------------------------------------------------------
    public CompletableFuture<Boolean> exists(String username) {
        // Delegate the database operation to the repository.
        return userRepository.existsByUsername(username);
    }

    // ---------------------------------------------------------------------
    // Deletes a user by username.
    //
    // Returns true when the user was found and deleted.
    // Returns false when no matching user exists.
    // ---------------------------------------------------------------------
    public CompletableFuture<Boolean> deleteUser(String username) {
        // Delegate the database operation to the repository.
        return userRepository.deleteByUsername(username);
    }

    // ---------------------------------------------------------------------
    // Replaces an existing user by username.
    //
    // Returns true when the user was found and updated.
    // Returns false when no matching user exists.
    // ---------------------------------------------------------------------
    public CompletableFuture<Boolean> updateUser(String username, User updatedUser) {
        // Delegate the database operation to the repository.
        return userRepository.updateByUsername(username, updatedUser);
    }

    // ---------------------------------------------------------------------
    // Partially updates an existing user by username.
    //
    // Returns the updated user when the user was found.
    // Returns null when no matching user exists.
    // ---------------------------------------------------------------------
    public CompletableFuture<User> patchUser(String username, Map<String, Object> updates) {
        // Delegate the database operation to the repository.
        return userRepository.patchByUsername(username, updates);
    }

    // ---------------------------------------------------------------------
    // Creates a new user.
    //
    // Returns true when the user was created successfully.
    // Returns false when the username is invalid or already exists.
    // ---------------------------------------------------------------------
    public CompletableFuture<Boolean> createUser(User user) {
        // Delegate the database operation to the repository.
        return userRepository.create(user);
    }
}