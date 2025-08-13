package org.example;

public class Letter extends MailItem {
    private boolean writenMsg;//דואר רשום-אמת, דואר רגיל-שקר
    private char size;//גודל מכתב Big Small Large

    public Letter(String senderName, String address, boolean writenMsg, char size, double price) {
        super(senderName,address,price);
        this.writenMsg = writenMsg;
        this.size = size;
    }

    public boolean isWritenMsg() {
        return writenMsg;
    }

    public void setWritenMsg(boolean writenMsg) {
        this.writenMsg = writenMsg;
    }

    public char getSize() {
        return size;
    }

    public void setSize(char size) {
        this.size = size;
    }

    @Override
    public double getRealPrice(){
        double finalPrice = this.price;
        if (size == 'S'){
            finalPrice -= 2;
        } else if (size == 'L') {
            finalPrice += 3;
        }
        if (writenMsg){
            finalPrice += 10;
        }
        return finalPrice;
    }

    @Override
    public String toString() {
        return "Letter{" +
                "writenMsg=" + writenMsg +
                ", size=" + size +
                "} " + super.toString();
    }
}
