#ifndef UNTITLED1_REMINDER_H
#define UNTITLED1_REMINDER_H

#include <string>
using namespace std;

// 🏥 Class representing a medical appointment reminder
class Reminder {
    string* cust;   // 👤 Customer name
    string* tel;    // ☎️ Customer phone
    string* inst;   // 🏢 Institution name
    string* date;   // 📅 Appointment date
    int* hour;      // ⏰ Appointment hour
    int* status;    // 🔢 Status (-2 canceled, -1 approved, 0 no response)

public:
    // =====================
    // 🧱 Constructors
    // =====================

    // Default constructor
    Reminder();

    // Parameterized constructor
    Reminder(const string *c, const string *t, const string *i, const string *d, const int *h);

    // Destructor
    ~Reminder();

    // =====================
    // 🧬 Rule of Five
    // =====================
    Reminder(const Reminder& other);              // Copy constructor
    Reminder(Reminder&& other) noexcept;          // Move constructor
    Reminder& operator=(const Reminder& other);   // Copy assignment
    Reminder& operator=(Reminder&& other) noexcept; // Move assignment

    // =====================
    // ⚙️ Getters
    // =====================
    const string* getCust() const;   // Get customer name (read-only)
    const string* getTel() const;    // Get phone (read-only)
    const string* getInst() const;   // Get institution (read-only)
    const string* getDate() const;   // Get date (read-only)
    const int* getHour() const;      // Get hour (read-only)
    const int* getStatus() const;    // Get status (read-only)

    // =====================
    // ⚙️ Setters
    // =====================
    void setCust(const string *c);   // Set customer name
    void setTel(const string *t);    // Set phone
    void setInst(const string *i);   // Set institution
    void setDate(const string *d);   // Set date
    void setHour(const int *h);      // Set hour
    void setStatus(const int *s);    // Set status

    // =====================
    // 🧾 Utility
    // =====================
    void print() const;              // Print reminder details
};

#endif // UNTITLED1_REMINDER_H
