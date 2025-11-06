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

private:
    // 🧮 Calculate index from middle two digits of ID
    int calculateIndex(const int* id) const;
};

#endif //UNTITLED1_GRADESFILE_H
