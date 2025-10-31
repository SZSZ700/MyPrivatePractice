#ifndef UNTITLED1_LIST_H
#define UNTITLED1_LIST_H
#include <sstream>
#include <string>
#include "../BinNode/BinNode.h"
#include <stdexcept>
// =======================================
// List<T>
// A generic doubly-linked list based on BinNode<T>
// - Supports add/remove at head and tail
// - Supports insert/remove at specific index
// - Provides size, empty check, and string conversion
// =======================================
template<typename T>
class List {
    BinNode<T>* head;   // pointer to first node
    BinNode<T>* tail;   // pointer to last node
    int sz;             // number of nodes

public:
    // ---------------------------------------
    // Constructor: initialize empty list
    // ---------------------------------------
    List() : head(nullptr), tail(nullptr), sz(0) {}

    // ---------------------------------------
    // Destructor: free all nodes (not the stored values)
    // ---------------------------------------
    ~List() {
        // iterate and delete all nodes
        while (head) {
            BinNode<T>* nxt = this->head->getRight(); // save next node
            delete this->head;                        // delete current node
            this->head = nxt;                         // advance forward
        }

        this->tail = nullptr; // reset tail
        this->sz = 0;         // reset size
    }

    // ---------------------------------------
    // Size
    // Return the number of nodes in the list
    // ---------------------------------------
    [[nodiscard]] int Size() const { return this->sz; }

    // ---------------------------------------
    // IsEmpty
    // Return true if the list has no nodes
    // ---------------------------------------
    [[nodiscard]] bool IsEmpty() const { return this->sz == 0; }

    // ---------------------------------------
    // AddFirst
    // Insert a new node at the beginning of the list
    // ---------------------------------------
    void AddFirst(T val) {
        auto* toAdd = new BinNode<T>(val);  // create new node with value

        toAdd->setRight(head);              // link new node to old head
        if (head) head->setLeft(toAdd);     // update old head's prev pointer
        this->head = toAdd;                       // update head to new node

        if (!tail) tail = toAdd;            // if list was empty, tail also points here

        ++this->sz;                              // increment size
    }

    // ---------------------------------------
    // AddLast
    // Insert a new node at the end of the list
    // ---------------------------------------
    void AddLast(T val) {
        auto* node = new BinNode<T>(val);  // create new node with value

        node->setLeft(tail);               // link new node to old tail
        if (tail) tail->setRight(node);    // update old tail's next pointer
        this->tail = node;                       // update tail to new node

        if (!head) head = node;            // if list was empty, head also points here

        ++this->sz;                              // increment size
    }

    // ---------------------------------------
    // RemoveFirst
    // Remove and return the value of the first node
    // Throws if the list is empty
    // ---------------------------------------
    T RemoveFirst() {
        if (!head) throw std::runtime_error("List is empty");

        BinNode<T>* start = this->head; // save first BinNode
        T val = start->getValue(); // copy value of first BinNode
        this->head = head->getRight(); // advance head

        if (head) {
            // remove back link
            head->setLeft(nullptr);
        }else {
            // list became empty
            tail = nullptr;
        }

        delete start; // free old head
        --this->sz; // decrement size

        return val; // return removed value
    }

    // ---------------------------------------
    // RemoveLast
    // Remove and return the value of the last node
    // Throws if the list is empty
    // ---------------------------------------
    T RemoveLast() {
        if (!tail) throw std::runtime_error("List is empty");

        BinNode<T>* end = tail;           // save last BinNode
        T val = end->getValue();          // copy value of the last BinNode
        tail = tail->getLeft();            // move tail backward

        if (tail) {
            // remove forward link
            tail->setRight(nullptr);
        }else {
            // list became empty
            head = nullptr;
        }

        delete end; // free old tail
        --this ->sz; // decrement size

        return val;  // return removed value
    }

    // ---------------------------------------
    // Get
    // Return the value at a given index (0-based)
    // Throws if index is out of range
    // ---------------------------------------
    T Get(const int index) const {
        if (index < 0 || index >= this->sz) throw std::out_of_range("Index out of range");

        // optimization: start from head if index is in first half
        if (index <= this->sz / 2) {
            BinNode<T>* cur = head; // pointer to the head
            for (int i = 0; i < index; i++) cur = cur->getRight(); // iteration till index
            return cur->getValue(); // return the value of the BinNode in the given index
        }

        // else: start from tail - move backward
        BinNode<T>* cur = tail; // pointer to the tail
        for (int i = sz - 1; i > index; i--) cur = cur->getLeft(); // iteration till index
        return cur->getValue(); // return the value of the BinNode in the given index
    }

