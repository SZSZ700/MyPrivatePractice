# 💧 Hi-Bari – Health & Water Tracking System

## 📌 Overview

Hi-Bari is a full-stack health tracking application designed to monitor daily water intake, calculate BMI, manage daily water goals, and store user health data.

The system combines an Android mobile client, a Spring Boot REST API, and Firebase Realtime Database.

The backend has been refactored into separated controller, service, repository, configuration, DTO, and exception-handling layers so that HTTP handling, business logic, and Firebase access are no longer mixed together.

---

## 🎥 Application Demo

Click the demo image in the repository to watch a short demonstration of the Hi-Bari Android application.

---

## 🗂 Repository Structure

The repository contains two main projects:

```text
MyPrivatePractice/
├── README.md
│
├── Hai-Bari android application/
│   ├── README.md
│   ├── app/
│   ├── gradle/
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   ├── gradle.properties
│   ├── gradlew
│   └── gradlew.bat
│
└── Spring Server/
    ├── README.md
    ├── pom.xml
    └── src/
        ├── main/
        │   └── java/
        │       └── org/example/CapstoneProject/
        │           ├── Application.java
        │           ├── config/
        │           │   └── FirebaseConfiguration.java
        │           ├── dto/
        │           │   ├── LoginRequest.java
        │           │   ├── SignupRequest.java
        │           │   ├── UpdateUserRequest.java
        │           │   ├── UserResponse.java
        │           │   ├── WaterResponse.java
        │           │   ├── GoalResponse.java
        │           │   ├── GoalUpdateResponse.java
        │           │   └── CaloriesResponse.java
        │           ├── exception/
        │           │   └── GlobalExceptionHandler.java
        │           ├── EnvConfiguration/
        │           │   └── EnvConfig.java
        │           ├── model/
        │           │   └── User.java
        │           ├── repository/
        │           │   ├── UserRepository.java
        │           │   ├── WaterRepository.java
        │           │   └── firebase/
        │           │       ├── FirebaseUserRepository.java
        │           │       └── FirebaseWaterRepository.java
        │           ├── service/
        │           │   ├── AuthenticationService.java
        │           │   ├── UserService.java
        │           │   ├── WaterService.java
        │           │   ├── UserHealthService.java
        │           │   └── StatisticsService.java
        │           └── web/
        │               └── UsersController.java
        │
        └── test/
            └── java/
                └── CapstoneTests/
                    ├── CapstoneServicesIntegrationTest.java
                    └── UsersControllerIntegrationTest.java
```

### 📱 Hai-Bari Android Application

Contains the Android client, user interface, session handling, water tracking, BMI calculation, charts, daily water goal management, and communication with the backend through OkHttp.

### 🌐 Spring Server

Contains the Spring Boot REST API, service layer, repository abstraction, Firebase repository implementations, request/response DTOs, validation, centralized exception handling, and transaction-safe database operations.

---

## 🧠 System Architecture

The system follows a layered client-server architecture:

```text
Android Application
        ↓
REST API
        ↓
UsersController
        ↓
Domain Services
        ↓
Repository Interfaces
        ↓
Firebase Repository Implementations
        ↓
Firebase Realtime Database
```

The backend flow is:

```text
Controller
    ↓
Service
    ↓
Repository Interface
    ↓
Firebase Repository Implementation
    ↓
Firebase
```

This separation keeps Firebase-specific code out of the controller and service layers.

### 📱 Android Client

- Java-based Android application
- Uses OkHttp for REST communication
- Stores local session data using SharedPreferences
- Uses MPAndroidChart for data visualization
- Sends and receives JSON through the Spring Boot REST API
- Does not contain Firebase Admin credentials

Main application areas include:

- Login
- Signup
- Home page
- Water tracking
- BMI tracking
- Water history and charts
- Daily water goal management

### 🌐 Backend – Spring Boot

The backend is divided into dedicated layers.

#### Controller Layer

```text
web/
└── UsersController.java
```

Responsibilities:

- Defines REST endpoints
- Reads path variables, query parameters, and request bodies
- Converts service results into HTTP responses
- Uses request and response DTOs
- Does not access Firebase directly

#### Service Layer

```text
service/
├── AuthenticationService.java
├── UserService.java
├── WaterService.java
├── UserHealthService.java
└── StatisticsService.java
```

Responsibilities:

- Coordinates application operations
- Contains domain-oriented service logic
- Delegates persistence operations to repository interfaces
- Does not contain Firebase initialization code

#### Repository Layer

```text
repository/
├── UserRepository.java
├── WaterRepository.java
└── firebase/
    ├── FirebaseUserRepository.java
    └── FirebaseWaterRepository.java
```

Responsibilities:

- Defines persistence contracts through interfaces
- Contains Firebase-specific database operations only in Firebase repository implementations
- Uses asynchronous Firebase callbacks and `CompletableFuture`
- Handles Firebase reads, writes, queries, updates, and transactions

#### Configuration Layer

```text
config/
└── FirebaseConfiguration.java
```

Responsibilities:

