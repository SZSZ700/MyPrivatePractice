// Define the package for this Activity
package com.example.myfinaltopapplication;

// Import Android Intent class for navigation between screens
import android.content.Intent;
// Import SharedPreferences for local key-value data storage
import android.content.SharedPreferences;
// Import Bundle to restore/save Activity state
import android.os.Bundle;
// Import Button widget
import android.widget.Button;
// Import EditText for user input fields
import android.widget.EditText;
// Import ImageButton for back navigation
import android.widget.ImageButton;
// Import TextView to display results
import android.widget.TextView;
// Import Toast for short popup messages
import android.widget.Toast;
// Import base class for Android activities
import androidx.appcompat.app.AppCompatActivity;
// Import CompletableFuture for async calls to server
import java.util.concurrent.CompletableFuture;
// Import logging for debug messages
import android.util.Log;
// Import MPAndroidChart classes for BMI PieChart
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
// Import JSON for BMI distribution response
import org.json.JSONObject;
// Import Java util for list handling
import java.util.ArrayList;

// -------------------------------------------------------------
// BMIActivity - calculates Body Mass Index and syncs with server
// Also shows global BMI distribution in a PieChart
// And manages a simple daily calories log
// -------------------------------------------------------------
public class BMIActivity extends AppCompatActivity {
    // Input field for weight (kg)
    private EditText weightEdit;
    // Input field for height (cm)
    private EditText heightEdit;
    // Button to trigger BMI calculation
    private Button calcButton;
    // TextView to show calculated or saved BMI
    private TextView resultText;
    // Back button to go home
    private ImageButton backHome;
    // PieChart to show global BMI distribution
    private PieChart bmiPieChart;

    // -------- Calories UI --------
    // EditText where user types calories amount to add/remove
    private EditText caloriesInput;
    // Button to add calories
    private Button addCaloriesButton;
    // Button to subtract calories
    private Button subtractCaloriesButton;
    // Button to reset calories to 0
    private Button resetCaloriesButton;
    // TextView that shows today's calories and status vs target
    private TextView caloriesStatusText;

    // Currently logged-in user (loaded from SharedPreferences)
    private String currentUser;

    // Current daily calories value
    private int currentCalories = 0;
    // Simple static target for calories (kcal)
    private static final int TARGET_CALORIES = 2000;

    // -------------------------------------------------------------------------
    // onCreate - lifecycle method called when Activity is created
    // -------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call parent implementation
        super.onCreate(savedInstanceState);
        // Load layout XML for BMIActivity
        setContentView(R.layout.activity_bmiactivity);

        // ---------------------------------------------------------------------
        // Load current logged-in user from SharedPreferences
        // ---------------------------------------------------------------------
        // Load user session preferences (local storage)
        var prefs = getSharedPreferences(getString(R.string.myprefs), MODE_PRIVATE);
        currentUser = prefs.getString(getString(R.string.currentuser), null);

        // If user is not logged in → force redirect to login
        if (currentUser == null) {
            // Show error toast
            Toast.makeText(this, "You must log in first", Toast.LENGTH_SHORT).show();
            // create intent to navigate to LoginActivity
            Intent login = new Intent(BMIActivity.this, LoginActivity.class);
            // start LoginActivity
            startActivity(login);
            // close BMIActivity so the user cannot go back
            finish();
            return; // Stop execution here
        }

        // ---------------------------------------------------------------------
        // Bind UI components from XML to Java fields
        // ---------------------------------------------------------------------
        weightEdit = findViewById(R.id.weightEdit);
        heightEdit = findViewById(R.id.heightEdit);
        calcButton = findViewById(R.id.calcButton);
        resultText = findViewById(R.id.resultText);
        backHome = findViewById(R.id.imageButton3);
        // Bind PieChart for global BMI distribution
        bmiPieChart = findViewById(R.id.bmiPieChart);

        // Bind calories views
        caloriesInput = findViewById(R.id.caloriesInput);
        addCaloriesButton = findViewById(R.id.addCaloriesButton);
        subtractCaloriesButton = findViewById(R.id.subtractCaloriesButton);
        resetCaloriesButton = findViewById(R.id.resetCaloriesButton);
        caloriesStatusText = findViewById(R.id.caloriesStatusText);

        // ---------------------------------------------------------------------
        // Try to load saved BMI from server
        // ---------------------------------------------------------------------
        // Load BMI from server
        RestClient.getBmi(currentUser).thenAccept(bmi -> runOnUiThread(() -> {
            // If BMI exists in server → display it
            if (bmi != null) {
                // If BMI exists in server → display it
                resultText.setText("Saved BMI: " + String.format("%.2f", bmi));
                // Save BMI locally in SharedPreferences as backup
                SharedPreferences.Editor editor = prefs.edit();
                editor.putFloat("lastBmi", bmi.floatValue());
                editor.apply();
            } else {
                // If no BMI in server → try to load local backup
                float savedBmi = prefs.getFloat("lastBmi", -1f);
                if (savedBmi != -1f) {
                    resultText.setText("Saved BMI: " + String.format("%.2f", savedBmi));
                }
            }
        }));

