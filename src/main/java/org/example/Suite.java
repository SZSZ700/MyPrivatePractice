package org.example;

public class Suite extends Room{
    //if there is jacuzzi
    private boolean jacuzzi;

    public Suite(int num, int beds, double price, boolean jacuzzi) {
        super(num, beds, price);
        this.jacuzzi = jacuzzi;
    }

    public boolean isJacuzzi() {
        return jacuzzi;
    }

    public void setJacuzzi(boolean jacuzzi) {
        this.jacuzzi = jacuzzi;
    }

    @Override
    public String toString() {
        return "Suite{" +
                "jacuzzi=" + jacuzzi +
                "} " + super.toString();
    }

    @Override
    public int calaCleanTime(){
        //call method to calc basic room cleaning time
        int baseCleanTime = super.calaCleanTime();
        //time to clean jacuzzi
        int jacuzziCleanTime = 20;

        //if thers is jacuzzi
        if (this.jacuzzi){ baseCleanTime += jacuzziCleanTime; }

        //return the total time to clean suit room
        return baseCleanTime;
    }
}
