package org.example;

public class Package extends MailItem {
    private int weight;
    private boolean isBreakable;

    public Package(String name, String address, int weight, boolean isBreakable, double price) {
        super(name, address, price);
        this.weight = weight;
        this.isBreakable = isBreakable;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isBreakable() {
        return isBreakable;
    }

    public void setBreakable(boolean breakable) {
        isBreakable = breakable;
    }

    @Override
    public double getRealPrice() {
        double finalPrice = price;
        if (weight > 5) {
            finalPrice += (weight - 5) * 3;
        }
        if (isBreakable) {
            finalPrice += 15;
        }
        return finalPrice;
    }

    @Override
    public String toString() {
        return "Package{" +
                "weight=" + weight +
                ", isBreakable=" + isBreakable +
                "} " + super.toString();
    }
}
