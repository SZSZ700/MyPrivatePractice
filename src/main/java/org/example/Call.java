package org.example;

public class Call {
    //סוג אירוע חירום:
    //1 – שריפה, 2 – תאונה, 3 – חילוץ, 4
    //– פלילי, 5 – ביטחוני
    private int type;
    private String location; // אזור
    private String description; // תיאור
    private String callerName; // המתקשר שם
    private int priority; // רמת הדחיפות: 1 – גבוהה, 2 – בינונית, 3 – נמוכה

    public Call(int type, String location, String description, String callerName, int priority) {
        this.type = type;
        this.location = location;
        this.description = description;
        this.callerName = callerName;
        this.priority = priority;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Call{" +
                "type=" + type +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", callerName='" + callerName + '\'' +
                ", priority=" + priority +
                '}';
    }
}