    // =====================================================
    // toString
    // Purpose: build a string that represents the list
    //          from head → tail, separated by spaces.
    // Supports both normal values and pointer values.
    // =====================================================
    [[nodiscard]] std::string toString() const {
        std::stringstream ss;        // string builder for concatenating values

        BinNode<T>* cur = head;      // start traversal from the head of the list

        // iteration
        while (cur) {
            // if T is a pointer
            if constexpr (std::is_pointer_v<T>) {
                // if the pointer is not null
                if (cur->getValue()) { ss << *cur->getValue(); }
                // if pointer is null → print "null"
                else { ss << "null"; }
            }

            // if T is not a pointer → print value directly
            else { ss << cur->getValue(); }

            if (cur->getRight()) ss << " "; // if there is a next node → add a space

            cur = cur->getRight(); // move to the next node
        }

        // return the final string
        return ss.str();
    }

    // =====================================================
    // toStringReverse
    // Purpose: build a string that represents the list
    //          from tail → head, separated by spaces.
    // Supports both normal values and pointer values.
    // =====================================================
    [[nodiscard]] std::string toStringReverse() const {
        std::stringstream ss;        // string builder for concatenating values
        BinNode<T>* cur = tail;      // start traversal from the tail of the list
        while (cur) {                // iterate until the beginning of the list (nullptr)
            if constexpr (std::is_pointer_v<T>) { // check at compile-time if T is a pointer
                if (cur->getValue())                   // if the pointer is not null
                    ss << *cur->getValue();            // dereference and print the pointed value
                else
                    ss << "null";                      // if pointer is null → print "null"
            } else {
                ss << cur->getValue();                 // if T is not a pointer → print value directly
            }
            if (cur->getLeft()) ss << " ";             // if there is a previous node → add a space
            cur = cur->getLeft();                      // move to the previous node
        }
        return ss.str();                               // return the final string
    }

    // ---------------------------------------
    // InsertAt
    // Insert a new value at given index (0-based)
    // ---------------------------------------
    void InsertAt(const int index, T val) {
        if (index < 0 || index > sz) throw std::out_of_range("Index out of range");

        if (index == 0) { AddFirst(val); return; }
        if (index == sz) { AddLast(val); return; }

        // traverse to node at given index
        BinNode<T>* cur;
        if (index <= sz / 2) {
            cur = head;
            for (int i = 0; i < index; i++) cur = cur->getRight();
        } else {
            cur = tail;
            for (int i = sz - 1; i >= index; i--) cur = cur->getLeft();
        }

        auto* node = new BinNode<T>(val);   // new node
        BinNode<T>* prev = cur->getLeft();  // node before current

        // link new node in between prev and cur
        node->setLeft(prev);
        node->setRight(cur);
        prev->setRight(node);
        cur->setLeft(node);

        sz++; // increment size
    }

    // ---------------------------------------
    // RemoveAt
    // Remove node at given index (0-based) and return its value
    // ---------------------------------------
    T RemoveAt(const int index) {
        if (index < 0 || index >= sz) throw std::out_of_range("Index out of range");

        if (index == 0) return RemoveFirst();
        if (index == sz - 1) return RemoveLast();

        // traverse to node at given index
        BinNode<T> *pos;
        if (index <= sz / 2) {
            pos = head;
            for (int i = 0; i < index; i++) pos = pos->getRight();
        } else {
            pos = tail;
            for (int i = sz - 1; i > index; i--) pos = pos->getLeft();
        }

        T val = pos->getValue();          // save value
        BinNode<T>* prev = pos->getLeft();
        BinNode<T>* next = pos->getRight();

        // unlink node
        prev->setRight(next);
        next->setLeft(prev);

        delete pos; // free node
        sz--;       // decrement size

        return val; // return removed value
    }
};
#endif //UNTITLED1_LIST_H