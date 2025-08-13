package org.example;

import java.util.ArrayList;
import java.util.List;
// מחלקה שמייצגת קנייה בחנות
public class Buy {
    // תאריך הקנייה
    private String date;
    // הסכום הכולל לתשלום
    private double totalAmount;
    // רשימת אמצעי התשלום
    private Node<Payments> payments;

    // בנאי שמקבל תאריך וסכום כולל
    public Buy(String date, double totalAmount) {
        this.date = date;
        this.totalAmount = totalAmount;
        this.payments = null;
    }

    // פעולה להוספת תשלום לרשימה
    public void addPayment(Payments payment) { this.payments = new Node<>(payment, this.payments.getNext()); }

    // מחזירה את רשימת התשלומים
    public Node<Payments> getPayments() {
        return this.payments;
    }

    // מחזירה את תאריך הקנייה
    public String getDate() {
        return this.date;
    }

    // מחזירה את הסכום הכולל לתשלום
    public double getTotalAmount() {
        return this.totalAmount;
    }

    // פעולה שבודקת האם סכום התשלומים שווה לסכום הקנייה
    public boolean isValid() {
        double sum = 0; // צובר
        var pos = this.payments; // פויינטר לרשימה

        // איטרציה על הרשימה
        while (pos != null) {
            // צבירת איבר נוכחי
            sum += pos.getValue().getAmount();
            // התקדמות לחולייה הבאה
            pos = pos.getNext();
        }

        return Math.abs(sum - totalAmount) < 0.001; // בדיקה אם סכום התשלומים שווה לסכום הקנייה
    }

    // פעולה שבודקת אם אחד מאמצעי התשלום הוא בכרטיס אשראי מסוים
    public boolean containsCard(String cardNumber) {
        var pos = this.payments; // פויינטר לרשימה

        // איטרציה על הרשימה
        while (pos != null) {
            // אם אחד מאמצעי התשלום הוא אשראי, ובעל אותו מספר כרטיס שהתקבל בפונקצייה כפרמטר, יוחזר אמת
            if (pos.getValue() instanceof Credit &&
                    ((Credit) pos.getValue()).getCardNumber().equals(cardNumber)) {
                return true;
            }

            // התקדמות לחולייה הבאה
            pos = pos.getNext();
        }

        return false; // החזר שקר לא נמצא כרטיס אשראי העונה על התנאים
    }

    // פעולה שבודקת אם כל התשלום בוצע במזומן בלבד
    public boolean isCashOnly() {
        var pos = this.payments;  // פויינטר לרשימה

        // איטרציה על הרשימה
        while (pos != null) {
            // אם נמצא אמצעי תשלום אחר ממזומן, יוחזר שקר
            if (!(pos.getValue() instanceof Cash)) return false;

            // התקדמות לחולייה הבאה
            pos = pos.getNext();
        }

        return true; //יוחזר אמת, כל הקנייה בוצעה במזומן
    }

    // פעולה להדפסת כל פרטי הקנייה
    public void printDetails() {
        System.out.println("Date: " + date + ", Total: " + totalAmount);
        var pos = this.payments; // פויינטר לרשימה

        // איטרציה על הרשימה
        while (pos != null) {
            System.out.println("  - " + pos.getValue().getDetails());
            // התקדמות לחולייה הבאה
            pos = pos.getNext();
        }
    }
}

