package org.example;

public class Area {
    //color of area
    private String color;
    //how many parking slots free
    private int freePlaces;
    //list of all cars already parking
    private Node<String> plates;

    public Area(String color, int freePlaces, Node<String> plates) {
        this.color = color;
        this.freePlaces = freePlaces;
        this.plates = plates;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getFreePlaces() {
        return freePlaces;
    }

    public void setFreePlaces(int freePlaces) {
        this.freePlaces = freePlaces;
    }

    public Node<String> getPlates() {
        return plates;
    }

    public void setPlates(Node<String> plates) {
        this.plates = plates;
    }

    @Override
    public String toString() {
        return "Area{" +
                "color='" + color + '\'' +
                ", freePlaces=" + freePlaces +
                ", plates=" + plates +
                '}';
    }
}
