// Declare the package for this service.
package org.example.CapstoneProject.service;

// Import Google credentials support.
import com.google.auth.oauth2.GoogleCredentials;
// Import the Firebase app bootstrap class.
import com.google.firebase.FirebaseApp;
// Import Firebase options builder.
import com.google.firebase.FirebaseOptions;
// Import Firebase Realtime Database classes.
import com.google.firebase.database.*;
// Import environment configuration helper.
import org.example.CapstoneProject.EnvConfiguration.EnvConfig;
// Import the user model.
import org.example.CapstoneProject.model.User;
// Import JSON object support.
import org.json.JSONObject;
// Mark this class as a Spring service.
import org.springframework.stereotype.Service;

// Import IOException for Firebase initialization.
import java.io.IOException;
// Import InputStream for the service account file.
import java.io.InputStream;
// Import SimpleDateFormat for daily keys.
import java.text.SimpleDateFormat;
// Import Java utility classes.
import java.util.*;
// Import CompletableFuture for async results.
import java.util.concurrent.CompletableFuture;

// Mark this class as injectable by Spring.
@SuppressWarnings("unused")
@Service
public class FirebaseService {
    // Hold a reference to the Users node in Firebase.
    private final DatabaseReference usersRef;

    // Build the Firebase service and initialize the SDK.
    public FirebaseService() throws IOException {
        // Load the Firebase service account file from resources.
        InputStream serviceAccount = getClass().getResourceAsStream("/myfinaltopap-firebase-adminsdk-fbsvc-765944770e.json");
        // Check that the resource was found.
        if (serviceAccount == null) {
            // Throw an error if the file is missing.
            throw new IllegalStateException("Service account JSON not found!");
        }

        // Read the Firebase database URL from configuration.
        String firebaseUrl = EnvConfig.getFirebaseUrl();

        // Build the Firebase options object.
        FirebaseOptions options = FirebaseOptions.builder()
                // Set credentials from the service account stream.
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                // Set the realtime database URL.
                .setDatabaseUrl(firebaseUrl)
                // Finish building the options.
                .build();

        // Initialize Firebase only once.
        if (FirebaseApp.getApps().isEmpty()) {
            // Create the Firebase app instance.
            FirebaseApp.initializeApp(options);
            // Print a connection message.
            System.out.println("Connected to Firebase project: myfinaltopap");
        }

        // Point to the Users collection in Firebase.
        this.usersRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    // Sign up a new user.
    public CompletableFuture<String> signup(User user) {
        // Create the future to return.
        var future = new CompletableFuture<String>();
        // Extract the username safely.
        var username = user != null ? user.getUserName() : null;

        // Reject invalid usernames.
        if (username == null || username.isBlank()) {
            // Complete with an error message.
            future.complete("Error: invalid username");
            // Return the future immediately.
            return future;
        }

        // Query users by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read the result one time.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle the returned snapshot.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matching children.
                        for (DataSnapshot ignored : snapshot.getChildren()) {
                            // Username already exists.
                            future.complete("Username already exists");
                            // Stop after the first match.
                            return;
                        }

                        // Insert a new user node.
                        usersRef.push().setValue(user, (error, ref) -> {
                            // Check if the write succeeded.
                            if (error == null) {
                                // Complete with a success message.
                                future.complete("User created successfully");
                            } else {
                                // Complete with the Firebase error message.
                                future.complete("Error: " + error.getMessage());
                            }
                        });
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with an error string.
                        future.complete("Error: " + error.getMessage());
                    }
                });

        // Return the future.
        return future;
    }

    // Log in an existing user.
    public CompletableFuture<User> login(String username, String password) {
        // Create the future to return.
        CompletableFuture<User> future = new CompletableFuture<>();

        // Query users by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read the result one time.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle returned data.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matching users.
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Deserialize the user.
                            User existingUser = child.getValue(User.class);
                            // Check that the user exists and the password matches.
                            if (existingUser != null
                                    && existingUser.getPassword() != null
                                    && existingUser.getPassword().equals(password)) {
                                // Complete with the logged-in user.
                                future.complete(existingUser);
                                // Stop after the first valid match.
                                return;
                            }
                        }
                        // Complete with null if no match was found.
                        future.complete(null);
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with the Firebase exception.
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return the future.
        return future;
    }

    // Create a user and return success as a boolean.
    public CompletableFuture<Boolean> createUser(User user) {
        // Create the future to return.
        var future = new CompletableFuture<Boolean>();
        // Extract the username safely.
        var username = user != null ? user.getUserName() : null;

        // Reject invalid usernames.
        if (username == null || username.isBlank()) {
            // Complete with false.
            future.complete(false);
            // Return immediately.
            return future;
        }

        // Query users by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read the result once.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle returned data.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matches.
                        for (DataSnapshot ignored : snapshot.getChildren()) {
                            // Username already exists.
                            future.complete(false);
                            // Stop after the first match.
                            return;
                        }

                        // Insert the user.
                        usersRef.push().setValue(user, (error, ref) -> future.complete(error == null));
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with the Firebase exception.
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return the future.
        return future;
    }

    // Read all users.
    public CompletableFuture<List<User>> getAllUsers() {
        // Create the future to return.
        var future = new CompletableFuture<List<User>>();

        // Read all users once.
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            // Handle returned data.
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Create the result list.
                List<User> users = new ArrayList<>();

                // Loop over all children.
                for (var child : snapshot.getChildren()) {
                    // Deserialize the user.
                    var user = child.getValue(User.class);
                    // Add non-null users to the result.
                    if (user != null) users.add(user);
                }

                // Complete with the list.
                future.complete(users);
            }

            // Handle Firebase cancellation.
            @Override
            public void onCancelled(DatabaseError error) {
                // Complete with the Firebase exception.
                future.completeExceptionally(error.toException());
            }
        });

        // Return the future.
        return future;
    }

    // Read one user by username.
    public CompletableFuture<User> getUser(String username) {
        // Create the future to return.
        var future = new CompletableFuture<User>();

        // Query by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read one time.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle returned data.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matches.
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Complete with the first matching user.
                            future.complete(child.getValue(User.class));
                            // Stop after the first match.
                            return;
                        }
                        // Complete with null when not found.
                        future.complete(null);
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with the Firebase exception.
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return the future.
        return future;
    }

    // Replace a user completely.
    public CompletableFuture<Boolean> updateUser(String username, User updatedUser) {
        // Create the future to return.
        var future = new CompletableFuture<Boolean>();

        // Query by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read one time.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle returned data.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matches.
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Keep the username stable.
                            updatedUser.setUserName(username);
                            // Write the full updated user.
                            child.getRef().setValue(updatedUser, (error, ref) -> future.complete(error == null));
                            // Stop after the first match.
                            return;
                        }
                        // Complete with false when not found.
                        future.complete(false);
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with the Firebase exception.
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return the future.
        return future;
    }

    // Partially update a user.
    public CompletableFuture<User> patchUser(String username, Map<String, Object> updates) {
        // Create the future to return.
        var future = new CompletableFuture<User>();

        // Query by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read one time.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle returned data.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matches.
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Keep a reference to the matched node.
                            var ref = child.getRef();
                            // Copy the incoming updates.
                            var safeUpdates = new HashMap<>(updates);
                            // Prevent username changes here.
                            safeUpdates.remove("userName");

                            // If nothing remains to update.
                            if (safeUpdates.isEmpty()) {
                                // Return the current user.
                                future.complete(child.getValue(User.class));
                                // Stop immediately.
                                return;
                            }

                            // Apply the partial update.
                            ref.updateChildren(safeUpdates, (error, ignoredRef) -> {
                                // Check for write failure.
                                if (error != null) {
                                    // Complete with the Firebase exception.
                                    future.completeExceptionally(error.toException());
                                } else {
                                    // Re-read the updated node.
                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        // Handle refreshed data.
                                        @Override
                                        public void onDataChange(DataSnapshot refreshed) {
                                            // Complete with the updated user.
                                            future.complete(refreshed.getValue(User.class));
                                        }

                                        // Handle Firebase cancellation.
                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            // Complete with the Firebase exception.
                                            future.completeExceptionally(error.toException());
                                        }
                                    });
                                }
                            });
                            // Stop after the first match.
                            return;
                        }
                        // Complete with null when not found.
                        future.complete(null);
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with the Firebase exception.
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return the future.
        return future;
    }

    // Delete a user by username.
    public CompletableFuture<Boolean> deleteUser(String username) {
        // Create the future to return.
        var future = new CompletableFuture<Boolean>();

        // Query by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read one time.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle returned data.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matches.
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Remove the matched user node.
                            child.getRef().removeValue((error, ref) -> future.complete(error == null));
                            // Stop after the first match.
                            return;
                        }
                        // Complete with false when not found.
                        future.complete(false);
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with the Firebase exception.
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return the future.
        return future;
    }

    // Check whether a user exists.
    public CompletableFuture<Boolean> exists(String username) {
        // Create the future to return.
        var future = new CompletableFuture<Boolean>();

        // Query by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read one time.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle returned data.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matches.
                        for (DataSnapshot ignored : snapshot.getChildren()) {
                            // A match means the user exists.
                            future.complete(true);
                            // Stop immediately.
                            return;
                        }
                        // No matches means the user does not exist.
                        future.complete(false);
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with the Firebase exception.
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return the future.
        return future;
    }

    // Update only the BMI field.
    public CompletableFuture<Boolean> updateBmi(String username, double bmi) {
        // Create the future to return.
        var future = new CompletableFuture<Boolean>();

        // Query by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read one time.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle returned data.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matches.
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Update the BMI value.
                            child.getRef().child("bmi").setValue(bmi, (error, ref) -> future.complete(error == null));
                            // Stop after the first match.
                            return;
                        }
                        // Complete with false when not found.
                        future.complete(false);
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with the Firebase exception.
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return the future.
        return future;
    }

    // Update the user's water log for today.
    public CompletableFuture<Boolean> updateWater(String username, int waterAmount) {
        // Create the future to return.
        var future = new CompletableFuture<Boolean>();

        // Reject invalid water amounts.
        if (waterAmount <= 0) {
            // Complete with false.
            future.complete(false);
            // Return immediately.
            return future;
        }

        // Query by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read one time.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle returned data.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matches.
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Build the date key for today.
                            var dayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                            // Point to today's water log node.
                            var todayRef = child.getRef().child("waterLog").child(dayKey);

                            // Run a transaction on today's list.
                            todayRef.runTransaction(new Transaction.Handler() {
                                // Update the list atomically.
                                @Override
                                public Transaction.Result doTransaction(MutableData currentData) {
                                    // Read the existing list.
                                    List<Long> dayList = currentData.getValue(new GenericTypeIndicator<>() {
                                    });
                                    // Create a new list if missing.
                                    if (dayList == null) {
                                        dayList = new ArrayList<>();
                                    }
                                    // Add the total slot if the list is empty.
                                    if (dayList.isEmpty()) {
                                        dayList.add(0L);
                                    } else if (dayList.getFirst() == null) {
                                        // Repair the total slot if it is null.
                                        dayList.set(0, 0L);
                                    }

                                    // Read the current total.
                                    @SuppressWarnings("DataFlowIssue") long currentSum = dayList.getFirst();
                                    // Update the total at index 0.
                                    dayList.set(0, currentSum + waterAmount);
                                    // Append the new drink amount.
                                    dayList.add((long) waterAmount);
                                    // Save the updated list.
                                    currentData.setValue(dayList);
                                    // Commit the transaction.
                                    return Transaction.success(currentData);
                                }

                                // Handle transaction completion.
                                @Override
                                public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                                    // Complete with true only on committed success.
                                    future.complete(error == null && committed);
                                }
                            });
                            // Stop after the first match.
                            return;
                        }
                        // Complete with false when not found.
                        future.complete(false);
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with the Firebase exception.
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return the future.
        return future;
    }

    // Get today's and yesterday's water totals.
    public CompletableFuture<JSONObject> getWater(String username) {
        // Create the future to return.
        var future = new CompletableFuture<JSONObject>();

        // Build today's date key.
        var todayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        // Create a calendar instance.
        var cal = Calendar.getInstance();
        // Move the calendar back one day.
        cal.add(Calendar.DAY_OF_YEAR, -1);
        // Build yesterday's date key.
        var yesterdayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());

        // Query by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read one time.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle returned data.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matches.
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Read today's total.
                            var todayAmt = child.child("waterLog").child(todayKey).child("0").getValue(Long.class);
                            // Read yesterday's total.
                            var yesterdayAmt = child.child("waterLog").child(yesterdayKey).child("0").getValue(Long.class);
                            // Create the JSON response.
                            var obj = new JSONObject();
                            try {
                                // Put today's water in the response.
                                obj.put("todayWater", todayAmt == null ? 0 : todayAmt);
                                // Put yesterday's water in the response.
                                obj.put("yesterdayWater", yesterdayAmt == null ? 0 : yesterdayAmt);
                            } catch (Exception e) {
                                // Complete with null on JSON failure.
                                future.complete(null);
                                // Stop immediately.
                                return;
                            }
                            // Complete with the JSON object.
                            future.complete(obj);
                            // Stop after the first match.
                            return;
                        }
                        // Complete with null when not found.
                        future.complete(null);
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with the Firebase exception.
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return the future.
        return future;
    }

    // Get water history for the last N days.
    public CompletableFuture<Map<String, Long>> getWaterHistoryMap(String username, int days) {
        // Create the future to return.
        var future = new CompletableFuture<Map<String, Long>>();

        // Create a list of date keys.
        List<String> keys = new ArrayList<>();
        // Create the formatter for day keys.
        var sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        // Create a calendar instance.
        var cal = Calendar.getInstance();
        // Build the list of requested days.
        for (var i = 0; i < days; i++) {
            // Add the current day key.
            keys.add(sdf.format(cal.getTime()));
            // Move one day back.
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }

        // Query by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read one time.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle returned data.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matches.
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Create the result map.
                            Map<String, Long> result = new LinkedHashMap<>();
                            try {
                                // Loop over all requested keys.
                                for (String key : keys) {
                                    // Read the amount for that day.
                                    var amt = child.child("waterLog").child(key).child("0").getValue(Long.class);
                                    // Put the amount into the result map.
                                    result.put(key, amt == null ? 0 : amt);
                                }
                                // Complete with the result map.
                                future.complete(result);
                            } catch (Exception e) {
                                // Complete with null on failure.
                                future.complete(null);
                            }
                            // Stop after the first match.
                            return;
                        }
                        // Complete with null when not found.
                        future.complete(null);
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with the Firebase exception.
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return the future.
        return future;
    }

    // Get weekly water averages for four weeks.
    public CompletableFuture<Map<String, Integer>> getWeeklyAverages(String username) {
        // Create the future to return.
        var future = new CompletableFuture<Map<String, Integer>>();

        // Create the formatter for day keys.
        var sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        // Create a calendar instance.
        var cal = Calendar.getInstance();
        // Hold the last 28 day keys.
        List<String> last28 = new ArrayList<>(28);
        // Build the last 28 keys.
        for (var i = 0; i < 28; i++) {
            // Add the current key.
            last28.add(sdf.format(cal.getTime()));
            // Move one day back.
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }

        // Query by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read one time.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle returned data.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matches.
                        for (DataSnapshot child : snapshot.getChildren()) {
                            try {
                                // Hold totals per week.
                                var sums = new long[4];
                                // Hold counts per week.
                                var counts = new int[4];
                                // Loop over all 28 days.
                                for (int i = 0; i < 28; i++) {
                                    // Read the date key at this index.
                                    var dateKey = last28.get(i);
                                    // Map the day to its week bucket.
                                    var weekIdx = i / 7;
                                    // Read the water total for the day.
                                    var amt = child.child("waterLog").child(dateKey).child("0").getValue(Long.class);
                                    // Add the amount if it exists.
                                    if (amt != null) {
                                        sums[weekIdx] += amt;
                                        counts[weekIdx] += 1;
                                    }
                                }

                                // Create the output map.
                                Map<String, Integer> out = new LinkedHashMap<>();
                                // Build four weekly averages.
                                for (int w = 0; w < 4; w++) {
                                    // Compute the average for this week.
                                    var avg = (counts[w] > 0) ? (int) (sums[w] / counts[w]) : 0;
                                    // Store the week label and value.
                                    out.put("Week " + (4 - w), avg);
                                }
                                // Complete with the output map.
                                future.complete(out);
                            } catch (Exception e) {
                                // Complete with an empty map on failure.
                                future.complete(Collections.emptyMap());
                            }
                            // Stop after the first match.
                            return;
                        }
                        // Complete with an empty map when not found.
                        future.complete(Collections.emptyMap());
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with the Firebase exception.
                        future.completeExceptionally(error.toException());
                    }
                });

        // Return the future.
        return future;
    }

    // Read the user's daily drink goal.
    public CompletableFuture<Integer> getGoalMl(String username) {
        // Create the future to return.
        var fut = new CompletableFuture<Integer>();

        // Query by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read one time.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle returned data.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matches.
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Read the goal value.
                            var goal = child.child("goalMl").getValue(Integer.class);
                            // Complete with the goal or the default.
                            fut.complete(goal != null ? goal : 3000);
                            // Stop after the first match.
                            return;
                        }
                        // Complete with the default when not found.
                        fut.complete(3000);
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with the Firebase exception.
                        fut.completeExceptionally(error.toException());
                    }
                });

        // Return the future.
        return fut;
    }

    // Update the user's daily drink goal.
    public CompletableFuture<Boolean> updateGoalMl(String username, int goalMl) {
        // Create the future to return.
        var fut = new CompletableFuture<Boolean>();

        // Reject invalid goal values.
        if (goalMl < 500 || goalMl > 10000) {
            // Complete with false.
            fut.complete(false);
            // Return immediately.
            return fut;
        }

        // Query by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read one time.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle returned data.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matches.
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Update the goal field.
                            child.getRef().child("goalMl").setValue(goalMl, (err, ref) -> fut.complete(err == null));
                            // Stop after the first match.
                            return;
                        }
                        // Complete with false when not found.
                        fut.complete(false);
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with the Firebase exception.
                        fut.completeExceptionally(error.toException());
                    }
                });

        // Return the future.
        return fut;
    }

    // Build the global BMI distribution.
    public CompletableFuture<Map<String, Integer>> getBmiDistribution() {
        // Create the future to return.
        var future = new CompletableFuture<Map<String, Integer>>();

        // Read all users once.
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            // Handle returned data.
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Count underweight users.
                var underweight = 0;
                // Count normal users.
                var normal = 0;
                // Count overweight users.
                var overweight = 0;
                // Count obese users.
                var obese = 0;

                // Loop over all users.
                for (var child : snapshot.getChildren()) {
                    // Read the bmi value.
                    var bmi = child.child("bmi").getValue(Double.class);
                    // Skip users with no bmi.
                    if (bmi == null) {
                        continue;
                    }

                    // Convert the BMI to a primitive double.
                    @SuppressWarnings("UnnecessaryUnboxing") var value = bmi.doubleValue();
                    // Count the user in the correct bucket.
                    if (value < 18.5) {
                        underweight++;
                    } else if (value < 25.0) {
                        normal++;
                    } else if (value < 30.0) {
                        overweight++;
                    } else {
                        obese++;
                    }
                }

                // Create the output map.
                Map<String, Integer> distribution = new LinkedHashMap<>();
                // Store the underweight count.
                distribution.put("Underweight", underweight);
                // Store the normal count.
                distribution.put("Normal", normal);
                // Store the overweight count.
                distribution.put("Overweight", overweight);
                // Store the obese count.
                distribution.put("Obese", obese);
                // Complete with the distribution map.
                future.complete(distribution);
            }

            // Handle Firebase cancellation.
            @Override
            public void onCancelled(DatabaseError error) {
                // Complete with the Firebase exception.
                future.completeExceptionally(error.toException());
            }
        });

        // Return the future.
        return future;
    }

    // Read the user's calories value.
    public CompletableFuture<Integer> getCalories(String username) {
        // Create the future to return.
        var fut = new CompletableFuture<Integer>();

        // Query by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read one time.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle returned data.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matches.
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Read the calories value.
                            var cals = child.child("calories").getValue(Integer.class);
                            // Complete with the calories value or zero.
                            fut.complete(cals != null ? cals : 0);
                            // Stop after the first match.
                            return;
                        }
                        // Complete with zero when not found.
                        fut.complete(0);
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with the Firebase exception.
                        fut.completeExceptionally(error.toException());
                    }
                });

        // Return the future.
        return fut;
    }

    // Update the user's calories value.
    public CompletableFuture<Boolean> updateCalories(String username, int calories) {
        // Create the future to return.
        var fut = new CompletableFuture<Boolean>();

        // Reject invalid calorie values.
        if (calories < 0 || calories > 20000) {
            // Complete with false.
            fut.complete(false);
            // Return immediately.
            return fut;
        }

        // Query by username.
        usersRef.orderByChild("userName").equalTo(username)
                // Read one time.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle returned data.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Loop over matches.
                        for (DataSnapshot child : snapshot.getChildren()) {
                            // Update the calories field.
                            child.getRef().child("calories").setValue(calories, (err, ref) -> fut.complete(err == null));
                            // Stop after the first match.
                            return;
                        }
                        // Complete with false when not found.
                        fut.complete(false);
                    }

                    // Handle Firebase cancellation.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Complete with the Firebase exception.
                        fut.completeExceptionally(error.toException());
                    }
                });

        // Return the future.
        return fut;
    }
}
