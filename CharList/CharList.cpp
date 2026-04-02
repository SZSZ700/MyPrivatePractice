#include "CharList.h"
#include <cassert>
// Constructor that initializes the list with a given head
CharList::CharList(Node<char*> *head) {
    // Assign the given head pointer
    this->head = head;
    // Initialize tail to null
    this->tail = nullptr;

    // If head exists, find the tail by traversing the list
    if (this->head) {
        // Start from head
        Node<char*>* curr = this->head;
        // Traverse until last node
        while (curr->getNext() != nullptr) { curr = curr->getNext(); }
        // Set tail to last node
        this->tail = curr;
    }
}

// Destructor that releases all nodes and their char data
CharList::~CharList() {
    // Start from head
    const auto* curr = this->head;

    // Traverse all nodes
    while (curr != nullptr) {
        // Store next node before deleting current
        const Node<char*>* next = curr->getNext();
        // Get the char pointer stored in the node
        const char* val = curr->getValue();
        // Delete the char array if it exists
        delete val;
        // Delete the node itself
        delete curr;
        // Move to next node
        curr = next;
    }

    // Set head and tail to null after cleanup
    this->head = nullptr;
    this->tail = nullptr;
}

// Copy constructor that performs deep copy
CharList::CharList(const CharList& other) {
    // Initialize head and tail to null
    this->head = nullptr;
    this->tail = nullptr;

    // ✅ CREATE NEW DATA ✅ // // ✅ CREATE NEW DATA ✅ //
    // Pointer to traverse the other list
    const auto* curr = other.head;

    // Traverse all nodes in other list
    while (curr != nullptr) {
        // Get the source char pointer
        const char* src = curr->getValue();
        // Create new char copy
        char* newChar = src ? new char(*src) : nullptr;
        // Create new node with copied value
        const auto toAdd = new Node(newChar);

        // If this is first node
        if (!this->head) {
            this->head = toAdd;
            this->tail = toAdd;
        } else {
            // ReSharper disable once CppDFANullDereference
            this->tail->setNext(toAdd);
            this->tail = toAdd;
        }

        // Move to next node
        curr = curr->getNext();
    }
    // ✅ CREATE NEW DATA ✅ // // ✅ CREATE NEW DATA ✅ //
}

// Copy assignment operator with deep copy
CharList& CharList::operator=(const CharList& other) {
    // Prevent self-assignment
    if (this != &other) {
        //❗DELETE OLD DATA❗// //❗DELETE OLD DATA❗//
        // Delete current list
        const auto* curr = this->head;

        // Traverse and delete
        while (curr != nullptr) {
            // pointer to the next node
            const auto* next = curr->getNext();
            // delete current node value
            delete curr->getValue();
            // delete the wrapper(node) itself
            delete curr;
            // mv the iteration [cur] pointer so it will point to were the
            // [next] pointer, points..
            curr = next;
        }

        // Reset head and tail
        this->head = nullptr;
        this->tail = nullptr;
        //❗DELETE OLD DATA❗// //❗DELETE OLD DATA❗//


        // ✅ CREATE NEW DATA ✅ // // ✅ CREATE NEW DATA ✅ //
        // Copy from other
        const Node<char*>* src = other.head;
        // Traverse source list
        while (src) {
            // keep pointer to the current value from the other source list
            const char* val = src->getValue();
            // be ready to create another char
            char* newChar = val ? new char(*val) : nullptr;
            // create new node with the new created char
            // so we can add them to the original list
            const auto toAdd = new Node(newChar);

            // if the original list is empty
            if (!this->head) {
                this->head = toAdd;
                this->tail = toAdd;
            }
            // else if the original list is not empty
            else {
                // add the new created node to the end of the list
                assert( tail != nullptr );
                this->tail->setNext(toAdd);
                this->tail = toAdd;
            }

            // advance to the next node from the other source list
            src = src->getNext();
        }
        // ✅ CREATE NEW DATA ✅ //
    }

    // Return current object - for enable chainning
    return *this;
}

// Move constructor that steals resources
CharList::CharList(CharList&& other) noexcept {
    // 🥷🏻 STEAL OTHER SOURCE DATA 🥷🏻 //  // 🥷🏻 STEAL OTHER SOURCE DATA 🥷🏻 //
    // Steal head pointer
    this->head = other.head;
    // Steal tail pointer
    this->tail = other.tail;
    // Reset other
    other.head = nullptr;
    other.tail = nullptr;
    // 🥷🏻 STEAL OTHER SOURCE DATA 🥷🏻 //  // 🥷🏻 STEAL OTHER SOURCE DATA 🥷🏻 //
}

