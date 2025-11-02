#ifndef UNTITLED1_DOCTOR_H
#define UNTITLED1_DOCTOR_H

#include "..//Employees/Employee.h"

// 🧑‍⚕️ Doctor class inheriting from Employee
// ReSharper disable once CppClassCanBeFinal
class Doctor : public Employee {
    // 👉 Pointer to doctor specialization (Cardiology, Surgery, etc.)
    string* specialization;

public:
    // =======================================================
    // 🏗️ Constructors (Rule of Five)
    // =======================================================

    // 👉 Default constructor
    Doctor();

    // 👉 Parameter constructor (name + specialization)
    Doctor(const string* n, const string* s);

    // 👉 Destructor
    ~Doctor() override;

    // 👉 Copy constructor
    Doctor(const Doctor& other);

    // 👉 Move constructor
    Doctor(Doctor&& other) noexcept;

    // 👉 Copy assignment
    Doctor& operator=(const Doctor& other);

    // 👉 Move assignment
    Doctor& operator=(Doctor&& other) noexcept;

    // =======================================================
    // ⚙️ Getters
    // =======================================================

    // 👉 Get specialization (read-only pointer)
    const string* getSpecialization() const;

    // =======================================================
    // ✏️ Setters
    // =======================================================

    // 👉 Set specialization (deep copy)
    void setSpecialization(const string* s);

    // =======================================================
    // 🧾 Utility
    // =======================================================

    // 👉 Convert doctor to string
    string toString() const override;

    // 👉 Print doctor details
    void print() const override;
};

#endif
