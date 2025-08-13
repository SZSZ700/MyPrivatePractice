package org.example;
import java.util.Arrays;
import java.util.Queue;

public class Parking {
    private Floor [] floors;

    public Parking(Floor[] floors) {
        this.floors = floors;
    }

    public Floor[] getFloors() {
        return floors;
    }

    public void setFloors(Floor[] floors) {
        this.floors = floors;
    }

    @Override
    public String toString() {
        return "Parking{" +
                "floors=" + Arrays.toString(floors) +
                '}';
    }

    public void workDay(Queue<String> plates){
        while (!plates.isEmpty()) {
            String plateToAdd = plates.peek();

            //iteration on floors array for trying adding the car
            for (Floor tempFloor : floors) {
                //call function to see if we can allow car enter the parking
                String canAdd = tempFloor.isTherePlace(plateToAdd);

                //if we can
                if (!canAdd.equals("no-room")) {
                    //print floor number
                    System.out.println("floor number: " + tempFloor.getFloorNum());
                    //print area color
                    System.out.println("area color: " + canAdd);
                    //remove the car from the queue
                    plates.poll();
                    plateToAdd = "polled out";
                    break;
                }
            }

            //check if car added successfully, if not break
            if (!plateToAdd.equals("polled out")){
                System.out.println("no even one free place in all floors");
                System.out.println("not all");
                break;
            }
        }

        if (plates.isEmpty()) {
            System.out.println("all");
        }
    }
}