        // ---------------------------------------------------------------------
        // Load daily calories from server (simple single field "calories")
        // ---------------------------------------------------------------------
        RestClient.getCalories(currentUser).thenAccept(calories -> runOnUiThread(() -> {
            if (calories != null) {
                // If server returned value → use it
                currentCalories = calories;
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("lastCalories", calories);
                editor.apply();
            } else {
                // Fallback to local stored value (if exists)
                currentCalories = prefs.getInt("lastCalories", 0);
            }
            // Update status text to reflect current calories
            updateCaloriesStatusText();
        }));

        // ---------------------------------------------------------------------
        // Calculate button - compute BMI and update server
        // ---------------------------------------------------------------------
        calcButton.setOnClickListener(v -> {
            // Read user inputs (weight and height) as strings
            // Trim to remove leading/trailing spaces
            var weightStr = weightEdit.getText().toString().trim();
            var heightStr = heightEdit.getText().toString().trim();

            // Validate that both fields are not empty
            if (weightStr.isEmpty() || heightStr.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
                return; // Stop if invalid input
            }

            try {
                // Convert weight and height to numbers
                var weight = Double.parseDouble(weightStr);
                var heightM = Double.parseDouble(heightStr) / 100.0; // convert cm to meters
                // Calculate BMI = weight / (height^2)
                var bmi = weight / (heightM * heightM);

                // Display calculated BMI immediately
                resultText.setText("Your BMI is " + String.format("%.2f", bmi));

                // Save BMI to server using RestClient
                // Use currentUser to identify the user for authentication
                RestClient.updateBmi(currentUser, bmi).thenAccept(success -> runOnUiThread(() -> {
                    if (success) {
                        // If server update succeeded → save locally
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putFloat("lastBmi", (float) bmi);
                        editor.apply();

                        // Show confirmation toast
                        Toast.makeText(BMIActivity.this, "BMI saved successfully", Toast.LENGTH_SHORT).show();

                        // Optionally reload global chart after save
                        loadBmiDistributionChart();
                    } else {
                        // If failed → show error toast
                        Toast.makeText(BMIActivity.this, "Failed to save BMI", Toast.LENGTH_SHORT).show();
                    }
                }));

            } catch (NumberFormatException e) {
                // If input is not a valid number
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });

        // ---------------------------------------------------------------------
        // Calories buttons - add / subtract / reset
        // ---------------------------------------------------------------------
        addCaloriesButton.setOnClickListener(v -> {
            // Read user input as string and trim
            var txt = caloriesInput.getText().toString().trim();

            if (txt.isEmpty()) {
                // show toast if input is empty
                Toast.makeText(BMIActivity.this, "Please enter calories amount", Toast.LENGTH_SHORT).show();
                // stop execution here
                return;
            }

            try {

                var delta = Integer.parseInt(txt);

                if (delta <= 0) {
                    // show toast if input is not positive
                    Toast.makeText(BMIActivity.this, "Amount must be positive", Toast.LENGTH_SHORT).show();
                    // stop execution here
                    return;
                }

                // Increase today's calories
                currentCalories += delta;
                // Update status text
                this.updateCaloriesStatusText();
                // Save to server
                this.saveCaloriesToServer();
            } catch (NumberFormatException e) {
                Toast.makeText(BMIActivity.this, "Invalid calories number", Toast.LENGTH_SHORT).show();
            }
        });

        subtractCaloriesButton.setOnClickListener(v -> {
            // Read user input as string and trim
            var txt = caloriesInput.getText().toString().trim();

            if (txt.isEmpty()) {
                // show toast if input is empty
                Toast.makeText(BMIActivity.this, "Please enter calories amount", Toast.LENGTH_SHORT).show();
                // stop execution here
                return;
            }

            try {
                var delta = Integer.parseInt(txt);

                if (delta <= 0) {
                    // show toast if input is not positive
                    Toast.makeText(BMIActivity.this, "Amount must be positive", Toast.LENGTH_SHORT).show();
                    // stop execution here
                    return;
                }

                // Decrease today's calories
                currentCalories -= delta;
                if (currentCalories < 0) {
                    currentCalories = 0;
                }
                // Update status text
                updateCaloriesStatusText();
                // Save to server
                saveCaloriesToServer();
            } catch (NumberFormatException e) {
                Toast.makeText(BMIActivity.this, "Invalid calories number", Toast.LENGTH_SHORT).show();
            }
        });

        resetCaloriesButton.setOnClickListener(v -> {
            // Reset calories to zero
            currentCalories = 0;
            // Update status text
            this.updateCaloriesStatusText();
            // Save to server
            this.saveCaloriesToServer();
        });

        // ---------------------------------------------------------------------
        // Back button - return to HomePage
        // ---------------------------------------------------------------------
        backHome.setOnClickListener(v -> {
            // create intent to navigate to HomePage
            var bhome = new Intent(BMIActivity.this, HomePage.class);
            // start HomePage
            startActivity(bhome);
        });

        // ---------------------------------------------------------------------
        // Load global BMI distribution for PieChart (does not affect old logic)
        // ---------------------------------------------------------------------
        this.loadBmiDistributionChart();
    }

