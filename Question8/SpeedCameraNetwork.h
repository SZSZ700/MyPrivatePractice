#ifndef UNTITLED1_SPEEDCAMERANETWORK_H           // Include guard start to prevent multiple inclusion
#define UNTITLED1_SPEEDCAMERANETWORK_H           // Define the include guard macro

#include "..//Node/Node.h"                       // Include the Node template for the linked list
#include "SpeedCamera.h"         // Include the SpeedCamera class
#include <string>                                // Include std::string
// ReSharper disable once CppUnusedIncludeDirective
#include <sstream>                               // Include std::stringstream
using namespace std;                             // Use the std namespace

// Class that represents a daily network of speed cameras (up to 100 cameras)
class SpeedCameraNetwork {
    // Pointer to the maximum number of cameras allowed in the network (usually 100)
    int* maxCameras;

    // Pointer to the current number of cameras actually stored in the network
    int* currentCameras;

    // Pointer to the head of a linked list of SpeedCamera* (each node holds a SpeedCamera pointer)
    Node<SpeedCamera*>* cameras;

public:
    // Constructor that allows passing a maxCameras value; if null, defaults to 100
    explicit SpeedCameraNetwork(const int* maxCamerasPtr = nullptr);

    // Destructor that releases all dynamically allocated resources
    ~SpeedCameraNetwork();

    // Copy constructor that performs a deep copy of all cameras and counters
    SpeedCameraNetwork(const SpeedCameraNetwork& other);

    // Copy assignment operator that releases old data and deep copies from other
    SpeedCameraNetwork& operator=(const SpeedCameraNetwork& other);

    // Move constructor that steals all resources from another network
    SpeedCameraNetwork(SpeedCameraNetwork&& other) noexcept;

    // Move assignment operator that releases current resources and steals from other
    SpeedCameraNetwork& operator=(SpeedCameraNetwork&& other) noexcept;

    // Getter that returns a const pointer to the maxCameras value
    const int* getMaxCameras() const;

    // Getter that returns a const pointer to the currentCameras value
    const int* getCurrentCameras() const;

    // Getter that returns a const pointer to the head of the cameras list
    const Node<SpeedCamera*>* getCameras() const;

    // Setter that sets maxCameras using a deep copy
    void setMaxCameras(const int* maxCamerasPtr);

    // Setter that sets currentCameras using a deep copy (logic control is up to the caller)
    void setCurrentCameras(const int* currentCamerasPtr);

    // Setter that replaces the entire cameras list with a deep copy of another list
    void setCameras(const Node<SpeedCamera*>* camerasHead);

    // Converts the whole network to a string (summary of cameras and their data)
    std::string toString() const;

    // Adds a new speed camera using a pointer (performs deep copy, does not take ownership of the given pointer)
    void addSpeedCamera(const SpeedCamera *sc);

    // Prints road numbers that require high enforcement (more than 200 violations)
    void printHighEnforcementRoads() const;

    // Checks if the given car plate was recorded speeding; prints camera codes if found
    bool checkCar(const std::string& plateNumber) const;
};

#endif //UNTITLED1_SPEEDCAMERANETWORK_H           // End of the include guard
