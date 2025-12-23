// Define the package for this test class
package org.example.service;
// Import assertion methods from JUnit Jupiter
import org.junit.jupiter.api.AfterAll;
// Import annotation to define methods that run before all tests
import org.junit.jupiter.api.BeforeAll;
// Import annotation for standard test methods
import org.junit.jupiter.api.Test;
// Import annotation to control test instance lifecycle (per class instead of per method)
import org.junit.jupiter.api.TestInstance;
// Import the TestInstance lifecycle enum
import org.junit.jupiter.api.TestInstance.Lifecycle;
// Import the ParameterizedTest annotation for parameterized test methods
import org.junit.jupiter.params.ParameterizedTest;
// Import ValueSource to supply simple parameter values for parameterized tests
import org.junit.jupiter.params.provider.ValueSource;
// Import the SpringBootTest annotation to load the full Spring context
import org.springframework.boot.test.context.SpringBootTest;
// Import Autowired to inject Spring beans into the test class
import org.springframework.beans.factory.annotation.Autowired;
// Import static assertion methods for cleaner code
import static org.junit.jupiter.api.Assertions.*;
// Import the User model used by FirebaseService
import org.example.model.User;
// Import JSONObject used by getWater method
import org.json.JSONObject;
// Import standard Java concurrency utilities
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
// Import date and time utilities for building expected date keys
import java.text.SimpleDateFormat;
import java.util.*;

// FirebaseServiceIntegrationTest is an end-to-end integration test class that
// verifies the behavior of FirebaseService against a real Firebase Realtime
// Database. Instead of calling the REST controllers, these tests interact
// directly with the service layer to ensure that all low-level operations
// (such as creating users, updating BMI, managing water and calories, and
// reading/writing structured data) work correctly with Firebase. By running
// these tests we can detect issues related to data structure, paths,
// serialization, asynchronous operations, and error handling before the
// HTTP layer is even involved. In other words, this class validates that
// FirebaseService is reliable and consistent as the core data access layer
// of the application.

// Annotate this class as a Spring Boot integration test (loads the full application context)
@SpringBootTest
// Use a single test instance for the whole class so @BeforeAll and @AfterAll can be non-static
@TestInstance(Lifecycle.PER_CLASS)
public class FirebaseServiceIntegrationTest {

    // Inject the real FirebaseService bean from the Spring context
    @Autowired
    private org.example.service.FirebaseService firebaseService;

    // Define a constant username for the main integration test user
    private final String TEST_USERNAME_1 = "integrationUser1";

    // Define a second test username that will be used for comparison scenarios
    private final String TEST_USERNAME_2 = "integrationUser2";

    // Store the main test user object for convenience
    private User testUser1;

    // Store the second test user object for convenience
    private User testUser2;

    // --------------------------- TEST LIFECYCLE ---------------------------

    // This method will run once before all tests in this class
    @BeforeAll
    void setUpTestUsers() throws Exception {
        // Create a new User instance for the first test user
        testUser1 = new User();
        // Set the username for the first test user
        testUser1.setUserName(TEST_USERNAME_1);
        // Set a password for the first test user
        testUser1.setPassword("pass1");

        // Create a new User instance for the second test user
        testUser2 = new User();
        // Set the username for the second test user
        testUser2.setUserName(TEST_USERNAME_2);
        // Set a password for the second test user
        testUser2.setPassword("pass2");

        // Try to create the first test user in Firebase (if already exists, result may be false)
        CompletableFuture<Boolean> create1 = firebaseService.createUser(testUser1);
        // Wait for the asynchronous creation result with a timeout of 20 seconds
        Boolean created1 = create1.get(20, TimeUnit.SECONDS);
        // Print debug information about the creation result for user 1
        System.out.println("DEBUG setUpTestUsers -> create user1 result = " + created1);

        // Try to create the second test user in Firebase
        CompletableFuture<Boolean> create2 = firebaseService.createUser(testUser2);
        // Wait for the asynchronous creation result with a timeout of 20 seconds
        Boolean created2 = create2.get(20, TimeUnit.SECONDS);
        // Print debug information about the creation result for user 2
        System.out.println("DEBUG setUpTestUsers -> create user2 result = " + created2);
    }