- Initializes Firebase Admin SDK
- Loads Firebase credentials
- Provides the shared `DatabaseReference` Spring bean
- Uses constructor-based dependency injection throughout the application

#### DTO Layer

```text
dto/
├── LoginRequest.java
├── SignupRequest.java
├── UpdateUserRequest.java
├── UserResponse.java
├── WaterResponse.java
├── GoalResponse.java
├── GoalUpdateResponse.java
└── CaloriesResponse.java
```

DTOs separate the REST API contract from the internal `User` persistence model.

Request DTOs are used for incoming JSON, while response DTOs define stable JSON structures returned to the Android client.

Current DTO integration preserves the existing Android JSON contract.

#### Validation and Exception Handling

Request validation uses Jakarta Bean Validation:

```text
@Valid
@NotBlank
@Min
```

Validation failures are handled centrally by:

```text
exception/
└── GlobalExceptionHandler.java
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

This keeps validation error handling out of individual controller methods.

---

## ☁️ Firebase Realtime Database

Main user data is stored under:

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

For each daily water list:

- Index `0` → total water consumed that day
- Index `1..N` → individual drink entries

Firebase access is performed by the repository layer instead of directly by controllers or services.

---

## 🔄 Data Flow – Water Update

1. The user presses an Add Water button.
2. The Android application sends a PATCH request.
3. `UsersController` receives the request.
4. `WaterService` handles the water-related application flow.
5. `WaterRepository` defines the required persistence operation.
6. `FirebaseWaterRepository` performs the Firebase transaction.
7. Firebase Realtime Database stores the updated water data.
8. The result travels back through the service and controller.
9. The server returns an HTTP response to the Android application.

```text
User Action
    ↓
Android Application
    ↓
PATCH /api/users/{username}/water
    ↓
UsersController
    ↓
WaterService
    ↓
WaterRepository
    ↓
FirebaseWaterRepository
    ↓
Firebase Realtime Database
    ↓
Response to Android
```

---

## ⚙️ Advanced Implementation

### 🔹 Repository Abstraction

Database access is defined through repository interfaces:

```text
UserRepository
WaterRepository
```

The current persistence implementation is Firebase:

```text
FirebaseUserRepository
FirebaseWaterRepository
```

This keeps the higher layers independent from Firebase-specific APIs.

### 🔹 Constructor Dependency Injection

Services and repositories are injected using constructors.

This makes dependencies explicit and avoids direct object creation inside controllers.

### 🔹 Asynchronous Backend Operations

Firebase callback-based APIs are wrapped with:

```java
CompletableFuture
```

This allows controller methods to return asynchronous results without blocking on Firebase operations.

### 🔹 Transaction-Based Water Updates

Water consumption is updated using Firebase transactions to prevent data loss when multiple updates happen close together.

Example:

```text
Two requests arrive at the same time

