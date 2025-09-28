// Define the package where this service class belongs
package org.example.service;

// Import Firebase Realtime Database classes (Admin SDK)
import com.google.firebase.database.*;

// Import our custom User model
import org.example.model.User;

// Import Spring annotation to mark this class as a service
import org.json.JSONObject;
import org.springframework.stereotype.Service;

// Import utilities
import java.text.SimpleDateFormat;
import java.util.*;

// Import CompletableFuture for async handling
import java.util.concurrent.CompletableFuture;

// Import ExecutionException for handling future errors
import java.util.concurrent.ExecutionException;

// -------------------------------------------------------------------------
// Marks this class as a Spring-managed service, so it can be injected into controllers
// -------------------------------------------------------------------------
@Service
public class FirebaseService {

    // -------------------------------------------------------------------------
    // Reference to the "Users" node in the Firebase Realtime Database
    // -------------------------------------------------------------------------
    private final DatabaseReference usersRef;

    // -------------------------------------------------------------------------
    // Constructor - initializes the Firebase Database reference
    // -------------------------------------------------------------------------
    public FirebaseService() {
        // Get the default FirebaseDatabase instance
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Reference the "Users" node
        this.usersRef = database.getReference("Users");
    }

    // -------------------------------------------------------------------------
    // CREATE USER (asynchronous)
    // -------------------------------------------------------------------------
    public CompletableFuture<Boolean> createUser(User user) {
        // Future to represent the result of the async operation
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Query the database to check if a user with the same username already exists
        usersRef.orderByChild("userName").equalTo(user.getUserName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // If user already exists -> return false
                        if (snapshot.exists()) {
                            future.complete(false);
                        } else {
                            // Otherwise, push the new user object into Firebase
                            usersRef.push()
                                    .setValueAsync(user)
                                    // On success, complete the future with true
                                    .addListener(() -> future.complete(true), Runnable::run);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // If something goes wrong -> complete future with exception
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return the CompletableFuture immediately (async result will come later)
        return future;
    }

    // -------------------------------------------------------------------------
    // GET ALL USERS (asynchronous)
    // -------------------------------------------------------------------------
    public CompletableFuture<List<User>> getAllUsers() {
        // Future to hold a list of User objects
        CompletableFuture<List<User>> future = new CompletableFuture<>();

        // Attach a single-event listener to fetch all users
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Create list to hold the result
                List<User> users = new ArrayList<>();

                // Iterate through all children in the "Users" node
                for (DataSnapshot child : snapshot.getChildren()) {
                    // Convert each child into a User object
                    User user = child.getValue(User.class);
                    if (user != null) {
                        users.add(user);
                    }
                }
                // Complete the future with the list of users
                future.complete(users);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Complete with exception if Firebase fails
                future.completeExceptionally(error.toException());
            }
        });

