#ifndef UNTITLED1_SUPERVISOR_H
#define UNTITLED1_SUPERVISOR_H
#include "Doctor.h"
#include "Nurse.h"
#include <iostream>
// 🧑‍⚕️👑 Supervisor = Doctor with a managed team
// ReSharper disable once CppClassCanBeFinal
class Supervisor : public Doctor {
    Employee** team;   // 👉 Pointer to array of 10 Employee* (team members)
    int* current;      // 👉 Pointer to number of employees currently in team

public:
    // ================================
    // 🏗️ Constructors / Rule of Five
    // ================================

    Supervisor();    // Default

    Supervisor(const string* name, const string* spec); // With details

    ~Supervisor() override; // Destructor

    Supervisor(const Supervisor& other);            // Copy ctor

    Supervisor& operator=(const Supervisor& other); // Copy assign

    Supervisor(Supervisor&& other) noexcept;        // Move ctor

    Supervisor& operator=(Supervisor&& other) noexcept; // Move assign

    // ================================
    // ⚙️ Team operations
    // ================================

    bool addToTeam(Employee* e);     // Add employee if room

    bool removeFromTeam(const int* num); // Remove by employee number

    int getTeamCount() const;        // Return how many currently

    // ================================
    // 🧾 Utility
    // ================================

    string toString() const override; // Convert to string

    void print() const override;      // Print details + team list
};

#endif
