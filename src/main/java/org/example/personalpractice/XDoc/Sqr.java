package org.example.personalpractice.XDoc;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class Sqr {
    private double length;
    private double width;

    public Sqr(double length, double width) {
        if (length <= 0) { this.length = 1; }
        else { this.length = length; }

        if (width <= 0) { this.width = 1; }
        else { this.width = width; }
    }

    public double getLength() { return this.length; }

    public void setLength(double length) {
        if (length <= 0) { this.length = 1; }
        else { this.length = length; }
    }

    public double getWidth() { return this.width; }

    public void setWidth(double width) {
        if (width <= 0) { this.width = 1; }
        else { this.width = width; }
    }

    public double getArea() { return this.length * this.width; }

    public boolean isVolumeGreaterThanInput(double num) { return this.getArea() > num; }
}