// Include the header that declares the Restaurant class
#include "Restaurant.h"
// Include the sstream header for std::stringstream used in toString
#include <assert.h>
#include <sstream>

// Helper method that deletes all table nodes and their table objects
void Restaurant::clearTables() {
    // Create a pointer that starts from the head of the tables list
    const Node<Table*>* curr = this->tables;

    // Traverse the linked list until there are no more nodes
    while (curr != nullptr) {
        // Store a pointer to the next node before deleting the current one
        const Node<Table*>* next = curr->getNext();
        // Get the table pointer stored in the current node
        const Table* tbl = curr->getValue();
        // Delete the table object if it is not null
        delete tbl;
        // Delete the current node object
        delete curr;
        // Move the current pointer forward to the next node
        curr = next;
    }
    // After deletion, set the head pointer to null
    this->tables = nullptr;
    // After deletion, set the tail pointer to null
    this->tablesTail = nullptr;
}

// Helper method that deletes all clients in the queue and the queue object itself
void Restaurant::clearClients() {
    // If the client queue pointer is null, there is nothing to clear
    if (!this->clients) {
        // Return immediately because there is no queue to delete
        return;
    }
    // While the queue still contains elements
    while (!this->clients->empty()) {
        // Get the client pointer at the front of the queue
        const Client* c = this->clients->front();
        // Delete the client object if it is not null
        delete c;
        // Remove the pointer from the queue
        this->clients->pop();
    }
    // Delete the queue object itself
    delete this->clients;
    // Set the queue pointer to null after deletion
    this->clients = nullptr;
}

// Helper method that deep copies the linked list of tables from another list
void Restaurant::copyTablesFrom(const Node<Table*>* otherHead) {
    // Initialize the head of the new list to null
    this->tables = nullptr;
    // Initialize the tail of the new list to null
    this->tablesTail = nullptr;
    // Create a pointer to traverse the other list starting from its head
    const Node<Table*>* pos = otherHead;

    // Traverse the other list as long as there are nodes
    while (pos != nullptr) {
        // Get the table pointer stored in the other list node
        const Table* otherTable = pos->getValue();
        // Initialize a pointer for the copied table
        Table* newTable = nullptr;
        // If the other table pointer is not null, perform a deep copy
        if (otherTable) {
            // Allocate a new table object using the copy constructor
            newTable = new Table(*otherTable);
        }
        // Create a new node that stores the copied table pointer
        const auto toAdd = new Node(newTable);

        // If this is the first node in the new list
        if (!this->tables) {
            // Set the head pointer of the new list
            this->tables = toAdd;
            // Set the tail pointer of the new list
            this->tablesTail = toAdd;
        } else {
            assert(this->tablesTail != nullptr);
            // Link the current tail node to the new node
            this->tablesTail->setNext(toAdd);
            // Move the tail pointer to the newly added node
            this->tablesTail = toAdd;
        }
        // Move to the next node in the other list
        pos = pos->getNext();
    }
}

// Helper method that deep copies the client queue from another queue
void Restaurant::copyClientsFrom(const queue<Client*>* otherQueue) {
    // If the other queue pointer is null, set this queue pointer to null
    if (!otherQueue) {
        // Assign null because there is no queue to copy
        this->clients = nullptr;
        // Exit the method early
        return;
    }

    // Allocate a new queue object on the heap
    this->clients = new queue<Client*>();
    // Create a local copy of the other queue to iterate over its elements
    queue<Client*> temp = *otherQueue;

    // Traverse the temporary queue until it becomes empty
    while (!temp.empty()) {
        // Get the client pointer at the front of the temporary queue
        const Client* c = temp.front();
        // Initialize a pointer for the copied client
        Client* newClient = nullptr;

        // If the client pointer is not null, perform a deep copy
        if (c) {
            // Allocate a new client object using the copy constructor
            newClient = new Client(*c);
        }

        // Push the copied client pointer into this restaurant's queue
        this->clients->push(newClient);
        // Remove the original pointer from the temporary queue
        temp.pop();
    }
}

