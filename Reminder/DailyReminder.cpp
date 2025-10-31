#include "DailyReminder.h"
#include <iostream>

// =========================
// 🏗️ Default constructor
// =========================
DailyReminder::DailyReminder() {
    this->chain = nullptr;  // 🧱 Initialize list head to null
    this->count = new int(0); // 🔢 Start with count = 0
}

// =========================
// 🧹 Destructor
// =========================
DailyReminder::~DailyReminder() {
    // 🚮 Delete all Reminder objects in the list
    // ReSharper disable once CppLocalVariableMayBeConst
    Node<Reminder*>* pos = this->chain;

    while (pos) {
        Node<Reminder*>* next = pos->getNext();  // ➡️ Move to next node
        delete pos->getValue();                  // 🧹 Delete Reminder*
        delete pos;                              // 🧱 Delete Node itself
        pos = next;                              // 🔁 Continue
    }

    delete this->count; // 🧹 Free the count pointer
    this->chain = nullptr; // 🚫 Nullify pointer
}

// =========================
// 📋 Copy constructor
// =========================
DailyReminder::DailyReminder(const DailyReminder& other) {
    // 🔢 Copy the count deeply
    this->count = other.count ? new int(*other.count) : nullptr;

    // 🔗 Deep copy the linked list
    if (other.chain == nullptr) {
        this->chain = nullptr; // 🚫 Empty list
    } else {
        // 🪄 Copy first node
        this->chain = new Node(new Reminder(*other.chain->getValue()));

        // ReSharper disable once CppLocalVariableMayBeConst
        Node<Reminder*>* src = other.chain->getNext();
        Node<Reminder*>* dst = this->chain;

        // 🔁 Copy the rest of the nodes
        while (src) {
            // ReSharper disable once CppDFANullDereference
            dst->setNext(new Node(new Reminder(*src->getValue())));
            dst = dst->getNext();
            src = src->getNext();
        }
    }
}

// =========================
// 🧳 Move constructor
// =========================
DailyReminder::DailyReminder(DailyReminder&& other) noexcept {
    // 🧳 Transfer ownership of pointers
    this->chain = other.chain;
    this->count = other.count;

    // 🚫 Nullify the source object
    other.chain = nullptr;
    other.count = nullptr;
}

// =========================
// 📋 Copy assignment operator
// =========================
DailyReminder& DailyReminder::operator=(const DailyReminder& other) {
    if (this != &other) {
        // 🧹 Delete current list
        // ReSharper disable once CppLocalVariableMayBeConst
        Node<Reminder*>* pos = this->chain;

        while (pos) {
            Node<Reminder*>* next = pos->getNext();
            delete pos->getValue();
            delete pos;
            pos = next;
        }
        delete this->count;

        // 🔢 Copy count deeply
        this->count = other.count ? new int(*other.count) : nullptr;

        // 🔗 Deep copy linked list
        if (other.chain == nullptr) {
            this->chain = nullptr;
        } else {
            this->chain = new Node(new Reminder(*other.chain->getValue()));
            // ReSharper disable once CppLocalVariableMayBeConst
            Node<Reminder*>* src = other.chain->getNext();
            Node<Reminder*>* dst = this->chain;

            while (src) {
                // ReSharper disable once CppDFANullDereference
                dst->setNext(new Node(new Reminder(*src->getValue())));
                dst = dst->getNext();
                src = src->getNext();
            }
        }
    }
    return *this;
}

// =========================
// 🧳 Move assignment operator
// =========================
DailyReminder& DailyReminder::operator=(DailyReminder&& other) noexcept {
    if (this != &other) {
        // 🧹 Clean existing data
        // ReSharper disable once CppLocalVariableMayBeConst
        Node<Reminder*>* pos = this->chain;

        while (pos) {
            Node<Reminder*>* next = pos->getNext();
            delete pos->getValue();
            delete pos;
            pos = next;
        }
        delete this->count;

        // 🧳 Transfer ownership
        this->chain = other.chain;
        this->count = other.count;

        // 🚫 Nullify other
        other.chain = nullptr;
        other.count = nullptr;
    }
    return *this;
}

// ===========
// ⚙️ Getter
// ===========
Node<Reminder *> *DailyReminder::getChain() const { return this->chain; }

// 🔢 Return count pointer (read-only)
const int* DailyReminder::getCount() const { return this->count; }

// =========================
// ⚙️ Setters
// =========================
void DailyReminder::setChain(Node<Reminder*>* ch) {
    // 🚮 Delete old list first
    // ReSharper disable once CppLocalVariableMayBeConst
    Node<Reminder*>* pos = this->chain; // pointer for the list

    // delete old data
    // iteration on list
    while (pos) {
        Node<Reminder*>* next = pos->getNext(); // pointer for the next node
        delete pos->getValue(); // delete current Node value (invoke destructor of Reminder)
        delete pos; // delete current Node wrapper (invoke destructor of Node)
        pos = next; // advance to the next node
    }

    // 🔗 Assign new list (take ownership)
    this->chain = ch;
}

