#include "Supervisor.h"         // 💡 Include Supervisor header
using namespace std;            // 💡 Use standard namespace

// =======================
// 🎯 Default Constructor
// =======================
// ReSharper disable once CppRedundantBaseClassInitializer
Supervisor::Supervisor() : Doctor() {        // 🧱 Call Doctor base constructor
    this->team = new Employee*[10];          // 🎒 Allocate array for 10 Employee* pointers

    for (int i = 0; i < 10; i++) {           // 🔁 Initialize all slots to nullptr
        this->team[i] = nullptr;             // 🧹 Empty slot means no employee
    }

    this->current = new int(0);              // 🔢 Start with 0 team members
}

// =======================
// 🎯 Constructor(name, specialization)
// =======================
Supervisor::Supervisor(const string* name, const string* spec) : Doctor(name, spec) {                          // 🏗️ Forward to Doctor constructor
    this->team = new Employee*[10];          // 🎒 Allocate team array

    for (int i = 0; i < 10; i++) {           // 🔁 Init empty slots
        this->team[i] = nullptr;
    }

    this->current = new int(0);              // 📌 No employees yet
}

// =======================
// 💣 Destructor
// =======================
Supervisor::~Supervisor() {
    for (int i = 0; i < *this->current; i++) {   // 🔁 Loop through team members
        delete this->team[i];                    // 💥 Free each Employee object
    }

    delete[] this->team;                         // 💥 Free array of pointers
    delete this->current;                        // 💥 Free counter
}

// =======================
// 📦 Copy Constructor (Deep Copy)
// =======================
Supervisor::Supervisor(const Supervisor& other): Doctor(other) {
    // ✨ Copy Doctor base class
    this->team = new Employee*[10];              // 🆕 Allocate new pointer array
    this->current = new int(*other.current);     // 📥 Copy # employees

    for (int i = 0; i < 10; i++) {               // 🔁 Copy each employee pointer
        if (i < *this->current && other.team[i]) {

            if (const auto* d = dynamic_cast<Doctor*>(other.team[i]))    // 👨‍⚕️ If Doctor
                this->team[i] = new Doctor(*d);                    // 🧬 Deep-copy doctor
            else if (const auto* n = dynamic_cast<Nurse*>(other.team[i]))// 👩‍⚕️ If Nurse
                this->team[i] = new Nurse(*n);                     // 🧬 Deep-copy nurse
            else
                this->team[i] = nullptr;                           // ❓ Should not happen
        } else {
            this->team[i] = nullptr;                               // 🧹 Empty slot
        }
    }
}

// =======================
// ✍️ Copy Assignment Operator
// =======================
Supervisor& Supervisor::operator=(const Supervisor& other) {
    // 🚫 Self-assignment guard
    if (this == &other) return *this;

    // delete old data
    // 🔥 Free old employees
    for (int i = 0; i < *this->current; i++) { delete this->team[i]; }
    delete[] this->team;                        // 💥 Free old array
    delete this->current;                       // 💥 Free old counter


    Doctor::operator=(other);                   // 👨‍⚕️ Copy Doctor part
    this->team = new Employee*[10];             // 🆕 Allocate fresh array
    this->current = new int(*other.current);    // 📥 Copy count

    for (int i = 0; i < 10; i++) {              // 🔁 Deep-copy employees
        if (i < *this->current && other.team[i]) {

            if (const auto* d = dynamic_cast<Doctor*>(other.team[i]))
                this->team[i] = new Doctor(*d);
            else if (const auto* n = dynamic_cast<Nurse*>(other.team[i]))
                this->team[i] = new Nurse(*n);
            else
                this->team[i] = nullptr;

        } else {
            this->team[i] = nullptr;
        }
    }

    return *this;                               // 🔙 Return this
}

// =======================
// 🚚 Move Constructor
// =======================
Supervisor::Supervisor(Supervisor&& other) noexcept : Doctor(std::move(other)) {
    // 📦 Take resources
    this->team = other.team;
    this->current = other.current;

    // 🧹 Reset source pointers
    other.team = nullptr;
    other.current = nullptr;
}

// =======================
// 🚚 Move Assignment Operator
// =======================
Supervisor& Supervisor::operator=(Supervisor&& other) noexcept {
    // 🚫 Self check
    if (this == &other) return *this;

    // delete old data - 💥 Free old employees
    for (int i = 0; i < *this->current; i++) { delete this->team[i]; }
    delete[] this->team; // 💥 Free array
    delete this->current; // 💥 Free counter


    // 📦 Steal data
    Doctor::operator=(std::move(other)); // 🚚 Move Doctor base
    this->team = other.team;
    this->current = other.current;

    // 🧹 Nullify old
    other.team = nullptr;
    other.current = nullptr;

    return *this;                               // 🔙 Return this
}

// =======================
// ➕ Add Employee
// =======================
// ReSharper disable once CppMemberFunctionMayBeConst
bool Supervisor::addToTeam(Employee* e) {
    if (*this->current >= 10 || !e) return false; // 🚫 No space OR null employee

    this->team[*this->current] = e; // 📌 Add to slot

    (*this->current)++; // 🔢 Increase count

    return true; // added successfully
}

// =======================
// ➖ Remove by ID
// =======================
// ReSharper disable once CppMemberFunctionMayBeConst
bool Supervisor::removeFromTeam(const int* num) {
    if (!num) return false;                         // 🚫 Null input

    for (int i = 0; i < *this->current; i++) {      // 🔍 Find employee
        if (*this->team[i]->getNum() == *num) {           // ✅ Match found

            delete this->team[i];                         // 💣 Delete employee

            // ↪️ Shift array left
            for (int j = i; j < *this->current - 1; j++) {
                team[j] = team[j + 1];
            }

            (*this->current)--;                     // 🔽 Reduce count

            this->team[*this->current] = nullptr;         // 🧹 Clear last slot

            return true;
        }
    }

    return false;                                   // ❌ Not found
}

// =======================
// 🔢 Get team size
// =======================
int Supervisor::getTeamCount() const {
    return *this->current;                          // 📤 Return count value
}

// =======================
// 🧾 String representation
// =======================
string Supervisor::toString() const {
    return "Supervisor: " + *getName() +            // 👤 Name
           " | Team size: " + to_string(*current);  // 🧮 Team size
}

// =======================
// 🖨️ Print everything
// =======================
void Supervisor::print() const {
    cout << toString() << endl;                     // 🖨️ Print self info
    cout << "--- Team ---" << endl;                 // 📍 Team header

    for (int i = 0; i < *current; i++)              // 🔁 Print each employee
        team[i]->print();
}

