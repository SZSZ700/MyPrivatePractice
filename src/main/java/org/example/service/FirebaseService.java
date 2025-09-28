// Define the package for this service class
package org.example.service;

// Import Firebase Realtime Database classes
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

// Import our custom User model
import org.example.model.User;

// Import Spring annotation to mark this class as a service
import org.json.JSONObject;
import org.springframework.stereotype.Service;

// Import utilities for date/time and collections
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

// Import CompletableFuture for async handling
import java.util.concurrent.CompletableFuture;

// Import ExecutionException if needed
import java.util.concurrent.ExecutionException;

// -------------------------------------------------------------------------
// Mark this class as a Spring-managed service (so it can be injected)
// -------------------------------------------------------------------------
@Service
public class FirebaseService {

    // ---------------------------------------------------------------------
    // Reference to the "Users" node in Firebase Realtime Database
    // ---------------------------------------------------------------------
    private final DatabaseReference usersRef;

    // ---------------------------------------------------------------------
    // Constructor: initialize Firebase reference
    // ---------------------------------------------------------------------
    public FirebaseService() throws IOException {
        // Load the service account key (from resources)
        InputStream serviceAccount = getClass().getResourceAsStream("/firebase-key.json");

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://YOUR_PROJECT_ID.firebaseio.com/")
                    .build();

            FirebaseApp.initializeApp(options);
        }

