#ifndef UNTITLED1_STUDENT_H
#define UNTITLED1_STUDENT_H
#include <iostream>
using namespace std;

class Student {
    string *id; // מזהה
    string *name; // שם סטודנט
    string *city; // עיר מגורים
    bool *hasCar; // אם יש לו רכב אמת - כן, שקר - לא
    string *mainLanguage; // שפת אם
    string *subLanguage; // שפה משנית ברמת שליטה גבוהה

    public:
    // constructor
    Student(const string *id, const string *name,
        const string *city, const string *mainLanguage, const string *subLanguage, const bool *hasCar);

    // destructor
    ~Student();

    // copy constructor
    Student(const Student &other);

    // copy assignment
    Student &operator=(const Student &other);

    //move constructor
    Student(Student &&other) noexcept;

    // move assignment
    Student &operator=(Student &&other)noexcept;

    // toString
    std::string toString()const;

    // getters
    const string *getId()const;
    const string *getName()const;
    const string *getCity()const;
    const string *getMainLanguage()const;
    const string *getSubLanguage()const;

    // setters
    void setId(const string *id);
    void setName(const string *name);
    void setCity(const string *city);
    void setMainLanguage(const string *mainLanguage);
    void setSubLanguage(const string *subLanguage);
};
#endif //UNTITLED1_STUDENT_H