#include "Appartment.h" // Include the Appartment class header
#include <iostream>
#include <sstream>

const int Appartment::COST_APP = 1000; // Define price per square meter for apartment
const int Appartment::COST_TERRACE = 300; // Define price per square meter for terrace
const int Appartment::COST_GARDEN = 50; // Define price per square meter for garden

Appartment::Appartment(const int floorVal, const int numAppVal, const int areaVal) { // Constructor that creates a free apartment
    this->owner = new string("FREE"); // Allocate owner string and set initial value to "FREE"
    this->floor = new int(floorVal); // Allocate and store floor number
    this->numApp = new int(numAppVal); // Allocate and store apartment number
    this->area = new int(areaVal); // Allocate and store apartment area
}

Appartment::~Appartment() { // Destructor that releases all dynamic memory
    delete this->owner; // Delete the owner string pointer
    delete this->floor; // Delete the floor pointer
    delete this->numApp; // Delete the apartment number pointer
    delete this->area; // Delete the area pointer
}

Appartment::Appartment(const Appartment& other) { // Copy constructor (deep copy)
    this->owner = other.owner ? new string(*other.owner) : nullptr; // Deep copy owner string if not null
    this->floor = other.floor ? new int(*other.floor) : nullptr; // Deep copy floor if not null
    this->numApp = other.numApp ? new int(*other.numApp) : nullptr; // Deep copy apartment number if not null
    // ReSharper disable once CppDFAMemoryLeak
    this->area = other.area ? new int(*other.area) : nullptr; // Deep copy area if not null
}

Appartment& Appartment::operator=(const Appartment& other) { // Copy assignment operator
    if (this != &other) { // Protect against self-assignment
        delete this->owner; // Delete current owner
        delete this->floor; // Delete current floor
        delete this->numApp; // Delete current apartment number
        delete this->area; // Delete current area

        this->owner = other.owner ? new string(*other.owner) : nullptr; // Deep copy owner
        this->floor = other.floor ? new int(*other.floor) : nullptr; // Deep copy floor
        this->numApp = other.numApp ? new int(*other.numApp) : nullptr; // Deep copy apartment number
        this->area = other.area ? new int(*other.area) : nullptr; // Deep copy area
    }
    return *this; // Return reference to this
}

Appartment::Appartment(Appartment&& other) noexcept { // Move constructor
    this->owner = other.owner; // Steal owner pointer from other
    this->floor = other.floor; // Steal floor pointer from other
    this->numApp = other.numApp; // Steal apartment number pointer from other
    this->area = other.area; // Steal area pointer from other

    other.owner = nullptr; // Reset other's owner pointer
    other.floor = nullptr; // Reset other's floor pointer
    other.numApp = nullptr; // Reset other's apartment number pointer
    other.area = nullptr; // Reset other's area pointer
}

Appartment& Appartment::operator=(Appartment&& other) noexcept { // Move assignment operator
    if (this != &other) { // Protect against self-assignment
        delete this->owner; // Delete current owner
        delete this->floor; // Delete current floor
        delete this->numApp; // Delete current apartment number
        delete this->area; // Delete current area

        this->owner = other.owner; // Steal owner pointer
        this->floor = other.floor; // Steal floor pointer
        this->numApp = other.numApp; // Steal apartment number pointer
        this->area = other.area; // Steal area pointer

        other.owner = nullptr; // Reset other's owner pointer
        other.floor = nullptr; // Reset other's floor pointer
        other.numApp = nullptr; // Reset other's apartment number pointer
        other.area = nullptr; // Reset other's area pointer
    }
    return *this; // Return reference to this
}

const string* Appartment::getOwner() const { // Getter for owner
    return this->owner; // Return pointer to owner string
}

const int* Appartment::getFloor() const { // Getter for floor
    return this->floor; // Return pointer to floor
}

const int* Appartment::getNumApp() const { // Getter for apartment number
    return this->numApp; // Return pointer to apartment number
}

const int* Appartment::getArea() const { // Getter for area
    return this->area; // Return pointer to area
}

void Appartment::setOwner(const string& name) { // Setter for owner
    if (this->owner) { // Check if owner pointer is already allocated
        *this->owner = name; // Assign new name to existing string
    } else { // If owner was null
        this->owner = new string(name); // Allocate new string with the given name
    }
}

void Appartment::setFloor(const int floorVal) { // Setter for floor
    if (this->floor) { // If floor pointer exists
        *this->floor = floorVal; // Assign new floor value
    } else { // If floor pointer is null
        this->floor = new int(floorVal); // Allocate a new int for floor
    }
}

void Appartment::setNumApp(const int numAppVal) { // Setter for apartment number
    if (this->numApp) { // If numApp pointer exists
        *this->numApp = numAppVal; // Assign new apartment number
    } else { // If numApp pointer is null
        this->numApp = new int(numAppVal); // Allocate a new int for apartment number
    }
}

void Appartment::setArea(const int areaVal) { // Setter for area
    if (this->area) { // If area pointer exists
        *this->area = areaVal; // Assign new area value
    } else { // If area pointer is null
        this->area = new int(areaVal); // Allocate a new int for area
    }
}

string Appartment::toString() const { // Build a string describing the apartment
    stringstream ss; // String stream used to build the output

    ss << "Appartment("; // Start of description
    ss << "owner=" << (this->owner ? *this->owner : string("NULL")); // Print owner or NULL
    ss << ", floor=" << (this->floor ? *this->floor : -1); // Print floor or -1
    ss << ", numApp=" << (this->numApp ? *this->numApp : -1); // Print apartment number or -1
    ss << ", area=" << (this->area ? *this->area : -1); // Print area or -1
    ss << ")"; // Close description

    return ss.str(); // Return the final string
}

int Appartment::getRealPrice() const { // Returns the total price of a regular apartment
    const int areaVal = this->area ? *this->area : 0; // Read the apartment area or use 0 if pointer is null
    const int price = areaVal * COST_APP; // Calculate base price using apartment area

    return price; // Return the total price
}

void printAvailableAppartmentsWithinBudget(Appartment* const* apps, const int size, const int maxBudget) { // Prints details of free apartments with price <= maxBudget
    if (!apps || size <= 0) return; // Safety check: if the array pointer is null or size is not positive, do nothing

    for (int i = 0; i < size; i++) { // Iterate over all apartments in the array
        const auto app = apps[i]; // Get the pointer to the current apartment

        if (!app) continue; // If the current pointer is null, skip it

        // ReSharper disable once CppTooWideScopeInitStatement
        const auto owner = app->getOwner(); // Get pointer to the owner name
        // If there is no owner string or the apartment is not free, skip it
        if (!owner || *owner != "FREE") continue;

        // Calculate the price of the apartment using virtual dispatch
        const auto price = app->getRealPrice();
        if (price > maxBudget) continue; // If the price is higher than the client's budget, skip it

        std::cout << app->toString() << ", price=" << price << std::endl; // Print the apartment details and its price
    }
}



