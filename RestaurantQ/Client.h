#ifndef UNTITLED1_CLIENT_H
#define UNTITLED1_CLIENT_H
// Include the string header for std::string
#include <string>

// Use the std namespace only for std::string to avoid polluting the global namespace
using std::string;

// Class that represents a restaurant client
class Client {
private:
    // Pointer to the client name stored on the heap
    string* name;
    // Pointer to the number of diners stored on the heap
    int* diners;

public:
    // Constructor that creates a client from name and number of diners
    explicit Client(const string* name, const int* diners);
    // Destructor that releases all dynamically allocated resources
    ~Client();
    // Copy constructor that performs a deep copy from another Client object
    Client(const Client& other);
    // Copy assignment operator that performs a deep copy from another Client object
    Client& operator=(const Client& other);
    // Move constructor that steals the resources from another Client object
    Client(Client&& other) noexcept;
    // Move assignment operator that steals the resources from another Client object
    Client& operator=(Client&& other) noexcept;

    // Getter that returns a const pointer to the client name
    const string* getName() const;
    // Getter that returns a const pointer to the number of diners
    const int* getDiners() const;

    // Setter that replaces the client name with a deep copy of the given name
    void setName(const string* name);
    // Setter that replaces the number of diners with a deep copy of the given value
    void setDiners(const int* diners);

    // Method that converts the client data into a human-readable string
    string toString() const;
};
#endif //UNTITLED1_CLIENT_H