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

    // pointer to the node that contains the letter
    static Node<char*>* firstAfterChain(const Node<char*>* chain, char letter);

    // pointer to the last node in the list that contains the letter
    Node<char*>* last (char letter) const;

    // make the list valid
    void swap(char letter);
};
#endif //UNTITLED1_CHARLIST_H
