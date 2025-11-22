#include "SpeedCameraNetwork.h" // Include the SpeedCameraNetwork header
#include <iostream> // For std::cout and std::endl

// =======================
// Constructor
// =======================
SpeedCameraNetwork::SpeedCameraNetwork(const int* maxCamerasPtr) {
    // If a maxCameras pointer is provided, deep copy its value
    if (maxCamerasPtr) { // Check if the given pointer is not null
        this->maxCameras = new int(*maxCamerasPtr); // Allocate and copy the max cameras value
    } else { // If no value is provided
        this->maxCameras = new int(100); // Default to 100 cameras (as described in the question)
    }

    // Initialize currentCameras to zero (no cameras yet)
    this->currentCameras = new int(0); // Allocate and set currentCameras to 0

    // Initialize the cameras list head pointer to nullptr (empty list)
    this->cameras = nullptr; // No cameras in the network at construction
}

// =======================
// Destructor
// =======================
SpeedCameraNetwork::~SpeedCameraNetwork() {
    // Delete dynamically allocated maxCameras integer
    delete this->maxCameras; // Free the maxCameras integer (safe if not null)

    // Delete dynamically allocated currentCameras integer
    delete this->currentCameras; // Free the currentCameras integer

    // Pointer used to traverse the linked list of cameras
    const Node<SpeedCamera*>* pos = this->cameras; // Start from the head of the list

    // Traverse the list and delete each camera and node
    while (pos != nullptr) { // Continue until the end of the list
        // Store the next node before deleting the current one
        const Node<SpeedCamera*>* temp = pos->getNext();
        delete pos->getValue(); // Delete the SpeedCamera* stored inside the node
        delete pos; // Delete the node itself
        pos = temp; // Move to the next node
    }
}

// =======================
// Copy constructor
// =======================
SpeedCameraNetwork::SpeedCameraNetwork(const SpeedCameraNetwork& other)
    : maxCameras(nullptr), // Initialize maxCameras to nullptr before assigning
      currentCameras(nullptr), // Initialize currentCameras to nullptr before assigning
      cameras(nullptr) { // Initialize cameras list head to nullptr

    // Deep copy of maxCameras integer
    this->maxCameras = other.maxCameras
        ? new int(*other.maxCameras)                   // If other has maxCameras, copy its value
        : nullptr;                                     // Otherwise set to nullptr

    // Deep copy of currentCameras integer
    this->currentCameras = other.currentCameras
        ? new int(*other.currentCameras)               // If other has currentCameras, copy its value
        : nullptr;                                     // Otherwise set to nullptr

    // Pointer used to traverse the other network's cameras list
    const Node<SpeedCamera*>* pos = other.cameras;     // Start from the head of other's list

    // Traverse the other list and deep copy each SpeedCamera
    while (pos != nullptr) {                           // Continue until the end of the other list
        // Get the SpeedCamera* from the current node of other
        const SpeedCamera* otherCamPtr = pos->getValue();    // Retrieve the SpeedCamera pointer from the node

        // Create a deep copy of the SpeedCamera if not null
        SpeedCamera* newCamPtr = nullptr; // Initialize new camera pointer to nullptr
        if (otherCamPtr) { // Check that the other camera pointer is not null
            // Use SpeedCamera copy constructor to deep copy the camera
            newCamPtr = new SpeedCamera(*otherCamPtr);
        }

        // Create a new node to hold the copied camera pointer
        // Allocate a new node with the new camera pointer
        // ReSharper disable once CppTemplateArgumentsCanBeDeduced
        const auto newNode = new Node<SpeedCamera*>(newCamPtr);

        // Insert the new node at the head of the list
        newNode->setNext(this->cameras); // Point the new node to the current head
        this->cameras = newNode; // Update the head pointer to the new node

        // Move to the next node in the other list
        pos = pos->getNext(); // Advance to the next node in other's list
    }

    // If we successfully copied the list, we can also update currentCameras to match the count
    if (this->currentCameras) { // Check that currentCameras pointer is valid
        // Reset the count and recount the nodes in the new list
        *this->currentCameras = 0; // Start counting from zero

        const Node<SpeedCamera*>* countPos = this->cameras; // Start from the head of our new list
        while (countPos != nullptr) { // Traverse until the end
            (*this->currentCameras)++; // Increment the counter for each node
            countPos = countPos->getNext(); // Move to the next node
        }
    }
}

