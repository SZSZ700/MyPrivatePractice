# 🌐 Hi-Bari Spring Boot Server

## 📌 Overview

This directory contains the backend server for the Hi-Bari health and water tracking system.

The server exposes a REST API used by the Android application, handles business logic and validation, and communicates securely with Firebase Realtime Database through the Firebase Admin SDK.

## 🧠 Server Responsibilities

The Spring Boot server is responsible for:

- User registration and login
- User data management
- BMI data updates
- Daily water goal management
- Water intake updates
- Water history retrieval
- Request validation
- Firebase communication
- Transaction-safe database updates
- REST API response handling

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
    │   │       ├── EnvConfiguration/
    │   │       │   └── EnvConfig.java
    │   │       ├── model/
    │   │       │   └── User.java
    │   │       ├── service/
    │   │       │   └── FirebaseService.java
    │   │       └── web/
    │   │           └── UsersController.java
    │   │
    │   └── resources/
    │
    └── test/
        └── java/
            └── CapstoneTests/
                ├── FirebaseServiceIntegrationTest.java
                └── UsersControllerIntegrationTest.java
```

## 🧩 Main Components

### `Application.java`

The main Spring Boot application class used to start the server.

### `UsersController.java`

Defines the REST API endpoints used by the Android application.

The controller receives HTTP requests, validates the input, calls the service layer, and returns the appropriate HTTP response.

### `FirebaseService.java`

Handles communication with Firebase Realtime Database.

Main responsibilities include:

- Creating users
- Retrieving users
- Updating user information
- Managing daily water logs
- Updating water consumption
- Handling asynchronous Firebase operations
- Performing transaction-safe updates

### `User.java`

Represents the user data model used by the backend and Firebase.

### `EnvConfig.java`

Loads environment configuration required by the server.

Sensitive values are not stored in the repository.

## 🧠 Architecture

```text
Android Application
        ↓
HTTP REST Request
        ↓
UsersController
        ↓
FirebaseService
        ↓
Firebase Realtime Database
        ↓
HTTP Response
        ↓
Android Application
```

The controller layer handles HTTP communication.

The service layer contains the Firebase operations and backend logic.

Firebase Realtime Database stores user and water tracking data.

## 🔄 Water Update Flow

When the Android application sends a request to add water:

1. The request reaches `UsersController`.
2. The request data is validated.
3. The controller calls `FirebaseService`.
4. The service locates the user's daily water log.
5. The new drink amount is added.
6. The total daily amount is updated.
7. Firebase stores the updated list.
8. The server returns the result to the Android application.

```text
PATCH Request
      ↓
UsersController
      ↓
FirebaseService
      ↓
Firebase Transaction
      ↓
Updated Water Log
      ↓
HTTP Response
```

## ⚙️ Transaction-Safe Updates

Water consumption updates use transaction-based logic to prevent data loss when multiple requests are processed at nearly the same time.

```text
Without transaction:
Request A reads old value
Request B reads old value
One update may overwrite the other ❌

With transaction:
Firebase applies both updates safely ✅
```

This helps prevent race conditions and maintains database consistency.

## ☁️ Firebase Data Structure

Example:

```text
Users/
  userId/
    userName
    password
    age
    bmi
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

## 🧪 Backend Testing

The server includes integration tests for the service and controller layers.

Testing technologies:

- JUnit 5
- JUnit Jupiter
- Spring Boot Test
- TestRestTemplate
- Firebase integration testing
- Parallel test execution
- Asynchronous operation testing

### `FirebaseServiceIntegrationTest`

Tests the interaction between `FirebaseService` and Firebase Realtime Database.

The tests cover operations such as:

- Creating users
- Retrieving user data
- Updating user information
- Updating water consumption
- Retrieving water history
- Concurrent requests
- Asynchronous Firebase operations
- Transaction-safe updates

### `UsersControllerIntegrationTest`

Loads the full Spring Boot application and sends real HTTP requests to the server using `TestRestTemplate`.

The tests cover:

- REST endpoints
- Request bodies
- Response bodies
- HTTP status codes
- GET requests
- POST requests
- PUT requests
- PATCH requests
- DELETE requests
- HEAD requests
- Validation and error handling

## 🔐 Security

The server uses Firebase Admin SDK credentials for secure access to Firebase.

Sensitive files are intentionally excluded from the repository.

Examples:

```text
.env
application.properties
Firebase Admin SDK JSON file
```

These files must never be committed to GitHub.

The `.gitignore` file prevents sensitive configuration files from being tracked.

## ⚙️ Local Configuration

Before running the server, create the required local configuration files.

Example environment values may include:

```text
Firebase credentials path
Firebase database URL
Server configuration
```

Do not place real credentials directly inside source code.

Do not upload credentials, passwords, private keys, or Firebase service-account files to GitHub.

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

### Start from the terminal

From the `Spring Server` directory:

```bash
mvn spring-boot:run
```

To run the tests:

```bash
mvn test
```

## 🛠 Technologies

- Java
- Spring Boot 3
- Spring Web
- Maven
- REST API
- Firebase Admin SDK
- Firebase Realtime Database
- JUnit 5
- Spring Boot Test
- TestRestTemplate
- CompletableFuture

## 🔗 Related Project

The Android client is located in:

```text
../Hai-Bari android application/
```

The main repository documentation is located in:

```text
../README.md
```

## 👨‍💻 Author

Sharbel Zarzour
