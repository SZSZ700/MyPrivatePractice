package org.example;

public class Room {
    //number of room
    private int num;
    //num of beds in room
    private int beds;
    //price per night
    private double price;

    public Room(int num, int beds, double price) {
        this.num = num;
        this.beds = beds;
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Room{" +
                "num=" + num +
                ", beds=" + beds +
                ", price=" + price +
                '}';
    }

    public int calaCleanTime(){
        //time to clean bed shits
        int bedShits = 5;
        //time to clean floor
        int cleanFloor = 10;
        //time to hover the carpet
        int hooverCarpet = 15;

        //return the total time to clean regular room
        return this.beds * bedShits + cleanFloor + hooverCarpet;
    }
}
