package org.example;

// מחלקת טלפון
public class Phone extends Cable {
    private double price; // מחיר קבוע לשיחה
    private int numberOfCalls; // מספר שיחות

    public Phone(double subscriptionPayment, double price, int numberOfCalls) {
        super(subscriptionPayment); // אתחול מחיקר קבוע למנוי
        this.price = price; // אתחול מחיר קבוע לשיחה
        this.numberOfCalls = numberOfCalls; // אתחול מס השיחות
    }

    // מחיר מנוי + (מחיר קבוע לשיחה * מס שיחות שבוצעו בפועל)
    public double getPrice() { return this.price * this.numberOfCalls; }

    // קביעת מחיר קבוע לשיחה
    public void setPrice(double price) {
        this.price = price;
    }

    // קבלת מספר השיחות אותן ביצע הלקוח
    public int getNumberOfCalls() {
        return numberOfCalls;
    }

    // קביעת מס השיחות אותן ביצע הלקוח
    public void setNumberOfCalls(int numberOfCalls) {
        this.numberOfCalls = numberOfCalls;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "price=" + price +
                ", numberOfCalls=" + numberOfCalls +
                '}';
    }
}
