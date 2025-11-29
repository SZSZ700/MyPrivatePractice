#include <windows.h>
#include <iostream>
#include <queue>
#include <stack>
#include "../Node/Node.h"
#include "../BinNode/BinNode.h"
#include <memory>
#include <cassert>
#include <limits>
#include "../Driver/DepartmentofTransportation.h"
#include "../Driver/Driver.h"
#include "../Driver/DriversDb.h"
#include "../Reminder/DailyReminder.h"
#include "../Reminder/Reminder.h"
using namespace std;

// ======================================================================
// A simple struct representing a person
// Contains public fields for name, age, and bmi
// ======================================================================
struct PersonOne {
    std::string name;   // person's name
    int age{};          // person's age (default 0)
    double bmi{};       // person's BMI (default 0.0)
};

// ======================================================================
// A base class representing a person with encapsulation
// Fields are protected and accessed via getters/setters
// Provides virtual methods for polymorphism
// ======================================================================
class PersonTwo {
protected:
    std::string name;   // protected: accessible to derived classes
    int age;            // protected: accessible to derived classes
    double bmi{};       // protected: accessible to derived classes

public:
    // default constructor, initializes with empty name and age 0
    PersonTwo() { this->name = ""; this->age = 0; }

    // parameterized constructor, initializes all fields
    PersonTwo(const std::string &name, const int age, const double bmi) {
        this->name = name;
        this->age = age;
        this->bmi = bmi;
    }

    // getter for name (returns by value)
    std::string getName() { return this->name; }
    // setter for name
    void setName(const std::string &name1) { this->name = name1; }

    // getter for age (const correctness)
    [[nodiscard]] int getAge() const { return this->age; }
    // setter for age
    void setAge(const int age1) { this->age = age1; }

    // getter for BMI (const correctness)
    [[nodiscard]] double getBmi() const { return this->bmi; }
    // setter for BMI
    void setBmi(const double bmi1) { this->bmi = bmi1; }

    // virtual method: can be overridden by derived classes
    virtual void speak() { std::cout << "Person speaking: " << this->name << std::endl; }

    // virtual method: string representation of the object
    virtual std::string toString() {
        std::stringstream ss;
        ss << "name: " << this->name << ", age: " << this->age << ", bmi: " << this->bmi << ".";
        return ss.str();
    }

    // virtual destructor to allow safe polymorphic deletion
    virtual ~PersonTwo() = default;
};

// ======================================================================
// A derived class representing a student
// Inherits from PersonTwo
// Adds a field "major" and overrides speak() and toString()
// ======================================================================
class Student final : public PersonTwo {
    std::string major; // student's major (field of study)
public:
    // default constructor
    Student() {
        this->major = "";
        setName("Unnamed"); // using base class setter
        setAge(0);          // using base class setter
    }

    // parameterized constructor
    Student(const std::string& n, const int a, const std::string& m) {
        this->name = n;     // accessing protected members from base
        this->age = a;
        this->major = m;
    }

    // setter for major
    void setMajor(const std::string& m) { major = m; }

    // override base class speak()
    void speak() override { std::cout << "Student of " << this->major << ": " << this->name << std::endl; }

    // override base class toString()
    std::string toString() override {
        std::stringstream ss;
        ss << "name: " << this->name << ", age: " << this->age << ", bmi: " << this->bmi
           << ", studying " << this->major << ".";
        return ss.str();
    }
};

// both are valid ways:
// change by pointer
void incPtr(int* p) { if (p != nullptr) { *p += 1; } }

// change by reference
void incRef(int& r) { r += 1; }

// Bubble Sort Algorithm
// Sorts the array in ascending order
void bsa(int arr[], const int size) {
    // outer loop -> number of passes (size - 1 passes are enough)
    for (int i = 0; i < size - 1; i++) {
        // inner loop -> compares adjacent elements in each pass
        for (int j = 0; j < size - i - 1; j++) {
            // if current element is greater than the next one
            if (arr[j] > arr[j + 1]) {
                // swap them to put the smaller element first
                std::swap(arr[j], arr[j + 1]);
            }
        }
    }
}

// remove duplicates from array
int* removeDuplicates(int arr[], const int size) {
    // if there are no elements, return an empty array
    if (size <= 0) { return new int[0]; }

    // sort the array so duplicates will be adjacent
    bsa(arr, size);

    // O(n) -> each element is visited exactly once
    // start with the original size
    auto sizet = size;

    // loop through the array to count duplicates
    for (auto i = 0; i < size - 1; i++) {
        // take the current value
        const int current = arr[i];
        // counter for how many times this value appears
        auto count = 0;

        // j will scan forward to count duplicates
        auto j = i;
        while (j < size && arr[j] == current) {
            count++; // increase the counter
            j++;    // move to the next element
        }

        // subtract duplicates (only keep one)
        sizet -= (count - 1);

        // skip all counted duplicates
        i += count - 1;
    }

    // allocate the new array with exact size
    const auto result = new int[sizet];
    int index = 0;

    // O(n) -> loop once more to copy unique elements
    for (int i = 0; i < size - 1; i++) {
        // if current element is different from the next, copy it
        if (arr[i] != arr[i + 1]) {
            result[index++] = arr[i];
        }
    }

    // always add the last element (not checked in loop above)
    // ReSharper disable once CppDFAUnusedValue
    result[index++] = arr[size - 1];

    // return the new array with unique values
    return result;
}

// ======================================================================
// Print a singly linked list forward
// Traverse the list from head to tail and print each value
// ======================================================================
template<typename T>
void PrintList(Node<T>* head) {
    // start from the head of the list
    Node<T>* cur = head;

    // iterate until reaching the end of the list
    while (cur != nullptr) {
        // print the value directly (no need to dereference)
        std::cout << *cur->getValue() << " ";

        // move to the next node
        cur = cur->getNext();
    }

    // end the lines after printing all values
    std::cout << std::endl;
}

// ======================================================================
// Free a singly linked list
// Delete all nodes in the list and free their memory
// ======================================================================
template<typename T>
void FreeList(Node<T>* head) {
    // start from the head
    Node<T>* cur = head;
    // iterate until reaching the end
    while (cur != nullptr) {
        // keep pointer to the next node
        Node<T>* nextNode = cur->getNext();
        // delete current node
        delete cur;
        // move to the next node
        cur = nextNode;
    }
}

// ======================================================================
// Print a doubly linked list forward
// Traverse from head to tail and print each value
// ======================================================================
template<typename T>
void PrintForward(BinNode<T>* head) {
    // start from the head of the doubly linked list
    BinNode<T>* cur = head;
    // iterate until reaching the end
    while (cur != nullptr) {
        // print the current node's value
        std::cout << *cur->getValue() << " ";
        // move to the next node
        cur = cur->getNext();
    }
    // end the line after printing all values
    std::cout << std::endl;
}

// ======================================================================
// Print a doubly linked list backward
// Traverse from tail to head and print each value
// ======================================================================
template<typename T>
void PrintBackward(BinNode<T>* tail) {
    // start from the tail of the doubly linked list
    BinNode<T>* cur = tail;
    // iterate until reaching the beginning
    while (cur != nullptr) {
        // print the current node's value
        std::cout << *cur->getValue() << " ";
        // move to the previous node
        cur = cur->getPrev();
    }
    // end the line after printing all values
    std::cout << std::endl;
}

// ======================================================================
// Free a doubly linked list
// Delete all nodes starting from the head and free their memory
// ======================================================================
template<typename T>
void FreeDList(BinNode<T>* head) {
    // start from the head
    BinNode<T>* cur = head;
    // iterate until reaching the end
    while (cur != nullptr) {
        // keep pointer to the next node
        BinNode<T>* next = cur->getNext();
        // delete current node
        delete cur;
        // move to the next node
        cur = next;
    }
}


// ================================================================================================
// O(n) - sort Node<int*> list, even values at the start of the list, then odd values after them
// change the head: bSortFirstWay.chain → main.chain → [Node<int*> chain]
// ================================================================================================
void bSortFirstWay(Node<int*> **chain) {
    if (chain == nullptr) { return; } // if the original list pointer is null

    Node<int*> *evenhead = nullptr; // head pointer for the even list
    Node<int*> *eventail = nullptr; // tail pointer for the even list
    Node<int*> *oddhead = nullptr;  // head pointer for the odd list
    Node<int*> *oddtail = nullptr;  // tail pointer for the odd list
    const Node<int*> *pos = *chain; // pointer for iterating over the original list

    // iterate over the original list and build the even and odd lists
    while (pos != nullptr) {
        int* value = pos->getValue();        // get the int* stored in the node
        auto *toAdd = new Node(value); // create new node pointing to the same int*

        // if the pointed value is even
        if (*value % 2 == 0) {
            if (evenhead == nullptr) {
                // if the even list is empty
                evenhead = toAdd;
                eventail = evenhead;
            } else {
                if (eventail != nullptr) {
                    // if the even list is not empty
                    eventail->setNext(toAdd);
                    eventail = eventail->getNext();
                }
            }
        } else {
            // if the pointed value is odd
            if (oddhead == nullptr) {
                // if the odd list is empty
                oddhead = toAdd;
                oddtail = oddhead;
            } else {
                // if the odd list is not empty
                if ( oddtail != nullptr) {
                    oddtail->setNext(toAdd);
                    oddtail = oddtail->getNext();
                }
            }
        }

        pos = pos->getNext(); // move to the next node in the list
    }

    // delete all nodes from the old version of the original list
    while (*chain != nullptr) {
        Node<int*> *next = (*chain)->getNext();
        delete *chain;
        *chain = next;
    }

    // connect the even list and the odd list
    if (eventail != nullptr) {
        eventail->setNext(oddhead);
        *chain = evenhead; // head points to the even list
    }

    // if there was no even list, but there is an odd list
    if (oddhead != nullptr && evenhead == nullptr) {
        *chain = oddhead; // head points to the odd list
    }
}


