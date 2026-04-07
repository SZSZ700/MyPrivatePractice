// Define the package for this integration test class
package CapstoneTests;
import org.example.CapstoneProject.Application;
// Import JUnit 5 test annotations
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
// Import the lifecycle enum for @TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle;
// Import static assertions from JUnit for readability
import static org.junit.jupiter.api.Assertions.*;
// Import the Spring Boot test annotation to load the full application context
import org.springframework.boot.test.context.SpringBootTest;
// Import Autowired to inject beans into this test class
import org.springframework.beans.factory.annotation.Autowired;
// Import the FirebaseService to prepare data directly in Firebase for tests
import org.example.CapstoneProject.service.FirebaseService;
// Import the User model used in requests and responses
import org.example.CapstoneProject.model.User;
// Import Spring's TestRestTemplate for real HTTP calls to the running server
import org.springframework.boot.test.web.client.TestRestTemplate;
// Import ResponseEntity and HttpStatus for inspecting HTTP responses
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
// Import HttpMethod for non-GET/POST HTTP verbs (PUT, PATCH, DELETE, HEAD)
import org.springframework.http.HttpMethod;
// Import HttpEntity to send request bodies for PUT/PATCH
import org.springframework.http.HttpEntity;
// Import Java TimeUnit for waiting on async FirebaseService operations in helpers
import java.util.concurrent.TimeUnit;
// Import Java utilities for maps and collections
import java.util.*;

// UsersControllerIntegrationTest is an end-to-end integration test class that
// verifies the REST API exposed by UsersController using a real embedded
// Spring Boot web server. Instead of calling FirebaseService directly, these
// tests use TestRestTemplate to perform real HTTP requests (GET, POST, PUT,
// PATCH, DELETE, HEAD) against the /api/users endpoints and assert on both
// the HTTP status codes and the response bodies. In this way, the class
// validates that request mappings, URL paths, query parameters, JSON
// serialization/deserialization, and error handling are all wired correctly
// on the controller layer, while FirebaseServiceIntegrationTest already
// ensures that the underlying FirebaseService logic works correctly with the
// database. Together, they give full confidence that the API behaves as
// expected from the client’s point of view.

// RestTemplate is a Spring HTTP client that allows us to call REST endpoints
// in a simple, type-safe way. Instead of manually opening connections,
// writing JSON, and parsing responses, we use RestTemplate to send HTTP
// requests (GET, POST, PUT, PATCH, DELETE, etc.) and automatically map
// the response into Java objects such as String, User, or Map. In this
// test class we use TestRestTemplate (a specialized version for tests) to
// simulate a real client calling our running Spring Boot server, so we can
// verify the full HTTP behavior of our controllers end-to-end.

// Mark this class as a Spring Boot integration test (loads the full context + web server)
@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
// Use a single test instance for the whole class (allows @BeforeAll non-static)
@TestInstance(Lifecycle.PER_CLASS)
public class UsersControllerIntegrationTest {

    // Inject TestRestTemplate to perform real HTTP calls to the running application
    @Autowired
    private TestRestTemplate restTemplate;

    // Inject FirebaseService to prepare test data directly in Firebase
    @Autowired
    private FirebaseService firebaseService;

    // Define a constant timeout in seconds for FirebaseService helper calls
    private final long TIMEOUT_SECONDS = 20L;

    // Keep track of all usernames created during this test class
    private final List<String> createdUsernames = new ArrayList<>();

    // --------------------------- HELPER METHODS ---------------------------

    // Helper method to build a basic User instance with required fields
    private User buildUser(String username, String password) {
        // Create a new User instance
        var user = new User();
        // Set the username field for this user
        user.setUserName(username);
        // Set the password field for this user
        user.setPassword(password);
        // set full name for clarity in debug
        user.setFullName("Test User " + username);
        // set an age for this user
        user.setAge(25);

        // Return the prepared user object
        return user;
    }

