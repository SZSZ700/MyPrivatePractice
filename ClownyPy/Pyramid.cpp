#include "Pyramid.h"

// Constructor: Creates an empty pyramid
Pyramid::Pyramid() {
    // No nodes yet
    this->head = nullptr;

    // Allocate size and set to 0
    this->size = new int(0);
}

// Destructor: Frees all nodes and clowns
Pyramid::~Pyramid() {
    // Pointer to current node
    const Node<Clown*>* curr = this->head;

    // Loop through the list
    while (curr != nullptr) {

        // Save next pointer before deletion
        const Node<Clown*>* next = curr->getNext();

        // Delete stored Clown*
        delete curr->getValue();

        // Delete node itself
        delete curr;

        // Move to next node
        curr = next;
    }

    // Delete size pointer
    delete this->size;
}

// Copy constructor: deep copy from another Pyramid
Pyramid::Pyramid(const Pyramid& other) {
    // Copy size value into new memory
    this->size = new int(*other.size);

    // If other is empty -> set head to null
    if (other.head == nullptr) {
        this->head = nullptr;
        return;
    }

    // Copy first node
    // ReSharper disable once CppTemplateArgumentsCanBeDeduced
    this->head = new Node<Clown*>(new Clown(*other.head->getValue()));

    // Pointers for scanning
    const Node<Clown*>* src = other.head->getNext();
    Node<Clown*>* dst = this->head;

    // Copy remaining nodes
    while (src != nullptr) {
        // Create new clown (deep copy)
        const auto clonedClown = new Clown(*src->getValue());

        // Create new node containing that clown
        // ReSharper disable once CppTemplateArgumentsCanBeDeduced
        const auto newNode = new Node<Clown*>(clonedClown);

        // Link node
        dst->setNext(newNode);

        // Move forward
        dst = newNode;
        src = src->getNext();
    }
}

// Copy assignment operator
Pyramid& Pyramid::operator=(const Pyramid& other) {

    // Prevent self-assignment
    if (this == &other)
        return *this;

    // First clean existing data
    this->~Pyramid();

    // Allocate new size
    this->size = new int(*other.size);

    // Copy nodes (same as in copy constructor)
    if (other.head == nullptr) {
        this->head = nullptr;
        return *this;
    }

    this->head = new Node<Clown*>(new Clown(*other.head->getValue()));

    const Node<Clown*>* src = other.head->getNext();
    Node<Clown*>* dst = this->head;

    while (src != nullptr) {
        // ReSharper disable once CppUseAuto
        Clown* clonedClown = new Clown(*src->getValue());
        // ReSharper disable once CppUseAuto
        // ReSharper disable once CppTemplateArgumentsCanBeDeduced
        Node<Clown*>* newNode = new Node<Clown*>(clonedClown);
        dst->setNext(newNode);

        dst = newNode;
        src = src->getNext();
    }

    // Return reference for chaining
    return *this;
}

// Move constructor: steals resources
Pyramid::Pyramid(Pyramid&& other) noexcept {
    // Steal pointers
    this->head = other.head;
    this->size = other.size;

    // Leave 'other' in a safe empty state
    other.head = nullptr;
    other.size = nullptr;
}

// Move assignment operator
Pyramid& Pyramid::operator=(Pyramid&& other) noexcept {

    // Prevent self-assignment
    if (this == &other)
        return *this;

    // Clean old data
    this->~Pyramid();

    // Steal pointers
    this->head = other.head;
    this->size = other.size;

    // Reset source
    other.head = nullptr;
    other.size = nullptr;

    return *this;
}

const Node<Clown *> *Pyramid::getHead() const {
    return this->head;
}

// ReSharper disable once CppParameterNamesMismatch
void Pyramid::setHead(Node<Clown *> *chain) {
    this->~Pyramid();
    this->head = chain;
}

const int *Pyramid::getSize() const {
    return this->size;
}

// ReSharper disable once CppParameterNamesMismatch
void Pyramid::setSize(const int* sizee) {
    delete this->size;
    this->size = new int(*sizee);
}

