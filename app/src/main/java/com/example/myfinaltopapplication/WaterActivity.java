// Define the package this Activity belongs to
package com.example.myfinaltopapplication;

// Import Android Intent class for navigation between screens
import android.content.Intent;
// Import SharedPreferences for saving and loading user session data locally
import android.content.SharedPreferences;
// Import Bundle for saving/restoring Activity state
import android.os.Bundle;
// Import all UI widgets like TextView, Button, Spinner, Toast
import android.widget.*;
// Import AppCompatActivity as the base class for Activities
import androidx.appcompat.app.AppCompatActivity;
// Import CompletableFuture for handling async REST API calls
import java.util.concurrent.CompletableFuture;
// Import Log for debugging
import android.util.Log;

// -------------------------------------------------------------
// WaterActivity - Activity to track and update water consumption
// -------------------------------------------------------------
public class WaterActivity extends AppCompatActivity {
    // TextView to display how much water was consumed today
    private TextView totalWaterText;
    // TextView to display how much water was consumed yesterday
    private TextView yesterdayText;
    // Button to log 150 ml of water
    private Button drink150;
    // Button to log 200 ml of water
    private Button drink200;
    // Button to log 1000 ml of water
    private Button drink1000;
    // ImageButton to navigate back to Home page
    private ImageButton bhome;

    // Holds the currently logged-in username (loaded from SharedPreferences)
    private String currentUser;
    // Holds the total water consumed today
    private int totalDrank = 0;

    // -------------------------------------------------------------------------
    // onCreate - called when the Activity is first created
    // -------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call parent implementation to initialize Activity
        super.onCreate(savedInstanceState);
        // Load the UI layout from XML (activity_water.xml)
        setContentView(R.layout.activity_water);

        // Load user session preferences (local storage)
        SharedPreferences prefs = getSharedPreferences(getString(R.string.myprefs), MODE_PRIVATE);
        // Retrieve current logged-in user
        currentUser = prefs.getString(getString(R.string.currentuser), null);

        // If no user is logged in → redirect to LoginActivity
        if (currentUser == null) {
            // Show error toast
            Toast.makeText(this, "You must log in first", Toast.LENGTH_SHORT).show();
            // Navigate to LoginActivity
            Intent login = new Intent(WaterActivity.this, LoginActivity.class);
            startActivity(login);
            // Close WaterActivity so the user cannot go back
            finish();
            // Stop further execution
            return;
        }

        // Link Java fields with actual UI components in the layout
        totalWaterText = findViewById(R.id.totalWaterText);
        yesterdayText = findViewById(R.id.yesterdayText);
        drink150 = findViewById(R.id.drink150);
        drink200 = findViewById(R.id.drink200);
        drink1000 = findViewById(R.id.drink1000);
        bhome = findViewById(R.id.imageButton4);

        // Load locally saved data for today and yesterday from SharedPreferences
        totalDrank = prefs.getInt("todayWater", 0);
        int yesterdayAmountLocal = prefs.getInt("yesterdayWater", 0);
        // Update the UI with local values
        totalWaterText.setText("So far today: " + totalDrank + " ml");
        yesterdayText.setText("Yesterday: " + yesterdayAmountLocal + " ml");

        // Fetch latest water log from the backend (Spring Boot → Firebase)
        RestClient.getWater(currentUser).thenAccept(obj -> runOnUiThread(() -> {
            // If server returned a valid JSON object
            if (obj != null) {
                // Extract today's water amount from JSON (default = 0)
                int today = obj.optInt("todayWater", 0);
                // Extract yesterday's water amount from JSON (default = 0)
                int yesterday = obj.optInt("yesterdayWater", 0);

                // Update local variable for today's consumption
                totalDrank = today;
                // Update the UI with new values from server
                totalWaterText.setText("So far today: " + today + " ml");
                yesterdayText.setText("Yesterday: " + yesterday + " ml");

                // Save updated values back to SharedPreferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("todayWater", today);
                editor.putInt("yesterdayWater", yesterday);
                // commit במקום apply → לשמור מיידית
                editor.commit();

                // Log values to Logcat
                Log.d("WATER_PREFS", "Saved from server: todayWater=" + today + ", yesterdayWater=" + yesterday);
            }
        }));

        // Set click listener for 150 ml button → calls updateWater(150)
        drink150.setOnClickListener(v -> updateWater(150));
        // Set click listener for 200 ml button → calls updateWater(200)
        drink200.setOnClickListener(v -> updateWater(200));
        // Set click listener for 1000 ml button → calls updateWater(1000)
        drink1000.setOnClickListener(v -> updateWater(1000));

        // Set click listener for home button → navigates to HomePage
        bhome.setOnClickListener(v -> {
            Intent bh = new Intent(WaterActivity.this, HomePage.class);
            startActivity(bh);
        });
    }

    // -------------------------------------------------------------------------
    // updateWater - called when user logs new water consumption
    // amount = amount of water consumed (ml)
    // -------------------------------------------------------------------------
    private void updateWater(int amount) {
        // Increase today's total amount
        totalDrank += amount;
        // Update the UI immediately
        totalWaterText.setText("So far today: " + totalDrank + " ml");

        // Call backend API to update water log in Firebase
        RestClient.updateWater(currentUser, amount).thenAccept(success -> runOnUiThread(() -> {
            // If update succeeded
            if (success) {
                // Save updated total to SharedPreferences
                SharedPreferences prefs = getSharedPreferences(getString(R.string.myprefs), MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("todayWater", totalDrank);
                // commit במקום apply → לשמור מיידית
                editor.commit();

                // Log value to Logcat
                Log.d("WATER_PREFS", "Updated locally: todayWater=" + totalDrank);

                // Show confirmation toast
                Toast.makeText(this, "+" + amount + " ml saved!", Toast.LENGTH_SHORT).show();
            } else {
                // If server update failed, show error toast
                Toast.makeText(this, "Failed to update water", Toast.LENGTH_SHORT).show();
            }
        }));
    }
}

