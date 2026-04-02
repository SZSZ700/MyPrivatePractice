#ifndef UNTITLED1_RESTAURANT_H
#define UNTITLED1_RESTAURANT_H
#include <deque>
#include <list>
#include <memory>
#include <string>
#include "Client.h"
#include "Table.h"
using namespace std;

class Restaurant {
    // Deque that owns all waiting clients
    deque<unique_ptr<Client>> clients;

    // List that owns all restaurant tables
    list<unique_ptr<Table>> tables;

public:
    // Constructor that creates all restaurant tables
    Restaurant(int smallTables, int mediumTables, int largeTables);

    // Getter that returns a const reference to the clients deque
    const deque<unique_ptr<Client>>& getClients() const;

    // Getter that returns a const reference to the tables list
    const list<unique_ptr<Table>>& getTables() const;

    // Setter that replaces the clients deque with a deep copy
    void setClients(const deque<unique_ptr<Client>>& other);

    // Setter that replaces the tables list with a deep copy
    void setTables(const list<unique_ptr<Table>>& other);

    // Adds a new client to the waiting deque
    void addClient(unique_ptr<Client> client);

    // Adds a new table to the restaurant
    void addTable(unique_ptr<Table> table);

    // Returns a readable string that describes the restaurant
    string toString() const;

    // Finds a table number with enough free places for the given number of diners
    int findAvailableTable(int numOfDiners) const;

    // Seats the next suitable client from the waiting deque
    bool seatNextClient();
};

#endif // UNTITLED1_RESTAURANT_H