// =======================
// Copy assignment operator
// =======================
SpeedCameraNetwork& SpeedCameraNetwork::operator=(const SpeedCameraNetwork& other) {
    // Protect against self-assignment
    if (this != &other) { // Check if assigning to a different object
        // First, release current resources
        // Delete current maxCameras integer
        delete this->maxCameras; // Free previously allocated maxCameras

        // Delete current currentCameras integer
        delete this->currentCameras; // Free previously allocated currentCameras

        // Delete the entire cameras list
        const Node<SpeedCamera*>* pos = this->cameras; // Start from the head of the list
        while (pos != nullptr) { // Traverse until the end
            const Node<SpeedCamera*>* temp = pos->getNext(); // Store next node
            delete pos->getValue(); // Delete the SpeedCamera* stored in the node
            delete pos; // Delete the node itself
            pos = temp; // Move to the next node
        }
        // After this loop, this->cameras is invalid, so set it to nullptr
        this->cameras = nullptr; // Reset cameras head pointer to nullptr

        // Now, deep copy from other
        // Deep copy maxCameras integer
        this->maxCameras = other.maxCameras
            ? new int(*other.maxCameras) // Copy value if other has one
            : nullptr; // Otherwise set to nullptr

        // Deep copy currentCameras integer
        this->currentCameras = other.currentCameras
            ? new int(*other.currentCameras) // Copy value if other has one
            : nullptr; // Otherwise set to nullptr

        // Pointer to traverse the other cameras list
        const Node<SpeedCamera*>* otherPos = other.cameras; // Start from other's head

        // Traverse the other list and deep copy each camera
        while (otherPos != nullptr) { // Continue until the end of the list
            // Retrieve the SpeedCamera pointer from the other node
            const SpeedCamera* otherCamPtr = otherPos->getValue(); // Get SpeedCamera* from other

            // Deep copy the SpeedCamera using its copy constructor
            SpeedCamera* newCamPtr = nullptr; // Initialize new camera pointer to nullptr
            if (otherCamPtr) { // Check that pointer is not null
                newCamPtr = new SpeedCamera(*otherCamPtr); // Allocate and copy the camera
            }

            // Create a new node to store the new camera pointer
            auto newNode = new Node(newCamPtr); // Allocate a new node

            // Insert the new node at the head of the list
            newNode->setNext(this->cameras); // Point new node to the current head
            this->cameras = newNode; // Update head to the new node

            // Move to the next node in the other list
            otherPos = otherPos->getNext(); // Advance to next node
        }

        // Recalculate currentCameras based on the new list
        if (this->currentCameras) { // Check that currentCameras is valid
            *this->currentCameras = 0; // Reset the counter
            const Node<SpeedCamera*>* countPos = this->cameras; // Start from head
            while (countPos != nullptr) { // Traverse the list
                (*this->currentCameras)++; // Increment count for each node
                countPos = countPos->getNext(); // Move to the next node
            }
        }
    }

    // Return reference to this object to allow assignment chaining
    return *this; // Return *this
}

// =======================
// Move constructor
// =======================
SpeedCameraNetwork::SpeedCameraNetwork(SpeedCameraNetwork&& other) noexcept {
    // Steal the maxCameras pointer from the other object
    this->maxCameras = other.maxCameras; // Take ownership of other's maxCameras pointer

    // Steal the currentCameras pointer from the other object
    this->currentCameras = other.currentCameras; // Take ownership of other's currentCameras pointer

    // Steal the cameras list head pointer from the other object
    this->cameras = other.cameras; // Take ownership of other's cameras list

    // Reset the other object's pointers to nullptr to avoid double delete
    other.maxCameras = nullptr; // Other no longer owns maxCameras
    other.currentCameras = nullptr; // Other no longer owns currentCameras
    other.cameras = nullptr; // Other no longer owns cameras list
}