    // Helper method to create a user in Firebase directly using FirebaseService
    @SuppressWarnings("UnusedReturnValue")
    private User createUserInFirebase(String username, String password) throws Exception {
        // Create a new User object with the requested username and password
        var user = buildUser(username, password);
        // Call createUser on FirebaseService to persist this user
        var future = firebaseService.createUser(user);
        // Wait for the async result with a timeout
        var created = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // If user was created successfully, remember the username for cleanup
        if (created) { createdUsernames.add(username); }
        // Return the User object that was attempted to be created
        return user;
    }

    // Helper method to delete a user in Firebase safely
    private void deleteUserInFirebase(String username) {
        try {
            // Call deleteUser on FirebaseService
            var future = firebaseService.deleteUser(username);
            // Wait for the async result with a timeout
            @SuppressWarnings("unused") var deleted = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            // Log a warning but do not fail the whole test suite because of cleanup
            System.out.println("WARN cleanup failed for username=" + username
                    + " message=" + e.getMessage());
        }
    }

    // --------------------------- BASIC SETUP (OPTIONAL) ---------------------------

    // Optional setup method to ensure the context is ready before tests
    @BeforeAll
    void beforeAll() {
        // Print a debug message indicating that UsersControllerIntegrationTest started
        System.out.println("DEBUG UsersControllerIntegrationTest");
    }

    @AfterAll
    void cleanupAllTestUsers() {
        // Iterate over all created usernames and delete each one
        for (String username : this.createdUsernames) {
            deleteUserInFirebase(username);
        }
    }

    // --------------------------- HEALTH CHECK TEST ---------------------------

    // Test that the /api/users/health endpoint returns 200 OK with body "OK"
    @Test
    void health_returnsOk() {
        // Perform a GET request to /api/users/health and expect a String body
        var response = this.restTemplate.getForEntity("/api/users/health", String.class);

        // Assert that the HTTP status is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Assert that the response body is exactly "OK"
        assertEquals("OK", response.getBody());
    }

    // --------------------------- SIGNUP TESTS ---------------------------

    // Test that signup creates a new user and rejects duplicate usernames
    @Test
    void signup_createsUserAndRejectsDuplicate() {
        // Build a unique username using current timestamp to avoid collisions
        var username = "signupController_" + System.currentTimeMillis();
        // Build a User object for signup
        var user = buildUser(username, "signupPass");

        // Perform the first POST request to /api/users/signup sending the User as JSON
        var firstResponse = this.restTemplate.postForEntity("/api/users/signup", user, String.class);

        // Assert that the HTTP status is 201 Created
        assertEquals(HttpStatus.CREATED, firstResponse.getStatusCode());
        // Assert that the response body is the success message
        assertEquals("User created successfully", firstResponse.getBody());

        // Remember this username for cleanup (only if first call succeeded)
        this.createdUsernames.add(username);

        // Perform the second POST request with the same username to test duplicate handling
        var secondResponse = this.restTemplate.postForEntity("/api/users/signup", user, String.class);

        // Assert that the HTTP status is 409 Conflict
        assertEquals(HttpStatus.CONFLICT, secondResponse.getStatusCode());
        // Assert that the response body is the duplicate username message
        assertEquals("Username already exists", secondResponse.getBody());
    }

    // --------------------------- LOGIN TESTS ---------------------------

    // Test that login returns a User JSON object when credentials are correct
    @Test
    void login_withCorrectCredentials_returnsUserJson() throws Exception {
        // Build a unique username for this test
        var username = "loginOk_" + System.currentTimeMillis();
        // Create the user directly in Firebase with the correct password
        createUserInFirebase(username, "pass1");

        // Build a login request body User with same username and password
        var loginRequestUser = new User();
        // Set the username to the same value
        loginRequestUser.setUserName(username);
        // Set the password to the correct password
        loginRequestUser.setPassword("pass1");

        // Perform the POST request to /api/users/login sending User as JSON and expecting a User body
        var response = this.restTemplate.postForEntity("/api/users/login", loginRequestUser, User.class);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extract the User object from the response body
        var body = response.getBody();
        // Assert that the body is not null
        assertNotNull(body);
        // Assert that the username in the response matches the test username
        assertEquals(username, body.getUserName());
        // Assert that the password in the response matches the correct password
        assertEquals("pass1", body.getPassword());
    }

