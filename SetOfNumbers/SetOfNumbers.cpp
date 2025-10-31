#include "SetOfNumbers.h"
#include <ctime>
#include <iostream>
// ==========================================================
// 🏗️ Default constructor
// Initializes an empty linked list.
// ==========================================================
SetOfNumbers::SetOfNumbers() {
    this->chain = nullptr;  // 🧱 No nodes yet
    this->tail = nullptr;   // 🔚 No tail node
    this->size = 0;         // 🧮 List size = 0
}

// ==========================================================
// 💣 Destructor
// Frees all nodes and their contained integer values.
// ==========================================================
SetOfNumbers::~SetOfNumbers() {
    // 🧹 Start from the head of the list
    // ReSharper disable once CppLocalVariableMayBeConst
    Node<int*>* curr = this->chain;

    // 🔁 Traverse all nodes and delete them one by one
    while (curr) {
        Node<int*>* next = curr->getNext();  // 📍 Keep pointer to next node
        delete curr->getValue();             // 🧮 Free the stored int*
        delete curr;                         // 🧱 Free the Node itself
        curr = next;                         // ⏩ Move to next node
    }

    // 🚫 Reset all pointers
    this->chain = nullptr;
    this->tail = nullptr;
    this->size = 0;
}

// ==========================================================
// 🧬 Copy constructor (Deep Copy)
// Creates a full, independent copy of another SetOfNumbers.
// ==========================================================
SetOfNumbers::SetOfNumbers(const SetOfNumbers& other) {
    this->chain = nullptr;   // 🧱 Start with empty list
    this->tail = nullptr;    // 🔚 No tail yet
    this->size = 0;          // 🧮 Reset size

    // ReSharper disable once CppLocalVariableMayBeConst
    Node<int*>* src = other.chain;    // 📦 Source pointer for traversal
    Node<int*>* prev = nullptr;       // 📦 Tracks previous node for linking

    // 🔁 Traverse the source chain
    while (src) {
        // 🧩 Deep copy the integer value if it exists
        int* val = src->getValue() ? new int(*src->getValue()) : nullptr;

        // 🧱 Create a new node with the copied value
        auto* newNode = new Node(val);

        // 🔗 Link into the new list
        if (!this->chain)
            this->chain = newNode;  // First node becomes the head
        if (prev)
            prev->setNext(newNode); // Link from previous node

        // ⏩ Move forward
        prev = newNode;
        src = src->getNext();

        // 🔚 Update tail and size
        this->tail = newNode;
        this->size++;
    }
}

// ==========================================================
// 🧾 Copy assignment operator (Deep Copy)
// Replaces current data with a deep copy of another instance.
// ==========================================================
SetOfNumbers& SetOfNumbers::operator=(const SetOfNumbers& other) {
    // 🚨 Self-assignment protection
    if (this != &other) {
        // 🧹 Delete current list before copying
        // ReSharper disable once CppLocalVariableMayBeConst
        Node<int*>* curr = this->chain;

        while (curr) {
            Node<int*>* next = curr->getNext();
            delete curr->getValue();
            delete curr;
            curr = next;
        }
        this->chain = nullptr;
        this->tail = nullptr;
        this->size = 0;

        // 🔁 Copy nodes from source
        // ReSharper disable once CppLocalVariableMayBeConst
        Node<int*>* src = other.chain;
        Node<int*>* prev = nullptr;

        while (src) {
            int* val = src->getValue() ? new int(*src->getValue()) : nullptr;
            auto* newNode = new Node(val);

            if (!this->chain)
                this->chain = newNode;
            if (prev)
                prev->setNext(newNode);

            prev = newNode;
            src = src->getNext();
            this->tail = newNode;
            this->size++;
        }
    }
    // 🔁 Return self for chaining
    return *this;
}

// ==========================================================
// ⚡ Move constructor
// Transfers ownership of resources (steal pointers).
// ==========================================================
SetOfNumbers::SetOfNumbers(SetOfNumbers&& other) noexcept {
    // 🧠 Steal internal data pointers
    this->chain = other.chain;
    this->tail = other.tail;
    this->size = other.size;

    // 🚫 Leave source in a valid, empty state
    other.chain = nullptr;
    other.tail = nullptr;
    other.size = 0;
}