// =======================
// Move assignment operator
// =======================
SpeedCameraNetwork& SpeedCameraNetwork::operator=(SpeedCameraNetwork&& other) noexcept {
    // Protect against self-assignment
    if (this != &other) { // Check that this and other are different objects
        // First, release current resources
        // Delete current maxCameras integer
        delete this->maxCameras; // Free existing maxCameras

        // Delete current currentCameras integer
        delete this->currentCameras; // Free existing currentCameras

        // Delete current cameras list
        const Node<SpeedCamera*>* pos = this->cameras; // Start from the current head
        while (pos != nullptr) { // Traverse entire list
            const Node<SpeedCamera*>* temp = pos->getNext(); // Store next node
            delete pos->getValue(); // Delete SpeedCamera* stored in the node
            delete pos; // Delete the node itself
            pos = temp; // Move to the next node
        }
        // Reset head pointer to nullptr after deletion
        this->cameras = nullptr; // No cameras left after cleanup

        // Now, steal resources from other
        // Steal maxCameras pointer
        this->maxCameras = other.maxCameras; // Take ownership of other's maxCameras

        // Steal currentCameras pointer
        this->currentCameras = other.currentCameras; // Take ownership of other's currentCameras

        // Steal cameras list head pointer
        this->cameras = other.cameras; // Take ownership of other's cameras list

        // Reset other object's pointers to nullptr to avoid double deletion
        other.maxCameras = nullptr; // Other no longer owns maxCameras
        other.currentCameras = nullptr; // Other no longer owns currentCameras
        other.cameras = nullptr; // Other no longer owns cameras list
    }

    // Return reference to this object for assignment chaining
    return *this; // Return *this
}

// =======================
// Getters
// =======================
const int* SpeedCameraNetwork::getMaxCameras() const {
    // Return const pointer to the maxCameras value
    return this->maxCameras; // Expose maxCameras as a const pointer
}

const int* SpeedCameraNetwork::getCurrentCameras() const {
    // Return const pointer to the currentCameras value
    return this->currentCameras; // Expose currentCameras as a const pointer
}

const Node<SpeedCamera*>* SpeedCameraNetwork::getCameras() const {
    // Return const pointer to the head of the cameras linked list
    return this->cameras; // Expose the head of the cameras list
}

// =======================
// Setters
// =======================
void SpeedCameraNetwork::setMaxCameras(const int* maxCamerasPtr) {
    // Delete the existing maxCameras integer
    delete this->maxCameras; // Free existing value of maxCameras

    // Deep copy the new maxCameras value or set to nullptr
    this->maxCameras = maxCamerasPtr
        ? new int(*maxCamerasPtr) // Allocate and copy the new value
        : nullptr; // Set to nullptr if incoming pointer is null
}

void SpeedCameraNetwork::setCurrentCameras(const int* currentCamerasPtr) {
    // Delete the existing currentCameras integer
    delete this->currentCameras; // Free existing value of currentCameras

    // Deep copy the new currentCameras value or set to nullptr
    this->currentCameras = currentCamerasPtr
        ? new int(*currentCamerasPtr) // Allocate and copy the new value
        : nullptr; // Set to nullptr if incoming pointer is null
}

void SpeedCameraNetwork::setCameras(const Node<SpeedCamera*>* camerasHead) {
    // First, delete the existing cameras list
    const Node<SpeedCamera*>* del = this->cameras; // Start from current head of the list
    while (del != nullptr) { // Traverse until the end
        const Node<SpeedCamera*>* temp = del->getNext(); // Store next node
        delete del->getValue(); // Delete the SpeedCamera* stored in the node
        delete del; // Delete the node itself
        del = temp; // Move to the next node
    }
    // Reset the head pointer to nullptr after deletion
    this->cameras = nullptr; // No cameras after cleanup

    // Reset currentCameras counter to zero if it exists
    if (this->currentCameras) { // Check if pointer is valid
        *this->currentCameras = 0; // Reset camera count
    }

    // Now, deep copy the new list from camerasHead
    const Node<SpeedCamera*>* pos = camerasHead; // Iterator pointer for the source list

    // Traverse the source list and copy each camera
    while (pos != nullptr) { // Continue until the end of the source list
        // Retrieve the SpeedCamera pointer from the current node
        const SpeedCamera* srcCamPtr = pos->getValue(); // Get SpeedCamera* from the source node

        // Deep copy the SpeedCamera object if not null
        SpeedCamera* newCamPtr = nullptr; // Initialize new camera pointer to nullptr
        if (srcCamPtr) { // Check that source camera pointer is not null
            newCamPtr = new SpeedCamera(*srcCamPtr); // Allocate and copy the camera
        }

        // Create a new node for the copied camera pointer
        const auto newNode = new Node(newCamPtr); // Allocate new node

        // Insert the new node at the head of the list
        newNode->setNext(this->cameras); // Link new node before the current head
        this->cameras = newNode; // Update head pointer to the new node

        // Increment currentCameras counter if it exists
        if (this->currentCameras) { // Check that currentCameras exists
            (*this->currentCameras)++; // Increment the cameras count
        }

        // Move to the next node in the source list
        pos = pos->getNext(); // Advance to the next node
    }
}

