// Include the header that declares the Table class
#include "Table.h"
// Include the sstream header for std::stringstream used in toString
#include <sstream>

// Normalize places to allowed values (2, 4, 8)
// Any other value defaults to 2
int Table::normalizePlaces(const int value) {
    return value == 2 || value == 4 || value == 8 ? value : 2;
}

// Clamp free places between 0 and places
int Table::normalizeFree(const int freeValue, const int placesValue) {
    return freeValue < 0 ? 0 : freeValue > placesValue ? placesValue : freeValue;
}

// Constructor that transfers ownership and ensures valid internal state
Table::Table(std::unique_ptr<int> num, std::unique_ptr<int> places,
             std::unique_ptr<int> freePlaces) {
    // Move ownership into class members
    this->num = std::move(num);
    this->places = std::move(places);
    this->freePlaces = std::move(freePlaces);

    // Ensure non-null pointers to prevent dereferencing crashes
    if (!this->num) { this->num = std::make_unique<int>(0); }
    if (!this->places) { this->places = std::make_unique<int>(2); }
    if (!this->freePlaces) { this->freePlaces = std::make_unique<int>(0); }

    // Normalize values after initialization
    *this->places = normalizePlaces(*this->places);
    *this->freePlaces = normalizeFree(*this->freePlaces, *this->places);
}

// Returns reference to table number
const int& Table::getNum() const { return *num; }

// Returns reference to total places
const int& Table::getPlaces() const { return *places; }

// Returns reference to free places
const int& Table::getFreePlaces() const { return *freePlaces; }

// Replaces table number with new value
// ReSharper disable once CppParameterNamesMismatch
void Table::setNum(std::unique_ptr<int> numm) {
    if (!numm) {
        this->num = std::make_unique<int>(0);
        return;
    }
    this->num = std::move(numm);
}

// Replaces places and re-validates dependent values
// ReSharper disable once CppParameterNamesMismatch
void Table::setPlaces(std::unique_ptr<int> placess) {
    if (!placess) { this->places = std::make_unique<int>(2); }
    else { this->places = std::move(placess); }

    *this->places = normalizePlaces(*this->places);
    *this->freePlaces = normalizeFree(*this->freePlaces, *this->places);
}

// Replaces freePlaces and clamps value
// ReSharper disable once CppParameterNamesMismatch
void Table::setFreePlaces(std::unique_ptr<int> freePlacess) {
    if (!freePlacess) { this->freePlaces = std::make_unique<int>(0); }
    else { this->freePlaces = std::move(freePlacess); }

    *this->freePlaces = normalizeFree(*this->freePlaces, *this->places);
}

// Builds a formatted string representation of the table
std::string Table::toString() const {
    std::ostringstream out;
    out << "Table " << *num
        << " | Places: " << *places
        << " | Free: " << *freePlaces;
    return out.str();
}