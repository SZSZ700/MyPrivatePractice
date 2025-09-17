package org.example;

// Class representing a Meeting that holds virtual rooms
public class Meeting {
    // Head of a linked list of VRoom nodes
    private Node<VRoom> rooms;

    // Constructor: initializes the meeting with no rooms
    public Meeting() {
        this.rooms = null;
    }

    // Getter: returns the list of rooms
    public Node<VRoom> getRooms() {
        return rooms;
    }

    // Setter: sets the list of rooms
    public void setRooms(Node<VRoom> rooms) {
        this.rooms = rooms;
    }

    // Override of toString: builds a string representation of all rooms
    @Override
    public String toString() {
        // StringBuilder used for efficient string concatenation
        var str = new StringBuilder();
        // Temporary pointer to traverse the linked list of rooms
        var pos = this.rooms;

        // Iterate through all rooms
        while (pos != null) {
            // Append the current room's value
            str.append(pos.getValue());
            // Add a space after each room
            str.append(" ");
            // Move to the next room in the list
            pos = pos.getNext();
        }

        // Return the final string
        return str.toString();
    }
}