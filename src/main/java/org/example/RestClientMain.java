package org.example;

// ייבוא OkHttp
import okhttp3.*;
import okhttp3.Call;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class RestClientMain {
    public static void main(String[] args) throws InterruptedException {
        // יצירת לקוח OkHttp (ישמש את כל הבקשות)
        OkHttpClient client = new OkHttpClient();

        // כתובת הבסיס ל־REST API שרץ על Tomcat (התאם לפי ה־context path שלך)
        String baseUrl = "http://localhost:8080/myapp/api/users";

        // טיפוס המדיה JSON
        MediaType jsonMedia = MediaType.parse("application/json; charset=utf-8");


        // לאפשר ל-main להמתין עד שהזרימה האסינכרונית מסתיימת (רק כדי שלא תסתיים התוכנית)
        CountDownLatch done = new CountDownLatch(1);

        // -----------------------------
        // POST: יצירת משתמש חדש --הוספה
        // -----------------------------

        // בניית גוף JSON: {"name":"Alice"}
        JSONObject postJson = new JSONObject();
        postJson.put("name", "Aliana");

        // המרת ה־JSON לגוף בקשה
        RequestBody postBody = RequestBody.create(postJson.toString(), jsonMedia);

        // בניית בקשת POST ל־/api/users
        Request postReq = new Request.Builder()
                .url(baseUrl)
                .post(postBody)
                .build();

        // שליחת הבקשה בצורה אסינכרונית
        client.newCall(postReq).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // הדפסת שגיאה אם POST נכשל
                System.err.println("POST failed: " + e.getMessage());
                // שחרור ה־latch כדי שה־main יוכל לצאת
                done.countDown();
            }

            @Override
            public void onResponse(Call call, Response postResp) {
                // שימוש ב-try-with-resources כדי לסגור את ה־Response
                try (Response resp = postResp) {
                    // בדיקה שהבקשה הצליחה (201 צפוי)
                    if (!resp.isSuccessful()) {
                        System.err.println("POST failed: " + resp.code() + " " + resp.message());
                        done.countDown();
                        return;
                    }

                    // קריאת הגוף כמחרוזת
                    String createdStr = resp.body() != null ? resp.body().string() : "";
                    // הדפסה לצורך בדיקה
                    System.out.println("POST /users => " + createdStr);

                    // המרה ל־JSONObject כדי לחלץ את ה־id
                    JSONObject createdObj = new JSONObject(createdStr);

                    int createdId = createdObj.getInt("id");

                    // -----------------------------------
                    // GET (רשימה): קבלת כל המשתמשים
                    // -----------------------------------

                    // בניית בקשת GET ל־/api/users
                    Request getListReq1 = new Request.Builder()
                            .url(baseUrl)
                            .get()
                            .build();

                    // שליחה אסינכרונית של ה־GET (רשימה)
                    client.newCall(getListReq1).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            // הדפסת שגיאה אם GET נכשל
                            System.err.println("GET list failed: " + e.getMessage());
                            done.countDown();
                        }

                        @Override
                        public void onResponse(Call call, Response listResp1) throws IOException {
                            try (Response r1 = listResp1) {
                                // בדיקת הצלחה
                                if (!r1.isSuccessful()) {
                                    System.err.println("GET list failed: " + r1.code() + " " + r1.message());
                                    done.countDown();
                                    return;
                                }

                                // קריאת הגוף -
                                // הגוף מכיל זרם בייטים,XML/JSON/IMAGE לכן דרוש להמיר למחרוזת
                                // ורק אז נמיר לאובייקט גייסון שיודע לקבל רק מחרוזת גייסונים
                                // ולא זרמים
                                String listStr1 = r1.body() != null ? r1.body().string() : "[]";
                                // המרה ל־JSONArray
                                JSONArray arr1 = new JSONArray(listStr1);
                                // הדפסה
                                System.out.println("GET /users => " + arr1.toString());

                                // -----------------------------------
                                // GET (פריט): קבלת משתמש לפי id
                                // -----------------------------------

                                // בניית כתובת /api/users/{id}
                                String oneUrl = baseUrl + "/" + createdId;

                                // בניית בקשת GET לפריט בודד
                                Request getOneReq = new Request.Builder()
                                        .url(oneUrl)
                                        .get()
                                        .build();

                                // שליחה אסינכרונית של GET (פריט)
                                client.newCall(getOneReq).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        // הדפסת שגיאה אם GET אחד נכשל
                                        System.err.println("GET one failed: " + e.getMessage());
                                        done.countDown();
                                    }

                                    @Override
                                    public void onResponse(Call call, Response oneResp) {
                                        try (Response r2 = oneResp) {
                                            // בדיקת הצלחה
                                            if (!r2.isSuccessful()) {
                                                System.err.println("GET one failed: " + r2.code() + " " + r2.message());
                                                done.countDown();
                                                return;
                                            }

                                            // קריאת הגוף
                                            String oneStr = r2.body() != null ? r2.body().string() : "{}";
                                            // המרה ל־JSONObject
                                            JSONObject oneObj = new JSONObject(oneStr);
                                            // הדפסה
                                            System.out.println("GET /users/" + createdId + " => " + oneObj.toString());

                                            // -----------------------------------
                                            // PUT: עדכון שם המשתמש
                                            // -----------------------------------

                                            // בניית JSON חדש: {"name":"Alice Cooper"}
                                            JSONObject putJson = new JSONObject();
                                            putJson.put("name", "Alice Cooper");

                                            // יצירת גוף הבקשה
                                            RequestBody putBody = RequestBody.create(putJson.toString(), jsonMedia);

                                            // בניית בקשת PUT ל־/api/users/{id}
                                            Request putReq = new Request.Builder()
                                                    .url(oneUrl)
                                                    .put(putBody)
                                                    .build();

                                            // שליחה אסינכרונית של PUT
                                            client.newCall(putReq).enqueue(new Callback() {
                                                @Override
                                                public void onFailure(Call call, IOException e) {
                                                    // הדפסת שגיאה אם PUT נכשל
                                                    System.err.println("PUT failed: " + e.getMessage());
                                                    done.countDown();
                                                }

                                                @Override
                                                public void onResponse(Call call, Response putResp) {
                                                    try (Response r3 = putResp) {
                                                        // בדיקת הצלחה
                                                        if (!r3.isSuccessful()) {
                                                            System.err.println("PUT failed: " + r3.code() + " " + r3.message());
                                                            done.countDown();
                                                            return;
                                                        }

                                                        // קריאת הגוף
                                                        String updatedStr = r3.body() != null ? r3.body().string() : "{}";

                                                        // המרה ל־JSONObject
                                                        JSONObject updatedObj = new JSONObject(updatedStr);

                                                        // הדפסה
                                                        System.out.println("PUT /users/" + createdId + " => " + updatedObj.toString());


                                                        // -----------------------------------
                                                        // DELETE: מחיקת המשתמש
                                                        // -----------------------------------

                                                        // בניית בקשת DELETE
                                                        Request delReq = new Request.Builder()
                                                                .url(oneUrl)
                                                                .delete()
                                                                .build();

                                                        // שליחה אסינכרונית של DELETE
                                                        client.newCall(delReq).enqueue(new Callback() {
                                                            // אם המחיקה נכשלה
                                                            @Override
                                                            public void onFailure(Call call, IOException e) {
                                                                // הדפסת שגיאה אם DELETE נכשל
                                                                System.err.println("DELETE failed: " + e.getMessage());
                                                                done.countDown();
                                                            }

                                                            // אם בוצעה מחיקה בהצלחה
                                                            @Override
                                                            public void onResponse(Call call, Response delResp) {
                                                                try (Response r4 = delResp) {
                                                                    if (r4.code() != 204 && !r4.isSuccessful()) {
                                                                        System.err.println("DELETE failed: " + r4.code() + " " + r4.message());
                                                                        done.countDown(); // count--;
                                                                        return;
                                                                    }
                                                                    // הדפסה על תוצאת המחיקה
                                                                    System.out.println("DELETE /users/" + createdId + " => " + r4.code());


                                                                    // -----------------------------------
                                                                    // GET (הצגת הרשימה אחרי השינוי): בדיקה אחרונה
                                                                    // -----------------------------------
                                                                    // בניית בקשת GET
                                                                    Request getListReq2 = new Request.Builder()
                                                                            .url(baseUrl)
                                                                            .get()
                                                                            .build();

                                                                    // קריאה אסינכרונית
                                                                    client.newCall(getListReq2).enqueue(new Callback() {
                                                                        // אם הבאת הרשימה נכשלה
                                                                        @Override
                                                                        public void onFailure(Call call, IOException e) {
                                                                            System.err.println("GET list (after delete) failed: " + e.getMessage());
                                                                            done.countDown();// count--;
                                                                        }

                                                                        // אם הבאת הרשימה צלחה
                                                                        @Override
                                                                        public void onResponse(Call call, Response listResp2) throws IOException {
                                                                            try (Response r5 = listResp2) {
                                                                                // בדיקת הצלחה
                                                                                if (!r5.isSuccessful()) {
                                                                                    System.err.println("GET list (after delete) failed: " + r5.code() + " " + r5.message());
                                                                                    done.countDown(); // count--;
                                                                                    return;
                                                                                }

                                                                                // קריאת הגוף
                                                                                String listStr2 = r5.body() != null ? r5.body().string() : "[]";

                                                                                // המרת הרשימה למערך ג'ייסונים
                                                                                JSONArray arr2 = new JSONArray(listStr2);

                                                                                // הדפסה
                                                                                System.out.println("GET /users (after delete) => " + arr2.toString());

                                                                                done.countDown(); // count--;
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            });
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                            }
                        }
                    });
                } catch (Exception ex) {
                    // הדפסת חריגה לא צפויה בכל אחד מהשלבים
                    ex.printStackTrace();
                    // שחרור ה־latch כדי לא להיתקע
                    done.countDown();
                }
            }
        });

        // המתנה עד שהשרשרת האסינכרונית תסתיים (רק כדי שהתוכנית לא תיסגר בטרם עת)
        done.await();

        // ניקוי משאבים אופציונלי
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
        // (אם הגדרת Cache – גם cache.close())
    }
}
