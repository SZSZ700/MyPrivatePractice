// Define the package for this controller
package org.example;

// Import the User model
import org.example.model.User;

// Import our Firebase service
import org.example.service.FirebaseService;

// Import Spring classes for HTTP responses and annotations
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Import Java utility classes
import java.util.*;
import java.util.concurrent.CompletableFuture;   // To handle async results from Firebase

// -------------------------------------------------------------------------
// Marks this class as a REST controller (all methods return JSON responses)
// -------------------------------------------------------------------------
@RestController
// Base URL for all endpoints in this controller
@RequestMapping("/api/users")
public class UsersController {

    // -------------------------------------------------------------------------
    // Reference to FirebaseService (business logic)
    // -------------------------------------------------------------------------
    private final FirebaseService firebaseService;

    // -------------------------------------------------------------------------
    // Constructor for dependency injection of FirebaseService
    // -------------------------------------------------------------------------
    public UsersController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    // -------------------------------------------------------------------------
    // CREATE (POST)
    // Endpoint: POST /api/users
    // Creates a new user if the username does not already exist
    // -------------------------------------------------------------------------
    @PostMapping
    public CompletableFuture<ResponseEntity<?>> createUser(@RequestBody User user) {
        // Call the service to create user
        return firebaseService.createUser(user).thenApply(success -> {
            if (!success) {
                // Return HTTP 409 Conflict if user already exists
                return ResponseEntity
                        .status(HttpStatus.CONFLICT).body("User already exists");
            }

            // Otherwise, return HTTP 201 Created with user object
            return ResponseEntity
                    .status(HttpStatus.CREATED).body(user);
        });
    }

    // -------------------------------------------------------------------------
    // LOGIN (POST)
    // Endpoint: POST /api/users/login
    // Accepts username + password and returns user if valid
    // -------------------------------------------------------------------------
    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<?>> loginUser(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("userName");
        String password = credentials.get("password");

        // Fetch user by username
        return firebaseService.getUser(username).thenApply(user -> {
            if (user == null || !user.getPassword().equals(password)) {
                // Wrong username or password
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid username or password");
            }

            // ✅ Return 200 OK with user object
            return ResponseEntity.ok(user);
        });
    }

    // -------------------------------------------------------------------------
    // READ (GET ALL)
    // Endpoint: GET /api/users
    // Returns a list of all users from Firebase
    // -------------------------------------------------------------------------
    @GetMapping
    public CompletableFuture<ResponseEntity<List<User>>> getAllUsers() {
        // Fetch all users and wrap in ResponseEntity
        return firebaseService.getAllUsers().thenApply(ResponseEntity::ok);
    }

    // -------------------------------------------------------------------------
    // READ (GET ONE)
    // Endpoint: GET /api/users/{username}
    // Returns a single user by username
    // -------------------------------------------------------------------------
    @GetMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> getUser(@PathVariable String username) {
        // Call service to fetch user
        return firebaseService.getUser(username).thenApply(user -> {
            if (user == null) {
                // Return 404 Not Found if no such user
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }

            // Otherwise return 200 OK with user object
            return ResponseEntity
                    .ok(user);
        });
    }

    // -------------------------------------------------------------------------
    // UPDATE (PUT)
    // Endpoint: PUT /api/users/{username}
    // Replaces the entire user object
    // -------------------------------------------------------------------------
    @PutMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> updateUser(@PathVariable String username,
                                                           @RequestBody User updatedUser) {
        // Call service to update user
        return firebaseService.updateUser(username, updatedUser).thenApply(success -> {
            if (!success) {
                // If user not found -> return 404
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }

            // Otherwise return updated user
            return ResponseEntity
                    .ok(updatedUser);
        });
    }

    // -------------------------------------------------------------------------
    // PARTIAL UPDATE (PATCH)
    // Endpoint: PATCH /api/users/{username}
    // Updates only provided fields
    // -------------------------------------------------------------------------
    @PatchMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> patchUser(@PathVariable String username,
                                                          @RequestBody Map<String, Object> updates) {
        // Call service to apply partial update
        return firebaseService.patchUser(username, updates).thenApply(updatedUser -> {
            if (updatedUser == null) {
                // If user not found -> return 404
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }

            // Return the partially updated user
            return ResponseEntity
                    .ok(updatedUser);
        });
    }

    // -------------------------------------------------------------------------
    // DELETE
    // Endpoint: DELETE /api/users/{username}
    // Deletes a user by username
    // -------------------------------------------------------------------------
    @DeleteMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> deleteUser(@PathVariable String username) {
        // Call service to delete user
        return firebaseService.deleteUser(username).thenApply(success -> {
            if (!success) {
                // Return 404 if user does not exist
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }

            // Otherwise return confirmation message
            return ResponseEntity
                    .ok("User deleted");
        });
    }

    // -------------------------------------------------------------------------
    // HEAD
    // Endpoint: HEAD /api/users/{username}
    // Checks if a user exists (returns only status, no body)
    // -------------------------------------------------------------------------
    @RequestMapping(value = "/{username}", method = RequestMethod.HEAD)
    public CompletableFuture<ResponseEntity<Void>> headUser(@PathVariable String username) {
        // Call service to check existence
        return firebaseService.exists(username).thenApply(exists -> {
            if (exists) {
                // Return 200 OK if user exists
                return ResponseEntity
                        .ok()
                        .build();
            }

            // Return 404 if not found
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        });
    }

    // -------------------------------------------------------------------------
    // UPDATE BMI FIELD
    // Endpoint: PATCH /api/users/{username}/bmi
    // Updates the BMI field for a user
    // -------------------------------------------------------------------------
    @PatchMapping("/{username}/bmi")
    public CompletableFuture<ResponseEntity<?>> updateBmi(@PathVariable String username,
                                                          @RequestParam double bmi) {
        // Call service to update BMI
        return firebaseService.updateBmi(username, bmi).thenApply(success -> {
            if (!success) {
                // Return 404 if user not found
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }

            // Otherwise return success message
            return ResponseEntity
                    .ok("BMI updated successfully");
        });
    }

    // עדכון מים (PATCH /api/users/{username}/water?amount=...)
    @PatchMapping("/{username}/water")
    public CompletableFuture<ResponseEntity<?>> updateWater(@PathVariable String username,
                                                            @RequestParam int amount) {
        return firebaseService.updateWater(username, amount).thenApply(success -> {
            if (!success) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found or error");
            }
            return ResponseEntity
                    .ok("Water updated successfully");
        });
    }

    // קבלת מים (GET /api/users/{username}/water)
    @GetMapping("/{username}/water")
    public CompletableFuture<ResponseEntity<?>> getWater(@PathVariable String username) {
        return firebaseService.getWater(username).thenApply(result -> {
            if (result == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND).body("User not found");
            }
            return ResponseEntity
                    .ok(result);
        });
    }
}
