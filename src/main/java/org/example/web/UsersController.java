package org.example.web;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {

    // -- Fields with sync && locking -- //

    // "בסיס נתונים" - רשימת משתמשים
    private static final List<User> users = new CopyOnWriteArrayList<>();

    // מונה מס משתמשים - ונותן ID לכל אחד לפי סדר ההוספה
    private static final AtomicInteger ID = new AtomicInteger(1);

    // מחלקת משתמש פנימית
    private static class User {
        private int id; // מזהה יחודי
        private String name; // שם

        // בנאי ריק
        User(){ }

        // בנאי המחלקה הפנימית
        User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        // פעולות קובעות ומאחזרות
        public int getId() { return this.id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return this.name; }
        public void setName(String name) { this.name = name; }
    }

    // אתחול נתוני דוגמה
    static {
        try (InputStream is = UsersController.class.getClassLoader().getResourceAsStream("users.json")) {

            // אם נקרא תוכן מקובץ הג'ייסונים
            if (is != null) {
                // קריאת כל תוכן קובץ ה JASON המרתו למחרוזת
                String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                // המרת המחרוזת למערך גייסונים בחזרה
                JSONArray arr = new JSONArray(text);

                // מעבר על מערך גייסונים
                for (int i = 0; i < arr.length(); i++) {
                    // קבלת גייסון נוכחי
                    JSONObject obj = arr.getJSONObject(i);

                    // יצירת אובייקט משתמש, מאובייקט הגייסון
                    User u = new User(
                            obj.getInt("id"),
                            obj.getString("name")
                    );

                    users.add(u); // הוספת אובייקט המשתמש לרשימה

                    // קידום מונה משתמשים (מזהה יחודי)
                    ID.set(Math.max(ID.get(), u.getId() + 1));
                }
            }

        } catch (Exception e) { e.printStackTrace(); }
    }



    // -- Methods -- //

    // המרת User ל-JSON
    private static JSONObject userToJson(User u) {
        // מחזיר אובייקט גייסון שנבנה ממשתמש
        return new JSONObject()
                .put("id", u.id)
                .put("name", u.name);
    }

    // המרת רשימת משתמשים ל-JSON
    private static String usersToJson(List<User> list) {
        // יצירת מערך אובייקטים גייסונים, שיכיל משתמשים
        JSONArray arr = new JSONArray();

        for (User u : list){ arr.put(userToJson(u)); } // בניית המערך

        return arr.toString(); // המרת המערך למחרוזת ארוכה, והחזרתה
    }


    // -----------------------------
    // GET /api/users  ( רשימת משתמשים)
    // -----------------------------
    @GetMapping
    public ResponseEntity<String> listUsers() {
        // החזר אובייקט תגובה תקינה
        return ResponseEntity
                .ok(
                        // המרת רשימת המשתמשים למערך גייסונים, ואז למחרוזת
                        usersToJson(users)
                );
    }


    // -----------------------------
    // GET /api/users/{id}  (משתמש בודד)
    // -----------------------------
    @GetMapping("/{id}")
    public ResponseEntity<String> getOne(@PathVariable("id") int id) {
        // איטרציה לחיפוש אחר משתמש יחיד בעל אותו מזהה יחודי כמו שהתקבל בפונקציה כפרמטר
        for (User u : users) {
            if (u.id == id) {
                return ResponseEntity
                        .ok(
                                // המרת אובייקט משתמש לגייסון, ואז למחרוזת
                                userToJson(u).toString()
                        );
            }
        }

        // אחרת החזר אובייקט תגובה "לא תקין"
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // סטטוס הבקשה
                // גוף הבקשה
                .body(
                        // אובייקט גייסון
                        new JSONObject()
                        .put("error", "User not found") // מפתח: שגיאה, ערך: משתמש לא נמצא
                        .toString() // המרת הגייסון למחרוזת
                );
    }


    // -----------------------------
    // POST /api/users  (יצירה)
    // הערה: מקבל ANY Content-Type כדי להתאים לקליינט שלך
    // -----------------------------
    @PostMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity<String> create(@RequestBody(required = false) byte[] bodyBytes) throws IOException {

        String body =
                bodyBytes == null
                        ?
                        ""
                        :
                        new String(bodyBytes, StandardCharsets.UTF_8);

        JSONObject json = new JSONObject(body);

        String name = json.optString("name", "").trim();

        if (name.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            new JSONObject()
                                    .put("error", "Name is required")
                                    .toString()
                    );
        }

        User u = new User(ID.getAndIncrement(), name);

        users.add(u);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        userToJson(u).toString()
                );
    }

    // -----------------------------
    // PUT /api/users/{id}  (עדכון)
    // גם כאן נקבל ANY Content-Type כדי שלא תיפול על ה-MediaType של הקליינט
    // -----------------------------
    @PutMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<String> update(@PathVariable("id") int id,
                                         @RequestBody(required = false) byte[] bodyBytes) {
        // קבלת גייסון - כלומר גוף הבקשה - המרתו ממערך בייטים למחרוזת, ואז לגייסון
        String body =
                // אם לא שלחו שום גוף בבקשה
                bodyBytes == null
                        ?
                        // נציב מחרוזת ריקה
                        ""
                        :
                        // אחרת: הופכים את המערך (byte[]) למחרוזת תיאור של אובייקט הגייסון
                        new String(bodyBytes, StandardCharsets.UTF_8);

        // המרת המחרוזת לאובייקט גייסון בחזרה
        JSONObject json = new JSONObject(body);

        // שליפת שדה השם מתוך הגייסון
        String name = json.optString("name", "").trim();

        // אם לא הוזן שם, או אין שדה שם
        if (!json.has("name") || name.isEmpty()) {
            // החזר אובייקט תגובה לא תקינה
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // סטטוס: בקשה לא תקינה
                    //גוף התגובה
                    .body(
                            // אובייקט גייסון המסביר את סוג השגיאה
                            new JSONObject()
                                    .put("error", "Name is required")
                                    .toString()
                    );
        }

        // חיפוש אחר האובייקט בעל מזהה יחודי זה לעדכון שדה שם
        for (User u : users) {
            // אם נמצא
            if (u.id == id) {
                u.name = name; // נעדכן את שמו
                // נחזיר תגובה תקינה אם האובייקט גייסון שעודכן
                return ResponseEntity
                        .ok(
                                userToJson(u).toString()
                        );
            }
        }

        // אם אין אובייקט גייסון בעל מזהה יחודי הזהה למה שהתקבל בפונקצייה כפרמטר
        // נחזיר תגובה לא תקינה, עם סטטוס בקשה: "לא נמצא", ואובייקט גייסון
        // המסביר על השגיאה
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // סטטוס - "לא נמצא"
                // גוף הבקשה
                .body(
                        new JSONObject()
                                .put("error", "User not found")
                                .toString()
                );
    }


    // -----------------------------
    // DELETE /api/users/{id}  (מחיקה לפי מזהה יחודי)
    // -----------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        // הסרה מהרשימה
        boolean removed = users.removeIf(u -> u.id == id);

        // אם הוסר
        return removed
                ?
                // אם הוסר החזר תגובה תקינה
                ResponseEntity
                        .noContent() // ללא תוכן: ללא גוף, סטטוס: 201 דיפולטיבי
                        .build() // בניית התגובה
                :
                // אחרת תגובה לא תקינה
                ResponseEntity
                        .status(HttpStatus.NOT_FOUND) // סטטוס בקשה: לא נמצא
                        .build(); // בניית התגובה
    }
}

