package org.example;

// מחלקת לקוח
public class Clientc {
    private String clientName; // שם לקוח
    private Cable cable; // חבילת כבלים

    // בנאי
    public Clientc(String clientName, Cable cable) {
        this.clientName = clientName;
        this.cable = cable;
    }

    public double getPrice(){ return cable.getPrice(); } // קבלת מחיר החבילה

    public String getClientName() { return clientName; } // קבלת שם לקוח

    public void setClientName(String clientName) { this.clientName = clientName; } // קביעת שם לקוח

    public Cable getCable() { return cable; } // קבלת חבילת כבלים

    public void setCable(Cable cable) { this.cable = cable; } // קביעת חבילת כבלים

    @Override
    public String toString() {
        return "Clientc{" +
                "clientName='" + clientName + '\'' +
                ", cable=" + cable +
                '}';
    }
}
