package org.example;
import java.util.Arrays;

public class School {
    private String name;
    private Node<Student>[] grades;

    public School(String name, Node<Student>[] grades) {
        this.name = name;
        this.grades = grades;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node<Student>[] getGrades() {
        return grades;
    }

    public void setGrades(Node<Student>[] grades) {
        this.grades = grades;
    }

    @Override
    public String toString() {
        return "School{" +
                "name='" + name + '\'' +
                ", grades=" + Arrays.toString(grades) +
                '}';
    }
}