// O(n)^2 - sort Node list, even values at the start of the list, then after them the odd values
void bSortSecWay(const Node<int*> *chain) {
    const auto *pos = chain; // pointer for the list

    // iteration on the list
    while (pos != nullptr) {
        const auto *current = pos->getNext(); // pointer for iteration on the rest of the list

        // iteration on the rest of the list
        while (current != nullptr && current->getNext() != nullptr) {
            if (*current->getValue() % 2 == 1 && *current->getNext()->getValue() % 2 == 0) {
                std::swap(*current->getValue(), *current->getNext()->getValue()); // swap
            }
            current = current->getNext(); // move to the next node in the list
        }
        pos = pos->getNext();// move to the next node in the list
    }
}

// =====================================================================================
// buildList
// Create a singly linked list from an initializer list of integers
// Each integer value is dynamically allocated on the heap
// Returns a pointer to the head of the created list
// =====================================================================================
Node<int*>* buildList(const std::initializer_list<int> &values) {
    Node<int*>* head = nullptr;
    Node<int*>* tail = nullptr;

    for (const int v : values) {
        const auto val = new int(v);                // allocate int on the heap
        auto* toAdd = new Node(val);    // node stores int*

        if (head == nullptr) {
            head = toAdd;
            tail = head;
        } else {
            assert(tail != nullptr);
            tail->setNext(toAdd);
            tail = toAdd;
        }
    }

    return head;
}

// O(n)
bool isQsortUp(std::queue<int*>* q) {
    if (q->empty()) { return false; } // if the queue is empty → not considered sorted
    if (q->size() == 1) { return true; } // if there is only one element → automatically sorted

    std::queue<int*> restoration; // temporary queue to restore elements later
    bool flag = false;           // flag to detect if the queue is not sorted

    const auto first = q->front(); // take the first element as the "previous" value
    int prevValue = *first;
    q->pop();
    restoration.push(first); // save it for restoration

    // iterate through the rest of the queue
    while (!q->empty()) {
        const auto currentNum = q->front(); // get the current element
        q->pop();
        restoration.push(currentNum); // save it for restoration

        // check if the order is violated (not ascending)
        if (*currentNum < prevValue) { flag = true; }

        prevValue = *currentNum; // update prevValue for the next iteration
    }

    // restore the original queue from the temporary one
    while (!restoration.empty()) {
        q->push(restoration.front());
        restoration.pop();
    }

    // return true if sorted in ascending order, false otherwise
    return !flag;
}

// O(n)^2
bool isListConatinsSortedQueuesOnly(const Node< std::queue<int*>* > *chain) {
    // pointer to iterate over the linked list
    const Node<std::queue<int*>*> *pos = chain;

    // iterate over each node in the list
    while (pos != nullptr) {
        // extract the queue stored in the current node
        if (queue<int*> *current = pos->getValue(); !isQsortUp(current)) {
            // if the queue is not sorted → return false
            return false;
        }
        // move to the next node
        pos = pos->getNext();
    }

    return true; // if all queues were found are sorted, return true
}

// O(n)^3
bool validTree(BinNode< Node< std::queue<int*>* >* > *root) {
    if (root == nullptr) { return false; } // empty tree is not valid

    // BFS queue holding tree nodes
    std::queue< BinNode< Node< std::queue<int*>* >* >* > q;
    q.push(root); // offer the root to the search queue

    // iteration on the tree using the search queue
    while (!q.empty()) {
        // current BinNode
        const BinNode<Node<std::queue<int*>*>*> *current = q.front();
        q.pop(); // remove the current BinNode from the search queue

        // check if the linked list stored here is sorted
        // get the Node<queue<int>*> stored in the current BinNode
        if (const Node<std::queue<int*>*> *currentNode = current->getValue();
            !isListConatinsSortedQueuesOnly(currentNode)) { return false;}

        // push children for BFS
        if (current->getLeft() != nullptr) { q.push(current->getLeft()); } // push the left son
        if (current->getRight() != nullptr) { q.push(current->getRight()); } // push the right son
    }
    return true;
}

// ==============================================================================
// sizeOfLst
// Purpose: Count how many nodes exist in a singly linked list.
// Input:  pointer to the head node of Node<int*> list
// Output: integer count of nodes in the list
// Complexity: O(n)
// ==============================================================================
int sizeOfLst(const Node<int*> *chain) {
    int count = 0;                 // counter for the number of nodes
    const auto *pos = chain;       // pointer to iterate through the list

    // traverse until reaching the end of the list
    while (pos != nullptr) {
        count++;                   // increment count for each node
        pos = pos->getNext();      // move to the next node
    }
    return count;                  // return total size of the list
}

// ==============================================================================
// listToArray
// Purpose: Convert a linked list of Node<int*> into a dynamic array.
//          arr[0] = number of nodes in the list
//          arr[1 to size] = the integer values (not supporting dynamic data)
//          stored in each node
// Input:  pointer to the head node of Node<int*> list
// Output: pointer to a dynamically allocated int array
//         (caller must delete[] after use)
// Complexity: O(n)
// ==============================================================================
int* listToArray(const Node<int*> *chain) {
    const auto *pos = chain;       // pointer to iterate through the list
    const int size = sizeOfLst(chain); // get the size of the list

    // allocate array of size+1 (first element will hold the size)
    auto *arr = new int[size + 1];
    arr[0] = size;                 // store size in the first cell
    int index = 1;                 // index for filling the array

    // copy values from the list into the array
    while (pos != nullptr) {
        arr[index++] = *pos->getValue(); // dereference int* to get the value
        pos = pos->getNext();            // move to the next node
    }

    return arr;                    // return the array
}

// ==============================================================================
// isLstPali
// Purpose: Check if a linked list of Node<int*> is a palindrome.
//          A list is a palindrome if values read from left to right
//          are the same as from right to left.
// Method: Convert the list into an array and compare symmetric elements.
// Input:  pointer to the head node of Node<int*> list
// Output: true if the list is a palindrome, false otherwise
// Complexity: O(n) time, O(n) memory
// ==============================================================================
bool isLstPali(const Node<int*> *chain) {
    // if list is empty → consider it a palindrome
    if (chain == nullptr) {
        return true;
    }

    const int* arr = listToArray(chain); // convert list into an array
    const int size = arr[0];       // retrieve list size from first cell

    // check values symmetrically from both ends
    for (int i = 1; i <= size/2; i++) {
        // if mismatch found → not a palindrome
        if (arr[i] != arr[size - i + 1]) {
            delete[] arr;          // free memory before returning
            return false;
        }
    }

    delete[] arr;                  // free allocated memory
    return true;                   // return true if palindrome
}


// ==============================================================================
// QueueToArray
// Purpose: Convert a std::queue<int*> into a dynamic int array
// Method: Pop each element into a temp queue to restore order later
// Input:  pointer to std::queue<int*>
// Output: pointer to int array where:
//         arr[0] = number of elements
//         arr[1 to n] = integer values (not supporting dynamic data)
//         from front to rear
//         Caller must delete[] the array after use
// Complexity: O(n)
// ==============================================================================
// Function that converts a queue<int*> into a dynamic array of int* pointers
// The returned array has arr[0] = count (number of elements), and arr[1..count] = int* pointers
int** QueueToArray(std::queue<int*>* q) {
    // temporary queue to restore the original queue after processing
    std::queue<int*> temp;

    // counter for number of elements
    int count = 0;

    // first pass: move elements from original queue into temp and count them
    while (!q->empty()) {
        int* val = q->front();   // take pointer from front
        q->pop();                // remove from original queue
        temp.push(val);          // push into temp queue
        count++;                 // increase count
    }

    // allocate dynamic array of int* (count+1 because arr[0] holds count)
    const auto arr = new int*[count + 1];

    // store the number of elements in the first cell (as a pointer to int)
    arr[0] = new int(count);

    // index for filling the array (starting from 1)
    int index = 1;

    // second pass: restore queue and fill array with original pointers
    while (!temp.empty()) {
        int* val = temp.front();   // get element from temp
        temp.pop();                // remove from temp
        arr[index++] = val;        // put the pointer directly in the array
        q->push(val);              // restore element into the original queue
    }

    // return the array of int* pointers
    return arr;
}



