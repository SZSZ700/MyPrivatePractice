package org.example;

public class Car {
    private int speed;

    public Car (int s) {
        speed = s;
    }

    public int getSpeed () {
        return speed;
    }

    public boolean equals(Car other) {
        return (other!=null) && (speed == other.speed);
    }
}
