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
    if (this == &other) { return *this; }

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

    // 🔁 Copy each linked list
    for (int i = 0; i < 100; i++) {
        // 🚫 if list at index [i] is not exist
        if (!other.lists[i]) { this->lists[i] = nullptr; }

        else {
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

// 🔍 return first student in array[k]
const Student* GradesFile::getStudent(const int k) const {
    if (k < 1 || k > 100) { return nullptr; }

    if (!this->lists[k]){ return nullptr; }

    // return the first student in the list at index k
    return this->lists[k]->getValue();
}

// ⁉️ if at index k there is no list
bool GradesFile::isEmpty(const int k) const {
    if (k < 0 || k >= 100 || !this->lists[k]) { return true; }
    return false;
}

// check if all the students in the collection at position k
// in the array match this position according to their studentId.
bool GradesFile::listIsGood (const int k) const {
    // If k is out of the array bounds or the collection at position k is empty,
    // the function returns "true".
    if (this->isEmpty(k)) { return true; }

    const Node<Student*>* pos = this->lists[k];

    while (pos) {
        if (const Student *currentStudent = pos->getValue()) {
            if (const int *id = currentStudent->getStudentId()) {
                if (const int calculationIndex = calculateIndex(id); calculationIndex != k) {
                    return false;
                }
            }
        }
        pos = pos->getNext();
    }

    return true;
}

// The function moves the first student from the collection located at position k in the array
// to become the last student in the collection located at position j in the array.
// ReSharper disable once CppMemberFunctionMayBeConst
void GradesFile::moveStudent(const int k, const int j) {
    if (k < 0 || k >= 100 || !this->lists[k] || j < 0 || j >= 100){ return; }

    // keep the first student from the collection located at position k
    // ReSharper disable once CppLocalVariableMayBeConst
    Student *first = this->lists[k]->getValue();
    this->lists[k] = this->lists[k]->getNext();

    // delete the first Node itself (delete the wrap not the *value in it)
    delete this->lists[k];

    // pointer for iteration on the list
    if (Node<Student*>* pos = this->lists[j]; pos != nullptr) {
        // iterate till we reach the end of the list at index j
        while (pos->getNext() != nullptr) {
            pos = pos->getNext();
        }

        // now pos points to the tail of the collection at index j
        // so now we can add new Node<Student*>* at the end
        // ReSharper disable once CppTemplateArgumentsCanBeDeduced
        pos->setNext(new Node<Student*>(first));
    }else {
        // ReSharper disable once CppTemplateArgumentsCanBeDeduced
        this->lists[j] = new Node<Student*>(first);
    }
}

// 🧾 Getter — returns pointer to the entire array of 100 linked lists
const Node<Student*>* const* GradesFile::getCollection() const {
    // 📤 Return the current pointer to the array (read-only)
    return this->lists;
}

// ✏️ Setter — replaces the entire array of lists with a new one
void GradesFile::setCollection(Node<Student*>** newCollection) {
    // 🚫 If null input → ignore
    if (!newCollection) return;

    // 💣 Delete all existing lists in the current array
    for (int i = 0; i < 100; i++) {
        if (this->lists[i]) {
            // 🧹 Delete every node + contained Student*
            const Node<Student*>* pos = this->lists[i];

            // iteration
            while (pos) {
                // additional temporary pointer for current Node that pos
                // points to it
                const Node<Student*>* temp = pos;

                pos = pos->getNext();    // move to the next Node
                delete temp->getValue(); // 🧽 Free the Student object
                delete temp;             // 🧽 Free the Node wrapper
            }
        }
    }

    // 💥 Free the old array itself
    delete[] this->lists;

    // 🔗 Assign the new array (take ownership)
    this->lists = newCollection;
}

// 🧩 During the data entry process, an error occurred,
// causing some students to be placed in incorrect positions according to their studentId.
// This external function receives the database
// (a pointer to an object of type GradesFile)
// and updates it so that in every cell of the array,
// there will be a collection of students whose studentId values
// correspond to the correct cell number in the array.

void updateGradesFile(GradesFile *gfile) {
    // ⚠️ Retrieve pointer to the current (old) collection of lists
    if (const Node<Student*>* const *array = gfile->getCollection(); array) {

        // ✨ Allocate new empty collection array on the heap (100 slots)
        // Each slot points to a linked list of Node<Student*> objects
        const auto newCollection = new Node<Student*>*[100];

        // 🧹 Initialize all 100 pointers to nullptr (empty linked lists)
        for (int i = 0; i < 100; i++) {
            newCollection[i] = nullptr;
        }

        // ⚙️ Iterate over every index in the old collection (0–99)
        for (int i = 0; i < 100; i++) {

            // 🚫 Skip if there is no linked list at index i
            if (!array[i]) continue;

            // 📍 Pointer to the head of the linked list at index i
            const Node<Student*>* pos = array[i];

            // 🔁 Iterate through all students in the linked list at this index
            while (pos != nullptr) {

                // ✅ Make sure the current node actually contains a Student*
                if (pos->getValue()) {

                    // 🎓 Extract pointer to the current student object
                    const Student *currentStudent = pos->getValue();

                    // 🧮 Variable to store the correct index according to studentId
                    int calculateIndex;

                    // 🪪 Extract the studentId (pointer to int)
                    if (const int *id = currentStudent->getStudentId(); id) {
                        // 🧠 Calculate the correct index in the array for this student
                        calculateIndex = gfile->calculateIndex(id);
                    }
                    // 🚫 If the studentId is null, skip this student safely
                    else {
                        pos = pos->getNext();
                        continue;
                    }

                    // ✨ Create a deep copy of the current student
                    // The new Student object is stored on the heap
                    // ReSharper disable once CppTemplateArgumentsCanBeDeduced
                    const auto toAdd = new Node<Student*>(new Student(*currentStudent));

                    // 📦 Insert the copied student node into the correct list
                    // at the index [calculateIndex] in the new collection

                    // 🧩 If no list exists yet at this index → this becomes the first node
                    if (!newCollection[calculateIndex]) {
                        newCollection[calculateIndex] = toAdd;
                    }
                    // 🔗 Otherwise, insert new node at the beginning of the list
                    else {
                        toAdd->setNext(newCollection[calculateIndex]);
                        newCollection[calculateIndex] = toAdd;
                    }
                } // .. end of if (pos->getValue())

                // ⏭️ Move to the next node in the linked list
                pos = pos->getNext();
            } // .. end of while 🔁
        } // .. end of for 🔁

        // ✅ Replace the old array with the newly organized one
        // This ensures that every student is now stored in the correct list
        gfile->setCollection(newCollection);
    } // .. end of if (array != nullptr)
}