// ==============================================================================
// StackToArray
// Purpose: Convert a std::stack<int*> into a dynamic int array
// Method: Pop into a temp stack to restore order later
// Input:  pointer to std::stack<int*>
// Output: pointer to int array where:
//         arr[0] = number of elements
//         arr[1 to n] = values (not supporting dynamic data) from bottom to top
//         Caller must delete[] the array after use
// Complexity: O(n)
// ==============================================================================
int** StackToArray(std::stack<int*>* s) {
    // temporary stack for restoring order
    std::stack<int*> temp;

    // count elements
    int count = 0;

    // first pass: pop everything into temp, count elements
    while (!s->empty()) {
        int* val = s->top();    // pointer to current top
        s->pop();               // remove from original
        temp.push(val);         // push into temp
        count++;                // increment count
    }

    // allocate array size+1
    const auto arr = new int*[count + 1];
    arr[0] = new int(count);             // first cell = size

    int index = 1;

    // second pass: pop from temp (bottom-to-top order), restore original stack
    std::stack<int*> restore;   // another stack to flip back

    while (!temp.empty()) {
        int* val = temp.top();   // get top of temp
        temp.pop();              // remove from temp
        arr[index++] = val;     // copy integer into array
        restore.push(val);       // push into restore stack
    }

    // now restore original stack in correct order
    while (!restore.empty()) {
        int* val = restore.top();
        restore.pop();
        s->push(val);
    }

    return arr;  // return built array
}

// O(n)^2
// Function that checks if all values in the first queue are smaller than all values in the second queue
bool isAllValuesInFirstQueueSmllerThanAllValuesInSecondQueue(std::queue<int*>* q1, std::queue<int*>* q2) {
    auto* temp1 = new std::queue<int*>(); // temporary queue to restore q1
    auto* temp2 = new std::queue<int*>(); // temporary queue to restore q2
    bool flag = false; // flag to detect if an invalid case is found

    // Iterate over the first queue
    while (!q1->empty()) {
        int* current = q1->front(); // take current element from q1
        temp1->push(current);       // store it in temp1
        q1->pop();                  // remove from q1

        // Iterate over the second queue to compare all its values with current
        while (!q2->empty()) {
            int* num = q2->front(); // take current element from q2
            temp2->push(num);       // store it in temp2
            q2->pop();              // remove from q2

            // If any value in q2 is smaller than current → condition fails
            if (*num < *current) { flag = true; }
        }

        // Restore q2 from temp2
        while (!temp2->empty()) {
            q2->push(temp2->front());
            temp2->pop();
        }
    }

    // Restore q1 from temp1
    while (!temp1->empty()) {
        q1->push(temp1->front());
        temp1->pop();
    }

    delete temp1; // free memory for temp1
    delete temp2; // free memory for temp2

    // Return true if condition is satisfied for all elements
    return !flag;
}

// O(n)^3
// Function that checks if a binary tree of queues is a "perfect tree of queues"
bool isPerfectTreeOfQueues(BinNode<std::queue<int*>*>* root) {
    // Create a BFS search queue
    // ReSharper disable once CppDFAMemoryLeak
    auto* q = new std::queue<BinNode<std::queue<int*>*>*>();
    q->push(root); // insert root node

    // Iterate over the tree using BFS
    while (!q->empty()) {
        const auto* currentFatherBinNode = q->front(); // get current node
        auto* fatherQueue = currentFatherBinNode->getValue(); // get its queue
        q->pop(); // remove node from search queue

        // If node has children → must have both left and right
        if (currentFatherBinNode->getLeft() != nullptr || currentFatherBinNode->getRight() != nullptr) {
            if (currentFatherBinNode->getLeft() == nullptr || currentFatherBinNode->getRight() == nullptr) {
                // If only one child exists → not perfect, clear queue and return false
                while (!q->empty()) { q->pop(); }
                delete q;
                return false;
            }

            // Left child queue
            std::queue<int*>* lefty = currentFatherBinNode->getLeft()->getValue();
            // Right child queue

            // Validate ordering constraints:
            // all values in father must be greater than all in left
            // all values in father must be smaller than all in right
            if (std::queue<int*>* righty = currentFatherBinNode->getRight()->getValue();
                !isAllValuesInFirstQueueSmllerThanAllValuesInSecondQueue(lefty, fatherQueue)
                    || !isAllValuesInFirstQueueSmllerThanAllValuesInSecondQueue(fatherQueue, righty)) {
                        while (!q->empty()) { q->pop(); } // clear queue before exit
                        delete q;
                        return false;
            }
        }

        // Add children to BFS queue if they exist
        if (currentFatherBinNode->getLeft() != nullptr) { q->push(currentFatherBinNode->getLeft()); }
        if (currentFatherBinNode->getRight() != nullptr) { q->push(currentFatherBinNode->getRight()); }
    }

    delete q; // free memory for BFS queue
    return true; // tree satisfies all conditions
}

// O(n)^4
bool islstContainsOnlyPerfectTreesOfQueues(const Node<BinNode<std::queue<int*>*>*>* chain) {
    auto *pos = chain;

    while (pos != nullptr) {
        if (!isPerfectTreeOfQueues(pos->getValue())) { return false; }
        pos = pos->getNext();
    }
    return true;
}

// ⤴️ ⤴️ //
void tryup_islstContainsOnlyPerfectTreesOfQueues() {
     // ------------------------------------------------------------------------
    // Create integers dynamically so we can store pointers inside the queues
    // ------------------------------------------------------------------------
    const auto a = new int(1);   // left queue values
    const auto b = new int(2);

    const auto c = new int(3);   // father queue values
    const auto d = new int(4);

    const auto e = new int(5);   // right queue values
    const auto f = new int(6);

    // ------------------------------------------------------------------------
    // Create three queues: left, father, right
    // ------------------------------------------------------------------------
    auto* leftQ = new queue<int*>();
    auto* fatherQ = new queue<int*>();
    auto* rightQ = new queue<int*>();

    leftQ->push(a);
    leftQ->push(b);

    fatherQ->push(c);
    fatherQ->push(d);

    rightQ->push(e);
    rightQ->push(f);

    // ------------------------------------------------------------------------
    // Build a perfect binary tree:
    //            fatherQ
    //           /       \
    //       leftQ       rightQ
    // ------------------------------------------------------------------------
    auto* root = new BinNode(fatherQ);
    root->setLeft(new BinNode(leftQ));
    root->setRight(new BinNode(rightQ));

    // ------------------------------------------------------------------------
    // Wrap this tree inside a chain (linked list of trees)
    // ------------------------------------------------------------------------
    const auto* chainNode = new Node<BinNode<queue<int*>*>*>(root);

    // ------------------------------------------------------------------------
    // Run the check on the single tree
    // ------------------------------------------------------------------------
    cout << "Is the tree perfect? "
         << (isPerfectTreeOfQueues(root) ? "YES" : "NO") << endl;

    // ------------------------------------------------------------------------
    // Run the check on the chain of trees
    // ------------------------------------------------------------------------
    cout << "Does the chain contain only perfect trees? "
         << (islstContainsOnlyPerfectTreesOfQueues(chainNode) ? "YES" : "NO") << endl;

    // ------------------------------------------------------------------------
    // Free allocated integers and structures (to avoid leaks)
    // ------------------------------------------------------------------------
    delete a; delete b; delete c; delete d; delete e; delete f;
    delete leftQ; delete fatherQ; delete rightQ;
    delete root->getLeft();
    delete root->getRight();
    delete root;
    delete chainNode;
}

// 1
// Build a NEW queue with NEW allocations, grouping equal values consecutively.
// The original 'q' is fully consumed. Caller must delete ints inside 'result'.
std::queue<int*>* OrderQ(std::queue<int*>* q) {
    // Lambda that decides if two pointers belong to the same group
    // null equals null; else compare values
    auto sameGroup = [&](const int* a, const int* b) -> bool {
        if (!a && !b) return true; // both null → same group
        if (!a || !b) return false; // exactly one is null → different groups
        return *a == *b; // both non-null → compare pointed values
    };

    // Queue for deferred NEW copies that do not match the current representative
    std::queue<int*> temp;

    // The resulting grouped queue (owns NEW allocations; caller must delete)
    auto* result = new std::queue<int*>();

    // Process until the original queue is completely consumed
    while (!q->empty()) {
        // Take representative from the front (original pointer)
        const int* number = q->front();
        q->pop();

        // Create NEW copy for the representative and push to result
        // ReSharper disable once CppDFAMemoryLeak
        int* first = number ? new int(*number) : nullptr;
        result->push(first);

        // We consumed 'number' from the original queue; delete the original pointer
        delete number;

        // Partition the remaining originals by equality with 'first'
        while (!q->empty()) {
            // Read next original pointer
            const int* nextnumber = q->front();
            q->pop();

            // Clone it into a NEW allocation (or nullptr passthrough)
            // ReSharper disable once CppDFAMemoryLeak
            int* next = nextnumber ? new int(*nextnumber) : nullptr;

            // We no longer need the original; delete it to avoid leaks
            delete nextnumber;

            // Group decision using the lambda (handles nullptr safely)
            if (sameGroup(first, next)) {
                // Same group → keep in result
                result->push(next);
            } else {
                //  group → defer for the next outer iteration
                temp.push(next);
            }
        }

        // Prepare the queue for the next representative from the deferred NEW copies
        while (!temp.empty()) {
            q->push(temp.front());
            temp.pop();
        }
    }

    // 'q' is now empty; 'result' holds all NEW allocations grouped consecutively
    return result;
}