// ==========================================================
// ⚡ Move assignment
// Clears current list and steals another instance’s data.
// ==========================================================
SetOfNumbers& SetOfNumbers::operator=(SetOfNumbers&& other) noexcept {
    // 🚨 Protect from self-move
    if (this != &other) {
        // 🧹 Free current memory
        // ReSharper disable once CppLocalVariableMayBeConst
        Node<int*>* curr = this->chain;

        while (curr) {
            Node<int*>* next = curr->getNext();
            delete curr->getValue();
            delete curr;
            curr = next;
        }

        // 💨 Take ownership of other's data
        this->chain = other.chain;
        this->tail = other.tail;
        this->size = other.size;

        // 🚫 Empty out the source safely
        other.chain = nullptr;
        other.tail = nullptr;
        other.size = 0;
    }
    // 🔁 Return self reference
    return *this;
}

// ==========================================================
// 🧩 AddToSet — Adds a number only if it doesn't already exist
// ==========================================================
void SetOfNumbers::AddToSet(const int num) {
    // 🚨 If set is empty, just add the first element
    if (!this->chain) {
        this->chain = new Node(new int(num));
        this->tail = this->chain;
        this->size++;
        return;
    }

    // 🔁 Check if the number already exists in the set
    // ReSharper disable once CppLocalVariableMayBeConst
    Node<int*>* curr = this->chain;

    while (curr) {
        // 🚫 Number already exists — do nothing
        if (curr->getValue() && *curr->getValue() == num) { return; }

        curr = curr->getNext();
    }

    // ➕ Add new number to the end (maintain O(1) with tail)
    this->tail->setNext(new Node(new int(num)));
    this->tail = this->tail->getNext();
    this->size++;
}

// ==========================================================
// ⚙️ IsEmpty — Returns true if the set is empty
// ==========================================================
// 🧩 Simply check if size == 0
bool SetOfNumbers::IsEmpty() const { return this->size == 0; }

// ==========================================================
// 🎲 RemoveRandom — Removes and returns a random number
// ==========================================================
int SetOfNumbers::RemoveRandom() {
    // 🚨 Empty check
    if (!this->chain) return -1;

    // 🎲 Random seed (better once globally)
    std::srand(static_cast<unsigned>(std::time(nullptr)));

    // 🎯 Pick random position [1..size]
    // ReSharper disable once CppLocalVariableMayBeConst
    int randomPos = (std::rand() % this->size) + 1;

    // 🧱 Special case — remove head
    if (randomPos == 1) {
        // ReSharper disable once CppLocalVariableMayBeConst
        Node<int*>* tmp = this->chain;                // 📍 save first node
        // ReSharper disable once CppLocalVariableMayBeConst
        int val = *tmp->getValue();                   // 💾 save its value

        this->chain = tmp->getNext();                 // ⏩ move head forward
        if (!this->chain) this->tail = nullptr;       // 🧹 update tail if needed

        delete tmp->getValue();                       // 🧽 free inner int
        delete tmp;                                   // 🧽 free node

        this->size--;                                 // 📉 update size

        return val;                                   // ✅ done
    }

    // 🧩 Runner starts from head
    Node<int*>* runner = this->chain;

    // 🔁 Move until the node *before* the one to delete
    for (int i = 1; i < randomPos - 1; ++i) {
        runner = runner->getNext();
    }

    // 🪓 Node to delete is runner->getNext()
    // ReSharper disable once CppLocalVariableMayBeConst
    Node<int*>* target = runner->getNext();
    // ReSharper disable once CppLocalVariableMayBeConst
    int val = *target->getValue();

    // 🔗 Skip target node
    runner->setNext(target->getNext());

    // 🧹 If target was tail → update tail
    if (target == this->tail) this->tail = runner;

    // 🧽 Free memory
    delete target->getValue();
    delete target;

    this->size--;  // 📉 Update size
    return val;    // ✅ Return removed number
}


