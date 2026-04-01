#ifndef UCLIENT_H
#define UCLIENT_H
#include <memory>
#include <string>

class Client {
    // Smart pointer that owns the client name (heap allocated string)
    std::unique_ptr<std::string> name;
    // Smart pointer that owns the number of diners (heap allocated int)
    std::unique_ptr<int> diners;

public:
    // Constructor that takes ownership of name and diners using move semantics
    Client(std::unique_ptr<std::string> name, std::unique_ptr<int> diners);

    // Getter that returns a const reference to the client name (no copy, no pointer exposure)
    const std::string& getName() const;

    // Getter that returns a const reference to the number of diners
    const int& getDiners() const;

    // Setter that replaces the current name by taking ownership of a new unique_ptr
    void setName(std::unique_ptr<std::string> name);

    // Setter that replaces the number of diners by taking ownership
    void setDiners(std::unique_ptr<int> diners);
};
#endif