// 2
/*
*א. נתונה מחסנית של מספרים שלמים. כל איבר במחסנית מופיע פעמיים בדיוק. המרחק בין שני
איברים במחסנית הוא כמות האיברים הנמצאים ביניהם.
כתבו פעולה המחזירה את המרחק הגדול ביותר בין שני איברים זהים במחסנית.
 */
// =======================================================
// 🧠 Checks if 'num' appears EXACTLY twice in the stack
// =======================================================
bool findTwiceInStk(std::stack<int*>* stk, const int num) {
    // 🚧 Guard against null stack pointer
    if (!stk) return false;

    // 🧱 Temporary stack to restore order later
    std::stack<int*> restore;

    // 🔢 Counter for occurrences of 'num'
    int count = 0;

    // 🔍 Scan phase — move all elements to 'restore' and count matches
    while (!stk->empty()) {
        // 👀 Look at top pointer (can be nullptr)
        int* p = stk->top();

        // 🧳 Move it to 'restore'
        restore.push(p);

        // 🧹 Pop from original stack
        stk->pop();

        // 🔁 If pointer not null and matches target — count it
        if (p && *p == num) ++count;
    }

    // 🔄 Restore original order
    while (!restore.empty()) {
        stk->push(restore.top());
        restore.pop();
    }

    // ✅ True iff value appears exactly twice
    return count == 2;
}

// =======================================================
// 🧩 Deep-copies a stack of int pointers (creates new ints)
// =======================================================
std::stack<int*>* deepCopyStack(std::stack<int*>* src) {
    // 🚧 Null check
    if (!src) return nullptr;

    // 🧱 Temp stacks
    std::stack<int*> temp;
    auto* result = new std::stack<int*>();  // 🧠 Heap-allocated copy

    // ⬇️ Drain source to 'temp' (reverse order)
    while (!src->empty()) {
        int* p = src->top();
        src->pop();
        temp.push(p);
    }

    // 🔁 Rebuild original + create deep copies
    while (!temp.empty()) {
        int* p = temp.top();
        temp.pop();

        // 🏗️ Restore original
        src->push(p);

        // 🧬 Create deep copy (new int or nullptr)
        // ReSharper disable once CppDFAMemoryLeak
        result->push(p ? new int(*p) : nullptr);
    }

    // ✅ Source restored, result ready
    // ReSharper disable once CppDFAMemoryLeak
    return result;
}

// =======================================================
// ✅ Verifies EVERY (non-null) value appears EXACTLY twice
// =======================================================
bool allFindTwiceOnceInStk(std::stack<int*>* stk) {
    // 🚧 Guard null input
    if (!stk) return false;

    // 🧬 Create deep copy for testing
    std::stack<int*>* clone = deepCopyStack(stk);

    // 🧱 Temporary stack to restore order
    std::stack<int*> restore;

    // 🟢 Assume true until proven otherwise
    bool ok = true;

    // 🔁 Traverse all elements
    while (!stk->empty()) {
        int* p = stk->top();
        restore.push(p);
        stk->pop();

        // 🧩 Check value existence in clone
        if (p) {
            if (!findTwiceInStk(clone, *p)) {
                ok = false;
                break; // 🚨 Stop early if one fails
            }
        }
    }

    // 🔄 Restore original stack
    while (!restore.empty()) {
        stk->push(restore.top());
        restore.pop();
    }

    // 🧹 Free deep-copied clone safely
    while (!clone->empty()) {
        delete clone->top();
        clone->pop();
    }
    delete clone;

    // ✅ Return overall result
    return ok;
}

// =======================================================
// 📏 Finds distance between two identical elements in stack
// =======================================================
int maxBetweenTwo(std::stack<int*>* stk, const int num) {
    if (!stk) return -1; // 🚧 Guard null input

    std::stack<int*> temp; // 🧱 Temp stack to restore order

    int count = 0; // 🔢 Counter for distance

    // 🔁 Iterate through stack
    while (!stk->empty()) {
        int* p = stk->top();
        stk->pop();
        temp.push(p);

        // 🎯 Found first occurrence
        if (p && *p == num) {
            // ⏱️ Count distance until second occurrence
            while (!stk->empty() && *stk->top() != num) {
                temp.push(stk->top());
                stk->pop();
                count++;
            }

            // 🚨 If reached bottom without finding a match
            if (stk->empty()) {
                // 🔄 Restore order before exiting
                while (!temp.empty()) {
                    stk->push(temp.top());
                    temp.pop();
                }
                return -1; // ❌ No second match
            }

            // 🎯 Second occurrence found — push it too
            temp.push(stk->top());

            // 🧹 Empty remaining stack into temp
            while (!stk->empty()) {
                temp.push(stk->top());
                stk->pop();
            }

            // 🔄 Restore full stack
            while (!temp.empty()) {
                stk->push(temp.top());
                temp.pop();
            }

            // ✅ Return distance between the two identical numbers
            return count;
        }
    }

    return -1; // ❌ Should never happen if every number appears twice
}

// =======================================================================
// 📏 Finds max distance between any two identical elements in the stack
// =======================================================================
int maxDis(std::stack<int*>* stk) {
    if (!stk) return -1; // 🚧 Guard null input
    if (!allFindTwiceOnceInStk(stk)) return -1; // 🧩 Only proceed if EVERY value appears exactly twice

    std::stack<int*>* copy = nullptr; // 🧬 Pointer for deep-copied stack
    int max = -1; // 🔢 Track maximum distance
    std::stack<int*> restore; // 🧱 Temporary stack to restore original state

    try {
        copy = deepCopyStack(stk); // 🏗️ Create deep copy for distance calculations

        // 🔁 Iterate through all elements in original stack
        while (!stk->empty()) {
            int* currentNumber = stk->top(); // 👀 Look at top
            restore.push(currentNumber);     // 🧳 Move to restore
            stk->pop();                      // 🧹 Remove from original

            // ⏱️ Compute distance using the deep copy, and then 🧮 Update maximum if needed
            // ReSharper disable once CppLocalVariableMayBeConst
            if (int distance = maxBetweenTwo(copy, *currentNumber); distance > max) max = distance;
        }
    }
    catch (const std::bad_alloc& e) {
        // ⚠️ Handle memory allocation failure gracefully
        std::cerr << "❌ Memory allocation failed in maxDis(): " << e.what() << std::endl;
        max = -1; // Default to error value
    }
    catch (...) {
        // ⚠️ Catch-all safeguard
        std::cerr << "❌ Unknown exception occurred in maxDis()" << std::endl;
        max = -1; // Default to error
    }

    // 🔄 Always restore original stack, even if exception
    while (!restore.empty()) {
        // ReSharper disable once CppDFANullDereference
        stk->push(restore.top());
        restore.pop();
    }

    // 🧹 Free copied stack safely
    while (!copy->empty()) {
        delete copy->top(); // Delete each deep copy
        copy->pop();
    }
    delete copy;


    // 🎯 Return maximum distance found (or -1 on error)
    return max;
}

// =======================================================================
// 🔢 Counts how many times 'num' appears in the linked list
// =======================================================================
int howMfInChain(Node<int*>* chain, const int num) {
    int count = 0;                      // 🧮 Counter for matches
    // ReSharper disable once CppLocalVariableMayBeConst
    Node<int*>* pos = chain;            // 📍 Traversal pointer

    while (pos) {                       // 🔁 Traverse the chain
        // ReSharper disable once CppTooWideScopeInitStatement
        // ReSharper disable once CppLocalVariableMayBeConst
        int* p = pos->getValue();       // 🔎 Get value pointer
        if (p && *p == num) count++;    // ✅ Count if matches
        pos = pos->getNext();           // ⏩ Move next
    }
    return count;                       // 🎯 Return total count
}

