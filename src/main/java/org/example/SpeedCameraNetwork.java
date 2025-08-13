package org.example;
import java.util.LinkedList;
import java.util.Queue;

public class SpeedCameraNetwork {

    // מערך בגודל מקסימלי של 100 מצלמות
    private SpeedCamera[] cameras;

    // מונה את מספר המצלמות שכבר קיימות ברשת
    private int cameraCount;

    // בנאי שמאתחל את רשת המצלמות
    public SpeedCameraNetwork() {
        cameras = new SpeedCamera[100]; // מקצה מקום ל־100 מצלמות
        cameraCount = 0; // מתחילים מאפס מצלמות
    }

    // פעולה להוספת מצלמה לרשת (בהנחה שלא קיימת כבר)
    public void addSpeedCamera(SpeedCamera sc) {
        // נוודא שלא חצינו את הגבול של 100 מצלמות
        if (cameraCount < 100) {
            cameras[cameraCount] = sc; // הוספה למערך
            cameraCount++; // עדכון מונה
        }
        // אם חרגנו – לא נעשה כלום (אפשר גם להדפיס שגיאה אם רוצים)
    }

    public void printHighEnforcementRoads() {
        for (int i = 0; i < cameraCount; i++) {
            SpeedCamera cam = cameras[i];
            if (cam.getSpeedingCars().size() > 200) {
                System.out.println("High enforcement needed on road: " + cam.getRoadNumber());
            }
        }
    }

    // פעולה ב-SCN שבודקת אם רכב עבר עבירת מהירות ומדפיסה את קודי המצלמות
    public boolean wasCarCaught(String carNumber) {
        boolean found = false;

        // מעבר על כל המצלמות ברשת
        for (int i = 0; i < cameraCount; i++) {
            Queue<String> q = cameras[i].getSpeedingCars();  // התור המקורי
            Queue<String> copy = new LinkedList<>();       // תור עזר

            // מעבר על התור
            while (!q.isEmpty()) {
                String plate = q.poll();       // שליפת לוחית
                copy.offer(plate);             // שמירה בתור עזר

                if (plate.equals(carNumber)) {
                    System.out.println("Caught by camera: " + cameras[i].getCameraCode());
                    found = true;
                }
            }

            // שחזור התור המקורי
            while (!copy.isEmpty()) {
                q.offer(copy.poll());
            }
        }

        return found;
    }
}

