package org.example;

public class RangeNode {
    private int min;
    private int max;
    public RangeNode(int min, int max) {
        super();
        this.min = min;
        this.max = max;
    }
    public int getMin() {
        return min;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }
    @Override
    public String toString() {
        return "RangeNode [min=" + min + ", max=" + max + "]";
    }

}
