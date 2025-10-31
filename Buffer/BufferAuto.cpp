#include "BufferAuto.h"
#include <iostream>
#include <memory>

BufferAuto::BufferAuto(std::unique_ptr<int[]> arr, const int size){
        this->data = std::move(arr);
        this->size = size;
    }

void BufferAuto::print() const {
    for (int i = 0; i < this->size; i++) {
        std::cout << this->data[i] << " ";
        std::cout << std::endl;
    }
}

/*
int main() {
    auto arr = make_unique<int[]>(5);
    for (int i = 0; i < 5; i++) { arr[i] = i; }

    const BufferAuto buffer(std::move(arr), 5);
    buffer.print();
}
*/