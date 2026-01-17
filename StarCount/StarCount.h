#ifndef UNTITLED1_STARCOUNT_H
#define UNTITLED1_STARCOUNT_H
#include "..//Node/Node.h"
class StarCount {
    // pointer for the head of the list that represent the number
    Node<int*> *number{};
    // pointer for the tail of the list that represent the number
    Node<int*> *tail{};
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
};
#endif //UNTITLED1_STARCOUNT_H