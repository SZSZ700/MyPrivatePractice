#include "Student.h"

// 🧱 Constructor - allocates and deep copies student ID and grade
Student::Student(const int *studentId, const int *grade) {
    // 🎯 Allocate and copy student ID
    this->studentId = new int(*studentId);

    // 🎯 Allocate and copy grade
    this->grade = new int(*grade);
}

// 💣 Destructor - frees allocated memory
Student::~Student() {
    // 🧹 Free student ID memory
    delete this->studentId;

    // 🧹 Free grade memory
    delete this->grade;
}

// 🧬 Copy constructor - deep copy
Student::Student(const Student &student) {
    // 📋 Allocate and copy ID
    this->studentId = new int(*student.studentId);

    // 📋 Allocate and copy grade
    this->grade = new int(*student.grade);
}

// ✍️ Copy assignment operator - deep copy
Student &Student::operator=(const Student &student) {
    // 🚫 Self-assignment guard
    if (this == &student)
        return *this;

    // 🧹 Free old memory
    delete this->studentId;
    delete this->grade;

    // 🧬 Deep copy new values
    this->studentId = new int(*student.studentId);
    this->grade = new int(*student.grade);

    // ↩️ Return reference to current object
    return *this;
}

// 🚚 Move constructor - transfers ownership
Student::Student(Student &&student) noexcept {
    // 📦 Steal pointers
    this->studentId = student.studentId;
    this->grade = student.grade;

    // ❌ Nullify source pointers
    student.studentId = nullptr;
    student.grade = nullptr;
}

// 🚚 Move assignment operator
Student &Student::operator=(Student &&student) noexcept {
    // 🚫 Self-move check
    if (this == &student)
        return *this;

    // 🧹 Free existing memory
    delete this->studentId;
    delete this->grade;

    // 📦 Steal new pointers
    this->studentId = student.studentId;
    this->grade = student.grade;

    // ❌ Nullify source
    student.studentId = nullptr;
    student.grade = nullptr;

    // ↩️ Return reference
    return *this;
}

// ⚙️ Setter: student ID
// ReSharper disable once CppParameterNamesMismatch
void Student::setStudentId(const int *studentIdd) {
    // 🧹 Delete old value
    delete this->studentId;

    // 🧬 Deep copy new one
    this->studentId = new int(*studentIdd);
}

// ⚙️ Setter: grade
// ReSharper disable once CppParameterNamesMismatch
void Student::setGrade(const int *gradee) {
    // 🧹 Delete old value
    delete this->grade;

    // 🧬 Deep copy new one
    this->grade = new int(*gradee);
}

// 🎯 Getter: student ID
const int* Student::getStudentId() const {
    return this->studentId;
}

// 🎯 Getter: grade
const int* Student::getGrade() const {
    return this->grade;
}

// 📜 Convert to string representation
string Student::toString() const {
    // 🧩 Build text like: "Student ID: 101 | Grade: 95"
    return "Student ID: " + to_string(*this->studentId) +
           " | Grade: " + to_string(*this->grade);
}

// 🖨️ Print info to console
void Student::print() const {
    // 🖨️ Output formatted text
    cout << toString() << endl;
}
