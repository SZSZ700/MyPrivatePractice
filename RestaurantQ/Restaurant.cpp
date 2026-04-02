#include "Restaurant.h"
#include <sstream>
#include <utility>

// Constructor that creates all tables based on the given counts
Restaurant::Restaurant(int smallTables, int mediumTables, int largeTables) {
    // Prevent negative counts
    if (smallTables < 0) { smallTables = 0; }
    if (mediumTables < 0) { mediumTables = 0; }
    if (largeTables < 0) { largeTables = 0; }

    // Start numbering tables from 1
    auto tableNumberCounter = 1;

    // Create small tables (2 places)
    for (auto i = 0; i < smallTables; i++) {
        tables.push_back(make_unique<Table>(
            make_unique<int>(tableNumberCounter),
            make_unique<int>(2),
            make_unique<int>(2)
        ));
        tableNumberCounter++;
    }

    // Create medium tables (4 places)
    for (auto i = 0; i < mediumTables; i++) {
        tables.push_back(make_unique<Table>(
            make_unique<int>(tableNumberCounter),
            make_unique<int>(4),
            make_unique<int>(4)
        ));
        tableNumberCounter++;
    }

    // Create large tables (8 places)
    for (int i = 0; i < largeTables; i++) {
        tables.push_back(make_unique<Table>(
            make_unique<int>(tableNumberCounter),
            make_unique<int>(8),
            make_unique<int>(8)
        ));
        tableNumberCounter++;
    }
}

// Returns the clients deque as a const reference
const deque<unique_ptr<Client>>& Restaurant::getClients() const {
    return this->clients;
}

// Returns the tables list as a const reference
const list<unique_ptr<Table>>& Restaurant::getTables() const {
    return this->tables;
}

// Replaces the clients deque with a deep copy
void Restaurant::setClients(const deque<unique_ptr<Client>>& other) {
    // Remove current clients
    clients.clear();

    // Deep copy every client
    for (auto i = 0; i < other.size(); i++) {
        if (!other[i]) {
            clients.push_back(nullptr);
        } else {
            clients.push_back(make_unique<Client>(
                make_unique<string>(other[i]->getName()),
                make_unique<int>(other[i]->getDiners())
            ));
        }
    }
}

// Replaces the tables list with a deep copy
void Restaurant::setTables(const std::list<std::unique_ptr<Table>>& other) {
    // Remove current tables
    tables.clear();

    // Deep copy every table
    for (const auto& tbl : other) {
        if (!tbl) {
            tables.push_back(nullptr);
        } else {
            tables.push_back(make_unique<Table>(
                make_unique<int>(tbl->getNum()),
                make_unique<int>(tbl->getPlaces()),
                make_unique<int>(tbl->getFreePlaces())
            ));
        }
    }
}

// Adds a new client to the end of the waiting deque
void Restaurant::addClient(unique_ptr<Client> client) {
    if (!client) { return; }
    clients.push_back(std::move(client));
}

// Adds a new table to the restaurant
void Restaurant::addTable(unique_ptr<Table> table) {
    if (!table) { return; }
    tables.push_back(std::move(table));
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
    for (const auto& tbl : tables) {
        if (!tbl) { continue; }
        if (tbl->getFreePlaces() >= numOfDiners) { return tbl->getNum(); }
    }
    return -1;
}

// Seats the next client that can fit in one of the available tables
bool Restaurant::seatNextClient() {
    // If there are no waiting clients, nothing can be seated
    if (clients.empty()) { return false; }

    // Store the original number of clients so each client is checked once
    const auto originalSize = clients.size();

    // Try each client once
    for (auto i = 0; i < originalSize; i++) {
        // Take ownership of the client at the front
        auto current = std::move(clients.front());
        clients.pop_front();
        // Skip null clients
        if (!current) { continue; }
        // Read diners count
        const auto diners = current->getDiners();
        // Find a suitable table
        const auto tableNum = findAvailableTable(diners);

        // If no table was found, return the client to the end
        if (tableNum == -1) {
            clients.push_back(std::move(current));
            continue;
        }

        // Update the matching table
        for (const auto& tbl : tables) {
            if (!tbl) { continue; }

            if (tbl->getNum() == tableNum) {
                tbl->setFreePlaces(
                    make_unique<int>(tbl->getFreePlaces() - diners)
                );

                // Client is seated successfully, so do not return them to the deque
                return true;
            }
        }

        // Safety fallback: if table number was not found, return client to the end
        clients.push_back(std::move(current));
    }

    return false;
}