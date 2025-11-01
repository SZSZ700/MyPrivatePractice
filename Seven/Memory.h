#ifndef UNTITLED1_MEMORY_H
#define UNTITLED1_MEMORY_H

#include "..//Node/Node.h"
#include "Data.h"

// ✅ Memory class simulates RAM as a linked list of Data* blocks
class Memory {
    Node<Data*>* start;   // pointer to the first memory block

public:
    // ✅ Constructor – create single free block of totalSize
    explicit Memory(int totalSize);

    // ✅ Destructor – releases the entire memory list
    ~Memory();

    // ✅ Copy constructor – deep copy of memory blocks
    Memory(const Memory& other);

    // ✅ Copy assignment – deep copy after clearing old data
    Memory& operator=(const Memory& other);

    // ✅ Move constructor – steal pointer (no deep copy)
    Memory(Memory&& other) noexcept;

    // ✅ Move assignment – free old list, steal pointer
    Memory& operator=(Memory&& other) noexcept;

    // ✅ Dangerous state - Check if memory is in dangerous state (free memory < 10%)
    bool isInDanger() const;

    // ✅ first fit algorithem
    bool firstFit(const int *num);
};

#endif
