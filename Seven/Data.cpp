#include "Data.h"
#include <sstream>

// 🏗️ Constructor – initializes size with deep copy (if provided) and free = true
Data::Data(const int *size) {
    // if size pointer is given, allocate and copy value, otherwise store nullptr
    this->size = size ? new int(*size) : nullptr;

    // allocate boolean and set free = true
    this->free = new bool(true);
}

// 🧹 Destructor – releases allocated memory to prevent leaks
Data::~Data() {
    // delete size pointer (if not null)
    delete this->size;

    // delete free pointer (if not null)
    delete this->free;
}

// 🧬 Copy constructor – deep copy values from another Data object
Data::Data(const Data &other) {
    // deep copy size if exists, else nullptr
    this->size = other.size ? new int(*other.size) : nullptr;

    // deep copy free if exists, else nullptr
    this->free = other.free ? new bool(*other.free) : nullptr;
}

// 📝 Copy assignment operator – deep copy with self-check
Data &Data::operator=(const Data &other) {
    // avoid self-assignment
    if (this != &other) {

        // free old memory
        delete this->size;
        delete this->free;

        // deep copy from source
        this->size = other.size ? new int(*other.size) : nullptr;
        this->free = other.free ? new bool(*other.free) : nullptr;
    }

    // return reference to support chaining
    return *this;
}

// 🚚 Move constructor – steals ownership of pointers (no deep copy)
Data::Data(Data &&other) noexcept {
    // steal pointers
    this->size = other.size;
    this->free = other.free;

    // leave source in valid but empty state
    other.size = nullptr;
    other.free = nullptr;
}

// 🚚 Move assignment – handles self-check, deletes old, steals pointers
Data &Data::operator=(Data &&other) noexcept {
    // avoid self-move
    if (this != &other) {

        // free old memory before stealing
        delete this->size;
        delete this->free;

        // steal pointers
        this->size = other.size;
        this->free = other.free;

        // reset source pointers
        other.size = nullptr;
        other.free = nullptr;
    }

    // return reference to support chaining
    return *this;
}

// 📰 toString – formats object into readable string
string Data::toString() const {
    // helper to build string
    std::stringstream ss;

    // print size (value or null)
    ss << "size: "
       << (this->size != nullptr ? std::to_string(*this->size) : "null");

    // separator
    ss << ", ";

    // print free status (true/false/null)
    ss << "free: "
       << (this->free != nullptr ? (*this->free ? "true" : "false") : "null");

    // return result string
    return ss.str();
}

// ✅ Getter: return pointer to free flag
const bool* Data::isFree() const {
    return this->free; // return internal pointer (read-only)
}

// ✅ Getter: return pointer to size value
const int* Data::getSize() const {
    return this->size; // return internal pointer (read-only)
}

// ✅ Setter: deep copy new free flag value
// ReSharper disable once CppParameterNamesMismatch
void Data::setFree(const bool *freee){
    // if both current and input pointers are valid
    if (this->free != nullptr && freee != nullptr) {

        // delete old boolean memory
        delete this->free;

        // allocate new boolean and assign copied value
        this->free = new bool(*freee);
    }
}

// ✅ Setter: deep copy new size value
// ReSharper disable once CppParameterNamesMismatch
void Data::setSize(const int *sizee){
    // if both current and input pointers are valid
    if (this->size != nullptr && sizee != nullptr) {

        // delete old integer memory
        delete this->size;

        // allocate new integer and assign copied value
        this->size = new int(*sizee);
    }
}