// =======================================================================
// 💎 Checks if the linked list is "perfect"
// =======================================================================
bool perfectoList(Node<int*>* chain) {
    // ReSharper disable once CppLocalVariableMayBeConst
    Node<int*>* pos = chain;            // 📍 Start from head

    while (pos) {                       // 🔁 Traverse chain
        // ReSharper disable once CppLocalVariableMayBeConst
        int* p = pos->getValue();       // 🔎 Get value pointer
        if (!p) {                       // 🚧 Skip null nodes
            pos = pos->getNext();
            continue;
        }

        // 🔢 Count total occurrences of current value in whole chain
        // ReSharper disable once CppLocalVariableMayBeConst
        int howmany = howMfInChain(chain, *p);

        // ➕ Positive values must appear an even number of times
        if (*p > 0 && howmany % 2 != 0) return false;

        // ➖ Negative values must appear an odd number of times
        if (*p < 0 && howmany % 2 == 0) return false;

        pos = pos->getNext();           // ⏩ Advance to next node
    }

    return true;                        // ✅ All checks passed
}

#include <unordered_map>  // 🧩 for std::unordered_map
// =======================================================================
// 💎 Checks if the linked list is "perfect" using a hash map
// =======================================================================
bool perfectoListUsingMap(Node<int*>* chain) {
    // 🧠 Hash map to count how many times each number appears
    std::unordered_map<int, int> freq;

    // 📍 Traverse the chain once to fill frequency map
    // ReSharper disable once CppLocalVariableMayBeConst
    Node<int*>* pos = chain;

    while (pos) {
        // ReSharper disable once CppLocalVariableMayBeConst
        if (int* p = pos->getValue()) { // 🚧 Skip nullptrs
            freq[*p]++; // 🔢 Increment count in map
        }
        pos = pos->getNext(); // ⏩ Move to next node
    }

    // 🧮 Now verify "perfect chain" rule for all recorded numbers
    // ReSharper disable once CppUseStructuredBinding
    for (const pair<const int, int>& pair : freq) {
        // ReSharper disable once CppLocalVariableMayBeConst
        int num = pair.first;         // 🔑 The actual number
        // ReSharper disable once CppLocalVariableMayBeConst
        int count = pair.second;      // 🔢 How many times it appeared

        // ➕ Positive numbers must appear an even number of times
        if (num > 0 && count % 2 != 0) return false;

        // ➖ Negative numbers must appear an odd number of times
        if (num < 0 && count % 2 == 0) return false;
    }

    // ✅ If all rules satisfied, the chain is perfect
    return true;
}

// 6 - a
/*
 *הגדרה :1 תור "מושלם N "הוא תור של מספרים שלמים המכיל את כל המספרים שבין 1 ל-אן כולל
 כתבו פעולה המקבלת תור ובודקת אם הוא מושלם
*/
// 🧠 Builds a “Super Perfect N” queue
// Keeps duplicates, removes invalid numbers, and adds missing ones (1..N)
void doItSuper(std::queue<int*>* q, const int n) {
    // 🚨 Basic validation: invalid pointer or non-positive N
    if (!q || n <= 0) return;

    int* arr = nullptr;  // 🧱 Pointer to frequency array

    try {
        // 🧮 Allocate an array of N integers to track presence
        arr = new int[n];
        // 🎯 Initialize all counts to 0
        for (int i = 0; i < n; ++i) arr[i] = 0;

        // 📦 Temporary queue for valid elements
        std::queue<int*> restore;

        // 🔁 Traverse all elements in the original queue
        while (!q->empty()) {
            // 📥 Take the front pointer from the queue
            int* currentNumber = q->front();
            // ⏩ Remove it from the original queue
            q->pop();

            // ✅ Keep only numbers within the valid range 1..N
            if (currentNumber && *currentNumber >= 1 && *currentNumber <= n) {
                arr[*currentNumber - 1]++;   // 🧮 Mark that number as seen
                restore.push(currentNumber); // ♻️ Keep this element in the new queue
            } else {
                delete currentNumber;         // 🧹 Delete invalid element
            }
        }

        // 🔄 Restore valid elements back to the original queue
        while (!restore.empty()) {
            q->push(restore.front());         // ♻️ Add element back
            restore.pop();                    // ⏩ Move to next
        }

        // ➕ Add any missing numbers from 1..N (no duplicates removed)
        for (int i = 0; i < n; ++i) {
            if (arr[i] == 0) {                // 🕳️ Number missing
                q->push(new int(i + 1));      // ➕ Add missing number safely
            }
        }

        // 🧹 Release allocated memory
        delete[] arr;
    }
    catch (const std::bad_alloc& e) {
        // 💥 Memory allocation failed
        std::cerr << "❌ Memory allocation failed in doItSuper(): " << e.what() << std::endl;

        // 🧹 Clean up if the array was partially allocated
        delete[] arr;
    }
    catch (...) {
        // 💣 Unexpected exception
        std::cerr << "⚠️ Unknown exception occurred in doItSuper()." << std::endl;

        // 🧹 Safe cleanup before exit
        delete[] arr;
    }
}


// ===========================================================
// 7 - a 🅐 ExistSum
// Checks if there are two elements in the stack whose sum = num
// ===========================================================
bool ExistSum(std::stack<int*>* stk, const int num) {
    // 🚨 Validation check - make sure the stack pointer is not null
    if (!stk) return false;

    // 🧩 Temporary stack to preserve original stack state
    std::stack<int*> restore;

    // 🔁 Outer loop: pick first number (a) until the stack becomes empty
    while (!stk->empty()) {
        // 📥 Take top pointer as candidate 'a'
        int* a = stk->top();

        // ⏩ Remove 'a' temporarily from the main stack
        stk->pop();

        // 🧩 Save 'a' in restore stack for later full restoration
        restore.push(a);

        // 🧱 Temporary stack for elements checked as 'b'
        std::stack<int*> temp;

        // 🔁 Inner loop: compare 'a' with every other element (b)
        while (!stk->empty()) {
            // ReSharper disable once CppLocalVariableMayBeConst
            // 📥 Take another pointer from the top as candidate 'b'
            int* b = stk->top();

            // 🧩 Push 'b' into temp stack so we can restore it later
            temp.push(b);

            // ⏩ Remove 'b' from the main stack to move deeper
            stk->pop();

            // ✅ Check if both pointers are valid and their sum equals num
            if (a && b && *a + *b == num) {
                // 🌀 First restore elements scanned in inner loop back to main stack
                while (!temp.empty()) {
                    // 🔙 Push element from temp back to main stack
                    stk->push(temp.top());
                    // 🧹 Remove the element from temp stack
                    temp.pop();
                }

                // 🌀 Then restore elements saved in restore stack back to main stack
                while (!restore.empty()) {
                    // 🔙 Push element from restore back to main stack
                    stk->push(restore.top());
                    // 🧹 Remove the element from restore stack
                    restore.pop();
                }

                // 🏁 Matching pair found - stack is fully restored, return true
                return true;
            }
        }

        // 🌀 Restore elements from temp after full inner loop (no pair with current 'a')
        while (!temp.empty()) {
            // 🔙 Push element from temp back to main stack
            stk->push(temp.top());
            // 🧹 Remove the element from temp stack
            temp.pop();
        }
    }

    // 🌀 Restore all elements from restore stack if no pair was found at all
    while (!restore.empty()) {
        // 🔙 Push element from restore back to main stack
        stk->push(restore.top());
        // 🧹 Remove the element from restore stack
        restore.pop();
    }

    // ❌ No matching pair found - return false
    return false;
}

// ===========================================================
// 🅑 MaxSum
// Finds the maximum sum of any two elements in the stack
// ===========================================================
int MaxSum(std::stack<int*>* stk) {
    // 🚨 Validation
    if (!stk || stk->size() < 2) return -1;

    int maxSum = std::numeric_limits<int>::min(); // 🧮 Start with the smallest int
    std::stack<int*> restore; // 📦 To restore later

    // 🔁 Outer loop: choose first number (a)
    while (!stk->empty()) {
        int* a = stk->top();   // 📥 Take top pointer
        stk->pop();            // ⏩ Remove temporarily
        restore.push(a);       // 🧩 Save for restore later

        // 📦 Copy of remaining elements
        std::stack<int*> temp;

        // 🔁 Compare with all others
        while (!stk->empty()) {
            // ReSharper disable once CppLocalVariableMayBeConst
            int* b = stk->top();
            temp.push(b);
            stk->pop();

            // ✅ Both valid pointers
            if (a && b) {
                // ReSharper disable once CppLocalVariableMayBeConst
                if (int sum = *a + *b; sum > maxSum)        // 🔼 Update max if larger
                    maxSum = sum;
            }
        }

        // ♻️ Restore original stack
        while (!temp.empty()) {
            stk->push(temp.top());
            temp.pop();
        }
    }

    // ♻️ Restore original stack
    while (!restore.empty()) {
        stk->push(restore.top());
        restore.pop();
    }

    // ✅ Return max sum found
    return maxSum;
}

