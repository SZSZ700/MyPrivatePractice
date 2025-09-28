package org.example.controller;

import org.example.model.User;
import org.example.service.FirebaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api") // Base URL -> http://localhost:8080/api
public class UserController {

    private final FirebaseService firebaseService;

    // Constructor injection of the FirebaseService
    public UserController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    // ---------------------------------------------------------------------
    // Endpoint: POST /api/register
    // Registers a new user into Firebase
    // ---------------------------------------------------------------------
    @PostMapping("/register")
    public CompletableFuture<ResponseEntity<String>> register(@RequestBody User user) {
        return firebaseService.register(user)
                .thenApply(success -> {
                    if (success) {
                        // 200 OK
                        return ResponseEntity
                                .ok("User registered successfully");
                    } else {
                        // 400 Bad Request
                        return ResponseEntity
                                .badRequest()
                                .body("Username already exists");
                    }
                });
    }

    // ---------------------------------------------------------------------
    // Endpoint: POST /api/login
    // Logs in an existing user
    // Returns:
    //   200 OK with User object if successful
    //   401 Unauthorized if login failed
    // ---------------------------------------------------------------------
    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<?>> login(@RequestParam String userName,
                                                      @RequestParam String password) {
        return firebaseService.login(userName, password)
                .thenApply(user -> {
                    if (user != null) {
                        // 200 OK -> return User as JSON
                        return ResponseEntity
                                .ok(user);
                    } else {
                        // 401 Unauthorized
                        return ResponseEntity
                                .status(401)
                                .body("Invalid username or password");
                    }
                });
    }
}
