// Include the header that declares the Client class
#include "Client.h"
// Include the sstream header for std::stringstream used in toString
#include <sstream>

// Constructor that creates a client from name and number of diners
Client::Client(const string* name, const int* diners) {
    // If the name pointer is not null, allocate and copy the name value
    if (name) {
        // Allocate memory on the heap and copy the name string
        this->name = new string(*name);
    } else {
        // If the name pointer is null, set the internal pointer to null
        this->name = nullptr;
    }

    // If the diners pointer is not null, allocate and copy the diners value
    if (diners) {
        // Allocate memory on the heap and copy the number of diners
        this->diners = new int(*diners);
    } else {
        // If the diners pointer is null, set the internal pointer to null
        this->diners = nullptr;
    }
}

// Destructor that releases all dynamically allocated resources
Client::~Client() {
    // Delete the name pointer if it is not null
    delete this->name;
    // Delete the diners pointer if it is not null
    delete this->diners;
}

// Copy constructor that performs a deep copy from another Client object
Client::Client(const Client& other) {
    // If the other object has a valid name pointer, copy its value
    if (other.name) {
        // Allocate memory and copy the string value from the other object
        this->name = new string(*other.name);
    } else {
        // If the other name pointer is null, set this name pointer to null
        this->name = nullptr;
    }

    // If the other object has a valid diners pointer, copy its value
    if (other.diners) {
        // Allocate memory and copy the integer value from the other object
        this->diners = new int(*other.diners);
    } else {
        // If the other diners pointer is null, set this diners pointer to null
        this->diners = nullptr;
    }
}

// Copy assignment operator that performs a deep copy from another Client object
Client& Client::operator=(const Client& other) {
    // Protect against self-assignment by checking if the objects are different
    if (this != &other) {
        // Delete the current name pointer to avoid memory leak
        delete this->name;
        // Delete the current diners pointer to avoid memory leak
        delete this->diners;

        // If the other object has a valid name pointer, copy its value
        if (other.name) {
            // Allocate memory and copy the string value from the other object
            this->name = new string(*other.name);
        } else {
            // If the other name pointer is null, set this name pointer to null
            this->name = nullptr;
        }

        // If the other object has a valid diners pointer, copy its value
        if (other.diners) {
            // Allocate memory and copy the integer value from the other object
            this->diners = new int(*other.diners);
        } else {
            // If the other diners pointer is null, set this diners pointer to null
            this->diners = nullptr;
        }
    }

    // Return a reference to this object to allow chained assignments
    return *this;
}

// Move constructor that steals the resources from another Client object
Client::Client(Client&& other) noexcept {
    // Steal the name pointer from the other object
    this->name = other.name;
    // Steal the diners pointer from the other object
    this->diners = other.diners;

    // Reset the other object's name pointer to null to avoid double deletion
    other.name = nullptr;
    // Reset the other object's diners pointer to null to avoid double deletion
    other.diners = nullptr;
}

// Move assignment operator that steals the resources from another Client object
Client& Client::operator=(Client&& other) noexcept {
    // Protect against self-assignment by checking if the objects are different
    if (this != &other) {
        // Delete the current name pointer to avoid memory leak
        delete this->name;
        // Delete the current diners pointer to avoid memory leak
        delete this->diners;

        // Steal the name pointer from the other object
        this->name = other.name;
        // Steal the diners pointer from the other object
        this->diners = other.diners;

        // Reset the other object's name pointer to null to avoid double deletion
        other.name = nullptr;
        // Reset the other object's diners pointer to null to avoid double deletion
        other.diners = nullptr;
    }

    // Return a reference to this object to allow chained assignments
    return *this;
}

// Getter that returns a const pointer to the client name
const string* Client::getName() const {
    // Return the pointer to the name string
    return this->name;
}

// Getter that returns a const pointer to the number of diners
const int* Client::getDiners() const {
    // Return the pointer to the number of diners
    return this->diners;
}

// Setter that replaces the client name with a deep copy of the given name
// ReSharper disable once CppParameterNamesMismatch
void Client::setName(const string* namee) {
    // Delete the existing name pointer to avoid memory leak
    delete this->name;

    // If the input pointer is not null, allocate and copy the name value
    if (namee) {
        // Allocate memory and copy the string value from the input pointer
        this->name = new string(*namee);
    } else {
        // If the input pointer is null, set the internal name pointer to null
        this->name = nullptr;
    }
}

// Setter that replaces the number of diners with a deep copy of the given value
// ReSharper disable once CppParameterNamesMismatch
void Client::setDiners(const int* dineers) {
    // Delete the existing diners pointer to avoid memory leak
    delete this->diners;

    // If the input pointer is not null, allocate and copy the diners value
    if (dineers) {
        // Allocate memory and copy the integer value from the input pointer
        this->diners = new int(*dineers);
    } else {
        // If the input pointer is null, set the internal diners pointer to null
        this->diners = nullptr;
    }
}

// Method that converts the client data into a human-readable string
string Client::toString() const {
    // Create a stringstream object to build the resulting string
    std::stringstream ss;

    // Append the class name and opening bracket to the stream
    ss << "Client(";

    // If the name pointer is not null, append the name value
    if (this->name) {
        // Append the name field label and value
        ss << "name=" << *this->name;
    } else {
        // Append a label that indicates the name is null
        ss << "name=null";
    }

    // Append a separator between fields
    ss << ", ";

    // If the diners pointer is not null, append the diners value
    if (this->diners) {
        // Append the diners field label and value
        ss << "diners=" << *this->diners;
    } else {
        // Append a label that indicates the diners pointer is null
        ss << "diners=null";
    }

    // Append the closing bracket of the object representation
    ss << ")";

    // Return the string built inside the stringstream
    return ss.str();
}
