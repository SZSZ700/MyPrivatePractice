# 🌐 Hi-Bari Spring Boot Server

## 📌 Overview

This directory contains the backend server for the Hi-Bari health and water tracking system.

The server exposes a REST API used by the Android application, handles business logic and validation, and communicates with Firebase Realtime Database through the Firebase Admin SDK.

The backend follows a layered architecture based on controllers, services, repository interfaces, Firebase repository implementations, DTOs, configuration, and centralized exception handling.

---

## 🧠 Server Responsibilities

The Spring Boot server is responsible for:

- User registration and login
- User data management
- BMI data updates
- Calories management
- Daily water goal management
- Water intake updates
- Water history retrieval
- Weekly water averages
- BMI statistics
- Request validation
- REST API response handling
- Firebase communication
- Transaction-safe database updates
- Asynchronous Firebase operations
- Centralized validation error handling

---

## 🗂 Project Structure

```text
Spring Server/
├── README.md
├── pom.xml
├── .gitignore
│
└── src/
    ├── main/
    │   ├── java/
    │   │   └── org/example/CapstoneProject/
    │   │       ├── Application.java
    │   │       │
    │   │       ├── config/
    │   │       │   └── FirebaseConfiguration.java
    │   │       │
    │   │       ├── dto/
    │   │       │   ├── LoginRequest.java
    │   │       │   ├── SignupRequest.java
    │   │       │   ├── UpdateUserRequest.java
    │   │       │   ├── UserResponse.java
    │   │       │   ├── WaterResponse.java
    │   │       │   ├── GoalResponse.java
    │   │       │   ├── GoalUpdateResponse.java
    │   │       │   └── CaloriesResponse.java
    │   │       │
    │   │       ├── EnvConfiguration/
    │   │       │   └── EnvConfig.java
    │   │       │
    │   │       ├── exception/
    │   │       │   └── GlobalExceptionHandler.java
    │   │       │
    │   │       ├── model/
    │   │       │   └── User.java
    │   │       │
    │   │       ├── repository/
    │   │       │   ├── UserRepository.java
    │   │       │   ├── WaterRepository.java
    │   │       │   │
    │   │       │   └── firebase/
    │   │       │       ├── FirebaseUserRepository.java
    │   │       │       └── FirebaseWaterRepository.java
    │   │       │
    │   │       ├── service/
    │   │       │   ├── AuthenticationService.java
    │   │       │   ├── UserService.java
    │   │       │   ├── WaterService.java
    │   │       │   ├── UserHealthService.java
    │   │       │   └── StatisticsService.java
    │   │       │
    │   │       └── web/
    │   │           └── UsersController.java
    │   │
    │   └── resources/
    │
    └── test/
        └── java/
            └── CapstoneTests/
                ├── CapstoneServicesIntegrationTest.java
                └── UsersControllerIntegrationTest.java
```

---

## 🧩 Main Components

### `Application.java`

The main Spring Boot application class used to start the server.

---

### `UsersController.java`

Defines the REST API endpoints used by the Android application.

The controller is responsible for:

- Receiving HTTP requests
- Reading path variables and query parameters
- Receiving and validating request DTOs
- Calling the appropriate service
- Converting service results into HTTP responses
- Returning response DTOs or dynamic map-based responses when appropriate

The controller does not communicate with Firebase directly.

---

## 🧠 Service Layer

The service layer is divided by responsibility.

### `AuthenticationService.java`

Handles authentication-related application logic.

Responsibilities include:

- User signup
- Username existence checks during registration
- Login credential validation

### `UserService.java`

Handles general user operations.

Responsibilities include:

- Retrieving users
- Updating users
- Patching users
- Deleting users
- Checking whether a user exists

### `WaterService.java`

Handles water-related application operations.

Responsibilities include:

- Updating daily water intake
- Retrieving today's and yesterday's water
- Retrieving water history
- Retrieving weekly averages
- Managing daily water goals

### `UserHealthService.java`

Handles user health-related data.

Responsibilities include:

- Updating BMI
- Retrieving calories
- Updating calories

### `StatisticsService.java`

Handles global statistical operations.

Responsibilities include:

- Retrieving BMI distribution statistics

---

## 🗄 Repository Layer

The repository layer separates persistence operations from the service layer.

### Repository Interfaces

```text
UserRepository
WaterRepository
```

These interfaces define the persistence operations required by the application without exposing Firebase-specific implementation details.

### Firebase Repository Implementations

```text
FirebaseUserRepository
FirebaseWaterRepository
```

These classes implement the repository interfaces using Firebase Realtime Database.

Responsibilities include:

- Reading data from Firebase
- Creating and updating users
- Deleting users
- Querying users by username
- Updating BMI and calories
- Reading and updating water data
- Managing daily water goals
- Calculating water-related database results
- Performing Firebase transactions
- Wrapping Firebase callbacks with `CompletableFuture`

The service layer depends on repository interfaces instead of depending directly on Firebase-specific classes.

---

## ⚙️ Firebase Configuration

### `FirebaseConfiguration.java`

Initializes the Firebase Admin SDK and exposes the shared Firebase database reference as a Spring bean.

