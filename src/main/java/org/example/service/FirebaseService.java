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
        // טוען את המפתח החדש מה־resources
        InputStream serviceAccount = getClass().getResourceAsStream("/myfinaltopap-firebase-adminsdk-fbsvc-765944770e.json");
        if (serviceAccount == null) {
            throw new IllegalStateException("Service account JSON not found!");
        }

        // בונה את ההגדרות עם המפתח וה־DB URL החדש
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://myfinaltopap-default-rtdb.firebaseio.com/")
                .build();

        // מונע כפילות של אפליקציה
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            System.out.println("✅ Connected to Firebase project: myfinaltopap");
        }

        this.usersRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    // =========================================================
    // SIGNUP → creates a new user with auto-generated key
    // With FULL DEBUG prints
    // =========================================================
    public CompletableFuture<String> signup(User user) {
        CompletableFuture<String> future = new CompletableFuture<>();

        System.out.println("DEBUG: Signup called for username = " + user.getUserName());

        // Print the reference path where we're working
        System.out.println("DEBUG: usersRef PATH = " + usersRef.getPath());
        System.out.println("DEBUG: usersRef URL  = " + usersRef.toString());

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("DEBUG: Checking existing users... total children = " + snapshot.getChildrenCount());

                // Iterate over existing users
                for (DataSnapshot child : snapshot.getChildren()) {
                    String existingUser = child.child("userName").getValue(String.class);

                    System.out.println("DEBUG: Found user in DB = " + existingUser);

                    if (existingUser != null && existingUser.equals(user.getUserName())) {
                        System.out.println("DEBUG: Username already exists → " + existingUser);
                        future.complete("Username already exists");
                        return;
                    }
                }

                // No duplicate → create new record
                String key = usersRef.push().getKey();
                if (key == null) {
                    System.err.println("DEBUG: Firebase push() returned null key!");
                    future.complete("Error generating key");
                    return;
                }

                System.out.println("DEBUG: Creating new user with key = " + key);
                System.out.println("DEBUG: User object to save = " + user);

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

                // Iterate through users
                for (DataSnapshot child : snapshot.getChildren()) {
                    String existingUser = child.child("userName").getValue(String.class);
                    String existingPass = child.child("password").getValue(String.class);

                    // Match credentials
                    if (existingUser != null && existingPass != null &&
                            existingUser.equals(username) &&
                            existingPass.equals(password)) {
                        foundUser = child.getValue(User.class);
                        break;
                    }
                }

                // Complete with user object or null
                future.complete(foundUser);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Return null on error
                future.complete(null);
            }
        });

        return future;
    }

    // ---------------------------- CREATE USER ---------------------------
    // Same semantics as signup but returns boolean and uses completion listener.
    public CompletableFuture<Boolean> createUser(User user) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        usersRef.orderByChild("userName").equalTo(user.getUserName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            future.complete(false);
                            return;
                        }
                        usersRef.push().setValueAsync(user)
                                .addListener(() -> future.complete(true), Runnable::run);
                    }
                    @Override public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ------------------------------ READ ALL ----------------------------
    public CompletableFuture<List<User>> getAllUsers() {
        CompletableFuture<List<User>> future = new CompletableFuture<>();

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    if (user != null) users.add(user);
                }
                future.complete(users);
            }
            @Override public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });

        return future;
    }

    // ------------------------------ READ ONE ----------------------------
    public CompletableFuture<User> getUser(String username) {
        CompletableFuture<User> future = new CompletableFuture<>();

        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            User user = child.getValue(User.class);
                            if (user != null) {
                                future.complete(user);
                                return;
                            }
                        }
                        future.complete(null);
                    }
                    @Override public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ------------------------------ UPDATE FULL -------------------------
    public CompletableFuture<Boolean> updateUser(String username, User updatedUser) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            future.complete(false);
                            return;
                        }
                        for (DataSnapshot child : snapshot.getChildren()) {
                            child.getRef().setValueAsync(updatedUser)
                                    .addListener(() -> future.complete(true), Runnable::run);
                            return;
                        }
                    }
                    @Override public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ------------------------------ PATCH (PARTIAL) ----------------------
    public CompletableFuture<User> patchUser(String username, Map<String, Object> updates) {
        CompletableFuture<User> future = new CompletableFuture<>();

        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            future.complete(null);
                            return;
                        }
                        for (DataSnapshot child : snapshot.getChildren()) {
                            DatabaseReference ref = child.getRef();
                            ref.updateChildrenAsync(updates).addListener(() -> {
                                // Re-read after update to return the fresh object
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override public void onDataChange(DataSnapshot refreshed) {
                                        User updated = refreshed.getValue(User.class);
                                        future.complete(updated);
                                    }
                                    @Override public void onCancelled(DatabaseError error) {
                                        future.completeExceptionally(error.toException());
                                    }
                                });
                            }, Runnable::run);
                            return;
                        }
                    }
                    @Override public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ------------------------------ DELETE ------------------------------
    public CompletableFuture<Boolean> deleteUser(String username) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            future.complete(false);
                            return;
                        }
                        for (DataSnapshot child : snapshot.getChildren()) {
                            child.getRef().removeValueAsync()
                                    .addListener(() -> future.complete(true), Runnable::run);
                            return;
                        }
                    }
                    @Override public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ------------------------------ EXISTS ------------------------------
    public CompletableFuture<Boolean> exists(String username) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot snapshot) {
                        future.complete(snapshot.exists());
                    }
                    @Override public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ------------------------------ UPDATE BMI --------------------------
    public CompletableFuture<Boolean> updateBmi(String username, double bmi) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            future.complete(false);
                            return;
                        }
                        for (DataSnapshot child : snapshot.getChildren()) {
                            child.getRef().child("bmi").setValueAsync(bmi)
                                    .addListener(() -> future.complete(true), Runnable::run);
                            return;
                        }
                    }
                    @Override public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ------------------------------ UPDATE WATER ------------------------
    // Stores per-day array (size 13): index 0 = daily sum (ml), 1..12 = cups
    public CompletableFuture<Boolean> updateWater(String username, int waterAmount) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            future.complete(false);
                            return;
                        }
                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            String dayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    .format(new Date());
                            DatabaseReference todayRef = userSnap.getRef()
                                    .child("waterLog").child(dayKey);

                            //--//
                            System.out.println("DEBUG: todayRef path = " + todayRef.toString());
                            //--//

                            todayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override public void onDataChange(DataSnapshot daySnapshot) {
                                    // Safely read as List<Long>
                                    GenericTypeIndicator<List<Long>> t =
                                            new GenericTypeIndicator<List<Long>>() {};
                                    List<Long> dayList = daySnapshot.getValue(t);

                                    //
                                    System.out.println("DEBUG: Existing value = " + daySnapshot.getValue());
                                    //

                                    if (dayList == null) {
                                        dayList = new ArrayList<>(Collections.nCopies(13, 0L));
                                    } else if (dayList.size() < 13) {
                                        // Ensure length 13
                                        List<Long> fixed = new ArrayList<>(Collections.nCopies(13, 0L));
                                        for (int i = 0; i < Math.min(dayList.size(), 13); i++) {
                                            Long val = dayList.get(i);
                                            fixed.set(i, val == null ? 0L : val);
                                        }
                                        dayList = fixed;
                                    } else {
                                        // Normalize nulls
                                        for (int i = 0; i < 13; i++) {
                                            if (dayList.get(i) == null) dayList.set(i, 0L);
                                        }
                                    }

                                    // Find next free cup slot 1..12
                                    int cupIndex = 1;
                                    while (cupIndex <= 12 && dayList.get(cupIndex) > 0) {
                                        cupIndex++;
                                    }

                                    if (cupIndex <= 12) {
                                        dayList.set(cupIndex, (long) waterAmount);
                                        long sum = dayList.get(0) == null ? 0L : dayList.get(0);
                                        dayList.set(0, sum + waterAmount);

                                        // Write back
                                        todayRef.setValue(dayList, (error, ref) -> {
                                            if (error == null) {
                                                future.complete(true);
                                            } else {
                                                future.complete(false);
                                            }
                                        });
                                    } else {
                                        // No more slots available (12 cups filled)
                                        future.complete(false);
                                    }
                                }
                                @Override public void onCancelled(DatabaseError error) {
                                    future.completeExceptionally(error.toException());
                                }
                            });
                        }
                    }
                    @Override public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ------------------------------- GET WATER --------------------------
    // Returns {"todayWater": <ml>, "yesterdayWater": <ml>} to match Android.
    public CompletableFuture<JSONObject> getWater(String username) {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();

        String todayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        String yesterdayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());

        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            future.complete(null);
                            return;
                        }
                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            Long todayAmt = userSnap.child("waterLog").child(todayKey).child("0").getValue(Long.class);
                            Long yesterdayAmt = userSnap.child("waterLog").child(yesterdayKey).child("0").getValue(Long.class);

                            JSONObject obj = new JSONObject();
                            try {
                                // IMPORTANT: keys must match WaterActivity
                                obj.put("todayWater", todayAmt == null ? 0 : todayAmt);
                                obj.put("yesterdayWater", yesterdayAmt == null ? 0 : yesterdayAmt);
                            } catch (Exception e) {
                                future.complete(null);
                                return;
                            }
                            future.complete(obj);
                            return;
                        }
                    }
                    @Override public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }

    // ------------------------------- GET WATER HISTORY MAP --------------------------
    // Returns {"2025-09-29": 4600, "2025-09-28": 0, ...} for last N days
    public CompletableFuture<Map<String, Long>> getWaterHistoryMap(String username, int days) {
        // Future result container (async)
        CompletableFuture<Map<String, Long>> future = new CompletableFuture<>();

        // Prepare list of last `days` date-keys (today, yesterday, etc.)
        List<String> keys = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < days; i++) {
            String dateKey = sdf.format(cal.getTime());
            keys.add(dateKey);
            cal.add(Calendar.DAY_OF_YEAR, -1); // move one day back
        }

        // 🔹 Debug log: which date keys we are about to query
        System.out.println("DEBUG getWaterHistoryMap -> generated keys: " + keys);

        // Query Firebase for this username
        usersRef.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            System.out.println("DEBUG getWaterHistoryMap -> user not found: " + username);
                            future.complete(null);
                            return;
                        }

                        // Map to hold final results
                        Map<String, Long> result = new LinkedHashMap<>();

                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            try {
                                for (String key : keys) {
                                    // Read slot 0 (daily sum)
                                    Long amt = userSnap.child("waterLog")
                                            .child(key)
                                            .child("0")
                                            .getValue(Long.class);

                                    System.out.println("DEBUG getWaterHistoryMap -> date=" + key
                                            + " raw=" + amt
                                            + " path=" + userSnap.child("waterLog").child(key).child("0").getRef());

                                    // Default to 0 if null
                                    long safeAmt = (amt == null ? 0 : amt);

                                    // 🔹 Debug log: what we read for this date
                                    System.out.println("DEBUG getWaterHistoryMap -> key=" + key + " amt=" + safeAmt);

                                    // Put into map
                                    result.put(key, safeAmt);
                                }

                                // 🔹 Debug log: final map before return
                                System.out.println("DEBUG getWaterHistoryMap -> final map: " + result);

                                future.complete(result);
                                return; // Important: exit loop after first userSnap
                            } catch (Exception e) {
                                System.err.println("ERROR getWaterHistoryMap -> exception: " + e.getMessage());
                                e.printStackTrace();
                                future.complete(null);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("ERROR getWaterHistoryMap -> cancelled: " + error.getMessage());
                        future.completeExceptionally(error.toException());
                    }
                });

        return future;
    }
}
