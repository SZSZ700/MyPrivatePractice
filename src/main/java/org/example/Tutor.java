package org.example;

public class Tutor extends Employee {
    private int courseNum; // מס קורס

    public Tutor(int courseNum, String name) {
        super(name);
        this.courseNum = courseNum;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(int courseNum) {
        this.courseNum = courseNum;
    }

    @Override
    public String toString() {
        return "Tutor{" +
                "courseNum=" + courseNum +
                '}';
    }
}
