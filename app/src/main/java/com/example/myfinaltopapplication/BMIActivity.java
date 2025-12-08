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

    // Currently logged-in user (loaded from SharedPreferences)
    private String currentUser;

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
        SharedPreferences prefs = getSharedPreferences(getString(R.string.myprefs), MODE_PRIVATE);
        currentUser = prefs.getString(getString(R.string.currentuser), null);

        // If user is not logged in → force redirect to login
        if (currentUser == null) {
            Toast.makeText(this, "You must log in first", Toast.LENGTH_SHORT).show();
            Intent login = new Intent(BMIActivity.this, LoginActivity.class);
            startActivity(login);
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

        // ---------------------------------------------------------------------
        // Try to load saved BMI from server
        // ---------------------------------------------------------------------
        RestClient.getBmi(currentUser).thenAccept(bmi -> runOnUiThread(() -> {
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
        // Calculate button - compute BMI and update server
        // ---------------------------------------------------------------------
        calcButton.setOnClickListener(v -> {
            // Read user inputs (weight and height) as strings
            String weightStr = weightEdit.getText().toString().trim();
            String heightStr = heightEdit.getText().toString().trim();

            // Validate that both fields are not empty
            if (weightStr.isEmpty() || heightStr.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
                return; // Stop if invalid input
            }

            try {
                // Convert weight and height to numbers
                double weight = Double.parseDouble(weightStr);
                double heightM = Double.parseDouble(heightStr) / 100.0; // convert cm to meters
                // Calculate BMI = weight / (height^2)
                double bmi = weight / (heightM * heightM);

                // Display calculated BMI immediately
                resultText.setText("Your BMI is " + String.format("%.2f", bmi));

                // Save BMI to server using RestClient
                RestClient.updateBmi(currentUser, bmi).thenAccept(success -> runOnUiThread(() -> {
                    if (success) {
                        // If server update succeeded → save locally
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putFloat("lastBmi", (float) bmi);
                        editor.apply();

                        // Show confirmation toast
                        Toast.makeText(BMIActivity.this, "BMI saved successfully", Toast.LENGTH_SHORT).show();

                        // Optionally reload global chart after save (comment out if you don't want this)
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
        // Back button - return to HomePage
        // ---------------------------------------------------------------------
        backHome.setOnClickListener(v -> {
            Intent bhome = new Intent(BMIActivity.this, HomePage.class);
            startActivity(bhome);
        });

        // ---------------------------------------------------------------------
        // Load global BMI distribution for PieChart (does not affect old logic)
        // ---------------------------------------------------------------------
        loadBmiDistributionChart();
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
        CompletableFuture<JSONObject> future = RestClient.getBmiDistribution();

        // Handle async response
        future.thenAccept(obj -> runOnUiThread(() -> {
            try {
                // If null → keep "no data" state
                if (obj == null) {
                    Log.w("BMI_CHART", "getBmiDistribution returned null");
                    return;
                }

                // Prepare entries for each BMI category
                ArrayList<PieEntry> entries = new ArrayList<>();

                // Read counts from JSON (0 if missing)
                int under = obj.optInt("Underweight", 0);
                int normal = obj.optInt("Normal", 0);
                int over = obj.optInt("Overweight", 0);
                int obese = obj.optInt("Obese", 0);

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
                PieDataSet dataSet = new PieDataSet(entries, "");
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