// =======================
// toString
// =======================
std::string SpeedCameraNetwork::toString() const {
    // Create a stringstream to build the final string
    std::stringstream ss; // String stream used to accumulate text

    // Print summary of maxCameras and currentCameras
    ss << "SpeedCameraNetwork("; // Start of network description
    if (this->maxCameras) { // If maxCameras is not null
        ss << "maxCameras=" << *this->maxCameras; // Print the maximum number of cameras
    } else { // If maxCameras is null
        ss << "maxCameras=null"; // Indicate that maxCameras is null
    }

    ss << ", "; // Separator between fields

    if (this->currentCameras) { // If currentCameras is not null
        ss << "currentCameras=" << *this->currentCameras; // Print the current number of cameras
    } else { // If currentCameras is null
        ss << "currentCameras=null"; // Indicate that currentCameras is null
    }

    ss << ")\n"; // Close the summary line

    // Print details of each camera in the network
    const Node<SpeedCamera*>* pos = this->cameras; // Start from the head of the cameras list
    int index = 0; // Index for numbering cameras

    // Traverse the list and print each camera
    while (pos != nullptr) { // Continue until the end of the list
        const SpeedCamera* camPtr = pos->getValue(); // Get SpeedCamera* from the node
        ss << "Camera #" << index << ": "; // Print camera index label

        if (camPtr) { // If the camera pointer is not null
            ss << camPtr->toString(); // Call SpeedCamera::toString() to print its details
        } else { // If the camera pointer is null
            ss << "null camera"; // Indicate that this node has a null camera pointer
        }

        ss << "\n"; // Newline after each camera
        pos = pos->getNext(); // Move to the next node
        index++; // Increment the camera index
    }

    // Return the final string built by the stringstream
    return ss.str(); // Convert the stringstream to std::string and return
}

void SpeedCameraNetwork::addSpeedCamera(const SpeedCamera *sc) { // Adds a new speed camera to the network if there is free capacity
    if (!sc) return; // If the given camera pointer is null, do nothing
    if (!this->maxCameras || !this->currentCameras) return; // If counters are not initialized, do nothing
    if (*this->currentCameras >= *this->maxCameras) return; // If the network reached its maximum capacity, do nothing

    const auto newCam = new SpeedCamera(*sc); // Allocate a new SpeedCamera by copying the given camera
    const auto newNode = new Node(newCam); // Create a new node that stores the SpeedCamera pointer

    newNode->setNext(this->cameras); // Link the new node before the current head of the list
    this->cameras = newNode; // Update the head pointer to the new node

    (*this->currentCameras)++; // Increase the number of cameras stored in the network
}

void SpeedCameraNetwork::printHighEnforcementRoads() const { // Prints road numbers that require high enforcement (more than 200 violations)
    const Node<SpeedCamera*>* outer = this->cameras; // Start from the head of the cameras list

    while (outer) { // Traverse each camera in the network
        const SpeedCamera* cam = outer->getValue(); // Get the SpeedCamera* stored in the current node

        if (!cam || !cam->getNumOfRoot()) { // If the camera or its road number pointer is null, skip this node
            outer = outer->getNext(); // Move to the next camera node
            continue; // Continue with the next iteration of the outer loop
        }

        const int roadNumber = *cam->getNumOfRoot(); // Read the road number value from the camera
        bool alreadyProcessed = false; // Flag that indicates whether this road was already processed

        const Node<SpeedCamera*>* check = this->cameras; // Start checking from the head of the list

        while (check != outer) { // Only inspect nodes before the current 'outer' node
            // ReSharper disable once CppTooWideScopeInitStatement
            const SpeedCamera* prevCam = check->getValue(); // Get the SpeedCamera* from the previous node

            // If we already saw this road number
            if (prevCam && prevCam->getNumOfRoot() && *prevCam->getNumOfRoot() == roadNumber) {
                alreadyProcessed = true; // Mark this road as already processed
                break; // Stop searching in the previous nodes
            }
            check = check->getNext(); // Move to the next previous node
        }

        if (alreadyProcessed) { // If this road was already handled earlier
            outer = outer->getNext(); // Move to the next camera node
            continue; // Skip aggregation for this road
        }

        int totalViolations = 0; // Counter for total violations on this road
        const Node<SpeedCamera*>* inner = this->cameras; // Start a full traversal over all cameras

        while (inner) { // Traverse the full cameras list
            // ReSharper disable once CppTooWideScopeInitStatement
            const SpeedCamera* currentCam = inner->getValue(); // Get the SpeedCamera* stored in the current node

            if (currentCam && currentCam->getNumOfRoot() && *currentCam->getNumOfRoot() == roadNumber) { // If this camera is on the same road
                const Node<string*>* plateNode = currentCam->getPlates(); // Get the head of the plates list for this camera

                while (plateNode) { // Traverse all plates (violations) for this camera
                    totalViolations++; // Increase the total violations counter
                    plateNode = plateNode->getNext(); // Move to the next plate node
                }
            }

            inner = inner->getNext(); // Move to the next camera node
        }

        if (totalViolations > 200) { // If the total number of violations on this road is more than 200
            std::cout << roadNumber << std::endl; // Print the road number that requires high enforcement
        }

        outer = outer->getNext(); // Move to the next camera in the outer loop
    }
}

