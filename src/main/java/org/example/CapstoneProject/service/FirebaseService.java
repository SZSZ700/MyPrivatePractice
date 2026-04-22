// Define the package for this service class
package org.example.CapstoneProject.service;
// Import Firebase async result wrapper.
import com.google.api.core.ApiFuture;
// Import Google service account credentials support.
import com.google.auth.oauth2.GoogleCredentials;
// Import the Firebase app bootstrap class.
import com.google.firebase.FirebaseApp;
// Import Firebase app configuration builder.
import com.google.firebase.FirebaseOptions;
// Import Realtime Database types used throughout the service.
import com.google.firebase.database.*;
// Import environment-based Firebase URL configuration.
import org.example.CapstoneProject.EnvConfiguration.EnvConfig;
// Import the app's user model.
import org.example.CapstoneProject.model.User;
// Import JSON object support for API responses.
import org.json.JSONObject;
// Mark this class as a Spring service bean.
import org.springframework.stereotype.Service;
// Import checked exception for Firebase initialization.
import java.io.IOException;
// Import stream support for reading the service account file.
import java.io.InputStream;
// Import date formatting for daily water keys.
import java.text.SimpleDateFormat;
// Import Java collections and date utilities.
import java.util.*;
// Import CompletableFuture for async method results.
import java.util.concurrent.CompletableFuture;

@Service
public class FirebaseService {
    // NOTE:
    // This DatabaseReference instance is shared across all requests and threads.
    // Firebase's SDK is thread-safe, and each call (addListenerForSingleValueEvent,
    // updateChildren, etc.) registers its own async operation internally.
    // Multiple HTTP requests can hit FirebaseService methods in parallel without
    // "waiting in line" on this reference. The real concurrency is handled
    // inside the Firebase SDK and the network layer, not by this Java object.
    private final DatabaseReference usersRef;
    // Keep a separate index from username -> user key for fast lookups.
    private final DatabaseReference usernamesRef;

