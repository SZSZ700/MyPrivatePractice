#ifndef UNTITLED1_EMPLOYEE_H
#define UNTITLED1_EMPLOYEE_H

#include <string>
#include <iostream>
using namespace std;

// 🧑‍⚕️ Base class for all hospital employees
class Employee {
protected:
    // 👉 Pointer to employee name
    string* name;
    // 👉 Pointer to employee id number
    int* num;

public:
    // 👉 Static counter to auto-assign employee IDs
    static int counter;

    // ========================================================
    // 🏗️ Constructors / Rule of Five
    // ========================================================

    // 👉 Default constructor
    Employee();

    // 👉 Parameter constructor (copy value from given name)
    explicit Employee(const string* n);

    // 👉 Virtual destructor (needed for polymorphism)
    virtual ~Employee();

    // 👉 Deep copy constructor
    Employee(const Employee& other);

    // 👉 Move constructor (steals resources)
    Employee(Employee&& other) noexcept;

    // 👉 Copy assignment operator
    Employee& operator=(const Employee& other);

    // 👉 Move assignment operator
    Employee& operator=(Employee&& other) noexcept;

    // ========================================================
    // 🎯 Getters — return const pointer to prevent modification
    // ========================================================

    // 👉 Get employee name (read-only pointer)
    const string* getName() const;

    // 👉 Get employee number (read-only pointer)
    const int* getNum() const;

    // ========================================================
    // ✏️ Setters — deep copy incoming data
    // ========================================================

    // 👉 Set employee name
    void setName(const string* n);

    // ========================================================
    // 🧾 Utility
    // ========================================================

    // 👉 Convert object to string
    virtual string toString() const;

    // 👉 Print employee info
    virtual void print() const;
};

#endif
