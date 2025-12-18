package com.example.myfinaltopapplication;
// Import assertions for JUnit tests
import static org.junit.Assert.*;
// Import Android Log for debug printing (available in instrumented tests)
import android.util.Log;
// Import JUnit4 runner for Android instrumented tests
import androidx.test.ext.junit.runners.AndroidJUnit4;
// Import JUnit annotations for lifecycle and tests
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
// Import JSON object for parsing and building JSON bodies
import org.json.JSONObject;
// Import reflection classes to override static client in RestClient
import java.lang.reflect.Field;
// Import collections for map-based responses
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
// Import concurrency utilities for waiting on CompletableFuture
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
// Import OkHttp client and HTTP URL for building test client
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
// Import MockWebServer classes for mocking HTTP server behavior
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

/**
 * RestClientTest - integration-like unit tests for the Android RestClient class
 * using OkHttp's MockWebServer. Each test verifies both:
 *  1) The HTTP request that RestClient sends (method, path, body).
 *  2) The way RestClient parses and exposes the response via CompletableFuture.
 */
@RunWith(AndroidJUnit4.class)
public class RestClientTest {

    // Hold a single MockWebServer instance shared by all tests
    private static MockWebServer mockWebServer;

    // Hold the original OkHttpClient from RestClient so we can restore it after tests
    private static OkHttpClient originalClient;

    // Define a timeout in seconds for waiting on CompletableFuture results
    private static final long FUTURE_TIMEOUT_SECONDS = 5L;

    // -------------------------------------------------------------
    // Setup before all tests
    // -------------------------------------------------------------
    @BeforeClass
    public static void setUpClass() throws Exception {
        // Create a new MockWebServer instance
        mockWebServer = new MockWebServer();
        // Start the mock server so it begins listening on an available port
        mockWebServer.start();

        // Log the URL of the mock server for debug purposes
        Log.d("TEST", "MockWebServer started at: " + mockWebServer.url("/"));

        // Use reflection to read the current static OkHttpClient from RestClient
        Field clientField = RestClient.class.getDeclaredField("client");
        // Allow access to the private static field
        clientField.setAccessible(true);
        // Save the original client so we can restore it later
        originalClient = (OkHttpClient) clientField.get(null);

        // Build a new OkHttpClient that rewrites the host and port to MockWebServer
        OkHttpClient testClient = new OkHttpClient.Builder()
                // Add an interceptor that changes each request URL to point to MockWebServer
                .addInterceptor(chain -> {
                    // Capture the original outgoing request
                    okhttp3.Request originalRequest = chain.request();
                    // Extract the original URL from the request
                    HttpUrl originalUrl = originalRequest.url();

                    // Build a new URL with the same path/query but MockWebServer host/port
                    HttpUrl newUrl = originalUrl.newBuilder()
                            .host(mockWebServer.getHostName())   // Use mock server host
                            .port(mockWebServer.getPort())       // Use mock server port
                            .build();

                    // Build a new request with the rewritten URL
                    okhttp3.Request newRequest = originalRequest.newBuilder()
                            .url(newUrl)
                            .build();

                    // Proceed with the chain using the rewritten request
                    return chain.proceed(newRequest);
                })
                // Build the configured test client
                .build();

        // Replace the static client in RestClient with our test client
        clientField.set(null, testClient);
    }

    // -------------------------------------------------------------
    // Cleanup after all tests
    // -------------------------------------------------------------
    @AfterClass
    public static void tearDownClass() throws Exception {
        // Use reflection to get the static client field in RestClient
        Field clientField = RestClient.class.getDeclaredField("client");
        // Allow access to the private field
        clientField.setAccessible(true);
        // Restore the original client instance back to RestClient
        clientField.set(null, originalClient);

        // Shut down the MockWebServer to free resources and port
        mockWebServer.shutdown();
    }

