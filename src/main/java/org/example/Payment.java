package org.example;

public class Payment {
    //יום תדלוק
    private int day;
    //מס רכב
    private int plateNumber;
    //שם הנהג
    private String DriverName;
    //כמות הדלק שתודלקה
    private double howMuch;

    public Payment(int day, int plateNumber, String driverName, double howMuch) {
        this.day = day;
        this.plateNumber = plateNumber;
        DriverName = driverName;
        this.howMuch = howMuch;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(int plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public double getHowMuch() {
        return howMuch;
    }

    public void setHowMuch(double howMuch) {
        this.howMuch = howMuch;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "day=" + day +
                ", plateNumber=" + plateNumber +
                ", DriverName='" + DriverName + '\'' +
                ", howMuch=" + howMuch +
                '}';
    }
}
