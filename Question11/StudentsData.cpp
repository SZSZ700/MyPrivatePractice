#include "StudentsData.h"

// 🏗️ Default constructor
StudentData::StudentData() {
    // 🧠 Allocate empty chain (no students yet)
    this->chain = nullptr;
}

// 💥 Destructor
StudentData::~StudentData() {
    // 🧹 Delete all nodes in the linked list (deep cleanup)
    while (this->chain != nullptr) {
        // 👉 Keep pointer to next node
        Node<Student*>* temp = this->chain->getNext();

        // 🧽 Delete the student stored in the current node
        delete this->chain->getValue();

        // 💣 Delete the node itself
        delete this->chain;

        // ⏭️ Move to next node
        this->chain = temp;
    }
}

// 🧬 Copy constructor (deep copy)
StudentData::StudentData(const StudentData &other) {
    // 🧠 Initialize empty chain first
    this->chain = nullptr;

    // 🚫 If the other list is empty, stop
    if (!other.chain) return;

    // 📍 Pointer to traverse the other chain
    const Node<Student*>* pos = other.chain;

    // 📍 Pointer to tail in the new chain (for linking)
    Node<Student*>* tail = nullptr;

    // 🔁 Traverse and copy each Student node
    while (pos != nullptr) {
        // 🧬 Deep copy the Student object
        const auto newStudent = new Student(*pos->getValue());

        // 🧩 Create a new Node containing this Student
        const auto newNode = new Node(newStudent);

        // 📎 If first node → make it head
        if (!this->chain)
            this->chain = newNode;
        else
            // ReSharper disable once CppDFANullDereference
            tail->setNext(newNode);  // 🔗 Link to previous tail

        // ⏭️ Move tail and pos forward
        tail = newNode;
        pos = pos->getNext();
    }
}

// ✍️ Copy assignment (deep copy)
StudentData &StudentData::operator=(const StudentData &other) {
    // 🛡️ Guard against self-assignment
    if (this == &other) return *this;

    // 💣 Delete existing chain first
    while (this->chain != nullptr) {
        Node<Student*>* temp = this->chain->getNext();
        delete this->chain->getValue();
        delete this->chain;
        this->chain = temp;
    }

    // 🚫 If source chain is empty
    if (!other.chain) {
        this->chain = nullptr;
        return *this;
    }

    // 📍 Start copying nodes from source
    const Node<Student*>* pos = other.chain;
    Node<Student*>* tail = nullptr;

    // 🔁 Build new chain node-by-node
    while (pos != nullptr) {
        const auto newStudent = new Student(*pos->getValue());
        Node<Student*>* newNode = new Node(newStudent);

        if (!this->chain)
            this->chain = newNode;
        else
            // ReSharper disable once CppDFANullDereference
            tail->setNext(newNode);

        tail = newNode;
        pos = pos->getNext();
    }

    // ✅ Return self-reference
    return *this;
}

// 🚚 Move constructor
StudentData::StudentData(StudentData &&other) noexcept {
    // 🏃‍♂️ Take ownership of the linked list
    this->chain = other.chain;

    // 🧹 Nullify the source pointer
    other.chain = nullptr;
}

// 🚚 Move assignment
StudentData &StudentData::operator=(StudentData &&other) noexcept {
    // 🛡️ Guard self-move
    if (this == &other) return *this;

    // 💣 Delete current list
    while (this->chain != nullptr) {
        Node<Student*>* temp = this->chain->getNext();
        delete this->chain->getValue();
        delete this->chain;
        this->chain = temp;
    }

    // 📦 Transfer ownership of chain
    this->chain = other.chain;

    // 🧹 Nullify source
    other.chain = nullptr;

    // ✅ Return self
    return *this;
}

// 🧾 Getter — returns pointer to the head of the student chain
const Node<Student*> *StudentData::getChain() const {
    return this->chain;
}

// ✏️ Setter — replaces entire chain with a new one
// ReSharper disable once CppParameterNamesMismatch
void StudentData::setChain(Node<Student*> *other) {
    // 💣 Delete old list first to prevent memory leaks
    while (this->chain != nullptr) {
        Node<Student*>* temp = this->chain->getNext();
        delete this->chain->getValue();
        delete this->chain;
        this->chain = temp;
    }

    // 🔗 Assign the new chain pointer (take ownership)
    this->chain = other;
}

