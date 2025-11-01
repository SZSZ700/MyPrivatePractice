#ifndef UNTITLED1_DATA_H        // ✅ Header guard start – prevents multiple inclusions
#define UNTITLED1_DATA_H

#include <string>               // ✅ Include string class for toString()
using namespace std;            // ✅ Allow using std types without std:: prefix

// ✅ Data class represents a block of memory metadata (size & free status)
class Data {
    bool *free;                 // ✅ Pointer to boolean indicating if block is free
    int *size;                  // ✅ Pointer to integer representing block size

public:
    // ✅ Constructor – receives pointer to size, allocates & copies it
    explicit Data(const int *size);

    // ✅ Destructor – frees allocated memory
    ~Data();

    // ✅ Copy constructor – deep copy source Data
    Data(const Data &other);

    // ✅ Copy assignment – deep copy, handles self-assignment
    Data& operator=(const Data &other);

    // ✅ Move constructor – steals ownership (no copy)
    Data(Data &&other) noexcept;

    // ✅ Move assignment – free old, steal new
    Data& operator=(Data &&other) noexcept;

    // ✅ Returns human-readable string of object state
    std::string toString() const;

    // ✅ Getter for free flag (returns pointer)
    const bool* isFree() const;

    // ✅ Getter for size (returns pointer)
    const int* getSize() const;

    // ✅ Setter for free flag (takes pointer to bool value)
    void setFree(const bool *free);

    // ✅ Setter for size pointer (takes pointer to int value)
    void setSize(const int *size);
};

#endif //UNTITLED1_DATA_H        // ✅ Header guard end
