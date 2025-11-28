#ifndef UNTITLED1_APPARTMENT_H
#define UNTITLED1_APPARTMENT_H

#include <string> // For std::string
using namespace std; // Use std namespace

// ReSharper disable once CppClassCanBeFinal
class Appartment {
protected:
    string* owner; // Pointer to the owner's name
    int* floor; // Pointer to the floor number
    int* numApp; // Pointer to the apartment number
    int* area; // Pointer to the apartment area in square meters

    static const int COST_APP; // Price per square meter of apartment
    static const int COST_TERRACE; // Price per square meter of terrace
    static const int COST_GARDEN; // Price per square meter of garden

public:
    Appartment(int floorVal, int numAppVal, int areaVal); // Constructor that creates a free apartment ready for sale
    virtual ~Appartment(); // Virtual destructor to allow polymorphic deletion

    Appartment(const Appartment& other); // Copy constructor (deep copy)
    Appartment& operator=(const Appartment& other); // Copy assignment operator (deep copy)

    Appartment(Appartment&& other) noexcept; // Move constructor
    Appartment& operator=(Appartment&& other) noexcept; // Move assignment operator

    const string* getOwner() const; // Getter for owner
    const int* getFloor() const; // Getter for floor
    const int* getNumApp() const; // Getter for apartment number
    const int* getArea() const; // Getter for apartment area

    void setOwner(const string& name); // Setter for owner
    void setFloor(int floorVal); // Setter for floor
    void setNumApp(int numAppVal); // Setter for apartment number
    void setArea(int areaVal); // Setter for area

    virtual string toString() const; // Virtual toString for printing apartment details

    // calc price
    virtual int getRealPrice() const;
};

#endif //UNTITLED1_APPARTMENT_H
