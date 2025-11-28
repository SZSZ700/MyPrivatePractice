#ifndef UNTITLED1_DRIVERESDB_H
#define UNTITLED1_DRIVERESDB_H

#include <string>
#include "../Node/Node.h"
#include "Driver.h"

/*
 * class DriversDb:
 * private:
 * Node<Driver*>* drivers;
 * int size;
 *
 * public:
 * constructor(); destructor(); copy_constructor(); copy_assignment();
 * move_constructor(); move_assignment();
 *
 * getSize();
 * adddriver();
 * isEmpty();
 * findById();
 * removeById();
 * clear();
 * matches();
 */

// Database class that manages a singly linked list of Driver* nodes
class DriversDb {
    // Explain: Head pointer of a linked list whose nodes store Driver* as their value
    Node<Driver*>* drivers;
    // Explain: Number of elements currently stored
    int size;

public:
    // Default constructor (no initializer list; fields set in body)
    DriversDb();

    // Destructor deletes all nodes and their owned Driver* pointers
    ~DriversDb();

    // Copy constructor performs a full deep copy (allocates NEW Driver for each source pointer)
    DriversDb(const DriversDb& other);

    // Copy assignment clears current and deep-copies from source (allocates NEW Driver per node)
    DriversDb& operator=(const DriversDb& other);

    // Move constructor steals the list (transfers ownership of Driver* pointers)
    DriversDb(DriversDb&& other) noexcept;

    // Move assignment clears current, steals list, and nulls source
    DriversDb& operator=(DriversDb&& other) noexcept;

    // Returns number of stored drivers
    [[nodiscard]] int getSize() const;

    // Returns true if list is empty
    [[nodiscard]] bool isEmpty() const;

    // Adds a new driver to the DB (deep copy)
    void addDriver(const Driver* d);

    // Finds first driver with given id; returns heap-allocated copy (caller must delete)
    [[nodiscard]] Driver* findById(int id) const;

    // Removes first driver with given id; deletes its Driver*; returns true if removed
    bool removeById(int id);

    // Deletes all nodes and their Driver* values; resets to empty
    void clear();

    // same first/last name, and same id
    [[nodiscard]] Driver* matches(const int *ident, const std::string *first_name, const std::string *last_name, const int *age) const;

    // Builds a string with one line per driver using Driver::toString()
    [[nodiscard]] std::string toString() const;
};

#endif // UNTITLED1_DRIVERESDB_H