// Move assignment operator that steals resources
CharList& CharList::operator=(CharList&& other) noexcept {
    // Prevent self-assignment
    if (this != &other) {
        //❗DELETE OLD DATA❗// //❗DELETE OLD DATA❗//
        // Delete current list
        // pointer for the head of the list
        const Node<char*>* curr = this->head;

        while (curr != nullptr) {
            // pointer to the next node
            const Node<char*>* next = curr->getNext();
            // delete current data
            delete curr->getValue();
            // delete current wrapper(Node)
            delete curr;
            curr = next;
        }
        //❗DELETE OLD DATA❗// //❗DELETE OLD DATA❗//


        // 🥷🏻 STEAL OTHER SOURCE DATA 🥷🏻 //  // 🥷🏻 STEAL OTHER SOURCE DATA 🥷🏻 //
        // Steal resources
        this->head = other.head;
        this->tail = other.tail;
        // Reset other
        other.head = nullptr;
        other.tail = nullptr;
        // 🥷🏻 STEAL OTHER SOURCE DATA 🥷🏻 //  // 🥷🏻 STEAL OTHER SOURCE DATA 🥷🏻 //
    }

    // return current object - for enabling future chainning
    return *this;
}


// k(this->head) → d(beforeFirst) → [a(first)] → b → c → d → [a(last)] → k(afterLast) → n(this->tail) → nullptr
// k → n → [a] → b → c → d → [a] → k → d
void CharList::swap(const char letter) {
    assert(this->head != nullptr);
    assert(this->tail != nullptr);
    // validation: letter can't be at the first node nor at the last node
    if (this->head && this->head->getValue() && *this->head->getValue()== letter
        || this->tail && this->tail->getValue() && *this->tail->getValue()== letter) { return; }

    // Lambda that returns the first node after a chain that contains a specific letter
    [[maybe_unused]] auto firstAfterChain = [&](const Node<char*>* chain, const char letterr) -> Node<char*>* {
        // If chain is null, return null
        if (!chain) { return nullptr; }

        // Start traversal from the given chain
        const Node<char*>* curr = chain;

        // Traverse nodes
        while (curr != nullptr) {
            // If value exists and matches the letter
            if (const char* val = curr->getValue(); val && *val == letterr) {
                // Return the next node after the match
                return curr->getNext();
            }

            // Move forward
            curr = curr->getNext();
        }

        // If not found, return null
        return nullptr;
    };

    // Lambda that returns pointer to the last node in the list that contains the letter
    [[maybe_unused]] auto lastLambda = [&](const char letterr) -> Node<char*>* {
        // Pointer used to iterate over the list
        const Node<char*>* pos = this->head;

        // Pointer that will store the last matching node
        Node<char*>* last = nullptr;

        // Traverse the list
        while (pos) {
            // Find the first node after the current one that contains the given letter
            Node<char*>* temp = firstAfterChain(pos, letterr);
            // Save the found node as the current last match
            if (temp) { last = temp; }
            // Move to the found node
            pos = temp;
        }

        // Return the last matching node, or nullptr if none was found
        return last;
    };

    // pointer to the first node in the list that contains the letter
    const auto first = firstAfterChain(this->head, letter);
    assert(first != nullptr);

    // pointer to the node before the first node in the list that contains the letter
    auto beforeFirst = this->head;
    assert(beforeFirst != nullptr);
    while (beforeFirst->getNext() != first) { beforeFirst = beforeFirst->getNext(); }

    // pointer to the last node in the list that contains the letter
    const auto last = lastLambda(letter);
    assert(last != nullptr);

    // pointer to the node after the last node in the list that contains the letter
    const auto afterLast = last->getNext();
    assert(afterLast != nullptr);

    // reconnect the list
    // old list: k → d → [a] → b → c → d → [a] → k → n → nullptr
    // new list: k → n → [a] → b → c → d → [a] → k → d → nullptr
    last->setNext(this->head);
    beforeFirst->setNext(nullptr);
    assert(this->tail != nullptr);
    this->tail->setNext(first);
    this->head = afterLast;
    this->tail = beforeFirst;
}
