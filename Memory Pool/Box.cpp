#include "Box.h"
using namespace std;

// אתחול מערך ההקצאות
// ['', '', '', null, null]
char Box::pool[POOL_SIZE * sizeof(Box)];

// אתחול מערך תאים תפוסים
// [flase, flase, flase, flase, flase, ...]
bool Box::used[POOL_SIZE] = {false};

Box::Box() {
    cout << "Constructor Box\n";
}

Box::~Box() {
    cout << "Destructor Box\n";
}

// העמסת האופרטור NEW
void* Box::operator new(size_t) {
    for (int i = 0; i < POOL_SIZE; i++) {
        if (!used[i]) {
            used[i] = true;
            cout << "Allocating Box from pool index " << i << endl;
            return pool + i * sizeof(Box);
        }
    }
    throw bad_alloc();
}

// העמסת האופרטור DELETE
void Box::operator delete(void* ptr) noexcept {
    // חישוב (offset) כגודל מצביע
    const uintptr_t offset = static_cast<char*>(ptr) - pool;

    const size_t index = offset / sizeof(Box);

    used[index] = false;
    std::cout << "Freeing Box from pool index " << index << std::endl;
}