Without transaction → one update may overwrite the other ❌
With transaction    → both updates are stored safely ✅
```

### 🔹 Dynamic Water Data Structure

- Daily water data is stored as a dynamic list
- There is no fixed number of drink entries
- The first value stores the daily total
- Additional values store individual drink amounts

### 🔹 Request and Response DTOs

The backend no longer needs to expose the internal `User` model directly through user-related REST responses.

Examples:

```text
LoginRequest
SignupRequest
UpdateUserRequest
UserResponse
WaterResponse
GoalResponse
GoalUpdateResponse
CaloriesResponse
```

This improves separation between persistence data and the public REST contract.

### 🔹 Centralized Validation Errors

Invalid request bodies are handled through a global exception handler instead of repeating validation response logic in every endpoint.

---

## 🧪 Software Testing

The project includes automated tests for both the Android application and the Spring Boot backend.

### 📱 Android Testing

Testing technologies include:

- JUnit 4
- Robolectric
- Mockito
- OkHttp MockWebServer
- AndroidX Test

Android tests cover:

- Activity behavior
- User interface logic
- Login and signup flows
- BMI calculations
- Daily water goal management
- Water intake updates
- Weekly chart behavior
- REST API communication
- HTTP request and response handling
- Network errors
- Toast messages
- Android runtime behavior

Robolectric is used to test Android components directly on the JVM without requiring a physical device or emulator.

MockWebServer is used to simulate backend responses and inspect outgoing HTTP requests.

Mockito is used to create mock objects and isolate dependencies.

### 🌐 Spring Boot Testing

Testing technologies include:

- JUnit 5 / JUnit Jupiter
- Spring Boot Test
- TestRestTemplate
- Firebase integration testing
- Asynchronous operation testing

The backend includes two main integration-test groups:

```text
CapstoneServicesIntegrationTest
UsersControllerIntegrationTest
```

`CapstoneServicesIntegrationTest` verifies the refactored service and repository flow against Firebase.

`UsersControllerIntegrationTest` runs Spring Boot with an embedded server on a random port and performs real HTTP requests through `TestRestTemplate`.

Backend tests cover:

- Spring application context startup
- Service-to-repository integration
- REST controller endpoints
- Signup and login behavior
- User creation, retrieval, update, patch, delete, and existence checks
- HTTP GET, POST, PUT, PATCH, DELETE, and HEAD
- HTTP status codes and response bodies
- Validation behavior
- Water intake updates
- Water history
- Weekly averages
- Daily water goals
- BMI updates and distribution
- Calories
- Firebase transactions
- Asynchronous operations
- Error handling

---

## 🔐 Security

Current security-related design:

- Firebase Admin SDK is used only by the Spring Boot backend
- Firebase Admin credentials are not stored in the Android application
- Sensitive local configuration files are excluded from version control
- Firebase access is centralized in backend repository implementations
- Request validation rejects missing or invalid required input before business operations run
- User passwords are no longer included in the `User.toString()` output, reducing accidental logging on the backend

Sensitive files excluded from the repository include:

```text
.env
application.properties
Firebase Admin SDK JSON file
local.properties
*.jks
*.keystore
```

> **Current limitation:** password hashing and token-based authentication are planned improvements. The current authentication flow still requires additional security hardening before production deployment.

---

## 🛠 Technologies Used

### Android Client

- Java
- Android SDK
- Gradle
- Kotlin DSL
- OkHttp
- SharedPreferences
- MPAndroidChart

### Backend

- Java
- Spring Boot 3
- Spring Web
- Jakarta Bean Validation
- Maven
- REST API
- Firebase Admin SDK
- CompletableFuture

### Database

- Firebase Realtime Database

### Testing

#### Android

- JUnit 4
- Robolectric
- Mockito
- OkHttp MockWebServer
- AndroidX Test

#### Backend

- JUnit 5 / JUnit Jupiter
- Spring Boot Test
- TestRestTemplate
- Firebase integration testing

### Development Tools

- Android Studio
- IntelliJ IDEA
- Git
- GitHub

---

## 📊 Features

### 👤 User System

- Signup
- Login
- User retrieval
- Full user update
- Partial user update
- User deletion
- User existence checks
- Local Android session management

### 💧 Water Tracking

- Add 150 ml
- Add 200 ml
- Add 1000 ml
- Track daily totals
- Store individual drink entries
- Store daily history
- Calculate weekly averages
- Configure a daily water goal

### ⚖️ BMI Tracking

- Calculate BMI
- Store BMI data
- Update BMI
- Retrieve BMI-related information
- Calculate global BMI distribution statistics

### 🔥 Calories

- Store calories
- Retrieve calories
- Validate allowed calorie updates

### 📈 Visualization

- Weekly water chart
- Daily water tracking
- Historical water consumption
- Daily goal progress

---

## 🌐 Main REST Endpoints

Base path:

```text
/api/users
```

Selected endpoints:

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

## ▶️ Running the Project

### Android Application

Open this directory in Android Studio:

```text
Hai-Bari android application
```

Allow Gradle to synchronize, make sure the Spring Boot server is running, select an emulator or physical device, and run the application.

For the Android emulator, the backend is accessed through:

```text
http://10.0.2.2:8080/myapp/api/users
```

### Spring Boot Server

Open this directory in IntelliJ IDEA:

```text
Spring Server
```

Run:

```text
Spring Server/
└── src/main/java/org/example/CapstoneProject/Application.java
```

Or from the terminal:

```bash
mvn spring-boot:run
```

The configured server context path is:

```text
/myapp
```

Example health endpoint:

```text
http://localhost:8080/myapp/api/users/health
```

Sensitive Firebase configuration files are required locally but are not included in the repository.

### Running Backend Tests

The Spring Boot integration tests start the required Spring application context automatically.

The controller integration tests use an embedded web server with a random port, so the server does not need to be started manually before running the tests.

Run from IntelliJ IDEA or:

```bash
mvn test
```

---

## 🚀 Future Improvements

- Password hashing
- Token-based authentication such as JWT
- Remove passwords from API response payloads
- Remove sensitive request-body logging from Android debug interceptors
- More detailed health statistics
- Smart hydration suggestions
- Improved UI and UX
- Improved notification scheduling
- Cloud deployment for the Spring Boot server
- Additional charts and reports
- Offline data support
- Additional automated test coverage
- Further separation of calculation logic from persistence logic where appropriate

---

## 👨‍💻 Author

Sharbel Zarzour

---

## 🎓 Academic Context

This project was developed as a final capstone project in Software Engineering studies.

---

## 💡 Key Strengths

- Full-stack architecture
- Android mobile client
- Spring Boot REST API
- Layered backend architecture
- Controller / Service / Repository separation
- Repository abstraction
- Dedicated Firebase repository implementations
- Request and response DTOs
- Jakarta Bean Validation
- Centralized exception handling
- Constructor dependency injection
- Cloud-based real-time database
- Automated Android and backend testing
- Robolectric-based JVM testing
- Mocked HTTP testing with MockWebServer
- Spring integration testing with TestRestTemplate
- Asynchronous Firebase operations with CompletableFuture
- Transaction-safe water updates
- Dynamic water-log data structure
- Separation between client, server, and database
- Organized multi-project repository
- API compatibility preserved during backend refactoring
