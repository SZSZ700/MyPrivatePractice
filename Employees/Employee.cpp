#include "Employee.h"

// 👉 Initialize static counter
int Employee::counter = 0;

// 👉 Default constructor
Employee::Employee() {
    // Allocate new name with placeholder
    this->name = new string("No Name");
    // Increase counter and assign ID
    this->num = new int(++this->counter);
}

// 👉 Parameter constructor
Employee::Employee(const string* n) {
    // Allocate and deep copy name
    this->name = new string(*n);
    // Increase counter and assign ID
    this->num = new int(++this->counter);
}

// 👉 Destructor
Employee::~Employee() {
    // Free name memory
    delete this->name;
    // Free num memory
    delete this->num;
}

// 👉 Copy constructor
Employee::Employee(const Employee& other) {
    // Deep copy name
    this->name = new string(*other.name);
    // Deep copy id
    this->num = new int(*other.num);
}

// 👉 Move constructor
Employee::Employee(Employee&& other) noexcept {
    // Steal pointer values
    this->name = other.name;
    this->num = other.num;

    // Null original
    other.name = nullptr;
    other.num = nullptr;
}

// 👉 Copy assignment
Employee& Employee::operator=(const Employee& other) {
    // Guard against self-assignment
    if (this == &other) return *this;

    // Delete old data
    delete this->name;
    delete this->num;

    // Deep copy new data
    this->name = new string(*other.name);
    this->num = new int(*other.num);

    return *this;
}

// 👉 Move assignment
Employee& Employee::operator=(Employee&& other) noexcept {
    // Guard against self-move
    if (this == &other) return *this;

    // Delete old data
    delete name;
    delete num;

    // Steal data
    name = other.name;
    num = other.num;

    // Null source
    other.name = nullptr;
    other.num = nullptr;

    return *this;
}

// 👉 Getter: name
const string* Employee::getName() const { return name; }

// 👉 Getter: num
const int* Employee::getNum() const { return num; }

// 👉 Setter: name
void Employee::setName(const string* n) {
    // Delete old memory
    delete name;
    // Deep-copy the new value
    name = new string(*n);
}

// 👉 toString conversion
string Employee::toString() const {
    return "Employee #" + to_string(*num) + ", Name: " + *name;
}

// 👉 Print details
void Employee::print() const {
    cout << toString() << endl;
}

