// Declare the package this class belongs to.
package org.example.personalpractice.Q4;

// Define a simple data class that represents a restaurant table.
public class Table {

    // Store the unique table number (identifier).
    private int num;

    // Store how many diners the table can hold (2/4/8 by your model).
    private int places;

    // Store how many chairs are currently free (changes after seating).
    int free;

    // Provide a no-arg constructor so frameworks (like Spring) can instantiate it easily.
    public Table() { }

    // Initialize a table with all of its fields explicitly.
    public Table(int num, int places, int free) {
        // Save the given table number.
        this.num = num;
        // Save the given capacity.
        this.places = places;
        // Save the given free seats count.
        this.free = free;
    }

    // Return the table number.
    public int getNum() { return this.num; }

    // Update the table number.
    public void setNum(int num) { this.num = num; }

    // Return the table capacity.
    public int getPlaces() { return this.places; }

    // Update the table capacity.
    public void setPlaces(int places) { this.places = places; }

    // Return how many seats are currently free.
    public int getFree() { return this.free; }

    // Update how many seats are currently free.
    public void setFree(int free) { this.free = free; }

    // Convert this object into a readable string for debugging/logging.
    @Override
    public String toString() {
        // Build a descriptive string that includes all fields.
        return "Table{" +
                // Include table number.
                "num=" + this.num +
                // Include capacity.
                ", places=" + this.places +
                // Include free seats.
                ", free=" + this.free +
                // Close the object format.
                '}';
    }
}