void DailyReminder::setCount(const int* c) {
    delete this->count;                 // 🧹 Free old count
    this->count = c ? new int(*c) : nullptr; // 🧱 Deep copy new count
}

// =========================
// ➕ Add new reminder to the list
// =========================
void DailyReminder::addReminder(Reminder* r) {
    // 🚨 Validate input: skip null pointer
    if (!r) return;

    auto* toAdd = new Node(r); // 🧱 Create a new node holding the given Reminder pointer

    if (!this->chain) { this->chain = toAdd; } // 🧩 If list is empty — make this node the head

    // 🔗 Otherwise, traverse to the end and attach it
    else {
        Node<Reminder*>* pos = this->chain;   // 🧭 Start from head

        while (pos->getNext()) { pos = pos->getNext(); } // 🔁 Move until last node

        pos->setNext(toAdd); // 🔗 Link new node at the end
    }

    if (this->count) (*this->count)++; // 🔢 Increment reminder count (if not null)
}

// =========================
// 🧾 Print all reminders
// =========================
void DailyReminder::print() const {
    cout << "Daily Reminder List (" << (this->count ? *this->count : 0) << " total):" << endl;

    // ReSharper disable once CppLocalVariableMayBeConst
    Node<Reminder*>* pos = this->chain; // 🔁 Start from head
    int index = 1;                      // 🔢 Counter

    while (pos) {
        cout << "Reminder #" << index++ << ":" << endl;
        if (pos->getValue()) {
            pos->getValue()->print();   // 🖨️ Print each reminder
        } else {
            cout << "Null Reminder" << endl;
        }
        pos = pos->getNext();           // ➡️ Move to next node
    }

    cout << "End of list.\n" << endl;
}

// =========================
// ❌ Remove a reminder by customer name
// =========================
void DailyReminder::removeReminderByName(const string* name) {
    // 🚨 Check for null input or empty list
    if (!name || !this->chain) return;

    // 🧭 Pointers to traverse the list
    Node<Reminder*>* current = this->chain;  // 👉 Start from head
    Node<Reminder*>* previous = nullptr;     // 👈 Keep track of the previous node

    // 🔁 Traverse all reminders
    while (current) {
        // 🧩 Check if reminder matches the given name
        // ReSharper disable once CppLocalVariableMayBeConst
        if (Reminder* r = current->getValue(); r && *r->getCust() == *name) {
            // ⚙️ Found the reminder to remove

            // 🪓 If it's the head node
            if (previous == nullptr) {
                this->chain = current->getNext(); // 🔗 Update head pointer
            } else {
                previous->setNext(current->getNext()); // 🔗 Skip the current node
            }

            delete r;        // 🧹 Free the Reminder object
            delete current;  // 🧱 Delete the Node itself

            // 🔢 Decrease the count (if not null and > 0)
            if (this->count && *this->count > 0) (*this->count)--;

            return; // ✅ Stop after removing the first match
        }

        // ⏩ Move both pointers forward
        previous = current;
        current = current->getNext();
    }

    // ⚠️ If no reminder found, do nothing
}

// 🎯 Collect all reminders from a specific institution
const Node<Reminder*>* DailyReminder::getCustomersFromInstitution(const string* inst) const {
    // 🚨 Validate inputs
    if (!inst || !this->chain) return nullptr;

    Node<Reminder*>* newey = nullptr; // 🧱 New head for filtered list
    Node<Reminder*>* tail = nullptr;  // 🪝 Tail pointer for building chain

    // ReSharper disable once CppLocalVariableMayBeConst
    Node<Reminder*>* current = this->chain; // 📍 Iterator over the original list

    while (current) {
        Reminder* tempReminder = current->getValue(); // 🎯 Current reminder

        // 🔍 Compare institution names by CONTENT, not address
        if (const string* tempInstitution = tempReminder->getInst();
            tempInstitution && *tempInstitution == *inst) {

            // 🧩 Append a copy of the pointer to the new list
            if (!newey) {
                // ReSharper disable once CppTemplateArgumentsCanBeDeduced
                newey = new Node<Reminder*>(tempReminder);
                tail = newey;
            } else {
                // ReSharper disable once CppDFANullDereference
                // ReSharper disable once CppTemplateArgumentsCanBeDeduced
                tail->setNext(new Node<Reminder*>(tempReminder));
                tail = tail->getNext();
            }
            }

        current = current->getNext(); // ⏩ Move to next reminder
    }

    // ✅ Return the head of the new filtered list
    return newey;
}

