// Define package for this Activity
package com.example.myfinaltopapplication;
// Import Intent for switching between activities
import android.content.Intent;
// Import SharedPreferences for saving user session locally
import android.content.SharedPreferences;
// Import Bundle for saving/restoring Activity state
import android.os.Bundle;
// Import Button widget for standard clickable buttons
import android.widget.Button;
// Import EditText widget for text input (username, password)
import android.widget.EditText;
// Import ImageButton for optional back/home navigation
import android.widget.ImageButton;
// Import Toast for displaying short popup messages
import android.widget.Toast;
// Import EdgeToEdge for full immersive screen support
import androidx.activity.EdgeToEdge;
// Import AppCompatActivity base class
import androidx.appcompat.app.AppCompatActivity;
// Import CompletableFuture for handling async calls from RestClient
import java.util.concurrent.CompletableFuture;

// -----------------------------------------------------------------------------
// LoginActivity
// Purpose: allows a user to log into the application by checking credentials
// against backend REST API (Spring Boot + Firebase).
// -----------------------------------------------------------------------------
public class LoginActivity extends AppCompatActivity {

    // Declare UI components (input fields + buttons)
    private EditText username;        // Input field for username
    private EditText pass;            // Input field for password
    private Button loginBtn;          // Button that triggers login process
    private Button signUpBtn;         // Button to navigate to signup page
    private ImageButton backTohome;   // Optional button for returning to home

    // -------------------------------------------------------------------------
    // onCreate - lifecycle method called when Activity is first created
    // -------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);            // Call parent constructor
        EdgeToEdge.enable(this);  // Enable edge-to-edge immersive layout
        setContentView(R.layout.activity_login);       // Load the layout XML file

        // ---------------------------------------------------------------------
        // Link Java fields with XML components from layout
        // ---------------------------------------------------------------------
        username = findViewById(R.id.editTextText);              // Bind username input
        pass = findViewById(R.id.editTextTextPassword);          // Bind password input
        loginBtn = findViewById(R.id.button);                    // Bind login button
        signUpBtn = findViewById(R.id.button2);                  // Bind signup button

        // ---------------------------------------------------------------------
        // Login button click logic
        // ---------------------------------------------------------------------
        loginBtn.setOnClickListener(v -> {
            // Read input values from text fields
            String usern = username.getText().toString().trim();
            String pas = pass.getText().toString().trim();

            // Validate that no field is empty
            if (usern.isEmpty() || pas.isEmpty()) {
                Toast.makeText(LoginActivity.this,
                        "Please fill all fields", Toast.LENGTH_SHORT).show();
                return; // Stop execution if input invalid
            }

            // Call RestClient.login() → sends POST /api/users/login to server
            CompletableFuture<User> loginFuture = RestClient.login(usern, pas);

            // Handle asynchronous server response
            loginFuture.thenAccept(user -> runOnUiThread(() -> {
                if (user != null) {
                    // ✅ If login successful → save user session locally
                    SharedPreferences prefs = getSharedPreferences(getString(R.string.myprefs), MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(getString(R.string.currentuser), user.getUserName()); // Save username
                    editor.putInt(getString(R.string.age), user.getAge());                 // Save age
                    editor.putString("fullName", user.getFullName());                      // Save full name

                    // ---------------------------------------------------------------------
                    // Save water data (today + yesterday) from server response
                    // ---------------------------------------------------------------------
                    try {
                        // RestClient.getWater מחזיר JSON – נקרא אותו כאן
                        RestClient.getWater(user.getUserName()).thenAccept(obj -> runOnUiThread(() -> {
                            if (obj != null) {
                                int today = obj.optInt("todayWater", 0);
                                int yesterday = obj.optInt("yesterdayWater", 0);

                                editor.putInt("todayWater", today);
                                editor.putInt("yesterdayWater", yesterday);

                                // נשמור מיידית כדי לוודא שהמים קיימים בשאר העמודים
                                editor.commit();

                                android.util.Log.d("LOGIN_PREFS",
                                        "Saved water at login: today=" + today + ", yesterday=" + yesterday);
                            } else {
                                editor.putInt("todayWater", 0);
                                editor.putInt("yesterdayWater", 0);
                                editor.commit();
                            }
                        }));
                    } catch (Exception e) {
                        editor.putInt("todayWater", 0);
                        editor.putInt("yesterdayWater", 0);
                        editor.commit();
                    }

                    // ✅ נשמור את שאר הנתונים (username/age/fullName)
                    editor.apply();

                    // Show success toast with user’s full name
                    Toast.makeText(LoginActivity.this,
                            "Welcome " + user.getFullName(), Toast.LENGTH_SHORT).show();

                    // Navigate to HomePage Activity
                    Intent intent = new Intent(LoginActivity.this, HomePage.class);
                    startActivity(intent);
                    finish(); // Close LoginActivity so user can’t go back
                } else {
                    // ❌ If login failed (wrong credentials)
                    Toast.makeText(LoginActivity.this,
                            "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }));
        });

        // ---------------------------------------------------------------------
        // Signup button click logic
        // ---------------------------------------------------------------------
        signUpBtn.setOnClickListener(v -> {
            // Open signup Activity for registration
            Intent intent = new Intent(LoginActivity.this, signup.class);
            startActivity(intent);
        });
    }
}
