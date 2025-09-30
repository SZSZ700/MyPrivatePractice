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
        System.out.println("DEBUG: getAllUsers called");
        return firebaseService.getAllUsers().thenApply(ResponseEntity::ok);
    }

    // ---------------------------------------------------------------------
    // GET USER BY USERNAME (GET /api/users/{username})
    // Retrieves a single user by username
    // ---------------------------------------------------------------------
    @GetMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> getUser(@PathVariable("username") String username) {
        System.out.println("DEBUG: getUser called → username=" + username);
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
    // UPDATE USER (PUT /api/users/{username})
    // Replaces the entire user object
    // ---------------------------------------------------------------------
    @PutMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> updateUser(
            @PathVariable("username") String username,
            @RequestBody User updatedUser) {
        System.out.println("DEBUG: updateUser called → username=" + username + ", body=" + updatedUser);
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
    // PATCH USER (PATCH /api/users/{username})
    // Performs a partial update of user fields
    // ---------------------------------------------------------------------
    @PatchMapping("/{username}")
    public CompletableFuture<ResponseEntity<?>> patchUser(
            @PathVariable("username") String username,
            @RequestBody Map<String, Object> updates) {
        System.out.println("DEBUG: patchUser called → username=" + username + ", updates=" + updates);
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
    // Checks if user exists (status only, no body)
    // ---------------------------------------------------------------------
    @RequestMapping(value = "/{username}", method = RequestMethod.HEAD)
    public CompletableFuture<ResponseEntity<Void>> headUser(@PathVariable("username") String username) {
        System.out.println("DEBUG: headUser called → username=" + username);
        return firebaseService.exists(username).thenApply(exists -> {
            if (exists) {
                return ResponseEntity.ok().build();
            }
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

        System.out.println("DEBUG: updateBmi called → username=" + username + ", bmi=" + bmi);

        return firebaseService.updateBmi(username, bmi).thenApply(success -> {
            if (!success) {
                // אם המשתמש לא נמצא → החזר 404
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
            // אחרת החזר הצלחה
            return ResponseEntity.ok("BMI updated successfully");
        });
    }

    // ---------------------------------------------------------------------
    // UPDATE WATER (PATCH /api/users/{username}/water?amount=...)
    // Adds a water entry for today (uses Firebase waterLog)
    // ---------------------------------------------------------------------
    @PatchMapping("/{username}/water")
    public CompletableFuture<ResponseEntity<?>> updateWater(
            @PathVariable("username") String username,
            @RequestParam("amount") int amount) {

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
    // GET WATER (GET /api/users/{username}/water)
    // Returns JSON object with { todayWater, yesterdayWater }
    // ---------------------------------------------------------------------
    @GetMapping("/{username}/water")
    public CompletableFuture<ResponseEntity<?>> getWater(
            @PathVariable("username") String username) {

        return firebaseService.getWater(username).thenApply(result -> {
            if (result == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }

            // ממירים את JSONObject שהגיע מה־FirebaseService ל־Map רגיל
            Map<String, Object> response = new HashMap<>();
            response.put("todayWater", result.optInt("todayWater", 0));
            response.put("yesterdayWater", result.optInt("yesterdayWater", 0));

            return ResponseEntity
                    .ok(response);
        });
    }

    // ---------------------------------------------------------------------
    // GET WATER HISTORY MAP (GET /api/users/{username}/waterHistoryMap?days=7)
    // Returns JSON: {"2025-09-29":4600, "2025-09-28":0, ...}
    // ---------------------------------------------------------------------
    @GetMapping("/{username}/waterHistoryMap")
    public CompletableFuture<ResponseEntity<?>> getWaterHistoryMap(
            @PathVariable("username") String username,
            @RequestParam(name = "days", defaultValue = "7") int days) {

        System.out.println("DEBUG UsersController.getWaterHistoryMap -> username="
                + username + " days=" + days);

        return firebaseService.getWaterHistoryMap(username, days)
                .thenApply(result -> {
                    if (result == null) {
                        System.out.println("DEBUG UsersController.getWaterHistoryMap -> result=null (user not found)");
                        return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body("User not found");
                    }

                    // 🔹 Debug log before sending response
                    System.out.println("DEBUG UsersController.getWaterHistoryMap -> sending response: " + result);

                    return ResponseEntity.ok(result);
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

        System.out.println("DEBUG UsersController.getWeeklyAverages -> username=" + username);

        // מקבל את ה FUTURE הריק
        return firebaseService.getWeeklyAverages(username)

                // ⚠️⤵️⚠️
                // thenApply - מוסיף קולבאק לרשימות הפנימיות של הפיוטשר
                //כשה־complete קורה → ה־Future מפעיל את כל ה־Callbacks שנרשמו.
                // ⚠️⤴️⚠️

                // מה ה CALLBACK עושה?
                // מקיא את תוכן הFUTURE שזה MAP לתוך משתנה MAP נוסף
                .thenApply(result -> {
                    if (result == null || result.isEmpty()) {

                        System.out.println("DEBUG getWeeklyAverages -> no data");

                        // ותחזיר בהמשך כמובן מתי שהיא תופעל, תגובה! ללקוח!
                        return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(Collections.emptyMap());
                    }

                    System.out.println("DEBUG getWeeklyAverages -> sending result: " + result);

                    return ResponseEntity
                            .ok(result);
                });

        // נזרק לחדר ההמתנה הפנימי בספרינג
    }
}

