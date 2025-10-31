#include "Reminder.h"
#include <iostream>

// =====================
// 🏗️ Default constructor
// =====================
Reminder::Reminder() {
    this->cust = nullptr;   // No name yet
    this->tel = nullptr;    // No phone yet
    this->inst = nullptr;   // No institution yet
    this->date = nullptr;   // No date yet
    this->hour = nullptr;   // No hour yet
    this->status = nullptr; // No status yet
}

// =====================
// 🧩 Parameterized constructor
// =====================
Reminder::Reminder(const string* c, const string* t, const string* i, const string* d, const int *h) {
    this->cust = new string(*c);   // Allocate and copy name
    this->tel = new string(*t);    // Allocate and copy phone
    this->inst = new string(*i);   // Allocate and copy institution
    this->date = new string(*d);   // Allocate and copy date
    this->hour = new int(*h);      // Allocate and copy hour
    this->status = new int(0);    // Default status: no response
}

// =====================
// 🧹 Destructor
// =====================
Reminder::~Reminder() {
    delete this->cust;   // Free name
    delete this->tel;    // Free phone
    delete this->inst;   // Free institution
    delete this->date;   // Free date
    delete this->hour;   // Free hour
    delete this->status; // Free status
}

// =====================
// 📋 Copy constructor
// =====================
Reminder::Reminder(const Reminder& other) {
    this->cust = other.cust ? new string(*other.cust) : nullptr;     // Copy name

    this->tel = other.tel ? new string(*other.tel) : nullptr;        // Copy phone

    this->inst = other.inst ? new string(*other.inst) : nullptr;     // Copy institution

    // ReSharper disable once CppDFAMemoryLeak
    this->date = other.date ? new string(*other.date) : nullptr;     // Copy date

    // ReSharper disable once CppDFAMemoryLeak
    this->hour = other.hour ? new int(*other.hour) : nullptr;        // Copy hour

    // ReSharper disable once CppDFAMemoryLeak
    this->status = other.status ? new int(*other.status) : nullptr;  // Copy status
}

// =====================
// 🧳 Move constructor
// =====================
Reminder::Reminder(Reminder&& other) noexcept {
    this->cust = other.cust;     // Transfer ownership
    this->tel = other.tel;
    this->inst = other.inst;
    this->date = other.date;
    this->hour = other.hour;
    this->status = other.status;

    // Nullify source
    other.cust = nullptr;
    other.tel = nullptr;
    other.inst = nullptr;
    other.date = nullptr;
    other.hour = nullptr;
    other.status = nullptr;
}

// =====================
// 📋 Copy assignment operator
// =====================
Reminder& Reminder::operator=(const Reminder& other) {
    if (this != &other) {           // Prevent self-assignment
        delete this->cust;                // Delete old name
        delete this->tel;                 // Delete old phone
        delete this->inst;                // Delete old institution
        delete this->date;                // Delete old date
        delete this->hour;                // Delete old hour
        delete this->status;              // Delete old status

        this->cust = other.cust ? new string(*other.cust) : nullptr;    // Deep copy new name
        this->tel = other.tel ? new string(*other.tel) : nullptr;       // Deep copy new phone
        this->inst = other.inst ? new string(*other.inst) : nullptr;    // Deep copy new institution
        this->date = other.date ? new string(*other.date) : nullptr;    // Deep copy new date
        this->hour = other.hour ? new int(*other.hour) : nullptr;       // Deep copy new hour
        this->status = other.status ? new int(*other.status) : nullptr; // Deep copy new status
    }

    return *this;
}

// =====================
// 🧳 Move assignment operator
// =====================
Reminder& Reminder::operator=(Reminder&& other) noexcept {
    if (this != &other) {           // Prevent self-move
        delete this->cust;                // Free old name
        delete this->tel;                 // Free old phone
        delete this->inst;                // Free old institution
        delete this->date;                // Free old date
        delete this->hour;                // Free old hour
        delete this->status;              // Free old status

        this->cust = other.cust;          // Steal name pointer
        this->tel = other.tel;            // Steal phone pointer
        this->inst = other.inst;          // Steal institution pointer
        this->date = other.date;          // Steal date pointer
        this->hour = other.hour;          // Steal hour pointer
        this->status = other.status;      // Steal status pointer

        other.cust = nullptr;       // Nullify moved source
        other.tel = nullptr;
        other.inst = nullptr;
        other.date = nullptr;
        other.hour = nullptr;
        other.status = nullptr;
    }
    return *this;
}

// =====================
// ⚙️ Getters (read-only, const pointers)
// =====================
const string* Reminder::getCust() const { return this->cust; }   // Return name pointer (const)
const string* Reminder::getTel() const { return this->tel; }     // Return phone pointer (const)
const string* Reminder::getInst() const { return this->inst; }   // Return institution pointer (const)
const string* Reminder::getDate() const { return this->date; }   // Return date pointer (const)
const int* Reminder::getHour() const { return this->hour; }      // Return hour pointer (const)
const int* Reminder::getStatus() const { return this->status; }  // Return status pointer (const)

// =====================
// ⚙️ Setters
// =====================
void Reminder::setCust(const string* c) {
    delete this->cust;                           // Delete old value
    this->cust = c ? new string(*c) : nullptr;   // Deep copy new value
}

void Reminder::setTel(const string* t) {
    delete this->tel;
    this->tel = t ? new string(*t) : nullptr;
}

void Reminder::setInst(const string* i) {
    delete this->inst;
    this->inst = i ? new string(*i) : nullptr;
}

void Reminder::setDate(const string* d) {
    delete this->date;
    this->date = d ? new string(*d) : nullptr;
}

void Reminder::setHour(const int* h) {
    delete this->hour;
    this->hour = h ? new int(*h) : nullptr;
}

void Reminder::setStatus(const int* s) {
    delete this->status;
    this->status = s ? new int(*s) : nullptr;
}

// =====================
// 🧾 Print all reminder details
// =====================
void Reminder::print() const {
    cout << "Reminder Info:\n";
    cout << "Customer: " << (this->cust ? *this->cust : "N/A") << endl;
    cout << "Phone: " << (this->tel ? *this->tel : "N/A") << endl;
    cout << "Institution: " << (this->inst ? *this->inst : "N/A") << endl;
    cout << "Date: " << (this->date ? *this->date : "N/A") << endl;
    cout << "Hour: " << (this->hour ? to_string(*this->hour) : "N/A") << endl;
    cout << "Status: " << (this->status ? to_string(*this->status) : "N/A") << endl;
    cout << " "<<endl;
}

