#ifndef UNTITLED1_PYRAMID_H
#define UNTITLED1_PYRAMID_H

#include "..//Node/Node.h"
#include "Clown.h"
using namespace Clownp;

// Class representing a pyramid of clowns
class Pyramid {
    // Pointer to the first node in the pyramid
    Node<Clown*>* head;

    // Pointer to the number of clowns in the pyramid
    int* size;

public:
    // Constructor - creates an empty pyramid
    Pyramid();

    // Destructor - frees all dynamically allocated memory
    ~Pyramid();

    // Copy constructor - performs deep copy
    Pyramid(const Pyramid& other);

    // Copy assignment operator - deep copy with cleanup
    Pyramid& operator=(const Pyramid& other);

    // Move constructor - steals ownership
    Pyramid(Pyramid&& other) noexcept;

    // Move assignment operator - steals ownership with cleanup
    Pyramid& operator=(Pyramid&& other) noexcept;

    // getters
    const Node<Clown*>* getHead() const;

    const int* getSize() const;

    // setters:

    // take ownership!
    void setHead(Node<Clown*>* head);

    void setSize(const int *size);
};

#endif // UNTITLED1_PYRAMID_H

