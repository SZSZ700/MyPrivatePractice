// Define the package where this controller belongs
package org.example.web;

// Import the User model (POJO with username, password, age, fullName)
import org.example.model.User;

// Import the Firebase service that handles database operations
import org.example.service.FirebaseService;

// Import Spring framework classes for HTTP status and response handling
import org.example.service.JwtService;
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

    // JwtService used for generating JWT tokens
    private final JwtService jwtService;

    // Constructor for dependency injection
    public UsersController(FirebaseService firebaseService, JwtService jwtService) {
        this.firebaseService = firebaseService;
        // Save JwtService instance in a field
        this.jwtService = jwtService;

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
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(result);
            } else if ("Username already exists".equals(result)) {
                // Return HTTP 409 if username exists
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(result);
            } else {
                // Return HTTP 500 for generic errors
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(result);
            }
        });
    }

    // =========================================================
    // LOGIN (POST /api/users/login)
    // Android → RestClient.login(username,password) → here
    // =========================================================
    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<?>> login(@RequestBody User loginRequest) {
        // Extract username from request body
        String username = loginRequest.getUserName();
        // Extract password from request body
        String password = loginRequest.getPassword();

        // Call service.login() which validates credentials against Firebase
        return firebaseService.login(username, password).thenApply(user -> {
            // If user object is not null → credentials are valid
            if (user != null) {
                // Generate JWT token for this username
                String token = jwtService.generateToken(username);

                // Create a Map to hold both user data and token
                Map<String, Object> responseBody = new HashMap<>();
                // Put full user object under key "user"
                responseBody.put("user", user);
                // Put JWT token string under key "token"
                responseBody.put("token", token);

                // Return HTTP 200 OK with JSON: { "user": {...}, "token": "..." }
                return ResponseEntity
                        .ok(responseBody);
            } else {
                // If user is null → invalid credentials, return HTTP 401
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid username or password");
            }
        });
    }


    // ---------------------------------------------------------------------
    // GET ALL USERS (GET /api/users)
    // Returns a list of all users stored in Firebase
    // ---------------------------------------------------------------------
    @GetMapping
    public CompletableFuture<ResponseEntity<List<User>>> getAllUsers() {
        // Debug log: controller endpoint triggered
        System.out.println("DEBUG: getAllUsers called");

        // Call Firebase service to fetch all users, wrap result in ResponseEntity
        return firebaseService.getAllUsers()
                .thenApply(
                        ResponseEntity::ok
                );
    }

    // ---------------------------------------------------------------------
    // GET USER BY USERNAME (GET /api/users/{username})
    // Retrieves a single user by username
    // ---------------------------------------------------------------------
    @GetMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> getUser(@PathVariable("username") String username) {
        // Debug log: controller endpoint triggered with parameter
        System.out.println("DEBUG: getUser called → username=" + username);

        // Call Firebase service to fetch a specific user
        return firebaseService.getUser(username).thenApply(user -> {
            // If user not found, return 404 response
            if (user == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
            // If user found, return 200 OK with the user object
            return ResponseEntity.ok(user);
        });
    }

    // ---------------------------------------------------------------------
    // UPDATE USER (PUT /api/users/{username})
    // Replaces the entire user object with the provided one
    // ---------------------------------------------------------------------
    @PutMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> updateUser(
            @PathVariable("username") String username,
            @RequestBody User updatedUser) {
        // Debug log: endpoint triggered with username and request body
        System.out.println("DEBUG: updateUser called → username=" + username + ", body=" + updatedUser);

        // Call Firebase service to update user
        return firebaseService.updateUser(username, updatedUser).thenApply(success -> {
            // If user not found, return 404
            if (!success) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
            // If success, return 200 OK with updated user
            return ResponseEntity.ok(updatedUser);
        });
    }

    // ---------------------------------------------------------------------
    // PATCH USER (PATCH /api/users/{username})
    // Performs a partial update of user fields
    // ---------------------------------------------------------------------
    @PatchMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> patchUser(
            @PathVariable("username") String username,
            @RequestBody Map<String, Object> updates) {
        // Debug log: endpoint triggered with username and updates map
        System.out.println("DEBUG: patchUser called → username=" + username + ", updates=" + updates);

        // Call Firebase service to patch user fields
        return firebaseService.patchUser(username, updates).thenApply(updatedUser -> {
            // If user not found, return 404
            if (updatedUser == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
            // If success, return 200 OK with updated user object
            return ResponseEntity.ok(updatedUser);
        });
    }

    // ---------------------------------------------------------------------
    // DELETE USER (DELETE /api/users/{username})
    // Removes a user from Firebase
    // ---------------------------------------------------------------------
    @DeleteMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> deleteUser(@PathVariable("username") String username) {
        System.out.println("DEBUG: deleteUser called → username=" + username);
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
    // HEAD USER (HEAD /api/users/{username})
    // Checks if a user exists (status code only, no response body)
    // ---------------------------------------------------------------------
    @RequestMapping(value = "/{username}", method = RequestMethod.HEAD)
    public CompletableFuture<ResponseEntity<Void>> headUser(@PathVariable("username") String username) {
        // Debug log: endpoint triggered
        System.out.println("DEBUG: headUser called → username=" + username);

        // Call Firebase service to check if user exists
        return firebaseService.exists(username).thenApply(exists -> {
            if (exists) {
                // Return 200 OK if user exists
                return ResponseEntity.ok().build();
            }
            // Return 404 if user not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        });
    }

    // ---------------------------------------------------------------------
    // UPDATE BMI (PATCH /api/users/{username}/bmi?bmi=...)
    // Updates the "bmi" field of a user
    // ---------------------------------------------------------------------
    @PatchMapping("/{username}/bmi")
    public CompletableFuture<ResponseEntity<?>> updateBmi(
            @PathVariable("username") String username,
            @RequestParam("bmi") double bmi) {

        // Debug log: endpoint triggered with username and bmi
        System.out.println("DEBUG: updateBmi called → username=" + username + ", bmi=" + bmi);

        // Call Firebase service to update BMI
        return firebaseService.updateBmi(username, bmi).thenApply(success -> {
            if (!success) {
                // If user not found, return 404 with error message
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
            // If success, return 200 OK with confirmation message
            return ResponseEntity.ok("BMI updated successfully");
        });
    }

    // ---------------------------------------------------------------------
    // UPDATE WATER (PATCH /api/users/{username}/water?amount=...)
    // Adds a water entry for today in the user's waterLog
    // ---------------------------------------------------------------------
    @PatchMapping("/{username}/water")
    public CompletableFuture<ResponseEntity<?>> updateWater(
            @PathVariable("username") String username,
            @RequestParam("amount") int amount) {

        // Call Firebase service to update water log

        // ⚠️⤵️⚠️
        // thenApply - adds a callback to the internal callback list of the Future.
        // Once `complete()` is called, the Future executes all registered callbacks.
        // ⚠️⤴️⚠️

        // What does this callback do?
        // It unwraps the Future's content (the Boolean) and places it into the response.
        return firebaseService.updateWater(username, amount).thenApply(success -> {
            if (!success) {
                // If user not found or slots are full, return 404 with error message
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found or error");
            }
            // If success, return 200 OK with confirmation message
            return ResponseEntity.ok("Water updated successfully");
        });
    }

    // ---------------------------------------------------------------------
    // GET WATER (GET /api/users/{username}/water)
    // Returns a JSON object with { todayWater, yesterdayWater }
    // ---------------------------------------------------------------------
    @GetMapping("/{username}/water")
    public CompletableFuture<ResponseEntity<?>> getWater(
            @PathVariable("username") String username) {

        // Call Firebase service to get today's and yesterday's water amounts
        return firebaseService.getWater(username)
                // ⚠️⤵️⚠️
                // thenApply - adds a callback to the internal callback list of the Future.
                // Once `complete()` is called, the Future executes all registered callbacks.
                // ⚠️⤴️⚠️

                // What does this callback do?
                // It unwraps the Future's content (the JASONObject) and places it into the response.
                .thenApply(result -> {
                    // If user not found, return 404
                    if (result == null) {
                        return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body("User not found");
                    }

                    // Convert JSONObject from FirebaseService into a plain Java Map
                    Map<String, Object> response = new HashMap<>();
                    response.put("todayWater", result.optInt("todayWater", 0));
                    response.put("yesterdayWater", result.optInt("yesterdayWater", 0));

                    // Return 200 OK with the response map
                    return ResponseEntity
                            .ok(response);
        });
    }

    // ---------------------------------------------------------------------
    // GET WATER HISTORY MAP (GET /api/users/{username}/waterHistoryMap?days=7)
    // Returns JSON object like: {"2025-09-29":4600, "2025-09-28":0, ...}
    // ---------------------------------------------------------------------
    @GetMapping("/{username}/waterHistoryMap")
    public CompletableFuture<ResponseEntity<?>> getWaterHistoryMap(
            @PathVariable("username") String username,
            @RequestParam(name = "days", defaultValue = "7") int days) {

        // Debug log: endpoint triggered with username and days parameter
        System.out.println("DEBUG UsersController.getWaterHistoryMap -> username="
                + username + " days=" + days);

        // Call Firebase service to get the water history map
        return firebaseService.getWaterHistoryMap(username, days)
                // ⚠️⤵️⚠️
                // thenApply - adds a callback to the internal callback list of the Future.
                // Once `complete()` is called, the Future executes all registered callbacks.
                // ⚠️⤴️⚠️

                // What does this callback do?
                // It unwraps the Future's content (the Map) and places it into the response.
                .thenApply(result -> {
                    // If user not found, return 404 with error message
                    if (result == null) {
                        System.out.println("DEBUG UsersController.getWaterHistoryMap -> result=null (user not found)");
                        return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body("User not found");
                    }

                    // Debug log: print the map before sending response
                    System.out.println("DEBUG UsersController.getWaterHistoryMap -> sending response: " + result);

                    // Return 200 OK with the history map
                    return ResponseEntity
                            .ok(result);
                });
    }

    // -------------------------------------------------------------
    // GET /api/users/{username}/weeklyAverages
    // Returns a JSON map with day labels as keys and averages as values
    // Example: {"Mon": 6200, "Tue": 2740, ...}
    // -------------------------------------------------------------
    // UsersController
    @GetMapping("/{username}/weeklyAverages")
    public CompletableFuture<ResponseEntity<Map<String, Integer>>> getWeeklyAverages(
            @PathVariable("username") String username) {

        // Debug log: endpoint triggered with username
        System.out.println("DEBUG UsersController.getWeeklyAverages -> username=" + username);

        // Call Firebase service to get weekly averages
        // Initially returns an empty FUTURE (container for async result)
        return firebaseService.getWeeklyAverages(username)

                // ⚠️⤵️⚠️
                // thenApply - adds a callback to the internal callback list of the Future.
                // Once `complete()` is called, the Future executes all registered callbacks.
                // ⚠️⤴️⚠️

                // What does this callback do?
                // It unwraps the Future's content (the Map) and places it into the response.
                .thenApply(result -> {
                    // If result is null or empty, return 404 with an empty map
                    if (result == null || result.isEmpty()) {

                        System.out.println("DEBUG getWeeklyAverages -> no data");

                        // ResponseEntity is returned asynchronously once Future is completed
                        return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(Collections.emptyMap());
                    }

                    // Debug log: show the result before sending response
                    System.out.println("DEBUG getWeeklyAverages -> sending result: " + result);

                    // Return 200 OK with the result map
                    return ResponseEntity
                            .ok(result);
                });

        // Execution is placed into Spring's internal async waiting queue
    }

    // -------------------------------------------
    // GET /api/users/{username}/goal  -> {"goalMl": 2600}
    // -------------------------------------------
    @GetMapping("/{username}/goal")
    public CompletableFuture<ResponseEntity<Map<String, Integer>>> getGoal(
            @PathVariable("username") String username) { // Explicit name avoids parameter-name reflection issues

        // Debug log: endpoint triggered
        System.out.println("DEBUG getGoal -> username=" + username);

        // Call Firebase service to get goal value
        return firebaseService.getGoalMl(username)
                // ⚠️⤵️⚠️
                // thenApply - adds a callback to the internal callback list of the Future.
                // Once `complete()` is called, the Future executes all registered callbacks.
                // ⚠️⤴️⚠️

                // What does this callback do?
                // It unwraps the Future's content (the Integer) and places it into the response.
                .thenApply(goal -> {
                    // Always return 200 OK with a value (default if not found)
                    return ResponseEntity
                            .ok(
                                    Map.of("goalMl", goal)
                            );
                })
                .exceptionally(ex -> {
                    // On failure return 500 Internal Server Error
                    System.err.println("ERROR getGoal -> " + ex.getMessage());

                    return ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(
                                    Map.of()
                            );
                });
    }

    // ------------------------------------------------------
    // PUT /api/users/{username}/goal?goalMl=2600  -> 200/400
    // ------------------------------------------------------
    @PutMapping("/{username}/goal")
    public CompletableFuture<ResponseEntity<Map<String, String>>> setGoal(
            @PathVariable("username") String username,
            @RequestParam("goalMl") int goalMl) {

        // Debug log: endpoint triggered
        System.out.println("DEBUG setGoal -> username=" + username + " goalMl=" + goalMl);

        // Call Firebase service to update goal
        return firebaseService.updateGoalMl(username, goalMl)
                // ⚠️⤵️⚠️
                // thenApply - adds a callback to the internal callback list of the Future.
                // Once `complete()` is called, the Future executes all registered callbacks.
                // ⚠️⤴️⚠️

                // What does this callback do?
                // It unwraps the Future's content (the Boolean) and places it into the response.
                .thenApply(ok -> ok
                        // If update succeeds -> 200 OK
                        ? ResponseEntity
                        .ok(Map.of("status", "OK"))
                        // If invalid value or user not found -> 400 BAD REQUEST
                        : ResponseEntity
                        .badRequest()
                        .body(
                                Map.of("status", "INVALID_OR_NOT_FOUND")
                        )
                )
                .exceptionally(ex -> {
                    // On exception -> 500 Internal Server Error
                    System.err.println("ERROR setGoal -> " + ex.getMessage());

                    return ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(
                                    Map.of("status", "ERROR")
                            );
                });
    }

    // ---------------------------------------------------------------------
    // BMI DISTRIBUTION (GET /api/users/stats/bmiDistribution)
    // Returns aggregated statistics of how many users fall into each BMI category.
    // Example response:
    // {
    //   "Underweight": 3,
    //   "Normal": 12,
    //   "Overweight": 5,
    //   "Obese": 2
    // }
    // ---------------------------------------------------------------------
    @GetMapping("/stats/bmiDistribution")
    public CompletableFuture<ResponseEntity<Map<String, Integer>>> getBmiDistribution() {

        // Debug log: endpoint triggered
        System.out.println("DEBUG UsersController.getBmiDistribution called");

        // Call Firebase service to calculate BMI distribution
        return firebaseService.getBmiDistribution()
                .thenApply(result -> {
                    // Never null – always at least empty map
                    return ResponseEntity
                            .ok(result);
                })
                .exceptionally(ex -> {
                    // On failure -> 500 Internal Server Error
                    System.err.println("ERROR UsersController.getBmiDistribution -> " + ex.getMessage());

                    return ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Collections.emptyMap());
                });
    }

    // -------------------------------- GET CALORIES ----------------------------
    // Returns JSON: {"calories": 1800}
    @GetMapping("/{username}/calories")
    public CompletableFuture<ResponseEntity<Map<String, Integer>>> getCalories(
            @PathVariable("username") String username) {

        return firebaseService.getCalories(username)
                .thenApply(cals -> {
                    // Build a simple JSON map: {"calories": X}
                    Map<String, Integer> body = Collections.singletonMap("calories",
                            (cals != null ? cals : 0));

                    return ResponseEntity
                            .ok(body);
                });
    }

    // -------------------------------- UPDATE CALORIES -------------------------
    // Updates the "calories" field for this user.
    // Example call: PUT /api/users/john/calories?calories=1800
    @PutMapping("/{username}/calories")
    public CompletableFuture<ResponseEntity<Void>> updateCalories(
            @PathVariable("username") String username,
            @RequestParam("calories") int calories) {

        return firebaseService.updateCalories(username, calories)
                .thenApply(success -> {
                    if (success) {
                        // 204 No Content on success
                        return ResponseEntity
                                .noContent()
                                .build();
                    } else {
                        // If invalid value or user not found → 400 Bad Request
                        return ResponseEntity
                                .badRequest()
                                .build();
                    }
                });
    }
}

