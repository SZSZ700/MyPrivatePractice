package org.example;

public class Lecturer extends Employee{
    private String specialization; // התמחות

    public Lecturer(String name, String specialization) {
        super(name);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Lecturer{" +
                "specialization='" + specialization + '\'' +
                "} " + super.toString();
    }
}
