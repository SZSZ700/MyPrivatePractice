package org.example;
import java.util.Arrays;

public class Hotel {
    private String name;//בא-נח
    private Room [] rooms;

    public Hotel(String name) {
        this.name = name;
        this.rooms = new Room[100];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "name='" + name + '\'' +
                ", rooms=" + Arrays.toString(rooms) +
                '}';
    }

    public int totalTime_firstSolution_thxTo_polymorphisem(){
        int total = 0;

        for (Room temp : this.rooms){
            total += temp.calaCleanTime();
        }

        return total;
    }

    public int totalTime_secSolution_notNeedded(int [] numArr){
        int total = 0;

        for (Room temp : this.rooms){

            if (temp instanceof FamilyRoom){
                total += ((FamilyRoom)temp).calaCleanTime();
            } else if (temp instanceof  Suite) {
                total += ((Suite)temp).calaCleanTime();
            }
        }

        return total;
    }
}