// ==========================================================
// 🖨️ print
// Prints all numbers in the list (for debugging).
// ==========================================================
void SetOfNumbers::print() const {
    // ReSharper disable once CppLocalVariableMayBeConst
    Node<int*>* curr = this->chain;  // 📍 Start from the head

    // 🔁 Traverse and print all values
    while (curr) {
        if (curr->getValue())
            std::cout << *curr->getValue() << " ";
        else
            std::cout << "null ";
        curr = curr->getNext();  // ⏩ Move forward
    }
    std::cout << std::endl;
}

int SetOfNumbers::sizeOfSet() {
    // 🧱 Create a temporary empty set to store removed numbers
    auto* temp = new SetOfNumbers();

    // 📏 Counter for number of elements
    int count = 0;

    // 🔁 Move every element from this set to temp while counting
    while (!this->IsEmpty()) {
        const int val = this->RemoveRandom();  // 🪓 Remove a random element
        temp->AddToSet(val);             // ➕ Add it to the temp set
        count++;                         // 📈 Increase element counter
    }

    // 🔄 Restore all elements from temp back to this set
    while (!temp->IsEmpty()) {
        const int val = temp->RemoveRandom();  // 🪓 Remove from temp
        this->AddToSet(val);             // 🔁 Return to original set
    }

    // 🧹 Clean up memory
    delete temp;

    // ✅ Return total number of elements found
    return count;
}

int SetOfNumbers::removeMin() {
    // 🧱 Create a temporary set to store all elements
    auto* temp = new SetOfNumbers();

    // 📏 Initialize min to a very large value
    int min = INT_MAX;

    // 🔁 Move every element from this set to temp and track the smallest
    while (!this->IsEmpty()) {
        const int val = this->RemoveRandom();   // 🪓 Remove random element
        temp->AddToSet(val);              // ➕ Store it in temp
        if (val < min) min = val;         // 🔍 Update minimum if needed
    }

    // 🔄 Restore all elements except the minimum one
    while (!temp->IsEmpty()) {
        if (const int val = temp->RemoveRandom(); val != min) this->AddToSet(val);  // 🔁 Add back only if not min
    }

    // 🧹 Clean up temporary set
    delete temp;

    // ✅ Return the smallest value found
    return min;
}

// 🧩 Checks if every element in s1 is greater than all elements in s2
bool bigger(SetOfNumbers* s1, SetOfNumbers* s2) {
    // 🚨 Basic validation
    if (!s1 || !s2) return false;

    // 🧱 Temporary sets to preserve original data
    auto* temp1 = new SetOfNumbers();
    auto* temp2 = new SetOfNumbers();

    // ❌ Flag — becomes true if any rule is broken
    bool notValid = false;

    // 🔁 Iterate through all elements in the first set
    while (!s1->IsEmpty() && !notValid) {
        const int current = s1->RemoveRandom();   // 🪓 Take one element from s1
        temp1->AddToSet(current);           // ➕ Save it in temp1

        // 🔁 Compare with all elements in the second set
        while (!s2->IsEmpty() && !notValid) {
            const int next = s2->RemoveRandom();  // 🪓 Take one from s2
            temp2->AddToSet(next);          // ➕ Save it in temp2

            // ⚖️ If any element in s2 >= current → rule broken
            if (next >= current) notValid = true;
        }

        // 🔄 Restore s2 from temp2 after each comparison round
        while (!temp2->IsEmpty()) {
            const int tempVal = temp2->RemoveRandom();
            s2->AddToSet(tempVal);
        }
    }

    // 🔄 Restore s1 to its original state
    while (!temp1->IsEmpty()) {
        const int tempVal = temp1->RemoveRandom();
        s1->AddToSet(tempVal);
    }

    // 🧹 Free temp sets
    delete temp1;
    delete temp2;

    // ✅ Return true only if all elements in s1 > all elements in s2
    return !notValid;
}
