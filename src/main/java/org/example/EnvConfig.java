package org.example;
import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {

    // טוען את קובץ ה־.env מהשורש של הפרויקט
    private static final Dotenv dotenv = Dotenv.load();

    // מחזיר את כתובת הפיירבייס מתוך הקובץ
    public static String getFirebaseUrl() {
        return dotenv.get("FIREBASE_URL");
    }
}
