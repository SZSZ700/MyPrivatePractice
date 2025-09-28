// Define the package of the Spring Boot application
package org.example;
import com.google.auth.oauth2.GoogleCredentials;    // For loading Firebase credentials
import com.google.firebase.FirebaseApp;             // For initializing Firebase
import com.google.firebase.FirebaseOptions;         // Options object for Firebase initialization
import org.springframework.boot.SpringApplication; // Main entry point for Spring Boot apps
import org.springframework.boot.autoconfigure.SpringBootApplication; // Enables auto-configuration
import java.io.FileInputStream;    // For reading the serviceAccountKey.json file
import java.io.IOException;        // For handling file-related exceptions

// -------------------------------------------------------------------------
// Marks this class as a Spring Boot application (enables auto-configuration)
// -------------------------------------------------------------------------
@SpringBootApplication
public class Application {

    // -------------------------------------------------------------------------
    // Main method - the starting point of the Spring Boot application
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        // Start the Spring Boot application (loads context, beans, etc.)
        SpringApplication.run(Application.class, args);

        try {
            // -----------------------------------------------------------------
            // Load the Firebase Admin SDK service account key file
            // -----------------------------------------------------------------
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");

            // -----------------------------------------------------------------
            // Build FirebaseOptions object with credentials and database URL
            // -----------------------------------------------------------------
            FirebaseOptions options = FirebaseOptions.builder()
                    // Load credentials from the service account JSON file
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    // Set the Realtime Database URL (must match your Firebase project)
                    .setDatabaseUrl("https://semband3-default-rtdb.firebaseio.com/")
                    .build();

            // -----------------------------------------------------------------
            // Initialize Firebase App with the options (only once per JVM)
            // -----------------------------------------------------------------
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase has been initialized successfully.");
            } else {
                System.out.println("Firebase app already initialized.");
            }

        } catch (IOException e) {
            // -----------------------------------------------------------------
            // Handle errors when loading the service account key
            // Example: file not found, invalid permissions, etc.
            // -----------------------------------------------------------------
            System.err.println("Failed to initialize Firebase: " + e.getMessage());
        }
    }
}

