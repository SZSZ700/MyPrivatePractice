// Define the package for this service class
package org.example.CapstoneProject.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.example.EnvConfig;
import org.example.model.User;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class FirebaseService {

    // Reference to /Users root
    // NOTE:
    // This DatabaseReference instance is shared across all requests and threads.
    // Firebase's SDK is thread-safe, and each call (addListenerForSingleValueEvent,
    // updateChildren, etc.) registers its own async operation internally.
    // Multiple HTTP requests can hit FirebaseService methods in parallel without
    // "waiting in line" on this reference. The real concurrency is handled
    // inside the Firebase SDK and the network layer, not by this Java object.
    private final DatabaseReference usersRef;

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
                // This parameter must be specified when creating a new instance of FirebaseOptions.
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
    }

    // =========================================================
    // SIGNUP → creates a new user with auto-generated key
    // With FULL DEBUG prints
    // =========================================================
    public CompletableFuture<String> signup(User user) {
        CompletableFuture<String> future = new CompletableFuture<>();

        // Debug log: signup request received
        System.out.println("DEBUG: Signup called for username = " + user.getUserName());

        // Debug log: Firebase reference details
        System.out.println("DEBUG: usersRef PATH = " + usersRef.getPath());
        System.out.println("DEBUG: usersRef URL  = " + usersRef.toString());

        // Read all users once
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Debug log: number of existing users
                System.out.println("DEBUG: Checking existing users... total children = " + snapshot.getChildrenCount());

                // Iterate through all existing users
                for (DataSnapshot child : snapshot.getChildren()) {
                    String existingUser = child.child("userName").getValue(String.class);

                    System.out.println("DEBUG: Found user in DB = " + existingUser);

                    // If username already exists → return error
                    if (existingUser != null && existingUser.equals(user.getUserName())) {
                        System.out.println("DEBUG: Username already exists → " + existingUser);
                        future.complete("Username already exists");
                        return;
                    }
                }

                // If no duplicate username, create a new record
                String key = usersRef.push().getKey();
                if (key == null) {
                    System.err.println("DEBUG: Firebase push() returned null key!");
                    future.complete("Error generating key");
                    return;
                }

                // Debug log: new key and user object
                System.out.println("DEBUG: Creating new user with key = " + key);
                System.out.println("DEBUG: User object to save = " + user);

                // Save new user under the generated key
                usersRef.child(key).setValue(user, (error, ref) -> {
                    if (error == null) {
                        System.out.println("DEBUG: Successfully saved user at " + ref.toString());
                        future.complete("User created successfully");
                    } else {
                        System.err.println("DEBUG: Failed to save user. Error = " + error.getMessage());
                        future.complete("Error: " + error.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle Firebase cancellation error
                System.err.println("DEBUG: Firebase read cancelled. Error = " + error.getMessage());
                future.complete("Error: " + error.getMessage());
            }
        });

        return future;
    }

    // =========================================================
    // LOGIN → check username + password against stored users
    // =========================================================
    public CompletableFuture<User> login(String username, String password) {
        CompletableFuture<User> future = new CompletableFuture<>();

        // Read all users once
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User foundUser = null;

                // Iterate through all users
                for (DataSnapshot child : snapshot.getChildren()) {
                    String existingUser = child.child("userName").getValue(String.class);
                    String existingPass = child.child("password").getValue(String.class);

                    // Check if both username and password match
                    if (existingUser != null && existingPass != null &&
                            existingUser.equals(username) &&
                            existingPass.equals(password)) {
                        // If match found, retrieve full User object
                        foundUser = child.getValue(User.class);
                        break;
                    }
                }

                // Complete with found user or null if not found
                future.complete(foundUser);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // On error, complete with null
                future.complete(null);
            }
        });

        return future;
    }

    // ---------------------------- CREATE USER ---------------------------
    // Creates a new user in Firebase if the username does not already exist.
    // Returns a CompletableFuture<Boolean> where true = user created, false = user already exists.
    public CompletableFuture<Boolean> createUser(User user) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Query by username to check if user already exists
        usersRef.orderByChild("userName").equalTo(user.getUserName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // If user already exists, return false
                        if (snapshot.exists()) {
                            future.complete(false);
                            return;
                        }
                        // If not, push a new user object into Firebase
                        usersRef.push().setValueAsync(user)
                                .addListener(() -> future.complete(true), Runnable::run);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with exception if query fails
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ------------------------------ READ ALL ----------------------------
    // Reads all users from Firebase and returns them as a List<User>.
    public CompletableFuture<List<User>> getAllUsers() {
        CompletableFuture<List<User>> future = new CompletableFuture<>();

        // Fetch all user nodes once
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<User> users = new ArrayList<>();
                // Convert each snapshot to User object and add to list
                for (DataSnapshot child : snapshot.getChildren()) {
                    User user = child.getValue(User.class);
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
        CompletableFuture<User> future = new CompletableFuture<>();

        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Iterate through matching user snapshots
                        for (DataSnapshot child : snapshot.getChildren()) {
                            User user = child.getValue(User.class);
                            if (user != null) {
                                future.complete(user);
                                return;
                            }
                        }
                        // Complete with null if no user found
                        future.complete(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with exception if query fails
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ------------------------------ UPDATE FULL -------------------------
    // Replaces a user record completely with an updated User object.
    // Returns a CompletableFuture<Boolean> indicating success or failure.
    public CompletableFuture<Boolean> updateUser(String username, User updatedUser) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // If user does not exist, return false
                        if (!snapshot.exists()) {
                            future.complete(false);
                            return;
                        }
                        // For each matching user (normally one)
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Replace existing data with the updated user object
                            child.getRef().setValueAsync(updatedUser)
                                    .addListener(() -> future.complete(true), Runnable::run);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with exception if query fails
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ------------------------------ PATCH (PARTIAL) ----------------------
    // Updates selected fields (partial update) of a user in Firebase.
    // Returns a CompletableFuture<User> with the updated User object.
    public CompletableFuture<User> patchUser(String username, Map<String, Object> updates) {
        // Future that will eventually hold the updated User object
        CompletableFuture<User> future = new CompletableFuture<>();

        // Query Firebase for the given username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // If user not found, complete with null
                        if (!snapshot.exists()) {
                            future.complete(null);
                            return;
                        }
                        // For each matching user (normally one)
                        for (DataSnapshot child : snapshot.getChildren()) {
                            DatabaseReference ref = child.getRef();
                            // Apply partial updates asynchronously
                            ref.updateChildrenAsync(updates).addListener(() -> {
                                // After update, re-read the snapshot to return fresh data
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot refreshed) {
                                        // Convert refreshed snapshot into User object
                                        User updated = refreshed.getValue(User.class);
                                        // Complete the future with updated User
                                        future.complete(updated);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        // Complete with exception if refresh query fails
                                        future.completeExceptionally(error.toException());
                                    }
                                });
                            }, Runnable::run);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with exception if query fails
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ------------------------------ DELETE ------------------------------
    // Deletes a user from Firebase by username
    // Returns a CompletableFuture<Boolean> indicating success or failure
    public CompletableFuture<Boolean> deleteUser(String username) {
        // Create future to return the result
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Query Firebase by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // If user does not exist, complete with false
                        if (!snapshot.exists()) {
                            future.complete(false);
                            return;
                        }
                        // For each matching user (usually one)
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Remove the user node asynchronously
                            child.getRef().removeValueAsync()
                                    .addListener(() -> future.complete(true), Runnable::run);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with exception if query fails
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ------------------------------ EXISTS ------------------------------
    // Checks if a user exists in Firebase by username
    // Returns a CompletableFuture<Boolean> indicating whether user exists
    public CompletableFuture<Boolean> exists(String username) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Query Firebase by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Complete with true if user exists, otherwise false
                        future.complete(snapshot.exists());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with exception if query fails
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ------------------------------ UPDATE BMI --------------------------
    // Updates the BMI value of a user in Firebase
    // Returns a CompletableFuture<Boolean> indicating success or failure
    public CompletableFuture<Boolean> updateBmi(String username, double bmi) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Query Firebase by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // If user not found, complete with false
                        if (!snapshot.exists()) {
                            future.complete(false);
                            return;
                        }
                        // For each matching user (normally one)
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Update the BMI field asynchronously
                            child.getRef().child("bmi").setValueAsync(bmi)
                                    .addListener(() -> future.complete(true), Runnable::run);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with exception if query fails
                        future.completeExceptionally(error.toException());
                    }
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
        // Future that will be completed once the async work finishes
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Optional guard: ignore invalid amounts (<= 0). You can remove this if you support negatives.
        if (waterAmount <= 0) {
            System.out.println("DEBUG updateWater -> ignored non-positive amount: " + waterAmount);
            future.complete(false);
            return future;
        }

        // Find user by userName
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // If user is not found, finish with false
                        if (!snapshot.exists()) {
                            System.out.println("DEBUG updateWater -> user not found: " + username);
                            future.complete(false);
                            return;
                        }

                        // We expect a single user, but loop just in case
                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            // Build today's date key: yyyy-MM-dd (matches your existing structure)
                            String dayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    .format(new Date());

                            // Reference to the "today" node (Users/<uid>/waterLog/<yyyy-MM-dd>)
                            DatabaseReference todayRef = userSnap.getRef()
                                    .child("waterLog")
                                    .child(dayKey);

                            System.out.println("DEBUG updateWater -> todayRef = " + todayRef);

                            // Run a transaction to update sum (index 0) and append the new cup atomically
                            todayRef.runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData currentData) {
                                    // Try to read current value as a List<Long> (array-like structure)
                                    List<Long> dayList = currentData.getValue(new GenericTypeIndicator<List<Long>>() {});

                                    // If there's no list yet for today, create one
                                    if (dayList == null) {
                                        dayList = new ArrayList<>();
                                    }

                                    // Ensure index 0 exists (daily sum slot)
                                    if (dayList.isEmpty()) {
                                        dayList.add(0L); // index 0 = sum
                                    } else if (dayList.get(0) == null) {
                                        dayList.set(0, 0L);
                                    }

                                    // Update the daily sum (index 0)
                                    long currentSum = dayList.get(0);
                                    long newSum = currentSum + waterAmount;
                                    dayList.set(0, newSum);

                                    // Append the new drink amount to the end of the list (index 1..N)
                                    dayList.add((long) waterAmount);

                                    // Write the updated list back to the transaction data
                                    currentData.setValue(dayList);

                                    // Commit the transaction
                                    return Transaction.success(currentData);
                                }

                                @Override
                                public void onComplete(DatabaseError error, boolean committed, DataSnapshot snapshot) {
                                    if (error != null) {
                                        System.err.println("ERROR updateWater -> transaction failed: " + error.getMessage());
                                        future.complete(false);
                                        return;
                                    }
                                    if (!committed) {
                                        System.err.println("WARN updateWater -> transaction not committed");
                                        future.complete(false);
                                        return;
                                    }

                                    // Debug: log the final state after commit
                                    try {
                                        List<Long> finalList = snapshot.getValue(new GenericTypeIndicator<List<Long>>() {});
                                        System.out.println("DEBUG updateWater -> committed, finalList=" + finalList);
                                    } catch (Exception ignored) { }

                                    future.complete(true);
                                }
                            });

                            // We handled the first (and only) user; stop iterating
                            break;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Read of the user failed
                        System.err.println("ERROR updateWater -> onCancelled: " + error.getMessage());
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return the future immediately; it will be completed asynchronously
        return future;
    }

    // ------------------------------- GET WATER --------------------------
    // Returns a JSON object in the format {"todayWater": <ml>, "yesterdayWater": <ml>}
    // This format is required to match what the Android client expects.
    public CompletableFuture<JSONObject> getWater(String username) {
        // Create a new future that will hold the resulting JSON object
        CompletableFuture<JSONObject> future = new CompletableFuture<>();

        // Build date key for today
        String todayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Build date key for yesterday (by subtracting one day from calendar)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        String yesterdayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());

        // Query Firebase for the given username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // If user does not exist, complete future with null
                        if (!snapshot.exists()) {
                            future.complete(null);
                            return;
                        }
                        // Loop through matching user snapshots (usually one user)
                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            // Read daily totals for today and yesterday (slot "0")
                            Long todayAmt = userSnap.child("waterLog").child(todayKey).child("0").getValue(Long.class);
                            Long yesterdayAmt = userSnap.child("waterLog").child(yesterdayKey).child("0").getValue(Long.class);

                            JSONObject obj = new JSONObject();
                            try {
                                // IMPORTANT: keys must match what Android WaterActivity expects
                                obj.put("todayWater", todayAmt == null ? 0 : todayAmt);
                                obj.put("yesterdayWater", yesterdayAmt == null ? 0 : yesterdayAmt);
                            } catch (Exception e) {
                                // If exception occurs while building JSON, complete with null
                                future.complete(null);
                                return;
                            }
                            // Successfully built JSON object, complete the future
                            future.complete(obj);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // If Firebase query fails, complete future with exception
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return the future immediately (will complete later asynchronously)
        return future;
    }

    // ------------------------------- GET WATER HISTORY MAP --------------------------
    // Returns {"2025-09-29": 4600, "2025-09-28": 0, ...} for last N days
    public CompletableFuture<Map<String, Long>> getWaterHistoryMap(String username, int days) {


        // ⚠️⤵️ Executed in the CURRENT THREAD ⤵️⚠️
        // Future result container (async) that will eventually hold a Map<String, Long>
        CompletableFuture<Map<String, Long>> future = new CompletableFuture<>();

        // Prepare a list of the last `days` date-keys (e.g., today, yesterday, etc.)
        List<String> keys = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();

        // Generate date strings for the last `days` days
        for (int i = 0; i < days; i++) {
            // Format the current calendar date into a key
            String dateKey = sdf.format(cal.getTime());
            keys.add(dateKey);                 // Add formatted date to the list
            cal.add(Calendar.DAY_OF_YEAR, -1); // Move one day backwards
        }

        // 🔹 Debug log: which date keys we are about to query
        System.out.println("DEBUG getWaterHistoryMap -> generated keys: " + keys);
        // ⚠️⤴️ Executed in the CURRENT THREAD ⤴️⚠️



        // ⚠️⤵️ Executed in a SEPARATE THREAD ⤵️⚠️
        // Query Firebase for this username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // If the user does not exist, complete future with null and stop
                        if (!snapshot.exists()) {
                            System.out.println("DEBUG getWaterHistoryMap -> user not found: " + username);
                            future.complete(null);
                            return;
                        }

                        // Map to store the final water history (date -> daily sum)
                        Map<String, Long> result = new LinkedHashMap<>();

                        // Loop through user snapshots (should normally be one user)
                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            try {
                                // Loop through all prepared date keys
                                for (String key : keys) {
                                    // Read slot "0" which contains the daily sum for this date
                                    Long amt = userSnap.child("waterLog")
                                            .child(key)
                                            .child("0")
                                            .getValue(Long.class);

                                    // Debug log: show raw value and Firebase reference path
                                    System.out.println("DEBUG getWaterHistoryMap -> date=" + key
                                            + " raw=" + amt
                                            + " path=" + userSnap.child("waterLog").child(key).child("0").getRef());

                                    // Default to 0 if value is null
                                    long safeAmt = (amt == null ? 0 : amt);

                                    // Debug log: show processed safe amount for this date
                                    System.out.println("DEBUG getWaterHistoryMap -> key=" + key + " amt=" + safeAmt);

                                    // Put the date and amount into the result map
                                    result.put(key, safeAmt);
                                }

                                // Debug log: final result map before returning
                                System.out.println("DEBUG getWaterHistoryMap -> final map: " + result);

                                // Complete the future with the final map
                                future.complete(result);
                                return; // Important: exit after first userSnap
                            } catch (Exception e) {
                                // Log error and complete future with null if exception occurs
                                System.err.println("ERROR getWaterHistoryMap -> exception: " + e.getMessage());
                                e.printStackTrace();
                                future.complete(null);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // If query is cancelled or fails, complete future with the exception
                        System.err.println("ERROR getWaterHistoryMap -> cancelled: " + error.getMessage());
                        future.completeExceptionally(error.toException());
                    }
                });
        // ⚠️⤴️ Executed in a SEPARATE THREAD ⤴️⚠️



        // ⚠️⤵️ Executed in the CURRENT THREAD ⤵️⚠️
        return future;
        // ⚠️⤴️ Executed in the CURRENT THREAD ⤴️⚠️
    }

    // ------------------------------- GET WEEKLY AVERAGES (4 WEEKS) --------------------------
    // Reads slot "0" (daily sum) for the last 28 days, groups by week (7-day chunks),
    // and returns a LinkedHashMap in this order: Week 1 (newest) .. Week 4 (oldest).
    public CompletableFuture<Map<String, Integer>> getWeeklyAverages(String username) {

        // ⚠️⤵️ Executed in the CURRENT THREAD ⤵️⚠️
        // Create a new CompletableFuture instance that will hold a Map<String, Integer> result
        CompletableFuture<Map<String, Integer>> future = new CompletableFuture<>();

        // Build the exact 28 date keys (today inclusive, going backwards)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();

        // Create a list where each element will hold a string representation of a specific date
        // The list will contain 28 dates -> 28 days -> 4 weeks
        List<String> last28 = new ArrayList<>(28);
        for (int i = 0; i < 28; i++) {
            // Add the current date (formatted as yyyy-MM-dd) to the list
            last28.add(sdf.format(cal.getTime()));
            // Move the calendar one day backwards
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }

        // Print debug message showing the number of generated keys (should be 28)
        System.out.println("DEBUG getWeeklyAverages(4w) -> keys size=" + last28.size());
        // ⚠️⤴️ Executed in the CURRENT THREAD ⤴️⚠️



        // ⚠️⤵️ Executed in a SEPARATE THREAD ⤵️⚠️
        // This code runs in a completely separate thread, holding a reference to the FUTURE object
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // If no user with this username exists, complete the future with an empty map
                        if (!snapshot.exists()) {
                            System.out.println("DEBUG getWeeklyAverages(4w) -> user not found: " + username);
                            future.complete(Collections.emptyMap());
                            return;
                        }

                        // Iterate over all matching users (should usually be just one)
                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            try {
                                // Arrays for weekly sums and counts
                                // sums[w] = total water amount for week w
                                // counts[w] = number of days with logged water intake for week w
                                long[] sums = new long[4];
                                int[] counts = new int[4];

                                // Loop over the 28 dates (last 4 weeks)
                                for (int i = 0; i < 28; i++) {
                                    String dateKey = last28.get(i);
                                    // Determine which week this date belongs to (0=newest week, 3=oldest)
                                    int weekIdx = i / 7;
                                    // Read slot "0" which represents the daily total
                                    Long amt = userSnap.child("waterLog").child(dateKey).child("0").getValue(Long.class);
                                    if (amt != null) {
                                        sums[weekIdx] += amt;   // add amount to this week's sum
                                        counts[weekIdx] += 1;   // increment count of active days in this week
                                    }
                                }

                                // Build output as a LinkedHashMap to preserve order
                                // Order: Week 1 (newest) -> Week 4 (oldest)
                                Map<String, Integer> out = new LinkedHashMap<>();
                                for (int w = 0; w < 4; w++) {
                                    // Calculate average if count > 0, otherwise set to 0
                                    int avg = (counts[w] > 0) ? (int) (sums[w] / counts[w]) : 0;
                                    out.put("Week " + (w + 1), avg);
                                }

                                // Debug output for verification
                                System.out.println("DEBUG getWeeklyAverages(4w) -> result=" + out);

                                // Complete the future with the calculated map
                                future.complete(out);
                                return;
                            } catch (Exception e) {
                                // If any exception occurs, complete future with empty map
                                e.printStackTrace();
                                future.complete(Collections.emptyMap());
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // If Firebase query is cancelled or fails, complete future with exception
                        future.completeExceptionally(error.toException());
                    }
                });
                // ⚠️⤴️ Executed in a SEPARATE THREAD ⤴️⚠️



        // ⚠️⤵️ Executed in the CURRENT THREAD ⤵️⚠️
        // Returned back to the USERSCONTROLLER
        // Inside the call to firebaseService.getWeeklyAverages(username)
        return future;
        // ⚠️⤴️ Executed in the CURRENT THREAD ⤴️⚠️
    }

    // get Daily drink goal
    public CompletableFuture<Integer> getGoalMl(String username) {

        // ⚠️⤵️ Executed in the CURRENT THREAD ⤵️⚠️
        // Future that will hold the user's goal (or default if missing)
        CompletableFuture<Integer> fut = new CompletableFuture<>();
        // ⚠️⤴️ Executed in the CURRENT THREAD ⤴️⚠️



        // ⚠️⤵️ Executed in a SEPARATE THREAD ⤵️⚠️
        // Query Firebase for user by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snap) {
                        // If user not found, return default value (3000)
                        if (!snap.exists()) {
                            fut.complete(3000);
                            return;
                        }

                        // For each user match (usually just one)
                        for (DataSnapshot userSnap : snap.getChildren()) {
                            // Read goalMl as Integer
                            Integer goal = userSnap.child("goalMl").getValue(Integer.class);
                            // Return value or default if null
                            fut.complete(goal != null ? goal : 3000);
                            return; // Only first match
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete exceptionally so controller can handle as 5xx
                        fut.completeExceptionally(error.toException());
                    }
                });
        // ⚠️⤴️ Executed in a SEPARATE THREAD ⤴️⚠️



        // ⚠️⤵️ Executed in the CURRENT THREAD ⤵️⚠️
        return fut;
        // ⚠️⤴️ Executed in the CURRENT THREAD ⤴️⚠️
    }

    // update Daily drink goal
    public CompletableFuture<Boolean> updateGoalMl(String username, int goalMl) {

        // ⚠️⤵️ Executed in the CURRENT THREAD ⤵️⚠️
        // Future that will hold true/false depending on update result
        CompletableFuture<Boolean> fut = new CompletableFuture<>();

        // Validate input (example: between 500ml and 10000ml)
        if (goalMl < 500 || goalMl > 10000) {
            fut.complete(false);
            return fut;
        }
        // ⚠️⤴️ Executed in the CURRENT THREAD ⤴️⚠️



        // ⚠️⤵️ Executed in a SEPARATE THREAD ⤵️⚠️
        // Query Firebase for user by username
        // Create a query in which child nodes are ordered by the values of the specified path.
        // Add a listener for a single change in the data at this location.
        // This listener will be triggered once with the value of the data at the location.
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snap) {
                        // If no user found, complete with false
                        if (!snap.exists()) {
                            fut.complete(false);
                            return;
                        }

                        // For each match, update goalMl field
                        for (DataSnapshot userSnap : snap.getChildren()) {

                            userSnap.getRef().child("goalMl").setValue(goalMl, (err, ref) -> {
                                if (err != null) { fut.complete(false); }

                                else { fut.complete(true); }
                            });

                            return; // Stop after first update
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete exceptionally if query cancelled
                        fut.completeExceptionally(error.toException());
                    }
                });
        // ⚠️⤴️ Executed in a SEPARATE THREAD ⤴️⚠️



        // ⚠️⤵️ Executed in the CURRENT THREAD ⤵️⚠️
        return fut;
        // ⚠️⤴️ Executed in the CURRENT THREAD ⤴️⚠️
    }

    // ------------------------------ BMI DISTRIBUTION (GLOBAL) --------------------------
    // Calculates how many users are in each BMI category:
    // Underweight (<18.5), Normal (18.5–24.9), Overweight (25–29.9), Obese (>=30).
    // Returns a CompletableFuture<Map<String, Integer>> like:
    // {"Underweight": 3, "Normal": 12, "Overweight": 5, "Obese": 2}
    public CompletableFuture<Map<String, Integer>> getBmiDistribution() {
        // Future that will hold the final distribution map
        CompletableFuture<Map<String, Integer>> future = new CompletableFuture<>();

        // Read all users once from Firebase
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                // Counters for each BMI category
                int underweight = 0; // BMI < 18.5
                int normal = 0;      // 18.5 <= BMI < 25
                int overweight = 0;  // 25   <= BMI < 30
                int obese = 0;       // BMI >= 30

                // Iterate over all user nodes
                for (DataSnapshot child : snapshot.getChildren()) {

                    // Read "bmi" field as Double
                    Double bmi = child.child("bmi").getValue(Double.class);

                    // If no BMI recorded for this user → skip (not counted)
                    if (bmi == null) { continue; }

                    double value = bmi.doubleValue();

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

                // Debug print
                System.out.println("DEBUG getBmiDistribution -> " + distribution);

                // Complete future with the calculated map
                future.complete(distribution);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // If query fails, complete with exception
                System.err.println("ERROR getBmiDistribution -> " + error.getMessage());
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
        // Future that will hold the result (calories or default 0)
        CompletableFuture<Integer> fut = new CompletableFuture<>();

        // Query Firebase by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snap) {
                        // If user not found → return 0 (default)
                        if (!snap.exists()) {
                            System.out.println("DEBUG getCalories -> user not found: " + username);
                            fut.complete(0);
                            return;
                        }

                        // For each matching user (usually one)
                        for (DataSnapshot userSnap : snap.getChildren()) {
                            // Read "calories" as Integer
                            Integer cals = userSnap.child("calories").getValue(Integer.class);
                            // Default to 0 if null
                            fut.complete(cals != null ? cals : 0);
                            return; // Only first match
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete exceptionally so controller can handle as 5xx if it wants
                        System.err.println("ERROR getCalories -> " + error.getMessage());
                        fut.completeExceptionally(error.toException());
                    }
                });

        return fut;
    }

    // ------------------------------ UPDATE CALORIES ---------------------------
    // Updates the "calories" field for a user.
    // Returns true if updated, false if user not found or invalid input.
    public CompletableFuture<Boolean> updateCalories(String username, int calories) {
        // Future that will hold true/false depending on update result
        CompletableFuture<Boolean> fut = new CompletableFuture<>();

        // Optional validation: we do not allow negative values
        // (You can change max if you want)
        if (calories < 0 || calories > 20000) {
            System.out.println("DEBUG updateCalories -> invalid value: " + calories);
            fut.complete(false);
            return fut;
        }

        // Query Firebase for user by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snap) {
                        // If no user found, complete with false
                        if (!snap.exists()) {
                            System.out.println("DEBUG updateCalories -> user not found: " + username);
                            fut.complete(false);
                            return;
                        }

                        // For each match, update "calories" field
                        for (DataSnapshot userSnap : snap.getChildren()) {
                            userSnap.getRef().child("calories").setValue(calories, (err, ref) -> {
                                if (err != null) {
                                    System.err.println("ERROR updateCalories -> " + err.getMessage());
                                    fut.complete(false);
                                } else {
                                    System.out.println("DEBUG updateCalories -> updated to " + calories +
                                            " for user " + username);
                                    fut.complete(true);
                                }
                            });

                            return; // Stop after first update
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete exceptionally if query cancelled
                        System.err.println("ERROR updateCalories onCancelled -> " + error.getMessage());
                        fut.completeExceptionally(error.toException());
                    }
                });

        return fut;
    }
}
