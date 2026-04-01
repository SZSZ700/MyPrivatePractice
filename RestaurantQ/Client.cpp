#include "Client.h"
#include <utility>

// Constructor implementation
// Takes ownership of the incoming unique_ptr objects and ensures valid defaults
Client::Client(std::unique_ptr<std::string> name, std::unique_ptr<int> diners) {
    // Transfer ownership from parameters to class members
    this->name = std::move(name);
    this->diners = std::move(diners);
    // Ensure name is never null to avoid dereferencing issues
    if (!this->name) { this->name = std::make_unique<std::string>("Unknown"); }
    // Ensure diners is never null
    if (!this->diners) { this->diners = std::make_unique<int>(0); }
}

// Returns a const reference to the string stored in the unique_ptr
// Dereferencing is required because unique_ptr wraps the actual object
const std::string& Client::getName() const { return *name; }

// Returns a const reference to the integer stored in the unique_ptr
const int& Client::getDiners() const { return *diners; }

// Replaces the current name by taking ownership of a new unique_ptr
// ReSharper disable once CppPassValueParameterByConstReference
// ReSharper disable once CppParameterNamesMismatch
void Client::setName(std::unique_ptr<std::string> namee) {
    // If null is provided, assign a safe default value
    if (!namee) {
        this->name = std::make_unique<std::string>("Unknown");
        return;
    }
    // Transfer ownership
    this->name = std::move(namee);
}

// Replaces diners with a new value using ownership transfer
// ReSharper disable once CppPassValueParameterByConstReference
// ReSharper disable once CppParameterNamesMismatch
void Client::setDiners(std::unique_ptr<int> dinerss) {
    // Handle null safely
    if (!dinerss) {
        this->diners = std::make_unique<int>(0);
        return;
    }
    this->diners = std::move(dinerss);
}