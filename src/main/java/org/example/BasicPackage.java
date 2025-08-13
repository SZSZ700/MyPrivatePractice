package org.example;

// מחלקת חבילה בסיסית
public class BasicPackage extends Cable{
    private double price; // מחיר חבילה בסיסית

    // בנאי
    public BasicPackage(double subscriptionPayment, double price) {
        // אתחול מחיר קבוע למנוי
        super(subscriptionPayment);
        this.price = price;
    }

    // מחיר קבוע למנוי + מחיר חבילה בסיסית
    @Override
    public double getPrice() { return this.price + super.getPrice(); }

    // קביעת מחיר חבילה בסיסית
    @Override
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "BasicPackage{" +
                "price=" + price +
                "} " + super.toString();
    }
}
