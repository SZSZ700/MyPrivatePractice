#ifndef UNTITLED1_FORWARDLIST_H
#define UNTITLED1_FORWARDLIST_H
#include <iostream>
#include <sstream>
#include "../Node/Node.h"
#include <type_traits>  // for std::is_pointer, std::remove_pointer
// =======================================
// ForwardList<T>
// Singly-linked list using Node<T>
// - Works with both value types and pointers
// - Supports push_front, insert_after, remove, toString, Size, IsEmpty, Find
// =======================================
template<typename T>
class ForwardList {
    Node<T>* head; // pointer to the first node
    int sz;        // size counter

public:
    // ---------------------------------------
    // Constructor: empty list
    // ---------------------------------------
    ForwardList() : head(nullptr), sz(0) {}

    // ---------------------------------------
    // Destructor: delete all nodes (not values)
    // ---------------------------------------
    ~ForwardList() {
        while (head) {
            Node<T>* nxt = head->getNext();
            delete head;
            head = nxt;
        }
        sz = 0;
    }

    // ---------------------------------------
    // push_front
    // Purpose: insert new value at the start
    // Complexity: O(1)
    // ---------------------------------------
    void push_front(const T val) {
        auto* node = new Node<T>(val);
        node->setNext(head);
        head = node;
        sz++;
    }

    // ---------------------------------------
    // insert_after
    // Purpose: insert new value after a given node
    // Complexity: O(1)
    // ---------------------------------------
    void insert_after(Node<T>* pos, const T val) {
        if (!pos) return;
        auto* node = new Node<T>(val);
        node->setNext(pos->getNext());
        pos->setNext(node);
        sz++;
    }

    // ---------------------------------------
    // remove
    // Purpose: remove all nodes whose values equal val
    // Complexity: O(n)
    // ---------------------------------------
    void remove(const T& val) {
        auto equals = [&](const T& a, const T& b) {
            if constexpr (std::is_pointer_v<T>) {
                return *a == *b; // compare pointed values
            } else {
                return a == b;   // compare values directly
            }
        };

        while (head && equals(head->getValue(), val)) {
            const Node<T>* tmp = head;
            head = head->getNext();
            delete tmp;
            sz--;
        }

        Node<T>* cur = head;
        while (cur && cur->getNext()) {
            if (equals(cur->getNext()->getValue(), val)) {
                const Node<T>* tmp = cur->getNext();
                cur->setNext(cur->getNext()->getNext());
                delete tmp;
                sz--;
            } else {
                cur = cur->getNext();
            }
        }
    }

    // ---------------------------------------
    // toString
    // Purpose: return list as string
    // Complexity: O(n)
    // ---------------------------------------
    [[nodiscard]] std::string toString() const {
        std::stringstream ss;
        Node<T>* cur = head;
        while (cur) {
            if constexpr (std::is_pointer_v<T>) {
                ss << *cur->getValue(); // print pointed value
            } else {
                ss << cur->getValue();  // print value directly
            }

            if (cur->getNext()) ss << " -> ";
            cur = cur->getNext();
        }
        return ss.str();
    }

    // ---------------------------------------
    // Size
    // Purpose: return number of nodes
    // Complexity: O(1)
    // ---------------------------------------
    [[nodiscard]] int Size() const { return sz; }

    // ---------------------------------------
    // IsEmpty
    // Purpose: check if list is empty
    // Complexity: O(1)
    // ---------------------------------------
    [[nodiscard]] bool IsEmpty() const { return sz == 0; }

    // ---------------------------------------
    // begin
    // Purpose: return pointer to first node
    // ---------------------------------------
    Node<T>* begin() const { return head; }

    // ---------------------------------------
    // Find
    // Purpose: return index of first occurrence of value, or -1 if not found
    // Complexity: O(n)
    // ---------------------------------------
    int Find(const T& val) const {

        auto equals = [&](const T& a, const T& b) {
            if constexpr (std::is_pointer_v<T>) {
                return *a == *b; // compare pointed values
            } else {
                return a == b;   // compare values directly
            }
        };

        Node<T>* cur = head;
        int index = 0;
        while (cur) {
            if (equals(cur->getValue(), val)) {
                return index;
            }
            cur = cur->getNext();
            index++;
        }
        return -1; // not found
    }
};
#endif //UNTITLED1_FORWARDLIST_H