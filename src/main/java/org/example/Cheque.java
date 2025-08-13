package org.example;

// מחלקה שמייצגת תשלום בצ'ק, יורשת מ-Payment
public class Cheque extends Payments {
    // מספר הצ'ק
    private String chequeNumber;
    // שם הבנק
    private String bankName;
    // תאריך הצ'ק
    private String chequeDate;

    // בנאי שמקבל את כל פרטי הצ'ק
    public Cheque(double amount, String chequeNumber, String bankName, String chequeDate) {
        super(amount);
        this.chequeNumber = chequeNumber;
        this.bankName = bankName;
        this.chequeDate = chequeDate;
    }

    // פעולה שמחזירה את מספר ההמחאה (לשימוש כללי)
    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    // מחזיר תיאור של תשלום בצ'ק
    @Override
    public String getDetails() {
        return "Cheque: amount=" + amount + ", number=" + chequeNumber +
                ", bank=" + bankName + ", date=" + chequeDate;
    }
}

