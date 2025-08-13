package org.example;

public class Appartment {
    // Price per square meter for apartment base
    protected static final int COST_APP = 1000;

    // Price per square meter for terrace (if applicable)
    protected static final int COST_TERRACE = 300;

    // Price per square meter for garden (if applicable)
    protected static final int COST_GARDEN = 50;

    // Name of the apartment owner
    private String owner;

    // Floor number
    private int floor;

    // Apartment number
    private int numApp;

    // Apartment area in square meters
    private int area;

    // Constructor
    public Appartment(int floor, int numApp, int area) {
        this.owner = "Free";
        this.floor = floor;
        this.numApp = numApp;
        this.area = area;
    }

    // Getters and setters
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getNumApp() {
        return numApp;
    }

    public void setNumApp(int numApp) {
        this.numApp = numApp;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    // toString method
    @Override
    public String toString() {
        return "Appartment{" +
                "owner='" + owner + '\'' +
                ", floor=" + floor +
                ", numApp=" + numApp +
                ", area=" + area +
                '}';
    }

    public double getPrice() {
        return area * COST_APP;
    }
}
