#ifndef UNTITLED1_WEEKLYREMINDER_H
#define UNTITLED1_WEEKLYREMINDER_H

#include "DailyReminder.h"
using namespace std;

// 📅 Class representing weekly reminder system (7 days: 0 = unanswered)
class WeeklyReminder {
    DailyReminder** arr; // 📦 Pointer to array of DailyReminder pointers
    int* days;           // 🔢 Number of days (always 7 in this system)

public:
    // =========================
    // 🧱 Constructors / Destructor
    // =========================

    // 🏗️ Default constructor
    WeeklyReminder();

    // 🧹 Destructor
    ~WeeklyReminder();

    // =========================
    // 🧬 Rule of Five
    // =========================
    WeeklyReminder(const WeeklyReminder& other);             // 📋 Copy constructor
    WeeklyReminder(WeeklyReminder&& other) noexcept;         // 🧳 Move constructor
    WeeklyReminder& operator=(const WeeklyReminder& other);  // 📋 Copy assignment
    WeeklyReminder& operator=(WeeklyReminder&& other) noexcept; // 🧳 Move assignment

    // =========================
    // ⚙️ Getters / Setters
    // =========================
    const DailyReminder* getDay(int index) const;  // 🔍 Get daily reminder by index (read-only)
    void setDay(int index, DailyReminder* dr) const;     // 🧩 Replace specific day reminder
    int getDaysCount() const;                            // 🔢 Return number of days (always 7)

    // =========================
    // 🧾 Utility
    // =========================
    void print() const; // 🖨️ Print all reminders for the week

    // =========================
    // ➕ Add a new reminder to a specific day
    // =========================
    void addReminder(const string* cust, const string* tel, const string* inst,
                     const string* date, const int* hour, int dayReminder) const;

    // =========================
    // 🔄 Update reminder status or move unanswered to day 0
    // =========================
    void updateReminder(const string* cust, const string* inst,
                        int dayReminder, int answer) const;

    // =========================
    // 🏥 Print all cancelled appointments per institute
    // =========================
    void printCancelledAppointments() const;
};

#endif // UNTITLED1_WEEKLYREMINDER_H