    // Test that login returns 401 Unauthorized when password is wrong
    @Test
    void login_withWrongPassword_returns401() throws Exception {
        // Build a unique username for this test
        var username = "loginBad_" + System.currentTimeMillis();
        // Create the user directly in Firebase with a known password
        createUserInFirebase(username, "realPass");

        // Build a login request body with the correct username but wrong password
        var loginRequestUser = new User();
        // Set the same username
        loginRequestUser.setUserName(username);
        // Set an incorrect password
        loginRequestUser.setPassword("wrongPass");

        // Perform the POST request to /api/users/login sending User as JSON and expecting String body
        var response = this.restTemplate.postForEntity("/api/users/login", loginRequestUser, String.class);

        // Assert that HTTP status is 401 Unauthorized
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        // Assert that the response body contains the error message
        assertEquals("Invalid username or password", response.getBody());
    }

    // --------------------------- GET ALL USERS TEST ---------------------------

    // Test that getAllUsers returns an array and contains at least one user
    @Test
    void getAllUsers_returnsArrayAndContainsAtLeastOneUser() throws Exception {
        // Create one user to ensure at least one user exists in the system
        var username = "allUsers_" + System.currentTimeMillis();
        // Create the user directly in Firebase
        createUserInFirebase(username, "p");

        // Perform a GET request to /api/users expecting an array of User
        var response = this.restTemplate.getForEntity("/api/users", User[].class);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extract the array of users from response
        var users = response.getBody();
        // Assert that the array is not null
        assertNotNull(users);
        // Assert that the array contains at least one element
        assertTrue(users.length >= 1);
    }

    // --------------------------- GET USER TESTS ---------------------------

    // Test that getUser returns 200 and a User JSON when user exists
    @Test
    void getUser_existingUser_returnsUserJson() throws Exception {
        // Build a unique username for this test
        var username = "getUserOk_" + System.currentTimeMillis();
        // Create this user in Firebase
        createUserInFirebase(username, "p");

        // Perform a GET request to /api/users/{username} expecting a User body
        var response = this.restTemplate.getForEntity("/api/users/" + username, User.class);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extract the User object from the response body
        var body = response.getBody();
        // Assert that body is not null
        assertNotNull(body);
        // Assert that the username field matches the expected username
        assertEquals(username, body.getUserName());
    }

    // Test that getUser returns 404 and error message when user does not exist
    @Test
    void getUser_nonExistingUser_returns404() {
        // Build a username that definitely does not exist
        String username = "noSuchUser_" + System.currentTimeMillis();

        // Perform a GET request to /api/users/{username} expecting String body
        ResponseEntity<String> response =
                this.restTemplate.getForEntity("/api/users/" + username, String.class);

        // Assert that HTTP status is 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // Assert that body is "User not found"
        assertEquals("User not found", response.getBody());
    }

    // --------------------------- UPDATE (PUT) USER TESTS ---------------------------

    // Test that updateUser replaces entire record and returns updated user for existing user
    @Test
    void updateUser_existingUser_replacesRecord() throws Exception {
        // Build a unique username for this test
        var username = "updateUserOk_" + System.currentTimeMillis();
        // Create the initial user in Firebase
        createUserInFirebase(username, "origPass");

        // Build a new User object with updated fields
        var updated = buildUser(username, "newPass");
        // Change the full name to indicate update
        updated.setFullName("Updated Name");
        // Change the age to a different value
        updated.setAge(30);

        // Wrap the updated user into HttpEntity so it can be sent as request body
        var entity = new HttpEntity<>(updated);

        // Perform a PUT request to /api/users/{username} expecting a User response
        var response = this.restTemplate.exchange("/api/users/{username}",
                        HttpMethod.PUT, entity, User.class, username);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extract the updated User from the response body
        var body = response.getBody();
        // Assert that body is not null
        assertNotNull(body);
        // Assert that password field is the new password
        assertEquals("newPass", body.getPassword());
        // Assert that fullName field is "Updated Name"
        assertEquals("Updated Name", body.getFullName());
        // Assert that age field is 30
        assertEquals(30, body.getAge());
    }

