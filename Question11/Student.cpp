#include "Student.h"

#include <sstream>

// constructor
Student::Student(const string *id, const string *name,
                 // ReSharper disable once CppParameterNamesMismatch
                 const string *city, const string *mainLanguage, const string *subLanguage, const bool *hasCarr) {
    this->hasCar = new bool(*hasCarr);
    this->id = id ? new string(*id) : nullptr;
    this->name = name ? new string(*name) : nullptr;
    this->city = city ? new string(*city) : nullptr;
    // ReSharper disable once CppDFAMemoryLeak
    this->mainLanguage = mainLanguage ? new string(*mainLanguage) : nullptr;
    // ReSharper disable once CppDFAMemoryLeak
    this->subLanguage = subLanguage ? new string(*subLanguage) : nullptr;
}

// destructor
Student::~Student() {
    delete this->id;
    delete this->name;
    delete this->city;
    delete this->mainLanguage;
    delete this->subLanguage;
    delete this->hasCar;
}

// copy constructor
Student::Student(const Student &other) {
    this->hasCar = new bool(*other.hasCar);
    this->id = other.id ? new string(*other.id) : nullptr;
    this->name = other.name ? new string(*other.name) : nullptr;
    this->city = other.city ? new string(*other.city) : nullptr;
    this->mainLanguage = other.mainLanguage;
    this->subLanguage = other.subLanguage;
}

// copy assignment
Student& Student::operator=(const Student &other) {
    if (this != &other) {
        // delete old data
        delete this->id;
        delete this->name;
        delete this->city;
        delete this->mainLanguage;
        delete this->subLanguage;
        delete this->hasCar;

        // copy other Student object data
        this->hasCar = new bool(*other.hasCar);
        this->id = other.id ? new string(*other.id) : nullptr;
        this->name = other.name ? new string(*other.name) : nullptr;
        this->city = other.city ? new string(*other.city) : nullptr;
        this->mainLanguage = other.mainLanguage;
        this->subLanguage = other.subLanguage;
    }

    return *this;
}

// move constructor
Student::Student(Student &&other) noexcept {
    // steal other student object data
    this->hasCar = other.hasCar;
    this->id = other.id;
    this->name = other.name;
    this->city = other.city;
    this->mainLanguage = other.mainLanguage;
    this->subLanguage = other.subLanguage;

    // leave other student object at safe state
    other.hasCar = nullptr;
    other.id = nullptr;
    other.city = nullptr;
    other.mainLanguage = nullptr;
    other.subLanguage = nullptr;
    other.name = nullptr;
}

// move assignment
Student& Student::operator=(Student &&other) noexcept{
    if (this != &other) {
        // delete old data
        delete this->id;
        delete this->name;
        delete this->city;
        delete this->mainLanguage;
        delete this->subLanguage;
        delete this->hasCar;

        // steal other student object data
        this->hasCar = other.hasCar;
        this->id = other.id;
        this->name = other.name;
        this->city = other.city;
        this->mainLanguage = other.mainLanguage;
        this->subLanguage = other.subLanguage;

        // leave other student object at safe state
        other.hasCar = nullptr;
        other.id = nullptr;
        other.city = nullptr;
        other.mainLanguage = nullptr;
        other.subLanguage = nullptr;
        other.name = nullptr;
    }
    return *this;
}

// getters
const string *Student::getId()const { return this->id; }

const string *Student::getName()const { return this->name; }

const string *Student::getCity()const { return this->city; }

const string *Student::getMainLanguage()const { return this->mainLanguage; }

const string *Student::getSubLanguage()const { return this->subLanguage; }

const bool *Student::getHasCar()const { return this->hasCar; }

// setters
// ReSharper disable once CppParameterNamesMismatch
void Student::setId(const string *idd) {
    delete this->id;
    this->id = new string(*idd);
}

// ReSharper disable once CppParameterNamesMismatch
void Student::setName(const string *namee) {
    delete this->name;
    this->name = new string(*namee);
}

// ReSharper disable once CppParameterNamesMismatch
void Student::setCity(const string *cityy) {
    delete this->city;
    this->city = new string(*cityy);
}

// ReSharper disable once CppParameterNamesMismatch
void Student::setMainLanguage(const string *mainLanguagee) {
    delete this->mainLanguage;
    this->mainLanguage = new string(*mainLanguagee);
}

// ReSharper disable once CppParameterNamesMismatch
void Student::setSubLanguage(const string *subLanguagee) {
    delete this->subLanguage;
    this->subLanguage = new string(*subLanguagee);
}

// ReSharper disable once CppParameterNamesMismatch
void Student::setHasCar(const bool *hasCarr) {
    delete this->hasCar;
    this->hasCar = new bool(*hasCarr);
}

// toString
std::string Student::toString() const {
    if (this->hasCar && this->name && this->city && this->mainLanguage && this->subLanguage && this->id) {
        std::stringstream ss;
        ss << "name: " << this->name->c_str() << ", "
        << "city: " << this->city->c_str() << ", "
        << "mainLanguage: " << this->mainLanguage->c_str() << ", "
        << "subLanguage: " << this->subLanguage->c_str() << ", "
        << "id: " << this->id->c_str() << ", "
        << "has car: " << this->hasCar << ", ";

        return ss.str();
    }
    return nullptr;
}