#ifndef UNTITLED1_STUDENT_H
#define UNTITLED1_STUDENT_H
// ReSharper disable once CppUnusedIncludeDirective
#include <iostream>
#include <string>
using namespace std;

// 🎓 Class representing a student with ID and grade
class Student {
    int *studentId;   // 🔢 Pointer to student ID
    int *grade;       // 🧮 Pointer to student's grade

public:
    // ===========================
    // 🏗️ Constructors / Rule of Five
    // ===========================

    // 🧱 Parameterized constructor (deep copy from provided values)
    Student(const int *studentId, const int *grade);

    // 💣 Destructor (releases heap memory)
    ~Student();

    // 🧬 Copy constructor (deep copy)
    Student(const Student &student);

    // ✍️ Copy assignment (deep copy)
    Student &operator=(const Student &student);

    // 🚚 Move constructor (steals ownership)
    Student(Student &&student) noexcept;

    // 🚚 Move assignment (steals ownership)
    Student &operator=(Student &&student) noexcept;

    // ===========================
    // ✏️ Setters
    // ===========================

    // ⚙️ Set new student ID (deep copy)
    void setStudentId(const int *studentId);

    // ⚙️ Set new grade (deep copy)
    void setGrade(const int *grade);

    // ===========================
    // 🎯 Getters (read-only)
    // ===========================

    // 📖 Get ID (const pointer)
    const int* getStudentId() const;

    // 📖 Get grade (const pointer)
    const int* getGrade() const;

    // ===========================
    // 🧾 Utility
    // ===========================

    // 📜 Convert to string representation
    string toString() const;

    // 🖨️ Print student info
    void print() const;
};

#endif //UNTITLED1_STUDENT_H
