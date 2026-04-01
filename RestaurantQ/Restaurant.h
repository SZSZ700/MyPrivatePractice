#ifndef UNTITLED1_RESTAURANT_H
#define UNTITLED1_RESTAURANT_H
#include <list>
#include <memory>
#include <queue>
#include <string>
#include "Client.h"
#include "Table.h"
using namespace std;

class Restaurant {
    // Queue that owns all waiting clients
    queue<unique_ptr<Client>> clients;
    // List that owns all restaurant tables
    list<unique_ptr<Table>> tables;

public:
    // Constructor that creates all restaurant tables
    Restaurant(int smallTables, int mediumTables, int largeTables);

    // Getter that returns a const reference to the client queue
    const queue<unique_ptr<Client>>& getClients() const;

    // Getter that returns a const reference to the table list
    const list<unique_ptr<Table>>& getTables() const;

    // Setter that replaces the client queue by taking ownership
    void setClients(queue<std::unique_ptr<Client>> clients);

    // Setter that replaces the table list by taking ownership
    void setTables(list<std::unique_ptr<Table>> tables);

    // Adds a new client to the waiting queue
    void addClient(unique_ptr<Client> client);

    // Adds a new table to the restaurant
    void addTable(unique_ptr<Table> table);

    // Returns a readable string that describes the restaurant
    string toString() const;

    // Finds a table number with enough free places for the given number of diners
    int findAvailableTable(int numOfDiners) const;

    // Seats the next suitable client from the queue
    bool seatNextClient();
};

#endif // UNTITLED1_RESTAURANT_H