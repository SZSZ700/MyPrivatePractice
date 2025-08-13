package org.example;

public class Floor {
    //number of the floor
    private int floorNum;
    //list of areas
    private Node<Area> areas;

    public Floor(int floorNum, Node<Area> areas) {
        this.floorNum = floorNum;
        this.areas = areas;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public Node<Area> getAreas() {
        return areas;
    }

    public void setAreas(Node<Area> areas) {
        this.areas = areas;
    }

    @Override
    public String toString() {
        return "Floor{" +
                "floorNum=" + floorNum +
                ", areas=" + areas +
                '}';
    }

    public String isTherePlace(String plateToAdd){
        //pointer for the areas list
        Node<Area> pos = this.areas;

        //iteration on areas list
        while (pos != null) {
            //create temporary area object
            Area temp = pos.getValue();
            //keep how many free parking slots in this specific area
            int howFree = temp.getFreePlaces();
            //the color of the area
            String color = temp.getColor();

            //if there is at least one free slot
            if (howFree > 0) {
                temp.setFreePlaces(temp.getFreePlaces() - 1);
                //create new Node : String, contain the plate of the car that wants to enter to the parking
                Node<String> toAdd = new Node<>(plateToAdd);
                //add the car to the plates list
                toAdd.setNext(temp.getPlates());
                temp.setPlates(toAdd);
                //return the color of the area
                return color;
            }

            //mv to the next area in the list
            pos = pos.getNext();
        }

        //if it reach the end it's mean there is no free slot
        return "no-room";
    }

}
