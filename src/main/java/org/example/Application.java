// Define the package of the Spring Boot application
package org.example;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
    }
}