    // This method will run once after all tests in this class
    @AfterAll
    void cleanUpTestUsers() throws Exception {
        // Call deleteUser on the first test username
        CompletableFuture<Boolean> delete1 = firebaseService.deleteUser(TEST_USERNAME_1);
        // Wait for the asynchronous delete result with a timeout of 20 seconds
        Boolean deleted1 = delete1.get(20, TimeUnit.SECONDS);
        // Print debug information about the deletion result for user 1
        System.out.println("DEBUG cleanUpTestUsers -> delete user1 result = " + deleted1);

        // Call deleteUser on the second test username
        CompletableFuture<Boolean> delete2 = firebaseService.deleteUser(TEST_USERNAME_2);
        // Wait for the asynchronous delete result with a timeout of 20 seconds
        Boolean deleted2 = delete2.get(20, TimeUnit.SECONDS);
        // Print debug information about the deletion result for user 2
        System.out.println("DEBUG cleanUpTestUsers -> delete user2 result = " + deleted2);
    }

    // --------------------------- SIGNUP / CREATE / DELETE ---------------------------

    // Test that signup can create a brand new user and prevents duplicate usernames
    @Test
    void signup_createsNewUserAndRejectsDuplicate() throws Exception {
        // Build a unique username for this test run using the current timestamp
        String uniqueUsername = "signupUser_" + System.currentTimeMillis();

        // Create a new User object for the signup flow
        User signupUser = new User();
        // Set the username for the signup user
        signupUser.setUserName(uniqueUsername);
        // Set a password for the signup user
        signupUser.setPassword("signupPass");
        // Set a fullname for the signup user
        signupUser.setFullName("Sasa li");
        // Set a age for the signup user
        signupUser.setAge(25);

        // Call the signup method asynchronously
        CompletableFuture<String> resultFuture = firebaseService.signup(signupUser);
        // Wait for the result of the signup call with a timeout of 20 seconds
        String firstResult = resultFuture.get(20, TimeUnit.SECONDS);
        // Assert that the first signup attempt completed with a success message
        assertEquals("User created successfully", firstResult);

        // Call signup again with the same username to check duplicate handling
        CompletableFuture<String> duplicateFuture = firebaseService.signup(signupUser);
        // Wait for the duplicate signup result with a timeout of 20 seconds
        String secondResult = duplicateFuture.get(20, TimeUnit.SECONDS);
        // Assert that the second signup attempt reports a duplicate username
        assertEquals("Username already exists", secondResult);
    }

    // Test that createUser + exists + deleteUser work consistently together
    @Test
    void createUser_existsAndDeleteUser_flowWorks() throws Exception {
        // Build a temporary username for this specific test
        String tempUsername = "tempUser_" + System.currentTimeMillis();

        // Create a new User instance for the temporary user
        User tempUser = new User();
        // Set the username of the temporary user
        tempUser.setUserName(tempUsername);
        // Set a password for the temporary user
        tempUser.setPassword("tempPass");
        // Set a fullname for the signup user
        tempUser.setFullName("Sasa li");
        // Set a age for the signup user
        tempUser.setAge(25);

        // Call createUser for the temporary user
        CompletableFuture<Boolean> createFuture = firebaseService.createUser(tempUser);
        // Wait for the create result with a timeout of 20 seconds
        Boolean created = createFuture.get(20, TimeUnit.SECONDS);
        // Assert that the user was created successfully
        assertTrue(created);

        // Call exists to check that the user now exists in Firebase
        CompletableFuture<Boolean> existsFuture = firebaseService.exists(tempUsername);
        // Wait for the exists result with a timeout of 20 seconds
        Boolean exists = existsFuture.get(20, TimeUnit.SECONDS);
        // Assert that the user indeed exists
        assertTrue(exists);

        // Call deleteUser to remove the temporary user
        CompletableFuture<Boolean> deleteFuture = firebaseService.deleteUser(tempUsername);
        // Wait for the delete result with a timeout of 20 seconds
        Boolean deleted = deleteFuture.get(20, TimeUnit.SECONDS);
        // Assert that the user was deleted successfully
        assertTrue(deleted);

        // Call exists again to verify that the user no longer exists
        CompletableFuture<Boolean> existsAfterDeleteFuture = firebaseService.exists(tempUsername);
        // Wait for the result with a timeout of 20 seconds
        Boolean existsAfterDelete = existsAfterDeleteFuture.get(20, TimeUnit.SECONDS);
        // Assert that the user does not exist anymore
        assertFalse(existsAfterDelete);
    }

