#include "StarCount.h" // Include the StarCount header
#include <assert.h> // For assert
#include <stack> // For std::stack
#include <sstream> // For std::stringstream

// constructor
StarCount::StarCount(const int *num) { // Constructor that builds a linked list representation of the number
    if (!num) { return; } // If the input pointer is null, do nothing and leave the list empty

    const auto temp = new int(*num); // Create a copy of the original number on the heap

    const auto stk = new std::stack<int*>(); // Create an auxiliary stack to reverse the digit order

    while (*temp > 0) { // Loop while there are still digits in the number
        auto currentDigit = new int(*temp % 10); // Extract the last digit and allocate it on the heap
        stk->push(currentDigit); // Push the digit pointer onto the stack
        *temp /= 10; // Remove the last digit from the copied number
    }

    delete temp; // Delete the copied number after all digits were extracted

    while (!stk->empty()) { // While the stack still holds digits
        const auto currentDigit = stk->top(); // Get the top digit pointer from the stack
        auto toAdd = new Node(currentDigit); // Create a new list node that stores this digit pointer

        if (this->number == nullptr) { // If the list is currently empty
            this->number = toAdd; // Set head pointer to the newly created node
            this->tail = toAdd; // Set tail pointer to the same node
        } else { // If the list is not empty
            assert(this->tail != nullptr); // Ensure the tail pointer is not null
            this->tail->setNext(toAdd); // Link the new node after the current tail
            this->tail = toAdd; // Move the tail pointer to the new node
        }

        stk->pop(); // Remove the top digit pointer from the stack
    }

    delete stk; // Delete the auxiliary stack
}

// destructor
StarCount::~StarCount() { // Destructor that releases all dynamic memory used by the list
    const Node<int*>* curr = this->number; // Start from the head of the list

    while (curr != nullptr) { // Traverse all nodes in the list
        const Node<int*>* temp = curr->getNext(); // Store pointer to the next node
        const int* digitPtr = curr->getValue(); // Get the digit pointer stored in the current node

        delete digitPtr; // Delete the digit pointer
        delete curr; // Delete the current node

        curr = temp; // Move to the next node
    }

    this->number = nullptr; // Set head pointer to null after deletion
    this->tail = nullptr; // Set tail pointer to null after deletion
}

// copy constructor
StarCount::StarCount(const StarCount &other) { // Copy constructor that performs a deep copy
    const Node<int*>* pos = other.number; // Start from the head of the other list

    while (pos != nullptr) { // Traverse all nodes of the other list
        const int* otherDigitPtr = pos->getValue(); // Get the digit pointer from the other list node
        int* newDigitPtr = nullptr; // Initialize local digit pointer for the new list

        if (otherDigitPtr) { // If the digit pointer is not null
            newDigitPtr = new int(*otherDigitPtr); // Allocate and copy the digit value
        }

        auto toAdd = new Node(newDigitPtr); // Create a new node that stores the copied digit pointer

        if (!this->number) { // If this list is currently empty
            this->number = toAdd; // Set head pointer to the new node
            this->tail = toAdd; // Set tail pointer to the new node
        } else { // If this list is not empty
            assert(this->tail != nullptr); // Ensure the tail pointer is not null
            this->tail->setNext(toAdd); // Link the new node after the current tail
            this->tail = toAdd; // Move the tail pointer to the new node
        }

        pos = pos->getNext(); // Move to the next node in the other list
    }
}

// copy assignment
StarCount& StarCount::operator=(const StarCount &other) noexcept { // Copy assignment operator that performs deep copy
    if (this != &other) { // Protect against self-assignment
        const Node<int*>* curr = this->number; // Start from the head of the current list

        while (curr != nullptr) { // Traverse and delete all nodes of the current list
            const Node<int*>* temp = curr->getNext(); // Store pointer to the next node
            const int* digitPtr = curr->getValue(); // Get the digit pointer from the current node

            delete digitPtr; // Delete the digit pointer
            delete curr; // Delete the current node

            curr = temp; // Move to the next node
        }
        this->number = nullptr; // Reset head pointer to null
        this->tail = nullptr; // Reset tail pointer to null

        const Node<int*>* pos = other.number; // Start from the head of the other list
        while (pos != nullptr) { // Traverse all nodes of the other list
            const int* otherDigitPtr = pos->getValue(); // Get the digit pointer from the other list node
            int* newDigitPtr = nullptr; // Initialize new digit pointer for this list

            if (otherDigitPtr) { // If the digit pointer is not null
                newDigitPtr = new int(*otherDigitPtr); // Allocate and copy the digit value
            }

            auto toAdd = new Node(newDigitPtr); // Create a new node for this list

            if (!this->number) { // If this list is empty
                this->number = toAdd; // Set head pointer to the new node
                this->tail = toAdd; // Set tail pointer to the new node
            } else { // If this list is not empty
                assert(this->tail != nullptr); // Ensure the tail pointer is not null
                this->tail->setNext(toAdd); // Link the new node after the current tail
                this->tail = toAdd; // Move the tail pointer to the new node
            }

            pos = pos->getNext(); // Move to the next node in the other list
        }
    }
    return *this; // Return reference to this object
}