    // Test that updateUser returns 404 when user does not exist
    @Test
    void updateUser_nonExistingUser_returns404() {
        // Build a username that does not exist
        var username = "updateNoSuch_" + System.currentTimeMillis();

        // Build an updated user body for this non-existing username
        var updated = buildUser(username, "p");
        // Wrap the updated user into HttpEntity for the request body
        var entity = new HttpEntity<>(updated);

        // Perform a PUT request to /api/users/{username} expecting String body
        var response = this.restTemplate.exchange(
                        // url template
                        "/api/users/{username}",
                        // http method
                        HttpMethod.PUT,
                        // the request
                        entity,
                        // cast the body of the response to string
                        String.class,
                        // add the parameter username to the url
                        // "/api/users/{username}"  +  username
                        // = "/api/users/patchNoSuch_12345"
                        username);

        // Assert that HTTP status is 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // Assert that body is "User not found"
        assertEquals("User not found", response.getBody());
    }

    // --------------------------- PATCH USER TESTS ---------------------------

    // Test that patchUser updates a single field (fullName) for an existing user
    @Test
    void patchUser_existingUser_updatesField() throws Exception {
        // Build a unique username for this test
        var username = "patchUserOk_" + System.currentTimeMillis();
        // Create initial user in Firebase
        createUserInFirebase(username, "p");

        // Build a partial update map as a Java Map
        Map<String, Object> updates = new HashMap<>();
        // Put new fullName into the updates map
        updates.put("fullName", "Patched Name");
        // Wrap the updates map into HttpEntity so it will be serialized as JSON
        var entity = new HttpEntity<>(updates);

        // Perform a PATCH request to /api/users/{username} expecting User response
        var response = this.restTemplate.exchange("/api/users/{username}",
                        HttpMethod.PATCH, entity, User.class, username);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extract updated User from response body
        var body = response.getBody();
        // Assert that body is not null
        assertNotNull(body);
        // Assert that the returned JSON contains the updated fullName
        assertEquals("Patched Name", body.getFullName());
        // Assert that the returned JSON still has the same userName
        assertEquals(username, body.getUserName());
    }

    // Test that patchUser returns 404 Not Found when user does not exist
    @Test
    void patchUser_nonExistingUser_returns404() {
        // Build a username that does not exist
        var username = "patchNoSuch_" + System.currentTimeMillis();

        // Build an updates map for this non-existing user
        Map<String, Object> updates = new HashMap<>();
        // Put some dummy field into updates
        updates.put("fullName", "Someone");
        // Wrap the updates map into HttpEntity
        var entity = new HttpEntity<>(updates);

        // Perform a PATCH request to /api/users/{username} expecting String body
        var response = this.restTemplate.exchange("/api/users/{username}",
                        HttpMethod.PATCH, entity, String.class, username);

        // Assert that HTTP status is 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // Assert that body is "User not found"
        assertEquals("User not found", response.getBody());
    }

    // --------------------------- DELETE USER TESTS ---------------------------

    // Test that deleteUser deletes an existing user and returns 200 with message
    @Test
    void deleteUser_existingUser_returnsOk() throws Exception {
        // Build a unique username for this test
        var username = "deleteUserOk_" + System.currentTimeMillis();
        // Create the user in Firebase
        createUserInFirebase(username, "p");

        // Perform a DELETE request to /api/users/{username} expecting String body
        var response = this.restTemplate.exchange("/api/users/{username}",
                        HttpMethod.DELETE, null, String.class, username);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Assert that body is "User deleted"
        assertEquals("User deleted", response.getBody());
    }

