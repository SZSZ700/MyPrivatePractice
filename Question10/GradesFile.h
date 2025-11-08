#ifndef UNTITLED1_GRADESFILE_H
#define UNTITLED1_GRADESFILE_H

#include "Student.h"
#include "..//Node/Node.h"
#include <iostream>
using namespace std;

// 📘 Class GradesFile manages 100 linked lists of Student* objects — all allocated on heap
class GradesFile {
    // 👉 Pointer to an array of 100 Node<Student*>* (each one is a list head)
    Node<Student*>* *lists;

public:
    // 🏗️ Default constructor
    GradesFile();

    // 💣 Destructor
    ~GradesFile();

    // 🧬 Copy constructor
    GradesFile(const GradesFile& other);

    // ✍️ Copy assignment
    GradesFile& operator=(const GradesFile& other);

    // 🚚 Move constructor
    GradesFile(GradesFile&& other) noexcept;

    // 🚚 Move assignment
    GradesFile& operator=(GradesFile&& other) noexcept;

    // ➕ Add a student to the correct list
    void addStudent(Student* s);

    // 🔍 Find student by ID
    const Student* findStudent(const int* id) const;

    // 🖨️ Print all data
    void printAll() const;

    // 🔍 return first student in array[k]
    const Student* getStudent(int k) const;

    // ⁉️ if at index k there is no list
    bool isEmpty(const int k) const;

    // check if all the students in the collection at position k
    // in the array match this position according to their studentId.
    bool listIsGood (const int k) const;

    // The function moves the first student from the collection located at position k in the array
    // to become the last student in the collection located at position j in the array.
    void moveStudent(int k, int j);

private:
    // 🧮 Calculate index from middle two digits of ID
    int calculateIndex(const int* id) const;
};

#endif //UNTITLED1_GRADESFILE_H
