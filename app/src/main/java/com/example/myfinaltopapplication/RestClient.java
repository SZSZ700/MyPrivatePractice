// Define the package this class belongs to (Android client app)
package com.example.myfinaltopapplication;
// Import OkHttp classes for HTTP requests and responses
import android.util.Log;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;

import okhttp3.*;
import okio.Buffer;
// Import JSON library for building and parsing JSON objects
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
// Import exception handling for input/output operations
import java.io.IOException;
// Import Map class for PATCH requests (partial updates)
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
// Import CompletableFuture for asynchronous programming
import java.util.concurrent.CompletableFuture;
import java.lang.reflect.Type;


/**
 * RestClient - class that manages communication between
 * the Android app and the Spring Boot backend server.
 * Provides methods for user registration, login, update, delete,
 * BMI management, and water tracking.
 */
public class RestClient {

    // Base URL of the backend server (10.0.2.2 = localhost for Android emulator)
    private static final String BASE_URL = "http://10.0.2.2:8080/myapp/api/users";

    // Define JSON MediaType for sending JSON data
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    // Reusable HTTP client for all network requests
    // Built with an interceptor that prints detailed logs for each request and response
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                // Capture the outgoing request
                Request request = chain.request();

                // Start timer for performance measurement
                long t1 = System.nanoTime();
                android.util.Log.d("HTTP", "➡️ Sending " + request.method() + " request to " + request.url());

                // If request has a body, log its contents
                if (request.body() != null) {
                    Buffer buffer = new Buffer();
                    request.body().writeTo(buffer);
                    android.util.Log.d("HTTP", "📤 Request body: " + buffer.readUtf8());
                }

                // Proceed with the request and capture the response
                Response response = chain.proceed(request);

                // End timer for performance measurement
                long t2 = System.nanoTime();
                android.util.Log.d("HTTP", "⬅️ Received response for " + response.request().url() +
                        " in " + ((t2 - t1) / 1e6d) + "ms, code = " + response.code());

                // Print response body (peek to avoid consuming original stream)
                ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
                android.util.Log.d("HTTP", "📥 Response body: " + responseBody.string());

