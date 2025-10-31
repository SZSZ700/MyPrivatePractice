// Header guard to prevent multiple inclusion
#ifndef UNTITLED1_DRIVER_H
#define UNTITLED1_DRIVER_H
#include <string>

// Class that stores driver data using raw pointers (can be nullptr by design)
class Driver {
    // pointer to first name
    std::string* firstName;
    // pointer to last name
    std::string* lastName;
    // pointer to id
    int* id;
    // pointer to age
    int* age;

public:
    // constructor (deep copy)
    Driver(const std::string* first_name, const std::string* last_name, const int* id, const int* age);

    // destructor
    ~Driver();

    // copy constructor
    Driver(const Driver& other);

    // copy assignment
    Driver& operator=(const Driver& other);

    // move constructor
    Driver(Driver&& other) noexcept;

    // move assignment
    Driver& operator=(Driver&& other) noexcept;

    // get first name (safe)
    [[nodiscard]] std::string getFirstName() const;

    // set first name
    void setFirstName(const std::string* first_name);

    // get last name (safe)
    [[nodiscard]] std::string getLastName() const;

    // set last name
    void setLastName(const std::string* last_name);

    // get id (safe)
    [[nodiscard]] int getId() const;

    // set id
    void setId(const int* ident);

    // get age (safe)
    [[nodiscard]] int getAge() const;

    // set age
    void setAge(const int* driverAge);

    // toString (safe)
    [[nodiscard]] std::string toString() const;
};
#endif // UNTITLED1_DRIVER_H