// 🧮 toString — returns formatted info of all students in the chain
std::string StudentData::toString() const {
    // 🧱 Use stringstream for efficient string building
    std::ostringstream out;

    // 🧩 If chain is empty, return clear message
    if (!this->chain) {
        out << "No students in database." << std::endl;
        return out.str();
    }

    // 🔁 Traverse all students and append their info
    const Node<Student*>* pos = this->chain;
    int index = 1; // For numbering students

    while (pos != nullptr) {
        // 🧑‍🎓 Get current student pointer

        // 📄 Append formatted info
        if (const Student* s = pos->getValue()) {
            out << "Student #" << index++ << ":" << std::endl;
            out << "  ID: " << *s->getId() << std::endl;
            out << "  Name: " << *s->getName() << std::endl;
            out << "  City: " << *s->getCity() << std::endl;
            out << "  Main Language: " << *s->getMainLanguage() << std::endl;
            out << "  Sub Language: " << *s->getSubLanguage() << std::endl;
            out << "  Has Car: " << ((*s->getHasCar()) ? "Yes" : "No") << std::endl;
            out << "--------------------------------------" << std::endl;
        }

        // ⏭️ Move to next node
        pos = pos->getNext();
    }

    // 🧾 Return the whole formatted string
    return out.str();
}

// erase student from the collection
void StudentData::eraseStudent(const string *id) {
    if (!this->chain) return;
    // remove from start
    // ReSharper disable once CppTooWideScopeInitStatement
    const string *ID = this->chain->getValue()->getId();

    if (*ID == *id) {
        // ReSharper disable once CppLocalVariableMayBeConst
        Node<Student*>* temp = this->chain;
        // now the list pointer, points to the next node
        this->chain = this->chain->getNext();

        // delete student
        delete temp->getValue();
        // delete the node itself
        delete temp;
        // stop, get out of the function
        return;
    }

    // remove from between
    Node<Student*>* pos = this->chain;
    Node<Student*> *prev = nullptr;

    while (pos->getNext() != nullptr) {
        if (*id == *pos->getNext()->getValue()->getId()) {
            // ReSharper disable once CppLocalVariableMayBeConst
            Node<Student*>* temp = pos->getNext();
            pos->setNext(pos->getNext()->getNext());

            // delete student
            delete temp->getValue();
            // delete the node itself
            delete temp;
            // stop, get out of the function
            return;
        }
        prev = pos;
        pos = pos->getNext();
    }

    // remove from end
    if (*pos->getValue()->getId() == *id) {
        // ReSharper disable once CppLocalVariableMayBeConst
        Node<Student*>* temp = pos;
        // ReSharper disable once CppDFANullDereference
        prev->setNext(pos->getNext());

        // delete student
        delete temp->getValue();
        // delete the node itself
        delete temp;
    }
}

// 🖨️ Print all students living in the given city and speaking the given language
void StudentData::print(const string* city, const string* lang) const {
    // 🚫 If chain is empty or parameters are null
    if (!this->chain || !city || !lang) return;

    // 🧮 Counter to check if someone matched
    int count = 0;

    // 🔁 Traverse the linked list
    const Node<Student*>* pos = this->chain;
    while (pos) {
        // 🎓 Current student
        if (const Student* s = pos->getValue(); s && s->getCity() && s->getMainLanguage()) {
            // ✅ Match city AND language
            if (*s->getCity() == *city && *s->getMainLanguage() == *lang) {
                cout << "🧍 " << *s->getName() << endl;
                count++;
            }
        }
        pos = pos->getNext();
    }

    // ⚠️ If no match found
    if (count == 0) {
        cout << "\nNo volunteers found in " << *city << " speaking " << *lang << "." << endl;
        cout << "🚗 Volunteers who speak " << *lang << " and have a car:" << endl;

        // 🔁 Traverse again for fallback
        pos = this->chain;
        while (pos) {
            if (const Student* s = pos->getValue(); s && s->getMainLanguage() && s->getHasCar()) {
                if (*s->getMainLanguage() == *lang && *s->getHasCar()) {
                    cout << "🧍 " << *s->getName() << endl;
                }
            }
            pos = pos->getNext();
        }
    }
}

// 🧮 Count how many students live in the given city
int StudentData::countByCity(const string* city) const {
    // 🚫 Validate input
    if (!this->chain || !city) return 0;

    // 🔢 Initialize counter
    int count = 0;

    // 🔁 Traverse the list
    const Node<Student*>* pos = this->chain;
    while (pos) {
        if (const Student* s = pos->getValue(); s && s->getCity()) {
            if (*s->getCity() == *city)
                count++;
        }
        pos = pos->getNext();
    }

    // 📤 Return total
    return count;
}
