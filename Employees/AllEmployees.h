#ifndef UNTITLED1_ALLEMPLOYEES_H
#define UNTITLED1_ALLEMPLOYEES_H
#include "Employee.h"
#include "Supervisor.h"
#include <iostream>

// 🏢 Class storing all hospital employees (up to 200)
class AllEmployees {
    // 👉 Pointer to array of 200 Employee pointers
    Employee **allEmployees;

    // 👉 Pointer holding how many employees are currently stored
    int *current;

public:
    // 🏗️ Default constructor
    AllEmployees();

    // 💥 Destructor
    ~AllEmployees();

    // 📦 Copy constructor (deep copy)
    AllEmployees(const AllEmployees &other);

    // ✍️ Copy assignment
    AllEmployees& operator=(const AllEmployees &other);

    // 🚚 Move constructor
    AllEmployees(AllEmployees &&other) noexcept;

    // 🚚 Move assignment
    AllEmployees& operator=(AllEmployees &&other) noexcept;

    // 🧾 Return pointer to employee array (read-only)
    const Employee* const* getEmployeesArray() const;

    // ➕ Add employee to array (deep-store pointer)
    bool addEmployee(Employee* emp);

    // 🔢 Get current number of employees
    int getCurrentCount() const;

    // 📋 Print all employees
    void printAll() const;

    // 🧮 num of all supervisors
    int numSupervisors() const;

    // 👩‍⚕️ returns spacific nurse
    Nurse* getNewNurse(const string* type) const;
};

#endif //UNTITLED1_ALLEMPLOYEES_H
