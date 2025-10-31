#ifndef UNTITLED1_SETOFNUMBERS_H
#define UNTITLED1_SETOFNUMBERS_H
#import "../Node/Node.h"
// ==========================================================
// 🔢 Class: SetOfNumbers
// Represents a linked list of integers (using Node<int*>).
// Manages memory manually with full Rule of Five.
// ==========================================================
class SetOfNumbers {
    // ReSharper disable once CppRedundantAccessSpecifier
private:
    Node<int*>* chain;  // 🧩 Pointer to the first node (head of the list)
    Node<int*>* tail;   // 🔚 Pointer to the last node in the list
    int size;           // 📏 Number of nodes currently stored

    // ReSharper disable once CppAccessSpecifierWithNoDeclarations
public:
    // 🏗️ Default constructor
    SetOfNumbers();

    // 💣 Destructor
    ~SetOfNumbers();

    // 🧬 Copy constructor (deep copy)
    SetOfNumbers(const SetOfNumbers& other);

    // 🧾 Copy assignment (deep copy)
    SetOfNumbers& operator=(const SetOfNumbers& other);

    // ⚡ Move constructor (steals ownership)
    SetOfNumbers(SetOfNumbers&& other) noexcept;

    // ⚡ Move assignment (clears current, steals new)
    SetOfNumbers& operator=(SetOfNumbers&& other) noexcept;

    // 🧩 AddToSet — Adds a new number if it doesn't exist
    void AddToSet(int num);

    // 🎲 RemoveRandom — Removes and returns a random number
    int RemoveRandom();

    // ⚙️ IsEmpty — Returns true if the set is empty
    bool IsEmpty() const;

    // 🧩 Utility: Print all values in the list
    void print() const;

    // 🧮 Returns the number of elements without using 'size'
    int sizeOfSet();

    // 📉 Removes and returns the smallest number
    int removeMin();
};
#endif // UNTITLED1_SETOFNUMBERS_H
