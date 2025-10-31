#ifndef UNTITLED1_BOX_H
#define UNTITLED1_BOX_H
#include <iostream>
class Box {
public:
    int x{};

    static constexpr int POOL_SIZE = 10; // אורך המערך הקצאות

    Box();
    ~Box();

    void* operator new(size_t size);
    void operator delete(void* ptr) noexcept;

private:
    static char pool[]; // מערך הקצאות
    static bool used[]; // מערך תאים תפוסים
};
#endif //UNTITLED1_BOX_H