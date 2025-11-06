#include "GradesFile.h"

// 🏗️ Default constructor
GradesFile::GradesFile() {
    // 🧠 Allocate an array of 100 pointers on heap
    this->lists = new Node<Student*>*[100];

    // 🔁 Initialize each pointer to nullptr
    for (int i = 0; i < 100; i++) {
        this->lists[i] = nullptr;
    }
}

// 💣 Destructor
GradesFile::~GradesFile() {
    // 🔁 Loop through all 100 lists
    for (int i = 0; i < 100; i++) {
        // ⚙️ Get current head
        const Node<Student*>* pos = this->lists[i];

        // 🔁 Delete each node and its student
        while (pos != nullptr) {
            const Node<Student*>* temp = pos;      // 📦 Save current node
            pos = pos->getNext();           // ⏭️ Move forward

            delete temp->getValue();          // 💥 Delete Student object
            delete temp;                      // 💥 Delete node
        }

        // 🧹 Set slot to null
        this->lists[i] = nullptr;
    }

    // 💣 Finally, delete the array itself
    delete[] this->lists;
    this->lists = nullptr;
}

// 🧬 Copy constructor
GradesFile::GradesFile(const GradesFile& other) {
    // 🧠 Allocate new array of 100 Node<Student*>*
    this->lists = new Node<Student*>*[100];

    // 🔁 Copy each linked list
    for (int i = 0; i < 100; i++) {
        if (!other.lists[i]) {
            this->lists[i] = nullptr; // 🚫 Empty
        }else {
            // ReSharper disable once CppLocalVariableMayBeConst
            Node<Student*>* pos = other.lists[i];

            while (pos != nullptr) {
                // new Student Node to add
                // ReSharper disable once CppDeclaratorNeverUsed
                // ReSharper disable once CppTemplateArgumentsCanBeDeduced
                // ReSharper disable once CppUseAuto
                Node<Student*>* toAdd = new Node<Student*>(new Student(*pos->getValue()));

                if (this->lists[i] == nullptr) {
                    // ReSharper disable once CppTemplateArgumentsCanBeDeduced
                    this->lists[i] = toAdd;
                }else {
                    toAdd->setNext(this->lists[i]);
                    this->lists[i] = toAdd;
                }

                pos = pos->getNext();
            }
        }
    }
}

// ✍️ Copy assignment
GradesFile& GradesFile::operator=(const GradesFile& other) {
    // 🚫 Self-assignment check
    if (this == &other)
        return *this;

    // 💣 Delete current data
    for (int i = 0; i < 100; i++) {
        const Node<Student*>* pos = this->lists[i];

        while (pos) {
            const Node<Student*>* temp = pos;
            pos = pos->getNext();

            delete temp->getValue();
            delete temp;
        }
        this->lists[i] = nullptr;
    }

    // 🧠 Copy new data
    // 🔁 Copy each linked list
    for (int i = 0; i < 100; i++) {
        if (!other.lists[i]) {
            this->lists[i] = nullptr; // 🚫 Empty
        }else {
            // ReSharper disable once CppLocalVariableMayBeConst
            Node<Student*>* pos = other.lists[i];

            while (pos != nullptr) {
                // new Student Node to add
                // ReSharper disable once CppDeclaratorNeverUsed
                // ReSharper disable once CppTemplateArgumentsCanBeDeduced
                // ReSharper disable once CppUseAuto
                Node<Student*>* toAdd = new Node<Student*>(new Student(*pos->getValue()));

                if (this->lists[i] == nullptr) {
                    // ReSharper disable once CppTemplateArgumentsCanBeDeduced
                    this->lists[i] = toAdd;
                }else {
                    toAdd->setNext(this->lists[i]);
                    this->lists[i] = toAdd;
                }

                pos = pos->getNext();
            }
        }
    }

    // ✅ Return this
    return *this;
}

// 🚚 Move constructor
GradesFile::GradesFile(GradesFile&& other) noexcept {
    // 🚚 Take ownership of the heap array
    this->lists = other.lists;

    // 🧹 Nullify source
    other.lists = nullptr;
}

// 🚚 Move assignment
GradesFile& GradesFile::operator=(GradesFile&& other) noexcept {
    // 🚫 Self-move check
    if (this == &other)
        return *this;

    // 💣 Delete current data
    for (int i = 0; i < 100; i++) {
        const Node<Student*>* curr = this->lists[i];

        while (curr != nullptr) {
            const Node<Student*>* temp = curr;
            curr = curr->getNext();

            delete temp->getValue();
            delete temp;
        }
    }
    delete[] this->lists;

    // 🚚 Take ownership
    this->lists = other.lists;

    // 🧹 Nullify source
    other.lists = nullptr;

    return *this;
}

// ➕ Add a new student to the proper list
// ReSharper disable once CppMemberFunctionMayBeConst
void GradesFile::addStudent(Student* s) {
    // 🧮 Find index using middle digits
    const int index = calculateIndex(s->getStudentId());

    // 🧩 Create new node
    auto toAdd = new Node(s);

    // ⚙️ If list empty → head = node
    if (this->lists[index] == nullptr) {
        this->lists[index] = toAdd;
        return;
    }

    // 🔁 Otherwise append to the start
    toAdd->setNext(this->lists[index]);
    this->lists[index] = toAdd;
}

// 🔍 Find student by ID
const Student* GradesFile::findStudent(const int* id) const {
    // 🧮 Get index
    const int index = calculateIndex(id);

    // 🔁 Traverse list
    const Node<Student*>* pos = this->lists[index];

    while (pos != nullptr) {
        if (*pos->getValue()->getStudentId() == *id) {
            // return const pointer to the Student
            return pos->getValue();
        }

        pos = pos->getNext();
    }

    return nullptr; // ❌ Not found
}

// 🖨️ Print all data
void GradesFile::printAll() const {
    cout << "📘 Grades File (Heap-based lists)" << endl;
    for (int i = 0; i < 100; i++) {
        if (this->lists[i] == nullptr)
            continue;

        cout << "📂 List " << i << ":" << endl;
        const Node<Student*>* curr = this->lists[i];

        while (curr != nullptr) {
            curr->getValue()->print();

            curr = curr->getNext();
        }
        cout << endl;
    }
}

// 🧮 Calculate index (middle two digits)
// ReSharper disable once CppMemberFunctionMayBeStatic
int GradesFile::calculateIndex(const int* id) const {
    const int val = *id;
    const int mid = (val / 1000) % 100;
    return mid;
}
