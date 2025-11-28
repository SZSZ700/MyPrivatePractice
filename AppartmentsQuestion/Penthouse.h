#ifndef UNTITLED1_PENTHOUSE_H // Include guard for Penthouse
#define UNTITLED1_PENTHOUSE_H // Define include guard macro

#include "Appartment.h" // Include base class Appartment

// ReSharper disable once CppClassCanBeFinal
class Penthouse : public Appartment {
protected:
    int* numTerr; // Pointer to number of terraces
    int* terraceArea; // Pointer to total terrace area
    bool* seaView; // Pointer to a boolean indicating sea view

public:
    Penthouse(int floorVal, int numAppVal, int areaVal, int numTerrVal, int terraceAreaVal, bool seaViewVal); // Constructor
    ~Penthouse() override; // Destructor

    Penthouse(const Penthouse& other); // Copy constructor
    Penthouse& operator=(const Penthouse& other); // Copy assignment operator

    Penthouse(Penthouse&& other) noexcept; // Move constructor
    Penthouse& operator=(Penthouse&& other) noexcept; // Move assignment operator

    const int* getNumTerr() const; // Getter for number of terraces
    const int* getTerraceArea() const; // Getter for terrace area
    const bool* getSeaView() const; // Getter for sea view flag

    void setNumTerr(int numTerrVal); // Setter for number of terraces
    void setTerraceArea(int terraceAreaVal); // Setter for terrace area
    void setSeaView(bool seaViewVal); // Setter for sea view flag

    string toString() const override; // Override toString to include penthouse details

    int getRealPrice() const override; // calc price
};

#endif // UNTITLED1_PENTHOUSE_H // End of include guard