// move constructor
StarCount::StarCount(StarCount &&other) noexcept { // Move constructor that steals the list from another object
    this->number = other.number; // Steal the head pointer from the other object
    this->tail = other.tail; // Steal the tail pointer from the other object

    other.number = nullptr; // Reset other object's head pointer to null
    other.tail = nullptr; // Reset other object's tail pointer to null
}

// move assignment
StarCount& StarCount::operator=(StarCount &&other) noexcept { // Move assignment operator that steals the list
    if (this != &other) { // Protect against self-assignment
        const Node<int*>* curr = this->number; // Start from the head of the current list

        while (curr != nullptr) { // Traverse and delete all nodes of the current list
            const Node<int*>* temp = curr->getNext(); // Store pointer to the next node
            const int* digitPtr = curr->getValue(); // Get the digit pointer from the current node

            delete digitPtr; // Delete the digit pointer
            delete curr; // Delete the current node

            curr = temp; // Move to the next node
        }

        this->number = other.number; // Steal the head pointer from the other object
        this->tail = other.tail; // Steal the tail pointer from the other object

        other.number = nullptr; // Reset other object's head pointer to null
        other.tail = nullptr; // Reset other object's tail pointer to null
    }
    return *this; // Return reference to this object
}

// get
const Node<int*> *StarCount::getNumber() const { // Getter for the head of the list
    return this->number; // Return the head pointer of the list
}

// set
void StarCount::setNumber(const Node<int*> *numberr) { // Setter that replaces the entire list with a deep copy of another list
    const Node<int*>* curr = this->number; // Start from the head of the current list

    while (curr != nullptr) { // Traverse and delete all nodes of the current list
        const Node<int*>* temp = curr->getNext(); // Store pointer to the next node
        const int* digitPtr = curr->getValue(); // Get the digit pointer from the current node

        delete digitPtr; // Delete the digit pointer
        delete curr; // Delete the current node

        curr = temp; // Move to the next node
    }
    this->number = nullptr; // Reset head pointer to null
    this->tail = nullptr; // Reset tail pointer to null

    const Node<int*>* pos = numberr; // Start from the head of the given list

    while (pos != nullptr) { // Traverse all nodes of the given list
        const int* otherDigitPtr = pos->getValue(); // Get the digit pointer from the given list node
        int* newDigitPtr = nullptr; // Initialize new digit pointer for this list

        if (otherDigitPtr) { // If the digit pointer is not null
            newDigitPtr = new int(*otherDigitPtr); // Allocate and copy the digit value
        }

        auto toAdd = new Node(newDigitPtr); // Create a new node for this list

        if (!this->number) { // If this list is empty
            this->number = toAdd; // Set head pointer to the new node
            this->tail = toAdd; // Set tail pointer to the new node
        } else { // If this list is not empty
            assert(this->tail != nullptr); // Ensure the tail pointer is not null
            this->tail->setNext(toAdd); // Link the new node after the current tail
            this->tail = toAdd; // Move the tail pointer to the new node
        }

        pos = pos->getNext(); // Move to the next node in the given list
    }
}

// tostring
std::string StarCount::toString() const { // Converts the number list into a string of digits
    std::stringstream ss; // String stream used to build the output

    const Node<int*>* curr = this->number; // Start from the head of the list
    if (!curr) { // If the list is empty
        ss << "EMPTY"; // Represent an empty number as the word "EMPTY"
        return ss.str(); // Return the string "EMPTY"
    }

    while (curr != nullptr) { // Traverse all nodes in the list
        const int* digitPtr = curr->getValue(); // Get the digit pointer from the current node
        const int digit = digitPtr ? *digitPtr : 0; // Read the digit value or use 0 if the pointer is null
        ss << digit; // Append the digit to the string stream

        curr = curr->getNext(); // Move to the next node in the list
    }

    return ss.str(); // Return the final string containing all digits
}

