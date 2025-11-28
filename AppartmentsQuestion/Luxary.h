//UNTITLED1_
#ifndef LUXARY_H // Include guard for Luxary
#define LUXARY_H // Define include guard macro

#include "Appartment.h" // Include base class Appartment

// ReSharper disable once CppClassCanBeFinal
class Luxary : public Appartment {
protected:
    int* terraceArea; // Pointer to terrace area (mini-penthouse)

public:
    Luxary(int floorVal, int numAppVal, int areaVal, int terraceAreaVal); // Constructor
    ~Luxary() override; // Destructor

    Luxary(const Luxary& other); // Copy constructor
    Luxary& operator=(const Luxary& other); // Copy assignment operator

    Luxary(Luxary&& other) noexcept; // Move constructor
    Luxary& operator=(Luxary&& other) noexcept; // Move assignment operator

    const int* getTerraceArea() const; // Getter for terrace area
    void setTerraceArea(int terraceAreaVal); // Setter for terrace area

    string toString() const override; // Override toString to include terrace details
};

#endif // LUXARY_H // End of include guard
