// Include the header that declares the Table class
#include "Table.h"
// Include the sstream header for std::stringstream used in toString
#include <sstream>

// Helper method that normalizes the number of places to one of the allowed values (2, 4, 8)
int Table::normalizePlaces(const int value) {
    // If the given value is exactly 2, return it unchanged
    if (value == 2) {
        return 2;
    }
    // If the given value is exactly 4, return it unchanged
    if (value == 4) {
        return 4;
    }
    // If the given value is exactly 8, return it unchanged
    if (value == 8) {
        return 8;
    }
    // For any other value, default to the smallest allowed table size (2 places)
    return 2;
}

// Helper method that normalizes the number of free places to be between 0 and places
int Table::normalizeFree(const int freeValue, const int placesValue) {
    // If free places is negative, clamp it to 0
    if (freeValue < 0) {
        return 0;
    }
    // If free places is greater than the total places, clamp it to the total places
    if (freeValue > placesValue) {
        return placesValue;
    }
    // Otherwise, return the free places value unchanged
    return freeValue;
}

// Constructor that creates a table from table number, total places and free places
Table::Table(const int* num, const int* places, const int* freePlaces) {
    // If the table number pointer is not null, allocate and copy the number
    if (num) {
        // Allocate memory and copy the table number
        this->num = new int(*num);
    } else {
        // If the pointer is null, set the internal pointer to null
        this->num = nullptr;
    }

    // If the places pointer is not null, allocate and copy a normalized value
    if (places) {
        // Normalize the given places value to one of the allowed sizes
        const int normalizedPlaces = normalizePlaces(*places);
        // Allocate memory and store the normalized number of places
        this->places = new int(normalizedPlaces);
    } else {
        // If the pointer is null, set the internal places pointer to null
        this->places = nullptr;
    }

    // If the freePlaces pointer is not null and places is not null, allocate and copy a normalized free value
    if (freePlaces && this->places) {
        // Normalize the number of free places to be within the valid range
        const int normalizedFree = normalizeFree(*freePlaces, *this->places);
        // Allocate memory and store the normalized number of free places
        this->freePlaces = new int(normalizedFree);
    } else if (this->places) {
        // If freePlaces is null but table size is known, assume all places are free
        this->freePlaces = new int(*this->places);
    } else {
        // If places is null, set the freePlaces pointer to null as well
        this->freePlaces = nullptr;
    }
}

// Destructor that releases all dynamically allocated resources
Table::~Table() {
    // Delete the table number pointer if it is not null
    delete this->num;
    // Delete the places pointer if it is not null
    delete this->places;
    // Delete the freePlaces pointer if it is not null
    delete this->freePlaces;
}

// Copy constructor that performs a deep copy from another Table object
Table::Table(const Table& other) {
    // If the other object has a valid table number pointer, copy its value
    if (other.num) {
        // Allocate memory and copy the table number value
        this->num = new int(*other.num);
    } else {
        // If the other num pointer is null, set this num pointer to null
        this->num = nullptr;
    }

    // If the other object has a valid places pointer, copy its value
    if (other.places) {
        // Allocate memory and copy the places value
        this->places = new int(*other.places);
    } else {
        // If the other places pointer is null, set this places pointer to null
        this->places = nullptr;
    }

    // If the other object has a valid freePlaces pointer, copy its value
    if (other.freePlaces) {
        // Allocate memory and copy the freePlaces value
        this->freePlaces = new int(*other.freePlaces);
    } else {
        // If the other freePlaces pointer is null, set this freePlaces pointer to null
        this->freePlaces = nullptr;
    }
}

// Copy assignment operator that performs a deep copy from another Table object
Table& Table::operator=(const Table& other) {
    // Protect against self-assignment by checking if the objects are different
    if (this != &other) {
        // Delete the current table number pointer to avoid memory leak
        delete this->num;
        // Delete the current places pointer to avoid memory leak
        delete this->places;
        // Delete the current freePlaces pointer to avoid memory leak
        delete this->freePlaces;

        // If the other object has a valid table number pointer, copy its value
        if (other.num) {
            // Allocate memory and copy the table number value
            this->num = new int(*other.num);
        } else {
            // If the other num pointer is null, set this num pointer to null
            this->num = nullptr;
        }

        // If the other object has a valid places pointer, copy its value
        if (other.places) {
            // Allocate memory and copy the places value
            this->places = new int(*other.places);
        } else {
            // If the other places pointer is null, set this places pointer to null
            this->places = nullptr;
        }

        // If the other object has a valid freePlaces pointer, copy its value
        if (other.freePlaces) {
            // Allocate memory and copy the freePlaces value
            this->freePlaces = new int(*other.freePlaces);
        } else {
            // If the other freePlaces pointer is null, set this freePlaces pointer to null
            this->freePlaces = nullptr;
        }
    }

    // Return a reference to this object to allow chained assignments
    return *this;
}

// Move constructor that steals the resources from another Table object
Table::Table(Table&& other) noexcept {
    // Steal the table number pointer from the other object
    this->num = other.num;
    // Steal the places pointer from the other object
    this->places = other.places;
    // Steal the freePlaces pointer from the other object
    this->freePlaces = other.freePlaces;

    // Reset the other object's num pointer to null to avoid double deletion
    other.num = nullptr;
    // Reset the other object's places pointer to null to avoid double deletion
    other.places = nullptr;
    // Reset the other object's freePlaces pointer to null to avoid double deletion
    other.freePlaces = nullptr;
}