    // -------------------------------------------------------------
    // Helper method: wait for a Boolean future with timeout
    // -------------------------------------------------------------
    private Boolean awaitBoolean(CompletableFuture<Boolean> future) throws Exception {
        // Wait for the CompletableFuture to complete and return its Boolean value
        return future.get(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    // -------------------------------------------------------------
    // Helper method: wait for a Double future with timeout
    // -------------------------------------------------------------
    private Double awaitDouble(CompletableFuture<Double> future) throws Exception {
        // Wait for the CompletableFuture to complete and return its Double value
        return future.get(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    // -------------------------------------------------------------
    // Helper method: wait for an Integer future with timeout
    // -------------------------------------------------------------
    private Integer awaitInteger(CompletableFuture<Integer> future) throws Exception {
        // Wait for the CompletableFuture to complete and return its Integer value
        return future.get(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    // -------------------------------------------------------------
    // Helper method: wait for a JSONObject future with timeout
    // -------------------------------------------------------------
    private JSONObject awaitJson(CompletableFuture<JSONObject> future) throws Exception {
        // Wait for the CompletableFuture to complete and return its JSONObject value
        return future.get(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    // -------------------------------------------------------------
    // Helper method: wait for a Map<String,Integer> future with timeout
    // -------------------------------------------------------------
    private Map<String, Integer> awaitMap(CompletableFuture<Map<String, Integer>> future) throws Exception {
        // Wait for the CompletableFuture to complete and return its Map value
        return future.get(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    // =============================================================
    // TESTS FOR: register(User user)
    // =============================================================

    // Test that register returns true when server responds with 201 Created
    @Test
    public void register_success_returnsTrueAndSendsCorrectBody() throws Exception {
        // Enqueue a fake HTTP response with status 201 and success message
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(201)
                        .setBody("User created successfully")
        );

        // Create a sample User object with test data
        User user = new User("john", "1234", 25, "John Doe");

        // Call RestClient.register which will send the HTTP request asynchronously
        CompletableFuture<Boolean> future = RestClient.register(user);

        // Wait for the result of the CompletableFuture
        Boolean result = awaitBoolean(future);

        // Assert that the result is true (successful registration)
        assertTrue(result);

        // Read the HTTP request that MockWebServer received
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that a request was actually received
        assertNotNull(request);
        // Assert that the HTTP method is POST
        assertEquals("POST", request.getMethod());
        // Assert that the request path matches the signup endpoint
        assertEquals("/myapp/api/users/signup", request.getPath());

        // Read the request body as a UTF-8 string
        String body = request.getBody().readUtf8();
        // Parse the body as JSON
        JSONObject obj = new JSONObject(body);
        // Assert that JSON contains the expected username
        assertEquals("john", obj.getString("userName"));
        // Assert that JSON contains the expected password
        assertEquals("1234", obj.getString("password"));
        // Assert that JSON contains the expected full name
        assertEquals("John Doe", obj.getString("fullName"));
        // Assert that JSON contains the expected age
        assertEquals(25, obj.getInt("age"));
    }

    // Test that register returns false when server responds with 409 Conflict
    @Test
    public void register_conflict_returnsFalse() throws Exception {
        // Enqueue a fake HTTP response with status 409 and error message
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(409)
                        .setBody("Username already exists")
        );

        // Create a User object for the duplicate registration attempt
        User user = new User("john", "1234", 25, "John Doe");

        // Call RestClient.register which will send the HTTP request
        CompletableFuture<Boolean> future = RestClient.register(user);

        // Wait for the result
        Boolean result = awaitBoolean(future);

        // Assert that the result is false (registration failed)
        assertFalse(result);
    }

    // =============================================================
    // TESTS FOR: login(String username, String password)
    // =============================================================

    // Test that login returns a User object when server responds with 200 and valid JSON
    @Test
    public void login_success_returnsUserObject() throws Exception {
        // Build the JSON body that the server should return
        String jsonBody = "{ \"userName\":\"john\", \"password\":\"1234\", \"age\":25, \"fullName\":\"John Doe\" }";

        // Enqueue a fake 200 OK response with the JSON body
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(jsonBody)
                        .addHeader("Content-Type", "application/json")
        );

        // Call RestClient.login with valid credentials
        CompletableFuture<User> future = RestClient.login("john", "1234");

        // Wait for the User object result
        User user = future.get(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that the returned User is not null
        assertNotNull(user);
        // Assert that username matches the JSON
        assertEquals("john", user.getUserName());
        // Assert that password matches the JSON
        assertEquals("1234", user.getPassword());
        // Assert that age matches the JSON
        assertEquals(25, user.getAge());
        // Assert that full name matches the JSON
        assertEquals("John Doe", user.getFullName());

        // Read back the sent HTTP request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that the request is not null
        assertNotNull(request);
        // Assert that HTTP method is POST
        assertEquals("POST", request.getMethod());
        // Assert that the path is /myapp/api/users/login
        assertEquals("/myapp/api/users/login", request.getPath());
    }

    // Test that login returns null when server responds with 401 Unauthorized
    @Test
    public void login_unauthorized_returnsNull() throws Exception {
        // Enqueue a 401 Unauthorized response with an error message
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(401)
                        .setBody("Invalid username or password")
        );

        // Call login with wrong credentials
        CompletableFuture<User> future = RestClient.login("john", "wrong");

        // Wait for the result
        User user = future.get(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that result is null (login failed)
        assertNull(user);
    }

    // =============================================================
    // TESTS FOR: updateUser(String username, User updatedUser)
    // =============================================================

    // Test that updateUser sends PUT to the correct path and returns true on 200
    @Test
    public void updateUser_success_sendsPutAndReturnsTrue() throws Exception {
        // Enqueue a 200 OK response for updateUser
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody("{\"status\":\"OK\"}")
        );

        // Create an updated User object
        User updated = new User("john", "newPass", 30, "New Name");

        // Call updateUser on RestClient
        CompletableFuture<Boolean> future = RestClient.updateUser("john", updated);

        // Wait for Boolean result
        Boolean result = awaitBoolean(future);

        // Assert that the call was successful (true)
        assertTrue(result);

        // Read the HTTP request received by MockWebServer
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that request is not null
        assertNotNull(request);
        // Assert that the HTTP method is PUT
        assertEquals("PUT", request.getMethod());
        // Assert that the path includes the username
        assertEquals("/myapp/api/users/john", request.getPath());
    }

    // =============================================================
    // TESTS FOR: patchUser(String username, Map<String,Object> updates)
    // =============================================================

    // Test that patchUser sends PATCH and returns true on 200
    @Test
    public void patchUser_success_sendsPatchAndReturnsTrue() throws Exception {
        // Enqueue a 200 OK response for patchUser
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody("{\"fullName\":\"Patched Name\"}")
                        .addHeader("Content-Type", "application/json")
        );

        // Create a simple updates map with one field
        Map<String, Object> updates = new LinkedHashMap<>();
        // Put new fullName value into the map
        updates.put("fullName", "Patched Name");

        // Call patchUser on RestClient
        CompletableFuture<Boolean> future = RestClient.patchUser("john", updates);

        // Wait for Boolean result
        Boolean result = awaitBoolean(future);

        // Assert that the operation is reported as successful
        assertTrue(result);

        // Read back the HTTP request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that request is not null
        assertNotNull(request);
        // Assert that HTTP method is PATCH
        assertEquals("PATCH", request.getMethod());
        // Assert that path targets the correct user
        assertEquals("/myapp/api/users/john", request.getPath());
    }

    // =============================================================
    // TESTS FOR: deleteUser(String username)
    // =============================================================

    // Test that deleteUser sends DELETE and returns true on 200
    @Test
    public void deleteUser_success_sendsDeleteAndReturnsTrue() throws Exception {
        // Enqueue a 200 OK response for deleteUser
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody("User deleted")
        );

        // Call deleteUser on RestClient
        CompletableFuture<Boolean> future = RestClient.deleteUser("john");

        // Wait for the Boolean result
        Boolean result = awaitBoolean(future);

        // Assert that the delete operation is successful
        assertTrue(result);

        // Read the HTTP request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that a request was captured
        assertNotNull(request);
        // Assert that HTTP method is DELETE
        assertEquals("DELETE", request.getMethod());
        // Assert that path is correct
        assertEquals("/myapp/api/users/john", request.getPath());
    }

    // =============================================================
    // TESTS FOR: headUser(String username)
    // =============================================================

    // Test that headUser sends HEAD and returns true on 200
    @Test
    public void headUser_success_sendsHeadAndReturnsTrue() throws Exception {
        // Enqueue a 200 OK response with no body
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
        );

        // Call headUser on RestClient
        CompletableFuture<Boolean> future = RestClient.headUser("john");

        // Wait for Boolean result
        Boolean result = awaitBoolean(future);

        // Assert that the HEAD call was successful
        assertTrue(result);

        // Read the HTTP request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that request is not null
        assertNotNull(request);
        // Assert that HTTP method is HEAD
        assertEquals("HEAD", request.getMethod());
        // Assert that path is correct
        assertEquals("/myapp/api/users/john", request.getPath());
    }

    // =============================================================
    // TESTS FOR: updateBmi(String username, double bmi)
    // =============================================================

    // Test that updateBmi sends PATCH with query parameter and returns true on 200
    @Test
    public void updateBmi_success_sendsPatchWithQueryAndReturnsTrue() throws Exception {
        // Enqueue a 200 OK response for updateBmi
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody("BMI updated successfully")
        );

        // Call updateBmi on RestClient
        CompletableFuture<Boolean> future = RestClient.updateBmi("john", 23.5);

        // Wait for Boolean result
        Boolean result = awaitBoolean(future);

        // Assert that the BMI update is successful
        assertTrue(result);

        // Read the HTTP request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that request is not null
        assertNotNull(request);
        // Assert that HTTP method is PATCH
        assertEquals("PATCH", request.getMethod());
        // Assert that query parameter bmi is present and correct in path
        assertEquals("/myapp/api/users/john/bmi?bmi=23.5", request.getPath());
    }

    // =============================================================
    // TESTS FOR: getBmi(String username)
    // =============================================================

    // Test that getBmi returns the bmi value when JSON contains "bmi"
    @Test
    public void getBmi_success_returnsParsedDouble() throws Exception {
        // Enqueue a 200 OK response with a JSON body containing bmi
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody("{\"userName\":\"john\",\"bmi\":22.7}")
                        .addHeader("Content-Type", "application/json")
        );

        // Call getBmi on RestClient
        CompletableFuture<Double> future = RestClient.getBmi("john");

        // Wait for the Double result
        Double bmi = awaitDouble(future);

        // Assert that the BMI is not null
        assertNotNull(bmi);
        // Assert that the BMI value is correctly parsed
        assertEquals(22.7, bmi, 0.0001);
    }

    // =============================================================
    // TESTS FOR: updateWater(String username, int amount)
    // =============================================================

    // Test that updateWater sends PATCH with amount query parameter and returns true on 200
    @Test
    public void updateWater_success_sendsPatchAndReturnsTrue() throws Exception {
        // Enqueue a 200 OK response for updateWater
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody("Water updated successfully")
        );

        // Call updateWater to add 400 ml
        CompletableFuture<Boolean> future = RestClient.updateWater("john", 400);

        // Wait for Boolean result
        Boolean result = awaitBoolean(future);

        // Assert that updateWater reports success
        assertTrue(result);

        // Read the HTTP request from MockWebServer
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that a request was received
        assertNotNull(request);
        // Assert that HTTP method is PATCH
        assertEquals("PATCH", request.getMethod());
        // Assert that query parameter amount is 400
        assertEquals("/myapp/api/users/john/water?amount=400", request.getPath());
    }

    // =============================================================
    // TESTS FOR: getWater(String username)
    // =============================================================

    // Test that getWater returns a JSONObject with todayWater and yesterdayWater
    @Test
    public void getWater_success_returnsJsonObject() throws Exception {
        // Enqueue a 200 OK response with JSON body for water totals
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody("{\"todayWater\":1200,\"yesterdayWater\":800}")
                        .addHeader("Content-Type", "application/json")
        );

        // Call getWater on RestClient
        CompletableFuture<JSONObject> future = RestClient.getWater("john");

        // Wait for the JSONObject result
        JSONObject obj = awaitJson(future);

        // Assert that the JSON object is not null
        assertNotNull(obj);
        // Assert that todayWater is 1200
        assertEquals(1200, obj.getInt("todayWater"));
        // Assert that yesterdayWater is 800
        assertEquals(800, obj.getInt("yesterdayWater"));
    }

