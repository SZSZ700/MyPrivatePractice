#ifndef UNTITLED1_CHARLIST_H
#define UNTITLED1_CHARLIST_H
#include "../Node/Node.h"
using namespace std;

class CharList {
    Node<char*> *head;
    Node<char*> *tail;

    public:
    // constructor
    explicit CharList(Node<char*> *head);
    // destructor
    ~CharList();

    // copy constructor
    CharList(const CharList& other);
    // copy assignment
    CharList& operator=(const CharList& other);

    // move constructor
    CharList(CharList&& other)noexcept;
    // move assignment
    CharList& operator=(CharList&& other)noexcept;

    // make the list valid
    void swap(char letter);
};
#endif //UNTITLED1_CHARLIST_H