    // --------------------------- GET USER NEGATIVE TEST ---------------------------
    // Test that getUser returns null for a username that does not exist in Firebase
    @Test
    void getUser_nonExisting_returnsNull() throws Exception {
        // Build a username that should not exist in Firebase
        String missingUsername = "getUserNoSuch_" + System.currentTimeMillis();

        // Call getUser for this missing username
        CompletableFuture<User> future =
                firebaseService.getUser(missingUsername);
        // Wait for the getUser result with a timeout of 20 seconds
        User result = future.get(20, TimeUnit.SECONDS);
        // Assert that no user object was found
        assertNull(result);
    }

    // --------------------------- UPDATE USER FULL RECORD TEST ---------------------------
    // Test that updateUser replaces the entire user record for an existing user
    @Test
    void updateUser_existing_replacesEntireRecord() throws Exception {
        // Build a unique username for this test run
        String tempUsername = "updateUserDeep_" + System.currentTimeMillis();

        // Create a new User instance representing the original state
        User originalUser = new User();
        // Set username for the original user
        originalUser.setUserName(tempUsername);
        // Set password for the original user
        originalUser.setPassword("origPass");
        // Set full name for the original user
        originalUser.setFullName("Original Name");
        // Set age for the original user
        originalUser.setAge(20);

        // Create the original user in Firebase using createUser
        CompletableFuture<Boolean> createFuture =
                firebaseService.createUser(originalUser);
        // Wait for the creation result with a timeout of 20 seconds
        Boolean created = createFuture.get(20, TimeUnit.SECONDS);
        // Assert that the user was created successfully
        assertTrue(created);

        // Create a new User instance representing the updated state
        User updatedUser = new User();
        // Keep the same username for the updated user
        updatedUser.setUserName(tempUsername);
        // Set a new password for the updated user
        updatedUser.setPassword("newPass");
        // Set a new full name for the updated user
        updatedUser.setFullName("Updated Name");
        // Set a new age for the updated user
        updatedUser.setAge(30);

        // Call updateUser to replace the existing record with the updated user
        CompletableFuture<Boolean> updateFuture =
                firebaseService.updateUser(tempUsername, updatedUser);
        // Wait for the update result with a timeout of 20 seconds
        Boolean updated = updateFuture.get(20, TimeUnit.SECONDS);
        // Assert that the update operation succeeded
        assertTrue(updated);

        // Call getUser to read back the user after the update
        CompletableFuture<User> getFuture =
                firebaseService.getUser(tempUsername);
        // Wait for the getUser result with a timeout of 20 seconds
        User fromDb = getFuture.get(20, TimeUnit.SECONDS);
        // Assert that the returned user object is not null
        assertNotNull(fromDb);
        // Assert that the password field was updated
        assertEquals("newPass", fromDb.getPassword());
        // Assert that the full name field was updated
        assertEquals("Updated Name", fromDb.getFullName());
        // Assert that the age field was updated
        assertEquals(30, fromDb.getAge());
        // Assert that the old password value is no longer present
        assertNotEquals("origPass", fromDb.getPassword());
        // Assert that the old full name value is no longer present
        assertNotEquals("Original Name", fromDb.getFullName());
        // Assert that the old age value is no longer present
        assertNotEquals(20, fromDb.getAge());

        // Call deleteUser to clean up the temporary user
        CompletableFuture<Boolean> deleteFuture =
                firebaseService.deleteUser(tempUsername);
        // Wait for the delete result with a timeout of 20 seconds
        Boolean deleted = deleteFuture.get(20, TimeUnit.SECONDS);
        // Assert that the user was deleted successfully
        assertTrue(deleted);
    }

    // --------------------------- UPDATE USER NON-EXISTING TEST ---------------------------
    // Test that updateUser returns false when trying to update a non-existing user
    @Test
    void updateUser_nonExisting_returnsFalse() throws Exception {
        // Build a username that should not exist in Firebase
        String missingUsername = "updateUserNoSuch_" + System.currentTimeMillis();

        // Create a User instance with this missing username
        User candidate = new User();
        // Set the username for the candidate user
        candidate.setUserName(missingUsername);
        // Set a password for the candidate user
        candidate.setPassword("somePass");
        // Set a full name for the candidate user
        candidate.setFullName("Some Name");
        // Set an age for the candidate user
        candidate.setAge(40);

        // Call updateUser for this missing username
        CompletableFuture<Boolean> updateFuture =
                firebaseService.updateUser(missingUsername, candidate);
        // Wait for the update result with a timeout of 20 seconds
        Boolean updated = updateFuture.get(20, TimeUnit.SECONDS);
        // Assert that the service returned false (user not found)
        assertFalse(updated);

        // Optionally verify that getUser still returns null for this username
        CompletableFuture<User> getFuture =
                firebaseService.getUser(missingUsername);
        // Wait for the getUser result with a timeout of 20 seconds
        User fromDb = getFuture.get(20, TimeUnit.SECONDS);
        // Assert that no user object exists in Firebase for this username
        assertNull(fromDb);
    }

