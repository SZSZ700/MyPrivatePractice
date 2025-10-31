#include "DriversDb.h"               // Include header for class definition
#include <iostream>                  // For console output
#include <sstream>                   // For stringstream use

// 🏗️👷‍ constructor: initializes an empty list
DriversDb::DriversDb() {
    // Initialize head pointer to nullptr
    this->drivers = nullptr;
    // Initialize size counter to zero
    this->size = 0;
}

// ⚠️ Destructor releases all Driver* and Node<Driver*> objects
DriversDb::~DriversDb() {
    // Non-const pointer to head (we delete nodes)
    // ReSharper disable once CppLocalVariableMayBeConst
    Node<Driver*>* cur = this->drivers;

    // Iterate through the entire linked list
    while (cur != nullptr) {
        // Store next node before deletion
        Node<Driver*>* nxt = cur->getNext();

        // Delete the stored Driver object if exists
        delete cur->getValue();
        // Delete the node wrapper itself
        delete cur;
        // Move to the next node
        cur = nxt;
    }

    // Reset head pointer
    this->drivers = nullptr;
    // Reset size counter
    this->size = 0;
}

// 🏗️👷‍ Copy constructor: performing full deep copy
DriversDb::DriversDb(const DriversDb& other) {
    // Initialize empty destination list
    this->drivers = nullptr;
    // Initialize size counter
    this->size = 0;

    // Return early if source is empty
    if (other.drivers == nullptr) return;

    // Pointer to source list head
    const Node<Driver*>* src = other.drivers;
    // Tail pointer for destination list
    Node<Driver*>* tail = nullptr;

    // Traverse source list
    while (src != nullptr) {
        // Create new node with deep-copied Driver (if not null)
        auto* toAdd = new Node(src->getValue() ? new Driver(*src->getValue()) : nullptr);
        // If first node, assign as head and tail
        if (this->drivers == nullptr) {
            this->drivers = toAdd;
            tail = toAdd;
        }
        // Else append to tail
        else {
            // ReSharper disable once CppDFANullDereference
            tail->setNext(toAdd);
            tail = toAdd;
        }
        // Move to next source node
        src = src->getNext();
        // Increment element counter
        this->size++;
    }
}

// 🏗️👷 Copy assignment: clears current and deep-copies from other
DriversDb& DriversDb::operator=(const DriversDb& other) {
    // Avoid self-assignment
    if (this == &other) return *this;
    // Clear old content
    clear();

    // Return if source is empty
    if (other.drivers == nullptr) return *this;

    // Pointer to source list
    const Node<Driver*>* src = other.drivers;
    // Tail pointer for destination list
    Node<Driver*>* tail = nullptr;
    // Traverse source list
    while (src != nullptr) {
        // Create deep-copied node
        auto* node = new Node(src->getValue() ? new Driver(*src->getValue()) : nullptr);
        // If first node, set as head
        if (this->drivers == nullptr) {
            this->drivers = node;
            tail = node;
        }
        // Else append to tail
        else {
            // ReSharper disable once CppDFANullDereference
            tail->setNext(node);
            tail = node;
        }
        // Move to next node
        src = src->getNext();
        // Increment size counter
        this->size++;
    }

    // Return current object
    return *this;
}

// ‍🥷 Move constructor: transfers ownership from source
DriversDb::DriversDb(DriversDb&& other) noexcept {
    // Steal head pointer from source
    this->drivers = other.drivers;
    // Steal size value
    this->size = other.size;

    // Reset source pointers
    other.drivers = nullptr;
    other.size = 0;
}

// ‍🥷 Move assignment: clears current and steals from source
DriversDb& DriversDb::operator=(DriversDb&& other) noexcept {
    // Prevent self-move
    if (this != &other) {
        // Clear existing content
        clear();

        // Steal linked list and size
        this->drivers = other.drivers;
        this->size = other.size;

        // Reset source to safe empty state
        other.drivers = nullptr;
        other.size = 0;
    }

    // Return current instance
    return *this;
}

// 📏 Returns total number of stored drivers
int DriversDb::getSize() const { return this->size; }

// 🧩 Returns true if the database is empty
// Check if size equals zero
bool DriversDb::isEmpty() const { return this->size == 0; }

