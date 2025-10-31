#include "Driver.h"
#include <sstream>

// constructor
Driver::Driver(const std::string* first_name, const std::string* last_name, const int* id, const int* age) {
    // deep copy or nullptr
    this->firstName = first_name ? new std::string(*first_name) : nullptr;
    this->lastName = last_name ? new std::string(*last_name) : nullptr;
    this->id = id ? new int(*id) : nullptr;
    // ReSharper disable once CppDFAMemoryLeak
    this->age = age ? new int(*age) : nullptr;
}

// destructor
Driver::~Driver() {
    // free all
    delete this->firstName;
    delete this->lastName;
    delete this->id;
    delete this->age;
}

// copy constructor
Driver::Driver(const Driver& other) {
    // deep copy all
    this->firstName = other.firstName ? new std::string(*other.firstName) : nullptr;
    this->lastName = other.lastName ? new std::string(*other.lastName) : nullptr;
    this->id = other.id ? new int(*other.id) : nullptr;
    // ReSharper disable once CppDFAMemoryLeak
    this->age = other.age ? new int(*other.age) : nullptr;
}

// copy assignment
Driver& Driver::operator=(const Driver& other) {
    // avoid self copy
    if (this != &other) {
        delete this->firstName;
        delete this->lastName;
        delete this->id;
        delete this->age;
        // deep copy again
        this->firstName = other.firstName ? new std::string(*other.firstName) : nullptr;
        this->lastName = other.lastName ? new std::string(*other.lastName) : nullptr;
        this->id = other.id ? new int(*other.id) : nullptr;
        this->age = other.age ? new int(*other.age) : nullptr;
    }
    return *this;
}

// move constructor
Driver::Driver(Driver&& other) noexcept {
    // steal pointers
    this->firstName = other.firstName;
    this->lastName = other.lastName;
    this->id = other.id;
    this->age = other.age;

    // reset source
    other.firstName = nullptr;
    other.lastName = nullptr;
    other.id = nullptr;
    other.age = nullptr;
}

// move assignment
Driver& Driver::operator=(Driver&& other) noexcept {
    // avoid self move
    if (this != &other) {
        delete this->firstName;
        delete this->lastName;
        delete this->id;
        delete this->age;

        // steal pointers
        this->firstName = other.firstName;
        this->lastName = other.lastName;
        this->id = other.id;
        this->age = other.age;

        // reset source
        other.firstName = nullptr;
        other.lastName = nullptr;
        other.id = nullptr;
        other.age = nullptr;
    }
    return *this;
}

// get first name (safe)
std::string Driver::getFirstName() const {
    // check nullptr
    return this->firstName ? *this->firstName : "null";
}

// set first name
void Driver::setFirstName(const std::string* first_name) {
    delete this->firstName;
    this->firstName = first_name ? new std::string(*first_name) : nullptr;
}

// get last name (safe)
std::string Driver::getLastName() const {
    // check nullptr
    return this->lastName ? *this->lastName : "null";
}

// set last name
void Driver::setLastName(const std::string* last_name) {
    delete this->lastName;
    this->lastName = last_name ? new std::string(*last_name) : nullptr;
}

// get id (safe)
int Driver::getId() const {
    // check nullptr
    return this->id ? *this->id : 0;
}

// set id
void Driver::setId(const int* ident) {
    delete this->id;
    this->id = ident ? new int(*ident) : nullptr;
}

// get age (safe)
int Driver::getAge() const {
    // check nullptr
    return this->age ? *this->age : 0;
}

// set age
void Driver::setAge(const int* driverAge) {
    delete this->age;
    this->age = driverAge ? new int(*driverAge) : nullptr;
}

// toString (safe)
std::string Driver::toString() const {
    // build output
    std::stringstream ss;
    ss << "firstName: " << (this->firstName ? *this->firstName : "null") << ", ";
    ss << "lastName: "  << (this->lastName ? *this->lastName : "null") << ", ";
    ss << "id: "        << (this->id ? std::to_string(*this->id) : "null") << ", ";
    ss << "age: "       << (this->age ? std::to_string(*this->age) : "null") << ".";
    return ss.str();
}
