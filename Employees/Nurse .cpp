#include "Nurse .h"

// 👉 Default constructor - Allocate default type
// ReSharper disable once CppRedundantBaseClassInitializer
Nurse::Nurse() : Employee() { this->type = new string("Practical"); }

// 👉 Parameter constructor - Deep-copy nurse type
Nurse::Nurse(const string* n, const string* t) : Employee(n) { this->type = new string(*t); }

// 👉 Destructor - Free nurse type memory
Nurse::~Nurse() { delete this->type; }

// 👉 Copy constructor - Deep-copy nurse type
Nurse::Nurse(const Nurse& other) : Employee(other) { this->type = new string(*other.type); }

// 👉 Copy assignment
Nurse& Nurse::operator=(const Nurse& other){
    // Guard self
    if (this == &other) return *this;

    // Call base copy assignment
    Employee::operator=(other);

    // Delete old
    delete this->type;

    // Deep copy new
    this->type = new string(*other.type);

    return *this;
}

// 👉 Move constructor
Nurse::Nurse(Nurse&& other) noexcept : Employee(std::move(other)) {
    // Steal pointer
    this->type = other.type;

    // Null original
    other.type = nullptr;
}

// 👉 Move assignment
Nurse& Nurse::operator=(Nurse&& other) noexcept {
    // Guard self
    if (this == &other) return *this;

    // Base move assignment
    Employee::operator=(std::move(other));

    // Delete old
    delete this->type;

    // Steal ownership
    this->type = other.type;
    other.type = nullptr;

    return *this;
}

// 👉 Getter for type
const string* Nurse::getType() const { return this->type; }

// 👉 Setter for type
void Nurse::setType(const string* t) {
    delete this->type;
    this->type = new string(*t);
}

// 👉 String output
string Nurse::toString() const {
    return "Nurse #" + to_string(*this->num) +
           ", Name: " + *this->name +
           ", Type: " + *this->type;
}

// 👉 Print nurse details
void Nurse::print() const {
    cout << toString() << endl;
}
