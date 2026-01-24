package com.example.myfinaltopapplication;
// Android imports
import android.content.Context;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
// JUnit + assertions
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
// Mockito static mocking
import org.mockito.MockedStatic;
import org.mockito.Mockito;
// Robolectric
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowToast;
// JSON + Future
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

// -----------------------------------------------------------------------------
// WaterActivityTest
// Deep tests for WaterActivity:
// 1. No current user -> redirect to Login + Toast.
// 2. Logged-in user -> initial server sync updates UI + SharedPreferences.
// 3. updateWater success -> updates text, prefs, and shows success Toast.
// 4. updateWater failure -> does NOT update prefs and shows error Toast.
// 5. History empty -> stats section shows "No history data available".
// 6. Home button -> navigates to HomePage.
// -----------------------------------------------------------------------------
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class WaterActivityTest {

    // -------------------------------------------------------------------------
    // Helper: build activity with NO user in SharedPreferences
    // -------------------------------------------------------------------------
    private WaterActivity buildActivityNoUser(MockedStatic<RestClient> restClientMock) {
        // Stub backend calls so they won't actually run (defensive)
        // Stub getWater
        restClientMock.when(
                // Lambda for RestClient.getWater(...)
                () -> RestClient.getWater(Mockito.anyString())
        ).thenReturn(CompletableFuture.completedFuture(null));

        // Stub getWaterHistoryMap (may or may not be used depending on history)
        restClientMock.when(
                // Lambda for RestClient.getWaterHistoryMap(...)
                () -> RestClient.getWaterHistoryMap(Mockito.anyString(), Mockito.anyInt())
        ).thenReturn(CompletableFuture.completedFuture(null));

        // Stub getGoal (may or may not be used depending on history)
        restClientMock.when(
                // Lambda for RestClient.getGoal(...)
                () -> RestClient.getGoal(Mockito.anyString())
        ).thenReturn(CompletableFuture.completedFuture(null));

        // Build controller, but DO NOT touch SharedPreferences before setup()
        ActivityController<WaterActivity> controller =
                Robolectric.buildActivity(WaterActivity.class);

        // Run onCreate / onStart / onResume
        WaterActivity activity = controller.setup().get();

        // Process any pending UI tasks
        Shadows.shadowOf(Looper.getMainLooper()).idle();

        return activity;
    }

    // -------------------------------------------------------------------------
    // Helper: build activity WITH logged-in user + initial prefs + backend stubs
    // userName: username to set in prefs
    // todayLocal / yestLocal: values to place in SharedPreferences BEFORE server sync
    // waterJson: JSON that backend getWater(...) should return
    // historyJson: JSON that backend getWaterHistoryMap(...) should return
    // goalJson: JSON that backend getGoal(...) should return
    // -------------------------------------------------------------------------
    private WaterActivity buildActivityWithUser(
            int todayLocal,
            int yestLocal,
            JSONObject waterJson,
            JSONObject historyJson,
            JSONObject goalJson,
            MockedStatic<RestClient> restClientMock
    ) {

        // Stub backend getWater(...) to return given JSON for this user
        restClientMock.when(
                // Lambda for RestClient.getWater(...)
                () -> RestClient.getWater("john")
        ).thenReturn(CompletableFuture.completedFuture(waterJson));

        // Stub backend history
        restClientMock.when(
                // Lambda for RestClient.getWaterHistoryMap(...)
                () -> RestClient.getWaterHistoryMap("john", 7)
        ).thenReturn(CompletableFuture.completedFuture(historyJson));

        // Stub getGoal
        restClientMock.when(
                // Lambda for RestClient.getGoal(...)
                () -> RestClient.getGoal("john")
        ).thenReturn(CompletableFuture.completedFuture(goalJson));

        // Prepare controller (onCreate not called yet)
        ActivityController<WaterActivity> controller =
                Robolectric.buildActivity(WaterActivity.class);

        // Get activity instance BEFORE setup, to access SharedPreferences
        WaterActivity activity = controller.get();

        // Fill SharedPreferences with currentuser + initial water values
        var prefs = activity.getSharedPreferences(
                activity.getString(R.string.myprefs),
                Context.MODE_PRIVATE
        );

        prefs.edit()
                .putString(activity.getString(R.string.currentuser), "john")
                .putInt("todayWater", todayLocal)
                .putInt("yesterdayWater", yestLocal)
                .commit();

        // Now run lifecycle (onCreate, etc.)
        controller.setup();

        // Process pending UI tasks (runOnUiThread callbacks)
        Shadows.shadowOf(Looper.getMainLooper()).idle();

        // Return activity instance
        return activity;
    }

    // -------------------------------------------------------------------------
    // TEST 1: No logged-in user -> redirect to LoginActivity + Toast message.
    // -------------------------------------------------------------------------
    @Test
    public void noCurrentUser_redirectsToLoginAndShowsToast() {
        try (var restClientMock = Mockito.mockStatic(RestClient.class)) {
            // Build activity with NO user in prefs
            var activity = buildActivityNoUser(restClientMock);

            // Inspect navigation using ShadowActivity
            var shadowActivity = Shadows.shadowOf(activity);
            // Get started Intent
            var startedIntent = shadowActivity.getNextStartedActivity();

            // We expect navigation to LoginActivity
            // Assert that we navigated to another Activity
            assertNotNull(startedIntent);
            // Assert that the target Activity is LoginActivity
            assertEquals(LoginActivity.class.getName(),
                    Objects.requireNonNull(startedIntent.getComponent()).getClassName());

            // Check latest Toast text
            CharSequence toastText = ShadowToast.getTextOfLatestToast();
            // Assert that Toast was shown
            assertNotNull(toastText);
            // Assert that Toast message equals "You must log in first"
            assertEquals("You must log in first", toastText.toString());

            // Activity should be finishing
            assertTrue(activity.isFinishing());
        }
    }

    // -------------------------------------------------------------------------
    // TEST 2: With logged-in user, server getWater overrides local prefs and
    // updates UI + SharedPreferences.
    // -------------------------------------------------------------------------
    @Test
    public void onCreate_withUser_syncsWaterFromServerAndUpdatesPrefsAndUi() throws Exception {
        try (var restClientMock = Mockito.mockStatic(RestClient.class)) {

            // Backend JSON that should override local values
            var waterJson = new JSONObject();
            waterJson.put("todayWater", 1200);
            waterJson.put("yesterdayWater", 800);

            // For this test we don't care about stats → history empty, goal null
            var emptyHistory = new JSONObject(); // length() == 0

            // Build activity with user "john" and local values (will be overridden)
            var activity = buildActivityWithUser(
                    50,   // local todayWater
                    30,   // local yesterdayWater
                    waterJson,
                    emptyHistory,
                    null,
                    restClientMock
            );

            // Find text views
            TextView totalWaterText = activity.findViewById(R.id.totalWaterText);
            TextView yesterdayText = activity.findViewById(R.id.yesterdayText);

            // UI should show values from server (1200 & 800)
            assertEquals("So far today: 1200 ml", totalWaterText.getText().toString());
            assertEquals("Yesterday: 800 ml", yesterdayText.getText().toString());

            // SharedPreferences should also be updated
            var prefs = activity.getSharedPreferences(
                    activity.getString(R.string.myprefs),
                    Context.MODE_PRIVATE
            );

            // Assert that SharedPreferences were updated
            assertEquals(1200, prefs.getInt("todayWater", -1));
            assertEquals(800, prefs.getInt("yesterdayWater", -1));
        }
    }

    // -------------------------------------------------------------------------
    // TEST 3: updateWater success (click 200 ml):
    // - RestClient.updateWater(...) returns true
    // - TextView updated
    // - SharedPreferences updated
    // - Success Toast shown.
    // -------------------------------------------------------------------------
    @Test
    public void updateWater_success_updatesTextPrefsAndShowsSuccessToast() throws Exception {
        try (var restClientMock = Mockito.mockStatic(RestClient.class)) {

            // Initial backend state: 0 today, 0 yesterday
            var waterJson = new JSONObject();
            waterJson.put("todayWater", 0);
            waterJson.put("yesterdayWater", 0);

            // History / goal not important in this test
            var emptyHistory = new JSONObject();

            // Stub updateWater("john", 200) to succeed
            restClientMock.when(
                    // Lambda for RestClient.updateWater(...)
                    () -> RestClient.updateWater("john", 200)
            ).thenReturn(CompletableFuture.completedFuture(true));

            // Stub getWater / history / goal used in onCreate
            restClientMock.when(
                    // Lambda for RestClient.getWater(...)
                    () -> RestClient.getWater("john")
            ).thenReturn(CompletableFuture.completedFuture(waterJson));

            // Stub getWaterHistoryMap
            restClientMock.when(
                    // Lambda for RestClient.getWaterHistoryMap(...)
                    () -> RestClient.getWaterHistoryMap("john", 7)
            ).thenReturn(CompletableFuture.completedFuture(emptyHistory));

            // Stub getGoal
            restClientMock.when(
                    // Lambda for RestClient.getGoal(...)
                    () -> RestClient.getGoal("john")
            ).thenReturn(CompletableFuture.completedFuture(null));

            // Build activity with logged-in user "john" (local today/yesterday = 0)
            var activity = buildActivityWithUser(
                    0,
                    0,
                    waterJson,
                    emptyHistory,
                    null,
                    restClientMock
            );

            // Find views
            TextView totalWaterText = activity.findViewById(R.id.totalWaterText);
            Button drink200 = activity.findViewById(R.id.drink200);

            // Click 200 ml button
            drink200.performClick();

            // Process UI tasks (runOnUiThread callbacks + Toast)
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            // Text should show 200 ml (0 + 200)
            assertEquals("So far today: 200 ml", totalWaterText.getText().toString());

            // SharedPreferences todayWater should be 200
            var prefs = activity.getSharedPreferences(
                    activity.getString(R.string.myprefs),
                    Context.MODE_PRIVATE
            );
            // Assert that SharedPreferences were updated
            assertEquals(200, prefs.getInt("todayWater", -1));

            // Toast text should be "+200 ml saved!"
            CharSequence toastText = ShadowToast.getTextOfLatestToast();
            // Assert that Toast was shown
            assertNotNull(toastText);
            // Assert that Toast message equals "+200 ml saved!"
            assertEquals("+200 ml saved!", toastText.toString());
        }
    }

    // -------------------------------------------------------------------------
    // TEST 4: updateWater failure (backend returns false):
    // - UI text still updates locally (200 ml)
    // - SharedPreferences NOT updated (remain 0)
    // - Error Toast shown.
    // -------------------------------------------------------------------------
    @Test
    public void updateWater_failure_doesNotChangePrefsAndShowsErrorToast() throws Exception {
        try (var restClientMock = Mockito.mockStatic(RestClient.class)) {

            // Backend state for onCreate
            var waterJson = new JSONObject();
            waterJson.put("todayWater", 0);
            waterJson.put("yesterdayWater", 0);

            var emptyHistory = new JSONObject();

            // Stub updateWater to FAIL
            restClientMock.when(
                    // Lambda for RestClient.updateWater(...)
                    () -> RestClient.updateWater("john", 200)
            ).thenReturn(CompletableFuture.completedFuture(false));

            // Stub calls from onCreate
            restClientMock.when(
                    // Lambda for RestClient.getWater(...)
                    () -> RestClient.getWater("john")
            ).thenReturn(CompletableFuture.completedFuture(waterJson));

            // Stub getWaterHistoryMap
            restClientMock.when(
                    // Lambda for RestClient.getWaterHistoryMap(...)
                    () -> RestClient.getWaterHistoryMap("john", 7)
            ).thenReturn(CompletableFuture.completedFuture(emptyHistory));

            // Stub getGoal
            restClientMock.when(
                    // Lambda for RestClient.getGoal(...)
                    () -> RestClient.getGoal("john")
            ).thenReturn(CompletableFuture.completedFuture(null));

            // Build activity
            var activity = buildActivityWithUser(
                    0,
                    0,
                    waterJson,
                    emptyHistory,
                    null,
                    restClientMock
            );

            // Find button & label
            TextView totalWaterText = activity.findViewById(R.id.totalWaterText);
            Button drink200 = activity.findViewById(R.id.drink200);

            // Click 200 ml button
            drink200.performClick();

            // Process UI tasks
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            // UI still shows 200 ml (since local counter increased)
            assertEquals("So far today: 200 ml", totalWaterText.getText().toString());

            // BUT SharedPreferences should remain 0 (updateWater failed)
            var prefs = activity.getSharedPreferences(
                    activity.getString(R.string.myprefs),
                    Context.MODE_PRIVATE
            );
            // Assert that SharedPreferences were NOT updated
            assertEquals(0, prefs.getInt("todayWater", 0));

            // Toast message should indicate failure
            CharSequence toastText = ShadowToast.getTextOfLatestToast();
            // Assert that Toast was shown
            assertNotNull(toastText);
            // Assert that Toast message equals "Failed to update water"
            assertEquals("Failed to update water", toastText.toString());
        }
    }

    // -------------------------------------------------------------------------
    // TEST 5: History is empty JSON -> stats section shows "No history data"
    // and progress bar is 0; best/lowest day show "no data".
    // -------------------------------------------------------------------------
    @Test
    public void historyEmpty_showsNoHistoryStats() throws Exception {
        try (var restClientMock = Mockito.mockStatic(RestClient.class)) {

            // getWater: some basic values
            var waterJson = new JSONObject();
            waterJson.put("todayWater", 500);
            waterJson.put("yesterdayWater", 300);

            // Empty history → triggers "No history data available"
            var emptyHistory = new JSONObject();

            // Goal JSON
            var goalJson = new JSONObject();
            goalJson.put("goalMl", 3000);

            // Build activity with user "john"
            var activity = buildActivityWithUser(
                    0,
                    0,
                    waterJson,
                    emptyHistory,
                    goalJson,
                    restClientMock
            );

            // Find stats views
            TextView goalSummaryTitle = activity.findViewById(R.id.goalSummaryTitle);
            TextView goalSummaryText = activity.findViewById(R.id.goalSummaryText);
            ProgressBar goalProgressBar = activity.findViewById(R.id.goalProgressBar);
            TextView bestDayText = activity.findViewById(R.id.bestDayText);
            TextView lowestDayText = activity.findViewById(R.id.lowestDayText);

            // Title mentions last 7 days
            assertEquals("Goal consistency (last 7 days)", goalSummaryTitle.getText().toString());
            // Text indicates no history
            assertEquals("No history data available", goalSummaryText.getText().toString());
            // Progress bar 0
            assertEquals(0, goalProgressBar.getProgress());
            // Best/lowest labels
            // Best/lowest should be "no data"
            assertEquals("Best day: no data", bestDayText.getText().toString());
            // Best/lowest should be "no data"
            assertEquals("Lowest day: no data", lowestDayText.getText().toString());
        }
    }

    // -------------------------------------------------------------------------
    // TEST 6: Home button starts HomePage activity.
    // -------------------------------------------------------------------------
    @Test
    public void clickingHomeButton_startsHomePage() throws Exception {
        try (var restClientMock = Mockito.mockStatic(RestClient.class)) {

            // Basic backend stubs for onCreate
            var waterJson = new JSONObject();
            waterJson.put("todayWater", 0);
            waterJson.put("yesterdayWater", 0);
            var emptyHistory = new JSONObject();

            restClientMock.when(
                    // Lambda for RestClient.getWater(...)
                    () -> RestClient.getWater("john")
            ).thenReturn(CompletableFuture.completedFuture(waterJson));

            // Stub getWaterHistoryMap
            restClientMock.when(
                    // Lambda for RestClient.getWaterHistoryMap(...)
                    () -> RestClient.getWaterHistoryMap("john", 7)
            ).thenReturn(CompletableFuture.completedFuture(emptyHistory));

            // Stub getGoal
            restClientMock.when(
                    // Lambda for RestClient.getGoal(...)
                    () -> RestClient.getGoal("john")
            ).thenReturn(CompletableFuture.completedFuture(null));

            // Build activity
            var activity = buildActivityWithUser(
                    0,
                    0,
                    waterJson,
                    emptyHistory,
                    null,
                    restClientMock
            );

            // Find home ImageButton
            ImageButton homeBtn = activity.findViewById(R.id.imageButton4);

            // Click home
            homeBtn.performClick();

            // Process UI tasks
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            // Inspect started activity
            var shadowActivity = Shadows.shadowOf(activity);
            // Get started Intent
            var startedIntent = shadowActivity.getNextStartedActivity();

            // We expect navigation to HomePage
            // Assert that we navigated to another Activity
            assertNotNull(startedIntent);
            // Assert that the target Activity is HomePage
            assertEquals(HomePage.class.getName(),
                    Objects.requireNonNull(startedIntent.getComponent()).getClassName());
        }
    }
}
