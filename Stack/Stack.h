#ifndef UNTITLED1_STACK_H
#define UNTITLED1_STACK_H
#include "../Node/Node.h"
// =======================================
// Stack<T>
// Purpose: LIFO stack implemented with Node<T>
// Notes:   Stack does NOT own the stored pointers (only wrapper nodes).
// =======================================
template<typename T>
class Stack {
    Node<T>* top;   // pointer to the top node

public:
    // Constructor - initialize empty stack
    Stack() {
        top = nullptr;
    }

    // Destructor - delete only wrapper nodes (not stored values)
    ~Stack() {
        while (top != nullptr) {
            const Node<T>* temp = top;     // current top
            top = top->getNext();    // move down
            delete temp;             // free wrapper node only
        }
    }

    // Push - add new pointer to top
    void Push(T* val) {
        auto* newNode = new Node<T>(val);
        newNode->setNext(top); // link new node to old top
        top = newNode;         // update top
    }

    // Pop - remove and return pointer
    // Returns nullptr if empty
    T* Pop() {
        if (top == nullptr) return nullptr;

        Node<T>* temp = top;        // current top
        T* val = temp->getValue();  // stored pointer
        top = top->getNext();       // move down
        delete temp;                // free wrapper node
        return val;                 // return pointer
    }

    // Peek - view pointer at top without removing
    T* Peek() const {
        if (top == nullptr) return nullptr;
        return top->getValue();
    }

    // Empty - check if stack empty
    [[nodiscard]] bool Empty() const {
        return top == nullptr;
    }

    // =======================================
    // toString
    // Purpose: return a string representation of the stack
    // Example: "3 2 1" (top to bottom)
    // =======================================
    [[nodiscard]] std::string toString() const {
        std::stringstream ss;
        Node<T>* pos = top;
        while (pos != nullptr) {
            ss << *(pos->getValue()); // dereference pointer
            if (pos->getNext() != nullptr) {
                ss << " ";           // add space
            }
            pos = pos->getNext();
        }
        return ss.str();
    }
};
#endif //UNTITLED1_STACK_H