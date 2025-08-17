package org.example;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONObject;

// הגדרת servlet שיטפל בכל בקשה לנתיב "/api/users"
@WebServlet(name = "UsersServlet", urlPatterns = "/api/users/*")
public class UsersServlet extends HttpServlet {

    // "בסיס נתונים" - רשימת משתמשים
    private static final List<User> users = new CopyOnWriteArrayList<>();
    // מזהה יחודי לכל משתמש
    private static final AtomicInteger idq = new AtomicInteger(1);

    // מחלקת משתמש
    public static class User {
        private int id; // מזהה יחודי
        private String name; // שם

        // בנאי
        public User(int id, String name) { this.id = id; this.name = name; }
    }

    // פונקציית עזר: כתיבת JSON ללקוח
    private static void writeJson(HttpServletResponse resp, int status, String json) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application.properties/json; charset=UTF-8");
        resp.getWriter().write(json);
    }

    // פונקציית עזר: המרת משתמש ל־JSON
    private static JSONObject userToJson(User u) {
        return new JSONObject()
                .put("idq", u.id)
                .put("name", u.name);
    }

    // פונקציית עזר: המרת רשימת משתמשים ל־JSON
    private static JSONArray usersToJson(List<User> list) {
        JSONArray arr = new JSONArray();
        for (User u : list) {
            arr.put(userToJson(u));
        }
        return arr;
    }

    // פונקציית עזר: קריאת גוף הבקשה
    private static String readBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = req.getReader()) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
        }
        return sb.toString().trim();
    }

    // פונקציית עזר: שליפת ID מהנתיב (/api/users/3 → 3)
    private static Integer pathId(HttpServletRequest req) {
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) return null;

        try {
            return Integer.parseInt(path.substring(1));
        } catch (Exception e) {
            return null;
        }
    }

    // GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = pathId(req);
        if (id == null) {
            writeJson(resp, 200, usersToJson(users).toString());
        } else {
            for (User u : users) {
                if (u.id == id) {
                    writeJson(resp, 200, userToJson(u).toString());
                    return;
                }
            }
            writeJson(resp, 404, new JSONObject().put("error","User not found").toString());
        }
    }

    // POST
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String body = readBody(req);
        JSONObject json = new JSONObject(body);  // כאן אנחנו באמת מפרשים JSON

        if (!json.has("name") || json.getString("name").isBlank()) {
            writeJson(resp, 400, new JSONObject().put("error","Name is required").toString());
            return;
        }

        User u = new User(idq.getAndIncrement(), json.getString("name"));
        users.add(u);
        writeJson(resp, 201, userToJson(u).toString());
    }

    // PUT
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = pathId(req);
        if (id == null) {
            writeJson(resp, 400, new JSONObject().put("error","ID required in path").toString());
            return;
        }

        String body = readBody(req);
        JSONObject json = new JSONObject(body);

        if (!json.has("name") || json.getString("name").isBlank()) {
            writeJson(resp, 400, new JSONObject().put("error","Name is required").toString());
            return;
        }

        for (User u : users) {
            if (u.id == id) {
                u.name = json.getString("name");
                writeJson(resp, 200, userToJson(u).toString());
                return;
            }
        }
        writeJson(resp, 404, new JSONObject().put("error","User not found").toString());
    }

    // DELETE
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = pathId(req);
        if (id == null) {
            writeJson(resp, 400, new JSONObject().put("error","ID required in path").toString());
            return;
        }

        boolean removed = users.removeIf(u -> u.id == id);
        if (removed) {
            resp.setStatus(204); // מחיקה מוצלחת בלי גוף
        } else {
            writeJson(resp, 404, new JSONObject().put("error","User not found").toString());
        }
    }
}
