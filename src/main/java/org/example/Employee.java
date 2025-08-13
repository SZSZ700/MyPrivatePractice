package org.example;

public class Employee {
    private int workerNumber;
    private static int num; // מס עובד
    private String name; // שם עובד

    public Employee(String name) {
        Employee.num += 1;
        this.workerNumber = Employee.num;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWorkerNumber() {
        return workerNumber;
    }

    public void setWorkerNumber(int workerNumber) {
        this.workerNumber = workerNumber;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                '}';
    }
}
