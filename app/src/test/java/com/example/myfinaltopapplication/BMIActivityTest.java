// Define package for the test class
package com.example.myfinaltopapplication;
// Android imports
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
// JSON for fake server responses
import org.json.JSONObject;
// JUnit imports
import org.junit.Test;
import org.junit.runner.RunWith;
// Assertions
import static org.junit.Assert.*;
// Robolectric imports
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowToast;
// Mockito for static mocking of RestClient
import org.mockito.MockedStatic;
import org.mockito.Mockito;
// Java concurrency
import java.util.concurrent.CompletableFuture;
// MPAndroidChart imports for checking PieChart state
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

// -----------------------------------------------------------------------------
// BMIActivityTest
// Purpose: Deep tests for BMIActivity using Robolectric + Mockito.
// We test:
//   1) Redirect to LoginActivity if no user in SharedPreferences
//   2) Initial load of BMI + calories from server into UI
//   3) Successful BMI calculation and update (updateBmi + SharedPreferences)
//   4) Calories add / subtract / reset flows (including setCalories calls)
//   5) BMI distribution PieChart data rendering
//   6) Back button navigation to HomePage
// -----------------------------------------------------------------------------
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class BMIActivityTest {

    // -------------------------------------------------------------------------
    // Helper: store a username in SharedPreferences before creating the Activity
    // -------------------------------------------------------------------------
    private void putUserInPrefs(Application app, String username) {
        // Get SharedPreferences by name as defined in strings.xml
        SharedPreferences prefs = app.getSharedPreferences(
                app.getString(R.string.myprefs),
                Application.MODE_PRIVATE
        );
        // Store the current user name
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(app.getString(R.string.currentuser), username);
        editor.commit();
    }

    // -------------------------------------------------------------------------
    // TEST 1:
    // If there is NO current user in SharedPreferences:
    //   - Activity should show Toast "You must log in first"
    //   - Activity should start LoginActivity
    //   - Activity should finish itself.
    // -------------------------------------------------------------------------
    @Test
    public void onCreate_noUser_redirectsToLoginAndFinishes() {
        // Mock static RestClient to avoid real calls (even though early return happens)
        try (MockedStatic<RestClient> ignored = Mockito.mockStatic(RestClient.class)) {

            // Get application instance
            Application app = RuntimeEnvironment.getApplication();

            // Do NOT put any user in SharedPreferences (simulate not logged in)

            // Build BMIActivity
            BMIActivity activity = Robolectric.buildActivity(BMIActivity.class)
                    .setup()
                    .get();

            // Run pending UI tasks
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            // Check latest Toast message
            CharSequence toastText = ShadowToast.getTextOfLatestToast();
            assertNotNull(toastText);
            assertEquals("You must log in first", toastText.toString());

            // Check that LoginActivity was started
            ShadowActivity shadowActivity = Shadows.shadowOf(activity);
            Intent started = shadowActivity.getNextStartedActivity();
            assertNotNull(started);
            assertEquals(LoginActivity.class.getName(),
                    started.getComponent().getClassName());

            // Activity should be finishing
            assertTrue(activity.isFinishing());
        }
    }

    // -------------------------------------------------------------------------
    // TEST 2:
    // Initial load with server data:
    //   - getBmi returns 24.5
    //   - getCalories returns 1500
    //   - UI should show "Saved BMI: 24.50"
    //   - caloriesStatusText should reflect 1500 with "Below target"
    //   - lastBmi and lastCalories should be stored in SharedPreferences.
    // -------------------------------------------------------------------------
    @Test
    public void onCreate_withServerData_loadsBmiAndCaloriesIntoUi() throws Exception {
        // Mock RestClient
        try (MockedStatic<RestClient> restClientMock = Mockito.mockStatic(RestClient.class)) {

            // Get application
            Application app = RuntimeEnvironment.getApplication();

            // Put logged-in user
            putUserInPrefs(app, "john");

            // Stub getBmi("john") -> 24.5
            CompletableFuture<Double> bmiFuture =
                    CompletableFuture.completedFuture(24.5);
            restClientMock.when(() -> RestClient.getBmi("john"))
                    .thenReturn(bmiFuture);

            // Stub getCalories("john") -> 1500
            CompletableFuture<Integer> caloriesFuture =
                    CompletableFuture.completedFuture(1500);
            restClientMock.when(() -> RestClient.getCalories("john"))
                    .thenReturn(caloriesFuture);

            // Stub getBmiDistribution() with empty JSON to simplify
            CompletableFuture<JSONObject> distFuture =
                    CompletableFuture.completedFuture(new JSONObject());
            restClientMock.when(RestClient::getBmiDistribution)
                    .thenReturn(distFuture);

            // Build BMIActivity
            BMIActivity activity = Robolectric.buildActivity(BMIActivity.class)
                    .setup()
                    .get();

            // Run pending UI tasks
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            // Find UI components
            TextView resultText = activity.findViewById(R.id.resultText);
            TextView caloriesStatusText = activity.findViewById(R.id.caloriesStatusText);

            // Check BMI text
            assertEquals("Saved BMI: 24.50", resultText.getText().toString());

            // Check calories status text
            String statusText = caloriesStatusText.getText().toString();
            assertTrue(statusText.contains("Today calories: 1500 kcal"));
            assertTrue(statusText.contains("Below target"));

            // Check SharedPreferences stored values
            SharedPreferences prefs = app.getSharedPreferences(
                    app.getString(R.string.myprefs),
                    Application.MODE_PRIVATE
            );
            float lastBmi = prefs.getFloat("lastBmi", -1f);
            int lastCalories = prefs.getInt("lastCalories", -1);

            assertEquals(24.5f, lastBmi, 0.001f);
            assertEquals(1500, lastCalories);
        }
    }

    // -------------------------------------------------------------------------
    // TEST 3:
    // BMI calculation success:
    //   - weight = 70, height = 170
    //   - BMI ≈ 24.22
    //   - resultText shows "Your BMI is XX.XX"
    //   - lastBmi stored in SharedPreferences
    //   - Toast "BMI saved successfully" is shown.
    // -------------------------------------------------------------------------
    @Test
    public void calcButton_validInput_calculatesAndSavesBmi() throws Exception {
        // Mock RestClient
        try (MockedStatic<RestClient> restClientMock = Mockito.mockStatic(RestClient.class)) {

            // Get application instance
            Application app = RuntimeEnvironment.getApplication();

            // Store logged in user "john" in SharedPreferences
            putUserInPrefs(app, "john");

            // Stub getBmi("john") -> null (no previous BMI)
            CompletableFuture<Double> bmiFuture =
                    CompletableFuture.completedFuture(null);
            restClientMock.when(() -> RestClient.getBmi("john"))
                    .thenReturn(bmiFuture);

            // Stub getCalories("john") -> 0
            CompletableFuture<Integer> caloriesFuture =
                    CompletableFuture.completedFuture(0);
            restClientMock.when(() -> RestClient.getCalories("john"))
                    .thenReturn(caloriesFuture);

            // Stub getBmiDistribution() -> empty JSON
            CompletableFuture<JSONObject> distFuture =
                    CompletableFuture.completedFuture(new JSONObject());
            restClientMock.when(RestClient::getBmiDistribution)
                    .thenReturn(distFuture);

            // Stub updateBmi("john", any double) -> success
            restClientMock.when(() -> RestClient.updateBmi(Mockito.eq("john"), Mockito.anyDouble()))
                    .thenReturn(CompletableFuture.completedFuture(true));

            // Build and start BMIActivity
            BMIActivity activity = Robolectric.buildActivity(BMIActivity.class)
                    .setup()
                    .get();

            // Run pending tasks from initial futures
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            // Find views in layout
            EditText weightEdit = activity.findViewById(R.id.weightEdit);
            EditText heightEdit = activity.findViewById(R.id.heightEdit);
            Button calcButton = activity.findViewById(R.id.calcButton);
            TextView resultText = activity.findViewById(R.id.resultText);

            // Set weight = 70 kg and height = 170 cm
            weightEdit.setText("70");
            heightEdit.setText("170");

            // Click the "Calculate" button
            calcButton.performClick();

            // Run pending UI / async tasks (updateBmi thenAccept + Toast)
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            // Read result text from UI
            String res = resultText.getText().toString();
            // Expect text to start with "Your BMI is "
            assertTrue(res.startsWith("Your BMI is "));

            // Extract numeric BMI value from "Your BMI is XX.XX"
            String[] parts = res.split(" ");
            double bmiValue = Double.parseDouble(parts[3]);

            // BMI should be approximately 24.22 (with small tolerance)
            assertEquals(24.22, bmiValue, 0.05);

            // Read SharedPreferences and check lastBmi was stored
            SharedPreferences prefs = app.getSharedPreferences(
                    app.getString(R.string.myprefs),
                    Application.MODE_PRIVATE
            );
            float storedBmi = prefs.getFloat("lastBmi", -1f);
            assertEquals((float) bmiValue, storedBmi, 0.001f);

            // Check that success Toast was shown
            CharSequence toastText = ShadowToast.getTextOfLatestToast();
            assertNotNull(toastText);
            assertEquals("BMI saved successfully", toastText.toString());
        }
    }

    // -------------------------------------------------------------------------
    // TEST 4:
    // Calories add flow:
    //   - Initial server calories = 1000
    //   - User enters "500" and clicks add
    //   - currentCalories should become 1500
    //   - caloriesStatusText updated
    //   - setCalories called once with 1500
    //   - "Calories saved" Toast is shown.
    // -------------------------------------------------------------------------
    @Test
    public void addCalories_validInput_updatesCaloriesAndSaves() throws Exception {
        // Mock RestClient
        try (MockedStatic<RestClient> restClientMock = Mockito.mockStatic(RestClient.class)) {

            // Get app
            Application app = RuntimeEnvironment.getApplication();

            // Put user
            putUserInPrefs(app, "john");

            // Stub getBmi -> null
            CompletableFuture<Double> bmiFuture =
                    CompletableFuture.completedFuture(null);
            restClientMock.when(() -> RestClient.getBmi("john"))
                    .thenReturn(bmiFuture);

            // Stub getCalories -> 1000
            CompletableFuture<Integer> caloriesFuture =
                    CompletableFuture.completedFuture(1000);
            restClientMock.when(() -> RestClient.getCalories("john"))
                    .thenReturn(caloriesFuture);

            // Stub getBmiDistribution -> empty JSON
            CompletableFuture<JSONObject> distFuture =
                    CompletableFuture.completedFuture(new JSONObject());
            restClientMock.when(RestClient::getBmiDistribution)
                    .thenReturn(distFuture);

            // Stub setCalories -> true
            restClientMock.when(() -> RestClient.setCalories(Mockito.eq("john"), Mockito.anyInt()))
                    .thenReturn(CompletableFuture.completedFuture(true));

            // Build activity
            BMIActivity activity = Robolectric.buildActivity(BMIActivity.class)
                    .setup()
                    .get();

            // Run pending tasks (initial futures)
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            // Find views
            EditText caloriesInput = activity.findViewById(R.id.caloriesInput);
            Button addButton = activity.findViewById(R.id.addCaloriesButton);
            TextView caloriesStatusText = activity.findViewById(R.id.caloriesStatusText);

            // Enter "500"
            caloriesInput.setText("500");

            // Click add
            addButton.performClick();

            // Run pending tasks (setCalories thenAccept)
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            // Status text should mention 1500
            String statusText = caloriesStatusText.getText().toString();
            assertTrue(statusText.contains("Today calories: 1500 kcal"));

            // Check that setCalories was called with 1500
            restClientMock.verify(
                    () -> RestClient.setCalories(Mockito.eq("john"), Mockito.eq(1500)),
                    Mockito.times(1)
            );

            // Latest Toast should be "Calories saved"
            CharSequence toastText = ShadowToast.getTextOfLatestToast();
            assertNotNull(toastText);
            assertEquals("Calories saved", toastText.toString());
        }
    }

    // -------------------------------------------------------------------------
    // TEST 5:
    // Calories reset flow:
    //   - Initial server calories = 1200
    //   - User clicks reset button
    //   - currentCalories becomes 0
    //   - status text shows "Today calories: 0 kcal ..."
    //   - setCalories called with 0.
    // -------------------------------------------------------------------------
    @Test
    public void resetCalories_setsToZero_andSaves() throws Exception {
        // Mock RestClient
        try (MockedStatic<RestClient> restClientMock = Mockito.mockStatic(RestClient.class)) {

            // Get app
            Application app = RuntimeEnvironment.getApplication();

            // Put user
            putUserInPrefs(app, "john");

            // getBmi -> null
            CompletableFuture<Double> bmiFuture =
                    CompletableFuture.completedFuture(null);
            restClientMock.when(() -> RestClient.getBmi("john"))
                    .thenReturn(bmiFuture);

            // getCalories -> 1200
            CompletableFuture<Integer> caloriesFuture =
                    CompletableFuture.completedFuture(1200);
            restClientMock.when(() -> RestClient.getCalories("john"))
                    .thenReturn(caloriesFuture);

            // getBmiDistribution -> empty
            CompletableFuture<JSONObject> distFuture =
                    CompletableFuture.completedFuture(new JSONObject());
            restClientMock.when(RestClient::getBmiDistribution)
                    .thenReturn(distFuture);

            // setCalories -> true
            restClientMock.when(() -> RestClient.setCalories(Mockito.eq("john"), Mockito.anyInt()))
                    .thenReturn(CompletableFuture.completedFuture(true));

            // Build activity
            BMIActivity activity = Robolectric.buildActivity(BMIActivity.class)
                    .setup()
                    .get();

            // Run pending tasks
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            // Find views
            Button resetButton = activity.findViewById(R.id.resetCaloriesButton);
            TextView caloriesStatusText = activity.findViewById(R.id.caloriesStatusText);

            // Click reset
            resetButton.performClick();

            // Run pending tasks
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            // Status text should show 0
            String statusText = caloriesStatusText.getText().toString();
            assertTrue(statusText.contains("Today calories: 0 kcal"));

            // setCalories should be called with 0
            restClientMock.verify(
                    () -> RestClient.setCalories(Mockito.eq("john"), Mockito.eq(0)),
                    Mockito.times(1)
            );
        }
    }

    // -------------------------------------------------------------------------
    // TEST 6:
    // BMI distribution PieChart:
    //   - getBmiDistribution returns JSON with some counts
    //   - PieChart should have entries for non-zero categories
    //   - DataSet entry count should match number of non-zero categories.
    // -------------------------------------------------------------------------
    @Test
    public void loadBmiDistributionChart_withData_populatesPieChart() throws Exception {
        // Mock RestClient
        try (MockedStatic<RestClient> restClientMock = Mockito.mockStatic(RestClient.class)) {

            // Get app
            Application app = RuntimeEnvironment.getApplication();

            // Put user
            putUserInPrefs(app, "john");

            // getBmi -> null
            CompletableFuture<Double> bmiFuture =
                    CompletableFuture.completedFuture(null);
            restClientMock.when(() -> RestClient.getBmi("john"))
                    .thenReturn(bmiFuture);

            // getCalories -> 0
            CompletableFuture<Integer> caloriesFuture =
                    CompletableFuture.completedFuture(0);
            restClientMock.when(() -> RestClient.getCalories("john"))
                    .thenReturn(caloriesFuture);

            // Build JSON distribution:
            // Underweight = 2, Normal = 5, Overweight = 3, Obese = 0
            JSONObject distJson = new JSONObject();
            distJson.put("Underweight", 2);
            distJson.put("Normal", 5);
            distJson.put("Overweight", 3);
            distJson.put("Obese", 0);

            CompletableFuture<JSONObject> distFuture =
                    CompletableFuture.completedFuture(distJson);
            restClientMock.when(RestClient::getBmiDistribution)
                    .thenReturn(distFuture);

            // Build activity
            BMIActivity activity = Robolectric.buildActivity(BMIActivity.class)
                    .setup()
                    .get();

            // Run pending tasks (including loadBmiDistributionChart)
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            // Find PieChart
            PieChart chart = activity.findViewById(R.id.bmiPieChart);
            assertNotNull(chart);

            // Get data from chart
            PieData data = chart.getData();
            assertNotNull(data);

            // There should be one dataset
            assertEquals(1, data.getDataSetCount());

            PieDataSet dataSet = (PieDataSet) data.getDataSetByIndex(0);

            // Non-zero categories are: Underweight, Normal, Overweight → 3 entries
            assertEquals(3, dataSet.getEntryCount());

            PieEntry e0 = dataSet.getEntryForIndex(0);
            PieEntry e1 = dataSet.getEntryForIndex(1);
            PieEntry e2 = dataSet.getEntryForIndex(2);

            // Just check Y-values and labels
            // Order is not guaranteed by JSON, but in our implementation we add
            // in Underweight, Normal, Overweight, Obese order.
            assertEquals(2f, e0.getY(), 0.001f);
            assertEquals("Underweight", e0.getLabel());

            assertEquals(5f, e1.getY(), 0.001f);
            assertEquals("Normal", e1.getLabel());

            assertEquals(3f, e2.getY(), 0.001f);
            assertEquals("Overweight", e2.getLabel());
        }
    }

    // -------------------------------------------------------------------------
    // TEST 7:
    // Back button:
    //   - Clicking backHome should start HomePage Activity.
    // -------------------------------------------------------------------------
    @Test
    public void backHomeButton_click_navigatesToHomePage() throws Exception {
        // Mock RestClient
        try (MockedStatic<RestClient> restClientMock = Mockito.mockStatic(RestClient.class)) {

            // Get application
            Application app = RuntimeEnvironment.getApplication();

            // Put user
            putUserInPrefs(app, "john");

            // getBmi -> null
            CompletableFuture<Double> bmiFuture =
                    CompletableFuture.completedFuture(null);
            restClientMock.when(() -> RestClient.getBmi("john"))
                    .thenReturn(bmiFuture);

            // getCalories -> 0
            CompletableFuture<Integer> caloriesFuture =
                    CompletableFuture.completedFuture(0);
            restClientMock.when(() -> RestClient.getCalories("john"))
                    .thenReturn(caloriesFuture);

            // getBmiDistribution -> empty JSON
            CompletableFuture<JSONObject> distFuture =
                    CompletableFuture.completedFuture(new JSONObject());
            restClientMock.when(RestClient::getBmiDistribution)
                    .thenReturn(distFuture);

            // Build activity
            BMIActivity activity = Robolectric.buildActivity(BMIActivity.class)
                    .setup()
                    .get();

            // Run pending tasks
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            // Find back button
            ImageButton backHome = activity.findViewById(R.id.imageButton3);

            // Click back button
            backHome.performClick();

            // Inspect next started activity
            ShadowActivity shadowActivity = Shadows.shadowOf(activity);
            Intent started = shadowActivity.getNextStartedActivity();

            // Assert navigation
            assertNotNull(started);
            assertEquals(HomePage.class.getName(),
                    started.getComponent().getClassName());
        }
    }
}

