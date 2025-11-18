#include "Clown.h"
#include <sstream>
using namespace Clownp;

// constructor
Clown::Clown(const string *name, const int *weight) {
    // set fields
    this->name = name ? new string(*name) : nullptr;
    this->weight = weight ? new int(*weight) : nullptr;
}

// destructor
Clown::~Clown() {
    // delete name
    delete this->name;
    // delete weight
    delete this->weight;
}

// copy constructor
Clown::Clown(const Clown &other) {
    // copy other Clown data
    this->name = other.name ? new string(*other.name) : nullptr;
    this->weight = other.weight ? new int(*other.weight) : nullptr;
}

// copy assignment
Clown& Clown::operator=(const Clown &other) {
    if (this != &other) {
        // delete old data
        delete this->name;
        delete this->weight;

        // copy other Clown data
        this->name = other.name ? new string(*other.name) : nullptr;
        this->weight = other.weight ? new int(*other.weight) : nullptr;
    }

    // allow chainning
    return *this;
}

// move constructor
Clown::Clown(Clown &&other) noexcept {
    // steal other Clown data
    this->name = other.name;
    this->weight = other.weight;

    // leave other Clown at safe state
    other.name = nullptr;
    other.weight = nullptr;
}

// move assignment
Clown& Clown::operator=(Clown &&other) noexcept {
    if (this != &other) {
        // delete old data
        delete this->name;
        delete this->weight;

        // steal other clown data
        this->name = other.name;
        this->weight = other.weight;

        // leave other Clown at safe state
        other.name = nullptr;
        other.weight = nullptr;
    }

    return *this;
}

// getters
const string *Clown::getName() const {
    return this->name;
}

const int* Clown::getWeight() const {
    return this->weight;
}

// setters
// ReSharper disable once CppParameterNamesMismatch
void Clown::setName(const string *Name) {
    // delete old data
    delete this->name;
    // set new data
    this->name = Name ? new string(*Name) : nullptr;
}

// ReSharper disable once CppParameterNamesMismatch
void Clown::setWeight(const int *Weight){
    // delete old data
    delete this->weight;
    // set new data
    this->weight = Weight ? new int(*Weight) : nullptr;
}

// toString
string Clown::toString() const {
    stringstream ss;
    ss << (this->name ? *this->name : "nullptr") << endl;
    ss << (this->weight ? *this->weight : 0) << endl;
    return ss.str();
}