    // --------------------------- LOGIN TESTS ---------------------------

    // Test that login returns a valid User object for correct credentials
    @Test
    void login_withCorrectCredentials_returnsUser() throws Exception {
        // Call login using the known username and password of testUser1
        CompletableFuture<User> loginFuture = firebaseService.login(TEST_USERNAME_1, "pass1");
        // Wait for the login result with a timeout of 20 seconds
        User loggedUser = loginFuture.get(20, TimeUnit.SECONDS);
        // Assert that a non-null user object was returned
        assertNotNull(loggedUser);
        // Assert that the returned user has the expected username
        assertEquals(TEST_USERNAME_1, loggedUser.getUserName());
        // Assert that the returned user has the expected password
        assertEquals("pass1", loggedUser.getPassword());
    }

    // Test that login returns null when incorrect credentials are used
    @Test
    void login_withWrongPassword_returnsNull() throws Exception {
        // Call login with the correct username but wrong password
        CompletableFuture<User> loginFuture = firebaseService.login(TEST_USERNAME_1, "wrongPass");
        // Wait for the login result with a timeout of 20 seconds
        User loggedUser = loginFuture.get(20, TimeUnit.SECONDS);
        // Assert that no user was found for the wrong credentials
        assertNull(loggedUser);
    }



    // --------------------------- WATER MODULE TESTS ---------------------------

    // Test that updateWater increases today's total and getWater reflects the change
    @Test
    void updateWater_increasesTodayTotal_and_getWaterIsConsistent() throws Exception {
        // Call getWater to read today's and yesterday's values before the update
        CompletableFuture<JSONObject> beforeFuture = firebaseService.getWater(TEST_USERNAME_1);
        // Wait for the JSON result with a timeout of 20 seconds
        JSONObject beforeJson = beforeFuture.get(20, TimeUnit.SECONDS);
        // Assert that the JSON object is not null
        assertNotNull(beforeJson);
        // Extract today's water amount from the JSON object
        long todayBefore = beforeJson.getLong("todayWater");

        // Define the amount of water to add in this test
        int addedAmount = 500;

        // Call updateWater to add the new amount for the given user
        CompletableFuture<Boolean> updateFuture = firebaseService.updateWater(TEST_USERNAME_1, addedAmount);
        // Wait for the update result with a timeout of 20 seconds
        Boolean updated = updateFuture.get(20, TimeUnit.SECONDS);
        // Assert that the update operation succeeded
        assertTrue(updated);

        // Call getWater again to read the updated values
        CompletableFuture<JSONObject> afterFuture = firebaseService.getWater(TEST_USERNAME_1);
        // Wait for the updated JSON result with a timeout of 20 seconds
        JSONObject afterJson = afterFuture.get(20, TimeUnit.SECONDS);
        // Assert that the JSON object is not null
        assertNotNull(afterJson);
        // Extract today's water amount after the update
        long todayAfter = afterJson.getLong("todayWater");

        // Assert that today's water increased exactly by the added amount
        assertEquals(todayBefore + addedAmount, todayAfter);

        // Build today's date key in the same format used by FirebaseService
        String todayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Call getWaterHistoryMap for the last 3 days for this user
        CompletableFuture<Map<String, Long>> historyFuture =
                firebaseService.getWaterHistoryMap(TEST_USERNAME_1, 3);
        // Wait for the history map result with a timeout of 20 seconds
        Map<String, Long> history = historyFuture.get(20, TimeUnit.SECONDS);
        // Assert that the history map is not null
        assertNotNull(history);
        // Assert that the history map contains exactly 3 entries (for 3 days)
        assertEquals(3, history.size());

        // Assert that the map contains an entry for today's date key
        assertTrue(history.containsKey(todayKey));
        // Assert that the value in the map for today equals today's water total we observed
        assertEquals(todayAfter, history.get(todayKey));
    }