Responsibilities include:

- Loading Firebase Admin credentials
- Reading the Firebase database URL
- Initializing Firebase only once
- Creating the shared `Users` database reference
- Providing the database dependency to Firebase repository implementations

This keeps Firebase initialization outside the service and controller layers.

---

## 📦 DTO Layer

The REST API uses dedicated request and response DTOs.

### Request DTOs

```text
LoginRequest
SignupRequest
UpdateUserRequest
```

These classes represent JSON sent from the Android application to the backend.

### Response DTOs

```text
UserResponse
WaterResponse
GoalResponse
GoalUpdateResponse
CaloriesResponse
```

These classes represent stable JSON structures returned by the backend.

Using DTOs prevents the REST layer from depending directly on the internal persistence model.

It also allows the backend structure to change while keeping the Android API contract stable.

---

## ✅ Validation

Request validation uses Jakarta Bean Validation.

Examples include:

```java
@Valid
@NotBlank
@Min
```

Validation is currently used for request DTOs such as:

```text
LoginRequest
SignupRequest
UpdateUserRequest
```

Example invalid request:

```json
{
  "userName": "",
  "password": ""
}
```

Example validation response:

```json
{
  "errors": {
    "userName": "Username is required",
    "password": "Password is required"
  }
}
```

---

## ⚠️ Global Exception Handling

### `GlobalExceptionHandler.java`

Validation exceptions are handled centrally using:

```java
@RestControllerAdvice
```

This prevents every controller endpoint from implementing its own validation error response logic.

The global exception handler currently converts `MethodArgumentNotValidException` into an HTTP `400 Bad Request` response containing field-specific validation messages.

---

## 🧠 Architecture

The backend follows this flow:

```text
Android Application
        ↓
HTTP REST Request
        ↓
UsersController
        ↓
Domain Service
        ↓
Repository Interface
        ↓
Firebase Repository Implementation
        ↓
Firebase Realtime Database
        ↓
HTTP Response
        ↓
Android Application
```

Example service flows:

```text
UsersController
    ↓
AuthenticationService
    ↓
UserRepository
    ↓
FirebaseUserRepository
```

```text
UsersController
    ↓
WaterService
    ↓
WaterRepository
    ↓
FirebaseWaterRepository
```

This architecture separates:

```text
HTTP handling
Business/application logic
Persistence contracts
Firebase-specific implementation
Database configuration
REST request/response models
```

---

## 🔄 Water Update Flow

When the Android application sends a request to add water:

1. The request reaches `UsersController`.
2. The controller reads the username and water amount.
3. The controller calls `WaterService`.
4. `WaterService` delegates the persistence operation to `WaterRepository`.
5. `FirebaseWaterRepository` locates the user's daily water log.
6. A Firebase transaction updates the data safely.
7. The new drink amount is added.
8. The total daily amount is updated.
9. Firebase stores the updated list.
10. The result returns through the repository and service layers.
11. The controller returns the HTTP response to the Android application.

```text
PATCH Request
      ↓
UsersController
      ↓
WaterService
      ↓
WaterRepository
      ↓
FirebaseWaterRepository
      ↓
Firebase Transaction
      ↓
Firebase Realtime Database
      ↓
HTTP Response
```

---

## ⚙️ Transaction-Safe Updates

Water consumption updates use Firebase transaction-based logic to prevent data loss when multiple requests are processed at nearly the same time.

```text
Without transaction:

Request A reads old value
Request B reads old value
One update may overwrite the other ❌
```

```text
With transaction:

Firebase processes the updates safely
Both updates are preserved ✅
```

This helps prevent race conditions and maintains database consistency.

---

## ⚡ Asynchronous Operations

Firebase uses callback-based asynchronous APIs.

The Firebase repository implementations wrap these callbacks with:

```java
CompletableFuture
```

This allows the rest of the application to use asynchronous method chains such as:

```java
thenApply(...)
thenCompose(...)
```

Controller methods can therefore return:

```java
CompletableFuture<ResponseEntity<...>>
```

without manually blocking while waiting for Firebase operations to complete.

---

## ☁️ Firebase Data Structure

Example:

```text
Users/
  userId/
    userName
    password
    fullName
    age
    bmi
    calories
    goalMl
    waterLog/
      yyyy-MM-dd/
        [total, drink1, drink2, ...]
```

Water log format:

```text
Index 0   → Total daily water intake
Index 1-N → Individual drink entries
```

Example:

```text
[1850, 150, 200, 500, 1000]
```

The list is dynamic and can contain multiple drink entries for the same day.

---

## 🌐 REST API

The controller base path is:

```text
/api/users
```

When using the configured application context path, the local server base URL is:

```text
http://localhost:8080/myapp/api/users
```

For the Android emulator:

```text
http://10.0.2.2:8080/myapp/api/users
```

### Main Endpoints

```text
GET     /health
POST    /signup
POST    /login

GET     /
GET     /{username}
HEAD    /{username}
PUT     /{username}
PATCH   /{username}
DELETE  /{username}

PATCH   /{username}/bmi

PATCH   /{username}/water
GET     /{username}/water
GET     /{username}/waterHistoryMap
GET     /{username}/weeklyAverages

GET     /{username}/goal
PUT     /{username}/goal

GET     /{username}/calories
PUT     /{username}/calories

GET     /stats/bmiDistribution
```