// Move assignment operator that steals the resources from another Table object
Table& Table::operator=(Table&& other) noexcept {
    // Protect against self-assignment by checking if the objects are different
    if (this != &other) {
        // Delete the current table number pointer to avoid memory leak
        delete this->num;
        // Delete the current places pointer to avoid memory leak
        delete this->places;
        // Delete the current freePlaces pointer to avoid memory leak
        delete this->freePlaces;

        // Steal the table number pointer from the other object
        this->num = other.num;
        // Steal the places pointer from the other object
        this->places = other.places;
        // Steal the freePlaces pointer from the other object
        this->freePlaces = other.freePlaces;

        // Reset the other object's num pointer to null to avoid double deletion
        other.num = nullptr;
        // Reset the other object's places pointer to null to avoid double deletion
        other.places = nullptr;
        // Reset the other object's freePlaces pointer to null to avoid double deletion
        other.freePlaces = nullptr;
    }

    // Return a reference to this object to allow chained assignments
    return *this;
}

// Getter that returns a const pointer to the table number
const int* Table::getNum() const {
    // Return the pointer to the table number
    return this->num;
}

// Getter that returns a const pointer to the total number of places
const int* Table::getPlaces() const {
    // Return the pointer to the total number of places
    return this->places;
}

// Getter that returns a const pointer to the number of free places
const int* Table::getFreePlaces() const {
    // Return the pointer to the number of free places
    return this->freePlaces;
}

// Setter that replaces the table number with a deep copy of the given value
// ReSharper disable once CppParameterNamesMismatch
void Table::setNum(const int* n) {
    // Delete the existing table number pointer to avoid memory leak
    delete this->num;

    // If the input pointer is not null, allocate and copy the number
    if (n) {
        // Allocate memory and copy the table number value
        this->num = new int(*n);
    } else {
        // If the input pointer is null, set the internal num pointer to null
        this->num = nullptr;
    }
}

// Setter that replaces the total number of places and normalizes it to 2, 4 or 8
// ReSharper disable once CppParameterNamesMismatch
void Table::setPlaces(const int* p) {
    // Delete the existing places pointer to avoid memory leak
    delete this->places;

    // If the input pointer is not null, allocate and store normalized places
    if (p) {
        // Normalize the input number of places to one of the allowed values
        const int normalizedPlaces = normalizePlaces(*p);
        // Allocate memory and store the normalized places value
        this->places = new int(normalizedPlaces);
    } else {
        // If the input pointer is null, set the internal places pointer to null
        this->places = nullptr;
    }

    // If we have a valid places pointer, we should also adjust freePlaces if needed
    if (this->places) {
        // If freePlaces pointer is null, assume all places are free
        if (!this->freePlaces) {
            // Allocate memory and set freePlaces equal to the total places
            this->freePlaces = new int(*this->places);
        } else {
            // Normalize the current freePlaces value according to the new total places
            const int normalizedFree = normalizeFree(*this->freePlaces, *this->places);
            // Update the freePlaces value after normalization
            *this->freePlaces = normalizedFree;
        }
    } else {
        // If places is now null, delete and nullify freePlaces as well
        delete this->freePlaces;
        // Set the freePlaces pointer to null
        this->freePlaces = nullptr;
    }
}

// Setter that replaces the number of free places and clamps it to a valid range
// ReSharper disable once CppParameterNamesMismatch
void Table::setFreePlaces(const int* fp) {
    // If there is no valid places information, we cannot set freePlaces meaningfully
    if (!this->places) {
        // Delete the existing freePlaces pointer to avoid inconsistent state
        delete this->freePlaces;
        // Set the freePlaces pointer to null
        this->freePlaces = nullptr;
        // Exit the function early because places is unknown
        return;
    }

    // Delete the existing freePlaces pointer to avoid memory leak
    delete this->freePlaces;

    // If the input pointer is not null, allocate and store a normalized freePlaces value
    if (fp) {
        // Normalize the freePlaces according to the current total places
        const int normalizedFree = normalizeFree(*fp, *this->places);
        // Allocate memory and store the normalized number of free places
        this->freePlaces = new int(normalizedFree);
    } else {
        // If the input pointer is null, assume all places are free
        this->freePlaces = new int(*this->places);
    }
}

// Method that converts the table data into a human-readable string
string Table::toString() const {
    // Create a stringstream object to build the resulting string
    std::stringstream ss;

    // Append the class name and opening bracket to the stream
    ss << "Table(";

    // If the num pointer is not null, append the table number value
    if (this->num) {
        // Append the num field label and value
        ss << "num=" << *this->num;
    } else {
        // Append a label that indicates the num pointer is null
        ss << "num=null";
    }

    // Append a separator between fields
    ss << ", ";

    // If the places pointer is not null, append the total places value
    if (this->places) {
        // Append the places field label and value
        ss << "places=" << *this->places;
    } else {
        // Append a label that indicates the places pointer is null
        ss << "places=null";
    }

    // Append a separator between fields
    ss << ", ";

    // If the freePlaces pointer is not null, append the number of free places
    if (this->freePlaces) {
        // Append the freePlaces field label and value
        ss << "free=" << *this->freePlaces;
    } else {
        // Append a label that indicates the freePlaces pointer is null
        ss << "free=null";
    }

    // Append the closing bracket of the object representation
    ss << ")";

    // Return the string built inside the stringstream
    return ss.str();
}