        this.usersRef = FirebaseDatabase.getInstance().getReference("users");
    }


    // ---------------------------------------------------------------------
    // CREATE USER
    // ---------------------------------------------------------------------
    public CompletableFuture<Boolean> createUser(User user) {
        // Future result
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Query to check if user exists already
        usersRef.orderByChild("userName").equalTo(user.getUserName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // If username exists -> fail
                        if (snapshot.exists()) {
                            future.complete(false);
                        } else {
                            // Otherwise push new user object
                            usersRef.push()
                                    .setValueAsync(user)
                                    // Complete with true on success
                                    .addListener(() -> future.complete(true), Runnable::run);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with error
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return async future
        return future;
    }

    // ---------------------------------------------------------------------
    // GET ALL USERS
    // ---------------------------------------------------------------------
    public CompletableFuture<List<User>> getAllUsers() {
        // Future with list of users
        CompletableFuture<List<User>> future = new CompletableFuture<>();

        // Attach listener to fetch all
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Collect results into list
                List<User> users = new ArrayList<>();

                // Convert each child into User object
                for (DataSnapshot child : snapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    if (user != null) {
                        users.add(user);
                    }
                }

                // Complete with user list
                future.complete(users);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Complete with exception
                future.completeExceptionally(error.toException());
            }
        });

        return future;
    }

    // ---------------------------------------------------------------------
    // GET USER BY USERNAME
    // ---------------------------------------------------------------------
    public CompletableFuture<User> getUser(String username) {
        // Future with User object
        CompletableFuture<User> future = new CompletableFuture<>();

        // Query by userName
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Iterate through children (only 1 expected)
                        for (DataSnapshot child : snapshot.getChildren()) {
                            User user = child.getValue(User.class);
                            if (user != null) {
                                // Complete with found user
                                future.complete(user);
                                return;
                            }
                        }
                        // If none found
                        future.complete(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with exception
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ---------------------------------------------------------------------
    // UPDATE USER (replace full object)
    // ---------------------------------------------------------------------
    public CompletableFuture<Boolean> updateUser(String username, User updatedUser) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Query for username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Fail if not found
                        if (!snapshot.exists()) {
                            future.complete(false);
                            return;
                        }

                        // Replace data with new user object
                        for (DataSnapshot child : snapshot.getChildren()) {
                            child.getRef().setValueAsync(updatedUser)
                                    .addListener(() -> future.complete(true), Runnable::run);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ---------------------------------------------------------------------
    // PATCH USER (partial update)
    // ---------------------------------------------------------------------
    public CompletableFuture<User> patchUser(String username, Map<String, Object> updates) {
        CompletableFuture<User> future = new CompletableFuture<>();

        // Query by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            future.complete(null);
                            return;
                        }

                        // Apply updates to found user
                        for (DataSnapshot child : snapshot.getChildren()) {
                            child.getRef().updateChildrenAsync(updates)
                                    .addListener(() -> {
                                        // Get updated user back
                                        User updatedUser = child.getValue(User.class);
                                        future.complete(updatedUser);
                                    }, Runnable::run);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ---------------------------------------------------------------------
    // DELETE USER
    // ---------------------------------------------------------------------
    public CompletableFuture<Boolean> deleteUser(String username) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Query user by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            // User not found
                            future.complete(false);
                            return;
                        }

                        // Delete node
                        for (DataSnapshot child : snapshot.getChildren()) {
                            child.getRef().removeValueAsync()
                                    .addListener(() -> future.complete(true), Runnable::run);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ---------------------------------------------------------------------
    // CHECK IF USER EXISTS
    // ---------------------------------------------------------------------
    public CompletableFuture<Boolean> exists(String username) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Query by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Complete with true/false
                        future.complete(snapshot.exists());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ---------------------------------------------------------------------
    // UPDATE BMI FIELD
    // ---------------------------------------------------------------------
    public CompletableFuture<Boolean> updateBmi(String username, double bmi) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Query by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            future.complete(false);
                            return;
                        }

                        // Update "bmi" field
                        for (DataSnapshot child : snapshot.getChildren()) {
                            child.getRef().child("bmi").setValueAsync(bmi)
                                    .addListener(() -> future.complete(true), Runnable::run);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ---------------------------------------------------------------------
    // UPDATE WATER LOG
    // ---------------------------------------------------------------------
    public CompletableFuture<Boolean> updateWater(String username, int waterAmount) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Query user by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            // Fail if user not found
                            future.complete(false);
                            return;
                        }

                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            // Generate today key (yyyy-MM-dd)
                            String dayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                            // Reference to today's water log
                            DatabaseReference todayRef = userSnap.getRef().child("waterLog").child(dayKey);

                            // Read data for today
                            todayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot daySnapshot) {
                                    // Load existing list or create new 13-slot list
                                    List<Long> dayList = daySnapshot.exists()
                                            ? (List<Long>) daySnapshot.getValue()
                                            : new ArrayList<>(Collections.nCopies(13, 0L));

                                    // Find next free slot (1–12)
                                    int cupIndex = 1;
                                    while (cupIndex <= 12 && dayList.get(cupIndex) > 0) {
                                        cupIndex++;
                                    }

                                    if (cupIndex <= 12) {
                                        // Insert water amount
                                        dayList.set(cupIndex, (long) waterAmount);

                                        // Update total at index 0
                                        long sum = dayList.get(0);
                                        dayList.set(0, sum + waterAmount);

                                        // Save back to Firebase
                                        todayRef.setValue(dayList, (error, ref) -> {
                                            if (error == null) {
                                                future.complete(true);
                                            } else {
                                                future.complete(false);
                                            }
                                        });
                                    } else {
                                        // No free slots left
                                        future.complete(false);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    future.completeExceptionally(error.toException());
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ---------------------------------------------------------------------
    // GET WATER LOG (today + yesterday totals)
    // ---------------------------------------------------------------------
    public CompletableFuture<JSONObject> getWater(String username) {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();

        // Generate today key
        String todayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        // Generate yesterday key
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        String yesterdayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());

        // Query user by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            // Fail if not found
                            future.complete(null);
                            return;
                        }

                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            // Extract totals from waterLog
                            Long todayAmount = userSnap.child("waterLog").child(todayKey).child("0").getValue(Long.class);
                            Long yesterdayAmount = userSnap.child("waterLog").child(yesterdayKey).child("0").getValue(Long.class);

                            // Build JSON response
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("totalWater", todayAmount != null ? todayAmount : 0);
                                obj.put("yesterdayWater", yesterdayAmount != null ? yesterdayAmount : 0);
                            } catch (Exception e) {
                                future.complete(null);
                                return;
                            }

                            // Complete with JSON
                            future.complete(obj);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }
}

