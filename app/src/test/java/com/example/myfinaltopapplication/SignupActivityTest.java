// Define the package of the test (same as app package)
package com.example.myfinaltopapplication;
// Import Android core classes used inside Activity
import android.content.Intent;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
// Import JUnit annotations
import org.junit.Test;
import org.junit.runner.RunWith;
// Import static assertions
import static org.junit.Assert.*;
// Import Mockito for static mocking of RestClient
import org.mockito.MockedStatic;
import org.mockito.Mockito;
// Import Robolectric runner + config
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
// Import Robolectric Shadows helpers
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowToast;
// Import CompletableFuture for async return values
import java.util.concurrent.CompletableFuture;

// -----------------------------------------------------------------------------
// SignupActivityTest
// Purpose: deep tests for signup Activity using Robolectric + Mockito.
// We test: validation, interaction with RestClient, SharedPreferences, navigation,
// and UI feedback (Toast messages).
// -----------------------------------------------------------------------------
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class SignupActivityTest {

    // -------------------------------------------------------------------------
    // Helper method: build signup Activity and run onCreate()
    // -------------------------------------------------------------------------
    private signup buildActivity() {
        // Build Activity instance with Robolectric and call lifecycle methods
        signup activity = Robolectric.buildActivity(signup.class)
                .setup()
                .get();

        // Return created Activity instance
        return activity;
    }

    // -------------------------------------------------------------------------
    // TEST 1: Empty fields → validation Toast + no RestClient.register call
    // -------------------------------------------------------------------------
    @Test
    public void signup_withEmptyFields_showsValidationToast_andDoesNotCallRegister() {
        // Create static mock for RestClient so we can verify there are no calls
        try (MockedStatic<RestClient> restClientMock = Mockito.mockStatic(RestClient.class)) {

            // Build Activity under test
            signup activity = buildActivity();

            // Find username EditText
            EditText usernameInput = activity.findViewById(R.id.editUsername);
            // Find password EditText
            EditText passwordInput = activity.findViewById(R.id.editPassword);
            // Find full name EditText
            EditText fullnameInput = activity.findViewById(R.id.editFullName);
            // Find age EditText
            EditText ageInput = activity.findViewById(R.id.editAge);
            // Find register Button
            Button registerButton = activity.findViewById(R.id.btnRegister);

            // Leave all fields empty (validation should fail)
            usernameInput.setText("");
            passwordInput.setText("");
            fullnameInput.setText("");
            ageInput.setText("");

            // Click register button to trigger validation logic
            registerButton.performClick();

            // Run pending UI tasks so Toast will be created
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            // Read latest Toast text
            CharSequence toastText = ShadowToast.getTextOfLatestToast();

            // Assert that Toast was shown
            assertNotNull(toastText);
            // Assert that Toast message equals "fill_all_fields" string resource
            assertEquals(
                    activity.getString(R.string.fill_all_fields),
                    toastText.toString()
            );

            // Verify that RestClient.register was NEVER called
            restClientMock.verify(
                    () -> RestClient.register(Mockito.any(User.class)),
                    Mockito.never()
            );
        }
    }

    // -------------------------------------------------------------------------
    // TEST 2: Successful signup → RestClient.register returns true,
    // Toast הצלחה + ניווט ל-LoginActivity + finish ל-signup.
    // -------------------------------------------------------------------------
    @Test
    public void signup_success_callsRegister_showsSuccessToast_andNavigatesToLogin() throws Exception {
        // Create static mock for RestClient
        try (MockedStatic<RestClient> restClientMock = Mockito.mockStatic(RestClient.class)) {

            // Create pre-completed future with true (registration success)
            CompletableFuture<Boolean> successFuture =
                    CompletableFuture.completedFuture(true);

            // Stub RestClient.register to return successFuture for any User
            restClientMock.when(() -> RestClient.register(Mockito.any(User.class)))
                    .thenReturn(successFuture);

            // Build Activity under test
            signup activity = buildActivity();

            // Find all input fields
            EditText usernameInput = activity.findViewById(R.id.editUsername);
            EditText passwordInput = activity.findViewById(R.id.editPassword);
            EditText fullnameInput = activity.findViewById(R.id.editFullName);
            EditText ageInput = activity.findViewById(R.id.editAge);
            Button registerButton = activity.findViewById(R.id.btnRegister);

            // Fill valid data into all fields
            usernameInput.setText("john");
            passwordInput.setText("1234");
            fullnameInput.setText("John Doe");
            ageInput.setText("25");

            // Click register button to trigger full signup flow
            registerButton.performClick();

            // Run pending UI tasks (thenAccept + Toast + startActivity)
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            // Verify that RestClient.register was called exactly once
            restClientMock.verify(
                    () -> RestClient.register(Mockito.any(User.class)),
                    Mockito.times(1)
            );

            // Get ShadowActivity to inspect navigation
            ShadowActivity shadowActivity = Shadows.shadowOf(activity);

            // Read started Activity Intent (should be LoginActivity)
            Intent startedIntent = shadowActivity.getNextStartedActivity();

            // Assert that we navigated to another Activity
            assertNotNull(startedIntent);
            // Assert that the target Activity is LoginActivity
            assertEquals(
                    LoginActivity.class.getName(),
                    startedIntent.getComponent().getClassName()
            );

            // Read latest Toast text
            CharSequence toastText = ShadowToast.getTextOfLatestToast();

            // Assert that Toast was shown
            assertNotNull(toastText);
            // Assert that Toast message equals "sign_up_succesfully"
            assertEquals(
                    activity.getString(R.string.sign_up_succesfully),
                    toastText.toString()
            );
        }
    }

    // -------------------------------------------------------------------------
    // TEST 3: Failed signup → RestClient.register returns false,
    // נשארים באותה Activity + Toast "username_allready_exists".
    // -------------------------------------------------------------------------
    @Test
    public void signup_failure_showsErrorToast_andDoesNotNavigate() throws Exception {
        // Create static mock for RestClient
        try (MockedStatic<RestClient> restClientMock = Mockito.mockStatic(RestClient.class)) {

            // Create pre-completed future with false (registration failed)
            CompletableFuture<Boolean> failFuture =
                    CompletableFuture.completedFuture(false);

            // Stub RestClient.register to return false for any User
            restClientMock.when(() -> RestClient.register(Mockito.any(User.class)))
                    .thenReturn(failFuture);

            // Build Activity under test
            signup activity = buildActivity();

            // Find all input fields
            EditText usernameInput = activity.findViewById(R.id.editUsername);
            EditText passwordInput = activity.findViewById(R.id.editPassword);
            EditText fullnameInput = activity.findViewById(R.id.editFullName);
            EditText ageInput = activity.findViewById(R.id.editAge);
            Button registerButton = activity.findViewById(R.id.btnRegister);

            // Fill valid data (so validation passes, but server fails)
            usernameInput.setText("john");
            passwordInput.setText("1234");
            fullnameInput.setText("John Doe");
            ageInput.setText("25");

            // Click register button
            registerButton.performClick();

            // Run pending UI tasks
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            // Verify that RestClient.register was called once
            restClientMock.verify(
                    () -> RestClient.register(Mockito.any(User.class)),
                    Mockito.times(1)
            );

            // Get ShadowActivity to inspect navigation
            ShadowActivity shadowActivity = Shadows.shadowOf(activity);

            // Try to get started Activity Intent (should be null on failure)
            Intent startedIntent = shadowActivity.getNextStartedActivity();

            // Assert that no navigation happened
            assertNull(startedIntent);

            // Read latest Toast text
            CharSequence toastText = ShadowToast.getTextOfLatestToast();

            // Assert that Toast was shown
            assertNotNull(toastText);
            // Assert that Toast message equals "username_allready_exists"
            assertEquals(
                    activity.getString(R.string.username_allready_exists),
                    toastText.toString()
            );
        }
    }
}

