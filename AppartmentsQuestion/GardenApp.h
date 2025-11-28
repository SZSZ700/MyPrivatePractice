#ifndef UNTITLED1_GARDENAPP_H // Include guard for GardenApp
#define UNTITLED1_GARDENAPP_H // Define include guard macro

#include "Appartment.h" // Include base class Appartment

// ReSharper disable once CppClassCanBeFinal
class GardenApp : public Appartment {
protected:
    int* gardenArea; // Pointer to garden area in square meters

public:
    GardenApp(int floorVal, int numAppVal, int areaVal, int gardenAreaVal); // Constructor for garden apartment
    ~GardenApp() override; // Destructor

    GardenApp(const GardenApp& other); // Copy constructor
    GardenApp& operator=(const GardenApp& other); // Copy assignment operator

    GardenApp(GardenApp&& other) noexcept; // Move constructor
    GardenApp& operator=(GardenApp&& other) noexcept; // Move assignment operator

    const int* getGardenArea() const; // Getter for garden area
    void setGardenArea(int gardenAreaVal); // Setter for garden area

    string toString() const override; // Override toString to include garden details

    int getRealPrice() const override; // Returns the total price including the garden area
};

#endif // UNTITLED1_GARDENAPP_H