// ===========================================================
// 🅒 Complexity Analysis (סיבוכיות זמן וזיכרון)
// ===========================================================
/*
🧮 ExistSum:
- Time: O(n²) → Every element compared with every other element.
- Space: O(n)  → Because of the copied temporary stack.

💪 MaxSum:
- Time: O(n²) → Same reason (nested comparisons).
- Space: O(n)  → Uses temporary stacks for scanning and restoration.
*/
void someTry0 () {
    //--------------------------------------------------------------------------//
    cout << " " <<endl;
    std::cout << "---- valid tree ----" << std::endl;
    // === Create queues of int* ===
    auto *q01 = new std::queue<int*>();
    q01->push(new int(3));
    q01->push(new int(5));

    auto *q02 = new std::queue<int*>();
    q02->push(new int(2));
    q02->push(new int(4));
    q02->push(new int(6));

    auto *q03 = new std::queue<int*>();
    q03->push(new int(10));
    q03->push(new int(20));
    q03->push(new int(25)); // not sorted on purpose

    // === Wrap each queue in a Node<queue<int*>*> ===
    auto* n1 = new Node(q01);
    auto* n2 = new Node(q02);
    auto* n3 = new Node(q03);

    // === Wrap each Node in a BinNode<Node<queue<int*>*>*> ===
    auto* b1 = new BinNode< Node< std::queue<int*>*>*>(n1); // root node
    auto* b2 = new BinNode< Node< std::queue<int*>*>*>(n2); // left child
    auto* b3 = new BinNode< Node< std::queue<int*>*>*>(n3); // right child

    // === Connect the binary tree ===
    b1->setLeft(b2);
    b1->setRight(b3);

    // === Run the validation ===
    if (validTree(b1)) {
        std::cout << "Tree is valid: all queues are sorted." << std::endl;
    } else {
        std::cout << "Tree is NOT valid: at least one queue is not sorted." << std::endl;
    }

    // === Memory cleanup ===
    // Delete all integers stored in the queues
    while (!q01->empty()) { delete q01->front(); q01->pop(); }
    while (!q02->empty()) { delete q02->front(); q02->pop(); }
    while (!q03->empty()) { delete q03->front(); q03->pop(); }

    // Delete Node wrappers
    delete n1;
    delete n2;
    delete n3;

    // Delete BinNode wrappers
    delete b1;
    delete b2;
    delete b3;

    // Delete queue
    delete q01;
    delete q02;
    delete q03;


    std::cout << " " << std::endl;
    std::cout << "-- list of trees of queues: --" << std::endl;
    tryup_islstContainsOnlyPerfectTreesOfQueues();

    std::cout<< " " <<endl;
    std::cout<< "Drivers: " <<endl;
    // ⤵️ create instance of Driver ⤵️:
    const auto* firstName = new std::string("shar"); // first name
    const auto* lastName = new std::string("cle"); // last name
    const auto* id = new int(123456789); // id
    const auto *age = new int(45); // age
    const auto *d1 = new Driver(firstName, lastName, id, age); // construct new driver
    cout<< "created new Driver: " << d1->toString() <<endl;
    cout<< " " << endl;

    // ⤵️ create instance of DriversDb ⤵️:
    std::cout<< "creating new DataBase..." <<endl;
    std::cout<< "add the new created Driver to the db..." <<endl;
    auto *database = new DriversDb(); // construct new DriversDb
    database->addDriver(d1); // add created driver to the db
    cout<< "if db empty? : " << database->isEmpty() << endl;
    cout<< "size of db: " << database->getSize() << endl;
    cout<< " all db content: " << database->toString() << endl;
    cout<< " " << endl;

    //⤵️ create instance of DepartmentofTransportation⤵️:
    cout<< "creating new DepartmentofTransportation... " << endl;
    auto *dept = new DepartmentOfTransportation();
    dept->setDriversDb(std::move(*database));

    cout<< " " << endl;
    cout<< "DepartmentofTransportation database content: " <<
        dept->toString() << endl;

    cout<< "free memory! " << endl;
    delete firstName;
    delete lastName;
    delete id;
    delete age;
    delete d1;
    delete database;
    delete dept;

    cout<< " " << endl;
}

void TryMe() {
    // 🎨 ANSI color codes for pretty output
    #define RESET   "\033[0m"     // Reset color
    #define RED     "\033[31m"    // Red text
    #define GREEN   "\033[32m"    // Green text
    #define GRAY    "\033[90m"    // Gray text
    #define CYAN    "\033[36m"    // Cyan text
    #define YELLOW  "\033[33m"    // Yellow text

    // 🧱 Reminder #1 — Ophthalmology
    const auto name1  = new string("David Levi");        // 👤 Customer name
    const auto phone1 = new string("054-1111111");       // ☎️ Phone number
    const auto inst1  = new string("Eye Clinic");        // 🏥 Institution name
    const auto date1  = new string("22/10/2025");        // 📅 Appointment date
    const auto hour1  = new int(9);                      // ⏰ Appointment hour
    const auto status1 = new int(0);                     // ⚪ No response

    const auto r1 = new Reminder(name1, phone1, inst1, date1, hour1); // 🚀 Create Reminder #1
    r1->setStatus(status1); // 🔢 Set status

    // 🧱 Reminder #2 — Heart Center:
    const auto name2  = new string("Sara Cohen");        // 👩 Customer name
    const auto phone2 = new string("054-2222222");       // ☎️ Phone number
    const auto inst2  = new string("Heart Center");      // ❤️ Institution name
    const auto date2  = new string("23/10/2025");        // 📅 Appointment date
    const auto hour2  = new int(11);                     // ⏰ Appointment hour
    const auto status2 = new int(2);                     // ❌ Canceled

    const auto r2 = new Reminder(name2, phone2, inst2, date2, hour2); // 🚀 Create Reminder #2
    r2->setStatus(status2); // 🔢 Set status

    // 🧱 Reminder #3 — Dentistry:
    const auto name3  = new string("Moshe Azulay");      // 👨 Customer name
    const auto phone3 = new string("054-3333333");       // ☎️ Phone number
    const auto inst3  = new string("Dental Clinic");     // 🦷 Institution name
    const auto date3  = new string("25/10/2025");        // 📅 Appointment date
    const auto hour3  = new int(10);                     // ⏰ Appointment hour
    const auto status3 = new int(1);                     // ✅ Approved

    const auto r3 = new Reminder(name3, phone3, inst3, date3, hour3); // 🚀 Create Reminder #3
    r3->setStatus(status3); // 🔢 Set status

    // 🧩 Build linked list of reminders:
    const auto first = new Node(r1);   // 🔗 Create first node
    const auto second = new Node(r2);  // 🔗 Create second node
    const auto third = new Node(r3);   // 🔗 Create third node

    first->setNext(second);            // 🔗 Connect 1 → 2
    second->setNext(third);            // 🔗 Connect 2 → 3


    // 🧩 Create and configure DailyReminder
    const auto d = new DailyReminder(); // 🧱 Create new daily reminder list
    d->setChain(first);                 // 📥 Transfer ownership of list


    // 🖨️ Print all reminders
    cout << CYAN << "\n=== 🗓️ All Reminders ===\n" << RESET;
    const Node<Reminder*>* allList = d->getChain();  // 📋 Get full chain
    const Node<Reminder*>* cur = allList;            // 📍 Iterator
    while (cur) {
        const Reminder* r = cur->getValue();         // 📦 Get current reminder

        // 🎨 Colorize by status
        if (const int* st = r->getStatus(); *st == 1)
            cout << GREEN;   // Approved
        else if (*st == 2)
            cout << RED;     // Canceled
        else
            cout << GRAY;    // No response

        r->print();          // 🖨️ Print reminder info
        cout << RESET;       // Reset color

        cur = cur->getNext(); // ⏩ Move to next
    }


    // 🔍 Filter by institution (Heart Center)
    cout << YELLOW << "\n=== ❤ Reminders from Heart Center ===\n" << RESET;
    const auto instQuery = new string("Heart Center");       // 📋 Create query institution name
    const Node<Reminder*>* filtered = d->getCustomersFromInstitution(instQuery); // 🔍 Filter chain

    auto pos = filtered; // 📍 Iterator for filtered list
    while (pos) {
        const Reminder* r = pos->getValue(); // 🎯 Current reminder

        // 🎨 Colorized output for filtered set
        if (const int* st = r->getStatus(); *st == 1)
            cout << GREEN;
        else if (*st == 2)
            cout << RED;
        else
            cout << GRAY;

        r->print();  // 🖨️ Print reminder
        cout << RESET;

        pos = pos->getNext(); // ⏩ Advance
    }

    // ========================================
    // 🧹 Clean up allocated memory
    // ========================================
    delete name1;
    delete phone1;
    delete inst1;
    delete date1;
    delete hour1;

    delete name2;
    delete phone2;
    delete inst2;
    delete date2;
    delete hour2;

    delete name3;
    delete phone3;
    delete inst3;
    delete date3;
    delete hour3;

    delete instQuery; // 🧹 Free the search institution string
    // 🧹 Free the temporary filtered chain
    // 🧹 Delete all nodes from the temporary filtered chain (but NOT the Reminders themselves)
    const Node<Reminder*>* temp = filtered;   // 📍 Iterator for deletion
    while (temp) {
        const Node<Reminder*>* next = temp->getNext();  // ⏩ Save next before deleting
        delete temp;                                   // ❌ Delete only the Node wrapper
        temp = next;                                   // Move to the next node
    }
    // ReSharper disable once CppDFAUnusedValue
    filtered = nullptr;  // 🚫 Safety: nullify pointer after deletion

    delete d;         // 🧹 Frees chain nodes and all Reminders inside
}

