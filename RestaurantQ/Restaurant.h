#ifndef UNTITLED1_RESTAURANT_H
#define UNTITLED1_RESTAURANT_H
// Include the queue header for std::queue
#include <queue>
// Include the string header for std::string
#include <string>
// Include the Node template header for linked list of tables
#include "..//Node/Node.h"
// Include the Client class header
#include "Client.h"
// Include the Table class header
#include "Table.h"

// Use std::string by name only to avoid bringing the whole std namespace
using std::string;
// Use std::queue by name only to avoid bringing the whole std namespace
using std::queue;

// Class that represents a restaurant with a client queue and a linked list of tables
class Restaurant {
private:
    // Pointer to a queue of client pointers stored on the heap
    queue<Client*>* clients;
    // Pointer to the head node of the linked list of table pointers
    Node<Table*>* tables;
    // Pointer to the tail node of the linked list of table pointers
    Node<Table*>* tablesTail;

    // Helper method that deletes all table nodes and their table objects
    void clearTables();
    // Helper method that deletes all clients in the queue and the queue object itself
    void clearClients();
    // Helper method that deep copies the linked list of tables from another list
    void copyTablesFrom(const Node<Table*>* otherHead);
    // Helper method that deep copies the client queue from another queue
    void copyClientsFrom(const queue<Client*>* otherQueue);

public:
    // Constructor that builds the restaurant from numbers of small, medium and large tables
    Restaurant(const int* smallTables, const int* mediumTables, const int* largeTables);
    // Destructor that releases all dynamically allocated resources
    ~Restaurant();
    // Copy constructor that performs a deep copy from another Restaurant object
    Restaurant(const Restaurant& other);
    // Copy assignment operator that performs a deep copy from another Restaurant object
    Restaurant& operator=(const Restaurant& other);
    // Move constructor that steals the resources from another Restaurant object
    Restaurant(Restaurant&& other) noexcept;
    // Move assignment operator that steals the resources from another Restaurant object
    Restaurant& operator=(Restaurant&& other) noexcept;

    // Getter that returns a const pointer to the client queue
    const queue<Client*>* getClients() const;
    // Getter that returns a const pointer to the head of the table linked list
    const Node<Table*>* getTables() const;

    // Setter that replaces the client queue with a deep copy of another client queue
    void setClients(const queue<Client*>* otherQueue);
    // Setter that replaces the table linked list with a deep copy of another table list
    void setTables(const Node<Table*>* otherHead);

    // Method that converts the restaurant data into a human-readable string
    string toString() const;
};
#endif //UNTITLED1_RESTAURANT_H