    // Query the users collection by the embedded "userName" field.
    private CompletableFuture<DataSnapshot> queryByUsername(String username) {
        var future = new CompletableFuture<DataSnapshot>();

        // Read matching users once and complete the future with the result snapshot.
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Resolve with the raw query snapshot for downstream processing.
                        future.complete(snapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Surface Firebase read failures to the caller.
                        future.completeExceptionally(error.toException());
                    }
                });
        return future;
    }

    // Fall back to scanning the user records when the username index is missing or stale.
    private CompletableFuture<DataSnapshot> scanFirstUserByUsername(String username) {
        var future = new CompletableFuture<DataSnapshot>();
        // Reuse the username query and extract only the first matching child.
        queryByUsername(username).whenComplete((snapshot, ex) -> {
            if (ex != null) {
                future.completeExceptionally(ex);
                return;
            }

            // Collapse the query result to a single user snapshot.
            var first = firstChildOrNull(snapshot);

            if (first != null) {
                // Best-effort index healing when found by scan.
                reserveUsername(username, first.getKey());
            }

            future.complete(first);
        });
        return future;
    }

    // Resolve a user snapshot through the username index, with scan-based recovery if needed.
    private CompletableFuture<DataSnapshot> findUserSnapshotByUsername(String username) {
        var future = new CompletableFuture<DataSnapshot>();

        // Reject blank usernames early to avoid unnecessary database reads.
        if (username == null || username.isBlank()) {
            future.complete(null);
            return future;
        }

        // Read the username index entry first to locate the actual user node.
        usernamesRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot usernameIndexSnap) {
                // Extract the user key stored in the username index.
                var userKey = usernameIndexSnap.getValue(String.class);

                if (userKey == null || userKey.isBlank()) {
                    // Fall back to a full scan when the index has no usable entry.
                    scanFirstUserByUsername(username).whenComplete((scanSnap, scanEx) -> {
                        if (scanEx != null) {
                            future.completeExceptionally(scanEx);
                            return;
                        }
                        future.complete(scanSnap);
                    });
                    return;
                }

                // Fetch the user node pointed to by the index entry.
                usersRef.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot userSnap) {
                        // Verify the indexed user still exists and still owns this username.
                        var storedUsername = userSnap.child("userName").getValue(String.class);

                        if (!userSnap.exists() || !username.equals(storedUsername)) {
                            // Clean up the stale index entry before retrying by scan.
                            clearUsernameIndexIfMatches(username, userKey).whenComplete((ignored, clearEx) -> {
                                if (clearEx != null) {
                                    future.completeExceptionally(clearEx);
                                    return;
                                }
                                scanFirstUserByUsername(username).whenComplete((scanSnap, scanEx) -> {
                                    if (scanEx != null) {
                                        future.completeExceptionally(scanEx);
                                        return;
                                    }
                                    future.complete(scanSnap);
                                });
                            });
                            return;
                        }
                        // Return the validated user snapshot.
                        future.complete(userSnap);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Propagate failures while loading the indexed user.
                        future.completeExceptionally(error.toException());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Propagate failures while loading the username index entry.
                future.completeExceptionally(error.toException());
            }
        });
        return future;
    }

    // Return the first child in a snapshot, or null when the snapshot is empty.
    private DataSnapshot firstChildOrNull(DataSnapshot snapshot) {
        for (var child : snapshot.getChildren()) {
            return child;
        }

        return null;
    }

    // Convert a Firebase ApiFuture into the service's Boolean completion pattern.
    private void completeBooleanFromApiFuture(ApiFuture<?> op, CompletableFuture<Boolean> target) {
        // Finish with true on success and false on any Firebase write failure.
        op.addListener(() -> {
            try {
                op.get();
                target.complete(true);
            } catch (Exception e) {
                target.complete(false);
            }
        }, Runnable::run);
    }

    // Reserve a username atomically so two users cannot claim it at the same time.
    private CompletableFuture<Boolean> reserveUsername(String username, String userKey) {
        var future = new CompletableFuture<Boolean>();
        // Use a transaction to avoid race conditions on the username index.
        usernamesRef.child(username).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                // Read the currently reserved user key, if any.
                var existing = currentData.getValue(String.class);

                if (existing == null || existing.isBlank()) {
                    // Claim the username when it is free.
                    currentData.setValue(userKey);
                    return Transaction.success(currentData);
                }

                if (userKey.equals(existing)) {
                    // Treat repeated reservation by the same user as success.
                    return Transaction.success(currentData);
                }

                // Abort when another user already owns this username.
                return Transaction.abort();
            }

            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                if (error != null) {
                    // Fail the future when the transaction itself errors.
                    future.completeExceptionally(error.toException());
                    return;
                }
                // committed=false means the username was already taken.
                future.complete(committed);
            }
        });
        return future;
    }

    // Release a username reservation after a failed user creation attempt.
    private void releaseUsernameReservation(String username, String userKey) {
        // Clear the index entry only if it still points to this user key.
        usernamesRef.child(username).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                // Read the current reservation target.
                var existing = currentData.getValue(String.class);
                if (userKey.equals(existing)) {
                    // Remove the reservation owned by this user key.
                    currentData.setValue(null);
                    return Transaction.success(currentData);
                }
                // Abort when the index has already changed.
                return Transaction.abort();
            }

            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                // Best-effort cleanup.
            }
        });
    }

    // Remove a stale username index entry only when it still points at the expected user key.
    private CompletableFuture<Boolean> clearUsernameIndexIfMatches(String username, String userKey) {
        var future = new CompletableFuture<Boolean>();
        // Empty usernames have nothing to clean up.
        if (username == null || username.isBlank()) {
            future.complete(true);
            return future;
        }
        // Use a transaction so the cleanup does not race with a new reservation.
        usernamesRef.child(username).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                // Read the current indexed user key.
                var existing = currentData.getValue(String.class);
                if (userKey.equals(existing)) {
                    // Remove the entry only when it still matches the stale key.
                    currentData.setValue(null);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                if (error != null) {
                    // Report transaction failures back to the caller.
                    future.completeExceptionally(error.toException());
                    return;
                }
                // Cleanup is treated as successful even when nothing changed.
                future.complete(true);
            }
        });
        return future;
    }

    // ----------------------------- INIT ---------------------------------
    // Initializes Firebase Admin SDK (only once) and binds usersRef to "/Users"
    public FirebaseService() throws IOException {
        // Load the service account JSON from resources
        InputStream serviceAccount = getClass().getResourceAsStream("/myfinaltopap-firebase-adminsdk-fbsvc-765944770e.json");
        if (serviceAccount == null) {
            // Throw error if JSON file is missing
            throw new IllegalStateException("Service account JSON not found!");
        }

        // load the address of the real-time database form the .env
        String firebaseUrl = EnvConfig.getFirebaseUrl();

        // Build Firebase options using the service account and database URL
        FirebaseOptions options = FirebaseOptions.builder()
                // Sets the GoogleCredentials to use to authenticate the SDK.
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                // Sets the Realtime Database URL to use for data storage.
                .setDatabaseUrl(firebaseUrl)
                // Builds the FirebaseOptions instance from the previously set options.
                .build();

        // Initialize FirebaseApp only if it has not been initialized before
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            System.out.println("✅ Connected to Firebase project: myfinaltopap");
        }

        // Bind usersRef to the "Users" node in the database
        this.usersRef = FirebaseDatabase.getInstance().getReference("Users");
        this.usernamesRef = FirebaseDatabase.getInstance().getReference("Usernames");
    }

    // =========================================================
    // SIGNUP → creates a new user with auto-generated key
    // With FULL DEBUG prints
    // =========================================================
    public CompletableFuture<String> signup(User user) {
        var future = new CompletableFuture<String>();
        // Safely extract the requested username from the incoming user object.
        var username = user != null ? user.getUserName() : null;

        if (username == null || username.isBlank()) {
            future.complete("Error: invalid username");
            return future;
        }

        // Generate a new Firebase key for the user record.
        var key = usersRef.push().getKey();
        if (key == null) {
            future.complete("Error generating key");
            return future;
        }

        // Reserve the username before writing the user record itself.
        reserveUsername(username, key).whenComplete((reserved, ex) -> {
            if (ex != null) {
                future.complete("Error: " + ex.getMessage());
                return;
            }

            if (!reserved) {
                future.complete("Username already exists");
                return;
            }

            //noinspection unused
            // Persist the user under the generated key.
            //noinspection unused
            usersRef.child(key).setValue(user, (error, ref) -> {
                if (error == null) {
                    future.complete("User created successfully");
                } else {
                    // Release the username if the actual user write fails.
                    releaseUsernameReservation(username, key);
                    future.complete("Error: " + error.getMessage());
                }
            });
        });

        return future;
    }

    // =========================================================
    // LOGIN → check username + password against stored users
    // =========================================================
    public CompletableFuture<User> login(String username, String password) {
        CompletableFuture<User> future = new CompletableFuture<>();
        // Load the user snapshot for the supplied username.
        findUserSnapshotByUsername(username).whenComplete((userSnap, ex) -> {
            if (ex != null) {
                future.completeExceptionally(ex);
                return;
            }
            if (userSnap == null || !userSnap.exists()) {
                future.complete(null);
                return;
            }
            // Compare the stored password to the one supplied by the caller.
            var existingPass = userSnap.child("password").getValue(String.class);
            if (existingPass != null && existingPass.equals(password)) {
                // Return the full user object on successful authentication.
                future.complete(userSnap.getValue(User.class));
            } else {
                // Hide auth failure details by returning null.
                future.complete(null);
            }
        });

        return future;
    }

    // ---------------------------- CREATE USER ---------------------------
    // Creates a new user in Firebase if the username does not already exist.
    // Returns a CompletableFuture<Boolean> where true = user created, false = user already exists.
    public CompletableFuture<Boolean> createUser(User user) {
        var future = new CompletableFuture<Boolean>();
        // Extract and validate the target username before allocating Firebase state.
        var username = user != null ? user.getUserName() : null;
        if (username == null || username.isBlank()) {
            future.complete(false);
            return future;
        }

        // Generate the storage key for the new user record.
        var key = usersRef.push().getKey();
        if (key == null) {
            future.complete(false);
            return future;
        }

        // Reserve the username before creating the user document.
        reserveUsername(username, key).whenComplete((reserved, ex) -> {
            if (ex != null) {
                future.completeExceptionally(ex);
                return;
            }
            if (!reserved) {
                future.complete(false);
                return;
            }
            // Write the user object asynchronously after the reservation succeeds.
            var writeFuture = usersRef.child(key).setValueAsync(user);
            writeFuture.addListener(() -> {
                try {
                    writeFuture.get();
                    future.complete(true);
                } catch (Exception e) {
                    // Roll back the reservation if the user write fails.
                    releaseUsernameReservation(username, key);
                    future.complete(false);
                }
            }, Runnable::run);
        });

        return future;
    }

    // ------------------------------ READ ALL ----------------------------
    // Reads all users from Firebase and returns them as a List<User>.
    public CompletableFuture<List<User>> getAllUsers() {
        var future = new CompletableFuture<List<User>>();

        // Fetch all user nodes once
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<User> users = new ArrayList<>();

                // Convert each snapshot to User object and add to list
                for (var child : snapshot.getChildren()) {
                    var user = child.getValue(User.class);
                    if (user != null) users.add(user);
                }

                // Complete with the list of users
                future.complete(users);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Complete with exception if query fails
                future.completeExceptionally(error.toException());
            }
        });

        return future;
    }

    // ------------------------------ READ ONE ----------------------------
    // Reads a single user by username from Firebase.
    // Returns a CompletableFuture<User> or null if user not found.
    public CompletableFuture<User> getUser(String username) {
        var future = new CompletableFuture<User>();

        // Resolve the user snapshot and deserialize it if present.
        findUserSnapshotByUsername(username).whenComplete((userSnap, ex) -> {
            if (ex != null) {
                future.completeExceptionally(ex);
                return;
            }
            future.complete(userSnap != null ? userSnap.getValue(User.class) : null);
        });

        return future;
    }

    // ------------------------------ UPDATE FULL -------------------------
    // Replaces a user record completely with an updated User object.
    // Returns a CompletableFuture<Boolean> indicating success or failure.
    public CompletableFuture<Boolean> updateUser(String username, User updatedUser) {
        var future = new CompletableFuture<Boolean>();

        // Load the existing user so we can update the correct Firebase path.
        findUserSnapshotByUsername(username).whenComplete((userSnap, ex) -> {
            if (ex != null) {
                future.completeExceptionally(ex);
                return;
            }
            if (userSnap == null || !userSnap.exists()) {
                future.complete(false);
                return;
            }
            // Keep path identity stable to avoid username index drift.
            updatedUser.setUserName(username);
            // Replace the entire stored user object in one write.
            completeBooleanFromApiFuture(userSnap.getRef().setValueAsync(updatedUser), future);
        });

        return future;
    }

    // ------------------------------ PATCH (PARTIAL) ----------------------
    // Updates selected fields (partial update) of a user in Firebase.
    // Returns a CompletableFuture<User> with the updated User object.
    public CompletableFuture<User> patchUser(String username, Map<String, Object> updates) {
        // Future that will eventually hold the updated User object
        var future = new CompletableFuture<User>();

        // Query Firebase for the given username
        findUserSnapshotByUsername(username).whenComplete((userSnap, ex) -> {
            if (ex != null) {
                future.completeExceptionally(ex);
                return;
            }
            // If user not found, complete with null
            if (userSnap == null || !userSnap.exists()) {
                future.complete(null);
                return;
            }
            // Hold a reference to the existing user node for patch and refresh operations.
            var ref = userSnap.getRef();

            // Copy incoming updates so the method can safely filter unsupported fields.
            var safeUpdates = new HashMap<>(updates);

            // username changes require index migration; block here for consistency.
            safeUpdates.remove("userName");
            if (safeUpdates.isEmpty()) {
                // No writable fields remain, so just return the current user state.
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot refreshed) {
                        future.complete(refreshed.getValue(User.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });
                return;
            }
            // Apply partial updates asynchronously
            var patchFuture = ref.updateChildrenAsync(safeUpdates);
            patchFuture.addListener(() -> {
                try {
                    patchFuture.get();
                } catch (Exception e) {
                    // Fail fast when Firebase rejects the partial update.
                    future.completeExceptionally(e);
                    return;
                }
                // After successful update, re-read snapshot to return fresh data
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot refreshed) {
                        future.complete(refreshed.getValue(User.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });
            }, Runnable::run);
        });

        return future;
    }

    // ------------------------------ DELETE ------------------------------
    // Deletes a user from Firebase by username
    // Returns a CompletableFuture<Boolean> indicating success or failure
    public CompletableFuture<Boolean> deleteUser(String username) {
        // Create future to return the result
        var future = new CompletableFuture<Boolean>();

        // Query Firebase by username
        findUserSnapshotByUsername(username).whenComplete((userSnap, ex) -> {
            if (ex != null) {
                future.completeExceptionally(ex);
                return;
            }
            if (userSnap == null || !userSnap.exists()) {
                future.complete(false);
                return;
            }
            // Capture identifiers needed to clear the username index after deletion.
            var userKey = userSnap.getKey();
            var storedUsername = userSnap.child("userName").getValue(String.class);
            // Remove the main user node first.
            var deleteFuture = userSnap.getRef().removeValueAsync();
            deleteFuture.addListener(() -> {
                try {
                    deleteFuture.get();
                } catch (Exception e) {
                    future.complete(false);
                    return;
                }
                // Clear the username index if it still points at the deleted record.
                clearUsernameIndexIfMatches(storedUsername, userKey)
                        .whenComplete((ignored, clearEx) -> {
                            if (clearEx != null) {
                                future.completeExceptionally(clearEx);
                                return;
                            }
                            future.complete(true);
                        });
            }, Runnable::run);
        });

        return future;
    }

    // ------------------------------ EXISTS ------------------------------
    // Checks if a user exists in Firebase by username
    // Returns a CompletableFuture<Boolean> indicating whether user exists
    public CompletableFuture<Boolean> exists(String username) {
        var future = new CompletableFuture<Boolean>();

        // Query Firebase by username
        findUserSnapshotByUsername(username).whenComplete((userSnap, ex) -> {
            if (ex != null) {
                future.completeExceptionally(ex);
                return;
            }
            future.complete(userSnap != null && userSnap.exists());
        });

        return future;
    }

    // ------------------------------ UPDATE BMI --------------------------
    // Updates the BMI value of a user in Firebase
    // Returns a CompletableFuture<Boolean> indicating success or failure
    public CompletableFuture<Boolean> updateBmi(String username, double bmi) {
        var future = new CompletableFuture<Boolean>();

        // Query Firebase by username
        findUserSnapshotByUsername(username).whenComplete((userSnap, ex) -> {
            if (ex != null) {
                future.completeExceptionally(ex);
                return;
            }
            if (userSnap == null || !userSnap.exists()) {
                future.complete(false);
                return;
            }
            // Update only the BMI field on the located user node.
            completeBooleanFromApiFuture(userSnap.getRef().child("bmi").setValueAsync(bmi), future);
        });

        return future;
    }

    // --------------------------------------------------------------
    // UPDATE WATER (no 12-cup limit, dynamic list)
    // - Keeps index 0 as the daily total (sum in ml)
    // - Appends each new drink amount to the end of the list (index 1..N)
    // - Uses a Firebase Realtime Database Transaction for atomic updates
    // --------------------------------------------------------------
    public CompletableFuture<Boolean> updateWater(String username, int waterAmount) {
        var future = new CompletableFuture<Boolean>();

        // Reject empty or negative drink entries before hitting Firebase.
        if (waterAmount <= 0) {
            future.complete(false);
            return future;
        }

        // Load the target user so we can update today's water log.
        findUserSnapshotByUsername(username).whenComplete((userSnap, ex) -> {
            if (ex != null) {
                System.err.println("ERROR updateWater -> " + ex.getMessage());
                future.completeExceptionally(ex);
                return;
            }
            if (userSnap == null || !userSnap.exists()) {
                future.complete(false);
                return;
            }

            // Build the per-day log key using the device locale date.
            var dayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            // Point to the list that stores today's cumulative total and individual drinks.
            var todayRef = userSnap.getRef().child("waterLog").child(dayKey);

            // Use a transaction so concurrent drink updates do not overwrite each other.
            todayRef.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData currentData) {
                    // Read the existing daily list where slot 0 is the running total.
                    List<Long> dayList = currentData.getValue(new GenericTypeIndicator<>() {
                    });
                    if (dayList == null) {
                        // Start a new day with an empty list when no entry exists yet.
                        dayList = new ArrayList<>();
                    }
                    if (dayList.isEmpty()) {
                        // Initialize slot 0 to hold the total consumed today.
                        dayList.add(0L);
                    } else if (dayList.getFirst() == null) {
                        // Repair malformed data where the total slot exists but is null.
                        dayList.set(0, 0L);
                    }

                    // Add the new drink both to the running total and the event list.
                    @SuppressWarnings("DataFlowIssue") long currentSum = dayList.getFirst();
                    dayList.set(0, currentSum + waterAmount);
                    dayList.add((long) waterAmount);
                    currentData.setValue(dayList);
                    return Transaction.success(currentData);
                }

                @Override
                public void onComplete(DatabaseError error, boolean committed, DataSnapshot snapshot) {
                    if (error != null || !committed) {
                        future.complete(false);
                        return;
                    }
                    // Signal success only after Firebase commits the transaction.
                    future.complete(true);
                }
            });
        });

        return future;
    }

    // ------------------------------- GET WATER --------------------------
    // Returns a JSON object in the format {"todayWater": <ml>, "yesterdayWater": <ml>}
    // This format is required to match what the Android client expects.
    public CompletableFuture<JSONObject> getWater(String username) {
        var future = new CompletableFuture<JSONObject>();

        // Compute the date key for today's water log.
        var todayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        // Compute the date key for yesterday's water log.
        var cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        var yesterdayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());

        // Load the user so both water totals can be read from a single snapshot.
        findUserSnapshotByUsername(username).whenComplete((userSnap, ex) -> {
            if (ex != null) {
                future.completeExceptionally(ex);
                return;
            }
            if (userSnap == null || !userSnap.exists()) {
                future.complete(null);
                return;
            }

            // Read slot 0 from each day, which stores the total water for that date.
            var todayAmt = userSnap.child("waterLog").child(todayKey).child("0").getValue(Long.class);
            var yesterdayAmt = userSnap.child("waterLog").child(yesterdayKey).child("0").getValue(Long.class);

            // Build the response object expected by the Android client.
            var obj = new JSONObject();
            try {
                obj.put("todayWater", todayAmt == null ? 0 : todayAmt);
                obj.put("yesterdayWater", yesterdayAmt == null ? 0 : yesterdayAmt);
            } catch (Exception e) {
                future.complete(null);
                return;
            }
            future.complete(obj);
        });

        return future;
    }

    // ------------------------------- GET WATER HISTORY MAP --------------------------
    // Returns {"2025-09-29": 4600, "2025-09-28": 0, ...} for last N days
    public CompletableFuture<Map<String, Long>> getWaterHistoryMap(String username, int days) {
        var future = new CompletableFuture<Map<String, Long>>();

        // Precompute the date keys to read, starting from today and moving backward.
        List<String> keys = new ArrayList<>();
        var sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        var cal = Calendar.getInstance();
        for (var i = 0; i < days; i++) {
            keys.add(sdf.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }

        findUserSnapshotByUsername(username).whenComplete((userSnap, ex) -> {
            if (ex != null) {
                future.completeExceptionally(ex);
                return;
            }
            if (userSnap == null || !userSnap.exists()) {
                future.complete(null);
                return;
            }

            // Preserve insertion order so the response follows the generated key list.
            Map<String, Long> result = new LinkedHashMap<>();
            try {
                for (var key : keys) {
                    // Read the stored daily total from slot 0 for each requested day.
                    var amt = userSnap.child("waterLog").child(key).child("0").getValue(Long.class);
                    result.put(key, amt == null ? 0 : amt);
                }
                future.complete(result);
            } catch (Exception e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
                future.complete(null);
            }
        });

        return future;
    }

    // ------------------------------- GET WEEKLY AVERAGES (4 WEEKS) --------------------------
    // Reads slot "0" (daily sum) for the last 28 days, groups by week (7-day chunks),
    // and returns a LinkedHashMap in this order: Week 1 (oldest) .. Week 4 (newest).
    public CompletableFuture<Map<String, Integer>> getWeeklyAverages(String username) {
        var future = new CompletableFuture<Map<String, Integer>>();

        // Build the last 28 day keys so they can be grouped into four 7-day buckets.
        var sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        var cal = Calendar.getInstance();
        List<String> last28 = new ArrayList<>(28);
        for (var i = 0; i < 28; i++) {
            last28.add(sdf.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }

        findUserSnapshotByUsername(username).whenComplete((userSnap, ex) -> {
            if (ex != null) {
                future.completeExceptionally(ex);
                return;
            }
            if (userSnap == null || !userSnap.exists()) {
                future.complete(Collections.emptyMap());
                return;
            }

            try {
                // Track the total and populated-day count for each week window.
                var sums = new long[4];
                var counts = new int[4];

                for (var i = 0; i < 28; i++) {
                    var dateKey = last28.get(i);
                    // Days 0-6 map to bucket 0, 7-13 to bucket 1, and so on.
                    var weekIdx = i / 7;
                    var amt = userSnap.child("waterLog").child(dateKey).child("0").getValue(Long.class);
                    if (amt != null) {
                        sums[weekIdx] += amt;
                        counts[weekIdx] += 1;
                    }
                }

                // Return the averages oldest-to-newest in a stable response order.
                Map<String, Integer> out = new LinkedHashMap<>();
                for (var w = 0; w < 4; w++) {
                    var avg = (counts[w] > 0) ? (int) (sums[w] / counts[w]) : 0;
                    out.put("Week " + (4 - w), avg);
                }
                future.complete(out);
            } catch (Exception e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
                future.complete(Collections.emptyMap());
            }
        });

        return future;
    }

    // get Daily drink goal
    public CompletableFuture<Integer> getGoalMl(String username) {
        var fut = new CompletableFuture<Integer>();

        // Read the user so the stored goal can be returned or defaulted.
        findUserSnapshotByUsername(username).whenComplete((userSnap, ex) -> {
            if (ex != null) {
                fut.completeExceptionally(ex);
                return;
            }
            if (userSnap == null || !userSnap.exists()) {
                fut.complete(3000);
                return;
            }

            // Fall back to 3000 ml when the goal field is missing.
            var goal = userSnap.child("goalMl").getValue(Integer.class);
            fut.complete(goal != null ? goal : 3000);
        });

        return fut;
    }

    // update Daily drink goal
    public CompletableFuture<Boolean> updateGoalMl(String username, int goalMl) {
        var fut = new CompletableFuture<Boolean>();

        // Keep goal values within a reasonable application-defined range.
        if (goalMl < 500 || goalMl > 10000) {
            fut.complete(false);
            return fut;
        }

        // Load the target user before writing the new goal.
        findUserSnapshotByUsername(username).whenComplete((userSnap, ex) -> {
            if (ex != null) {
                fut.completeExceptionally(ex);
                return;
            }
            if (userSnap == null || !userSnap.exists()) {
                fut.complete(false);
                return;
            }

            //noinspection unused
            // Persist the updated daily water goal on the user record.
            //noinspection unused
            userSnap.getRef().child("goalMl").setValue(goalMl, (err, ref) -> {
                if (err != null) {
                    fut.complete(false);
                } else {
                    fut.complete(true);
                }
            });
        });

        return fut;
    }

    // ------------------------------ BMI DISTRIBUTION (GLOBAL) --------------------------
    // Calculates how many users are in each BMI category:
    // Underweight (<18.5), Normal (18.5–24.9), Overweight (25–29.9), Obese (>=30).
    // Returns a CompletableFuture<Map<String, Integer>> like:
    // {"Underweight": 3, "Normal": 12, "Overweight": 5, "Obese": 2}
    public CompletableFuture<Map<String, Integer>> getBmiDistribution() {
        // Future that will hold the final distribution map
        var future = new CompletableFuture<Map<String, Integer>>();

        // Read all users once from Firebase
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Counters for each BMI category
                var underweight = 0; // BMI < 18.5
                var normal = 0;      // 18.5 <= BMI < 25
                var overweight = 0;  // 25   <= BMI < 30
                var obese = 0;       // BMI >= 30

                // Iterate over all user nodes
                for (var child : snapshot.getChildren()) {
                    // Read "bmi" field as Double
                    var bmi = child.child("bmi").getValue(Double.class);
                    // If no BMI recorded for this user → skip (not counted)
                    if (bmi == null) { continue; }
                    @SuppressWarnings("UnnecessaryUnboxing") var value = bmi.doubleValue();
                    // Classify into category
                    if (value < 18.5) { underweight++; }
                    else if (value < 25.0) { normal++; }
                    else if (value < 30.0) { overweight++; }
                    else { obese++; }
                }

                // Use LinkedHashMap to preserve insertion order
                Map<String, Integer> distribution = new LinkedHashMap<>();
                distribution.put("Underweight", underweight);
                distribution.put("Normal", normal);
                distribution.put("Overweight", overweight);
                distribution.put("Obese", obese);

                // Complete future with the calculated map
                future.complete(distribution);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // If query fails, complete with exception
                future.completeExceptionally(error.toException());
            }
        });

        // Return future immediately (will be completed asynchronously)
        return future;
    }

    // ------------------------------ GET CALORIES ------------------------------
    // Returns the current calories field for a user.
    // If user not found or field missing → returns 0.
    public CompletableFuture<Integer> getCalories(String username) {
        var fut = new CompletableFuture<Integer>();

        // Read the user once and return the stored calories total if available.
        findUserSnapshotByUsername(username).whenComplete((userSnap, ex) -> {
            if (ex != null) {
                fut.completeExceptionally(ex);
                return;
            }
            if (userSnap == null || !userSnap.exists()) {
                fut.complete(0);
                return;
            }

            // Default to 0 when no calories value has been saved yet.
            var cals = userSnap.child("calories").getValue(Integer.class);
            fut.complete(cals != null ? cals : 0);
        });

        return fut;
    }

    // ------------------------------ UPDATE CALORIES ---------------------------
    // Updates the "calories" field for a user.
    // Returns true if updated, false if user not found or invalid input.
    public CompletableFuture<Boolean> updateCalories(String username, int calories) {
        var fut = new CompletableFuture<Boolean>();

        // Reject clearly invalid calorie totals before attempting a write.
        if (calories < 0 || calories > 20000) {
            fut.complete(false);
            return fut;
        }

        // Load the user first so we update the correct Firebase node.
        findUserSnapshotByUsername(username).whenComplete((userSnap, ex) -> {
            if (ex != null) {
                fut.completeExceptionally(ex);
                return;
            }
            if (userSnap == null || !userSnap.exists()) {
                fut.complete(false);
                return;
            }

            //noinspection unused
            // Persist the new calories value on the user record.
            //noinspection unused
            userSnap.getRef().child("calories").setValue(calories, (err, ref) -> {
                if (err != null) {
                    fut.complete(false);
                } else {
                    fut.complete(true);
                }
            });
        });

        return fut;
    }
}
