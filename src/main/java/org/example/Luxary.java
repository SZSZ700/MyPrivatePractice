package org.example;

public class Luxary extends Appartment {
    // Terrace area in square meters
    private int terraceArea;

    // Constructor
    public Luxary(String owner, int floor, int numApp, int area, int terraceArea) {
        super(floor, numApp, area);
        this.terraceArea = terraceArea;
    }

    // Getter and setter
    public int getTerraceArea() {
        return terraceArea;
    }

    public void setTerraceArea(int terraceArea) {
        this.terraceArea = terraceArea;
    }

    // toString method
    @Override
    public String toString() {
        return super.toString() + ", Luxary{" +
                "terraceArea=" + terraceArea +
                '}';
    }

    @Override
    public double getPrice() {
        return super.getPrice() + terraceArea * COST_TERRACE;
    }
}