    // Test that deleteUser returns 404 when user does not exist
    @Test
    void deleteUser_nonExistingUser_returns404() {
        // Build a username that does not exist
        var username = "deleteNoSuch_" + System.currentTimeMillis();

        // Perform a DELETE request to /api/users/{username} expecting String body
        var response = this.restTemplate.exchange("/api/users/{username}",
                        HttpMethod.DELETE, null, String.class, username);

        // Assert that HTTP status is 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // Assert that body is "User not found"
        assertEquals("User not found", response.getBody());
    }

    // --------------------------- HEAD USER TESTS ---------------------------

    // Test that headUser returns 200 OK when user exists (no body)
    @Test
    void headUser_existingUser_returns200() throws Exception {
        // Build a unique username and create user in Firebase
        var username = "headUserOk_" + System.currentTimeMillis();
        // Create this user in Firebase
        createUserInFirebase(username, "p");

        // Perform a HEAD request to /api/users/{username} expecting no body
        var response = this.restTemplate.exchange("/api/users/{username}",
                        HttpMethod.HEAD, null, Void.class, username);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Assert that the body is null (no content)
        assertNull(response.getBody());
    }

    // Test that headUser returns 404 Not Found when user does not exist
    @Test
    void headUser_nonExistingUser_returns404() {
        // Build a username that does not exist
        var username = "headNoSuch_" + System.currentTimeMillis();

        // Perform a HEAD request to /api/users/{username}
        var response = this.restTemplate.exchange("/api/users/{username}",
                        HttpMethod.HEAD, null, Void.class, username);

        // Assert that HTTP status is 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // --------------------------- UPDATE BMI TESTS ---------------------------

    // Test that updateBmi returns 200 OK with success message for existing user
    @Test
    void updateBmi_existingUser_returns200() throws Exception {
        // Build a unique username for this test
        var username = "bmiOk_" + System.currentTimeMillis();
        // Create the user in Firebase
        createUserInFirebase(username, "p");

        // Build URL with query parameter for bmi
        var url = "/api/users/" + username + "/bmi?bmi=23.5";

        // Perform a PATCH request to /api/users/{username}/bmi expecting String body
        var response = this.restTemplate.exchange(url, HttpMethod.PATCH, null, String.class);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Assert that body contains confirmation message
        assertEquals("BMI updated successfully", response.getBody());
    }

    // Test that updateBmi returns 404 Not Found when user does not exist
    @Test
    void updateBmi_nonExistingUser_returns404() {
        // Build a username that does not exist
        var username = "bmiNoSuch_" + System.currentTimeMillis();
        // Build URL with query parameter for bmi
        var url = "/api/users/" + username + "/bmi?bmi=23.5";

        // Perform a PATCH request to /api/users/{username}/bmi expecting String body
        var response = this.restTemplate.exchange(url, HttpMethod.PATCH, null, String.class);

        // Assert that HTTP status is 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // Assert that body contains "User not found"
        assertEquals("User not found", response.getBody());
    }

    // --------------------------- WATER MODULE TESTS ---------------------------

    // Test that updateWater and getWater work together via controller endpoints
    @Test
    void updateWater_and_getWater_flowForExistingUser() throws Exception {
        // Build a unique username for this test
        var username = "waterOk_" + System.currentTimeMillis();
        // Create the user in Firebase
        createUserInFirebase(username, "p");

        // Perform a GET request to read initial water values
        var beforeResponse = this.restTemplate.getForEntity("/api/users/" + username + "/water", Map.class);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, beforeResponse.getStatusCode());
        // Extract the response body as a Map
        @SuppressWarnings("unchecked") Map<String, Object> beforeBody = beforeResponse.getBody();
        // Assert that the map is not null
        assertNotNull(beforeBody);
        // Extract today's water as a Number and convert to long
        var todayBeforeNumber = (Number) beforeBody.getOrDefault("todayWater", 0);
        var todayBefore = todayBeforeNumber.longValue();

        // Define an amount of water to add
        var addedAmount = 400;

        // Build URL for PATCH request with amount parameter
        var patchUrl = "/api/users/" + username + "/water?amount=" + addedAmount;

        // Perform PATCH request to update water
        var patchResponse = this.restTemplate.exchange(patchUrl, HttpMethod.PATCH, null, String.class);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, patchResponse.getStatusCode());
        // Assert that body is "Water updated successfully"
        assertEquals("Water updated successfully", patchResponse.getBody());