// Constructor that builds the restaurant from numbers of small, medium and large tables
Restaurant::Restaurant(const int* smallTables, const int* mediumTables, const int* largeTables) {
    // Initialize the client queue pointer to a new empty queue
    this->clients = new queue<Client*>();
    // Initialize the head of the tables list to null
    this->tables = nullptr;
    // Initialize the tail of the tables list to null
    this->tablesTail = nullptr;

    // Determine how many small tables to create, defaulting to zero if pointer is null
    const int smallCount = smallTables ? *smallTables : 0;
    // Determine how many medium tables to create, defaulting to zero if pointer is null
    const int mediumCount = mediumTables ? *mediumTables : 0;
    // Determine how many large tables to create, defaulting to zero if pointer is null
    const int largeCount = largeTables ? *largeTables : 0;

    // Initialize the table number counter to start numbering from 1
    int tableNumberCounter = 1;

    // Loop to create all small tables
    for (int i = 0; i < smallCount; i++) {
        // Store the current table number in a local variable
        int num = tableNumberCounter;
        // Store the total places for a small table (2 places)
        int places = 2;
        // Store the number of free places, initially equal to total places
        int freePlaces = 2;
        // Allocate a new Table object using the pointer-style constructor
        const auto tbl = new Table(&num, &places, &freePlaces);
        // Allocate a new node that stores the table pointer
        const auto node = new Node(tbl);

        // If this is the first node in the tables list
        if (!this->tables) {
            // Set the head pointer of the tables list
            this->tables = node;
            // Set the tail pointer of the tables list
            this->tablesTail = node;
        } else {
            assert(this->tablesTail != nullptr);
            // Link the current tail node to the new node
            this->tablesTail->setNext(node);
            // Move the tail pointer to the newly added node
            this->tablesTail = node;
        }
        // Increase the table number counter for the next table
        tableNumberCounter++;
    }

    // Loop to create all medium tables
    for (int i = 0; i < mediumCount; i++) {
        // Store the current table number in a local variable
        int num = tableNumberCounter;
        // Store the total places for a medium table (4 places)
        int places = 4;
        // Store the number of free places, initially equal to total places
        int freePlaces = 4;
        // Allocate a new Table object using the pointer-style constructor
        const auto tbl = new Table(&num, &places, &freePlaces);
        // Allocate a new node that stores the table pointer
        const auto node = new Node(tbl);

        // If this is the first node in the tables list
        if (!this->tables) {
            // Set the head pointer of the tables list
            this->tables = node;
            // Set the tail pointer of the tables list
            this->tablesTail = node;
        } else {
            assert(this->tablesTail != nullptr);
            // Link the current tail node to the new node
            this->tablesTail->setNext(node);
            // Move the tail pointer to the newly added node
            this->tablesTail = node;
        }
        // Increase the table number counter for the next table
        tableNumberCounter++;
    }

    // Loop to create all large tables
    for (int i = 0; i < largeCount; i++) {
        // Store the current table number in a local variable
        int num = tableNumberCounter;
        // Store the total places for a large table (8 places)
        int places = 8;
        // Store the number of free places, initially equal to total places
        int freePlaces = 8;
        // Allocate a new Table object using the pointer-style constructor
        const auto tbl = new Table(&num, &places, &freePlaces);
        // Allocate a new node that stores the table pointer
        const auto node = new Node(tbl);

        // If this is the first node in the tables list
        if (!this->tables) {
            // Set the head pointer of the tables list
            this->tables = node;
            // Set the tail pointer of the tables list
            this->tablesTail = node;
        } else {
            assert(this->tablesTail != nullptr);
            // Link the current tail node to the new node
            this->tablesTail->setNext(node);
            // Move the tail pointer to the newly added node
            this->tablesTail = node;
        }
        // Increase the table number counter for the next table
        tableNumberCounter++;
    }
}

// Destructor that releases all dynamically allocated resources
Restaurant::~Restaurant() {
    // Clear and delete all table nodes and table objects
    this->clearTables();
    // Clear and delete all client objects and the queue object
    this->clearClients();
}

// Copy constructor that performs a deep copy from another Restaurant object
Restaurant::Restaurant(const Restaurant& other) {
    // Initialize the head of the tables list to null
    this->tables = nullptr;
    // Initialize the tail of the tables list to null
    this->tablesTail = nullptr;
    // Initialize the client queue pointer to null
    this->clients = nullptr;

    // Deep copy the table list from the other restaurant
    this->copyTablesFrom(other.tables);
    // Deep copy the client queue from the other restaurant
    this->copyClientsFrom(other.clients);
}

