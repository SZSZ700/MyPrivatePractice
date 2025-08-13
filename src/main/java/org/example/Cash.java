package org.example;

// מחלקה שמייצגת תשלום במזומן, יורשת מ-Payment
public class Cash extends Payments {
    // בנאי שמקבל סכום בלבד
    public Cash(double amount) { super(amount); }

    // מחזיר תיאור של תשלום מזומן
    @Override
    public String getDetails() { return "Cash payment: " + amount; }
}
