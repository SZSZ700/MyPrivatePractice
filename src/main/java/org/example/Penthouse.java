package org.example;

public class Penthouse extends Appartment {
    // Number of terraces
    private int numTerr;

    // Total terrace area
    private int terraceArea;

    // Whether it has sea view
    private boolean seaView;

    // Constructor
    public Penthouse(String owner, int floor, int numApp, int area, int numTerr, int terraceArea, boolean seaView) {
        super(floor, numApp, area);
        this.numTerr = numTerr;
        this.terraceArea = terraceArea;
        this.seaView = seaView;
    }

    // Getters and setters
    public int getNumTerr() {
        return numTerr;
    }

    public void setNumTerr(int numTerr) {
        this.numTerr = numTerr;
    }

    public int getTerraceArea() {
        return terraceArea;
    }

    public void setTerraceArea(int terraceArea) {
        this.terraceArea = terraceArea;
    }

    public boolean isSeaView() {
        return seaView;
    }

    public void setSeaView(boolean seaView) {
        this.seaView = seaView;
    }

    // toString method
    @Override
    public String toString() {
        return super.toString() + ", Penthouse{" +
                "numTerr=" + numTerr +
                ", terraceArea=" + terraceArea +
                ", seaView=" + seaView +
                '}';
    }

    @Override
    public double getPrice() {
        double basePrice = super.getPrice() + terraceArea * COST_TERRACE;
        if (!seaView) {
            basePrice *= 0.95; // apply 5% discount
        }
        return basePrice;
    }
}

