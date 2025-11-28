#include "GardenApp.h" // Include GardenApp header

#include <sstream>

GardenApp::GardenApp(const int floorVal, const int numAppVal, const int areaVal, const int gardenAreaVal)
    : Appartment(floorVal, numAppVal, areaVal) { // Call base constructor for basic apartment data
    this->gardenArea = new int(gardenAreaVal); // Allocate and store garden area
}

GardenApp::~GardenApp() { // Destructor
    delete this->gardenArea; // Delete the garden area pointer
}

GardenApp::GardenApp(const GardenApp& other) : Appartment(other) { // Call base copy constructor
    this->gardenArea = other.gardenArea ? new int(*other.gardenArea) : nullptr; // Deep copy garden area
}

GardenApp& GardenApp::operator=(const GardenApp& other) { // Copy assignment operator
    if (this != &other) { // Protect against self-assignment

        Appartment::operator=(other); // Use base class copy assignment

        delete this->gardenArea; // Delete current garden area

        this->gardenArea = other.gardenArea ? new int(*other.gardenArea) : nullptr; // Deep copy new garden area
    }
    return *this; // Return reference to this
}

// Call base move constructor
GardenApp::GardenApp(GardenApp&& other) noexcept : Appartment(std::move(other)) {
    this->gardenArea = other.gardenArea; // Steal garden area pointer

    other.gardenArea = nullptr; // Reset other's garden area pointer
}

GardenApp& GardenApp::operator=(GardenApp&& other) noexcept { // Move assignment operator
    if (this != &other) { // Protect against self-assignment

        Appartment::operator=(std::move(other)); // Use base class move assignment

        delete this->gardenArea; // Delete current garden area

        this->gardenArea = other.gardenArea; // Steal garden area pointer

        other.gardenArea = nullptr; // Reset other's pointer
    }
    return *this; // Return reference to this
}

const int* GardenApp::getGardenArea() const { // Getter for garden area
    return this->gardenArea; // Return pointer to garden area
}

void GardenApp::setGardenArea(const int gardenAreaVal) { // Setter for garden area
    if (this->gardenArea) { // If pointer exists
        *this->gardenArea = gardenAreaVal; // Update value
    } else { // If pointer is null
        this->gardenArea = new int(gardenAreaVal); // Allocate and set value
    }
}

string GardenApp::toString() const { // Build a string describing the garden apartment
    const string base = Appartment::toString(); // Get base apartment description

    stringstream ss; // String stream to build extended description
    ss << base; // Start with base description
    ss << ", gardenArea=" << (this->gardenArea ? *this->gardenArea : -1); // Append garden area or -1

    return ss.str(); // Return the final string
}

int GardenApp::getRealPrice() const { // Returns the total price of a garden apartment
    int basePrice = Appartment::getRealPrice(); // Get the base apartment price from the base class

    const int gardenVal = this->gardenArea ? *this->gardenArea : 0; // Read the garden area or use 0 if pointer is null

    basePrice += gardenVal * COST_GARDEN; // Add the price for the garden area

    return basePrice; // Return the total price including garden
}
