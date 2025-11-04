#include "AllEmployees.h"
#include "Doctor.h"
#include "Nurse.h"
#include <iostream>
using namespace std;

// 🏗️ Default constructor
AllEmployees::AllEmployees() {
    // 🧠 Allocate space for 200 employees (pointers)
    this->allEmployees = new Employee*[200];

    // 🧹 Initialize all slots to null
    for (int i = 0; i < 200; i++)
        this->allEmployees[i] = nullptr;

    // 🔢 Start count at 0
    this->current = new int(0);
}

// 💥 Destructor
AllEmployees::~AllEmployees() {
    // 🧹 Delete all employee objects stored
    for (int i = 0; i < *this->current; i++)
        delete this->allEmployees[i];

    // 🧹 Free array memory
    delete[] this->allEmployees;

    // ❌ Free current counter
    delete this->current;
}

// 📦 Copy constructor
AllEmployees::AllEmployees(const AllEmployees &other) {
    // 🧠 Allocate new 200-pointer array
    this->allEmployees = new Employee*[200];

    // 🔢 Deep copy current count
    this->current = new int(*other.current);

    // 🔁 Deep copy each employee (by dynamic cast)
    for (int i = 0; i < *this->current; i++) {
        if (const auto* d = dynamic_cast<Doctor*>(other.allEmployees[i]))
            this->allEmployees[i] = new Doctor(*d);
        else if (const auto* n = dynamic_cast<Nurse*>(other.allEmployees[i]))
            this->allEmployees[i] = new Nurse(*n);
        else
            this->allEmployees[i] = nullptr;
    }

    // 🧹 Set remaining slots to null
    for (int i = *this->current; i < 200; i++) { this->allEmployees[i] = nullptr; }
}

// ✍️ Copy assignment
AllEmployees& AllEmployees::operator=(const AllEmployees &other) {
    // 🔒 Self-assignment check
    if (this == &other) return *this;

    // delete old data //
    // 🧹 Delete existing employees
    for (int i = 0; i < *this->current; i++) { delete this->allEmployees[i]; }
    // ❌ Delete old array + counter
    delete[] this->allEmployees;
    delete this->current;


    // copy new data //
    this->allEmployees = new Employee*[200]; // 🧠 Allocate new memory
    this->current = new int(*other.current); // 🔢 Copy count

    // 🔁 Copy employee objects (deep)
    for (int i = 0; i < *this->current; i++) {
        if (const auto* d = dynamic_cast<Doctor*>(other.allEmployees[i])) {
            this->allEmployees[i] = new Doctor(*d);
        }

        else if (const auto* n = dynamic_cast<Nurse*>(other.allEmployees[i])) {
            this->allEmployees[i] = new Nurse(*n);
        }

        else { this->allEmployees[i] = nullptr; }
    }

    // 🧹 Fill remain with null
    for (int i = *this->current; i < 200; i++) { this->allEmployees[i] = nullptr; }

    // ✅ Return self
    return *this;
}

// 🚚 Move constructor
AllEmployees::AllEmployees(AllEmployees &&other) noexcept {
    // 🏃‍♂️ Take ownership of pointers
    this->allEmployees = other.allEmployees;
    this->current = other.current;

    // 🧼 Null source
    other.allEmployees = nullptr;
    other.current = nullptr;
}

// 🚚 Move assignment
AllEmployees& AllEmployees::operator=(AllEmployees &&other) noexcept {
    // 🔒 Self-assign guard
    if (this == &other) return *this;

    // 🧹 delete old data
    for (int i = 0; i < *this->current; i++) { delete this->allEmployees[i]; }
    delete[] this->allEmployees;
    delete this->current;

    // 🏃‍♂️ Steal resources
    this->allEmployees = other.allEmployees;
    this->current = other.current;

    // 🧼 Null source
    other.allEmployees = nullptr;
    other.current = nullptr;

    return *this;
}

// 🧾 Getter for employee array (read only)
const Employee* const* AllEmployees::getEmployeesArray() const { return this->allEmployees; }

// ➕ Add employee to array
// ReSharper disable once CppMemberFunctionMayBeConst
bool AllEmployees::addEmployee(Employee* emp) {
    // 🚫 If full or null input → fail
    if (*this->current >= 200 || emp == nullptr)
        return false;

    // ✅ Add pointer to array
    this->allEmployees[*this->current] = emp;

    // 🔢 Increase count
    (*this->current)++;

    return true;
}

// 🔢 Return number of employees
int AllEmployees::getCurrentCount() const { return *this->current; }

// 🖨️ Print all employees
void AllEmployees::printAll() const {
    // 🖨️ Loop & print each object
    for (int i = 0; i < *this->current; i++) { this->allEmployees[i]->print(); }
}
