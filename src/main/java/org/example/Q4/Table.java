package org.example.Q4;

public class Table {
    private int num; // table number
    // places == 2 ? => small, places == 4? => medium, places == 8? => big
    private int places; // how many diners can seat on the table
    int free; // num of empty chairs

    public Table(int num, int places, int free) {
        this.num = num;
        this.places = places;
        this.free = free;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    @Override
    public String toString() {
        return "Table{" +
                "num=" + num +
                ", places=" + places +
                ", free=" + free +
                '}';
    }
}
