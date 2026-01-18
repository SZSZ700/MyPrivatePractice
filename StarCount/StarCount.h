#ifndef UNTITLED1_STARCOUNT_H
#define UNTITLED1_STARCOUNT_H
#include "..//Node/Node.h"
#include "..//BinNode/BinNode.h"
class StarCount {
    // pointer for the head of the list that represent the number
    Node<int*> *number{};

    // pointer for the tail of the list that represent the number
    Node<int*> *tail{};

    // Convert this.number (single list) to a doubly linked list
    BinNode<int*>* convert_THIS_SingleListToDoubly() const;

    // Return the tail of a given doubly linked list
    BinNode<int*>* returnTailOfDlist(BinNode<int*>* chain) const;

    // Convert doubly list back into this.number and free the doubly list
    void convertDoublyListTo_THIS_Single(const BinNode<int*>* doublychain);

public:
    // constructor
    explicit StarCount(const int *num);

    // destructor
    ~StarCount();

    // copy constructor
    StarCount(const StarCount &other);

    // copy assignment
    StarCount& operator=(const StarCount &other) noexcept;

    // move constructor
    StarCount(StarCount &&other) noexcept;

    // move assignment
    StarCount& operator=(StarCount &&other) noexcept;

    // get
    const Node<int*> *getNumber() const;

    // set
    void setNumber(const Node<int*> *numberr);

    // tostring
    std::string toString() const;

    // build numlist
    Node<int*> *buildlist(const int *num);

    // fix the list
    void fixNumber();

    // inc the number
    void addOne();
};
#endif //UNTITLED1_STARCOUNT_H