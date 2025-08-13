package org.example;

//מחלקת ממתין
public class Caller {
    private String name;//שם הממתין
    private String phoneNumber;//מס טלפון הממתין

    //בניית ממתין
    public Caller(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    //הדפסת פרטי ממתין
    public String getName() {
        return name;
    }

    //קביעת שם ממתין
    public void setName(String name) {
        this.name = name;
    }

    //החזרת מס טלפון של ממתין
    public String getPhoneNumber() {
        return phoneNumber;
    }

    //קביעת מס טלפון של ממתין
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //הדפסת פרטי ממתין
    @Override
    public String toString() {
        return "Caller{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