    // Test for a "fresh" user, history map should contain only zeros for all requested days
    @Test
    void getWaterHistoryMap_forNewUser_returnsAllZerosWithExpectedKeys() throws Exception {
        // Choose the number of days we want to request in the history map
        int days = 7;
        // Create a date formatter that matches the format used inside FirebaseService ("yyyy-MM-dd")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        // Create a Calendar instance initialized to "now" (today)
        Calendar cal = Calendar.getInstance();

        // Create a LinkedHashMap to store the expected result (keeps insertion order)
        Map<String, Long> expected = new LinkedHashMap<>();
        // Generate the last `days` date keys and put 0L for each (new user has no water logs)
        for (int i = 0; i < days; i++) {
            // Format the current calendar date to the string key
            String dateKey = sdf.format(cal.getTime());
            // Put this date key with a value of 0 (no water logged) into the expected map
            expected.put(dateKey, 0L);
            // Move the calendar one day backwards
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }

        // Call the service to get the actual water history map for the second test user
        CompletableFuture<Map<String, Long>> future =
                firebaseService.getWaterHistoryMap(TEST_USERNAME_2, days);
        // Wait for the asynchronous result with a timeout of 20 seconds
        Map<String, Long> actual = future.get(20, TimeUnit.SECONDS);
        // Assert that the actual map is not null
        assertNotNull(actual);
        // Assert that the actual map is exactly equal to the expected map (keys and values)
        assertEquals(expected, actual);
    }

    // Test that a "fresh" user (second test user) with no water updates returns zeros
    @Test
    void getWater_forNewUser_returnsZeroTotals() throws Exception {
        // Call getWater for the second test user (assuming no water updates done yet)
        CompletableFuture<JSONObject> future = firebaseService.getWater(TEST_USERNAME_2);
        // Wait for the JSON result with a timeout of 20 seconds
        JSONObject json = future.get(20, TimeUnit.SECONDS);
        // Assert that the JSON object is not null (service returns an object, not null)
        assertNotNull(json);
        // Extract today's water amount from the JSON object
        long today = json.getLong("todayWater");
        // Extract yesterday's water amount from the JSON object
        long yesterday = json.getLong("yesterdayWater");
        // Assert that today's water is zero for a new user
        assertEquals(0, today);
        // Assert that yesterday's water is also zero for a new user
        assertEquals(0, yesterday);
    }

    // --------------------------- GOAL MODULE TESTS ---------------------------

    // Test that updateGoalMl changes the goal and getGoalMl reads the updated value
    @Test
    void updateGoalMl_changesGoal_and_getGoalMlReadsIt() throws Exception {
        // Choose a valid goal value in the allowed range (between 500 and 10000)
        int newGoal = 3200;

        // Call updateGoalMl to set the new goal for the main test user
        CompletableFuture<Boolean> updateFuture =
                firebaseService.updateGoalMl(TEST_USERNAME_1, newGoal);
        // Wait for the update result with a timeout of 20 seconds
        Boolean updated = updateFuture.get(20, TimeUnit.SECONDS);
        // Assert that the update operation returned true
        assertTrue(updated);

        // Call getGoalMl to read the goal for the same user
        CompletableFuture<Integer> getFuture =
                firebaseService.getGoalMl(TEST_USERNAME_1);
        // Wait for the goal result with a timeout of 20 seconds
        Integer goalValue = getFuture.get(20, TimeUnit.SECONDS);
        // Assert that the returned goal is not null
        assertNotNull(goalValue);
        // Assert that the returned goal equals the value we set
        assertEquals(newGoal, goalValue.intValue());
    }

