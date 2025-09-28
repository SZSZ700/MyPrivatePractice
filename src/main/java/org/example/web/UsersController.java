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

    // Reference to FirebaseService (business logic)
    private final FirebaseService firebaseService;

    // Constructor for dependency injection of FirebaseService
    public UsersController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    // ---------------------------------------------------------------------
    // CREATE USER
    // POST /api/users
    // Creates a new user in Firebase if not already existing
    // ---------------------------------------------------------------------
    @PostMapping
    public CompletableFuture<ResponseEntity<?>> createUser(@RequestBody User user) {
        return firebaseService.createUser(user).thenApply(success -> {
            if (!success) {
                // If user already exists → return 409
                return ResponseEntity
                        .status(HttpStatus.CONFLICT).body("User already exists");
            }
            // Otherwise return 201 with user data
            return ResponseEntity
                    .status(HttpStatus.CREATED).body(user);
        });
    }

    // ---------------------------------------------------------------------
    // LOGIN
    // POST /api/users/login
    // Validates username + password against Firebase
    // ---------------------------------------------------------------------
    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<?>> loginUser(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("userName");
        String password = credentials.get("password");

        return firebaseService.getUser(username).thenApply(user -> {
            if (user == null || !user.getPassword().equals(password)) {
                // Wrong credentials → return 401
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid username or password");
            }
            // Success → return user
            return ResponseEntity.ok(user);
        });
    }

    // ---------------------------------------------------------------------
    // GET ALL USERS
    // GET /api/users
    // ---------------------------------------------------------------------
    @GetMapping
    public CompletableFuture<ResponseEntity<List<User>>> getAllUsers() {
        return firebaseService.getAllUsers().thenApply(ResponseEntity::ok);
    }

    // ---------------------------------------------------------------------
    // GET USER BY USERNAME
    // GET /api/users/{username}
    // ---------------------------------------------------------------------
    @GetMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> getUser(@PathVariable String username) {
        return firebaseService.getUser(username).thenApply(user -> {
            if (user == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
            return ResponseEntity.ok(user);
        });
    }

    // ---------------------------------------------------------------------
    // UPDATE USER
    // PUT /api/users/{username}
    // Replace entire user object
    // ---------------------------------------------------------------------
    @PutMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> updateUser(@PathVariable String username,
                                                           @RequestBody User updatedUser) {
        return firebaseService.updateUser(username, updatedUser).thenApply(success -> {
            if (!success) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
            return ResponseEntity.ok(updatedUser);
        });
    }

    // ---------------------------------------------------------------------
    // PATCH USER
    // PATCH /api/users/{username}
    // Partial update
    // ---------------------------------------------------------------------
    @PatchMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> patchUser(@PathVariable String username,
                                                          @RequestBody Map<String, Object> updates) {
        return firebaseService.patchUser(username, updates).thenApply(updatedUser -> {
            if (updatedUser == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
            return ResponseEntity.ok(updatedUser);
        });
    }

    // ---------------------------------------------------------------------
    // DELETE USER
    // DELETE /api/users/{username}
    // ---------------------------------------------------------------------
    @DeleteMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> deleteUser(@PathVariable String username) {
        return firebaseService.deleteUser(username).thenApply(success -> {
            if (!success) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
            return ResponseEntity.ok("User deleted");
        });
    }

    // ---------------------------------------------------------------------
    // HEAD USER
    // HEAD /api/users/{username}
    // Check if user exists (only status, no body)
    // ---------------------------------------------------------------------
    @RequestMapping(value = "/{username}", method = RequestMethod.HEAD)
    public CompletableFuture<ResponseEntity<Void>> headUser(@PathVariable String username) {
        return firebaseService.exists(username).thenApply(exists -> {
            if (exists) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        });
    }

    // ---------------------------------------------------------------------
    // UPDATE BMI
    // PATCH /api/users/{username}/bmi?bmi=...
    // ---------------------------------------------------------------------
    @PatchMapping("/{username}/bmi")
    public CompletableFuture<ResponseEntity<?>> updateBmi(@PathVariable String username,
                                                          @RequestParam double bmi) {
        return firebaseService.updateBmi(username, bmi).thenApply(success -> {
            if (!success) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
            return ResponseEntity.ok("BMI updated successfully");
        });
    }

    // ---------------------------------------------------------------------
    // UPDATE WATER
    // PATCH /api/users/{username}/water?amount=...
    // ---------------------------------------------------------------------
    @PatchMapping("/{username}/water")
    public CompletableFuture<ResponseEntity<?>> updateWater(@PathVariable String username,
                                                            @RequestParam int amount) {
        return firebaseService.updateWater(username, amount).thenApply(success -> {
            if (!success) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found or error");
            }
            return ResponseEntity.ok("Water updated successfully");
        });
    }

    // ---------------------------------------------------------------------
    // GET WATER
    // GET /api/users/{username}/water
    // Returns JSON { totalWater, yesterdayWater }
    // ---------------------------------------------------------------------
    @GetMapping("/{username}/water")
    public CompletableFuture<ResponseEntity<?>> getWater(@PathVariable String username) {
        return firebaseService.getWater(username).thenApply(result -> {
            if (result == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND).body("User not found");
            }
            return ResponseEntity.ok(result);
        });
    }
}
