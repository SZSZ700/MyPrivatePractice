#include "Restaurant.h"
#include <sstream>
#include <utility>

// Constructor that creates all tables based on the given counts
Restaurant::Restaurant(int smallTables, int mediumTables, int largeTables) {
    // Prevent negative table counts
    if (smallTables < 0) { smallTables = 0; }
    if (mediumTables < 0) { mediumTables = 0; }
    if (largeTables < 0) { largeTables = 0; }

    // Start numbering tables from 1
    int tableNumberCounter = 1;

    // Create all small tables (2 places)
    for (int i = 0; i < smallTables; i++) {
        tables.push_back(std::make_unique<Table>(
            std::make_unique<int>(tableNumberCounter),
            std::make_unique<int>(2),
            std::make_unique<int>(2)
        ));
        tableNumberCounter++;
    }

    // Create all medium tables (4 places)
    for (int i = 0; i < mediumTables; i++) {
        tables.push_back(std::make_unique<Table>(
            std::make_unique<int>(tableNumberCounter),
            std::make_unique<int>(4),
            std::make_unique<int>(4)
        ));
        tableNumberCounter++;
    }

    // Create all large tables (8 places)
    for (int i = 0; i < largeTables; i++) {
        tables.push_back(std::make_unique<Table>(
            std::make_unique<int>(tableNumberCounter),
            std::make_unique<int>(8),
            std::make_unique<int>(8)
        ));
        tableNumberCounter++;
    }
}

// Returns the client queue as a const reference
const std::queue<std::unique_ptr<Client>>& Restaurant::getClients() const {
    return clients;
}

// Returns the table list as a const reference
const std::list<std::unique_ptr<Table>>& Restaurant::getTables() const {
    return tables;
}

// Replaces the entire client queue (move ownership)
// ReSharper disable once CppParameterNamesMismatch
void Restaurant::setClients(std::queue<std::unique_ptr<Client>> clientss) {
    // Move the entire queue into this object
    this->clients = std::move(clientss);
}

// Replaces the entire table list (move ownership)
// ReSharper disable once CppParameterNamesMismatch
void Restaurant::setTables(std::list<std::unique_ptr<Table>> tabless) {
    // Move the entire list into this object
    this->tables = std::move(tabless);
}
// Adds a new client to the queue
void Restaurant::addClient(std::unique_ptr<Client> client) {
    // Ignore null clients
    if (!client) { return; }
    this->clients.push(std::move(client));
}

// Adds a new table to the restaurant
void Restaurant::addTable(std::unique_ptr<Table> table) {
    // Ignore null tables
    if (!table) { return; }
    this->tables.push_back(std::move(table));
}

// Builds a readable string that describes the restaurant
std::string Restaurant::toString() const {
    std::stringstream ss;
    ss << "Restaurant(";
    ss << "tables=" << tables.size();
    ss << ", ";
    ss << "clientsInQueue=" << clients.size();
    ss << ")";
    return ss.str();
}

// Finds the first table that has enough free places
int Restaurant::findAvailableTable(const int numOfDiners) const {
    // Traverse all tables
    for (const auto &tbl : this->tables) {
        // Skip null table pointers just in case
        if (!tbl) { continue; }

        // If this table has enough free places, return its number
        if (tbl->getFreePlaces() >= numOfDiners) {
            return tbl->getNum();
        }
    }

    // No suitable table was found
    return -1;
}

// Seats the next client that can fit in one of the available tables
bool Restaurant::seatNextClient() {
    // If there are no waiting clients, nothing can be seated
    if (this->clients.empty()) { return false; }

    // Store the original queue size so we do at most one full rotation
    const auto originalSize = clients.size();

    // Try each client once
    for (auto i = 0; i < originalSize; i++) {
        // Take ownership of the client at the front of the queue
        auto current = std::move(clients.front());
        this->clients.pop();

        // Skip null clients
        if (!current) { continue; }
        // Read the number of diners from the client object
        const auto diners = current->getDiners();
        // Find a suitable table for this client
        const auto tableNum = findAvailableTable(diners);

        // If no table was found, move the client to the end of the queue
        if (tableNum == -1) {
            this->clients.push(std::move(current));
            continue;
        }

        // Search for the matching table and update its free places
        for (const auto& tbl : this->tables) {
            if (!tbl) { continue; }

            if (tbl->getNum() == tableNum) {
                tbl->setFreePlaces(
                    std::make_unique<int>(tbl->getFreePlaces() - diners)
                );
                // Client was seated successfully, so do not return it to the queue
                return true;
            }
        }

        // Safety fallback:
        // if a table number was found but not located, return the client to the queue
        clients.push(std::move(current));
    }

    // No client could be seated
    return false;
}