void lambdasTry() {
    std::cout << "lambdas Try: " << std::endl;

    // ==============================
    // 🧮 Part 1: Parameters as raw pointers
    // ==============================
    std::cout << "params: raw pointers: " << std::endl;

    const auto x = new int(8);      // allocate integer with value 8
    const auto y = new int(10);     // allocate integer with value 10

    // ⚙️ Lambda that takes two pointers and returns a NEW allocated sum
    auto add = [](const int* a, const int* b) -> int* {
        const auto z = new int(0);  // create new int on heap
        *z = *a + *b;               // store the sum
        return z;                   // return pointer to result
    };

    // 🚀 call the lambda and store result
    const int* result = add(x, y);

    // 🧹 clean up all allocated heap memory
    delete x;
    delete y;
    delete result;

    // ==============================
    // 🎯 Part 2: Capture by reference [&]
    // ==============================
    std::cout << "\ncatch: by reference: " << std::endl;

    const auto a1 = new int(1);   // allocate integer 1
    const auto a2 = new int(2);   // allocate integer 2

    // ⚙️ Lambda captures outer scope by reference, modifies directly
    auto changeValues = [&] {
        *a1 = 10;  // modify first pointer value
        *a2 = 20;  // modify second pointer value
    };

    // 🚀 execute lambda
    changeValues();

    // 🧹 clean up memory
    delete a1;
    delete a2;

    // ==============================
    // 🔗 Part 3: Change by double pointer (**)
    // ==============================
    cout << "\nchange by pointer to pointer: " << std::endl;

    auto r = new int(10);   // allocate integer 10
    auto s = new int(10);   // allocate integer 10

    int** rr = &r;          // pointer to pointer (address of r)
    int** ss = &s;          // pointer to pointer (address of s)

    // ⚙️ Lambda that takes double pointers and replaces their contents:
    auto changeByDoublePointer = [](int** rd, int** sd) {
        delete *rd;                  // delete old int at *rd
        delete *sd;                  // delete old int at *sd
        *rd = new int(111);          // assign new int(111)
        *sd = new int(111);          // assign new int(111)
    };

    // 🚀 Execute lambda
    changeByDoublePointer(ss, rr);

    // 🧹 clean up final allocations
    delete r;
    delete s;
}

void mipmap() {
    unordered_map<int*, string*> map;

    const auto id1 = new int(87);
    const auto name1 = new string("Sharb zar");
    const auto name2 = new string("Sh zr");
    const auto id2 = new int(97);

    map[id1] = name1;
    map[id2] = name2;

    // ReSharper disable once CppTemplateArgumentsCanBeDeduced
    for (const pair<int*, string*> p : map) {
        const int *copy = p.first;
        const string *s = p.second;
        cout << "i'D: " << *copy<< " ,full-name: "<< *s <<endl;

    }

    delete id1;
    delete name1;
    delete name2;
    delete id2;

}

// Tree of upList
bool isUpTreeList(BinNode<int*> *root) {
    // bfs queue
    const auto q = new queue<BinNode<int*>*>();
    // push root : BinNode to the search queue
    q->push(root);

    // iteration
    while (!q->empty()) {
        // pointer to the current first BinNode from the search queue
        const BinNode<int*> *currentBin = q->front();
        // remove current first BinNode from the search queue
        q->pop();

        // ever BinNode value should be positive, and should be even
        if (const int *currentValue = currentBin->getValue();
            *currentValue % 2 == 1 || *currentValue < 0) {
            // delete queue
            delete q;
            // return false, value is not positive or is not even
            return false;
        }

        // if this BinNode is a parent, it should have only one child
        if (currentBin->getLeft() || currentBin->getRight()) {

            // if current parent have two children, return false
            if (currentBin->getLeft() && currentBin->getRight()) {
                // delete queue
                delete q;
                // return false, parent can't have two children, it should have only one
                return false;
            }

            // son BinNode value should be bigger than its father BinNode value: //
            // if current parrent have left child
            // check this statement for left child
            if (currentBin->getLeft() && currentBin->getLeft()->getValue()) {
                if (currentBin->getLeft()->getValue() < currentBin->getValue()) {
                    // delete queue
                    delete q;
                    // return false, son BinNode value can't be smaller than father BinNode value
                    return false;
                }
            }

            // if current parrent have right child
            // check this statement for right child
            if (currentBin->getRight() && currentBin->getRight()->getValue()) {
                if (currentBin->getRight()->getValue() < currentBin->getValue()) {
                    // delete queue
                    delete q;
                    // return false, son BinNode value can't be smaller than father BinNode value
                    return false;
                }
            }
        }
    }

    // delete queue
    delete q;
    // tree is valid
    return true;
}

// 🧠 Returns the value at position 'pos' in the queue without removing it.
// If there is no such position, returns -1.
int* valueAt(std::queue<int*> *q, const int pos) {
    // Counter starts from 1 because the first element is at position 1
    int count = 1;

    // Temporary queue for restoration
    const auto temp = new std::queue<int*>();

    // Will hold the value at position 'pos' (copied)
    int *valAtPos = nullptr;

    // Iterate through the original queue
    while (!q->empty()) {
        // Get pointer to current front element
        int *x = q->front();

        // Remove it from the original queue
        q->pop();

        // If we reached the desired position, copy its value
        // ReSharper disable once CppDFAMemoryLeak
        if (count == pos) { valAtPos = new int(*x); }

        // Increase position counter
        count++;

        // Push the element into the temporary restoration queue
        temp->push(x);
    }

    // Restore all elements back to the original queue
    while (!temp->empty()) {
        q->push(temp->front());
        temp->pop();
    }

    // Delete the temporary queue to free memory
    delete temp;

    // If position not found, return -1
    if (!valAtPos)
        // ReSharper disable once CppDFAMemoryLeak
        valAtPos = new int(-1);

    // Return the found (or default) value
    return valAtPos;
}

// 🧩 Creates and returns a new queue that merges q1 and q2 by the following rule:
// First element from q1, last element from q2, second element from q1,
// element before last from q2, and so on.
// Assumes both queues have the same size.
std::queue<int*>* merge (std::queue<int*> *q1, std::queue<int*> *q2) {
    // Create a new queue for the merged result
    const auto merged = new std::queue<int*>();

    // Get sizes of both queues
    const int size1 = q1->size();
    // ReSharper disable once CppTooWideScopeInitStatement
    const int size2 = q2->size();

    // If sizes are not equal, return nullptr (invalid case)
    if (size1 != size2) { return nullptr; }

    // Iterate through both queues by position
    for (int i = 1; i <= size1; i++) {
        // Push i-th element from q1 (1-based index)
        merged->push(valueAt(q1, i));

        // Push (size - i + 1)-th element from q2 (reverse order)
        merged->push(valueAt(q2, size1 - i + 1));
    }

    // Return the new merged queue
    // ReSharper disable once CppDFAMemoryLeak
    return merged;
}

// 🔍 Get value from an array (vector-like) at given position (1-based index)
int* valueAt(int** arr, const int size, const int pos) {
    // ⚠️ Check invalid position
    if (pos < 1 || pos > size) return nullptr;

    // ✅ Return a copy of the value at that position (1-based)
    // ReSharper disable once CppDFAMemoryLeak
    return new int(*arr[pos - 1]);
}

// 🔄 Merge two queues into a new one (based on position logic)
std::queue<int*>* merge2(std::queue<int*>* q1, std::queue<int*>* q2) {
    // 🚫 Check if any queue is null
    if (!q1 || !q2) return nullptr;

    // 🧠 Create new empty queue to store merged result
    // ReSharper disable once CppDFAMemoryLeak
    auto* merged = new std::queue<int*>();

    // 📏 Get sizes of both queues
    const int size1 = q1->size();
    const int size2 = q2->size();

    // ⚠️ If not same size, return nullptr (invalid merge)
    if (size1 != size2) return nullptr;

    // 🧩 Create dynamic arrays to store queue elements
    const auto arr1 = new int*[size1];
    const auto arr2 = new int*[size2];

    // 🔁 Copy q1 → arr1
    for (int i = 0; i < size1; i++) {
        arr1[i] = q1->front();
        q1->pop();
    }

    // 🔁 Copy q2 → arr2
    for (int i = 0; i < size2; i++) {
        arr2[i] = q2->front();
        q2->pop();
    }

    // ⚡ Merge using valueAt() with O(1) access
    for (int i = 1; i <= size1; i++) {
        // 👈 Add i-th element from arr1
        merged->push(valueAt(arr1, size1, i));

        // 👉 Add (reverse) element from arr2
        merged->push(valueAt(arr2, size2, size2 - i + 1));
    }

    // 🧹 Free arrays (not values, they belong to queues)
    delete[] arr1;
    delete[] arr2;

    // ✅ Return merged queue
    // ReSharper disable once CppDFAMemoryLeak
    return merged;
}

