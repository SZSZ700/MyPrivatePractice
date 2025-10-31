#include "DepartmentOfTransportation.h" // 🧠 Include class header

#include <iostream>
#include <new>                           // 🧩 For std::bad_alloc
#include <utility>

// 🚀 Default constructor
DepartmentOfTransportation::DepartmentOfTransportation() noexcept : driversDb(nullptr) {
    // 🧱 Try to allocate empty DriversDb
    try { this->driversDb = new DriversDb(); }

    // ⚠️ Handle allocation failure
    catch (const std::bad_alloc& e) { std::cerr << "Allocation failed: " << e.what() << std::endl; }
}

// 💣 Destructor
// 🧹 Safely delete the database
DepartmentOfTransportation::~DepartmentOfTransportation() { delete this->driversDb; }

// 📋 Copy constructor
DepartmentOfTransportation::DepartmentOfTransportation(const DepartmentOfTransportation& other) {
    // 🧠 Try to deep-copy DriversDb
    try { this->driversDb = other.driversDb ? new DriversDb(*other.driversDb) : nullptr; }

    // ⚠️ Handle bad_alloc
    catch (const std::bad_alloc& e) {
        std::cerr << "Copy failed: " << e.what() << std::endl;
        this->driversDb = nullptr;
    }
}

// 🧩 Copy assignment
DepartmentOfTransportation& DepartmentOfTransportation::operator=(const DepartmentOfTransportation& other) {
    if (this == &other) return *this; // ⚠️ Check for self-assignment
    DepartmentOfTransportation temp(other); // 🧱 Create safe temporary copy
    // 🔄 Swap with temporary - it's ensure that the object always remains in a valid state.
    std::swap(this->driversDb, temp.driversDb);
    return *this; // for allowing chainning
}

// ⚡ Move constructor - 🏃 Transfer ownership and nullify source
DepartmentOfTransportation::DepartmentOfTransportation(DepartmentOfTransportation&& other) noexcept
    : driversDb(std::exchange(other.driversDb, nullptr)) {}

// ⚡ Move assignment
DepartmentOfTransportation& DepartmentOfTransportation::operator=(DepartmentOfTransportation&& other) noexcept {
    // ⚠️ Check for self-move
    if (this != &other) {
        delete this->driversDb; // 🧹 Delete current DB
        this->driversDb = std::exchange(other.driversDb, nullptr); // 🏃 Take ownership from source
    }

    // ✅ Return this
    return *this;
}

// 🔍 Const getter - 🧠 Return internal pointer (read-only)
const DriversDb* DepartmentOfTransportation::getDriversDb() const noexcept { return this->driversDb; }

// ✏️ Mutable getter - 🧠 Return pointer (modifiable)
DriversDb* DepartmentOfTransportation::getDriversDb() noexcept { return this->driversDb; }

// 🪄 Deep-copy setter
void DepartmentOfTransportation::setDriversDb(const DriversDb& db) {
    // 🧱 Try to create new deep copy
    try {
        auto* copy = new DriversDb(db);
        delete this->driversDb;// 🧹 Delete old DB
        this->driversDb = copy; // 🔄 Replace pointer
    }
    // ⚠️ Handle allocation error
    catch (const std::bad_alloc& e) {
        std::cerr << "setDriversDb(copy) failed: " << e.what() << std::endl;
    }
}

// ⚙️ Move-based setter
void DepartmentOfTransportation::setDriversDb(DriversDb&& db) noexcept {
    delete this->driversDb; // 🧹 Delete old DB

    try {
        // 🧱 Try to allocate new moved DB
        this->driversDb = new DriversDb(std::move(db));
    }catch (const std::bad_alloc& e) {
        // ⚠️ Catch bad_alloc
        std::cerr << "setDriversDb(move) failed: " << e.what() << std::endl;
        this->driversDb = nullptr;
    }
}

// 🧩 Raw-pointer setter
void DepartmentOfTransportation::setDriversDb(DriversDb* ptr) noexcept {
    delete this->driversDb; // 🧹 Delete current DB
    this->driversDb = ptr; // 🏃 Take ownership of provided pointer
}

// 🧾 String summary
std::string DepartmentOfTransportation::toString() const {
    // 🧱 Build readable output
    std::stringstream ss;

    if (!this->driversDb) { ss << "No database loaded\n"; } // 🚫 If DB is null

    else if (this->driversDb->isEmpty()) { ss << "Database empty\n"; } // 📭 If DB is empty

    // ✅ Otherwise show details
    else { ss << "✅ Drivers count: " << this->driversDb->getSize() << "\n" << this->driversDb->toString(); }

    return ss.str(); // 🎯 Return composed string
}
