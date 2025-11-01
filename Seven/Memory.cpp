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

// ✅ Check if memory is in dangerous state (free memory < 10%)
bool Memory::isInDanger() const {

    // ✅ Get the size of the first memory block (total memory size)
    const auto totalSize = *this->start->getValue()->getSize();

    // ✅ Variable to accumulate total free memory size
    int sumFree = 0;

    // ✅ Pointer to traverse memory list starting from second block
    const Node<Data*>* pos = this->start->getNext();

    // ✅ Iterate through all memory blocks
    while (pos) {

        // ✅ Get pointer to the Data object stored in this node
        const Data* currentData = pos->getValue();

        // ✅ Get pointer to size value of current block
        const int *size = currentData->getSize();

        // ✅ Get pointer to free flag of current block
        // ReSharper disable once CppTooWideScopeInitStatement
        const bool *free = currentData->isFree();

        // ✅ If block is free, add its size to free memory sum
        if (free && *free == true) {
            sumFree += *size;
        }

        // ✅ Move to the next memory block
        pos = pos->getNext();
    }

    // ✅ Calculate threshold for danger (10% of total memory)
    const double threshold = totalSize * 0.10;

    // ✅ Return true if free memory is less than 10%
    return sumFree < threshold;
}

// ✅ First-Fit allocation – finds first free block >= num and splits it
bool Memory::firstFit(const int *num) {

    // 🛑 Check dangerous memory state (free < 10%) → no allocation allowed
    if (this->isInDanger()) {  return false;  }

    if (!num){ return false; }

    // 🧱 Create a new allocated block (with size = *num)
    const auto toAdd = new Node(new Data(num));

    // 🔒 Set the block as occupied (free = false)
    const bool* markOccupied = new bool(false);
    toAdd->getValue()->setFree(const_cast<bool*>(markOccupied));
    delete markOccupied; // 🧹 free temp flag (deep copied inside)

    // ✅ CASE 1️⃣ — Try fitting at the first memory block
    if (this->start->getValue()->getSize()) {

        // ReSharper disable once CppTooWideScopeInitStatement
        const bool *firstFree = this->start->getValue()->isFree(); // 🔎 check if first is free

        // ✅ only if free AND size >= num
        if (firstFree && *firstFree == true && *num <= *this->start->getValue()->getSize()) {

            // 🔗 insert at start
            toAdd->setNext(this->start);
            this->start = toAdd;

            // ✂️ compute remainder
            int* toExtract = toAdd->getNext()->getValue()->getSize() ?
                new int(*toAdd->getNext()->getValue()->getSize() - *num) : nullptr;

            // ✅ remainder positive → resize free block
            if (toExtract && *toExtract > 0) {
                this->start->getNext()->getValue()->setSize(toExtract);
            }
            // ❌ no remainder → convert next block to 0-sized occupied
            else {
                const int* zero = new int(0);
                this->start->getNext()->getValue()->setSize(zero);

                const bool* markUsed = new bool(false);
                this->start->getNext()->getValue()->setFree(markUsed);

                delete zero; // 🧹 clean up
                delete markUsed; // 🧹 clean up
            }

            delete toExtract; // 🧹 clean up
            return true; // ✅ allocated!
        }
    }

    // ✅ CASE 2️⃣ — Search in the list (First-Fit)
    Node<Data*>* pos = this->start;

    while (pos->getNext() != nullptr) {

        // 👉 grab block data
        const Data* nextData = pos->getNext()->getValue();
        const int* blockSize = nextData->getSize();

        // 🎯 condition: free AND size >= requested
        if (const bool* isFree = nextData->isFree();
            isFree && *isFree == true && blockSize && *blockSize >= *num) {

            // 🔗 insert toAdd BEFORE the found block
            toAdd->setNext(pos->getNext());
            pos->setNext(toAdd);

            // ✂️ compute remainder
            int* toExtract = toAdd->getNext()->getValue()->getSize() ?
                new int(*toAdd->getNext()->getValue()->getSize() - *num) : nullptr;

            if (toExtract && *toExtract > 0) {
                // ➕ shrink the free block to leftover size
                toAdd->getNext()->getValue()->setSize(toExtract);
            }
            else {
                // ❌ zero remainder → turn next into occupied dummy block
                const int* zero = new int(0);
                toAdd->getNext()->getValue()->setSize(zero);

                const bool* markUsed = new bool(false);
                toAdd->getNext()->getValue()->setFree(markUsed);

                delete zero; // 🧹 cleanup
                delete markUsed; // 🧹 cleanup
            }

            delete toExtract; // 🧹 cleanup
            return true; // ✅ allocated!
        }

        pos = pos->getNext(); // 🚶 move forward
    }

    // ❌ CASE 3️⃣ — End reached & no suitable free block found
    // ❌ According to rules: MUST return false (cannot append at end)
    return false;
}


