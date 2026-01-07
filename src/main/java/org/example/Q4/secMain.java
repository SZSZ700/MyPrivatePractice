// Declare the package this class belongs to.
package org.example.Q4;

// Import Spring's Java-config application context implementation.
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

// Define a simple entry-point class to run the Spring context and test the restaurant.
class tryMain {

    // Program entry point.
    public static void main(String[] args) {

        // Create a Spring ApplicationContext using the Java configuration class (AppConfig).
        var ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        // Ask Spring for the managed Restaurant bean (singleton by default).
        var r = ctx.getBean(Restaurant.class);

        // Create and enqueue a client group of 4 diners using a prototype Client bean.
        r.addClient("Avi", 4);

        // Create and enqueue a client group of 2 diners using a prototype Client bean.
        r.addClient("Dana", 2);

        // Attempt to seat the next possible client and print whether seating succeeded.
        System.out.println(r.seatNextClient());

        // Close the context to release resources and trigger any shutdown callbacks.
        ctx.close();
    }
}
