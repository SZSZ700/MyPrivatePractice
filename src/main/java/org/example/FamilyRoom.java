package org.example;

public class FamilyRoom extends Room{
    //if has been added baby bed to the room
    private boolean babyBed;
    //is there is balcony
    private boolean balcony;

    public FamilyRoom(int num, int beds, double price, boolean babyBed, boolean balcony) {
        super(num, beds, price);
        this.babyBed = babyBed;
        this.balcony = balcony;
    }

    public boolean isBabyBed() {
        return babyBed;
    }

    public void setBabyBed(boolean babyBed) {
        this.babyBed = babyBed;
    }

    public boolean isBalcony() {
        return balcony;
    }

    public void setBalcony(boolean balcony) {
        this.balcony = balcony;
    }

    @Override
    public String toString() {
        return "FamilyRoom{" +
                "babyBed=" + babyBed +
                ", balcony=" + balcony +
                "} " + super.toString();
    }

    @Override
    public int calaCleanTime(){
        //call method to calc basic room cleaning time
        int baseCleanTime = super.calaCleanTime();
        //time for cleaning balcony
        int balconyClean = 5;
        //time for cleaning babey bed
        int babyBedCleanTime = 5;

        //if thers is babey bed add 5 mins
        if (this.babyBed){ baseCleanTime += babyBedCleanTime; }

        //if thers is balcony add 5 mins
        if (this.balcony){ baseCleanTime += balconyClean; }

        //return the total time to clean family room
        return baseCleanTime;
    }
}
