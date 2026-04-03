#ifndef UNTITLED1_UCHARLIST_H
#define UNTITLED1_UCHARLIST_H
#include <memory>
#include <list>
using namespace std;

class UCharList {
    list<unique_ptr<char>> chain;

    public:
    // constructor
    explicit UCharList(list<unique_ptr<char>> &otherList);

    // make the list valid
    void swap(char letter);
};
#endif //UNTITLED1_UCHARLIST_H