        return future;
    }

    // -------------------------------------------------------------------------
    // GET A USER BY USERNAME
    // -------------------------------------------------------------------------
    public CompletableFuture<User> getUser(String username) {
        // Future to hold a User object
        CompletableFuture<User> future = new CompletableFuture<>();

        // Query for user where userName equals the provided username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop through results (normally one user)
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Deserialize Firebase data into User object
                            User user = child.getValue(User.class);
                            if (user != null) {
                                future.complete(user);
                                return;
                            }
                        }

                        // If no user found, return null
                        future.complete(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Exception on error
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // -------------------------------------------------------------------------
    // UPDATE USER (PUT - replace entire object)
    // -------------------------------------------------------------------------
    public CompletableFuture<Boolean> updateUser(String username, User updatedUser) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Find user by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // If user not found
                        if (!snapshot.exists()) {
                            future.complete(false);
                            return;
                        }

                        // Replace user data with updatedUser
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

    // -------------------------------------------------------------------------
    // PATCH USER (partial update - only some fields)
    // -------------------------------------------------------------------------
    public CompletableFuture<User> patchUser(String username, Map<String, Object> updates) {
        CompletableFuture<User> future = new CompletableFuture<>();

        // Query user by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // If not found -> return null
                        if (!snapshot.exists()) {
                            future.complete(null);
                            return;
                        }

                        // Apply updates to the found user
                        for (DataSnapshot child : snapshot.getChildren()) {
                            child.getRef().updateChildrenAsync(updates)
                                    .addListener(() -> {
                                        // Fetch updated user back from snapshot
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

    // -------------------------------------------------------------------------
    // DELETE USER
    // -------------------------------------------------------------------------
    public CompletableFuture<Boolean> deleteUser(String username) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Query user by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // If user not found
                        if (!snapshot.exists()) {
                            future.complete(false);
                            return;
                        }

                        // Remove the node
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

    // -------------------------------------------------------------------------
    // CHECK IF USER EXISTS
    // -------------------------------------------------------------------------
    public CompletableFuture<Boolean> exists(String username) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Complete with true if found, false otherwise
                        future.complete(snapshot.exists());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // -------------------------------------------------------------------------
    // UPDATE BMI FIELD FOR USER
    // -------------------------------------------------------------------------
    public CompletableFuture<Boolean> updateBmi(String username, double bmi) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            future.complete(false);
                            return;
                        }

                        // Update the "bmi" field
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

    // ============================================================================
    // Update water log for a specific user and day
    // ============================================================================
    // This method will:
    // 1. Find the user by username inside Firebase
    // 2. Locate today's date key (yyyy-MM-dd)
    // 3. Load the list of water slots (0 = total, 1–12 = cups)
    // 4. Insert the new water amount in the first free slot
    // 5. Update the total (index 0)
    // 6. Save the list back to Firebase
    // ============================================================================
    public CompletableFuture<Boolean> updateWater(String username, int waterAmount) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Step 1: Query users where userName == username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            // User not found
                            future.complete(false);
                            return;
                        }

                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            // Step 2: Create today key
                            String dayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                            // Step 3: Reference to today's water log
                            DatabaseReference todayRef = userSnap.getRef().child("waterLog").child(dayKey);

                            // Step 4: Load data for today
                            todayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot daySnapshot) {
                                    // Load existing list or create new 13-slot array
                                    List<Long> dayList = daySnapshot.exists()
                                            ? (List<Long>) daySnapshot.getValue()
                                            : new ArrayList<>(Collections.nCopies(13, 0L));

                                    // Step 5: Find next free slot
                                    int cupIndex = 1;
                                    while (cupIndex <= 12 && dayList.get(cupIndex) > 0) {
                                        cupIndex++;
                                    }

                                    if (cupIndex <= 12) {
                                        // Insert new water amount
                                        dayList.set(cupIndex, (long) waterAmount);

                                        // Update total at index 0
                                        long sum = dayList.get(0);
                                        dayList.set(0, sum + waterAmount);

                                        // Step 6: Save back to Firebase
                                        todayRef.setValue(dayList, (error, ref) -> {
                                            if (error == null) {
                                                future.complete(true);
                                            } else {
                                                future.complete(false);
                                            }
                                        });
                                    } else {
                                        // No free slots today
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

    // ============================================================================
    // Get water log for today and yesterday
    // ============================================================================
    // This method will:
    // 1. Find the user by username
    // 2. Compute today and yesterday keys (yyyy-MM-dd)
    // 3. Read total water at index 0 for both days
    // 4. Return JSON with { totalWater, yesterdayWater }
    // ============================================================================
    public CompletableFuture<JSONObject> getWater(String username) {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();

        // Step 1: Create today + yesterday keys
        String todayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        String yesterdayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());

        // Step 2: Query user by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            future.complete(null);
                            return;
                        }

                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            // Step 3: Extract totals from Firebase
                            Long todayAmount = userSnap.child("waterLog").child(todayKey).child("0").getValue(Long.class);
                            Long yesterdayAmount = userSnap.child("waterLog").child(yesterdayKey).child("0").getValue(Long.class);

                            // Step 4: Build JSON response
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("totalWater", todayAmount != null ? todayAmount : 0);
                                obj.put("yesterdayWater", yesterdayAmount != null ? yesterdayAmount : 0);
                            } catch (Exception e) {
                                future.complete(null);
                                return;
                            }

                            // Complete with JSON object
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
