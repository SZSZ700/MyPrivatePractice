#ifndef UNTITLED1_BUFFER_H
#define UNTITLED1_BUFFER_H

#include <string>

// ====================================================
// Buffer
// A dynamic array class (similar to a simplified vector)
// Supports adding, removing, searching, and copying values
// Implements copy/move semantics for safe memory management
// ====================================================
class Buffer {
    int* data;    // pointer to the dynamically allocated array (on heap)
    int size;     // capacity of the array (how many elements can fit)
    int current;  // current number of values stored in the array

public:
    // 1.) Constructor
    // Create a buffer with initial capacity 'n'
    explicit Buffer(int n);

    // 2.) Destructor
    // Free allocated memory
    ~Buffer();

    // 3.) Copy constructor (deep copy) - without old data
    // Create a new Buffer as a copy of another
    Buffer(const Buffer& other);

    // 4.) Copy assignment operator (deep copy) - delete old data
    // Assign values from another Buffer into this one
    Buffer& operator=(const Buffer& other);

    // 5.) Move constructor - - without old data
    // Transfer ownership of resources from another Buffer
    Buffer(Buffer&& other) noexcept;

    // 6.) Move assignment operator - - delete old data
    // Transfer ownership when assigning from another Buffer
    Buffer& operator=(Buffer&& other) noexcept;

    // Get the capacity of the array
    [[nodiscard]] int length() const;

    // Convert the array contents into a string
    [[nodiscard]] std::string toString() const;

    // Add a value to the array (resize if needed)
    void add(int n);

    // Remove the first occurrence of a value
    // Returns the removed value or sentinel if not found
    int removeValue(int n);

    // Remove all occurrences of a given value
    void eraseValue(int num);

    // Count how many times a value appears in the array
    [[nodiscard]] int count(int n) const;

    // Check if a value exists in the array
    [[nodiscard]] bool contains(int n) const;
};

#endif //UNTITLED1_BUFFER_H
