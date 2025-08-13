package org.example;

public class Transformations {
    // Rotation around the X-axis
    public static Vector rotateX(Vector vector, double angle) {
        // Calculate cosine and sine of the angle
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);

        // Calculate the new coordinates after rotation
        // X stays the same in rotation around the X-axis
        double y = vector.getY() * cosTheta - vector.getZ() * sinTheta;  // New Y
        double z = vector.getY() * sinTheta + vector.getZ() * cosTheta;  // New Z
        double x = vector.getX(); // X remains unchanged

        // Return a new vector with the updated coordinates
        return new Vector(x, y, z);
    }

    // Rotation of a triangle around the X-axis
    public static Triangle rotateX(Triangle triangle, double angle) {
        // Perform rotation for each vertex of the triangle
        return new Triangle(
                rotateX(triangle.getV1(), angle),  // Rotate vertex A
                rotateX(triangle.getV2(), angle),  // Rotate vertex B
                rotateX(triangle.getV3(), angle)   // Rotate vertex C
        );
    }

    // Rotation around the Y-axis
    public static Vector rotateY(Vector vector, double angle) {
        // Calculate cosine and sine of the angle
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);

        // Calculate the new coordinates after rotation
        // Y stays the same in rotation around the Y-axis
        double x = vector.getZ() * sinTheta + vector.getX() * cosTheta;  // New X
        double z = vector.getZ() * cosTheta - vector.getX() * sinTheta;  // New Z
        double y = vector.getY(); // Y remains unchanged

        // Return a new vector with the updated coordinates
        return new Vector(x, y, z);
    }

    // Rotation of a triangle around the Y-axis
    public static Triangle rotateY(Triangle triangle, double angle) {
        // Perform rotation for each vertex of the triangle
        return new Triangle(
                rotateY(triangle.getV1(), angle),  // Rotate vertex A
                rotateY(triangle.getV2(), angle),  // Rotate vertex B
                rotateY(triangle.getV3(), angle)   // Rotate vertex C
        );
    }

    // Rotation around the Z-axis
    public static Vector rotateZ(Vector vector, double angle) {
        // Calculate cosine and sine of the angle
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);

        // Calculate the new coordinates after rotation
        // Z stays the same in rotation around the Z-axis
        double x = vector.getX() * cosTheta - vector.getY() * sinTheta;  // New X
        double y = vector.getX() * sinTheta + vector.getY() * cosTheta;  // New Y
        double z = vector.getZ(); // Z remains unchanged

        // Return a new vector with the updated coordinates
        return new Vector(x, y, z);
    }

    // Rotation of a triangle around the Z-axis
    public static Triangle rotateZ(Triangle triangle, double angle) {
        // Perform rotation for each vertex of the triangle
        return new Triangle(
                rotateZ(triangle.getV1(), angle),  // Rotate vertex A
                rotateZ(triangle.getV2(), angle),  // Rotate vertex B
                rotateZ(triangle.getV3(), angle)   // Rotate vertex C
        );
    }
}
