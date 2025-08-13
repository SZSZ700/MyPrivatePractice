package org.example;

public class Doctor {
    protected String name;// שם
    protected String specialization;// התמחות
    protected int numOfPatients; // חולים מספר

    public Doctor(String name, String spec) {
        this.name = name;
        this.specialization = spec;
        this.numOfPatients = 0;
    }

    public Doctor(String name, String spec, int num) {
        this.name = name;
        this.specialization = spec;
        this.numOfPatients = num;
    }

    public Doctor(Doctor other) {
        this.name = other.name;
        this.specialization = other.specialization;
        this.numOfPatients = other.numOfPatients;
    }

    public void addPatients(int num) {
        if(numOfPatients + num >=0)
            this.numOfPatients += num;
    }

    public String toString(){
        return "Doctor:" + name + ", " + specialization+ ","
                +numOfPatients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getNumOfPatients() {
        return numOfPatients;
    }

    public void setNumOfPatients(int numOfPatients) {
        this.numOfPatients = numOfPatients;
    }
}
