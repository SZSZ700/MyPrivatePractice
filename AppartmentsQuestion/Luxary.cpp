#include "Luxary.h" // Include Luxary header

#include <sstream>

Luxary::Luxary(const int floorVal, const int numAppVal, const int areaVal, const int terraceAreaVal)
    : Appartment(floorVal, numAppVal, areaVal) { // Call base constructor
    this->terraceArea = new int(terraceAreaVal); // Allocate and store terrace area
}

Luxary::~Luxary() { // Destructor
    delete this->terraceArea; // Delete terrace area pointer
}

Luxary::Luxary(const Luxary& other) : Appartment(other) { // Call base copy constructor
    this->terraceArea = other.terraceArea ? new int(*other.terraceArea) : nullptr; // Deep copy terrace area
}

Luxary& Luxary::operator=(const Luxary& other) { // Copy assignment operator
    if (this != &other) { // Protect against self-assignment

        Appartment::operator=(other); // Use base class copy assignment

        delete this->terraceArea; // Delete current terrace area

        this->terraceArea = other.terraceArea ? new int(*other.terraceArea) : nullptr; // Deep copy new terrace area
    }
    return *this; // Return reference to this
}

Luxary::Luxary(Luxary&& other) noexcept : Appartment(std::move(other)) { // Call base move constructor
    this->terraceArea = other.terraceArea; // Steal terraceArea pointer
    other.terraceArea = nullptr; // Reset other's terraceArea pointer
}

Luxary& Luxary::operator=(Luxary&& other) noexcept { // Move assignment operator
    if (this != &other) { // Protect against self-assignment

        Appartment::operator=(std::move(other)); // Use base class copy assignment

        delete this->terraceArea; // Delete current terrace area

        this->terraceArea = other.terraceArea; // Steal terraceArea pointer

        other.terraceArea = nullptr; // Reset other's terraceArea pointer
    }
    return *this; // Return reference to this
}

const int* Luxary::getTerraceArea() const { // Getter for terraceArea
    return this->terraceArea; // Return pointer to terrace area
}

void Luxary::setTerraceArea(const int terraceAreaVal) { // Setter for terraceArea
    if (this->terraceArea) { // If pointer exists
        *this->terraceArea = terraceAreaVal; // Update value
    } else { // If pointer is null
        this->terraceArea = new int(terraceAreaVal); // Allocate and set value
    }
}

string Luxary::toString() const { // Build string describing Luxary apartment
    const string base = Appartment::toString(); // Get base apartment description

    stringstream ss; // String stream to build extended description
    ss << base; // Start with base description
    ss << ", terraceArea=" << (this->terraceArea ? *this->terraceArea : -1); // Append terrace area or -1

    return ss.str(); // Return final string
}

int Luxary::getRealPrice() const { // Returns the total price of a luxury apartment
    int basePrice = Appartment::getRealPrice(); // Get the base apartment price from the base class

    // Read the terrace area or use 0 if pointer is null
    const int terraceVal = this->terraceArea ? *this->terraceArea : 0;

    basePrice += terraceVal * COST_TERRACE; // Add the price of the terrace

    return basePrice; // Return the total price
}
