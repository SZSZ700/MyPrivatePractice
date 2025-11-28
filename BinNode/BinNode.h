#ifndef UNTITLED1_BINNODE_H
#define UNTITLED1_BINNODE_H
using namespace std;
#include <sstream>
// ====================================================
// BinNode<T>
// A template class representing a doubly linked list node
// Holds a value of type T, a pointer to the next node,
// and a pointer to the previous node.
// ====================================================
template<typename T>
class BinNode {
    T value;          // the value stored in this node
    BinNode* left;    // pointer to the next node in the list
    BinNode* right;    // pointer to the previous node in the list

public:
    // Constructor - initializes node with given value
    // next and prev are set to nullptr by default
    explicit BinNode(T val) {
        this->value = val;
        this->left = nullptr;
        this->right = nullptr;
    }

    // Getter for value
    T getValue() const { return this->value; }

    // Setter for value
    void setValue(T val) { this->value = val; }

    // Getter for next pointer
    BinNode* getLeft() const { return this->left; }

    // Setter for next pointer
    void setRight(BinNode* n) { this->right = n; }

    // Getter for previous pointer
    // [[nodiscard]] → compiler warns if return value is ignored
    BinNode* getRight() const { return this->right; }


    // Setter for previous pointer
    void setLeft(BinNode* p) { this->left = p; }

    // Convert node's value to string
    // constexpr: decide if the "if/else" statement will be eliminated
    // in Compilation-time, so we won't get compilation-time error
    [[nodiscard]] string toString() const {
        // create an empty string buffer:
        stringstream ss;

        // if T is a pointer → print the pointed value:
        if constexpr (std::is_pointer_v<T>) {
            if (this->value) { ss << *value; }
            else { ss << "null"; }  // if pointer is null → print "null"
        }

        // if T is a normal type → print directly
        else { ss << this->value; }

        // Copying out the string buffer, and return it as a string
        return ss.str();
    }
};
#endif //UNTITLED1_BINNODE_H
