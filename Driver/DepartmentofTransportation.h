#ifndef UNTITLED1_DEPARTMENTOFTRANSPORTATION_H
#define UNTITLED1_DEPARTMENTOFTRANSPORTATION_H

#include <string>      // 🧩 For std::string
#include "DriversDb.h" // 🧠 Include DriversDb dependency

// 🏢 Class that manages a DriversDb safely and elegantly
class DepartmentOfTransportation {
    // 🧠 Owned pointer to DriversDb
    DriversDb* driversDb;

public:
    // 🚀 Default constructor
    DepartmentOfTransportation() noexcept;

    // 💣 Destructor
    ~DepartmentOfTransportation();

    // 📋 Copy constructor
    DepartmentOfTransportation(const DepartmentOfTransportation& other);

    // 🧩 Copy assignment
    DepartmentOfTransportation& operator=(const DepartmentOfTransportation& other);

    // ⚡ Move constructor
    DepartmentOfTransportation(DepartmentOfTransportation&& other) noexcept;

    // ⚡ Move assignment
    DepartmentOfTransportation& operator=(DepartmentOfTransportation&& other) noexcept;

    // 🔍 Const getter
    [[nodiscard]] const DriversDb* getDriversDb() const noexcept;

    // ✏️ Mutable getter
    [[nodiscard]] DriversDb* getDriversDb() noexcept;

    // 🪄 Deep-copy setter
    void setDriversDb(const DriversDb& db);

    // ⚙️ Move-based setter
    void setDriversDb(DriversDb&& db) noexcept;

    // 🧩 Raw pointer setter
    void setDriversDb(DriversDb* ptr) noexcept;

    // 🧾 String summary
    [[nodiscard]] std::string toString() const;
};

#endif // UNTITLED1_DEPARTMENTOFTRANSPORTATION_H

