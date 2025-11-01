#include "Memory.h"                                   // Include the header file for Memory class

// ✅ Constructor: create a single free node with given size
Memory::Memory(const int totalSize) {
    // Allocate a new Data object dynamically with given size
    const auto d = new Data(&totalSize);

    // Create the first node in the memory list holding this block
    this->start = new Node(d);
}

// ✅ Destructor: delete all nodes & their stored Data blocks
Memory::~Memory() {
    // Start iterating from the first node
    const Node<Data*>* curr = this->start;

    // Loop through the entire linked list
    while (curr != nullptr) {
        // Save pointer to the next node before deleting current
        const Node<Data*>* temp = curr->getNext();

        // Delete the Data object stored in the current node
        delete curr->getValue();

        // Delete the node itself
        delete curr;

        // Move to the next node
        curr = temp;
    }

    // Set start to null after full cleanup
    this->start = nullptr;
}

// ✅ Copy constructor: deep copy memory structure
Memory::Memory(const Memory& other) {
    // If the source list is empty, set start to null
    if (!other.start) {
        this->start = nullptr;
        return;
    }

    // Create a new node by deep-copying the first memory block
    this->start = new Node(new Data(*other.start->getValue()));

    // Source pointer for traversal
    const Node<Data*>* src = other.start->getNext();

    // Destination pointer to append copied nodes
    Node<Data*>* dst = this->start;

    // Loop to deep-copy the rest of the nodes
    while (src != nullptr) {
        // Allocate new Data copy and wrap inside new Node
        // ReSharper disable once CppDFANullDereference
        dst->setNext(new Node(new Data(*src->getValue())));

        // Move destination forward
        dst = dst->getNext();

        // Move source forward
        src = src->getNext();
    }
}

// ✅ Copy assignment: delete existing list, then deep copy other list
Memory& Memory::operator=(const Memory& other) {
    // Check self-assignment to avoid deleting this object
    if (this == &other) return *this;

    // Delete existing list by manually calling destructor
    this->~Memory();

    // If other list is empty, set start to null
    if (!other.start) {
        this->start = nullptr;
        return *this;
    }

    // Deep copy the first block
    this->start = new Node(new Data(*other.start->getValue()));

    // Source traversal pointer
    const Node<Data*>* src = other.start->getNext();

    // Destination append pointer
    Node<Data*>* dst = this->start;

    // Deep copy remaining nodes
    while (src != nullptr) {
        // ReSharper disable once CppDFANullDereference
        dst->setNext(new Node(new Data(*src->getValue())));
        dst = dst->getNext();
        src = src->getNext();
    }

    // Return reference to this object to allow chaining
    return *this;
}

// ✅ Move constructor: steal pointer to memory list
Memory::Memory(Memory&& other) noexcept {
    // Take ownership of other's list
    this->start = other.start;

    // Leave other in a safe empty state
    other.start = nullptr;
}

// ✅ Move assignment: delete current list, then steal pointer
Memory& Memory::operator=(Memory&& other) noexcept {
    // Check for self-move
    if (this != &other) {
        // Delete current memory list
        this->~Memory();

        // Take ownership of other's memory list
        this->start = other.start;

        // Nullify other's pointer to avoid double free
        other.start = nullptr;
    }

    // Return reference to allow chaining
    return *this;
}
