# 💧 Hi-Bari – Health & Water Tracking System

## 📌 Overview

Hi-Bari is a full-stack health tracking application designed to monitor daily water intake, calculate BMI, and manage user health data.

The system integrates a mobile Android client, a Spring Boot backend, and Firebase Realtime Database to provide a real-time and scalable solution.

## 🎥 Application Demo

[![Watch the Hi-Bari application demo](https://img.youtube.com/vi/3k6u2FfhNGw/hqdefault.jpg)](https://youtube.com/shorts/3k6u2FfhNGw)

Click the image above to watch a short demonstration of the Hi-Bari Android application.

## 🗂 Repository Structure

The repository is organized into two main projects:

```text
MyCapstone/
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
```

### 📱 Hai-Bari Android Application

The `Hai-Bari android application` directory contains the Android client.

Main responsibilities:

- User interface
- Signup and login
- Water intake tracking
- BMI calculation
- Daily water goal management
- Weekly chart visualization
- Communication with the Spring Boot server

### 🌐 Spring Server

The `Spring Server` directory contains the Spring Boot backend.

Main responsibilities:

- REST API endpoints
- Business logic
- Input validation
- Firebase communication
- User data management
- Water log updates
- Transaction-safe database operations

## 🧠 System Architecture

The system is based on a three-tier architecture:

```text
Android Application
        ↓
Spring Boot REST API
        ↓
Firebase Realtime Database
```

The Android application communicates with the Spring Boot server through HTTP requests.

The Spring Boot server handles business logic, validation, secure operations, and communication with Firebase Realtime Database.

## 📱 Android Client

The Android application is written in Java and built with the Android SDK.

Main technologies and components:

- Java
- Android SDK
- Gradle
- OkHttp
- SharedPreferences
- MPAndroidChart

Main activities:

- `LoginActivity`
- `Signup`
- `HomePage`
- `WaterActivity`
- `BMIActivity`
- `WaterChartActivity`
- `DailyWaterGoal`

The Android client is responsible for displaying the user interface, collecting user input, sending requests to the backend, and displaying the returned data.

## 🌐 Backend – Spring Boot

The backend is implemented using Spring Boot 3.

Main responsibilities:

- Exposing RESTful API endpoints
- Handling business logic
- Validating incoming requests
- Managing user information
- Processing water tracking updates
- Communicating securely with Firebase
- Preventing data loss during concurrent updates

Main technologies:

- Java
- Spring Boot 3
- Maven
- REST API
- Firebase Admin SDK

## ☁️ Firebase Realtime Database

The system stores user information and water tracking data in Firebase Realtime Database.

Example database structure:

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

Water log structure:

```text
Index 0   → Total daily water intake
Index 1-N → Individual drink entries
```

Example:

```text
[1850, 150, 200, 500, 1000]
```

Meaning:

```text
1850 → Total water consumed
150  → First drink
200  → Second drink
500  → Third drink
1000 → Fourth drink
```

## 🔄 Data Flow – Water Update

When the user adds water:

1. The user presses the `Add Water` button.
2. The Android application sends a PATCH request.
3. The Spring Boot backend receives the request.
4. The backend validates the request.
5. `FirebaseService` updates the user water log.
6. The updated data is stored in Firebase.
7. The backend returns a response to the Android application.
8. The Android interface displays the updated daily total.

```text
User Action
    ↓
Android Application
    ↓
PATCH Request
    ↓
Spring Boot REST API
    ↓
FirebaseService
    ↓
Firebase Realtime Database
    ↓
Response to Android
```

## ⚙️ Advanced Implementation

### 🔹 Transaction-Based Water Update

To prevent data overwrite and maintain consistency, water consumption updates are performed using atomic transaction-based operations.

This is important when multiple requests arrive at nearly the same time.

```text
Two requests arrive at the same time

Without transaction → one update may overwrite the other ❌
With transaction    → both updates are stored safely ✅
```

This prevents race conditions and possible data loss.

### 🔹 Dynamic Water Log

The water log is stored as a dynamic list.

Benefits:

- No fixed limit on the number of drinks
- Supports continuous daily updates
- Stores both the daily total and individual drink entries
- Allows detailed water consumption history
- Supports real-time growth

## 🧪 Software Testing

The project includes automated tests for both the Android application and the Spring Boot backend.

### 📱 Android Testing

The Android application is tested using:

- JUnit 4
- Robolectric
- Mockito
- OkHttp MockWebServer
- AndroidX Test

Robolectric is used to run Android component tests directly on the JVM without requiring a physical device or emulator.

Mockito is used to create mock objects and isolate dependencies during unit testing.

MockWebServer is used to simulate backend HTTP responses and inspect outgoing requests from the Android application.

The Android tests cover areas such as:

- Activity behavior
- User interface logic
- Login and signup flows
- BMI calculations
- Daily water goal management
- Water intake updates
- Chart-related behavior
- REST communication
- HTTP request and response handling
- Toast messages and Android runtime behavior

### 🌐 Spring Boot Testing

The Spring Boot backend is tested using:

- JUnit 5 / JUnit Jupiter
- Spring Boot Test
- TestRestTemplate
- Integration testing
- Parallel test execution
- Asynchronous operation testing

`@SpringBootTest` is used to load the full Spring application context.

`TestRestTemplate` is used to send real HTTP requests to the running Spring Boot server and verify status codes, response bodies, and REST endpoint behavior.

The backend tests also interact with `FirebaseService` to prepare, update, retrieve, and verify data stored in Firebase.

The Spring tests cover areas such as:

- REST controller endpoints
- User creation and retrieval
- Login behavior
- HTTP GET, POST, PUT, PATCH, DELETE, and HEAD requests
- Response status validation
- Firebase service operations
- Water intake updates
- Concurrent requests
- Transaction-safe updates
- Asynchronous operations
- Error handling

## 🔐 Security

The backend uses Firebase Admin SDK for server authentication.

Sensitive files and credentials are not exposed in the repository.

Security measures:

- Firebase credentials are excluded using `.gitignore`
- The Android application does not contain Firebase Admin credentials
- Secure database operations are handled by the backend
- Local configuration files are not uploaded
- Signing keys are excluded from the repository

Sensitive files excluded from the repository include:

```text
.env
application.properties
Firebase Admin SDK JSON file
local.properties
*.jks
*.keystore
```

## 🛠 Technologies Used

### Android Client

- Java
- Android SDK
- Gradle
- OkHttp
- SharedPreferences
- MPAndroidChart

### Backend

- Java
- Spring Boot 3
- Maven
- REST API
- Firebase Admin SDK

### Database

- Firebase Realtime Database

### Testing Technologies

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
- Parallel and asynchronous testing

### Development Tools

- Android Studio
- IntelliJ IDEA
- Git
- GitHub

## 📊 Features

### 👤 User System

- User signup
- User login
- User data storage
- Local session management

### 💧 Water Tracking

- Add 150 ml
- Add 200 ml
- Add 1000 ml
- Track daily water total
- Store individual drink entries
- Store daily water history
- Set a daily water goal

### ⚖️ BMI Tracking

- Calculate BMI
- Store BMI data
- Display health-related information

### 📈 Data Visualization

- Weekly water chart
- Daily water tracking
- Historical water consumption
- Visual progress display

## ▶️ Running the Project

### Android Application

Open this directory in Android Studio:

```text
Hai-Bari android application
```

The Android project includes the Gradle Wrapper, so the required Gradle version can be used automatically.

### Spring Boot Server

Open this directory in IntelliJ IDEA:

```text
Spring Server
```

The backend uses Maven and can be started through the main Spring Boot application class:

```text
Spring Server/
└── src/main/java/org/example/CapstoneProject/Application.java
```

Sensitive Firebase configuration files are required locally but are not included in the repository.

## 🚀 Future Improvements

- Smart hydration suggestions using AI
- Improved UI and UX
- Password encryption
- Token-based authentication
- More detailed health statistics
- Notifications based on hydration progress
- Cloud deployment for the Spring Boot server
- Additional charts and reports

## 👨‍💻 Author

Sharbel Zarzour

## 🎓 Academic Context

This project was developed as a final capstone project in Software Engineering studies.

## 💡 Key Strengths

- Full-stack architecture
- Android mobile client
- Spring Boot REST API
- Cloud-based real-time database
- Secure backend communication
- Automated Android and backend testing
- JVM-based Android component testing with Robolectric
- Mocked HTTP testing with MockWebServer
- Full Spring integration testing with TestRestTemplate
- Concurrent and asynchronous operation testing
- Transaction-safe updates
- Dynamic data structure
- Separation between client, server, and database
- Organized multi-project repository
- Scalable system design
