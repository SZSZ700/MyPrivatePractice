#ifndef TABLE_H
#define TABLE_H
#include <memory>
#include <string>

class Table {
    // Smart pointer that owns the table number
    std::unique_ptr<int> num;
    // Smart pointer that owns the number of seats
    std::unique_ptr<int> places;
    // Smart pointer that owns the number of free seats
    std::unique_ptr<int> freePlaces;

    // Normalizes places to valid values (2, 4, or 8)
    static int normalizePlaces(int value);

    // Ensures freePlaces is within valid range [0, places]
    static int normalizeFree(int freeValue, int placesValue);

public:
    // Constructor that takes ownership of all values and normalizes them
    Table(std::unique_ptr<int> num,
          std::unique_ptr<int> places,
          std::unique_ptr<int> freePlaces);

    // Getter returning const reference to table number
    const int& getNum() const;

    // Getter returning const reference to total places
    const int& getPlaces() const;

    // Getter returning const reference to free places
    const int& getFreePlaces() const;

    // Setter that replaces table number using ownership transfer
    void setNum(std::unique_ptr<int> num);

    // Setter that replaces places and normalizes it
    void setPlaces(std::unique_ptr<int> places);

    // Setter that replaces freePlaces and clamps it
    void setFreePlaces(std::unique_ptr<int> freePlaces);

    // Converts the table data to a readable string
    std::string toString() const;
};

#endif