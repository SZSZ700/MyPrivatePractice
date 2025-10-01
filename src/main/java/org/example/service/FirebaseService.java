// Define the package for this service class
package org.example.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
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

        // Build Firebase options using the service account and database URL
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://myfinaltopap-default-rtdb.firebaseio.com/")
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

    // ------------------------------ UPDATE WATER ------------------------
    // Stores per-day array of size 13:
    // index 0 = daily total sum (ml), indexes 1..12 = individual cups
    public CompletableFuture<Boolean> updateWater(String username, int waterAmount) {
        // Future that will hold true/false result after operation
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Query Firebase for the given username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // If user not found, complete with false
                        if (!snapshot.exists()) {
                            future.complete(false);
                            return;
                        }

                        // Iterate over user snapshots (normally just one)
                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            // Generate today's date key
                            String dayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    .format(new Date());

                            // Reference to today's waterLog entry
                            DatabaseReference todayRef = userSnap.getRef()
                                    .child("waterLog").child(dayKey);

                            //-- Debug log for path reference
                            System.out.println("DEBUG: todayRef path = " + todayRef.toString());
                            //--

                            // Fetch today's record once
                            todayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot daySnapshot) {
                                    // Read the data as List<Long>
                                    GenericTypeIndicator<List<Long>> t =
                                            new GenericTypeIndicator<List<Long>>() {};
                                    List<Long> dayList = daySnapshot.getValue(t);

                                    // Debug log for existing value
                                    System.out.println("DEBUG: Existing value = " + daySnapshot.getValue());

                                    // Normalize the list to ensure correct size and no nulls
                                    if (dayList == null) {
                                        // Initialize with 13 zeros
                                        dayList = new ArrayList<>(Collections.nCopies(13, 0L));
                                    } else if (dayList.size() < 13) {
                                        // If list is shorter than 13, extend to length 13
                                        List<Long> fixed = new ArrayList<>(Collections.nCopies(13, 0L));
                                        for (int i = 0; i < Math.min(dayList.size(), 13); i++) {
                                            Long val = dayList.get(i);
                                            fixed.set(i, val == null ? 0L : val);
                                        }
                                        dayList = fixed;
                                    } else {
                                        // Replace any nulls with 0
                                        for (int i = 0; i < 13; i++) {
                                            if (dayList.get(i) == null) dayList.set(i, 0L);
                                        }
                                    }

                                    // Find next available cup slot (1..12)
                                    int cupIndex = 1;
                                    while (cupIndex <= 12 && dayList.get(cupIndex) > 0) {
                                        cupIndex++;
                                    }

                                    if (cupIndex <= 12) {
                                        // Place water amount into the next free slot
                                        dayList.set(cupIndex, (long) waterAmount);

                                        // Update daily total at index 0
                                        long sum = dayList.get(0) == null ? 0L : dayList.get(0);
                                        dayList.set(0, sum + waterAmount);

                                        // Write updated list back to Firebase
                                        todayRef.setValue(dayList, (error, ref) -> {
                                            if (error == null) {
                                                future.complete(true);   // success
                                            } else {
                                                future.complete(false);  // failed write
                                            }
                                        });
                                    } else {
                                        // All 12 cup slots are already filled
                                        future.complete(false);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // If query cancelled, complete with exception
                                    future.completeExceptionally(error.toException());
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // If outer query cancelled, complete with exception
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return the future immediately, will complete later asynchronously
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

        return future;
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
                            return; // ⚠️ stop the listener ⚠️
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
                                return; // ⚠️ stop the listener ⚠️
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

    // Updates the daily water goal (goalMl) for a given user
    public CompletableFuture<Boolean> updateGoalMl(String username, int goalMl) {
        // Future that will hold the success/failure result
        CompletableFuture<Boolean> fut = new CompletableFuture<>();

        // Validate input range for goal (example: between 500ml and 10000ml)
        if (goalMl < 500 || goalMl > 10000) {
            fut.complete(false);
            return fut;
        }

        // Query Firebase by username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snap) {
                        // If user not found, complete with false
                        if (!snap.exists()) {
                            fut.complete(false);
                            return;
                        }

                        // Update "goalMl" field for the first matched user
                        for (DataSnapshot userSnap : snap.getChildren()) {
                            userSnap.getRef().child("goalMl").setValue(goalMl, (error, ref) -> {
                                if (error != null) {
                                    // If error occurred, complete with false
                                    fut.complete(false);
                                } else {
                                    // If update succeeded, complete with true
                                    fut.complete(true);
                                }
                            });
                            return; // Exit after first update
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // If query is cancelled, complete future with exception
                        fut.completeExceptionally(error.toException());
                    }
                });

        return fut;
    }
}
