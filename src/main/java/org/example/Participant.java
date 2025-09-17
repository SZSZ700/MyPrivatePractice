package org.example;

// Class representing a participant in a meeting
public class Participant {
    // The participant's name
    private String name;
    // The participant's department number (between 1–10)
    private int dep;

    // Constructor: initializes a participant with name and department
    public Participant(String n, int d) {
        this.name = n;
        this.dep = d;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for department number
    public int getDep() {
        return dep;
    }

    // Setter for department number
    public void setDep(int dep) {
        this.dep = dep;
    }

    // Override of toString: returns a string representation of the participant
    @Override
    public String toString() {
        return "Participant{" +
                "name='" + name + '\'' +
                ", dep=" + dep +
                '}';
    }
}