package org.example;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import java.util.concurrent.CompletableFuture;
import java.io.IOException;
import okhttp3.Call;

public class OkHttpTry {

    public static void main(String[] args) {
        // יצירת מופע OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // ====== 1) GET ========

        // בניית אובייקט HttpUrl
        HttpUrl getUrl = new HttpUrl.Builder()
                .scheme("https")                             // פרוטוקול
                .host("jsonplaceholder.typicode.com")        // כתובת
                .addPathSegment("posts")                     //  נתיב הראשון
                .addPathSegment("1")                         //  נתיב שני (מזהה הפוסט)
                .addQueryParameter("lang", "he") // ניתן להוסיף פרמטרים
                .build();

        // בניית הבקשה
        Request getReq = new Request.Builder()
                .url(getUrl)   // שימוש באובייקט Url
                .get()         // פעולה GET
                .build();

        // יצירת Future להמתנה לתוצאה
        CompletableFuture<String> getFuture = new CompletableFuture<>();

        // שליחת הבקשה - אסינכרונית
        client.newCall(getReq).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (response) { getFuture.complete(response.body().string()); }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getFuture.completeExceptionally(e);
            }
        });

        // הדפסת התוצאה
        try {
            JSONObject json = new JSONObject(getFuture.get());
            System.out.println("GET title = " + json.getString("title"));
        }
        catch (Exception e) { e.printStackTrace(); }


        // ====== 2) POST =======

        // בניית אובייקט HttpUrl ל-POST
        HttpUrl postUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("jsonplaceholder.typicode.com")
                .addPathSegment("posts")
                .build();

        // יצירת גוף JSON
        JSONObject postBodyJson = new JSONObject()
                .put("id",1)
                .put("title", "Hello From OkHttp + HttpUrl")
                .put("body", "Created using HttpUrl object")
                .put("userId", 123);

        // סוג המידע שנשלח (..jason, xml, txt, image, etc) וקידוד
        MediaType type = MediaType.parse("application/json; charset=utf-8");

        // בניית גוף הבקשה
        RequestBody postBody = RequestBody.create(postBodyJson.toString(), type);

        // בניית בקשת post
        Request postReq = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                // .put(postBody) // עדכון, שינוי שדות גייסון
                // .delete() // מחיקה
                .build();

        client.newCall(postReq).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (response) {
                    System.out.println("POST status = " + response.code());
                    System.out.println("POST body   = " + response.body().string());
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                System.err.println("POST failed: " + e.getMessage());
            }

        });
    }
}
