package org.example;

public class GardenApp extends Appartment {
    // Garden area in square meters
    private int gardenArea;

    // Constructor
    public GardenApp(String owner, int floor, int numApp, int area, int gardenArea) {
        super(floor, numApp, area);
        this.gardenArea = gardenArea;
    }

    // Getter and setter
    public int getGardenArea() {
        return gardenArea;
    }

    public void setGardenArea(int gardenArea) {
        this.gardenArea = gardenArea;
    }

    // toString method
    @Override
    public String toString() {
        return super.toString() + ", GardenApp{" +
                "gardenArea=" + gardenArea +
                '}';
    }

    @Override
    public double getPrice() {
        return super.getPrice() + gardenArea * COST_GARDEN;
    }
}