    // Test that updateGoalMl rejects out-of-range values and does not change the stored goal
    @Test
    void updateGoalMl_outOfRange_isRejectedAndValueNotChanged() throws Exception {
        // Build a unique username for this test run
        String username = "goalInvalidDeep_" + System.currentTimeMillis();

        // Create a new User instance for this test
        User user = new User();
        // Set username for the test user
        user.setUserName(username);
        // Set a password for the test user
        user.setPassword("p");

        // Create the user in Firebase using createUser
        CompletableFuture<Boolean> createFuture =
                firebaseService.createUser(user);
        // Wait for the creation result with a timeout of 20 seconds
        Boolean created = createFuture.get(20, TimeUnit.SECONDS);
        // Assert that the user was created successfully
        assertTrue(created);

        // Call getGoalMl before any explicit update to read the default goal
        CompletableFuture<Integer> beforeFuture =
                firebaseService.getGoalMl(username);
        // Wait for the goal result with a timeout of 20 seconds
        Integer before = beforeFuture.get(20, TimeUnit.SECONDS);
        // Assert that the default goal value is 3000 for a new user
        assertEquals(3000, before.intValue());

        // Try to update the goal with a value below the allowed range
        CompletableFuture<Boolean> lowFuture =
                firebaseService.updateGoalMl(username, 100);
        // Wait for the low update result with a timeout of 20 seconds
        Boolean low = lowFuture.get(20, TimeUnit.SECONDS);

        // Try to update the goal with a value above the allowed range
        CompletableFuture<Boolean> highFuture =
                firebaseService.updateGoalMl(username, 20000);
        // Wait for the high update result with a timeout of 20 seconds
        Boolean high = highFuture.get(20, TimeUnit.SECONDS);

        // Assert that both out-of-range updates were rejected
        assertFalse(low);
        assertFalse(high);

        // Call getGoalMl again after the invalid updates
        CompletableFuture<Integer> afterFuture =
                firebaseService.getGoalMl(username);
        // Wait for the goal result with a timeout of 20 seconds
        Integer after = afterFuture.get(20, TimeUnit.SECONDS);
        // Assert that the goal value did not change after invalid updates
        assertEquals(before.intValue(), after.intValue());

        // Clean up: delete the temporary test user from Firebase
        CompletableFuture<Boolean> deleteFuture =
                firebaseService.deleteUser(username);
        // Wait for the delete result with a timeout of 20 seconds
        Boolean deleted = deleteFuture.get(20, TimeUnit.SECONDS);
        // Assert that the delete operation succeeded
        assertTrue(deleted);
    }

    // Test that patchUser can update goalMl and that getGoalMl reflects this change
    @Test
    void patchUser_canUpdateGoalMlField_and_getGoalMlSeesChange() throws Exception {
        // Create a Map to hold partial updates for the user
        Map<String, Object> updates = new HashMap<>();
        // Put a new goalMl value into the updates map
        updates.put("goalMl", 4500);

        // Call patchUser with the partial updates for the main test user
        CompletableFuture<User> patchFuture =
                firebaseService.patchUser(TEST_USERNAME_1, updates);
        // Wait for the updated User object with a timeout of 20 seconds
        User updatedUser = patchFuture.get(20, TimeUnit.SECONDS);
        // Assert that the returned User object is not null
        assertNotNull(updatedUser);

        // Call getGoalMl to verify that goalMl was really updated in Firebase
        CompletableFuture<Integer> goalFuture =
                firebaseService.getGoalMl(TEST_USERNAME_1);
        // Wait for the goal result with a timeout of 20 seconds
        Integer goalValue = goalFuture.get(20, TimeUnit.SECONDS);
        // Assert that the returned goal is not null
        assertNotNull(goalValue);
        // Assert that the returned goal equals the value we patched
        assertEquals(4500, goalValue.intValue());
    }

    // --------------------------- CALORIES MODULE TESTS ---------------------------

    // Test that updateCalories sets the field and getCalories reads the same value
    @Test
    void updateCalories_setsValue_and_getCaloriesReadsIt() throws Exception {
        // Choose a valid calories value (between 0 and 20000 according to validation)
        int newCalories = 1234;

        // Call updateCalories for the main test user
        CompletableFuture<Boolean> updateFuture =
                firebaseService.updateCalories(TEST_USERNAME_1, newCalories);
        // Wait for the update result with a timeout of 20 seconds
        Boolean updated = updateFuture.get(20, TimeUnit.SECONDS);
        // Assert that the update operation succeeded
        assertTrue(updated);

        // Call getCalories to read the calories value for the same user
        CompletableFuture<Integer> getFuture =
                firebaseService.getCalories(TEST_USERNAME_1);
        // Wait for the calories result with a timeout of 20 seconds
        Integer calories = getFuture.get(20, TimeUnit.SECONDS);
        // Assert that the returned calories value is not null
        assertNotNull(calories);
        // Assert that the returned calories value matches what we set
        assertEquals(newCalories, calories.intValue());
    }

    // Test that getCalories returns 0 for a user with no calories set yet (second test user)
    @Test
    void getCalories_forNewUser_returnsZero() throws Exception {
        // Call getCalories for the second test user
        CompletableFuture<Integer> getFuture =
                firebaseService.getCalories(TEST_USERNAME_2);
        // Wait for the calories result with a timeout of 20 seconds
        Integer calories = getFuture.get(20, TimeUnit.SECONDS);
        // Assert that the returned calories value is not null
        assertNotNull(calories);
        // Assert that for a new user, calories default is 0
        assertEquals(0, calories.intValue());
    }

