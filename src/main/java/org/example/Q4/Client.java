package org.example.Q4;

public class Client {
    private String name;
    private int diners; // num of diners

    public Client(String name, int diners) {
        this.name = name;
        this.diners = diners;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDiners() {
        return diners;
    }

    public void setDiners(int diners) {
        this.diners = diners;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", diners=" + diners +
                '}';
    }
}
