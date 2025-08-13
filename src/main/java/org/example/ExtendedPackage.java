package org.example;
import java.util.Arrays;

// מחלקת חבילה מורחבת
public class ExtendedPackage extends BasicPackage{
    private double price; // מחיר קבוע עבור כל ערוץ נוסף
    private int numOfChannels; // מס הערוצים
    private String[] namesOfChannels; // מערך שמות הערוצים

    // בנאי
    public ExtendedPackage(double subscriptionPayment, double basicPrice, double price, int numOfChannels, String[] namesOfChannels) {
        // אתחול מחיר קבוע למנוי, אתחול מחיר חבילה בסיסי
        super(subscriptionPayment, basicPrice);
        this.price = price; // אתחול מחיר קבוע עבור כל ערוץ נוסף
        this.numOfChannels = numOfChannels; // אתחול מס הערוצים
        this.namesOfChannels = namesOfChannels; // אתחול מערך שמות הערוצים
    }


    // מחיר קבוע למנוי + מחיר חבילה בסיסית + (מחיר עבור ערוץ * מס הערוצים הנוספים)
    public double getPrice() { return super.getPrice() + this.price * this.numOfChannels; }

    public void setPrice(double price) { this.price = price; } // קביעת מחיר ערוץ

    public int getNumOfChannels() { return numOfChannels; } // קבלת מס הערוצים


    public void setNumOfChannels(int numOfChannels) { this.numOfChannels = numOfChannels; } // קביעת מס ערוצים

    public String[] getNamesOfChannels() { return namesOfChannels; } // קבלת מערך שמות הערוצים

    // קביעת מערך שמות הערוצים
    public void setNamesOfChannels(String[] namesOfChannels) { this.namesOfChannels = namesOfChannels; }

    @Override
    public String toString() {
        return "ExtendedPackage{" +
                "price=" + price +
                ", numOfChannels=" + numOfChannels +
                ", namesOfChannels=" + Arrays.toString(namesOfChannels) +
                '}';
    }
}
