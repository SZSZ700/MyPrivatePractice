#include "SpeedCamera.h" // Include the SpeedCamera class header

// =======================
// Constructor
// =======================
SpeedCamera::SpeedCamera(const int *maxspeed, const int *numofroot, const int *code) {
    // Allocate and copy maxspeed if not null, else set to nullptr
    this->maxSpeed = maxspeed ? new int(*maxspeed) : nullptr;
    // Allocate and copy numofroot if not null, else set to nullptr
    this->numOfRoot = numofroot ? new int(*numofroot) : nullptr;
    // Allocate and copy code if not null, else set to nullptr
    this->code = code ? new int(*code) : nullptr;
    // Initialize plates list pointer to nullptr (empty list)
    this->plates = nullptr;
}

// =======================
// Destructor
// =======================
SpeedCamera::~SpeedCamera() {
    delete this->maxSpeed; // Delete dynamically allocated maxSpeed (safe for nullptr)
    delete this->numOfRoot; // Delete dynamically allocated numOfRoot
    delete this->code; // Delete dynamically allocated code

    const Node<string*>* pos = this->plates; // Iterator pointer for traversing the plates list
    while (pos != nullptr) { // Loop until end of list
        const Node<string*>* temp = pos->getNext(); // Store next node before deletion
        delete pos->getValue(); // Delete the string* stored in the node
        delete pos; // Delete the node itself
        pos = temp; // Move to the next node
    }
    delete pos; // pos is nullptr here, delete nullptr is a no-op (redundant but safe)
}

// =======================
// Copy constructor
// =======================
SpeedCamera::SpeedCamera(const SpeedCamera &other)
    : plates(nullptr) { // Initialize plates to nullptr before building the new list
    this->maxSpeed = other.maxSpeed ? new int(*other.maxSpeed) : nullptr;   // Deep copy of maxSpeed
    this->numOfRoot = other.numOfRoot ? new int(*other.numOfRoot) : nullptr; // Deep copy of numOfRoot
    this->code = other.code ? new int(*other.code) : nullptr; // Deep copy of code

    const Node<string *> *pos = other.plates; // Iterator pointer for the other object's plates list

    while (pos != nullptr) { // Traverse the other list
        if (!this->plates) { // If this is the first node in our new list
            // ReSharper disable once CppTemplateArgumentsCanBeDeduced
            this->plates = new Node<string *>(
                new string(pos->getValue()->c_str())); // Allocate a new string and copy its content from other node
        } else { // If list already exists, prepend a new node
            // ReSharper disable once CppTemplateArgumentsCanBeDeduced
            const auto toAdd = new Node<string *>(
                new string(pos->getValue()->c_str())); // Allocate a new string and copy content
            toAdd->setNext(this->plates); // Point the new node to the current head
            this->plates = toAdd; // Update head to the new node
        }
        pos = pos->getNext(); // Move to the next node in the other list
    }
}

// =======================
// Copy assignment operator
// =======================
SpeedCamera& SpeedCamera::operator=(const SpeedCamera &other) {
    if (this != &other) { // Protect against self-assignment
        this->~SpeedCamera(); // Explicitly call destructor to free current resources

        this->maxSpeed = other.maxSpeed ? new int(*other.maxSpeed) : nullptr; // Deep copy maxSpeed
        this->numOfRoot = other.numOfRoot ? new int(*other.numOfRoot) : nullptr; // Deep copy numOfRoot
        this->code = other.code ? new int(*other.code) : nullptr; // Deep copy code

        const Node<string *> *pos = other.plates; // Iterator pointer for other plates list

        while (pos != nullptr) { // Traverse the other plates list
            if (!this->plates) { // If this is the first node in our list
                // ReSharper disable once CppTemplateArgumentsCanBeDeduced
                this->plates = new Node<string *>(
                    new string(*pos->getValue())); // Allocate and copy the string from other node
            } else { // If list already exists
                // ReSharper disable once CppTemplateArgumentsCanBeDeduced
                const auto toAdd = new Node<string *>(
                    new string(*pos->getValue())); // Allocate and copy the string
                toAdd->setNext(this->plates); // Link the new node before current head
                this->plates = toAdd; // Update head pointer
            }
            pos = pos->getNext(); // Move to the next node
        }
    }

    return *this; // Return reference to this to allow chaining
}

// =======================
// Move constructor (fixed – no double delete)
// =======================
SpeedCamera::SpeedCamera(SpeedCamera &&other) noexcept {
    this->maxSpeed = other.maxSpeed; // Steal maxSpeed pointer from other
    this->numOfRoot = other.numOfRoot; // Steal numOfRoot pointer from other
    this->code = other.code; // Steal code pointer from other
    this->plates = other.plates; // Steal plates list head pointer from other

    other.maxSpeed = nullptr; // Reset other.maxSpeed to avoid double delete
    other.numOfRoot = nullptr; // Reset other.numOfRoot
    other.code = nullptr; // Reset other.code
    other.plates = nullptr; // Reset other.plates
}