                // Return the response so the client can use it
                return response;
            })
            .build();

    // =========================================================
    // REGISTER (POST /api/users/signup)
    // =========================================================
    public static CompletableFuture<Boolean> register(User user) {
        // Create a CompletableFuture to return the result asynchronously
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        try {
            // Build JSON object with user details
            JSONObject json = new JSONObject();
            json.put("userName", user.getUserName()); // Add username
            json.put("password", user.getPassword()); // Add password
            json.put("fullName", user.getFullName()); // Add full name
            json.put("age", user.getAge());           // Add age

            // Create HTTP request body from JSON
            RequestBody body = RequestBody.create(json.toString(), JSON);

            // Build POST request for /signup endpoint
            Request request = new Request.Builder()
                    .url(BASE_URL + "/signup") // Correct endpoint for signup
                    .post(body)                // Use POST method
                    .build();

            // Send request asynchronously
            client.newCall(request).enqueue(callbackBoolean(future));

        } catch (Exception e) {
            // If building JSON fails, complete with false
            future.complete(false);
        }

        // Return CompletableFuture
        return future;
    }

    // =========================================================
    // LOGIN (POST /api/users/login)
    // =========================================================
    public static CompletableFuture<User> login(String username, String password) {
        // Future object that will complete with User if success
        CompletableFuture<User> future = new CompletableFuture<>();

        try {
            // Build JSON with login credentials
            JSONObject json = new JSONObject();
            json.put("userName", username); // Add username
            json.put("password", password); // Add password

            // Create request body with JSON
            RequestBody body = RequestBody.create(json.toString(), JSON);

            // Build POST request for login endpoint
            Request request = new Request.Builder()
                    .url(BASE_URL + "/login") // Target endpoint
                    .post(body)               // Use POST
                    .build();

            // Send request asynchronously
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("HTTP", "❌ LOGIN request failed: " + e.getMessage());
                    // If network error, return null
                    future.complete(null);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body() != null ? response.body().string() : "null";

                    Log.d("HTTP", "⬅️ LOGIN response code: " + response.code());
                    Log.d("HTTP", "⬅️ LOGIN response body: " + responseBody);

                    // If server responded with success
                    if (response.isSuccessful()) {
                        try {
                            // Read response body

                            // Parse JSON object from string
                            JSONObject obj = new JSONObject(responseBody);

                            // Create User object from response
                            User user = new User(
                                    obj.getString("userName"), // Extract username
                                    obj.getString("password"), // Extract password
                                    obj.getInt("age"),         // Extract age
                                    obj.getString("fullName")  // Extract full name
                            );

                            // Complete with User object
                            future.complete(user);

                        } catch (Exception e) {
                            Log.e("HTTP", "❌ LOGIN parsing error: " + e.getMessage());
                            // If parsing fails -> complete with null
                            future.complete(null);
                        }
                    } else {
                        // If response is not successful -> complete with null
                        future.complete(null);
                    }
                }
            });

        } catch (Exception e) {
            Log.e("HTTP", "❌ LOGIN exception: " + e.getMessage());
            // General exception -> complete with null
            future.complete(null);
        }

        // Return future
        return future;
    }

    // =========================================================
    // UPDATE (PUT /api/users/{username})
    // =========================================================
    public static CompletableFuture<Boolean> updateUser(String username, User updatedUser) {
        // CompletableFuture for async result
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        try {
            // Build JSON with updated user info
            JSONObject json = new JSONObject();
            json.put("userName", updatedUser.getUserName());
            json.put("password", updatedUser.getPassword());
            json.put("fullName", updatedUser.getFullName());
            json.put("age", updatedUser.getAge());

            // Create request body
            RequestBody body = RequestBody.create(json.toString(), JSON);

            // Build PUT request
            Request request = new Request.Builder()
                    .url(BASE_URL + "/" + username) // Endpoint with username path
                    .put(body)                      // Use PUT method
                    .build();

            // Send async request
            client.newCall(request).enqueue(callbackBoolean(future));

        } catch (Exception e) {
            // On error -> complete false
            future.complete(false);
        }

        return future;
    }

    // =========================================================
    // PATCH (PATCH /api/users/{username})
    // =========================================================
    public static CompletableFuture<Boolean> patchUser(String username, Map<String, Object> updates) {
        // CompletableFuture for result
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        try {
            // Convert updates map into JSON object
            JSONObject json = new JSONObject(updates);

            // Create request body
            RequestBody body = RequestBody.create(json.toString(), JSON);

            // Build PATCH request
            Request request = new Request.Builder()
                    .url(BASE_URL + "/" + username) // Endpoint with username
                    .patch(body)                    // Use PATCH
                    .build();

            // Send async request
            client.newCall(request).enqueue(callbackBoolean(future));

        } catch (Exception e) {
            // On error -> false
            future.complete(false);
        }

        return future;
    }

    // =========================================================
    // DELETE (DELETE /api/users/{username})
    // =========================================================
    public static CompletableFuture<Boolean> deleteUser(String username) {
        // Future for result
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Build DELETE request
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + username) // Endpoint
                .delete()                       // Use DELETE
                .build();

        // Send async request
        client.newCall(request).enqueue(callbackBoolean(future));
        return future;
    }

    // =========================================================
    // HEAD (HEAD /api/users/{username})
    // =========================================================
    public static CompletableFuture<Boolean> headUser(String username) {
        // Future for result
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Build HEAD request
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + username) // Endpoint
                .head()                         // Use HEAD
                .build();

        // Send async request
        client.newCall(request).enqueue(callbackBoolean(future));
        return future;
    }

    // =========================================================
    // UPDATE BMI (PATCH /api/users/{username}/bmi?bmi=...)
    // =========================================================
    public static CompletableFuture<Boolean> updateBmi(String username, double bmi) {
        // Future for result
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Build PATCH request with query param bmi
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + username + "/bmi?bmi=" + bmi) // Add BMI as query
                .patch(RequestBody.create(new byte[0], null))       // Empty body required
                .build();

        // Send async request
        client.newCall(request).enqueue(callbackBoolean(future));
        return future;
    }

    // =========================================================
    // GET BMI (GET /api/users/{username})
    // =========================================================
    public static CompletableFuture<Double> getBmi(String username) {
        // Future for result
        CompletableFuture<Double> future = new CompletableFuture<>();

        // Build GET request
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + username) // Endpoint
                .get()                          // Use GET
                .build();

        // Send async request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // If request fails
                future.complete(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // If response is success
                if (response.isSuccessful()) {
                    try {
                        // Read response body
                        String body = response.body().string();
                        // Parse JSON
                        JSONObject obj = new JSONObject(body);
                        // Check if bmi exists
                        if (obj.has("bmi")) {
                            future.complete(obj.getDouble("bmi"));
                        } else {
                            future.complete(null);
                        }
                    } catch (Exception e) {
                        future.complete(null);
                    }
                } else {
                    future.complete(null);
                }
            }
        });

        return future;
    }

    // =========================================================
    // UPDATE WATER (PATCH /api/users/{username}/water?amount=...)
    // Sends a PATCH request to update the user's water intake
    // =========================================================
    public static CompletableFuture<Boolean> updateWater(String username, int amount) {
        // Future that will hold the result of the network call
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Build the request URL
        String url = BASE_URL + "/" + username + "/water?amount=" + amount;

        // Debug logs before sending request
        Log.d("HTTP", "➡️ Sending PATCH request to " + url);
        Log.d("HTTP", "📤 Request body: (empty)");

        // Build the PATCH request with an empty body
        Request request = new Request.Builder()
                .url(url)
                .patch(RequestBody.create(new byte[0], null))
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Log error if request fails
                Log.e("HTTP", "❌ updateWater request failed: " + e.getMessage());
                future.complete(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Capture status code and body
                int code = response.code();
                String body = response.body() != null ? response.body().string() : "";

                // Debug logs for response
                Log.d("HTTP", "⬅️ Received response for updateWater, code = " + code);
                Log.d("HTTP", "📥 Response body: " + body);

                // Complete the future depending on success
                if (response.isSuccessful()) {
                    Log.d("DEBUG", "✅ updateWater worked!");
                    future.complete(true);
                } else {
                    Log.d("DEBUG", "❌ updateWater failed with code " + code);
                    future.complete(false);
                }
            }
        });

        // Return the future immediately (async result will be set later)
        return future;
    }

    // =========================================================
    // GET WATER (GET /api/users/{username}/water)
    // =========================================================
    public static CompletableFuture<JSONObject> getWater(String username) {
        // Future for result
        CompletableFuture<JSONObject> future = new CompletableFuture<>();

        // Build GET request
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + username + "/water") // Endpoint
                .get()                                    // Use GET
                .build();

        // Send async request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // On failure
                future.complete(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // If success
                if (response.isSuccessful()) {
                    try {
                        // Read body
                        String body = response.body().string();
                        // Parse into JSONObject
                        JSONObject obj = new JSONObject(body);
                        // Complete with object
                        future.complete(obj);
                    } catch (Exception e) {
                        future.complete(null);
                    }
                } else {
                    future.complete(null);
                }
            }
        });

        return future;
    }

    // =========================================================
    // Helper: completes a Boolean future
    // =========================================================
    private static Callback callbackBoolean(CompletableFuture<Boolean> future) {
        return new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HTTP", "❌ Request failed: " + e.getMessage());
                // On failure -> false
                future.complete(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body() != null ? response.body().string() : "null";
                Log.d("HTTP", "⬅️ Response code: " + response.code());
                Log.d("HTTP", "⬅️ Response body: " + body);
                // On success -> true if status is 200-299
                future.complete(response.isSuccessful());
            }
        };
    }

    // =========================================================
    // Helper: completes a JSONObject future
    // =========================================================
    private static Callback callbackJson(CompletableFuture<JSONObject> future) {
        return new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // On failure -> null
                future.complete(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // If success
                if (response.isSuccessful()) {
                    try {
                        // Parse JSON
                        String body = response.body().string();
                        JSONObject obj = new JSONObject(body);
                        future.complete(obj);
                    } catch (Exception e) {
                        future.complete(null);
                    }
                } else {
                    future.complete(null);
                }
            }
        };
    }


    // ---------------------------------------------------------------------
    // GET WATER HISTORY MAP
    // Calls: GET /api/users/{username}/waterHistoryMap?days=7
    // Returns: JSONObject {"2025-09-29":1200, "2025-09-28":2000, ...}
    // ---------------------------------------------------------------------
    public static CompletableFuture<JSONObject> getWaterHistoryMap(String username, int days) {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();

        // Build the URL for water history map
        String url = BASE_URL + "/" + username + "/waterHistoryMap?days=" + days;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        // Send request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Log error and complete future with null
                Log.e("HTTP", "❌ getWaterHistoryMap failed", e);
                future.complete(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (Response r = response) {
                    // If response is not successful, complete with null
                    if (!r.isSuccessful()) {
                        Log.e("HTTP", "❌ getWaterHistoryMap error code=" + r.code());
                        future.complete(null);
                        return;
                    }
                    // Read body and log response
                    String body = r.body() != null ? r.body().string() : "{}";
                    Log.d("HTTP", "📥 getWaterHistoryMap response=" + body);
                    // Parse JSON and complete future
                    future.complete(new JSONObject(body));
                } catch (Exception e) {
                    // Handle JSON parse errors
                    Log.e("HTTP", "❌ getWaterHistoryMap parse error", e);
                    future.complete(null);
                }
            }
        });

        return future;
    }

    // -------------------------------------------------------------
    // getWeeklyAverages
    // Calls: GET {BASE_URL}/{username}/weeklyAverages
    // Returns: CompletableFuture<Map<String, Integer>>
    // -------------------------------------------------------------
    public static CompletableFuture<Map<String, Integer>> getWeeklyAverages(String username) {
        CompletableFuture<Map<String, Integer>> future = new CompletableFuture<>();
        // Build the URL for weakly water history map
        String url = BASE_URL + "/" + username + "/weeklyAverages";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        // Send request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Log error and complete with exception
                Log.w("HTTP", "weeklyAverages onFailure: " + e.getMessage());
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody body = response.body()) {
                    // Handle non-OK responses
                    if (!response.isSuccessful()) {
                        String resp = (body != null ? body.string() : "");
                        Log.w("HTTP", "⚠️ Non-OK weeklyAverages: " + response.code() + " body=" + resp);
                        future.complete(Collections.emptyMap());
                        return;
                    }

                    // Parse JSON response
                    String json = (body != null ? body.string() : "{}");
                    JSONObject obj = new JSONObject(json);

                    // Use LinkedHashMap to preserve order
                    Map<String, Integer> map = new LinkedHashMap<>();
                    Iterator<String> keys = obj.keys();
                    // Iterate over JSON keys and populate map
                    while (keys.hasNext()) {
                        String k = keys.next();
                        map.put(k, obj.optInt(k, 0));
                    }

                    // Complete future with parsed map
                    future.complete(map);
                } catch (Exception e) {
                    // Handle parsing exceptions
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }
}

