#include "Doctor.h"

// 👉 Default constructor
// ReSharper disable once CppRedundantBaseClassInitializer
Doctor::Doctor() : Employee() {
    // Allocate default specialization
    this->specialization = new string("General");
}

// 👉 Parameter constructor
Doctor::Doctor(const string* n, const string* s) : Employee(n) {
    // Deep-copy specialization
    this->specialization = new string(*s);
}

// 👉 Destructor
Doctor::~Doctor() {
    // Free specialization memory
    delete this->specialization;
}

// 👉 Copy constructor
Doctor::Doctor(const Doctor& other) : Employee(other) {
    // Deep-copy specialization
    this->specialization = new string(*other.specialization);
}

// 👉 Move constructor
Doctor::Doctor(Doctor&& other) noexcept : Employee(std::move(other)) {
    // Steal pointer
    specialization = other.specialization;
    // Null out source
    other.specialization = nullptr;
}

// 👉 Copy assignment
Doctor& Doctor::operator=(const Doctor& other) {
    // Guard self-assignment
    if (this == &other) return *this;

    // Base copy assignment
    Employee::operator=(other);

    // Free old memory
    delete this->specialization;

    // Deep-copy new
    this->specialization = new string(*other.specialization);

    return *this;
}

// 👉 Move assignment
Doctor& Doctor::operator=(Doctor&& other) noexcept {
    // Guard self-move
    if (this == &other) return *this;

    // Base move assignment
    Employee::operator=(std::move(other));

    // Free old memory
    delete this->specialization;

    // Steal pointer
    this->specialization = other.specialization;
    other.specialization = nullptr;

    return *this;
}

// 👉 Getter for specialization
const string* Doctor::getSpecialization() const {
    return this->specialization;
}

// 👉 Setter for specialization
void Doctor::setSpecialization(const string* s) {
    // Delete old value
    delete this->specialization;
    // Deep-copy new
    this->specialization = new string(*s);
}

// 👉 Convert doctor to string
string Doctor::toString() const {
    return "Doctor #" + to_string(*this->num) +
           ", Name: " + *this->name +
           ", Specialization: " + *this->specialization;
}

// 👉 Print doctor details
void Doctor::print() const {
    cout << toString() << endl;
}
