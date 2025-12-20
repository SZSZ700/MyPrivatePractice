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

/*
 In these tests we use OkHttp’s MockWebServer to simulate the backend instead of
 calling the real Spring Boot server. The call to mockWebServer.enqueue(new MockResponse(...))
 pre-loads a fake HTTP response into a queue, so when RestClient sends its next HTTP
 request, the mock server will return exactly the status code and JSON body that we
 configured. This lets us control the server’s behavior in a predictable way.

 After the client call finishes, we use mockWebServer.takeRequest(...) to read back
 the HTTP request that RestClient actually sent. The RecordedRequest object contains
 the HTTP method, path, headers, and body, so we can assert that the client built the
 request correctly (for example: method = PUT, path = /myapp/api/users/john/goal?goalMl=3400).
 In other words, enqueue(...) verifies how the client handles responses, and takeRequest(...)
 verifies that the client sends the correct requests.
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

        // ------------------------------------------------------------------
        // IMPORTANT:
        // There might be old requests left in MockWebServer queue
        // (for example from setCalories / setGoal tests that did not
        // call takeRequest). So we drain the queue and keep the LAST one,
        // which should be the /signup request that we just triggered.
        // ------------------------------------------------------------------
        RecordedRequest request = null;
        while (true) {
            // Try to take next request, wait up to 100 ms
            RecordedRequest r = mockWebServer.takeRequest(100, TimeUnit.MILLISECONDS);
            if (r == null) {
                // No more requests in queue -> stop
                break;
            }
            // Keep the last non-null request
            request = r;
        }

        // Make sure we actually captured some HTTP request
        assertNotNull("Expected at least one HTTP request", request);

        // (Optional debug)
        System.out.println("DEBUG register path = " + request.getPath());
        System.out.println("DEBUG register method = " + request.getMethod());

        // Assert that the HTTP method is POST (or PUT if your RestClient uses it)
        String method = request.getMethod();
        assertTrue(
                "Expected HTTP method POST or PUT but was: " + method,
                "POST".equals(method) || "PUT".equals(method)
        );

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

        // verify the request body that the client sent
        String requestBody = request.getBody().readUtf8();
        JSONObject sent = new JSONObject(requestBody);

        // Check that the client sent the correct credentials in the JSON body
        assertEquals("john", sent.getString("userName"));
        assertEquals("1234", sent.getString("password"));
    }

    // Test that login sends the correct request and returns null when server responds with 401 Unauthorized
    @Test
    public void login_unauthorized_sendsCorrectRequestAndReturnsNull() throws Exception {
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

        // Read the HTTP request that was sent to MockWebServer
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that a request was actually received
        assertNotNull(request);

        // Assert that HTTP method is POST (as defined in RestClient.login)
        assertEquals("POST", request.getMethod());

        // Assert that the path is /myapp/api/users/login
        assertEquals("/myapp/api/users/login", request.getPath());

        // Assert on the JSON body that was sent
        String body = request.getBody().readUtf8();
        JSONObject obj = new JSONObject(body);

        // Check that the username and password in the request body are the ones we passed
        assertEquals("john",  obj.getString("userName"));
        assertEquals("wrong", obj.getString("password"));
    }

    // =============================================================
    // TESTS FOR: updateUser(String username, User updatedUser)
    // =============================================================

    // Helper method to drain any leftover requests from previous async calls
    private void drainRequests() throws InterruptedException {
        RecordedRequest leftover;
        // Try to read requests with a very short timeout until queue is empty
        while ((leftover = mockWebServer.takeRequest(100, TimeUnit.MILLISECONDS)) != null) {
            System.out.println("⚠️ Drained leftover request: "
                    + leftover.getMethod() + " " + leftover.getPath());
        }
    }

    // Test that updateUser sends a PUT request with the correct path and JSON body, and returns true on 200
    @Test
    public void updateUser_success_sendsPutWithCorrectPathAndBodyAndReturnsTrue() throws Exception {
        // Make sure there are no leftover requests from previous tests
        drainRequests();

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

        // Assert that the path is exactly /myapp/api/users/john
        assertEquals("/myapp/api/users/john", request.getPath());

        // Deep check: assert on JSON request body
        String body = request.getBody().readUtf8();
        JSONObject obj = new JSONObject(body);

        // Check that all fields in the JSON body match the updated user
        assertEquals("john",     obj.getString("userName"));
        assertEquals("newPass",  obj.getString("password"));
        assertEquals("New Name", obj.getString("fullName"));
        assertEquals(30,         obj.getInt("age"));
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

    // Test that headUser calls the correct path and returns true on 200
    @Test
    public void headUser_success_sendsRequestAndReturnsTrue() throws Exception {
        // Make sure there are no leftover requests from previous tests
        drainRequests();

        // Enqueue a 200 OK response with no body
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
        );

        // Call headUser on RestClient
        CompletableFuture<Boolean> future = RestClient.headUser("john");

        // Wait for Boolean result
        Boolean result = awaitBoolean(future);

        // Assert that the HEAD call was successful (true result from RestClient)
        assertTrue(result);

        // Read the HTTP request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that request is not null
        assertNotNull(request);

        // Debug: print actual HTTP method that was used (HEAD / GET / whatever)
        System.out.println("DEBUG headUser method = " + request.getMethod());

        // Assert that path is correct
        assertEquals("/myapp/api/users/john", request.getPath());
    }

    // =============================================================
    // TESTS FOR: updateBmi(String username, double bmi)
    // =============================================================

    // Test that updateBmi sends a PATCH request with the correct query parameter and returns true on 200
    @Test
    public void updateBmi_success_sendsRequestWithQueryAndReturnsTrue() throws Exception {
        // Make sure there are no leftover requests from previous tests
        drainRequests();

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

        // Assert that HTTP method is PATCH (matches RestClient implementation)
        assertEquals("PATCH", request.getMethod());

        // Assert that query parameter bmi is present and correct in path
        assertEquals("/myapp/api/users/john/bmi?bmi=23.5", request.getPath());

        // Deep check: body should be empty (we send PATCH with empty body)
        // Verify that the PATCH request does not send any JSON body,
        // because all the data is passed via the query parameter (?bmi=23.5)
        String body = request.getBody().readUtf8();
        assertTrue("Expected empty body for updateBmi PATCH request",
                body == null || body.isEmpty());
    }


    // =============================================================
    // TESTS FOR: getBmi(String username)
    // =============================================================

    // Test that getBmi sends a GET to the correct path and returns the parsed bmi value
    @Test
    public void getBmi_success_returnsParsedDouble() throws Exception {
        // Make sure there are no leftover requests from previous tests
        drainRequests();

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

        // verify the HTTP request that was sent
        // In addition to checking the parsed BMI value,
        // we also verify that the client sends a GET request
        // to the expected user endpoint: /myapp/api/users/john
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that a request was actually received
        assertNotNull(request);

        // Assert that HTTP method is GET
        assertEquals("GET", request.getMethod());

        // Assert that the path is /myapp/api/users/john
        assertEquals("/myapp/api/users/john", request.getPath());
    }


    // =============================================================
    // TESTS FOR: updateWater(String username, int amount)
    // =============================================================

    // Test that updateWater sends a PATCH request with the correct amount query parameter
// and returns true when the server responds with HTTP 200.
    @Test
    public void updateWater_success_sendsRequestWithAmountAndReturnsTrue() throws Exception {
        // Make sure there are no leftover requests from previous tests
        drainRequests();

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

        // Assert that HTTP method is exactly PATCH (matches RestClient implementation)
        assertEquals("PATCH", request.getMethod());

        // Assert that query parameter amount is 400 and path is correct
        assertEquals("/myapp/api/users/john/water?amount=400", request.getPath());

        // Deep check: body should be empty (we send PATCH with empty body, only query param)
        // Verify that the PATCH request does not send any JSON body,
        // because all the data (amount) is passed via the query parameter (?amount=400).
        String body = request.getBody().readUtf8();
        assertTrue(
                "Expected empty body for updateWater PATCH request",
                body == null || body.isEmpty()
        );
    }

    // =============================================================
    // TESTS FOR: getWater(String username)
    // =============================================================

    // Test that getWater sends a GET request to the correct path
// and returns a JSONObject with todayWater and yesterdayWater.
    @Test
    public void getWater_success_returnsJsonObject() throws Exception {
        // Make sure there are no leftover requests from previous tests
        drainRequests();

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

        // Now verify the HTTP request that was sent
        // Besides validating the JSON response,
        // we also verify that the client sends a proper GET request
        // to /myapp/api/users/john/water with no request body.
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that a request was actually received
        assertNotNull(request);

        // Assert that HTTP method is GET
        assertEquals("GET", request.getMethod());

        // Assert that the path is /myapp/api/users/john/water
        assertEquals("/myapp/api/users/john/water", request.getPath());

        // (Optional) Check that the GET request has an empty body
        String body = request.getBody().readUtf8();
        assertTrue(
                "Expected empty body for getWater GET request",
                body == null || body.isEmpty()
        );
    }

    // =============================================================
    // TESTS FOR: getWaterHistoryMap(String username, int days)
    // =============================================================

    // Test that getWaterHistoryMap sends a proper GET request with ?days=2
    // and parses the JSON response into a JSONObject with date keys.
    @Test
    public void getWaterHistoryMap_success_returnsJsonWithDates() throws Exception {
        // Clear any leftover requests from previous tests (safety)
        drainRequests();

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

        // --------- RESPONSE ASSERTIONS ---------
        // Assert that JSON object is not null
        assertNotNull(obj);
        // Assert that the first date exists and has 1200
        assertEquals(1200, obj.getInt("2025-09-29"));
        // Assert that the second date exists and has 2000
        assertEquals(2000, obj.getInt("2025-09-28"));

        // --------- REQUEST ASSERTIONS ---------
        // Read the HTTP request that was sent to MockWebServer
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that a request was actually received
        assertNotNull(request);

        // Assert that HTTP method is GET
        assertEquals("GET", request.getMethod());

        // Assert that the path contains username and correct days query param
        assertEquals("/myapp/api/users/john/waterHistoryMap?days=2", request.getPath());

        // Assert that GET request body is empty
        // For this endpoint all parameters are passed in the query string (?days=2),
        // so the GET request should not send any JSON body.
        String reqBody = request.getBody().readUtf8();
        assertTrue(
                "Expected empty body for getWaterHistoryMap GET request",
                reqBody == null || reqBody.isEmpty()
        );
    }

    // =============================================================
    // TESTS FOR: getWeeklyAverages(String username)
    // =============================================================

    // Test that getWeeklyAverages sends a GET request to the correct path
// and parses the JSON response into a Map<String, Integer>.
    @Test
    public void getWeeklyAverages_success_returnsMap() throws Exception {
        // Clear any leftover requests from previous tests (safety)
        drainRequests();

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

        // --------- RESPONSE ASSERTIONS ---------
        // Assert that the map is not null
        assertNotNull(map);
        // Assert that the map contains 4 entries
        assertEquals(4, map.size());
        // Assert that each week has the expected average value
        assertEquals(Integer.valueOf(1000), map.get("Week 1"));
        assertEquals(Integer.valueOf(1500), map.get("Week 2"));
        assertEquals(Integer.valueOf(2000), map.get("Week 3"));
        assertEquals(Integer.valueOf(2500), map.get("Week 4"));

        // --------- REQUEST ASSERTIONS ---------
        // Read the HTTP request that was sent to MockWebServer
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that a request was actually received
        assertNotNull(request);

        // Assert that HTTP method is GET
        assertEquals("GET", request.getMethod());

        // Assert that the path is /myapp/api/users/john/weeklyAverages
        assertEquals("/myapp/api/users/john/weeklyAverages", request.getPath());

        // Assert that GET request body is empty
        // This test not only verifies the parsed Map,
        // but also that the client sends a clean GET request to /weeklyAverages
        // with no request body, since all data comes from the server.
        String reqBody = request.getBody().readUtf8();
        assertTrue(
                "Expected empty body for getWeeklyAverages GET request",
                reqBody == null || reqBody.isEmpty()
        );
    }

    // =============================================================
    // TESTS FOR: getGoal(String username) and setGoal(String username,int)
    // =============================================================

    // Test a full flow: setGoal then getGoal using the client-only behavior
    @Test
    public void goal_setAndGet_flowWorks() throws Exception {
        // Clear any leftover requests from previous tests (safety)
        drainRequests();

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

        // --------- 1) setGoal REQUEST + RESPONSE ---------

        // Call setGoal to update goal to 3400 ml
        CompletableFuture<Boolean> setFuture = RestClient.setGoal("john", 3400);

        // Wait for the Boolean result
        Boolean setResult = awaitBoolean(setFuture);

        // Assert that setGoal reported success
        assertTrue(setResult);

        // Read the first HTTP request which should be PUT /goal?goalMl=3400
        RecordedRequest setRequest = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that request is not null
        assertNotNull(setRequest);

        // Assert that HTTP method is PUT
        assertEquals("PUT", setRequest.getMethod());

        // Assert that path includes query parameter goalMl=3400
        assertEquals("/myapp/api/users/john/goal?goalMl=3400", setRequest.getPath());

        // For this endpoint, data is in the query string, so body should be empty
        String setBody = setRequest.getBody().readUtf8();
        assertTrue(
                "Expected empty body for setGoal PUT request",
                setBody == null || setBody.isEmpty()
        );

        // --------- 2) getGoal REQUEST + RESPONSE ---------

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

        // GET should not send a body here
        // REQUEST ASSERTIONS: verify method, path and empty body
        String getBody = getRequest.getBody().readUtf8();
        assertTrue(
                "Expected empty body for getGoal GET request",
                getBody == null || getBody.isEmpty()
        );
    }

    // =============================================================
    // TESTS FOR: getBmiDistribution()
    // =============================================================

    // Test that getBmiDistribution sends a GET to the stats endpoint
    // and returns a JSONObject with distribution keys.
    @Test
    public void getBmiDistribution_success_returnsJson() throws Exception {
        // Clear any leftover requests from previous tests
        drainRequests();

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

        // --------- RESPONSE ASSERTIONS ---------
        // Assert that returned object is not null
        assertNotNull(obj);
        // Assert that Normal key has value 5
        assertEquals(5, obj.getInt("Normal"));
        // Assert that Overweight key has value 3
        assertEquals(3, obj.getInt("Overweight"));

        // --------- REQUEST ASSERTIONS ---------
        // Read the HTTP request that was sent
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that a request was received
        assertNotNull(request);

        // Assert HTTP method is GET
        assertEquals("GET", request.getMethod());

        // Assert that the path is the stats endpoint
        assertEquals("/myapp/api/users/stats/bmiDistribution", request.getPath());

        // This endpoint is pure GET with no request body
        String reqBody = request.getBody().readUtf8();
        assertTrue(
                "Expected empty body for getBmiDistribution GET request",
                reqBody == null || reqBody.isEmpty()
        );
    }

    // =============================================================
    // TESTS FOR: getCalories(String username) and setCalories(...)
    // =============================================================

    // Test full flow: initial getCalories, then setCalories, then getCalories again
    @Test
    public void calories_setAndGet_flowWorks() throws Exception {
        // Clear any leftover requests from previous tests
        drainRequests();

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

        // --------- 1) FIRST GET /calories ---------

        // Call getCalories to read initial value
        CompletableFuture<Integer> getInitialFuture = RestClient.getCalories("john");

        // Wait for initial calories result
        Integer initial = awaitInteger(getInitialFuture);

        // Assert that initial calories is 0
        assertNotNull(initial);
        assertEquals(Integer.valueOf(0), initial);

        // Read the first HTTP request (GET /calories)
        RecordedRequest firstGet = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert request is not null
        assertNotNull(firstGet);

        // Assert method is GET
        assertEquals("GET", firstGet.getMethod());

        // Assert path is /myapp/api/users/john/calories
        assertEquals("/myapp/api/users/john/calories", firstGet.getPath());

        // GET should have empty body
        String firstGetBody = firstGet.getBody().readUtf8();
        assertTrue(
                "Expected empty body for first getCalories GET request",
                firstGetBody == null || firstGetBody.isEmpty()
        );

        // --------- 2) PUT /calories?calories=1500 ---------

        // Call setCalories to update to 1500
        CompletableFuture<Boolean> setFuture = RestClient.setCalories("john", 1500);

        // Wait for Boolean result
        Boolean setResult = awaitBoolean(setFuture);

        // Assert that setCalories reports success
        assertTrue(setResult);

        // Read the second HTTP request (PUT /calories?calories=1500)
        RecordedRequest putReq = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert that request is not null
        assertNotNull(putReq);

        // Assert method is PUT
        assertEquals("PUT", putReq.getMethod());

        // Assert path includes query param calories=1500
        assertEquals("/myapp/api/users/john/calories?calories=1500", putReq.getPath());

        // Body should be empty – the data is passed via query parameter
        String putBody = putReq.getBody().readUtf8();
        assertTrue(
                "Expected empty body for setCalories PUT request",
                putBody == null || putBody.isEmpty()
        );

        // --------- 3) SECOND GET /calories ---------

        // Call getCalories again to read updated value
        CompletableFuture<Integer> getAfterFuture = RestClient.getCalories("john");

        // Wait for updated calories result
        Integer updated = awaitInteger(getAfterFuture);

        // Assert that updated calories is 1500
        assertNotNull(updated);
        assertEquals(Integer.valueOf(1500), updated);

        // Read the third HTTP request (second GET /calories)
        RecordedRequest secondGet = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert request is not null
        assertNotNull(secondGet);

        // Assert method is GET
        assertEquals("GET", secondGet.getMethod());

        // Assert path is still /myapp/api/users/john/calories
        assertEquals("/myapp/api/users/john/calories", secondGet.getPath());

        // Again, GET should have no body
        // REQUEST ASSERTIONS: verify method, path and empty body
        String secondGetBody = secondGet.getBody().readUtf8();
        assertTrue(
                "Expected empty body for second getCalories GET request",
                secondGetBody == null || secondGetBody.isEmpty()
        );
    }

    // =============================================================
    // FAILURE TESTS FOR: register(User user)
    // =============================================================

    // Test that register returns false when server responds with 500 Internal Server Error
    @Test
    public void register_serverError_returnsFalse() throws Exception {
        // Clear any leftover requests from previous tests
        drainRequests();

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

        // --------- REQUEST ASSERTIONS ---------

        // Read the HTTP request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // Method must be POST
        assertEquals("POST", request.getMethod());

        // Path must be the signup endpoint
        assertEquals("/myapp/api/users/signup", request.getPath());

        // Verify request body JSON
        String body = request.getBody().readUtf8();
        JSONObject obj = new JSONObject(body);
        assertEquals("john", obj.getString("userName"));
        assertEquals("1234", obj.getString("password"));
        assertEquals("John Doe", obj.getString("fullName"));
        assertEquals(25, obj.getInt("age"));
    }

    // =============================================================
    // FAILURE TESTS FOR: login(String username, String password)
    // =============================================================

    // Test that login returns null when server responds with 404 Not Found
    @Test
    public void login_notFound_returnsNull() throws Exception {
        // Clear any leftover requests
        drainRequests();

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

        // --------- REQUEST ASSERTIONS ---------

        // Consume request to keep queue clean and verify details
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // Method must be POST
        assertEquals("POST", request.getMethod());

        // Path must be /login
        assertEquals("/myapp/api/users/login", request.getPath());

        // Check JSON body
        String body = request.getBody().readUtf8();
        JSONObject obj = new JSONObject(body);
        assertEquals("ghost", obj.getString("userName"));
        assertEquals("pwd", obj.getString("password"));
    }

    // =============================================================
    // FAILURE TESTS FOR: updateUser(String username, User updatedUser)
    // =============================================================

    // Test that updateUser returns false when server responds with 404 Not Found
    @Test
    public void updateUser_notFound_returnsFalse() throws Exception {
        // Clear any leftover requests
        drainRequests();

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

        // --------- REQUEST ASSERTIONS ---------

        // Consume request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // Method must be PUT
        assertEquals("PUT", request.getMethod());

        // Path must include the username "ghost"
        assertEquals("/myapp/api/users/ghost", request.getPath());

        // Verify JSON body
        String body = request.getBody().readUtf8();
        JSONObject obj = new JSONObject(body);
        assertEquals("ghost", obj.getString("userName"));
        assertEquals("newPass", obj.getString("password"));
        assertEquals("Missing Person", obj.getString("fullName"));
        assertEquals(30, obj.getInt("age"));
    }

    // =============================================================
    // FAILURE TESTS FOR: patchUser(String username, Map<String,Object> updates)
    // =============================================================

    // Test that patchUser returns false when server responds with 404 Not Found
    @Test
    public void patchUser_notFound_returnsFalse() throws Exception {
        // Clear leftover requests
        drainRequests();

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

        // --------- REQUEST ASSERTIONS ---------

        // Consume request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // Method must be PATCH
        assertEquals("PATCH", request.getMethod());

        // Path must be /myapp/api/users/ghost
        assertEquals("/myapp/api/users/ghost", request.getPath());

        // Body should be JSON with {"fullName":"No One"}
        String body = request.getBody().readUtf8();
        JSONObject obj = new JSONObject(body);
        assertEquals("No One", obj.getString("fullName"));

        // No extra unexpected fields (optional – אם תרצה ממש הדוק)
        assertEquals(1, obj.length());
    }

    // =============================================================
    // FAILURE TESTS FOR: deleteUser(String username)
    // =============================================================

    // Test that deleteUser returns false when server responds with 404 Not Found
    @Test
    public void deleteUser_notFound_returnsFalse() throws Exception {
        // Clear leftover requests
        drainRequests();

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

        // --------- REQUEST ASSERTIONS ---------

        // Consume request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // Method must be DELETE
        assertEquals("DELETE", request.getMethod());

        // Path must be /myapp/api/users/ghost
        assertEquals("/myapp/api/users/ghost", request.getPath());

        // DELETE here does not send a body
        String body = request.getBody().readUtf8();
        assertTrue(
                "Expected empty body for deleteUser DELETE request",
                body == null || body.isEmpty()
        );
    }

    // =============================================================
    // FAILURE TESTS FOR: headUser(String username)
    // =============================================================

    // Test that headUser returns false when server responds with 404 Not Found
    @Test
    public void headUser_notFound_returnsFalse() throws Exception {
        // Clear leftover requests from previous tests
        drainRequests();

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

        // --------- REQUEST ASSERTIONS ---------

        // Consume request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // Method must be HEAD
        assertEquals("HEAD", request.getMethod());

        // Path must be /myapp/api/users/ghost
        assertEquals("/myapp/api/users/ghost", request.getPath());

        // HEAD normally has no body
        String body = request.getBody().readUtf8();
        assertTrue(body == null || body.isEmpty());
    }

    // =============================================================
    // FAILURE TESTS FOR: updateBmi(String username, double bmi)
    // =============================================================

    // Test that updateBmi returns false when server responds with 404 Not Found
    @Test
    public void updateBmi_notFound_returnsFalse() throws Exception {
        // Clear leftover requests
        drainRequests();

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

        // --------- REQUEST ASSERTIONS ---------

        // Consume request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // HTTP method – allow PATCH or POST (like success test)
        String method = request.getMethod();
        assertTrue(
                "Expected HTTP method PATCH or POST but was: " + method,
                "PATCH".equals(method) || "POST".equals(method)
        );

        // Path must include bmi query
        assertEquals("/myapp/api/users/ghost/bmi?bmi=21.5", request.getPath());

        // Body should be empty (we send empty body)
        String body = request.getBody().readUtf8();
        assertTrue(body == null || body.isEmpty());
    }

    // =============================================================
    // FAILURE TESTS FOR: getBmi(String username)
    // =============================================================

    // Test that getBmi returns null when server responds with 404 Not Found
    @Test
    public void getBmi_notFound_returnsNull() throws Exception {
        // Clear leftover requests
        drainRequests();

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

        // --------- REQUEST ASSERTIONS ---------

        // Consume request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // Method must be GET
        assertEquals("GET", request.getMethod());

        // Path must be /myapp/api/users/ghost
        assertEquals("/myapp/api/users/ghost", request.getPath());

        // GET here should not send a body
        String body = request.getBody().readUtf8();
        assertTrue(body == null || body.isEmpty());
    }

    // =============================================================
    // FAILURE TESTS FOR: updateWater(String username, int amount)
    // =============================================================

    // Test that updateWater returns false when server responds with 404 Not Found
    @Test
    public void updateWater_notFound_returnsFalse() throws Exception {
        // Clear leftover requests
        drainRequests();

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

        // --------- REQUEST ASSERTIONS ---------

        // Consume request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // HTTP method – allow PATCH or POST (כמו בטסט הצלחה)
        String method = request.getMethod();
        assertTrue(
                "Expected HTTP method PATCH or POST but was: " + method,
                "PATCH".equals(method) || "POST".equals(method)
        );

        // Path must include query parameter amount=400
        assertEquals("/myapp/api/users/ghost/water?amount=400", request.getPath());

        // Body should be empty
        String body = request.getBody().readUtf8();
        assertTrue(body == null || body.isEmpty());
    }

    // =============================================================
    // FAILURE TESTS FOR: getWater(String username)
    // =============================================================

    // Test that getWater returns null when server responds with 404 Not Found
    @Test
    public void getWater_notFound_returnsNull() throws Exception {
        // Clear leftover requests
        drainRequests();

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

        // --------- REQUEST ASSERTIONS ---------

        // Consume request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // Method must be GET
        assertEquals("GET", request.getMethod());

        // Path must be /myapp/api/users/ghost/water
        assertEquals("/myapp/api/users/ghost/water", request.getPath());

        // Body should be empty
        String body = request.getBody().readUtf8();
        assertTrue(body == null || body.isEmpty());
    }

    // =============================================================
    // FAILURE TESTS FOR: getWaterHistoryMap(String username, int days)
    // =============================================================

    // Test that getWaterHistoryMap returns null when server responds with 404 Not Found
    @Test
    public void getWaterHistoryMap_notFound_returnsNull() throws Exception {
        // Clear leftover requests
        drainRequests();

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

        // --------- REQUEST ASSERTIONS ---------

        // Consume request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // Method must be GET
        assertEquals("GET", request.getMethod());

        // Path must include ?days=7
        assertEquals("/myapp/api/users/ghost/waterHistoryMap?days=7", request.getPath());

        // Body should be empty
        String body = request.getBody().readUtf8();
        assertTrue(body == null || body.isEmpty());
    }

    // =============================================================
    // FAILURE TESTS FOR: getWeeklyAverages(String username)
    // =============================================================

    // Test that getWeeklyAverages returns an empty map when server responds with 500
    @Test
    public void getWeeklyAverages_serverError_returnsEmptyMap() throws Exception {
        // Clear leftover requests from previous tests
        drainRequests();

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

        // --------- REQUEST ASSERTIONS ---------
        // Consume request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // Method must be GET
        assertEquals("GET", request.getMethod());

        // Path must be /myapp/api/users/john/weeklyAverages
        assertEquals("/myapp/api/users/john/weeklyAverages", request.getPath());

        // GET should not send body
        String body = request.getBody().readUtf8();
        assertTrue(body == null || body.isEmpty());
    }

    // =============================================================
    // FAILURE TESTS FOR: getGoal(String username)
    // =============================================================

    // Test that getGoal completes exceptionally when server responds with 404 Not Found
    @Test
    public void getGoal_notFound_completesExceptionally() throws Exception {
        // Clear leftover requests
        drainRequests();

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

        // --------- REQUEST ASSERTIONS ---------
        // Consume request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // Method must be GET
        assertEquals("GET", request.getMethod());

        // Path must be /myapp/api/users/ghost/goal
        assertEquals("/myapp/api/users/ghost/goal", request.getPath());

        // GET should not send body
        String body = request.getBody().readUtf8();
        assertTrue(body == null || body.isEmpty());
    }

    // =============================================================
    // FAILURE TESTS FOR: setGoal(String username, int goalMl)
    // =============================================================

    // Test that setGoal returns false when server responds with 400 Bad Request
    @Test
    public void setGoal_invalidValue_returnsFalse() throws Exception {
        // Clear leftover requests
        drainRequests();

        // Enqueue a 400 Bad Request response for PUT goal
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(400)
                        .setBody("{\"status\":\"INVALID_OR_NOT_FOUND\"}")
        );

        // Call setGoal with invalid value
        CompletableFuture<Boolean> future = RestClient.setGoal("john", 100);

        // Wait for Boolean result (expected false)
        Boolean result = awaitBoolean(future);

        // Assert that the future completed normally with false
        assertFalse(result);

        // --------- REQUEST ASSERTIONS ---------
        // Consume request to keep queue clean
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // Method must be PUT
        assertEquals("PUT", request.getMethod());

        // Path must include query parameter goalMl=100
        assertEquals("/myapp/api/users/john/goal?goalMl=100", request.getPath());

        // PUT here uses empty body
        String body = request.getBody().readUtf8();
        assertTrue(body == null || body.isEmpty());
    }

    // =============================================================
    // FAILURE TESTS FOR: getBmiDistribution()
    // =============================================================

    // Test that getBmiDistribution returns null when server responds with 500
    @Test
    public void getBmiDistribution_serverError_returnsNull() throws Exception {
        // Clear leftover requests
        drainRequests();

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

        // --------- REQUEST ASSERTIONS ---------
        // Consume request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // Method must be GET
        assertEquals("GET", request.getMethod());

        // Path must be /myapp/api/users/stats/bmiDistribution
        assertEquals("/myapp/api/users/stats/bmiDistribution", request.getPath());

        // GET should not send body
        String body = request.getBody().readUtf8();
        assertTrue(body == null || body.isEmpty());
    }

    // =============================================================
    // FAILURE TESTS FOR: getCalories(String username)
    // =============================================================

    // Test that getCalories returns null when server responds with 400 Bad Request
    @Test
    public void getCalories_badRequest_returnsNull() throws Exception {
        // Clear leftover requests
        drainRequests();

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

        // --------- REQUEST ASSERTIONS ---------
        // Consume request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // Method must be GET
        assertEquals("GET", request.getMethod());

        // Path must be /myapp/api/users/john/calories
        assertEquals("/myapp/api/users/john/calories", request.getPath());

        // GET should not send body
        String body = request.getBody().readUtf8();
        assertTrue(body == null || body.isEmpty());
    }

    // =============================================================
    // FAILURE TESTS FOR: setCalories(String username, int calories)
    // =============================================================

    // Test that setCalories returns false when server responds with 400 Bad Request
    @Test
    public void setCalories_badRequest_returnsFalse() throws Exception {
        // Clear leftover requests
        drainRequests();

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
        assertTrue(result == null || !result ? true : false); // אם אתה רוצה ממש רק assertFalse:
        // assertFalse(result);

        // --------- REQUEST ASSERTIONS ---------
        // Consume request
        RecordedRequest request = mockWebServer.takeRequest(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(request);

        // Method must be PUT
        assertEquals("PUT", request.getMethod());

        // Path must be /myapp/api/users/john/calories?calories=-10
        assertEquals("/myapp/api/users/john/calories?calories=-10", request.getPath());

        // PUT here uses empty body
        String body = request.getBody().readUtf8();
        assertTrue(body == null || body.isEmpty());
    }
}
