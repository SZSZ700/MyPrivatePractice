# 💧 Hi-Bari – Health & Water Tracking System

## 📌 Overview

Hi-Bari is a full-stack health tracking application designed to monitor daily water intake, calculate BMI, and manage user health data.

The system integrates a mobile client, backend services, and a cloud database to provide a real-time and scalable solution.

---

## 🎥 Application Demo

[![Watch the Hi-Bari application demo](https://img.youtube.com/vi/3k6u2FfhNGw/hqdefault.jpg)](https://youtube.com/shorts/3k6u2FfhNGw)

Click the image above to watch a short demonstration of the Hi-Bari Android application.

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
```

### 📱 Hai-Bari Android Application

Contains the Android client, user interface, water tracking features, BMI calculation, charts, session management, and communication with the backend.

### 🌐 Spring Server

Contains the Spring Boot REST API, business logic, Firebase communication, validation, and transaction-safe database operations.

---

## 🧠 System Architecture

The system is based on a three-tier architecture:

```text
Android App → REST API (Spring Boot) → Firebase Realtime Database
```

### 📱 Android Client

- Java-based Android application
- Activities: Login, Signup, HomePage, WaterActivity, BMIActivity, WaterChartActivity, and DailyWaterGoal
- Uses OkHttp for REST communication
- Stores session data using SharedPreferences
- Uses MPAndroidChart for data visualization

### 🌐 Backend – Spring Boot

- RESTful API
- Handles business logic and validation
- Uses Firebase Admin SDK
- Manages user and water-tracking data
- Implements transaction-safe updates

### ☁️ Firebase Realtime Database

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

- Index 0 → total daily water
- Index 1..N → individual drink entries

---

## 🔄 Data Flow – Water Update

1. The user presses the `Add Water` button.
2. The Android application sends a PATCH request.
3. The Spring Boot backend processes and validates the request.
4. `FirebaseService` updates the water log.
5. The updated data is stored in Firebase.
6. The server returns a response to the Android application.

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

---

## ⚙️ Advanced Implementation

### 🔹 Transaction-Based Updates

To avoid data overwrite and maintain consistency:

- Water consumption is updated using atomic transaction-based operations.
- Concurrent requests are handled safely.
- Race conditions and possible data loss are prevented.

Example:

```text
Two requests arrive at the same time

Without transaction → one update may overwrite the other ❌
With transaction    → both updates are stored safely ✅
```

### 🔹 Dynamic Data Structure

- The water log is stored as a dynamic list.
- There is no fixed limit on the number of drinks.
- The structure supports continuous daily growth.
- Both the total amount and individual drink entries are stored.

---

## 🧪 Software Testing

The project includes automated tests for both the Android application and the Spring Boot backend.

### 📱 Android Testing

Testing technologies:

- JUnit 4
- Robolectric
- Mockito
- OkHttp MockWebServer
- AndroidX Test

The Android tests cover:

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

Testing technologies:

- JUnit 5 / JUnit Jupiter
- Spring Boot Test
- TestRestTemplate
- Firebase integration testing
- Parallel test execution
- Asynchronous operation testing

The backend tests cover:

- REST controller endpoints
- User creation and retrieval
- Login behavior
- HTTP GET, POST, PUT, PATCH, DELETE, and HEAD requests
- HTTP status codes and response bodies
- Firebase service operations
- Water intake updates
- Concurrent requests
- Transaction-safe updates
- Asynchronous operations
- Error handling

---

## 🔐 Security

- The backend uses Firebase Admin SDK for server authentication.
- Firebase credentials are not exposed in the repository.
- Sensitive configuration files are excluded using `.gitignore`.
- The Android application does not contain Firebase Admin credentials.
- Secure database operations are handled by the backend.

Sensitive files excluded from the repository include:

```text
.env
application.properties
Firebase Admin SDK JSON file
local.properties
*.jks
*.keystore
```

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
- Maven
- REST API
- Firebase Admin SDK

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
- Parallel and asynchronous testing

### Development Tools

- Android Studio
- IntelliJ IDEA
- Git
- GitHub

---

## 📊 Features

### 👤 User System

- Signup and login
- User data storage
- Local session management

### 💧 Water Tracking

- Add 150 ml
- Add 200 ml
- Add 1000 ml
- Track daily totals
- Store individual drink entries
- Store daily history
- Configure a daily water goal

### ⚖️ BMI Tracking

- Calculate BMI
- Store BMI data
- Display health-related information

### 📈 Visualization

- Weekly water chart
- Daily water tracking
- Historical water consumption
- Daily goal progress

---

## ▶️ Running the Project

### Android Application

Open this directory in Android Studio:

```text
Hai-Bari android application
```

Allow Gradle to synchronize, make sure the Spring Boot server is running, select an emulator or physical device, and run the application.

### Spring Boot Server

Open this directory in IntelliJ IDEA:

```text
Spring Server
```

Run the main application class:

```text
Spring Server/
└── src/main/java/org/example/CapstoneProject/Application.java
```

Or run from the terminal:

```bash
mvn spring-boot:run
```

Sensitive Firebase configuration files are required locally but are not included in the repository.

---

## 🚀 Future Improvements

- Smart hydration suggestions using AI
- Improved UI and UX
- Password encryption
- Token-based authentication
- More detailed health statistics
- Improved notification scheduling
- Cloud deployment for the Spring Boot server
- Additional charts and reports
- Offline data support
- Expanded automated test coverage

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
- Cloud-based real-time database
- Secure backend communication
- Automated Android and backend testing
- Robolectric-based JVM testing
- Mocked HTTP testing with MockWebServer
- Spring integration testing with TestRestTemplate
- Concurrent and asynchronous operation testing
- Transaction-safe database updates
- Dynamic water-log data structure
- Separation between client, server, and database
- Organized multi-project repository
- Scalable system design
