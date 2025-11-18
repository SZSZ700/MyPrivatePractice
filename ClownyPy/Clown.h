#ifndef UNTITLED1_CLOWN_H
#define UNTITLED1_CLOWN_H
#include <iostream>

using namespace std;

namespace Clownp {
    class Clown {
        string *name;
        int *weight;
    public:
        // constructor
        Clown(const string *name, const int *weight);

        // destructor
        ~Clown();

        // copy constructor
        Clown(const Clown &other);

        // copy assignment
        Clown &operator=(const Clown &other);

        // move constructor
        Clown(Clown &&other) noexcept;

        // move assignment
        Clown &operator=(Clown &&other) noexcept;

        // getters
        const string *getName() const;
        const int* getWeight() const;

        // setters
        void setName(const string *name);
        void setWeight(const int *weight);

        // toString
        string toString() const;
    };
}
#endif //UNTITLED1_CLOWN_H