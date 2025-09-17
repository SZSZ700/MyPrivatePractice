package org.example;
import java.util.*;

// Class representing a virtual room in the meeting system
public class VRoom {
    // The room number (positive integer)
    private int num;
    // A queue of participants in this room
    Queue<Participant> parts;

    // Constructor: initializes a room with a given number and an empty queue of participants
    public VRoom(int p) {
        this.num = p;
        this.parts = new LinkedList<>();
    }

    // Method to add a participant to the room's queue
    public void add(Participant participant) {
        this.parts.offer(participant);
    }

    // Getter for room number
    public int getNum() {
        return num;
    }

    // Setter for room number
    public void setNum(int num) {
        this.num = num;
    }

    // Getter for the queue of participants
    public Queue<Participant> getParts() {
        return parts;
    }

    // Setter for the queue of participants
    public void setParts(Queue<Participant> parts) {
        this.parts = parts;
    }

    // Override of toString: returns a string representation of the room
    @Override
    public String toString() {
        return "VRoom{" +
                "num=" + num +
                ", parts=" + parts +
                '}';
    }
}