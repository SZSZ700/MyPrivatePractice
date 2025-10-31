#ifndef UNTITLED1_QUEUE_H
#define UNTITLED1_QUEUE_H
#import "../Node/Node.h"
#import <sstream>
#import <type_traits>

// =======================================
// Queue<T>
// Purpose: FIFO queue implemented with Node<T>
// Notes:   Queue does NOT own the stored values (only wrapper nodes).
//          If T is a pointer → queue does NOT delete the pointees.
// =======================================
template<typename T>
class Queue {
    Node<T>* front;   // pointer to front node
    Node<T>* rear;    // pointer to rear node

public:
    // Constructor - initialize empty queue
    Queue() : front(nullptr), rear(nullptr) {}

    // Destructor - delete only the Node wrappers (not the stored values)
    ~Queue() {
        while (this->front != nullptr) {
            const Node<T>* temp = this->front;      // current front
            this->front = this->front->getNext();   // move to next
            delete temp;                // free wrapper node only
        }
        this->rear = nullptr;
    }

    // Offer - enqueue a new value
    void offer(const T& val) {
        auto* newNode = new Node<T>(val); // create node wrapper
        if (this->rear == nullptr) {
            // empty queue
            this->front = newNode;
            this->rear  = newNode;
        } else {
            this->rear->setNext(newNode); // link after current rear
            this->rear = newNode;         // update rear
        }
    }

    // Poll - dequeue and return a value
    // If empty → throws (לשיקולך אפשר להחזיר nullptr/ערך ברירת מחדל)
    T poll() {
        if (this->front == nullptr) {
            // for pointer types
            if constexpr (std::is_pointer_v<T>) { return nullptr; }
            else { throw std::runtime_error("Queue is empty"); }
        }

        Node<T>* temp = this->front;           // current front node
        T val = temp->getValue();        // stored value
        this->front = this->front->getNext();        // advance front

        if (this->front == nullptr) this->rear = nullptr; // queue became empty

        delete temp;                     // delete wrapper node

        return val;                      // return stored value
    }

    // Head - peek front without removing
    T head() const {
        if (this->front == nullptr) {
            // for pointer types
            if constexpr (std::is_pointer_v<T>) { return nullptr; }
            else { throw std::runtime_error("Queue is empty"); }
        }

        return this->front->getValue();
    }

    // QIsEmpty - check if queue empty
    [[nodiscard]] bool isEmpty() const {
        return this->front == nullptr;
    }

    // =======================================
    // toString
    // Purpose: return a string representation of the queue
    // Handles both value and pointer types
    // =======================================
    [[nodiscard]] std::string toString() const {
        std::stringstream ss;
        Node<T>* pos = this->front;

        while (pos != nullptr) {

            // if the current value is a pointer:
            if constexpr (std::is_pointer_v<T>) {
                // derefernce for reciving the value(created on heap)
                // then add the value to the stringstream
                if (pos->getValue()) { ss << *pos->getValue(); }
                else { ss << "null"; }

            }

            // if the current value is not a pointer:
            // add the value as is to the stringstream
            else { ss << pos->getValue(); }

            // add space between values
            if (pos->getNext() != nullptr) { ss << " "; }

            pos = pos->getNext();
        }

        return ss.str();
    }
};

#endif //UNTITLED1_QUEUE_H
