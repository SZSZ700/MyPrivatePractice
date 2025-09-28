// Define the package where this controller belongs
package org.example.web;

// Import the User model (POJO with username, password, age, fullName)
import org.example.model.User;

// Import the Firebase service that handles database operations
import org.example.service.FirebaseService;

// Import Spring framework classes for HTTP status and response handling
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Import Java utility classes
import java.util.*;
import java.util.concurrent.CompletableFuture;   // For async non-blocking calls

// -------------------------------------------------------------------------
// Marks this class as a REST controller → all methods return JSON by default
// -------------------------------------------------------------------------
@RestController
// Base URL for all endpoints in this controller
@RequestMapping("/api/users")
public class UsersController {

    // ---------------------------------------------------------------------
    // Reference to FirebaseService (business logic layer)
    // Will be injected automatically by Spring Boot (constructor injection)
    // ---------------------------------------------------------------------
    private final FirebaseService firebaseService;

    // Constructor for dependency injection
    public UsersController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }
    // ---------------------------------------------------------------------
    // HEALTH CHECK (GET /api/users/health)
    // Simple endpoint to verify the server is alive
    // ---------------------------------------------------------------------
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }


    // =========================================================
    // SIGNUP (POST /api/users/signup)
    // Android → RestClient.register(user) → here
    // =========================================================
    @PostMapping("/signup")
    public CompletableFuture<ResponseEntity<String>> signup(@RequestBody User user) {
        // Call service.signup() which checks username and creates user
        return firebaseService.signup(user).thenApply(result -> {
            if ("User created successfully".equals(result)) {
                // Return HTTP 201 if success
                return ResponseEntity.status(HttpStatus.CREATED).body(result);
            } else if ("Username already exists".equals(result)) {
                // Return HTTP 409 if username exists
                return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
            } else {
                // Return HTTP 500 for generic errors
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            }
        });
    }

    // =========================================================
    // LOGIN (POST /api/users/login)
    // Android → RestClient.login(username,password) → here
    // =========================================================
    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<?>> login(@RequestBody User loginRequest) {
        // Extract username & password from request body
        String username = loginRequest.getUserName();
        String password = loginRequest.getPassword();

        // Call service.login() which validates credentials
        return firebaseService.login(username, password).thenApply(user -> {
            if (user != null) {
                // Return HTTP 200 with user object if valid
                return ResponseEntity.ok(user);
            } else {
                // Return HTTP 401 if invalid
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid username or password");
            }
        });
    }

    // ---------------------------------------------------------------------
    // GET ALL USERS (GET /api/users)
    // Returns list of all users stored in Firebase
    // ---------------------------------------------------------------------
    @GetMapping
    public CompletableFuture<ResponseEntity<List<User>>> getAllUsers() {
        // Call service and wrap result in 200 OK
        return firebaseService.getAllUsers().thenApply(ResponseEntity::ok);
    }

    // ---------------------------------------------------------------------
    // GET USER BY USERNAME (GET /api/users/{username})
    // Retrieves a single user by username
    // ---------------------------------------------------------------------
    @GetMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> getUser(@PathVariable String username) {
        return firebaseService.getUser(username).thenApply(user -> {
            if (user == null) {
                // If user not found → return 404
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
            // Otherwise return 200 OK with user object
            return ResponseEntity.ok(user);
        });
    }

    // ---------------------------------------------------------------------
    // UPDATE USER (PUT /api/users/{username})
    // Replaces the entire user object
    // ---------------------------------------------------------------------
    @PutMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> updateUser(@PathVariable String username,
                                                           @RequestBody User updatedUser) {
        return firebaseService.updateUser(username, updatedUser).thenApply(success -> {
            if (!success) {
                // If user does not exist → return 404
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
            // Otherwise return 200 OK with updated user
            return ResponseEntity.ok(updatedUser);
        });
    }

    // ---------------------------------------------------------------------
    // PATCH USER (PATCH /api/users/{username})
    // Performs a partial update of user fields
    // ---------------------------------------------------------------------
    @PatchMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> patchUser(@PathVariable String username,
                                                          @RequestBody Map<String, Object> updates) {
        return firebaseService.patchUser(username, updates).thenApply(updatedUser -> {
            if (updatedUser == null) {
                // If not found → 404
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
            // Return updated user object
            return ResponseEntity.ok(updatedUser);
        });
    }

    // ---------------------------------------------------------------------
    // DELETE USER (DELETE /api/users/{username})
    // Removes a user from Firebase
    // ---------------------------------------------------------------------
    @DeleteMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> deleteUser(@PathVariable String username) {
        return firebaseService.deleteUser(username).thenApply(success -> {
            if (!success) {
                // If not found → 404
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
            // Otherwise → return success message
            return ResponseEntity.ok("User deleted");
        });
    }

    // ---------------------------------------------------------------------
    // HEAD USER (HEAD /api/users/{username})
    // Checks if user exists (status only, no body)
    // ---------------------------------------------------------------------
    @RequestMapping(value = "/{username}", method = RequestMethod.HEAD)
    public CompletableFuture<ResponseEntity<Void>> headUser(@PathVariable String username) {
        return firebaseService.exists(username).thenApply(exists -> {
            if (exists) {
                // If exists → return 200 OK
                return ResponseEntity.ok().build();
            }
            // Otherwise → return 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        });
    }

    // ---------------------------------------------------------------------
    // UPDATE BMI (PATCH /api/users/{username}/bmi?bmi=...)
    // Updates the "bmi" field of a user
    // ---------------------------------------------------------------------
    @PatchMapping("/{username}/bmi")
    public CompletableFuture<ResponseEntity<?>> updateBmi(@PathVariable String username,
                                                          @RequestParam double bmi) {
        return firebaseService.updateBmi(username, bmi).thenApply(success -> {
            if (!success) {
                // If user not found → return 404
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
            // Otherwise return success
            return ResponseEntity.ok("BMI updated successfully");
        });
    }

    // ---------------------------------------------------------------------
    // UPDATE WATER (PATCH /api/users/{username}/water?amount=...)
    // Adds a water entry for today (uses Firebase waterLog)
    // ---------------------------------------------------------------------
    @PatchMapping("/{username}/water")
    public CompletableFuture<ResponseEntity<?>> updateWater(@PathVariable String username,
                                                            @RequestParam int amount) {
        return firebaseService.updateWater(username, amount).thenApply(success -> {
            if (!success) {
                // If user not found or error → 404
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found or error");
            }
            // Otherwise → success
            return ResponseEntity.ok("Water updated successfully");
        });
    }

    // ---------------------------------------------------------------------
    // GET WATER (GET /api/users/{username}/water)
    // Returns JSON object with { totalWater, yesterdayWater }
    // ---------------------------------------------------------------------
    @GetMapping("/{username}/water")
    public CompletableFuture<ResponseEntity<?>> getWater(@PathVariable String username) {
        return firebaseService.getWater(username).thenApply(result -> {
            if (result == null) {
                // If not found → 404
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND).body("User not found");
            }
            // Otherwise return the JSON result
            return ResponseEntity.ok(result);
        });
    }
}

