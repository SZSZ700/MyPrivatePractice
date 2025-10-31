#ifndef UNTITLED1_PRIORITYQUEUE_H
#define UNTITLED1_PRIORITYQUEUE_H
#include <vector>
#include <stdexcept>
#include <sstream>
// =======================================
// PriorityQueue<T>
// A max-heap based priority queue (like std::priority_queue)
// Supports push, top, pop, empty, size
// =======================================
template<typename T>
class PriorityQueue {
    std::vector<T> heap; // underlying container

    // helper: swap two elements
    void swap(int i, int j) {
        T tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    // helper: move element up to maintain heap property
    void heapifyUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap[i] > heap[parent]) {
                swap(i, parent);
                i = parent;
            } else break;
        }
    }

    // helper: move element down to maintain heap property
    void heapifyDown(int i) {
        const int n = heap.size();
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;
            if (left < n && heap[left] > heap[largest]) largest = left;
            if (right < n && heap[right] > heap[largest]) largest = right;
            if (largest != i) {
                swap(i, largest);
                i = largest;
            } else break;
        }
    }

public:
    // push - insert new value
    void push(const T& val) {
        heap.push_back(val);           // add at the end
        heapifyUp(heap.size() - 1);    // restore heap property
    }

    // top - get the largest element
    const T& top() const {
        if (heap.empty()) throw std::runtime_error("PriorityQueue is empty");
        return heap[0];
    }

    // pop - remove largest element
    void pop() {
        if (heap.empty()) throw std::runtime_error("PriorityQueue is empty");
        heap[0] = heap.back();         // move last to root
        heap.pop_back();               // remove last
        if (!heap.empty()) heapifyDown(0);
    }

    // empty - check if queue is empty
    [[nodiscard]] bool empty() const { return heap.empty(); }

    // size - return number of elements
    [[nodiscard]] int size() const { return heap.size(); }

    // toString - show heap contents (for debugging)
    [[nodiscard]] std::string toString() const {
        std::stringstream ss;
        for (size_t i = 0; i < heap.size(); i++) {
            ss << heap[i];
            if (i < heap.size() - 1) ss << " ";
        }
        return ss.str();
    }
};
#endif //UNTITLED1_PRIORITYQUEUE_H