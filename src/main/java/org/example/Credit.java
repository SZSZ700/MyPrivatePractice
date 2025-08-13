package org.example;

// מחלקה שמייצגת תשלום בכרטיס אשראי, יורשת מ-Payment
public class Credit extends Payments {
    // מספר כרטיס האשראי
    private String cardNumber;
    // תוקף הכרטיס
    private String expiryDate;
    // תאריך החיוב
    private String chargeDate;

    // בנאי שמקבל את כל פרטי האשראי
    public Credit(double amount, String cardNumber, String expiryDate, String chargeDate) {
        super(amount);
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.chargeDate = chargeDate;
    }

    // פעולה שמחזירה את מספר כרטיס האשראי
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setChargeDate(String chargeDate) {
        this.chargeDate = chargeDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getChargeDate() {
        return chargeDate;
    }

    // מחזיר תיאור של תשלום באשראי
    @Override
    public String getDetails() {
        return "Credit: amount=" + amount + ", card=" + cardNumber +
                ", expiry=" + expiryDate + ", charge date=" + chargeDate;
    }
}