    // -------------------------------------------------------------------------
    // Helper: updateCaloriesStatusText
    // Builds a message like:
    // "Today calories: 1500 kcal (Below target, target 2000 kcal)"
    // -------------------------------------------------------------------------
    private void updateCaloriesStatusText() {
        String status;
        if (currentCalories < TARGET_CALORIES) {
            status = "Below target";
        } else if (currentCalories == TARGET_CALORIES) {
            status = "At target";
        } else {
            status = "Above target";
        }

        var msg = "Today calories: " + currentCalories + " kcal (" +
                status + ", target " + TARGET_CALORIES + " kcal)";
        caloriesStatusText.setText(msg);
    }

    // -------------------------------------------------------------------------
    // Helper: saveCaloriesToServer
    // Uses RestClient.setCalories and also updates SharedPreferences backup
    // -------------------------------------------------------------------------
    private void saveCaloriesToServer() {
        RestClient.setCalories(currentUser, currentCalories).thenAccept(success ->
                runOnUiThread(() -> {
                    if (success) {
                        // Update local backup
                        var prefs = getSharedPreferences(getString(R.string.myprefs), MODE_PRIVATE);
                        // create editor to save locally
                        SharedPreferences.Editor editor = prefs.edit();
                        // save today's calories
                        editor.putInt("lastCalories", currentCalories);
                        // save changes to SharedPreferences
                        editor.apply();
                        // Show confirmation toast
                        Toast.makeText(BMIActivity.this, "Calories saved", Toast.LENGTH_SHORT).show();
                    } else {
                        // If failed → show error toast
                        Toast.makeText(BMIActivity.this, "Failed to save calories", Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

    // -------------------------------------------------------------------------
    // loadBmiDistributionChart - fetches global BMI distribution and renders it
    // in a PieChart using RestClient.getBmiDistribution()
    // -------------------------------------------------------------------------
    private void loadBmiDistributionChart() {
        // If PieChart view is not found in layout → avoid crash
        if (bmiPieChart == null) {
            Log.w("BMI_CHART", "PieChart view is null – check activity_bmiactivity.xml");
            return;
        }

        // Show default text if there is no data yet
        bmiPieChart.setNoDataText("No BMI statistics available");

        // Call REST API to get global BMI distribution
        var future = RestClient.getBmiDistribution();

        // Handle async response
        future.thenAccept(obj -> runOnUiThread(() -> {
            try {
                // If null → keep "no data" state
                if (obj == null) {
                    Log.w("BMI_CHART", "getBmiDistribution returned null");
                    return;
                }

                // Prepare entries for each BMI category
                var entries = new ArrayList<PieEntry>();

                // Read counts from JSON (0 if missing)
                var under = obj.optInt("Underweight", 0);
                var normal = obj.optInt("Normal", 0);
                var over = obj.optInt("Overweight", 0);
                var obese = obj.optInt("Obese", 0);

                // Add only categories with at least one user
                if (under > 0) {
                    entries.add(new PieEntry(under, "Underweight"));
                }
                if (normal > 0) {
                    entries.add(new PieEntry(normal, "Normal"));
                }
                if (over > 0) {
                    entries.add(new PieEntry(over, "Overweight"));
                }
                if (obese > 0) {
                    entries.add(new PieEntry(obese, "Obese"));
                }

                // If still empty → no BMI recorded yet
                if (entries.isEmpty()) {
                    bmiPieChart.clear();
                    bmiPieChart.setNoDataText("No BMI statistics yet");
                    return;
                }

                // Create data set for PieChart
                var dataSet = new PieDataSet(entries, "");
                // Use built-in color template
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                // Text size for values on slices
                dataSet.setValueTextSize(12f);

                // Create PieData object from dataset
                PieData data = new PieData(dataSet);

                // Assign data to chart
                bmiPieChart.setData(data);

                // Optional appearance configs
                bmiPieChart.getDescription().setEnabled(false);
                bmiPieChart.setUsePercentValues(false);
                bmiPieChart.setEntryLabelTextSize(10f);

                // Enable legend to show categories
                bmiPieChart.getLegend().setEnabled(true);

                // Refresh chart
                bmiPieChart.invalidate();

            } catch (Exception e) {
                // Log any unexpected error
                Log.e("BMI_CHART", "Error rendering BMI PieChart", e);
            }
        }));
    }
}
