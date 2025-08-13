package org.example;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import java.util.concurrent.CompletableFuture;
import java.io.IOException;

public class OkHttpTry {
    public static void main(String[] args) {
        // יצירת מופע OkHttpClient אחד שישמש את כל הבקשות
        OkHttpClient client = new OkHttpClient();

        // ======================
        // ====== 1) GET ========
        // ======================

        // בניית אובייקט HttpUrl ל-GET כולל Query Parameters
        HttpUrl getUrl = new HttpUrl.Builder()
                .scheme("https")                           // פרוטוקול HTTPS
                .host("jsonplaceholder.typicode.com")      // כתובת השרת
                .addPathSegment("posts")                   // חלק הנתיב הראשון
                .addPathSegment("1")                       // חלק הנתיב השני (מזהה הפוסט)
                // ניתן להוסיף פרמטרים (רק להדגמה – לא באמת נדרש כאן)
                .addQueryParameter("lang", "he")
                .build();

        // בניית הבקשה
        Request getReq = new Request.Builder()
                .url(getUrl)   // שימוש באובייקט HttpUrl
                .get()         // פעולה GET
                .build();

        // יצירת Future להמתנה לתוצאה
        CompletableFuture<String> getFuture = new CompletableFuture<>();

        // שליחת הבקשה
        client.newCall(getReq).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                try (response) {
                    getFuture.complete(response.body().string());
                }
            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                getFuture.completeExceptionally(e);
            }
        });

        // הדפסת התוצאה
        try {
            JSONObject json = new JSONObject(getFuture.get());
            System.out.println("GET title = " + json.getString("title"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ======================
        // ====== 2) POST =======
        // ======================

        // בניית אובייקט HttpUrl ל-POST (ללא מזהה כי זה יוצר חדש)
        HttpUrl postUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("jsonplaceholder.typicode.com")
                .addPathSegment("posts")
                .build();

        // יצירת גוף JSON עם JSONObject
        JSONObject postBodyJson = new JSONObject()
                .put("title", "Hello From OkHttp + HttpUrl")
                .put("body", "Created using HttpUrl object")
                .put("userId", 123);

        MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
        RequestBody postBody = RequestBody.create(postBodyJson.toString(), JSON_TYPE);

        // בניית בקשת POST
        Request postReq = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(postReq).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                try (response) {
                    System.out.println("POST status = " + response.code());
                    System.out.println("POST body   = " + response.body().string());
                }

            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                System.err.println("POST failed: " + e.getMessage());
            }

        });

        // ======================
        // ====== 3) PUT ========
        // ======================

        // בניית אובייקט HttpUrl ל-PUT (עדכון פוסט קיים עם מזהה 1)
        HttpUrl putUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("jsonplaceholder.typicode.com")
                .addPathSegment("posts")
                .addPathSegment("1")
                .build();

        JSONObject putBodyJson = new JSONObject()
                .put("id", 1)
                .put("title", "Updated via HttpUrl")
                .put("body", "Updated content")
                .put("userId", 123);

        RequestBody putBody = RequestBody.create(putBodyJson.toString(), JSON_TYPE);

        Request putReq = new Request.Builder()
                .url(putUrl)
                .put(putBody)
                .build();

        client.newCall(putReq).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                try (response) {
                    System.out.println("PUT status = " + response.code());
                    System.out.println("PUT body   = " + response.body().string());
                }
            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                System.err.println("PUT failed: " + e.getMessage());
            }
        });

        // ======================
        // ====== 4) DELETE =====
        // ======================

        // בניית אובייקט HttpUrl ל-DELETE
        HttpUrl deleteUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("jsonplaceholder.typicode.com")
                .addPathSegment("posts")
                .addPathSegment("1")
                .build();

        Request deleteReq = new Request.Builder()
                .url(deleteUrl)
                .delete()
                .build();

        client.newCall(deleteReq).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                try (response) {
                    System.out.println("DELETE status = " + response.code());
                    System.out.println("DELETE body   = " + response.body().string());
                }
            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                System.err.println("DELETE failed: " + e.getMessage());
            }
        });

        // השהייה קצרה כדי לאפשר לקריאות הא-סינכרוניות להסתיים
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
    }
}
