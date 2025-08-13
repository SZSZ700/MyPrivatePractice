package org.example;

public class Vector {
    private double[] coord;

    // Constructor
    public Vector(double x, double y, double z) {
        this.coord = new double[]{x, y, z};
    }

    //החזרת הקורדינציות
    public double getX() {
        return coord[0];
    }

    public double getY() {
        return coord[1];
    }

    public double getZ() {
        return coord[2];
    }

    //חיבור בין שני וקטורים
    public Vector add(Vector other) {
        return new Vector(
                this.coord[0] + other.coord[0],
                this.coord[1] + other.coord[1],
                this.coord[2] + other.coord[2]
        );
    }

    //חיסור בין שני וקטורים
    public Vector minus(Vector other) {
        return new Vector(
                this.coord[0] - other.coord[0],
                this.coord[1] - other.coord[1],
                this.coord[2] - other.coord[2]
        );
    }

    // Method to compute the dot product of this vector and another vector
    public double dotProduct(Vector other) {
        return this.coord[0] * other.coord[0] +
                this.coord[1] * other.coord[1] +
                this.coord[2] * other.coord[2];
    }

    //אורך וקטור-משפט פיתגורס
    //   ___________________
    // |/a1^2 + a2^2 + a3^2 = |a|
    public double V_length() {
        return Math.sqrt(
                coord[0] * coord[0] +
                        coord[1] * coord[1] +
                        coord[2] * coord[2]
        );
    }

    @Override
    public String toString() {
        return "(" + coord[0] + ", " + coord[1] + ", " + coord[2] + ")";
    }
}
