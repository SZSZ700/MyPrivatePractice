package org.example;

public class Motorcycle {
    private int speed;

    public Motorcycle (int s) {
        speed = s;
    }

    public int getSpeed () {
        return speed;
    }

    public boolean equals (Object other) {
        return ((other != null) &&
                (other instanceof Motorcycle) &&
                (speed == ((Motorcycle)other).speed));
    }
}