---

## 📤 API Response DTO Examples

### Water Response

```json
{
  "todayWater": 1850,
  "yesterdayWater": 1600
}
```

### Daily Goal Response

```json
{
  "goalMl": 3000
}
```

### Goal Update Response

```json
{
  "status": "OK"
}
```

### Calories Response

```json
{
  "calories": 1800
}
```

These DTOs preserve the JSON structure expected by the Android application.

---

## 🧪 Backend Testing

The server includes integration tests for the service/repository architecture and the REST controller layer.

Testing technologies include:

- JUnit 5
- JUnit Jupiter
- Spring Boot Test
- TestRestTemplate
- Firebase integration testing
- Asynchronous operation testing

---

### `CapstoneServicesIntegrationTest`

Tests the refactored backend service flow against Firebase.

The test class works with services such as:

```text
UserService
AuthenticationService
WaterService
UserHealthService
StatisticsService
```

The tests cover operations such as:

- Creating users
- Retrieving users
- Authentication behavior
- Updating user information
- Updating BMI
- Updating calories
- Updating water consumption
- Retrieving water history
- Retrieving weekly averages
- Managing daily goals
- BMI distribution
- Asynchronous Firebase operations
- Transaction-safe updates

---

### `UsersControllerIntegrationTest`

Loads the Spring Boot application and sends real HTTP requests using `TestRestTemplate`.

The test server uses a random embedded port.

The Spring Boot server does not need to be started manually before running these integration tests.

The tests cover:

- REST endpoints
- Signup
- Login
- Request DTOs
- Response DTOs
- Validation
- Response bodies
- HTTP status codes
- GET requests
- POST requests
- PUT requests
- PATCH requests
- DELETE requests
- HEAD requests
- Water operations
- BMI operations
- Calories operations
- Daily goals
- Error handling

---

## 🔐 Security

The server uses Firebase Admin SDK credentials for backend access to Firebase.

Sensitive files are intentionally excluded from the repository.

Examples:

```text
.env
application.properties
Firebase Admin SDK JSON file
```

These files must never be committed to GitHub.

The Android application does not contain Firebase Admin credentials.

Firebase database access is performed by the backend repository layer.

The backend also uses request validation to reject invalid required input before database operations are performed.

Passwords were removed from the `User.toString()` representation to reduce accidental backend logging.

### Current Security Limitation

The current authentication implementation still requires additional security improvements before production use.

Planned improvements include:

```text
Password hashing
Token-based authentication
Removing passwords from API responses
Removing sensitive request-body logging
```

---

## ⚙️ Local Configuration

Before running the server, create the required local configuration files.

Configuration may include:

```text
Firebase credentials path
Firebase database URL
Server configuration
```

Do not place real credentials directly inside source code.

Do not upload credentials, passwords, private keys, or Firebase service-account files to GitHub.

---

## ▶️ Running the Server

### Requirements

- Java
- Maven
- Internet access
- Firebase project
- Firebase Admin SDK credentials

### Start from IntelliJ IDEA

1. Open the `Spring Server` directory.
2. Allow Maven to download the dependencies.
3. Add the required local Firebase configuration.
4. Run:

```text
src/main/java/org/example/CapstoneProject/Application.java
```

### Start from the Terminal

From the `Spring Server` directory:

```bash
mvn spring-boot:run
```

---

## 🧪 Running the Tests

Run:

```bash
mvn test
```

The Spring integration tests automatically start the required application context.

`UsersControllerIntegrationTest` starts an embedded server on a random port, so a manually running server is not required.

---

## 🛠 Technologies

- Java
- Spring Boot 3
- Spring Web
- Jakarta Bean Validation
- Maven
- REST API
- Firebase Admin SDK
- Firebase Realtime Database
- CompletableFuture
- JUnit 5
- JUnit Jupiter
- Spring Boot Test
- TestRestTemplate

---

## 🏗 Key Backend Design Features

- Layered backend architecture
- Controller / Service / Repository separation
- Repository interfaces
- Dedicated Firebase repository implementations
- Constructor dependency injection
- Centralized Firebase configuration
- Request DTOs
- Response DTOs
- Jakarta Bean Validation
- Global exception handling
- Asynchronous Firebase operations
- Transaction-safe water updates
- Dynamic daily water logs
- Android API compatibility preserved during backend refactoring
- Automated integration testing

---

## 🚀 Future Improvements

- Password hashing
- Token-based authentication such as JWT
- Remove passwords from response DTOs
- Remove sensitive request-body logging
- More detailed validation
- Additional centralized exception handling
- More detailed health statistics
- Cloud deployment
- Additional automated tests
- Additional repository abstractions if the application grows

---

## 🔗 Related Project

The Android client is located in:

```text
../Hai-Bari android application/
```

The main repository documentation is located in:

```text
../README.md
```

---

## 👨‍💻 Author

Sharbel Zarzour
