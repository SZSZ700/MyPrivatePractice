package org.example;
import okhttp3.*;
import okhttp3.Call;
import org.json.JSONObject;
import java.io.IOException;

public class OkHttpTry {

    public static void main(String[] args) {
        // יצירת מופע OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // ====== 1) GET ========

        // בניית אובייקט HttpUrl
        HttpUrl getUrl = new HttpUrl.Builder()
                .scheme("https")                             // פרוטוקול
                .host("jsonplaceholder.typicode.com")        // כתובת
                .addPathSegment("posts")                     // נתיב ראשון
                .addPathSegment("1")                         // מזהה פוסט
                .addQueryParameter("lang", "he")             // ניתן להוסיף פרמטרים
                .build();

        // בניית הבקשה
        Request getReq = new Request.Builder()
                .url(getUrl)
                .get()
                .build();

        // שליחת הבקשה - אסינכרונית בלבד
        client.newCall(getReq).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (response) {
                    String body = response.body().string();
                    JSONObject json = new JSONObject(body);
                    System.out.println("GET title = " + json.getString("title"));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                System.err.println("GET failed: " + e.getMessage());
            }
        });

        // ====== 2) POST =======

        // בניית URL ל־POST
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

        // סוג המידע שנשלח
        MediaType type = MediaType.parse("application.properties/json; charset=utf-8");

        // בניית גוף הבקשה
        RequestBody postBody = RequestBody.create(postBodyJson.toString(), type);

        // בניית בקשת POST
        Request postReq = new Request.Builder()
                .url(postUrl)
                .post(postBody)
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
