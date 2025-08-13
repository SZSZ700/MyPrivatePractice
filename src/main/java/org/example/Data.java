package org.example;

public class Data {
    private boolean free;
    private int size;

    //constructor
    public Data (int size) {
        this.free = true;
        this.size = size;
    }

    public boolean isFree(){ return this.free; }

    public int getSize() { return this.size; }

    public void setFree(boolean free){this.free = free; }

    public void setSize(int size){ this.size = size;}

}
