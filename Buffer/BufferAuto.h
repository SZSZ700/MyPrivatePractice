#ifndef UNTITLED1_BUFFERAUTO_H
#define UNTITLED1_BUFFERAUTO_H

#include <memory>

class BufferAuto {
    std::unique_ptr<int[]> data;
    int size;

    public:
    // 1.) constructor
    BufferAuto(std::unique_ptr<int[]> arr, int size);
    // 2.) print
    void print() const;
};

#endif //UNTITLED1_BUFFERAUTO_H