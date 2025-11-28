#include "Penthouse.h" // Include Penthouse header
#include <sstream>

Penthouse::Penthouse(const int floorVal, const int numAppVal, const int areaVal,
    const int numTerrVal, const int terraceAreaVal, const bool seaViewVal)
    : Appartment(floorVal, numAppVal, areaVal) { // Call base constructor

    this->numTerr = new int(numTerrVal); // Allocate and store number of terraces
    this->terraceArea = new int(terraceAreaVal); // Allocate and store total terrace area
    this->seaView = new bool(seaViewVal); // Allocate and store sea view flag
}

Penthouse::~Penthouse() { // Destructor
    delete this->numTerr; // Delete number of terraces pointer
    delete this->terraceArea; // Delete terrace area pointer
    delete this->seaView; // Delete sea view pointer
}

Penthouse::Penthouse(const Penthouse& other) : Appartment(other) { // Call base copy constructor
    this->numTerr = other.numTerr ? new int(*other.numTerr) : nullptr; // Deep copy numTerr
    this->terraceArea = other.terraceArea ? new int(*other.terraceArea) : nullptr; // Deep copy terraceArea
    this->seaView = other.seaView ? new bool(*other.seaView) : nullptr; // Deep copy seaView
}

Penthouse& Penthouse::operator=(const Penthouse& other) { // Copy assignment operator
    if (this != &other) { // Protect against self-assignment

        Appartment::operator=(other); // Use base class copy assignment

        delete this->numTerr; // Delete current numTerr
        delete this->terraceArea; // Delete current terraceArea
        delete this->seaView; // Delete current seaView

        this->numTerr = other.numTerr ? new int(*other.numTerr) : nullptr; // Deep copy numTerr
        this->terraceArea = other.terraceArea ? new int(*other.terraceArea) : nullptr; // Deep copy terraceArea
        this->seaView = other.seaView ? new bool(*other.seaView) : nullptr; // Deep copy seaView
    }
    return *this; // Return reference to this
}

// Call base move constructor (currently copying base)
Penthouse::Penthouse(Penthouse&& other) noexcept : Appartment(std::move(other)) {
    this->numTerr = other.numTerr; // Steal numTerr pointer
    this->terraceArea = other.terraceArea; // Steal terraceArea pointer
    this->seaView = other.seaView; // Steal seaView pointer

    other.numTerr = nullptr; // Reset other's numTerr pointer
    other.terraceArea = nullptr; // Reset other's terraceArea pointer
    other.seaView = nullptr; // Reset other's seaView pointer
}

Penthouse& Penthouse::operator=(Penthouse&& other) noexcept { // Move assignment operator
    if (this != &other) { // Protect against self-assignment
        Appartment::operator=(std::move(other)); // Use base class copy assignment

        delete this->numTerr; // Delete current numTerr
        delete this->terraceArea; // Delete current terraceArea
        delete this->seaView; // Delete current seaView

        this->numTerr = other.numTerr; // Steal numTerr pointer
        this->terraceArea = other.terraceArea; // Steal terraceArea pointer
        this->seaView = other.seaView; // Steal seaView pointer

        other.numTerr = nullptr; // Reset other's numTerr pointer
        other.terraceArea = nullptr; // Reset other's terraceArea pointer
        other.seaView = nullptr; // Reset other's seaView pointer
    }
    return *this; // Return reference to this
}

const int* Penthouse::getNumTerr() const { // Getter for numTerr
    return this->numTerr; // Return pointer to number of terraces
}

const int* Penthouse::getTerraceArea() const { // Getter for terraceArea
    return this->terraceArea; // Return pointer to terrace area
}

const bool* Penthouse::getSeaView() const { // Getter for seaView
    return this->seaView; // Return pointer to sea view flag
}

void Penthouse::setNumTerr(const int numTerrVal) { // Setter for numTerr
    if (this->numTerr) { // If pointer exists
        *this->numTerr = numTerrVal; // Update value
    } else { // If pointer is null
        this->numTerr = new int(numTerrVal); // Allocate and set value
    }
}

void Penthouse::setTerraceArea(const int terraceAreaVal) { // Setter for terraceArea
    if (this->terraceArea) { // If pointer exists
        *this->terraceArea = terraceAreaVal; // Update value
    } else { // If pointer is null
        this->terraceArea = new int(terraceAreaVal); // Allocate and set value
    }
}

void Penthouse::setSeaView(const bool seaViewVal) { // Setter for seaView
    if (this->seaView) { // If pointer exists
        *this->seaView = seaViewVal; // Update value
    } else { // If pointer is null
        this->seaView = new bool(seaViewVal); // Allocate and set value
    }
}

string Penthouse::toString() const { // Build string describing penthouse
    const string base = Appartment::toString(); // Get base description
    stringstream ss; // String stream to build extended description
    ss << base; // Start with base
    ss << ", numTerr=" << (this->numTerr ? *this->numTerr : -1); // Append number of terraces
    ss << ", terraceArea=" << (this->terraceArea ? *this->terraceArea : -1); // Append terrace area
    ss << ", seaView=" << (this->seaView ? (*this->seaView ? "true" : "false") : "NULL"); // Append sea view flag
    return ss.str(); // Return final string
}
