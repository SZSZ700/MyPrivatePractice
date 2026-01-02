// Declare the package this class belongs to.
package org.example.Q4;

// Define a simple data class that represents a client/group waiting to be seated.
public class Client {

    // Store the client name (for identification / debugging).
    private String name;

    // Store how many diners are in this client group.
    private int diners;

    // Provide a no-arg constructor so frameworks (like Spring) can instantiate it easily.
    public Client() { }

    // Initialize a client with name and number of diners.
    public Client(String name, int diners) {
        // Save the given name.
        this.name = name;
        // Save the given group size.
        this.diners = diners;
    }

    // Return the client's name.
    public String getName() { return this.name; }

    // Update the client's name.
    public void setName(String name) { this.name = name; }

    // Return the number of diners in the group.
    public int getDiners() { return this.diners; }

    // Update the number of diners in the group.
    public void setDiners(int diners) { this.diners = diners; }

    // Convert this object into a readable string for debugging/logging.
    @Override
    public String toString() {
        // Build a descriptive string that includes all fields.
        return "Client{" +
                // Include the name value with quotes.
                "name='" + this.name + '\'' +
                // Include the diners count.
                ", diners=" + this.diners +
                // Close the object format.
                '}';
    }
}