// 🚙 Adds a new driver to the database (deep copy)
void DriversDb::addDriver(const Driver* d) {
    // Deep copy incoming driver or store nullptr
    auto* toAdd = new Node(d ? new Driver(*d) : nullptr);
    // Link new node to the current head
    toAdd->setNext(this->drivers);

    // Update head pointer
    this->drivers = toAdd;
    // Increment size counter
    this->size++;
}

// 🚓 Finds first driver with given id (returns new copy)
Driver* DriversDb::findById(const int id) const {
    // Start from head node
    const Node<Driver*>* cur = this->drivers;

    // Traverse entire list
    while (cur != nullptr) {
        // Access stored driver pointer
        // If exists and id matches
        // ReSharper disable once CppLocalVariableMayBeConst
        if (Driver* pv = cur->getValue(); pv && pv->getId() == id) {
            // Return heap-allocated copy (caller must delete)
            return new Driver(*pv);
        }
        // Move to next node
        cur = cur->getNext();
    }

    // Not found → return nullptr
    return nullptr;
}

// 🧹 Removes driver by id (deletes both node and driver)
bool DriversDb::removeById(const int id) {
    Node<Driver*>* prev = nullptr; // Keep track of previous node
    Node<Driver*>* cur = this->drivers; // Start at head node

    // Traverse list
    while (cur != nullptr) {
        // Access stored driver
        // If found driver with matching id
        // ReSharper disable once CppLocalVariableMayBeConst
        if (Driver* pv = cur->getValue(); pv && pv->getId() == id) {
            // If head node, move head pointer
            if (!prev) {
                this->drivers = cur->getNext();
            }else {
                // Else skip the current node
                prev->setNext(cur->getNext());
            }

            delete pv; // Delete the driver object
            delete cur; // Delete the node itself
            this->size--; // Decrease counter

            return true; // Return success
        }
        // Advance both pointers
        prev = cur;
        cur = cur->getNext();
    }

    return false; // Not found
}

// 🧼 Clears the entire list (deletes all nodes and drivers)
void DriversDb::clear() {
    // Start from head
    // ReSharper disable once CppLocalVariableMayBeConst
    Node<Driver*>* cur = this->drivers;

    // Traverse all nodes
    while (cur != nullptr) {
        // Store next pointer
        Node<Driver*>* nxt = cur->getNext();

        delete cur->getValue(); // Delete driver inside node
        delete cur; // Delete the node wrapper
        cur = nxt; // Move to next
    }

    // Reset list to empty
    this->drivers = nullptr;
    this->size = 0;
}

// 📋 Converts all stored drivers to text lines
std::string DriversDb::toString() const {
    std::stringstream ss; // Build string with stringstream

    ss << "Drivers count: " << this->size << "\n"; // Print total count

    const Node<Driver*>* cur = this->drivers; // Pointer to head node

    // Traverse all nodes
    while (cur != nullptr) {
        // If driver exists, print its toString()
        if (const Driver* pv = cur->getValue()) ss << pv->toString() << "\n";
        else ss << "null\n"; // Else print null

        cur = cur->getNext(); // Advance node
    }

    return ss.str(); // Return built string
}

// 🔍 Finds driver matching all given criteria
Driver* DriversDb::matches(const int *ident, const std::string *first_name, const std::string *last_name, const int *age) const {
    // Validate input pointers
    if (!ident || !first_name || !last_name || !age) return nullptr;

    // Start from head
    const Node<Driver*>* pos = this->drivers;

    // Traverse list
    while (pos != nullptr) {
        // Access stored driver pointer
        if (const Driver *current = pos->getValue()) {
            // Extract all fields
            std::string firstname = current->getFirstName();
            std::string lastname = current->getLastName();
            // ReSharper disable once CppLocalVariableMayBeConst
            int driverage = current->getAge();
            // Compare all fields
            // ReSharper disable once CppLocalVariableMayBeConst
            if (int id = current->getId();
                *ident == id && *first_name == firstname && *last_name == lastname && *age == driverage) {
                // Return new deep-copied Driver (caller must delete)
                return new Driver(*current);
            }
        }
        // Move to next node
        pos = pos->getNext();
    }
    // No match found
    return nullptr;
}

