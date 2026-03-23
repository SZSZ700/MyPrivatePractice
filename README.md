# 💧 Hi-Bari – Health & Water Tracking System

## 📌 Overview

Hi-Bari is a full-stack health tracking application designed to monitor daily water intake, calculate BMI, and manage user health data.

The system integrates a mobile client, backend services, and a cloud database to provide a real-time and scalable solution.

---

## 🧠 System Architecture

The system is based on a 3-tier architecture:

```
Android App → REST API (Spring Boot) → Firebase Realtime Database
```

### 📱 Android Client

* Java-based Android application
* Activities: Login, Signup, HomePage, WaterActivity, BmiActivity, WaterChartActivity, DailyWaterGoal
* Uses OkHttp for REST communication
* Stores session using SharedPreferences

---

### 🌐 Backend (Spring Boot)

* RESTful API
* Handles business logic and validation
* Uses Firebase Admin SDK
* Implements safe updates using transaction-like logic

---

### ☁️ Firebase Realtime Database

```
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

* Index 0 → total daily water
* Index 1..N → individual drink entries

---

## 🔄 Data Flow (Water Update)

1. User presses "Add Water"
2. Android sends PATCH request
3. Backend processes request
4. FirebaseService updates waterLog
5. Data stored in Firebase
6. Response returned to client

---

## ⚙️ Advanced Implementation (🔥 Important)

### 🔹 Transaction-Based Update (Critical Feature)

To avoid data overwrite and ensure consistency:

* The system updates water consumption using **atomic operations**
* Prevents race conditions when multiple updates occur simultaneously

👉 Example:

* Two requests arrive at the same time
* Without transaction → data loss ❌
* With transaction → safe update ✅

---

### 🔹 Dynamic Data Structure

* Water log is stored as a dynamic list
* No fixed limit on number of drinks
* Supports real-time growth

---

## 🔐 Security

* Uses `serviceAccountKey.json` for server authentication
* Key is NOT exposed in repository
* Backend handles all secure operations

---

## 🛠 Technologies Used

* Java
* Android SDK
* Spring Boot 3
* Firebase Realtime Database
* OkHttp
* MPAndroidChart

---

## 📊 Features

### 👤 User System

* Signup / Login
* Store user data

### 💧 Water Tracking

* Add water (150ml / 200ml / 1000ml)
* Track daily totals
* Store history

### 📈 Visualization

* Weekly chart
* Daily tracking

---

## 🚀 Future Improvements

* Smart hydration suggestions using Ai
* UI/UX improvements

---

## 👨‍💻 Authors

* Sharbel Zarzour

---

## 🎓 Academic Context

This project was developed as a final capstone project in Software Engineering studies.

---

## 💡 Key Strengths (For Evaluation)

* Full-stack architecture
* Cloud-based real-time database
* Secure backend communication
* Transaction-safe updates
* Scalable design