    // Test that getCalories returns 0 when the user does not exist in Firebase
    @Test
    void getCalories_userNotFound_returnsZero() throws Exception {
        // Build a username that should not exist in Firebase
        String missingUsername = "noSuchUser_" + System.currentTimeMillis();

        // Call getCalories for this non-existing username
        CompletableFuture<Integer> future =
                firebaseService.getCalories(missingUsername);
        // Wait for the calories result with a timeout of 20 seconds
        Integer cals = future.get(20, TimeUnit.SECONDS);
        // Assert that the returned calories value is exactly 0
        assertEquals(0, cals.intValue());
    }

    // Test that updateCalories accepts valid values, rejects invalid ones, and keeps the last valid value
    @Test
    void updateCalories_validAndInvalidValues_behaveAsExpected() throws Exception {
        // Build a unique username for this test run
        String username = "calDeep_" + System.currentTimeMillis();

        // Create a new User instance for this test
        User user = new User();
        // Set username for the test user
        user.setUserName(username);
        // Set a password for the test user
        user.setPassword("p");

        // Create the user in Firebase
        CompletableFuture<Boolean> createFuture =
                firebaseService.createUser(user);
        // Wait for the creation result with a timeout of 20 seconds
        Boolean created = createFuture.get(20, TimeUnit.SECONDS);
        // Assert that the user was created successfully
        assertTrue(created);

        // Read the initial calories value for this user
        CompletableFuture<Integer> initialFuture =
                firebaseService.getCalories(username);
        // Wait for the calories result with a timeout of 20 seconds
        Integer initial = initialFuture.get(20, TimeUnit.SECONDS);
        // Assert that the initial calories value is 0
        assertEquals(0, initial.intValue());

        // ---- Valid update ----

        // Call updateCalories with a valid value inside the allowed range
        CompletableFuture<Boolean> validUpdateFuture =
                firebaseService.updateCalories(username, 1200);
        // Wait for the update result with a timeout of 20 seconds
        Boolean validUpdated = validUpdateFuture.get(20, TimeUnit.SECONDS);
        // Assert that the update operation succeeded
        assertTrue(validUpdated);

        // Read calories after the valid update
        CompletableFuture<Integer> afterValidFuture =
                firebaseService.getCalories(username);
        // Wait for the calories result with a timeout of 20 seconds
        Integer afterValid = afterValidFuture.get(20, TimeUnit.SECONDS);
        // Assert that the calories value was updated correctly to 1200
        assertEquals(1200, afterValid.intValue());

        // ---- Invalid updates ----

        // Try to update calories with a negative value (invalid)
        CompletableFuture<Boolean> invalidLowFuture =
                firebaseService.updateCalories(username, -5);
        // Wait for the update result with a timeout of 20 seconds
        Boolean invalidLow = invalidLowFuture.get(20, TimeUnit.SECONDS);
        // Assert that the negative update was rejected
        assertFalse(invalidLow);

        // Try to update calories with an extremely high value (invalid)
        CompletableFuture<Boolean> invalidHighFuture =
                firebaseService.updateCalories(username, 50000);
        // Wait for the update result with a timeout of 20 seconds
        Boolean invalidHigh = invalidHighFuture.get(20, TimeUnit.SECONDS);
        // Assert that the too-high update was rejected
        assertFalse(invalidHigh);

        // Read calories again after both invalid updates
        CompletableFuture<Integer> afterInvalidFuture =
                firebaseService.getCalories(username);
        // Wait for the calories result with a timeout of 20 seconds
        Integer afterInvalid = afterInvalidFuture.get(20, TimeUnit.SECONDS);
        // Assert that the calories value remained equal to the last valid value (1200)
        assertEquals(1200, afterInvalid.intValue());

        // Clean up: delete the temporary test user
        CompletableFuture<Boolean> deleteFuture =
                firebaseService.deleteUser(username);
        // Wait for the delete result with a timeout of 20 seconds
        Boolean deleted = deleteFuture.get(20, TimeUnit.SECONDS);
        // Assert that the delete operation succeeded
        assertTrue(deleted);
    }

