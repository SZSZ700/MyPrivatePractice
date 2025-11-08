#ifndef UNTITLED1_STUDENTSDATA_H
#define UNTITLED1_STUDENTSDATA_H
#include "Student.h"
#include "..//Node/Node.h"

class StudentData {
    Node<Student*>* chain;

    public:
    // constructor
    StudentData();

    // destructor
    ~StudentData();

    // copy constructor
    StudentData(const StudentData &other);

    // copy assignment
    StudentData &operator=(const StudentData &other);

    // move constructor
    StudentData(StudentData &&other) noexcept;

    // move assignment
    StudentData &operator=(StudentData &&other) noexcept;

    // get
    const Node<Student*> *getChain()const;

    // set
    void setChain(Node<Student*> *chain);

    // toString
    // 🧮 toString — returns formatted info of all students in the chain
    std::string toString() const;

    // remove student from the collection
    void eraseStudent(const string *id);

    // 🖨️ Print all students living in the given city and speaking the given language
    void print(const string* city, const string* lang) const;

    // 🧮 Count how many students live in the given city
    int countByCity(const string* city) const;
};
#endif //UNTITLED1_STUDENTSDATA_H