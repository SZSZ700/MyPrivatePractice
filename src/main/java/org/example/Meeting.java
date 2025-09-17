package org.example;
import java.util.LinkedList;
import java.util.Scanner;

// Class representing a Meeting that holds virtual rooms
public class Meeting {
    // Head of a linked list of VRoom nodes
    private Node<VRoom> rooms;
    private int size;  // Number of rooms currently in the meeting

    // Constructor: initializes the meeting with no rooms
    public Meeting() {
        this.size = 0;
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


    // Method that divides participants into virtual rooms
    // based on the given room size ✔️
    public void divide(int number) {
        // Scanner to read user input
        var scanner = new Scanner(System.in);
        // Flag to detect termination condition
        var notValid = false;
        // Counter for room numbers
        var i = 1;

        // 🔃 Infinite loop to keep creating rooms until stop condition
        while (true) {
            // Create a new virtual room with current number
            VRoom room = new VRoom(i++);

            // 🔃 Fill the room with the required number of participants
            for (int j = 0; j < number; j++) {
                // Prompt for participant name
                System.out.println("enter your name: ");
                var name = scanner.next();
                // Prompt for participant department number
                System.out.println("enter your department number: ");
                var num = scanner.nextInt();

                // ⚠️ If department number is 0 → stop reading input ⚠️
                // so the new participant will not be added to the current vroom
                if (num == 0) { notValid = true; break; }

                // Create new participant object
                var participant = new Participant(name, num);
                // Add the participant to the current virtual room
                room.add(participant);
            } // end of 🔃


            // ⤵️ firstly: add the new virtual room to the list ⤵️ //
            // Wrap the new room in a Node object
            var roomToAdd = new Node<VRoom>(room);

            // Add the room node to the linked list of rooms in this meeting
            if (this.rooms == null) { this.rooms = roomToAdd; }
            else {
                roomToAdd.setNext(this.rooms);
                this.rooms = roomToAdd;
            }

            this.size++;

            // ⤵️ then (if needed): stop reading another input ⤵️ //
            // ⚠️ Stop condition: user entered dep = 0 ⚠️
            if (notValid) { return; }

        } // end of 🔃
    }

    // Method that prints the missing units for each virtual room
    public void printMissingUnits() {
        // Pointer to traverse the linked list of virtual rooms
        var pos = this.rooms;

        // 🔃 Iterate over the list of virtual rooms
        while (pos != null) {
            // Counter array to track departments (1–10)
            var monim = new int[10];
            // Get the current virtual room
            var currentRoom = pos.getValue();
            // Get the queue of participants in this room
            var queueOfparticipants = currentRoom.getParts();
            // Temporary queue for restoring participants later
            var restorationParticipantsQueue = new LinkedList<Participant>();

            // 🔃 Iterate over the current room’s queue of participants
            while (!queueOfparticipants.isEmpty()) {
                // Remove current participant from the queue
                var currentParticipant = queueOfparticipants.poll();
                // Get the department number of this participant
                var departmentNumberOfCurrentParticipant = currentParticipant.getDep();
                // Increment the counter for this department
                monim[departmentNumberOfCurrentParticipant - 1]++;
                // Save participant for later restoration
                restorationParticipantsQueue.offer(currentParticipant);
            }

            // Restore all participants back into the original queue
            while (!restorationParticipantsQueue.isEmpty()) {
                queueOfparticipants.offer(restorationParticipantsQueue.poll());
            }

            // Print room number
            System.out.println("For virtual room number: " + (currentRoom.getNum()));
            // Print missing department numbers
            System.out.println("The department numbers missing in this room are:");
            for (int i = 0; i < monim.length; i++) {
                if (monim[i] == 0) { System.out.println(i + 1 + ", "); }
            }

            // Move to next room in the linked list
            pos = pos.getNext();
        }
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