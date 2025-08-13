package org.example;

// מחלקת בסיס מופשטת שמייצגת תשלום כללי (ללא תלות בסוגו)
public abstract class Payments {
    // סכום התשלום
    protected double amount;

    // בנאי של תשלום עם סכום
    public Payments(double amount) {
        this.amount = amount;
    }

    // מחזיר את סכום התשלום
    public double getAmount() {
        return amount;
    }

    // פעולה אבסטרקטית להחזרת תיאור של התשלום
    public abstract String getDetails();
}
