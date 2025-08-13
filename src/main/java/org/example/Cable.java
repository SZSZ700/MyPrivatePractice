package org.example;

// מחלקת כבלים
public class Cable {
    private double subscriptionPayment; // מחיר קבוע למנוי

    public Cable(double subscriptionPayment) { this.subscriptionPayment = subscriptionPayment; }

    // פונק המחזירה מחיר קבוע למינוי (בסיסי)
    public double getPrice(){ return this.subscriptionPayment; }

    // עדכון מחיר מנוי
    public void setPrice(double price){ this.subscriptionPayment = price; }

    @Override
    public String toString() {
        return "Cable{" +
                "subscriptionPayment=" + subscriptionPayment +
                '}';
    }
}