        // Perform another GET request to read updated water totals
        var afterResponse = this.restTemplate.getForEntity("/api/users/" + username + "/water", Map.class);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, afterResponse.getStatusCode());
        // Extract body as Map
        @SuppressWarnings("unchecked") Map<String, Object> afterBody = afterResponse.getBody();
        // Assert that body is not null
        assertNotNull(afterBody);
        // Extract today's water after update
        var todayAfterNumber = (Number) afterBody.getOrDefault("todayWater", 0);
        var todayAfter = todayAfterNumber.longValue();

        // Assert that today's water increased exactly by the added amount
        assertEquals(todayBefore + addedAmount, todayAfter);
    }

    // Test that getWater returns 404 for a non-existing user
    @Test
    void getWater_nonExistingUser_returns404() {
        // Build a username that does not exist
        var username = "waterNoSuch_" + System.currentTimeMillis();

        // Perform a GET request to /api/users/{username}/water expecting String body
        var response = this.restTemplate.getForEntity("/api/users/" + username + "/water", String.class);

        // Assert that HTTP status is 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // Assert that body is "User not found"
        assertEquals("User not found", response.getBody());
    }

    // --------------------------- WATER HISTORY MAP TESTS ---------------------------

    // Test that getWaterHistoryMap returns a JSON map with exactly "days" entries for existing user
    @Test
    void getWaterHistoryMap_existingUser_returnsMapWithRequestedDays() throws Exception {
        // Build a unique username for this test
        var username = "waterHistoryOk_" + System.currentTimeMillis();
        // Create the user in Firebase
        createUserInFirebase(username, "p");
        // Define how many days we want
        var days = 5;
        // Build URL for GET request with days query parameter
        var url = "/api/users/" + username + "/waterHistoryMap?days=" + days;

        // Perform GET request expecting a Map body
        var response = this.restTemplate.getForEntity(url, Map.class);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extract response body as Map
        @SuppressWarnings("unchecked") Map<String, Object> body = response.getBody();
        // Assert that body is not null
        assertNotNull(body);
        // Assert that the map contains exactly "days" entries
        assertEquals(days, body.size());
    }

    // Test that getWaterHistoryMap returns 404 when user does not exist
    @Test
    void getWaterHistoryMap_nonExistingUser_returns404() {
        // Build a username that does not exist
        var username = "waterHistoryNoSuch_" + System.currentTimeMillis();
        // Build URL with days parameter
        var url = "/api/users/" + username + "/waterHistoryMap?days=3";

        // Perform GET request expecting String body
        var response = this.restTemplate.getForEntity(url, String.class);

        // Assert that HTTP status is 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // Assert that body is "User not found"
        assertEquals("User not found", response.getBody());
    }

    // --------------------------- WEEKLY AVERAGES TESTS ---------------------------

    // Test that getWeeklyAverages returns 200 OK and a map for an existing user
    @Test
    void getWeeklyAverages_existingUser_returnsMap() throws Exception {
        // Build a unique username for this test
        var username = "weeklyAvgOk_" + System.currentTimeMillis();
        // Create the user in Firebase
        createUserInFirebase(username, "p");
        // add some water so at least one week has non-zero average
        var waterUrl = "/api/users/" + username + "/water?amount=300";

        // Perform PATCH request to add water
        var waterResponse = this.restTemplate.exchange(waterUrl, HttpMethod.PATCH, null, String.class);

        // Assert that water update succeeded with status 200 OK
        assertEquals(HttpStatus.OK, waterResponse.getStatusCode());

        // Build URL for GET request to weekly averages endpoint
        var url = "/api/users/" + username + "/weeklyAverages";

        // Perform GET request expecting Map<String,Integer> body (as raw Map)
        var response = this.restTemplate.getForEntity(url, Map.class);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extract body as Map
        @SuppressWarnings("unchecked") Map<String, Object> body = response.getBody();
        // Assert that body is not null
        assertNotNull(body);
        // Assert that there are exactly 4 entries (Week 1..Week 4)
        assertEquals(4, body.size());
    }

    // Test that getWeeklyAverages returns 404 and empty map for non-existing user
    @Test
    void getWeeklyAverages_nonExistingUser_returns404() {
        // Build a username that does not exist
        var username = "weeklyAvgNoSuch_" + System.currentTimeMillis();
        // Build URL for GET request
        var url = "/api/users/" + username + "/weeklyAverages";

        // Perform GET request expecting String body
        var response = this.restTemplate.getForEntity(url, String.class);

        // Assert that HTTP status is 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // Assert that response body is "{}" (empty JSON object)
        assertEquals("{}", response.getBody());
    }

    // --------------------------- GOAL MODULE TESTS ---------------------------

    // Test that setGoal updates the goal and getGoal reads the same value via controller
    @Test
    void goal_setAndGet_flowForExistingUser() throws Exception {
        // Build a unique username for this test
        var username = "goalOk_" + System.currentTimeMillis();
        // Create the user in Firebase
        createUserInFirebase(username, "p");
        // Define a valid goalMl value
        var newGoal = 3400;
        // Build URL for PUT request with goalMl parameter
        var setUrl = "/api/users/" + username + "/goal?goalMl=" + newGoal;

        // Perform PUT request expecting Map body
        var setResponse = this.restTemplate.exchange(setUrl, HttpMethod.PUT, null, Map.class);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, setResponse.getStatusCode());
        // Extract body as Map
        @SuppressWarnings("unchecked") Map<String, Object> setBody = setResponse.getBody();
        // Assert that body is not null
        assertNotNull(setBody);
        // Assert that status field is "OK"
        assertEquals("OK", setBody.get("status"));

        // Build URL for GET request to read the goal
        var getUrl = "/api/users/" + username + "/goal";
        // Perform GET request expecting Map body
        var getResponse = this.restTemplate.getForEntity(getUrl, Map.class);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        // Extract body as Map
        @SuppressWarnings("unchecked") Map<String, Object> getBody = getResponse.getBody();
        // Assert that body is not null
        assertNotNull(getBody);
        // Extract goalMl as Number and compare with newGoal
        var goalNumber = (Number) getBody.get("goalMl");
        // Assert that goalMl equals the newGoal value
        assertEquals(newGoal, goalNumber.intValue());
    }

    // Test that setGoal with invalid value returns 400 Bad Request and proper status
    @Test
    void setGoal_invalidValue_returnsBadRequest() throws Exception {
        // Build a unique username for this test
        var username = "goalInvalid_" + System.currentTimeMillis();
        // Create the user in Firebase
        createUserInFirebase(username, "p");

        // Build URL for PUT request with invalid goalMl value (too low)
        var url = "/api/users/" + username + "/goal?goalMl=100";
        // Perform PUT request expecting Map body
        var response = this.restTemplate.exchange(url, HttpMethod.PUT, null, Map.class);

        // Assert that HTTP status is 400 Bad Request
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // Extract body as Map
        @SuppressWarnings("unchecked") Map<String, Object> body = response.getBody();
        // Assert that body is not null
        assertNotNull(body);
        // Assert that status field equals "INVALID_OR_NOT_FOUND"
        assertEquals("INVALID_OR_NOT_FOUND", body.get("status"));
    }

    // --------------------------- BMI DISTRIBUTION TEST ---------------------------

    // Test that getBmiDistribution returns 200 OK and a JSON map
    @Test
    void getBmiDistribution_returnsOkWithMap() {
        // Build URL for GET request to BMI distribution endpoint
        var url = "/api/users/stats/bmiDistribution";

        // Perform GET request expecting Map body
        var response = this.restTemplate.getForEntity(url, Map.class);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extract body as Map
        @SuppressWarnings("unchecked") Map<String, Object> body = response.getBody();
        // Assert that body is not null (can be empty map, but not null)
        assertNotNull(body);
    }

    // --------------------------- CALORIES MODULE TESTS ---------------------------

    // Test that updateCalories and getCalories behave correctly for valid and invalid values
    @Test
    void calories_updateAndGet_flowWithValidAndInvalidValues() throws Exception {
        // Build a unique username for this test
        var username = "caloriesOk_" + System.currentTimeMillis();
        // Create the user in Firebase
        createUserInFirebase(username, "p");

        // Build URL for initial GET request to calories endpoint
        var getInitialUrl = "/api/users/" + username + "/calories";
        // Perform initial GET request expecting Map body
        var initialResponse = this.restTemplate.getForEntity(getInitialUrl, Map.class);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, initialResponse.getStatusCode());
        // Extract initial body as Map
        @SuppressWarnings("unchecked") Map<String, Object> initialBody = initialResponse.getBody();
        // Assert that body is not null
        assertNotNull(initialBody);
        // Extract "calories" field as Number
        var initialCalories = (Number) initialBody.getOrDefault("calories", 0);
        // Assert that initial calories are 0
        assertEquals(0, initialCalories.intValue());

        // Build URL for valid PUT request to update calories to 1500
        var putValidUrl = "/api/users/" + username + "/calories?calories=1500";

        // Perform PUT request with valid value expecting no content
        var validResponse = this.restTemplate.exchange(putValidUrl, HttpMethod.PUT, null, Void.class);

        // Assert that HTTP status is 204 No Content
        assertEquals(HttpStatus.NO_CONTENT, validResponse.getStatusCode());

        // Perform GET request again to verify updated calories
        var afterValidResponse = this.restTemplate.getForEntity(getInitialUrl, Map.class);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, afterValidResponse.getStatusCode());
        // Extract body as Map
        @SuppressWarnings("unchecked") Map<String, Object> afterValidBody = afterValidResponse.getBody();
        // Assert that body is not null
        assertNotNull(afterValidBody);
        // Extract "calories" after valid update
        var afterValidCalories = (Number) afterValidBody.getOrDefault("calories", 0);
        // Assert that calories are now 1500
        assertEquals(1500, afterValidCalories.intValue());

        // Build URL for PUT request with invalid negative calories
        var putInvalidLowUrl = "/api/users/" + username + "/calories?calories=-10";

        // Perform PUT request expecting bad request
        var invalidLowResponse = this.restTemplate.exchange(putInvalidLowUrl, HttpMethod.PUT, null, Void.class);

        // Assert that HTTP status is 400 Bad Request
        assertEquals(HttpStatus.BAD_REQUEST, invalidLowResponse.getStatusCode());

        // Build URL for PUT request with excessively high invalid calories
        var putInvalidHighUrl = "/api/users/" + username + "/calories?calories=50000";

        // Perform PUT request expecting bad request again
        var invalidHighResponse = this.restTemplate.exchange(putInvalidHighUrl, HttpMethod.PUT, null, Void.class);

        // Assert that HTTP status is 400 Bad Request
        assertEquals(HttpStatus.BAD_REQUEST, invalidHighResponse.getStatusCode());

        // Perform GET request again to verify calories did not change after invalid updates
        var afterInvalidResponse = this.restTemplate.getForEntity(getInitialUrl, Map.class);

        // Assert that HTTP status is 200 OK
        assertEquals(HttpStatus.OK, afterInvalidResponse.getStatusCode());
        // Extract body as Map
        @SuppressWarnings("unchecked") Map<String, Object> afterInvalidBody = afterInvalidResponse.getBody();
        // Assert that body is not null
        assertNotNull(afterInvalidBody);
        // Extract "calories" after invalid updates
        var afterInvalidCalories = (Number) afterInvalidBody.getOrDefault("calories", 0);
        // Assert that calories are still 1500 (unchanged)
        assertEquals(1500, afterInvalidCalories.intValue());
    }
}
