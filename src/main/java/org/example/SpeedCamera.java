package org.example;
import java.util.LinkedList;
import java.util.Queue;

public class SpeedCamera {

    // מזהה ייחודי של המצלמה
    private int cameraCode;

    // מספר הכביש בו ממוקמת המצלמה
    private int roadNumber;

    // המהירות המותרת בכביש
    private int speedLimit;

    // תור של מספרי רכבים שנסעו מהר מהמותר
    private Queue<String> speedingCars;

    // בנאי אתחול
    public SpeedCamera(int cameraCode, int roadNumber, int speedLimit) {
        this.cameraCode = cameraCode;
        this.roadNumber = roadNumber;
        this.speedLimit = speedLimit;
        this.speedingCars = new LinkedList<>();
    }

    public int getCameraCode() {
        return cameraCode;
    }

    public void setCameraCode(int cameraCode) {
        this.cameraCode = cameraCode;
    }

    public int getRoadNumber() {
        return roadNumber;
    }

    public void setRoadNumber(int roadNumber) {
        this.roadNumber = roadNumber;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public Queue<String> getSpeedingCars() {
        return speedingCars;
    }

    public void setSpeedingCars(Queue<String> speedingCars) {
        this.speedingCars = speedingCars;
    }

    // פעולה להוספת רכב שעבר את המהירות המותרת
    public void addCar(String licensePlate, int speed) {
        // אם המהירות גבוהה מהמותר – נוסיף את הרכב לתור
        if (speed > speedLimit) {
            speedingCars.offer(licensePlate);
        }
    }

    // פעולה שמחזירה מחרוזת עם כל פרטי המצלמה והרכבים שצולמו
    @Override
    public String toString() {
        // יצירת מחרוזת רגילה (לא StringBuilder)
        String result = "";

        // הוספת פרטי המצלמה
        result += "Camera Code: " + cameraCode + "\n";
        result += "Road Number: " + roadNumber + "\n";
        result += "Speed Limit: " + speedLimit + " km/h\n";
        result += "Speeding Cars: ";

        // תור עזר זמני כדי לא לפגוע בתור המקורי
        Queue<String> temp = new LinkedList<>();

        // אם התור ריק – נדפיס None
        if (speedingCars.isEmpty()) {
            result += "None";
        } else {
            // מעבר על התור עם poll ו-offer כדי לא לאבד נתונים
            while (!speedingCars.isEmpty()) {
                String car = speedingCars.poll(); // שליפה
                result += car + " "; // הוספה למחרוזת
                temp.offer(car); // שמירה בתור עזר
            }

            // החזרת הרכבים חזרה לתור המקורי
            while (!temp.isEmpty()) {
                speedingCars.offer(temp.poll());
            }
        }

        // החזרת המחרוזת הסופית
        return result;
    }
}
