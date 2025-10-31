#ifndef UNTITLED1_NODE_H
#define UNTITLED1_NODE_H
#include <string>
#include <sstream>

template<typename T>
class Node {
    T value;        // the actual value (not a pointer)
    Node* next;     // pointer to the next node in the list

public:
    // Constructor - initializes node with a given value
    explicit Node(const T val) {
        this->value = val;    // store the value directly
        this->next = nullptr; // initially there is no next node
    }

    // Destructor - default (no dynamic memory owned by this class)
    ~Node() = default;

    // Getter for value (returns a copy of the value)
    T getValue() const {
        return this->value;   // return the stored value
    }

    // Getter for value by reference (allows modifying the value directly)
    T& getValueRef() {
        return this->value;   // return reference to the stored value
    }

    // Setter for value (replaces the stored value)
    void setValue(const T& val) {
        this->value = val;    // update the value
    }

    // Getter for the next node pointer
    Node* getNext() const {
        return this->next;    // return pointer to the next node
    }

    // Setter for the next node pointer
    void setNext(Node* n) {
        this->next = n;       // link this node to another node
    }

    // Convert node's value to string
    // Works with both normal types and pointers
    [[nodiscard]] std::string toString() const {
        // create an empty string buffer:
        std::stringstream ss;

        // if T is a pointer → print the pointed value:
        if constexpr (std::is_pointer_v<T>) {
            if (value) { ss << *value; }
            else { ss << "null"; } // if pointer is null → print "null"
        }

        // if T is a normal type → print directly
        else { ss << value; }

        // Copying out the string buffer, and return it as a string
        return ss.str();
    }
};
#endif //UNTITLED1_NODE_H