// ReSharper disable once CppMemberFunctionMayBeConst
Node<int *> *StarCount::buildlist(const int *num)  {
    assert(num != nullptr); // If the input pointer is null, do nothing and leave the list empty
    // head pointer for the new list
    Node<int*> *head = nullptr;
    // tail pointer for the new list
    Node<int*> *taily = nullptr;

    const auto temp = new int(*num); // Create a copy of the original number on the heap

    const auto stk = new std::stack<int*>(); // Create an auxiliary stack to reverse the digit order

    while (*temp > 0) { // Loop while there are still digits in the number
        auto currentDigit = new int(*temp % 10); // Extract the last digit and allocate it on the heap
        stk->push(currentDigit); // Push the digit pointer onto the stack
        *temp /= 10; // Remove the last digit from the copied number
    }

    delete temp; // Delete the copied number after all digits were extracted

    while (!stk->empty()) { // While the stack still holds digits
        const auto currentDigit = stk->top(); // Get the top digit pointer from the stack
        auto toAdd = new Node(currentDigit); // Create a new list node that stores this digit pointer

        if (head == nullptr) { // If the list is currently empty
            head = toAdd; // Set head pointer to the newly created node
            taily = toAdd; // Set tail pointer to the same node
        } else { // If the list is not empty
            assert(taily != nullptr); // Ensure the tail pointer is not null
            taily->setNext(toAdd); // Link the new node after the current tail
            taily = toAdd; // Move the tail pointer to the new node
        }

        stk->pop(); // Remove the top digit pointer from the stack
    }

    delete stk; // Delete the auxiliary stack

    return head;
}

// ReSharper disable once CppMemberFunctionMayBeConst
void StarCount::fixNumber() {
    [[maybe_unused]] auto sizeOfNumber = [&](const int *num) -> int { // Lambda to calculate how many digits the number has
        auto size = 0; // Counter for the number of digits
        auto temp = *num; // Local copy of the number value

        while (temp > 0) { // Loop while there are still digits
            temp /= 10; // Remove the last digit
            size++; // Increase the digit counter
        }

        return size; // Return the total number of digits
    };

    // If the head node holds a multi-digit number
    if (this->number && this->number->getValue() && sizeOfNumber(this->number->getValue()) > 1) {
        const Node<int*>* oldHead = this->number; // Store the old head pointer
        // Store the node that comes after the old head in the original list
        const auto after = oldHead->getNext();
        // Build a new list of single-digit nodes from the head value
        const auto Head = buildlist(oldHead->getValue());
        // Start from the head of the new list
        auto Tail = Head;

        // Move Tail to the last node of the new list
        while (Tail->getNext() != nullptr) { Tail = Tail->getNext(); }
        // Connect the last node of the new list to the original 'after' node
        Tail->setNext(after);

        // Delete the integer stored in the old head node
        delete oldHead->getValue();
        // Delete the old head node itself
        delete oldHead;
        // Update the list head to point to the new head
        this->number = Head;
    }

    // at the middle or at the end
    auto pos = this->number; // Start from the (possibly updated) head
    while (pos != nullptr && pos->getNext() != nullptr) { // Traverse as long as there is a next node

        if (pos->getNext() && pos->getNext()->getValue() &&
            sizeOfNumber(pos->getNext()->getValue()) == 1) { // If the next node holds a single-digit number
            pos = pos->getNext(); // Move forward without changing anything
        } else { // The next node holds a multi-digit number and needs to be split

            const auto before = pos; // Node before the multi-digit node
            const auto middle = pos->getNext(); // The multi-digit node itself
            const auto after = pos->getNext()->getNext(); // Node after the multi-digit node (can be nullptr)

            const auto new_list = buildlist(middle->getValue()); // Build a list of single-digit nodes from the middle value
            auto tail_for_new_list = new_list; // Start from the head of the new sublist

            while (tail_for_new_list->getNext() != nullptr) { // Move to the last node of the new sublist
                tail_for_new_list = tail_for_new_list->getNext(); // Advance the tail pointer
            }

            before->setNext(new_list); // Connect the 'before' node to the head of the new sublist
            tail_for_new_list->setNext(after); // Connect the end of the new sublist to the 'after' node

            delete middle->getValue(); // Delete the integer stored in the old multi-digit node
            delete middle; // Delete the old multi-digit node itself

            pos = tail_for_new_list; // Continue traversal from the last node of the newly inserted sublist
        }
    }

    // Recalculate tail pointer to keep it valid after all modifications
    this->tail = nullptr; // Reset the tail pointer
    auto scan = this->number; // Start scanning from the head
    while (scan != nullptr) { // Traverse the entire list
        this->tail = scan; // Update tail to the current node
        scan = scan->getNext(); // Move to the next node
    }
}
