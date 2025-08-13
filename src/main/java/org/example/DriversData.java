package org.example;

public class DriversData {
    private Node<Driver> drivers;

    public DriversData() {
        this.drivers = null;
    }

    public Node<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(Node<Driver> drivers) {
        this.drivers = drivers;
    }

    @Override
    public String toString() {
        return "DriversData{" +
                "drivers=" + drivers +
                '}';
    }

    public Driver removeFromChain(String idToRemove){
        Node<Driver> pos = this.drivers;
        //אם האדם להסרה נמצא בהתחלה
        if (pos.getValue().getId().equals(idToRemove)){
            Driver driver = pos.getValue();
            this.drivers = this.drivers.getNext();
            return driver;
        }

        //אם האדם להסרה נמצא בכל מקום אחר בשרשרת גם בסוף
        while (pos.getNext() != null && !pos.getNext().getValue().getId().equals(idToRemove)){
            pos = pos.getNext();
        }

        Driver driver = pos.getNext().getValue();
        pos.setNext(pos.getNext().getNext());
        return driver;
    }

    public int numOfIllegalActivitiesPerAge(int age){
        int MaxillegalActsPerAge = Integer.MIN_VALUE;

        Node<Driver> pos = this.drivers;

        while (pos != null){
            if (pos.getValue().getAge() == age){
                int numActs = pos.getValue().getIllegalActs();

                if (numActs > MaxillegalActsPerAge){
                    MaxillegalActsPerAge = numActs;
                }
            }
            pos = pos.getNext();
        }

        return MaxillegalActsPerAge;
    }

    public void printUnActiveDrivers(){
        Node<Driver> pos = this.drivers;

        while (pos != null){
            Driver driver = pos.getValue();
            int age = driver.getAge();
            int illegalActs = pos.getValue().getIllegalActs();
            int maxIllegalActsPerAge = numOfIllegalActivitiesPerAge(age);

            if (illegalActs > maxIllegalActsPerAge){
                System.out.println(driver);
            }

            pos = pos.getNext();
        }
    }
}