// Copy assignment operator that performs a deep copy from another Restaurant object
Restaurant& Restaurant::operator=(const Restaurant& other) {
    // Protect against self-assignment by checking if the objects are different
    if (this != &other) {
        // Clear and delete the current table list
        this->clearTables();
        // Clear and delete the current client queue
        this->clearClients();

        // Deep copy the table list from the other restaurant
        this->copyTablesFrom(other.tables);
        // Deep copy the client queue from the other restaurant
        this->copyClientsFrom(other.clients);
    }

    // Return a reference to this object to allow chained assignments
    return *this;
}

// Move constructor that steals the resources from another Restaurant object
Restaurant::Restaurant(Restaurant&& other) noexcept {
    // Steal the head pointer of the tables list from the other restaurant
    this->tables = other.tables;
    // Steal the tail pointer of the tables list from the other restaurant
    this->tablesTail = other.tablesTail;
    // Steal the client queue pointer from the other restaurant
    this->clients = other.clients;

    // Reset the other restaurant tables head pointer to null
    other.tables = nullptr;
    // Reset the other restaurant tables tail pointer to null
    other.tablesTail = nullptr;
    // Reset the other restaurant clients pointer to null
    other.clients = nullptr;
}

// Move assignment operator that steals the resources from another Restaurant object
Restaurant& Restaurant::operator=(Restaurant&& other) noexcept {
    // Protect against self-assignment by checking if the objects are different
    if (this != &other) {
        // Clear and delete the current table list
        this->clearTables();
        // Clear and delete the current client queue
        this->clearClients();

        // Steal the head pointer of the tables list from the other restaurant
        this->tables = other.tables;
        // Steal the tail pointer of the tables list from the other restaurant
        this->tablesTail = other.tablesTail;
        // Steal the client queue pointer from the other restaurant
        this->clients = other.clients;

        // Reset the other restaurant tables head pointer to null
        other.tables = nullptr;
        // Reset the other restaurant tables tail pointer to null
        other.tablesTail = nullptr;
        // Reset the other restaurant clients pointer to null
        other.clients = nullptr;
    }

    // Return a reference to this object to allow chained assignments
    return *this;
}

// Getter that returns a const pointer to the client queue
const queue<Client*>* Restaurant::getClients() const {
    // Return the pointer to the client queue
    return this->clients;
}

// Getter that returns a const pointer to the head of the table linked list
const Node<Table*>* Restaurant::getTables() const {
    // Return the pointer to the head of the tables list
    return this->tables;
}

// Setter that replaces the client queue with a deep copy of another client queue
void Restaurant::setClients(const queue<Client*>* otherQueue) {
    // Clear and delete the current client queue
    this->clearClients();
    // Deep copy the given queue into this restaurant
    this->copyClientsFrom(otherQueue);
}

// Setter that replaces the table linked list with a deep copy of another table list
void Restaurant::setTables(const Node<Table*>* otherHead) {
    // Clear and delete the current table list
    this->clearTables();
    // Deep copy the given table list into this restaurant
    this->copyTablesFrom(otherHead);
}

// Method that converts the restaurant data into a human-readable string
string Restaurant::toString() const {
    // Create a stringstream object to build the resulting string
    std::stringstream ss;
    // Append the class name and opening bracket to the stream
    ss << "Restaurant(";

    // Initialize a counter for the total number of tables
    int tableCount = 0;
    // Create a pointer to traverse the table list
    const Node<Table*>* pos = this->tables;

    // Traverse the table list and count all nodes
    while (pos != nullptr) {
        // Increase the table counter for each node
        tableCount++;
        // Move to the next node in the list
        pos = pos->getNext();
    }

    // Initialize a counter for the total number of clients
    int clientCount = 0;
    // If the client queue pointer is not null, count the number of clients
    if (this->clients) {
        // Create a local copy of the queue to iterate over it
        queue<Client*> temp = *this->clients;
        // Traverse until the temporary queue becomes empty
        while (!temp.empty()) {
            // Increase the client counter for each element
            clientCount++;
            // Remove the element from the temporary queue
            temp.pop();
        }
    }

    // Append the total number of tables to the string
    ss << "tables=" << tableCount;
    // Append a separator between fields
    ss << ", ";
    // Append the total number of clients to the string
    ss << "clientsInQueue=" << clientCount;
    // Append the closing bracket of the object representation
    ss << ")";

    // Return the string built inside the stringstream
    return ss.str();
}

