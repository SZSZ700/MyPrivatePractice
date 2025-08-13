package org.example;

// מחלקת טריפל - חבילה משולבת
public class Triple extends Cable{
    private Cable Hpackage; // חבילה בסיסית או מורחבת
    private Phone phone; // טלפון
    private double priceOfInternet; // מחיר חבילת אינטרנט

    // בנאי
    public Triple(double subscriptionPayment, Cable hpackage, Phone phone, double priceOfInternet) {
        super(subscriptionPayment); // אתחול מחיר מנוי בסיסי
        Hpackage = hpackage; // אתחול חבילת כבלים בסיסית או מורחבת
        this.phone = phone; // אתחול טלפון
        this.priceOfInternet = priceOfInternet; // אתחול מחיר חבילת רשת
    }

    public double getPrice(){
        // חישוב מחיר טריפל: מחיר חבילת כבלים + מחיר טלפון + מחיר חבילת גלישה באינטרנט
        var finalPrice = this.Hpackage.getPrice() + this.phone.getPrice() + this.priceOfInternet;
        // לקוח הרוכש חבילת טריפל מקבל 10% הנחה
        return finalPrice - 0.10 * finalPrice;
    }

    public Cable getHpackage() { return Hpackage; } // קבלת חבילת הכבלים

    public void setHpackage(Cable hpackage) { Hpackage = hpackage; } // קביעת חבילת הקבלים

    public Phone getPhone() { return phone; } // קבלת הטלפון

    public void setPhone(Phone phone) { this.phone = phone; } // קביעת טלפון

    public double getPriceOfInternet() { return priceOfInternet; } // קבלת מחיר חבילת הגלישה

    // קביעת מחיר חבילת הגלישה
    public void setPriceOfInternet(double priceOfInternet) { this.priceOfInternet = priceOfInternet; }

    @Override
    public String toString() {
        return "Triple{" +
                "Hpackage=" + Hpackage +
                ", phone=" + phone +
                ", priceOfInternet=" + priceOfInternet +
                "} " + super.toString();
    }
}