#include "..//AppartmentsQuestion/Appartment.h"
#include "..//AppartmentsQuestion/GardenApp.h"
#include "..//AppartmentsQuestion/Luxary.h"
#include "..//AppartmentsQuestion/Penthouse.h"
void twentyTwentyTwoSummerA() {
    // 1-a
    auto HefreshimList = [&] (const Node<int*> *chain) -> Node<int*>* {
        if (!chain) { return nullptr; }

        Node<int*> *head = nullptr;
        Node<int*> *tail = nullptr;

        const Node<int*> *pos = chain;

        while (pos->getNext()) {
            if (pos->getValue() && pos->getNext()->getValue()) {
                // ✅ dereference values correctly
                const auto difference = new int(std::abs(*pos->getValue() - *pos->getNext()->getValue()));

                // ReSharper disable once CppTemplateArgumentsCanBeDeduced
                // ReSharper disable once CppDFAMemoryLeak
                const auto toAdd = new Node<int*>(difference);

                if (!head) {
                    head = toAdd;
                    tail = toAdd;
                } else {
                    // ReSharper disable once CppDFANullDereference
                    tail->setNext(toAdd);
                    tail = tail->getNext();
                }
            }

            pos = pos->getNext();
        }

        return head;
    };

    // 1-b
    auto theSurvives = [&] (Node<int*> *chain) -> int* {
        // ReSharper disable once CppDFAMemoryLeak
        if (!chain) { return new int(-1); }

        BinNode<Node<int*>*> *newey = nullptr;
        BinNode<Node<int*>*> *tail = nullptr;

        auto toAdd = new BinNode(chain);
        newey = toAdd;
        tail = toAdd;

        while (true) {
            // ReSharper disable once CppDFAMemoryLeak
            if (tail && tail->getValue()) {
                // preventing infinite loop
                if (!tail->getValue()->getNext()) { break; }

                tail->setRight(new BinNode(HefreshimList(tail->getValue())));
                // ReSharper disable once CppDFANullDereference
                tail->getRight()->setLeft(tail);

                // ✅ print chain values
                const Node<int*>* pos = tail->getValue();
                while (pos) {
                    std::cout << *pos->getValue() << " ";
                    pos = pos->getNext();
                }
                std::cout << std::endl;

                tail = tail->getRight();
            } else { break; }
        }

        // ✅ Save survivor value before deletion
        // ReSharper disable once CppDFAMemoryLeak
        const auto survivor = new int(-1);
        // ReSharper disable once CppDFANullDereference
        if (const Node<int*>* lastList = tail->getValue(); lastList && lastList->getValue()) {
            *survivor = *lastList->getValue();
        }

        // ✅ FREE_MEMORY_AREA
        const BinNode<Node<int*>*> *pos = newey; // delete all created lists - NO_MEMORY_LEAK

        while (pos) {
            const BinNode<Node<int*>*> *next = pos->getRight(); //🔁

            //❗❗DELETE POS->GETVALUE()❗❗//
            const Node<int*> *current = pos->getValue();

            while (current) {
                const Node<int*> *nextToCurrent = current->getNext(); //🔁
                delete current->getValue(); // delete integer
                delete current; // delete node
                current = nextToCurrent; //🔁
            }

            delete pos; // delete BinNode
            pos = next; //🔁
        }

        // ✅ return survivor
        // ReSharper disable once CppDFAMemoryLeak
        return survivor;
    };

    auto example = [theSurvives] () {
        // create Node-list
        // ReSharper disable once CppDFAMemoryLeak
        auto *a = new Node(new int(5));
        // ReSharper disable once CppDFAMemoryLeak
        auto *b = new Node(new int(20));
        // ReSharper disable once CppDFAMemoryLeak
        auto *c = new Node(new int(9));
        // ReSharper disable once CppDFAMemoryLeak
        auto *d = new Node(new int(6));
        // ReSharper disable once CppDFAMemoryLeak
        auto *e = new Node(new int(5));
        // ReSharper disable once CppDFAMemoryLeak
        auto *f = new Node(new int(8));
        // ReSharper disable once CppDFAMemoryLeak
        auto *g = new Node(new int(2));
        a->setNext(b);
        b->setNext(c);
        c->setNext(d);
        d->setNext(e);
        e->setNext(f);
        f->setNext(g);
        int *lastSurvivor = theSurvives(a);
        std::cout << lastSurvivor << std::endl;
        delete lastSurvivor;
    };

    example();

    [[maybe_unused]] auto sumQ = [] (std::queue<int*>*q) -> int {
        if (!q) { return -1; }

        const auto temp = new queue<int*>();
        auto sum_q = 0;

        while (!q->empty()) {
            const auto tempNum = q->front();
            sum_q += *tempNum;
            q->pop();
            temp->push(tempNum);
        }

        while (!temp->empty()) {
            q->push(temp->front());
            temp->pop();
        }

        delete temp;
        return sum_q;
    };

    [[maybe_unused]] auto lastValue = [] (std::queue<int*>*q) -> int {
        if (!q) { return -1; }

        int last = 0;
        const auto temp = new queue<int*>();

        while (!q->empty()) {
            auto tempNum = q->front();
            q->pop();
            last = *tempNum;
            temp->push(tempNum);
        }

        while (!temp->empty()) {
            q->push(temp->front());
            temp->pop();
        }

        delete temp;
        return last;
    };

    [[maybe_unused]] auto qList = [&] (const Node<std::queue<int*>*> *chain) -> std::queue<int*>* {
        const auto q = new std::queue<int*>();
        auto pos = chain;

        while (pos) {
            const auto currentQueue = pos->getValue();
            // ReSharper disable once CppTooWideScopeInitStatement
            const auto firstValue = currentQueue->front();

            if (firstValue && *firstValue % 2 == 1) {
                q->push(new int(sumQ(currentQueue)));
            }else if (firstValue && *firstValue % 2 == 0) {
                q->push(new int(lastValue(currentQueue)));
            }

            pos = pos->getNext();
        }

        return q;
    };

    [[maybe_unused]] auto checkOp = [&] (Appartment* const* apps, const int size, int& regularCount,
        int& gardenCount,
        int& penthouseCount,
        int& luxaryCount) -> void{

        // Counts how many apartments of each type exist in the given array
        regularCount = 0; // Initialize regular apartment counter
        gardenCount = 0; // Initialize garden apartment counter
        penthouseCount = 0; // Initialize penthouse apartment counter
        luxaryCount = 0; // Initialize luxury apartment counter

        if (!apps || size <= 0) return; // If the array is null or size is not positive, there is nothing to count

        for (auto i = 0; i < size; i++) { // Iterate over all apartments in the array
            const auto app = apps[i]; // Get pointer to the current apartment

            if (!app) continue; // If the pointer is null, skip this element

            // If the object is a GardenApp, Increase garden apartment counter
            if (dynamic_cast<GardenApp*>(app)) gardenCount++;

            // If the object is a Penthouse, Increase penthouse counter
            else if (dynamic_cast<Penthouse*>(app)) penthouseCount++;

            // If the object is a Luxary, Increase luxury apartment counter
            else if (dynamic_cast<Luxary*>(app)) luxaryCount++;

            // If it is not any of the derived types, Count it as a regular Appartment
            else regularCount++;
        }
    };
}

int main() {
    system("chcp 65001 > nul"); // 💡 Change console to UTF-8 mode (Windows CMD command)
    someTry0();
    TryMe();
    cout<<""<<endl;
    twentyTwentyTwoSummerA();
    // program finished successfully
    return 0;
}
/*
void ByValue(Node head); מפנה לאובייקט אחר בזיכרון מועתק, המכיל את אותו תוכן
main.head ⟶ [Node(90)]     func.head ⟶ [Node(90)]

void ByReference(Node& head); // עוד משתנה שמכיל את אותו כתובת
main.head = func.head ⟶ [Node(70)]

void ByPointer(Node* head); // פויינטר נוסף אחר לאובייקט
main.head ⟶ [Node(80)] ⟵ func.head

void ByPointerToPointer(Node** head); // פויינטר לפויינטר כך שיהיה ניתן לשנות את המצביע המקורי
func.head ⟶ main.head ⟶ [Node(100)]
 */