// Method that finds a table number that has enough free places for the given number of diners
int Restaurant::findAvailableTable(const int numOfDiners) const {
    // If there are no tables at all, return -1 to indicate failure
    if (!this->tables) { return -1; }

    // Create a pointer to traverse the linked list of tables
    const Node<Table*>* pos = this->tables;

    // Traverse the table list as long as there are nodes
    while (pos != nullptr) {
        // Get the table pointer stored in the current node
        // ReSharper disable once CppTooWideScope
        const Table* tbl = pos->getValue();

        // If the table pointer is valid
        if (tbl) {
            // Get the pointer to the number of free places at this table
            const int* freePtr = tbl->getFreePlaces();
            // Get the pointer to the table number
            // ReSharper disable once CppTooWideScopeInitStatement
            const int* numPtr = tbl->getNum();

            // If both pointers are valid
            if (freePtr && numPtr) {
                // Read the number of free places from the table
                // ReSharper disable once CppTooWideScopeInitStatement
                const int free = *freePtr;

                // If the number of free places is enough for the given diners
                if (free >= numOfDiners) {
                    // Return the table number because this table can seat the group
                    return *numPtr;
                }
            }
        }

        // Move to the next node in the tables list
        pos = pos->getNext();
    }

    // If no suitable table was found, return -1 to indicate failure
    return -1;
}

// Method that seats the next suitable client from the queue at a table with enough free places
// ReSharper disable once CppMemberFunctionMayBeConst
bool Restaurant::seatNextClient() {
    // If the client queue pointer is null, there are no clients to seat
    if (!this->clients) {
        // Return false because no client was seated
        return false;
    }

    // If the queue is empty, there are no clients waiting
    if (this->clients->empty()) {
        // Return false because no client was seated
        return false;
    }

    // Store the current number of clients in the queue
    const std::size_t originalSize = this->clients->size();

    // Initialize a flag that indicates whether a client was successfully seated
    bool seated = false;

    // Loop over the queue at most once (one full rotation)
    for (std::size_t i = 0; i < originalSize; i++) {
        // Take the client pointer from the front of the queue
        Client* current = this->clients->front();
        // Remove the client pointer from the front of the queue
        this->clients->pop();

        // If the client pointer is null, skip it and continue to the next
        if (!current) {
            // Continue to the next client in the loop
            continue;
        }

        // Get the pointer to the number of diners for this client
        const int* dinersPtr = current->getDiners();
        // If the diners pointer is null, treat this client as having zero diners
        const int diners = dinersPtr ? *dinersPtr : 0;

        // Call the method that finds a suitable table number for this group size
        // ReSharper disable once CppTooWideScopeInitStatement
        const int tableNum = this->findAvailableTable(diners);

        // If a valid table number was found
        if (tableNum != -1) {
            // Create a pointer to traverse the tables list again to locate this table object
            const Node<Table*>* pos = this->tables;

            // Traverse the table list until the matching table is found
            while (pos != nullptr) {
                // Get the table pointer stored in the current node
                // ReSharper disable once CppTooWideScopeInitStatement
                Table* tbl = pos->getValue();

                // If the table pointer is valid and has a valid table number
                if (tbl && tbl->getNum()) {
                    // If the current table number matches the one returned by findAvailableTable
                    if (*(tbl->getNum()) == tableNum) {
                        // Get the pointer to the current number of free places at this table
                        // ReSharper disable once CppTooWideScope
                        const int* freePtr = tbl->getFreePlaces();

                        // If the free places pointer is valid
                        if (freePtr) {
                            // Compute the new number of free places after seating this client
                            int newFree = *freePtr - diners;
                            // Update the table object with the new number of free places
                            tbl->setFreePlaces(&newFree);
                        }

                        // Delete the client object because the client has been seated and leaves the queue
                        delete current;

                        // Set the flag to indicate that a client was seated successfully
                        seated = true;

                        // Break out of the inner loop because we have found and updated the table
                        break;
                    }
                }

                // Move to the next node in the tables list
                pos = pos->getNext();
            }

            // After seating the client, break out of the outer loop as well
            if (seated) {
                // Stop processing more clients because one client has been seated
                break;
            }
        } else {
            // If no suitable table was found for this client, push the client back to the end of the queue
            this->clients->push(current);
        }
    }

    // Return true if a client was seated, or false if no client could be seated
    return seated;
}
