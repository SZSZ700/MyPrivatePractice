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

    // 🛑 If memory is already dangerous → refuse allocation immediately
    if (this->isInDanger()) { return false; }

    // 🛑 if num is nullptr
    if (!num) { return false; }

    // 🧱 Create new allocated block with required size
    auto toAdd = new Node(new Data(num));

    // 🔒 Mark newly added block as occupied (free = false)
    const bool *markOccupied = new bool(false);
    toAdd->getValue()->setFree(markOccupied);
    delete markOccupied; // 🧹 delete temporary flag

    // ✅ Case 1️⃣: Try to fit at the start of the list
    if (this->start->getValue()->getSize()) {

        // ✅ Allocate only if first block is free and has enough space
        if (const bool *firstFree = this->start->getValue()->isFree();
            firstFree && *firstFree == true && *num <= *this->start->getValue()->getSize()) {

            // 📸 --- SNAPSHOT for UNDO --- save original state before change
            Node<Data*>* oldNext = this->start;                         // original first block
            const int oldSize = *oldNext->getValue()->getSize();              // original size
            const bool oldFree = *oldNext->getValue()->isFree();              // original free flag

            // 🔗 Insert new allocated block before the original first
            toAdd->setNext(this->start);
            this->start = toAdd;

            // ✂️ Calculate leftover memory size
            const int *toExtract = oldSize? new int(oldSize - *num) : nullptr;

            // ✅ If leftover > 0 → update size of next block
            if (toExtract && *toExtract > 0) {
                this->start->getNext()->getValue()->setSize(toExtract);
            }
            else {
                // ❌ No leftover → convert next block to 0-sized occupied
                const int *zero = new int(0);
                this->start->getNext()->getValue()->setSize(zero);

                const bool *markUsed = new bool(false);
                this->start->getNext()->getValue()->setFree(markUsed);

                delete zero;
                delete markUsed;
            }

            delete toExtract; // 🧹 cleanup temp

            // 🚨 Check if we accidentally made memory dangerous
            if (this->isInDanger()) {

                // 🔄 --- UNDO: Restore original state ---
                const Node<Data*>* toDelete = this->start;       // block we added
                this->start = oldNext;                     // restore original first
                oldNext->getValue()->setSize(&oldSize);    // restore size
                oldNext->getValue()->setFree(&oldFree);    // restore free flag
                delete toDelete;                           // delete new block

                return false; // ❌ undo & refuse
            }

            return true; // ✅ success at start
        }
    }

    // ✅ Case 2️⃣: Search for first fitting block in the list
    Node<Data*>* pos = this->start;

    while (pos->getNext() != nullptr) {

        const Data* currentData = pos->getNext()->getValue(); // 📦 next block data
        const int* blockSize = currentData->getSize();        // 📏 block size

        // 🎯 Only allocate if block is free AND big enough
        if (const bool* isFree = currentData->isFree();
            isFree && *isFree == true && blockSize && *blockSize >= *num) {

            // 📸 --- SNAPSHOT for UNDO ---
            Node<Data*>* oldNext = pos->getNext();               // original next node
            const int oldSize = *blockSize;                            // backup original size
            const bool oldFree = *isFree;                              // backup free flag

            // 🔗 Insert allocation block before this free block
            toAdd->setNext(pos->getNext());
            pos->setNext(toAdd);

            // ✂️ Calculate leftover free memory
            int* toExtract = oldSize ? new int(oldSize - *num) : nullptr;

            if (toExtract && *toExtract > 0) {
                // ✏️ update remainder block size
                toAdd->getNext()->getValue()->setSize(toExtract);
            } else {
                // ❌ remainder = 0 → turn into occupied dummy
                const int *zero = new int(0);
                toAdd->getNext()->getValue()->setSize(zero);

                const bool *markUsed = new bool(false);
                toAdd->getNext()->getValue()->setFree(markUsed);

                delete zero;
                delete markUsed;
            }

            delete toExtract; // 🧹 cleanup

            // 🚨 Check for dangerous state after allocation
            if (this->isInDanger()) {

                // 🔄 --- UNDO: revert to original state ---
                pos->setNext(oldNext);                          // restore pointer link
                oldNext->getValue()->setSize(&oldSize);         // restore size
                oldNext->getValue()->setFree(&oldFree);         // restore free flag

                delete toAdd; // 💥 remove inserted block

                return false; // ❌ undo + deny
            }

            return true; // ✅ success in middle
        }

        pos = pos->getNext(); // 🚶 move forward
    }

    // ❌ Case 3️⃣ — NO free block found → return false
    return false;
}



