#ifndef UNTITLED1_SPEEDCAMERA_H               // Include guard start to prevent multiple inclusion
#define UNTITLED1_SPEEDCAMERA_H               // Define the include guard macro

#include "..//Node/Node.h"                    // Include the Node template class
using namespace std;                          // Use the std namespace (for string, std::string, etc.)

// Class that represents a speed camera with a list of violating license plates
class SpeedCamera {
    int *code;                                // Pointer to the camera code (stored on the heap)
    int *numOfRoot;                           // Pointer to the road number that the camera covers
    int *maxSpeed;                            // Pointer to the maximum allowed speed in this area
    Node<string*>* plates;                    // Pointer to the head of a linked list of violating plate numbers

public:
    // Constructor – allocates and copies the given values (if not null)
    SpeedCamera(const int *maxspeed, const int *numofroot, const int *code);

    // Destructor – frees all dynamically allocated memory
    ~SpeedCamera();

    // Copy constructor – performs a deep copy of all fields (including the plates list)
    SpeedCamera(const SpeedCamera &other);

    // Copy assignment operator – deep copy assignment (releases old data, copies new data)
    SpeedCamera &operator=(const SpeedCamera &other);

    // Move constructor – steals resources from another SpeedCamera and leaves it empty
    SpeedCamera(SpeedCamera &&other) noexcept;

    // Move assignment operator – frees current resources and steals from another SpeedCamera
    SpeedCamera &operator=(SpeedCamera &&other) noexcept;

    // Getters – return const pointers to prevent external modification
    const Node<string*>* getPlates() const;   // Get the head of the plates list
    const int* getMaxSpeed() const;           // Get pointer to maxSpeed
    const int* getNumOfRoot() const;          // Get pointer to numOfRoot
    const int* getCode() const;               // Get pointer to code

    // Setters – update values with deep copies where needed
    void setPlates(const Node<string*> *plates); // Set the plates list with a deep copy
    void setMaxSpeed(const int *maxspeed);       // Set maxSpeed with deep copy
    void setNumOfRoot(const int *numofroot);     // Set numOfRoot with deep copy
    void setCode(const int *codee);              // Set code with deep copy

    // Convert the object to a string representation
    std::string toString() const;

    // Adds a car to the plates list if its speed is above the allowed maximum speed
    void addCar(const std::string &plateNumber, const int carSpeed);
};

#endif //UNTITLED1_SPEEDCAMERA_H               // End of the include guard
