package org.example;
import java.util.Arrays;

public class Head extends Lecturer{
    // מערך עובדים תחתיו
    private Employee [] arr;
    //מס עובדים נוכחי בפועל
    private int current;

    public Head(String name, String specialization, Employee[] arr, int current) {
        super(name, specialization);
        this.arr = arr;
        this.current = current;
    }

    public Head(String name, String specialization) {
        super(name, specialization);
        this.arr = new Employee[10];
        this.current = 0;
    }

    public Employee[] getArr() {
        return arr;
    }

    public void setArr(Employee[] arr) {
        this.arr = arr;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "Head{" +
                "arr=" + Arrays.toString(arr) +
                ", current=" + current +
                "} " + super.toString();
    }
}