    // --------------------------- BMI DISTRIBUTION BASIC TEST ---------------------------
    // Deep test: BMI distribution should correctly count 4 new users, one in each category
    @Test
    void getBmiDistribution_countsEachBmiCategoryForNewUsers() throws Exception {
        // Call getBmiDistribution once to capture the initial state before adding test users
        CompletableFuture<Map<String, Integer>> beforeFuture =
                firebaseService.getBmiDistribution();
        // Wait for the "before" distribution map with a timeout of 20 seconds
        Map<String, Integer> before = beforeFuture.get(20, TimeUnit.SECONDS);
        // Assert that the "before" map is not null
        assertNotNull(before);

        // Helper method to read a value from the map or default to 0 if missing
        // Read the initial count for the "Underweight" category
        int underBefore = before.getOrDefault("Underweight", 0);
        // Read the initial count for the "Normal" category
        int normalBefore = before.getOrDefault("Normal", 0);
        // Read the initial count for the "Overweight" category
        int overBefore = before.getOrDefault("Overweight", 0);
        // Read the initial count for the "Obese" category
        int obeseBefore = before.getOrDefault("Obese", 0);

        // Build a common prefix for temporary BMI test users using the current timestamp
        String prefix = "bmiTestUser_" + System.currentTimeMillis();

        // Create an array of usernames for the four BMI test users
        String[] bmiUsers = new String[] {
                prefix + "_u",   // Underweight user
                prefix + "_n",   // Normal user
                prefix + "_o",   // Overweight user
                prefix + "_ob"   // Obese user
        };

        // Create an array of BMI values matching the categories in the same order
        double[] bmiValues = new double[] {
                17.0,  // Underweight (<18.5)
                22.0,  // Normal     (18.5–24.9)
                27.0,  // Overweight (25–29.9)
                32.0   // Obese      (>=30)
        };

        // Loop index to create users and assign BMI values
        for (int i = 0; i < bmiUsers.length; i++) {
            // Create a new User instance for the current BMI test user
            User u = new User();
            // Set the username for this test user
            u.setUserName(bmiUsers[i]);
            // Set a simple password
            u.setPassword("bmiPass");

            // Create the user in Firebase using createUser
            CompletableFuture<Boolean> createFuture = firebaseService.createUser(u);
            // Wait for the creation result with a timeout of 20 seconds
            Boolean created = createFuture.get(20, TimeUnit.SECONDS);
            // Assert that the user was created successfully (or at least not failed)
            assertTrue(created);

            // Update the BMI value for this user according to the array
            CompletableFuture<Boolean> bmiFuture =
                    firebaseService.updateBmi(bmiUsers[i], bmiValues[i]);
            // Wait for the BMI update result with a timeout of 20 seconds
            Boolean bmiUpdated = bmiFuture.get(20, TimeUnit.SECONDS);
            // Assert that the BMI update operation succeeded
            assertTrue(bmiUpdated);
        }

        // Call getBmiDistribution again after adding the four new test users
        CompletableFuture<Map<String, Integer>> afterFuture =
                firebaseService.getBmiDistribution();
        // Wait for the "after" distribution map with a timeout of 20 seconds
        Map<String, Integer> after = afterFuture.get(20, TimeUnit.SECONDS);
        // Assert that the "after" map is not null
        assertNotNull(after);

        // Read the updated count for the "Underweight" category
        int underAfter = after.getOrDefault("Underweight", 0);
        // Read the updated count for the "Normal" category
        int normalAfter = after.getOrDefault("Normal", 0);
        // Read the updated count for the "Overweight" category
        int overAfter = after.getOrDefault("Overweight", 0);
        // Read the updated count for the "Obese" category
        int obeseAfter = after.getOrDefault("Obese", 0);

        // Assert that the "Underweight" count increased exactly by 1
        assertEquals(underBefore + 1, underAfter);
        // Assert that the "Normal" count increased exactly by 1
        assertEquals(normalBefore + 1, normalAfter);
        // Assert that the "Overweight" count increased exactly by 1
        assertEquals(overBefore + 1, overAfter);
        // Assert that the "Obese" count increased exactly by 1
        assertEquals(obeseBefore + 1, obeseAfter);

        // Finally, clean up all four temporary BMI test users from Firebase
        for (String uname : bmiUsers) {
            // Call deleteUser for the current temporary BMI user
            CompletableFuture<Boolean> deleteFuture = firebaseService.deleteUser(uname);
            // Wait for the deletion result with a timeout of 20 seconds
            Boolean deleted = deleteFuture.get(20, TimeUnit.SECONDS);
            // Assert that the delete operation finished successfully
            assertTrue(deleted);
        }
    }

}