    // =============================================================
    // TESTS FOR: getWaterHistoryMap(String username, int days)
    // =============================================================

    // Test that getWaterHistoryMap parses JSON into a JSONObject with date keys
    @Test
    public void getWaterHistoryMap_success_returnsJsonWithDates() throws Exception {
        // Build a JSON body with two dates and their totals
        String body = "{ \"2025-09-29\": 1200, \"2025-09-28\": 2000 }";

        // Enqueue a 200 OK response with the JSON body
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(body)
                        .addHeader("Content-Type", "application/json")
        );

        // Call getWaterHistoryMap asking for 2 days
        CompletableFuture<JSONObject> future = RestClient.getWaterHistoryMap("john", 2);

        // Wait for the JSONObject result
        JSONObject obj = awaitJson(future);

        // Assert that JSON object is not null
        assertNotNull(obj);
        // Assert that the first date exists and has 1200
        assertEquals(1200, obj.getInt("2025-09-29"));
        // Assert that the second date exists and has 2000
        assertEquals(2000, obj.getInt("2025-09-28"));
    }

    // =============================================================
    // TESTS FOR: getWeeklyAverages(String username)
    // =============================================================

    // Test that getWeeklyAverages parses JSON into a Map<String,Integer>
    @Test
    public void getWeeklyAverages_success_returnsMap() throws Exception {
        // Build a JSON body with week labels and average values
        String body = "{ \"Week 1\": 1000, \"Week 2\": 1500, \"Week 3\": 2000, \"Week 4\": 2500 }";

        // Enqueue a 200 OK response with the JSON body
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(body)
                        .addHeader("Content-Type", "application/json")
        );

        // Call getWeeklyAverages on RestClient
        CompletableFuture<Map<String, Integer>> future = RestClient.getWeeklyAverages("john");

        // Wait for the Map result
        Map<String, Integer> map = awaitMap(future);

        // Assert that the map is not null
        assertNotNull(map);
        // Assert that the map contains 4 entries
        assertEquals(4, map.size());
        // Assert that each week has the expected average value
        assertEquals(Integer.valueOf(1000), map.get("Week 1"));
        assertEquals(Integer.valueOf(1500), map.get("Week 2"));
        assertEquals(Integer.valueOf(2000), map.get("Week 3"));
        assertEquals(Integer.valueOf(2500), map.get("Week 4"));
    }

    // =============================================================
    // TESTS FOR: getGoal(String username) and setGoal(String username,int)
    // =============================================================

    // Test a full flow: setGoal then getGoal using the client only behavior
    @Test
    public void goal_setAndGet_flowWorks() throws Exception {
        // Enqueue response for setGoal (PUT /goal)
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody("{\"status\":\"OK\"}")
                        .addHeader("Content-Type", "application/json")
        );

        // Enqueue response for getGoal (GET /goal)
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody("{\"goalMl\":3400}")
                        .addHeader("Content-Type", "application/json")
        );

        // Call setGoal to update goal to 3400 ml
        CompletableFuture<Boolean> setFuture = RestClient.setGoal("john", 3400);

        // Wait for the Boolean result
        Boolean setResult = awaitBoolean(setFuture);

        // Assert that setGoal reported success
        assertTrue(setResult);

        // Read the first request which should be PUT /goal
        RecordedRequest setRequest = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that request is not null
        assertNotNull(setRequest);
        // Assert that HTTP method is PUT
        assertEquals("PUT", setRequest.getMethod());
        // Assert that path includes query parameter goalMl=3400
        assertEquals("/myapp/api/users/john/goal?goalMl=3400", setRequest.getPath());

        // Call getGoal to read back the goal
        CompletableFuture<JSONObject> getFuture = RestClient.getGoal("john");

        // Wait for JSONObject result
        JSONObject goalObj = awaitJson(getFuture);

        // Assert that JSON object is not null
        assertNotNull(goalObj);
        // Assert that goalMl equals 3400
        assertEquals(3400, goalObj.getInt("goalMl"));

        // Read the second HTTP request which should be GET /goal
        RecordedRequest getRequest = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that second request is not null
        assertNotNull(getRequest);
        // Assert that HTTP method is GET
        assertEquals("GET", getRequest.getMethod());
        // Assert that path is the correct goal endpoint
        assertEquals("/myapp/api/users/john/goal", getRequest.getPath());
    }

    // =============================================================
    // TESTS FOR: getBmiDistribution()
    // =============================================================

    // Test that getBmiDistribution returns a JSONObject with distribution keys
    @Test
    public void getBmiDistribution_success_returnsJson() throws Exception {
        // Build a sample JSON distribution
        String body = "{ \"Underweight\": 2, \"Normal\": 5, \"Overweight\": 3, \"Obese\": 1 }";

        // Enqueue a 200 OK response with distribution JSON
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(body)
                        .addHeader("Content-Type", "application/json")
        );

        // Call getBmiDistribution on RestClient
        CompletableFuture<JSONObject> future = RestClient.getBmiDistribution();

        // Wait for JSONObject result
        JSONObject obj = awaitJson(future);

        // Assert that returned object is not null
        assertNotNull(obj);
        // Assert that Normal key has value 5
        assertEquals(5, obj.getInt("Normal"));
        // Assert that Overweight key has value 3
        assertEquals(3, obj.getInt("Overweight"));
    }

    // =============================================================
    // TESTS FOR: getCalories(String username) and setCalories(...)
    // =============================================================

    // Test full flow: initial getCalories, then setCalories, then getCalories again
    @Test
    public void calories_setAndGet_flowWorks() throws Exception {
        // Enqueue first GET /calories response with starting value 0
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody("{\"calories\":0}")
                        .addHeader("Content-Type", "application/json")
        );

        // Enqueue PUT /calories response with 204 No Content (successful update)
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(204)
        );

        // Enqueue second GET /calories response with updated value 1500
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody("{\"calories\":1500}")
                        .addHeader("Content-Type", "application/json")
        );

        // Call getCalories to read initial value
        CompletableFuture<Integer> getInitialFuture = RestClient.getCalories("john");

        // Wait for initial calories result
        Integer initial = awaitInteger(getInitialFuture);

        // Assert that initial calories is 0
        assertNotNull(initial);
        assertEquals(Integer.valueOf(0), initial);

        // Call setCalories to update to 1500
        CompletableFuture<Boolean> setFuture = RestClient.setCalories("john", 1500);

        // Wait for Boolean result
        Boolean setResult = awaitBoolean(setFuture);

        // Assert that setCalories reports success
        assertTrue(setResult);

        // Call getCalories again to read updated value
        CompletableFuture<Integer> getAfterFuture = RestClient.getCalories("john");

        // Wait for updated calories result
        Integer updated = awaitInteger(getAfterFuture);

        // Assert that updated calories is 1500
        assertNotNull(updated);
        assertEquals(Integer.valueOf(1500), updated);
    }

    // =============================================================
    // FAILURE TESTS FOR: register(User user)
    // =============================================================

    // Test that register returns false when server responds with 500 Internal Server Error
    @Test
    public void register_serverError_returnsFalse() throws Exception {
        // Enqueue a fake 500 response for the signup endpoint
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(500)
                        .setBody("Internal error")
        );

        // Create a sample user object
        User user = new User("john", "1234", 25, "John Doe");

        // Call RestClient.register which will hit MockWebServer
        CompletableFuture<Boolean> future = RestClient.register(user);

        // Wait for the Boolean result
        Boolean result = awaitBoolean(future);

        // Assert that the result is false because response is not successful (500)
        assertFalse(result);
    }

    // =============================================================
    // FAILURE TESTS FOR: login(String username, String password)
    // =============================================================

    // Test that login returns null when server responds with 404 Not Found
    @Test
    public void login_notFound_returnsNull() throws Exception {
        // Enqueue a 404 Not Found response for the login endpoint
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setBody("User not found")
        );

        // Call login with some username and password
        CompletableFuture<User> future = RestClient.login("ghost", "pwd");

        // Wait for User result (should be null)
        User user = future.get(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that login returned null on 404
        assertNull(user);
    }

    // =============================================================
    // FAILURE TESTS FOR: updateUser(String username, User updatedUser)
    // =============================================================

    // Test that updateUser returns false when server responds with 404 Not Found
    @Test
    public void updateUser_notFound_returnsFalse() throws Exception {
        // Enqueue a 404 response for updating a non-existing user
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setBody("User not found")
        );

        // Create an updated user object
        User updated = new User("ghost", "newPass", 30, "Missing Person");

        // Call updateUser on RestClient
        CompletableFuture<Boolean> future = RestClient.updateUser("ghost", updated);

        // Wait for Boolean result (expected false)
        Boolean result = awaitBoolean(future);

        // Assert that updateUser reports false on 404
        assertFalse(result);

        // Read the HTTP request from MockWebServer
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that the request was received
        assertNotNull(request);
        // Assert that the method is PUT
        assertEquals("PUT", request.getMethod());
        // Assert that the path targets the given username
        assertEquals("/myapp/api/users/ghost", request.getPath());
    }

    // =============================================================
    // FAILURE TESTS FOR: patchUser(String username, Map<String,Object> updates)
    // =============================================================

    // Test that patchUser returns false when server responds with 404 Not Found
    @Test
    public void patchUser_notFound_returnsFalse() throws Exception {
        // Enqueue a 404 response for patching non-existing user
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setBody("User not found")
        );

        // Create a map with some field to update
        Map<String, Object> updates = new LinkedHashMap<>();
        // Put a new fullName into updates
        updates.put("fullName", "No One");

        // Call patchUser on RestClient
        CompletableFuture<Boolean> future = RestClient.patchUser("ghost", updates);

        // Wait for Boolean result (expected false)
        Boolean result = awaitBoolean(future);

        // Assert that patchUser reports false on 404
        assertFalse(result);

        // Read the HTTP request from MockWebServer
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that request is not null
        assertNotNull(request);
        // Assert that HTTP method is PATCH
        assertEquals("PATCH", request.getMethod());
        // Assert that path targets the missing user
        assertEquals("/myapp/api/users/ghost", request.getPath());
    }

    // =============================================================
    // FAILURE TESTS FOR: deleteUser(String username)
    // =============================================================

    // Test that deleteUser returns false when server responds with 404 Not Found
    @Test
    public void deleteUser_notFound_returnsFalse() throws Exception {
        // Enqueue a 404 response for deleting non-existing user
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setBody("User not found")
        );

        // Call deleteUser on RestClient
        CompletableFuture<Boolean> future = RestClient.deleteUser("ghost");

        // Wait for Boolean result (expected false)
        Boolean result = awaitBoolean(future);

        // Assert that deleteUser reports false on 404
        assertFalse(result);

        // Read the HTTP request from MockWebServer
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that request was captured
        assertNotNull(request);
        // Assert that HTTP method is DELETE
        assertEquals("DELETE", request.getMethod());
        // Assert that path targets missing user
        assertEquals("/myapp/api/users/ghost", request.getPath());
    }

    // =============================================================
    // FAILURE TESTS FOR: headUser(String username)
    // =============================================================

    // Test that headUser returns false when server responds with 404 Not Found
    @Test
    public void headUser_notFound_returnsFalse() throws Exception {
        // Enqueue a 404 response for HEAD on non-existing user
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(404)
        );

        // Call headUser on RestClient
        CompletableFuture<Boolean> future = RestClient.headUser("ghost");

        // Wait for Boolean result (expected false)
        Boolean result = awaitBoolean(future);

        // Assert that headUser reports false on 404
        assertFalse(result);

        // Read HTTP request from MockWebServer
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert request is not null
        assertNotNull(request);
        // Assert method is HEAD
        assertEquals("HEAD", request.getMethod());
        // Assert path targets missing user
        assertEquals("/myapp/api/users/ghost", request.getPath());
    }

    // =============================================================
    // FAILURE TESTS FOR: updateBmi(String username, double bmi)
    // =============================================================

    // Test that updateBmi returns false when server responds with 404 Not Found
    @Test
    public void updateBmi_notFound_returnsFalse() throws Exception {
        // Enqueue a 404 response for updating BMI
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setBody("User not found")
        );

        // Call updateBmi on RestClient
        CompletableFuture<Boolean> future = RestClient.updateBmi("ghost", 21.5);

        // Wait for Boolean result (expected false)
        Boolean result = awaitBoolean(future);

        // Assert that updateBmi reports false on 404
        assertFalse(result);

        // Read the HTTP request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert request is not null
        assertNotNull(request);
        // Assert method is PATCH
        assertEquals("PATCH", request.getMethod());
        // Assert path includes bmi query parameter
        assertEquals("/myapp/api/users/ghost/bmi?bmi=21.5", request.getPath());
    }

    // =============================================================
    // FAILURE TESTS FOR: getBmi(String username)
    // =============================================================

    // Test that getBmi returns null when server responds with 404 Not Found
    @Test
    public void getBmi_notFound_returnsNull() throws Exception {
        // Enqueue a 404 response for GET user (no bmi field)
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setBody("User not found")
        );

        // Call getBmi on RestClient
        CompletableFuture<Double> future = RestClient.getBmi("ghost");

        // Wait for result (should be null)
        Double bmi = awaitDouble(future);

        // Assert that BMI is null on 404
        assertNull(bmi);
    }

    // =============================================================
    // FAILURE TESTS FOR: updateWater(String username, int amount)
    // =============================================================

    // Test that updateWater returns false when server responds with 404 Not Found
    @Test
    public void updateWater_notFound_returnsFalse() throws Exception {
        // Enqueue a 404 response for updating water
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setBody("User not found")
        );

        // Call updateWater on RestClient
        CompletableFuture<Boolean> future = RestClient.updateWater("ghost", 400);

        // Wait for Boolean result (expected false)
        Boolean result = awaitBoolean(future);

        // Assert that updateWater reports false on 404
        assertFalse(result);

        // Read HTTP request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that request is not null
        assertNotNull(request);
        // Assert method is PATCH
        assertEquals("PATCH", request.getMethod());
        // Assert path includes amount query parameter
        assertEquals("/myapp/api/users/ghost/water?amount=400", request.getPath());
    }

    // =============================================================
    // FAILURE TESTS FOR: getWater(String username)
    // =============================================================

    // Test that getWater returns null when server responds with 404 Not Found
    @Test
    public void getWater_notFound_returnsNull() throws Exception {
        // Enqueue a 404 response for GET water
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setBody("User not found")
        );

        // Call getWater on RestClient
        CompletableFuture<JSONObject> future = RestClient.getWater("ghost");

        // Wait for JSONObject result (expected null)
        JSONObject obj = awaitJson(future);

        // Assert that getWater returns null on 404
        assertNull(obj);
    }

    // =============================================================
    // FAILURE TESTS FOR: getWaterHistoryMap(String username, int days)
    // =============================================================

    // Test that getWaterHistoryMap returns null when server responds with 404 Not Found
    @Test
    public void getWaterHistoryMap_notFound_returnsNull() throws Exception {
        // Enqueue a 404 response for waterHistoryMap
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setBody("User not found")
        );

        // Call getWaterHistoryMap on RestClient
        CompletableFuture<JSONObject> future = RestClient.getWaterHistoryMap("ghost", 7);

        // Wait for JSONObject result (expected null)
        JSONObject obj = awaitJson(future);

        // Assert that result is null on 404
        assertNull(obj);
    }

    // =============================================================
    // FAILURE TESTS FOR: getWeeklyAverages(String username)
    // =============================================================

    // Test that getWeeklyAverages returns an empty map when server responds with 500
    @Test
    public void getWeeklyAverages_serverError_returnsEmptyMap() throws Exception {
        // Enqueue a 500 Internal Server Error response
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(500)
                        .setBody("Server error")
        );

        // Call getWeeklyAverages on RestClient
        CompletableFuture<Map<String, Integer>> future = RestClient.getWeeklyAverages("john");

        // Wait for Map result
        Map<String, Integer> map = awaitMap(future);

        // Assert that map is not null
        assertNotNull(map);
        // Assert that map is empty because method returns Collections.emptyMap() on non-OK
        assertTrue(map.isEmpty());
    }

    // =============================================================
    // FAILURE TESTS FOR: getGoal(String username)
    // =============================================================

    // Test that getGoal completes exceptionally when server responds with 404 Not Found
    @Test
    public void getGoal_notFound_completesExceptionally() throws Exception {
        // Enqueue a 404 response for GET goal
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setBody("{}")
        );

        // Call getGoal on RestClient
        CompletableFuture<JSONObject> future = RestClient.getGoal("ghost");

        // Define a flag to indicate that an exception was thrown
        boolean threw = false;

        // Try to wait for JSONObject result (expected to throw)
        try {
            awaitJson(future);
        } catch (Exception e) {
            // Mark that an exception occurred
            threw = true;
        }

        // Assert that the future completed exceptionally
        assertTrue(threw);
    }

    // =============================================================
    // FAILURE TESTS FOR: setGoal(String username, int goalMl)
    // =============================================================

    // Test that setGoal completes exceptionally when server responds with 400 Bad Request
    @Test
    public void setGoal_invalidValue_completesExceptionally() throws Exception {
        // Enqueue a 400 Bad Request response for PUT goal
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(400)
                        .setBody("{\"status\":\"INVALID_OR_NOT_FOUND\"}")
        );

        // Call setGoal with invalid value
        CompletableFuture<Boolean> future = RestClient.setGoal("john", 100);

        // Define a flag to mark that an exception was thrown
        boolean threw = false;

        // Try to wait for Boolean result (expected to throw ExecutionException)
        try {
            awaitBoolean(future);
        } catch (Exception e) {
            // Mark that exception was thrown
            threw = true;
        }

        // Assert that the future completed exceptionally
        assertTrue(threw);
    }

    // =============================================================
    // FAILURE TESTS FOR: getBmiDistribution()
    // =============================================================

    // Test that getBmiDistribution returns null when server responds with 500
    @Test
    public void getBmiDistribution_serverError_returnsNull() throws Exception {
        // Enqueue a 500 Internal Server Error response
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(500)
                        .setBody("Error")
        );

        // Call getBmiDistribution on RestClient
        CompletableFuture<JSONObject> future = RestClient.getBmiDistribution();

        // Wait for JSONObject result (expected null)
        JSONObject obj = awaitJson(future);

        // Assert that result is null on server error
        assertNull(obj);
    }

    // =============================================================
    // FAILURE TESTS FOR: getCalories(String username)
    // =============================================================

    // Test that getCalories returns null when server responds with 400 Bad Request
    @Test
    public void getCalories_badRequest_returnsNull() throws Exception {
        // Enqueue a 400 Bad Request response for GET calories
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(400)
                        .setBody("Bad request")
        );

        // Call getCalories on RestClient
        CompletableFuture<Integer> future = RestClient.getCalories("john");

        // Wait for Integer result (expected null)
        Integer value = awaitInteger(future);

        // Assert that getCalories returns null on 400
        assertNull(value);
    }

    // =============================================================
    // FAILURE TESTS FOR: setCalories(String username, int calories)
    // =============================================================

    // Test that setCalories returns false when server responds with 400 Bad Request
    @Test
    public void setCalories_badRequest_returnsFalse() throws Exception {
        // Enqueue a 400 Bad Request response for PUT calories
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(400)
                        .setBody("Invalid calories")
        );

        // Call setCalories with invalid value
        CompletableFuture<Boolean> future = RestClient.setCalories("john", -10);

        // Wait for Boolean result (expected false)
        Boolean result = awaitBoolean(future);

        // Assert that setCalories reports false on 400
        assertFalse(result);

        // Read HTTP request from MockWebServer
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert request is not null
        assertNotNull(request);
        // Assert method is PUT
        assertEquals("PUT", request.getMethod());
        // Assert path contains the invalid value in query parameter
        assertEquals("/myapp/api/users/john/calories?calories=-10", request.getPath());
    }
}
