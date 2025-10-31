#ifndef UNTITLED1_DAILYREMINDER_H
#define UNTITLED1_DAILYREMINDER_H

#include "../Node/Node.h"
#include "Reminder.h"
using namespace std;

// 📅 Class representing a list of reminders for a single day
class DailyReminder {
    Node<Reminder*>* chain;  // 🧱 Head of the linked list of reminders
    int* count;              // 🔢 Number of reminders (stored as pointer)

public:
    // =========================
    // 🧱 Constructors / Destructor
    // =========================

    // 🏗️ Default constructor
    DailyReminder();

    // 🧹 Destructor
    ~DailyReminder();

    // =========================
    // 🧬 Rule of Five
    // =========================
    DailyReminder(const DailyReminder& other);             // 📋 Copy constructor
    DailyReminder(DailyReminder&& other) noexcept;         // 🧳 Move constructor
    DailyReminder& operator=(const DailyReminder& other);  // 📋 Copy assignment
    DailyReminder& operator=(DailyReminder&& other) noexcept; // 🧳 Move assignment

    // =========================
    // ⚙️ Getters
    // =========================
    Node<Reminder *> *getChain() const;  // 🔗 Get pointer to head (read-only)
    const int* getCount() const;              // 🔢 Get pointer to count (read-only)

    // =========================
    // ⚙️ Setters
    // =========================
    void setChain(Node<Reminder*>* ch);       // 🧩 Set new reminder list (takes ownership)
    void setCount(const int* c);              // 🔢 Set new count value (deep copy)

    // =========================
    // 🧾 Utility
    // =========================
    void print() const;                       // 🖨️ Print all reminders for this day

    // =========================
    // ➕ Add new reminder
    // =========================
    void addReminder(Reminder* r);

    // =========================
    // ❌ Remove reminder by name
    // =========================
    void removeReminderByName(const string* name);

    // all the customers from some same institution
    const Node<Reminder*>* getCustomersFromInstitution(const string *inst) const;
};

#endif // UNTITLED1_DAILYREMINDER_H