bool SpeedCameraNetwork::checkCar(const std::string& plateNumber) const { // Checks if the given car plate was recorded speeding in the network
    Node<int>* cameraCodes = nullptr; // Dynamic linked list that stores all camera codes that recorded this plate
    const Node<SpeedCamera*>* camNode = this->cameras; // Start from the head of the cameras list

    while (camNode) { // Traverse all camera nodes in the network
        // ReSharper disable once CppTooWideScopeInitStatement
        const SpeedCamera* cam = camNode->getValue(); // Get the SpeedCamera* stored in the current node

        if (cam && cam->getCode()) { // Make sure the camera and its code pointer are not null
            const Node<std::string*>* plateNode = cam->getPlates(); // Get the head of the plates list for this camera
            bool cameraAlreadyAdded = false; // Indicates whether this camera has already been added to the dynamic list

            while (plateNode && !cameraAlreadyAdded) { // Traverse plates for this camera until we either find a match or reach the end
                // ReSharper disable once CppTooWideScopeInitStatement
                const std::string* platePtr = plateNode->getValue(); // Get the pointer to the plate string in the current node

                // Check if the stored plate matches the requested plate
                if (platePtr && *platePtr == plateNumber) {
                    // Read the camera code value from the camera
                    const int camCodeValue = *cam->getCode();

                    // Allocate a new node that stores this camera code
                    // ReSharper disable once CppDFAMemoryLeak
                    const auto newNode = new Node(camCodeValue);

                    // Insert the new node at the head of the dynamic list
                    newNode->setNext(cameraCodes);
                    // Update the head pointer of the dynamic list
                    cameraCodes = newNode;
                    // Mark that this camera was already added
                    cameraAlreadyAdded = true;
                }

                plateNode = plateNode->getNext(); // Move to the next plate node
            }
        }

        camNode = camNode->getNext(); // Move to the next camera node in the network
    }

    int count = 0; // Counter for how many cameras recorded this plate
    const Node<int>* countNode = cameraCodes; // Pointer used to count the nodes in the dynamic list
    while (countNode) { // Traverse the dynamic list
        count++; // Increase the counter for each node
        countNode = countNode->getNext(); // Move to the next node
    }

    bool result = false; // Final return value

    if (count == 0) { // No camera recorded this plate
        result = false; // Car was not recorded speeding
    } else if (count == 1) { // Exactly one camera recorded this plate
        result = true; // Car was recorded speeding, but only in one camera, so we do not print anything
    } else { // More than one camera recorded this plate
        const Node<int>* printNode = cameraCodes; // Pointer used to print all camera codes
        while (printNode) { // Traverse the dynamic list again
            std::cout << printNode->getValue() << std::endl; // Print the camera code that recorded the plate
            printNode = printNode->getNext(); // Move to the next node
        }
        result = true; // Car was recorded speeding (in multiple cameras)
    }

    const Node<int>* del = cameraCodes; // Pointer used to free the dynamic list
    while (del) { // Traverse the dynamic list and delete all nodes
        const Node<int>* temp = del->getNext(); // Store the next node before deleting the current one
        delete del; // Delete the current node
        del = temp; // Move to the next node
    }

    return result; // Return true if the car was recorded speeding in at least one camera, otherwise false
}