// =======================
// Move assignment operator (fixed – no destructor call, no double delete)
// =======================
SpeedCamera& SpeedCamera::operator=(SpeedCamera &&other) noexcept {
    if (this != &other) {                                       // Protect against self-assignment
        // Release current resources of this object

        delete this->maxSpeed; // Delete current maxSpeed
        delete this->numOfRoot; // Delete current numOfRoot
        delete this->code; // Delete current code

        const Node<string*>* pos = this->plates; // Iterator pointer for current plates list
        while (pos != nullptr) { // Traverse and delete current list
            const Node<string*>* temp = pos->getNext(); // Store next node
            delete pos->getValue(); // Delete stored string*
            delete pos; // Delete node itself
            pos = temp; // Move to next node
        }

        // Steal resources from other
        this->maxSpeed = other.maxSpeed; // Take over other.maxSpeed pointer
        this->numOfRoot = other.numOfRoot; // Take over other.numOfRoot pointer
        this->code = other.code; // Take over other.code pointer
        this->plates = other.plates; // Take over other.plates pointer

        // Reset other to a safe, empty state
        other.maxSpeed = nullptr; // Other no longer owns maxSpeed
        other.numOfRoot = nullptr; // Other no longer owns numOfRoot
        other.code = nullptr; // Other no longer owns code
        other.plates = nullptr; // Other no longer owns plates list
    }

    return *this; // Return reference to this object
}

// =======================
// Getters
// =======================
const Node<string*>* SpeedCamera::getPlates() const {
    return this->plates; // Return head pointer of plates list (can be nullptr)
}

const int* SpeedCamera::getMaxSpeed() const {
    return this->maxSpeed; // Return pointer to maxSpeed
}

const int* SpeedCamera::getNumOfRoot() const {
    return this->numOfRoot; // Return pointer to numOfRoot
}

const int* SpeedCamera::getCode() const {
    return this->code; // Return pointer to code
}

// =======================
// Setters
// =======================
void SpeedCamera::setPlates(const Node<string*> *platess) {
    const Node<string*>* del = this->plates; // Iterator pointer for current plates list
    while (del != nullptr) { // Traverse current list
        const Node<string*>* temp = del->getNext(); // Store next node
        delete del->getValue(); // Delete stored string*
        delete del; // Delete node
        del = temp; // Move to next node
    }
    delete del; // del is nullptr here – delete nullptr is a no-op

    this->plates = nullptr; // Reset plates to nullptr before building new list

    const Node<string *> *pos = platess; // Iterator pointer for source plates list

    while (pos != nullptr) { // Traverse source list
        if (!this->plates) { // If this is the first node in the new list
            // ReSharper disable once CppTemplateArgumentsCanBeDeduced
            this->plates = new Node<string *>(
                new string(*pos->getValue())); // Allocate and copy the string from source node
        } else { // If list already exists
            // ReSharper disable once CppTemplateArgumentsCanBeDeduced
            const auto toAdd = new Node<string *>(
                new string(*pos->getValue())); // Allocate and copy the string
            toAdd->setNext(this->plates); // Link new node before current head
            this->plates = toAdd; // Update head to new node
        }
        pos = pos->getNext(); // Move to next source node
    }
}

void SpeedCamera::setMaxSpeed(const int *maxspeed) {
    delete this->maxSpeed; // Delete current maxSpeed
    // Allocate and copy new value if not null, else nullptr
    this->maxSpeed = maxspeed ? new int(*maxspeed) : nullptr;
}

void SpeedCamera::setNumOfRoot(const int *numofroot) {
    delete this->numOfRoot; // Delete current numOfRoot
    // Allocate and copy new value if not null, else nullptr
    this->numOfRoot = numofroot ? new int(*numofroot) : nullptr;
}

void SpeedCamera::setCode(const int *codee) {
    delete this->code; // Delete current code
    // Allocate and copy new value if not null, else nullptr
    this->code = codee ? new int(*codee) : nullptr;
}

// =======================
// toString
// =======================
std::string SpeedCamera::toString() const {
    std::stringstream ss; // String stream to build the output

    // Check that all pointers are valid
    if (this->maxSpeed && this->numOfRoot && this->code && this->plates) {
        ss << "code: " << this->code // Append pointer address of code
           << ", numOfRoot: " << this->numOfRoot // Append pointer address of numOfRoot
           << ", maxSpeed: " << this->maxSpeed // Append pointer address of maxSpeed
           << ", numOfRoot: " << this->numOfRoot // Append numOfRoot again (likely redundant)
           << "plates: \n"; // Start plates section

        const Node<string *> *pos = this->plates; // Iterator pointer for plates list
        int i = 0; // Index counter for plates
        while (pos) { // Traverse the list
            if (pos->getValue()) { // Ensure stored string* is not null
                ss << ", plate - " << i // Append plate index
                   << ": " << pos->getValue(); // Append pointer value of string* (address, not content)
                i++; // Increment plate index
            }
            pos = pos->getNext(); // Move to next node in list
        }
    }

    return ss.str(); // Return the built string
}

// Adds a car plate to the list if the given speed is above the allowed maximum speed
void SpeedCamera::addCar(const std::string &plateNumber, int carSpeed) {
    // Check that maxSpeed is not null and the car speed is higher than the allowed maximum speed
    if (this->maxSpeed && carSpeed > *this->maxSpeed) {
        // Allocate a new string on the heap that holds the given plate number
        string* newPlate = new string(plateNumber);
        // Allocate a new Node that stores the pointer to the newPlate string
        Node<string*>* newNode = new Node<string*>(newPlate);
        // Set the next pointer of the new node to the current head of the plates list
        newNode->setNext(this->plates);
        // Update the head of the plates list to point to the new node
        this->plates = newNode;
    }
}
