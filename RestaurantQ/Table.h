#ifndef UNTITLED1_TABLE_H
#define UNTITLED1_TABLE_H
// Include the string header for std::string
#include <string>

// Use the std namespace only for std::string
using std::string;

// Class that represents a table in the restaurant
class Table {
private:
    // Pointer to the table number stored on the heap
    int* num;
    // Pointer to the total number of places at the table (2, 4 or 8) stored on the heap
    int* places;
    // Pointer to the number of free places at the table stored on the heap
    int* freePlaces;

    // Helper method that normalizes the number of places to one of the allowed values (2, 4, 8)
    static int normalizePlaces(int value);
    // Helper method that normalizes the number of free places to be between 0 and places
    static int normalizeFree(int freeValue, int placesValue);

public:
    // Constructor that creates a table from table number, total places and free places
    Table(const int* num, const int* places, const int* freePlaces);
    // Destructor that releases all dynamically allocated resources
    ~Table();
    // Copy constructor that performs a deep copy from another Table object
    Table(const Table& other);
    // Copy assignment operator that performs a deep copy from another Table object
    Table& operator=(const Table& other);
    // Move constructor that steals the resources from another Table object
    Table(Table&& other) noexcept;
    // Move assignment operator that steals the resources from another Table object
    Table& operator=(Table&& other) noexcept;

    // Getter that returns a const pointer to the table number
    const int* getNum() const;
    // Getter that returns a const pointer to the total number of places
    const int* getPlaces() const;
    // Getter that returns a const pointer to the number of free places
    const int* getFreePlaces() const;

    // Setter that replaces the table number with a deep copy of the given value
    void setNum(const int* num);
    // Setter that replaces the total number of places and normalizes it to 2, 4 or 8
    void setPlaces(const int* places);
    // Setter that replaces the number of free places and clamps it to a valid range
    void setFreePlaces(const int* freePlaces);

    // Method that converts the table data into a human-readable string
    string toString() const;
};
#endif //UNTITLED1_TABLE_H