#import "Buffer.h"
#import <limits>
#import <memory>
#import <sstream>

// 1.) Constructor
Buffer::Buffer(const int n) {
    // if user passes 0 or negative, ensure at least size 1
    this->size = (n > 0) ? n : 1;
    // allocate array on heap
    this->data = new int[this->size];
    // initially no values stored
    this->current = 0;
    // initialize all elements to 0
    for (int i = 0; i < size; i++) data[i] = 0;
}

// 2.) Destructor
Buffer::~Buffer() {
    // free allocated array
    delete[] data;
}

// 3.) Copy constructor (deep copy)
Buffer::Buffer(const Buffer& other) {
    // copy array size
    this->size = other.size;
    // allocate new memory for this object
    this->data = new int[this->size];
    // copy how many values are currently in use
    this->current = other.current;

    // copy each element from other's array
    for (int i = 0; i < size; i++) {
        data[i] = other.data[i];
    }
}

// 4.) Copy assignment operator (deep copy)
Buffer& Buffer::operator=(const Buffer& other) {
    // check for self-assignment
    if (this != &other) {
        // delete current data to prevent memory leak
        delete[] this->data;

        // copy size and allocate new memory
        this->size = other.size;
        this->data = new int[this->size];
        this->current = other.current;

        // copy elements
        for (int i = 0; i < size; i++) data[i] = other.data[i];
    }
    // return *this to allow chaining
    return *this;
}


// example:⤵️
// A a0( new int(10) );
// A a1 = std::move( a0 );
// a1.getNum() => 10;

// 5.) Move constructor - "steal" resources
Buffer::Buffer(Buffer&& other) noexcept {
    //noexcept:
    // אם הקומפיילר יודע ש־move constructor הוא noexcept,
    // הוא יעדיף להשתמש בו אוטומטית במקום copy constructor.

    // copy source fields
    this->size = other.size;
    this->data = other.data;
    this->current = other.current;

    // leave source in safe state
    other.current = 0;
    other.data = nullptr;
    other.size = 0;
}


// example:⤵️
// A a0( new int(10) );
// A a1( new int(6) )
// A a1 = std::move( a0 );
// a1.getNum() => 10;

// 6.) Move assignment operator - "steal" resources
Buffer& Buffer::operator=(Buffer&& other) noexcept {
    // avoid self-assignment
    if (this != &other) {
        // delete current memory
        delete[] data;

        // steal other's memory and values
        this->size = other.size;
        this->data = other.data;
        this->current = other.current;

        // leave source empty
        other.data = nullptr;
        other.size = 0;
        other.current = 0;
    }
    // return *this for chaining
    return *this;
}

// 7.) Get length (capacity of array)
[[nodiscard]] int Buffer::length() const {
    return size;
}

// 8.) Convert to string
[[nodiscard]] std::string Buffer::toString() const {
    std::stringstream ss; // string builder

    // print capacity and number of current elements
    ss << "size=" << size << ", current=" << current << " -> [";

    // print current elements
    for (int i = 0; i < current; ++i) {
        if (i) ss << ' '; // add space between numbers
        ss << data[i];
    }

    ss << ']'; // close the list
    return ss.str();
}

// 9.) Add value to the array
void Buffer::add(const int n) {
    // if there is room
    if (this->current < this->size) {
        this->data[this->current++] = n;
    } else {
        // calculate new capacity (double the size, or at least 1)
        const int newCapacity = (this->size == 0) ? 1 : (this->size * 2);
        // allocate bigger array
        auto *temp = new int[newCapacity];

        // copy existing values
        for (int i = 0; i < this->current; i++) {
            temp[i] = this->data[i];
        }

        // add new value
        temp[this->current] = n;

        // delete old memory
        delete[] this->data;
        // point to new memory
        this->data = temp;
        // increase logical size
        this->current++;
        // update capacity
        this->size = newCapacity;
    }
}

// 10.) Remove first occurrence of n
int Buffer::removeValue(const int n) {
    // if empty, return sentinel
    if (this->current == 0) {
        return std::numeric_limits<int>::min();
    }

    // search for the value
    for (int i = 0; i < this->current; ++i) {
        if (this->data[i] == n) {
            const int removed = this->data[i]; // store removed value

            // shift elements left
            for (int j = i; j < this->current - 1; ++j) {
                this->data[j] = this->data[j + 1];
            }

            this->current--; // reduce logical size
            return removed;
        }
    }

    // not found → return sentinel
    return std::numeric_limits<int>::min();
}

// 11.) Remove all occurrences of num
void Buffer::eraseValue(const int num) {
    auto count = 0; // count occurrences

    // count duplicates
    for (auto i = 0; i < this->current; i++) {
        if (this->data[i] == num) { count++; }
    }

    // nothing found
    if (count == 0) { return; }

    // allocate new array with reduced size
    auto *temp = new int[this->current - count];
    auto index = 0;

    // copy only elements different from num
    for (auto i = 0; i < this->current; i++) {
        if (this->data[i] != num) {
            temp[index++] = this->data[i];
        }
    }

    // delete old memory
    delete[] this->data;
    this->data = temp;
    this->current -= count; // adjust current size
    this->size = this->current; // shrink capacity to fit
}

// 12.) Count how many times n occurs
int Buffer::count(const int n) const {
    int count = 0;
    // iterate over array
    for (int i = 0; i < this->size; i++) {
        if (this->data[i] == n) { count++; }
    }
    return count;
}

// 13.) Check if n exists in array
bool Buffer::contains(const int n) const {
    // search for value
    for (int i = 0; i < this->size; i++) {
        if (this->data[i] == n) { return true; }
    }
    // not found
    return false;
}
