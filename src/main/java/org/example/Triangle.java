package org.example;

public class Triangle {
    private Vector v1;
    private Vector v2;
    private Vector v3;

    // Constructor
    public Triangle(Vector vertexA, Vector vertexB, Vector vertexC) {
        this.v1 = vertexA;
        this.v2 = vertexB;
        this.v3 = vertexC;
    }

    // Getters
    public Vector getV1() {
        return v1;
    }

    public Vector getV2() {
        return v2;
    }

    public Vector getV3() {
        return v3;
    }

    //היקף משולש תלת מימד
    public double calcPerimeter() {
        double ab = v1.minus(v2).V_length();
        double bc = v2.minus(v3).V_length();
        double ca = v3.minus(v1).V_length();
        return ab + bc + ca;
    }

    //שטח משולש תלת מימד
    public double calcArea() {
        Vector AB = v2.minus(v1);
        Vector AC = v3.minus(v1);
        Vector crossProduct = new Vector(
                AB.getY() * AC.getZ() - AB.getZ() * AC.getY(),
                AB.getZ() * AC.getX() - AB.getX() * AC.getZ(),
                AB.getX() * AC.getY() - AB.getY() * AC.getX()
        );
        return 0.5 * crossProduct.V_length();
    }

    @Override
    public String toString() {
        return "Triangle3D: \nVertex A: " + v1 + "\nVertex B: " + v2 + "\nVertex C: " + v3;
    }
}
