#ifndef NURSE_H
#define UNTITLED1_NURSE_H

#include "..//Employees/Employee.h"

// 👩‍⚕️ Nurse inherits from Employee
// ReSharper disable once CppClassCanBeFinal
class Nurse : public Employee {
    // 👉 Pointer to nurse type (Practical / Certified)
    string* type;

public:
    // ========================================================
    // 🏗️ Constructors / Rule of Five
    // ========================================================

    // 👉 Default constructor
    Nurse();

    // 👉 Parameter constructor (name + type)
    Nurse(const string* n, const string* t);

    // 👉 Destructor
    ~Nurse() override;

    // 👉 Copy constructor
    Nurse(const Nurse& other);

    // 👉 Move constructor
    Nurse(Nurse&& other) noexcept;

    // 👉 Copy assignment
    Nurse& operator=(const Nurse& other);

    // 👉 Move assignment
    Nurse& operator=(Nurse&& other) noexcept;

    // ========================================================
    // ⚙️ Getters
    // ========================================================

    // 👉 Get nurse type (read-only pointer)
    const string* getType() const;

    // ========================================================
    // ✏️ Setters
    // ========================================================

    // 👉 Set nurse type (deep copy)
    void setType(const string* t);

    // ========================================================
    // 🧾 Utility
    // ========================================================

    // 👉 Convert nurse to string
    string toString() const override;

    // 👉 Print nurse details
    void print() const override;
};

#endif
